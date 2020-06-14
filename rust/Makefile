
SRCS=$(shell ls -1 *.rs)
BINS=$(SRCS:.rs=)

% : %.rs
	rustc $<
	./$@

clean:
	rm -f $(BINS)
