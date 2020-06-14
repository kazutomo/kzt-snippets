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
		if(res != ZE_RESULT_SUCCESS) {
			printf("zeDriverGetProperties() failed. res=%d\n", res);
			return;
		}
		printf("%d: version=%u\n", i,  prop.driverVersion);
	}
}

int main(int argc, char *argv[])
{
	listdevices();
	return 0;
}


