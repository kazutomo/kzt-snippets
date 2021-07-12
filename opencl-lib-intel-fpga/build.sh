rm -f rev8.aoco cnt32.aoco libtest.aoclib
aocl library hdl-comp-pkg rev8.xml  -o rev8.aoco
aocl library hdl-comp-pkg cnt32.xml -o cnt32.aoco

aocl library create -name testlib -vendor kzt -version 16.0 -o libtest.aoclib  rev8.aoco cnt32.aoco

time aoc -v  -fast-compile --report -l libtest.aoclib testbitlib.cl 2>&1 | tee aoc.log

