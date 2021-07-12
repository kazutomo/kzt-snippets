#
# A simple makefile for OpenCL host codes and emulated kernels
#

CC=gcc
CXX=g++

CLWRAP_PATH ?= ../clwrap/

CFLAGS = -Wall -O2 -g  -Wno-unknown-pragmas -I. -I$(CLWRAP_PATH)
CXXFLAGS = $(CFLAGS) $(shell aocl compile-config) -std=c++0x
LDFLAGS_AOC = $(shell aocl link-config) -L/opt/glibc-2.14/lib

all: checkenv  host testbitlib-emu.aocx

host.o : host.cpp

host : host.o
	$(CXX) -o $@ $^ $(CXXFLAGS) $(LDFLAGS_AOC)

testbitlib-emu.aocx : testbitlib.cl rev8_c_model.cl cnt32_c_model.cl
	aoc -march=emulator -o $@ $^

clean:
	rm -f host *.o

distclean: clean
	rm -rf testbitlib testbitlib-emu
	rm -f *.aoco *.aocx
	rm -f aoc.log *~
	rm -f __all_sources.cl


.PHONY: checkenv
checkenv: 
	@ if [ "${ALTERAOCLSDKROOT}" = "" ] ; then \
		echo "Please source aoc environment!"; \
		exit 1; \
	fi

