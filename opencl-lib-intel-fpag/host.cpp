#include "clwrap.hpp"
#include <sys/time.h>
#include <stdio.h>

static void testbitlib(const char *aocx)
{
	clWrap  cw;
	int gsiz = 8;
	cl_uchar *din  = new cl_uchar[gsiz];
	cl_uchar *dout = new cl_uchar[gsiz];
	cl_uint *t1  = new cl_uint[gsiz];
	cl_uint *t2  = new cl_uint[gsiz];
	int i;

	for (i = 0; i < gsiz; i++) {
		din[i] = i%256;
	}

	cw.prepKernel(aocx, "testbitlib");

	cw.appendArg(sizeof(cl_uchar)*gsiz, din,  cw.HOST2DEV);
	cw.appendArg(sizeof(cl_uchar)*gsiz, dout, cw.DEV2HOST);
	cw.appendArg(sizeof(cl_int)*gsiz, t1, cw.DEV2HOST);
	cw.appendArg(sizeof(cl_int)*gsiz, t2, cw.DEV2HOST);

	cw.runKernel(gsiz);

	for (i = 0; i < gsiz; i++) {
		printf("%02x => %02x\n", din[i], dout[i]);
		printf("t1=%d t2=%d\n", t1[i], t2[i]);
	}

	delete t1;
	delete t2;
	delete dout;
	delete din;
};

int main(int argc, char *argv[])
{
	char *aocx = (char*)"testbitlib.aocx";

	if (argc >= 2) {
		aocx = argv[1];
	}
	printf("aocx: %s\n", aocx);

	testbitlib(aocx);

	return 0;
}
