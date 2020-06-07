SRCS=$(shell ls -1 *.cpp)
BINS=$(SRCS:.cpp=)

% : %.cpp
	dpcpp -Wall -O2 -g -o $<

clean:
	rm -f $(BINS)
