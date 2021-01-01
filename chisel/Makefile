# Chisel sample makefile
# written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>

T=Foo
M=test

all:
	@echo "To run test:"
	@echo "$ make TARGET"
	@echo ""
	@echo "To generate Verilog file"
	@echo "$ make TARGET.v	"
	@echo ""
	@echo "To simulate using Verilator"
	@echo "$ make TARGET.vcd"
	@echo ""
	@echo "To list target available modules:"
	@echo "$ make list"
	@echo ""

% : %.scala
	@./run.sh test $@

%.v : %.scala
	@./run.sh verilog $(@:.v=)

%.vcd : %.scala
	@./run.sh simulate $(@:.vcd=)

l list:
	@./run.sh list

h help:
	@./run.sh test $T --help

clean:
	rm -rf project target test_run_dir generated *.class
	rm -rf *.fir *.anno.json *.v
