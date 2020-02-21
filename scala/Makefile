all:
	echo do nothing

% %.class : %.scala
	scalac -feature $<
	scala  $@

dummytwo : dummyone.scala dummytwo.scala
	scalac  $^
	scala  $@


clean:
	rm -f *.class
	rm -rf target
	rm -rf project
	rm -f tmpoutput.txt
	rm -rf dummyone

distclean: clean
	rm -f *~
