CFLAGS=-Wall -O2 -g
LDFLAGS=-lze_loader

SRCS=$(shell ls -1 *.c)
BINS=$(SRCS:.c=)

all: $(BINS)

% : %.c
	$(CC) -o $@ $< $(CFLAGS) $(LDFLAGS)

clean:
	rm -f $(BINS)
