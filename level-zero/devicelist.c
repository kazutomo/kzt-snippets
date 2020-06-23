#include <level_zero/ze_api.h>
#include <level_zero/zet_api.h>

#include <stdio.h>
#include <stdlib.h>

static void listdevices(void)
{
	ze_result_t res;

	// check level-zero/include/core/ze_driver.h
	res = zeInit(ZE_INIT_FLAG_NONE);
	if(res != ZE_RESULT_SUCCESS) {
		printf("zeInit() failed. res=%d\n", res);
		return;
	}

	// driver is platform in OpenCL
	uint32_t  ndrivers;
	res = zeDriverGet(&ndrivers, NULL);
	if(res != ZE_RESULT_SUCCESS) {
		printf("zeDriverGet() failed. res=%d\n", res);
		return;
	}
	printf("Number of drivers: %d\n", ndrivers);

	ze_driver_handle_t *drvh;
	drvh = malloc(sizeof(ze_driver_handle_t) * ndrivers);
	if(!drvh)  {
		printf("malloc() failed\n");
		return;
	}
	res = zeDriverGet(&ndrivers, drvh);
	if(res != ZE_RESULT_SUCCESS) {
		printf("zeDriverGet() failed. res=%d\n", res);
		return;
	}

	// iterate over drivers
	for (int i = 0; i < ndrivers; i++) {
		ze_driver_handle_t d = drvh[i];
		ze_driver_properties_t prop;
		res = zeDriverGetProperties(d, &prop);
		if (res != ZE_RESULT_SUCCESS) {
			printf("zeDriverGetProperties() failed. res=%d\n", res);
			return;
		}

		// devices
		uint32_t ndevs = 0;
		zeDeviceGet(drvh[i], &ndevs, NULL);
		ze_device_handle_t *devh;
		devh = malloc(sizeof(ze_device_handle_t) * ndevs);
		if(!devh)  {
		    printf("malloc() failed\n");
		    return;
		}
		zeDeviceGet(drvh[i], &ndevs, devh);

		printf("driver%d: version=%u ndevs=%d\n",
		       i, prop.driverVersion, ndevs);

		for (int di = 0; di < ndevs; di++) {
		    ze_device_properties_t devprop;
		    zeDeviceGetProperties(devh[di], &devprop);
		    printf("\tdev%d: ", di);
		    if (devprop.type == ZE_DEVICE_TYPE_GPU) {
			printf("TYPE=GPU ");

			zet_sysman_handle_t sysmanh = (zet_sysman_handle_t)devh[di];
			uint32_t pdomains = 0;
			zetSysmanPowerGet(sysmanh, &pdomains, NULL);
			printf("NPDOMAINS=%d\n", pdomains);


		    } else {
			printf("TYPE=!GPU");
		    }

		    printf("\n");
		}
	}
}

int main(int argc, char *argv[])
{
	listdevices();
	return 0;
}
