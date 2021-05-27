/*

  APIs are described here
  https://spec.oneapi.com/level-zero/latest/index.html


  (setq c-basic-offset 4)
*/

#include <level_zero/ze_api.h>
#include <level_zero/zes_api.h>

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <unistd.h>
#include <sys/time.h>
#include <string.h>

static double gettime(void)
{
    struct timeval tv;
    gettimeofday(&tv, 0);
    return (double)tv.tv_sec + (double)tv.tv_usec/1000.0/1000.0;
}


#define _ZE_ERROR_MSG(NAME,RES) {printf("%s() failed at %d(%s): res=%x\n",(NAME),__LINE__,__FILE__,(RES));}
#define _ERROR_MSG(MSG) {perror((MSG)); printf("errno=%d at %d(%s)",errno,__LINE__,__FILE__);}

#define Z printf("%d\n",__LINE__)

#if 0
struct l0driverdata_struct {
	ze_driver_properties_t prop;
	uint32_t npwrdoms;
	zes_pwr_handle_t *pwrh;
	// add more handers
};

struct l0drivers_struct {
	uint32_t  ndrivers;
	ze_driver_handle_t *drvh;
	struct l0driverdata *drvdata;
};

static struct l0drivers_struct l0drivers;
#endif


static void listdevices(void)
{
    ze_result_t res;

    res = zeInit(ZE_INIT_FLAG_GPU_ONLY);
    if(res != ZE_RESULT_SUCCESS) {
	_ZE_ERROR_MSG("zeInit", res);
	return;
    }

    uint32_t  ndrivers = 0;
    res = zeDriverGet(&ndrivers, NULL);
    if(res != ZE_RESULT_SUCCESS) {
	_ZE_ERROR_MSG("zeDriverGet", res);
	return;
    }
    printf("Number of drivers: %d\n", ndrivers);

    ze_driver_handle_t *drvh;
    drvh = malloc(sizeof(ze_driver_handle_t) * ndrivers);
    if(!drvh)  {
	_ERROR_MSG("malloc");
	return;
    }
    res = zeDriverGet(&ndrivers, drvh);
    if(res != ZE_RESULT_SUCCESS) {
	_ZE_ERROR_MSG("zeDriverGet", res);
	return;
    }

    // iterate over drivers
    for (int i = 0; i < ndrivers; i++) {
	ze_driver_properties_t prop;
	res = zeDriverGetProperties(drvh[i], &prop);
	if (res != ZE_RESULT_SUCCESS) {
	    _ZE_ERROR_MSG("zeDriverGetProperties", res);
	    return;
	}
	printf("driver%d: version=%u\n",
	    i, prop.driverVersion);

	// devices
	uint32_t ndevs = 0;
	res = zeDeviceGet(drvh[i], &ndevs, NULL);
	if (res != ZE_RESULT_SUCCESS) {
	    _ZE_ERROR_MSG("zeDeviceGet", res);
	    return;
	}
	printf("ndevs=%d\n",      ndevs);

	ze_device_handle_t *devh;
	devh = malloc(sizeof(ze_device_handle_t) * ndevs);
	if(!devh)  {
	    _ERROR_MSG("malloc");
	    return;
	}

	res = zeDeviceGet(drvh[i], &ndevs, devh);
	if (res != ZE_RESULT_SUCCESS) {
	    _ZE_ERROR_MSG("zeDeviceGet", res);
	    return;
	}

	for (int di = 0; di < ndevs; di++) {
	    ze_device_properties_t devprop;
	    res = zeDeviceGetProperties(devh[di], &devprop);
	    if (res != ZE_RESULT_SUCCESS) {
		_ZE_ERROR_MSG("zeDeviceGetProperties", res);
		return;
	    }
	    printf("\tdev%d: ", di);
	    if (devprop.type == ZE_DEVICE_TYPE_GPU) {
		printf("TYPE=GPU ");
	    } else {
		printf("TYPE=!GPU");
	    }
	    printf("\n");
	    zes_device_handle_t sysmanh = (zes_device_handle_t)devh[di];

	    uint32_t npwrdoms = 0;
	    res = zesDeviceEnumPowerDomains(sysmanh, &npwrdoms ,NULL);
	    if (res != ZE_RESULT_SUCCESS) {
		_ZE_ERROR_MSG("zesDeviceEnumPowerDomains", res);
		return;
	    }
	    printf("\nPower: %u domain(s)\n", npwrdoms);

	    zes_pwr_handle_t *pwrh;
	    if (npwrdoms > 0) {
		pwrh = malloc( sizeof(zes_pwr_handle_t) * npwrdoms);
		res = zesDeviceEnumPowerDomains(sysmanh, &npwrdoms, pwrh);
		if (res != ZE_RESULT_SUCCESS) {
		    _ZE_ERROR_MSG("zesDeviceEnumPowerDomains", res);
		    return;
		}

		for (int di =0; di < npwrdoms; di++) {
		    double startsec = gettime();
		    double pt = 0.0;
		    double pe = -1;
		    ze_result_t res;

		    printf("  pwrdom=%d\n", di);

		    zes_power_properties_t pprop;

		    if (zesPowerGetProperties(pwrh[di], &pprop)==ZE_RESULT_SUCCESS) {
			printf("    onsubdev=%d subdevId=%d canControl=%d\n",
			    pprop.onSubdevice, pprop.subdeviceId, pprop.canControl);
			printf("    defaultLimit=%d minLimit=%d maxLimit=%d [mW]\n",
			    pprop.defaultLimit, pprop.minLimit, pprop.maxLimit);
		    }

		    zes_power_sustained_limit_t pSustained;
		    zes_power_burst_limit_t pBurst;
		    zes_power_peak_limit_t pPeak;
		    if (zesPowerGetLimits(pwrh[di], &pSustained, &pBurst, &pPeak) == ZE_RESULT_SUCCESS) {
			printf("    sustained: enabled=%d power=%d [mW] interval=%d [ms]\n",
			    pSustained.enabled,
			    pSustained.power,
			    pSustained.interval);
			printf("    burst: enabled=%d power=%d [mW]\n",
			    pBurst.enabled,
			    pBurst.power);
			printf("    peak: powerAC=%d powerDC=%d [mW]\n",
			    pPeak.powerAC, pPeak.powerDC);
		    }

		    if(pprop.isEnergyThresholdSupported) {
			zes_energy_threshold_t pThreshold;
			res = zesPowerGetEnergyThreshold(pwrh[di], &pThreshold);
			if (res == ZE_RESULT_SUCCESS) {
			    printf("    threshold enable=%d threshold=%lf [J] processId=%d\n",
				pThreshold.enable, pThreshold.threshold, pThreshold.processId);
			} else {
			    printf("    zesPowerGetEnergyThreshold: ");
			    switch(res) {
			    case ZE_RESULT_ERROR_UNSUPPORTED_FEATURE:
				    printf("unsupported\n");
				    break;
			    case ZE_RESULT_ERROR_INSUFFICIENT_PERMISSIONS:
				    printf("insufficient permissions");
				    break;
			    default:
				    printf("other errors");
			    }
			}
		    } else {
			printf("    zesPowerGetEnergyThreshold: unsupported\n");
		    }

		    // XXX: record to a file
		    for (int k=0; k<2; k++) {
			zes_power_energy_counter_t ecounter;
			if (zesPowerGetEnergyCounter(pwrh[di], &ecounter) == ZE_RESULT_SUCCESS) {
			    if (pe<0.0) {
				pe = ecounter.energy;
				pt = gettime();
			    } else {
				double dt = gettime() - pt;
				uint64_t ects = ecounter.timestamp; // usec
				double de = ecounter.energy - pe;
				//printf("\tpowerreadtest: %lu [S] %lf [S]  %lf [W]\n", ects, gettime()-startsec, de/dt/1000.0/1000.0);
				printf("    powerreadtest: %.1lf [W]\n", de/dt/1000.0/1000.0);
			    }
			}
			usleep(100*1000);
		    }
		}
	    }

	    //
	    //
	    //
	    uint32_t nfreqdoms = 0;
	    res = zesDeviceEnumFrequencyDomains(sysmanh, &nfreqdoms ,NULL);
	    if (res != ZE_RESULT_SUCCESS) {
		_ZE_ERROR_MSG("zesDeviceEnumFrequencyDomains", res);
		//return;
	    } else {
		printf("Frequency: %u domain(s)\n", nfreqdoms);
		zes_freq_handle_t *freqh;

		freqh = malloc( sizeof(zes_freq_handle_t) * nfreqdoms);
		res = zesDeviceEnumFrequencyDomains(sysmanh, &nfreqdoms, freqh);
		if (res != ZE_RESULT_SUCCESS) {
		    _ZE_ERROR_MSG("zesDeviceEnumFrequencyDomains", res);
		    //return;
		} else {
		    for (int di =0; di < nfreqdoms; di++) {
			printf("  fdom=%d\n",di);
			zes_freq_properties_t fprop;
			memset(&fprop, 0, sizeof(zes_freq_properties_t));
			res = zesFrequencyGetProperties(freqh[di], &fprop);
			if (res == ZE_RESULT_SUCCESS) {
			    printf("    onsubdev=%d subdevId=%d canControl=%d\n",
				fprop.onSubdevice, fprop.subdeviceId, fprop.canControl); //fprop.isThrottleEventSupported
			    printf("    min=%.1lf max=%.1lf [MHz]\n",
				fprop.min, fprop.max);
			}

			zes_freq_state_t fstat;
			res = zesFrequencyGetState(freqh[di], &fstat);
			if (res == ZE_RESULT_SUCCESS) {
			    printf("    freqreadtest: req=%.1lf [MHz] efficient=%.1lf [MHz] actual=%.1lf\n",
				fstat.request, fstat.efficient, fstat.actual);
			}
		    }
		}
	    }

	    //
	    uint32_t ntempsensors = 0;
	    res = zesDeviceEnumTemperatureSensors(sysmanh, &ntempsensors ,NULL);
	    if (res != ZE_RESULT_SUCCESS) {
		    _ZE_ERROR_MSG("zesDeviceEnumTemperatureSensors", res);
		//return;
	    } else {
		printf("Temperature: %u sensor(s)\n", ntempsensors);
		zes_temp_handle_t *temph;

		temph = malloc( sizeof(zes_temp_handle_t) * ntempsensors);
		res = zesDeviceEnumTemperatureSensors(sysmanh, &ntempsensors, temph);
		if (res != ZE_RESULT_SUCCESS) {
		    _ZE_ERROR_MSG("zesDeviceEnumTemperatureSensors", res);
		    //return;
		} else {
		    for (int di =0; di < ntempsensors; di++) {
			printf("  dom=%d ",di);
			zes_temp_properties_t tprop;
			memset(&tprop, 0, sizeof(zes_temp_properties_t));
			res = zesTemperatureGetProperties(temph[di], &tprop);
			if (res == ZE_RESULT_SUCCESS) {
// https://spec.oneapi.com/level-zero/latest/sysman/api.html#_CPPv418zes_temp_sensorst_t
			    printf("type=");
			    switch(tprop.type) {
			    case ZES_TEMP_SENSORS_GLOBAL: printf("GLOBAL"); break;
			    case ZES_TEMP_SENSORS_GPU: printf("GPU"); break;
			    case ZES_TEMP_SENSORS_MEMORY: printf("MEM"); break;
			    case ZES_TEMP_SENSORS_GLOBAL_MIN: printf("GLOBAL_MIN"); break;
			    case ZES_TEMP_SENSORS_GPU_MIN: printf("GPU_MIN"); break;
			    case ZES_TEMP_SENSORS_MEMORY_MIN: printf("MEM_MIN"); break;
			    default: printf("Unknow");
			    }
			    printf(" onsubdev=%d subdevId=%d",
				tprop.onSubdevice, tprop.subdeviceId);

			    //max=%lf crit=%d threshold1=%d threadhold2=%d\n",
			    //tprop.maxTemperature, tprop.isCriticalTempSupported,
			    //tprop.isThreshold1Supported,
			    //tprop.isThreshold2Supported);
			    printf("\n");

			} else {
			    printf("zesTemperatureGetProperties() failed\n");
			}
		    }
		}
	    }


	}
    }
    printf("done\n");


}

int main(int argc, char *argv[])
{
    listdevices();
    return 0;
}
