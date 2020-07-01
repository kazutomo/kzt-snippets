SRCS=$(shell ls -1 *.cpp)
BINS=$(SRCS:.cpp=)

all: $(BINS)-dp $(BINS)

%-dp : %.cpp
	dpcpp -DENABLE_DP -Wall -O2 -g -o $@ $<

% : %.cpp
	dpcpp -Wall -O2 -g -o $@ $<

clean:
	rm -f $(BINS)-dp $(BINS)
