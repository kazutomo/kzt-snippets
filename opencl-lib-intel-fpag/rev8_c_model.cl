
uchar c_rev8(uchar b)
{
	uchar tmp = 0;
	int i;

	for (i = 0; i < 7; i++) {
                tmp |= (b&1);
                b  = b >> 1;
                tmp = tmp << 1;
        }
        tmp |= (b&1);

	return tmp;
}
