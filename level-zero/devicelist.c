#include <level_zero/ze_api.h>
#include <level_zero/zes_api.h>

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>

#define _ZE_ERROR_MSG(NAME,RES) {printf("%s() failed at %d(%s): res=%x\n",(NAME),__LINE__,__FILE__,(RES));}
#define _ERROR_MSG(MSG) {perror((MSG)); printf("errno=%d at %d(%s)",errno,__LINE__,__FILE__);}

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
// Calling zesDeviceEnumPowerDomains() causes segv now. Wait until the next release. 
#if 0
			zes_device_handle_t sysmanh = (zes_device_handle_t)devh[di];
			uint32_t npwrdoms = 0;
			res = zesDeviceEnumPowerDomains(sysmanh, &npwrdoms ,NULL);
			if (res != ZE_RESULT_SUCCESS) {
				_ZE_ERROR_MSG("zesDeviceEnumPowerDomains", res);
				return;
			}
			printf("npwrdoms=%u\n", npwrdoms);

			zes_pwr_handle_t *pwrh;
			if (npwrdoms > 0) {
				pwrh = malloc( sizeof(zes_pwr_handle_t) * npwrdoms);
				res = zesDeviceEnumPowerDomains(sysmanh, &npwrdoms, pwrh);
				if (res != ZE_RESULT_SUCCESS) {
					_ZE_ERROR_MSG("zesDeviceEnumPowerDomains", res);
					return;
				}
			}
#endif
		}
	}
}

int main(int argc, char *argv[])
{
	listdevices();
	return 0;
}
