# Chisel sample makefile
# written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>

T=Foo

all:
	@echo "To run:"
	@echo ""
	@echo "$ make test       # run Scala test"
	@echo "$ make simulate   # invoke Verilator"
	@echo "$ make verilog    # generate a Verilog code"
	@echo ""
	@echo "Shortcut for convinice"
	@echo "$ make t   # for test"
	@echo "$ make s   # for simulate"
	@echo "$ make v   # for verilog"
	@echo ""
	@echo ""
	@echo "To test a specific module:"
	@echo "$ make test T=Rev  #  test the Rev module"
	@echo "NOTE: it searches for the shortest possible match with lower case. If there is no other module starts with 'r', you can simply say T=r"
	@echo ""
	@echo "To list target available modules:"
	@echo "$ make list"
	@echo ""


# run Scala Unit test
t test:
	sbt "test:runMain foobar.TestMain t $T"

# generate a Verilog fil
v verilog:
	sbt "test:runMain foobar.TestMain v $T"

# simulate using Verilator
s simulate:
	sbt "test:runMain foobar.TestMain s $T"

l list:
	sbt "test:runMain foobar.TestMain l"

h help:
	sbt "test:runMain foobar.TestMain t $T --help"

clean:
	rm -rf project target test_run_dir generated *.class
	rm -rf *.fir *.anno.json *.v
