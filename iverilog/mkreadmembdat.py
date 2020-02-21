#!/usr/bin/env python

for i in range(0,16):
    str = ""
    for j in range(15,-1,-1):
        if i==j :
            str += "1"
        else:
            str += "0"
    print str

