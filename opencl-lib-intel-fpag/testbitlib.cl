#include "libtest.h"


__kernel void testbitlib(
	 __global uchar *restrict in,
         __global uchar *restrict out,
	 __global uint *restrict t1,
	 __global uint *restrict t2)

{
	int gid = get_global_id(0);
	uint tt;

	tt = 0;

	tt = c_cnt32(tt);
	t1[gid] = tt;
	out[gid] = c_rev8(in[gid]); // c_cnt32() and c_rev8() runs at the same time?

	tt = c_cnt32((uint)out[gid]); //  
	t2[gid] = tt;
}


