
SRCS=$(shell ls *.v)
TARGETS=$(SRCS:.v=)

all:
	@echo Targets: $(TARGETS)

% : %.v
	@iverilog -o $@ $<
	@vvp $@

clean:
	rm -f $(TARGETS)
	rm -f *.o
	rm -f *.vcd
