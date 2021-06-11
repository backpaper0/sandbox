.PHONY: all
all: build run

.PHONY: build
build:
	docker build -t demo --build-arg FOO1=fooxxx --build-arg BAR=barxxx .

.PHONY: run
run:
	docker run --rm demo cat foo1.txt foo2.txt bar.txt
	docker run --rm demo ./test.sh
