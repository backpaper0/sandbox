BIN=mycalc

.PHONY: all
all: clean build

.PHONY: build
build:
	@go build -o $(BIN) ./cmd

.PHONY: clean
clean:
	@if [ -f $(BIN) ]; then \
		rm $(BIN); \
	fi

