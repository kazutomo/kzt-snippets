# Chisel sample makefile
# written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>

T=rev

all:
	@echo "To run:"
	@echo ""
	@echo "$ make test       # run Scala test"
	@echo "$ make simulate   # invoke Verilator"
	@echo "$ make verilog    # generate Verilog codes"
	@echo ""
	@echo "Shortcut for convinice"
	@echo "$ make t   # for test"
	@echo "$ make s   # for simulate"
	@echo "$ make v   # for verilog"
	@echo ""
	@echo ""
	@echo "To test invididual module"
	@echo "$ make test T=squeeze  #  test the squeeze module"
	@echo ""
	@echo "To list target available modules"
	@echo "$ make list"
	@echo ""



# generate a Verilog fil
v verilog:
	sbt "test:runMain foobar.TestMain $T:verilog"

# run Scala Unit test
t test:
	sbt "test:runMain foobar.TestMain $T"

# simulate using Verilator
s simulate:
	sbt "test:runMain foobar.TestMain $T --backend-name verilator"

l list:
	sbt "test:runMain foobar.TestMain list"

h help:
	sbt "test:runMain foobar.TestMain --help"

clean:
	rm -rf project target test_run_dir generated *.class
	rm -rf *.fir *.anno.json *.v
