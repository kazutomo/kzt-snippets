# Chisel sample makefile
# written by Kazutomo Yoshii <kazutomo.yoshii@gmail.com>

T=rev

all:
	@echo "Targets: verilog test simulate"


# generate a Verilog fil
v verilog:
	sbt "test:runMain foobar.TestMain $T:verilog"

# run Scala Unit test
t test:
	sbt "test:runMain foobar.TestMain $T"

# simulate using Verilator
s simulate:
	sbt "test:runMain foobar.TestMain $T --backend-name verilator"

h help:
	sbt "test:runMain foobar.TestMain --help"

clean:
	rm -rf project target test_run_dir generated *.class
	rm -rf *.fir *.anno.json *.v
