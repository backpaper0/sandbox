.PHONY: build-HelloWorldFunction
build-HelloWorldFunction:
	cp bootstrap $(ARTIFACTS_DIR)
	cp function.sh $(ARTIFACTS_DIR)

.PHONY: test
test:
	echo '{"message": "Hey, are you there?" }' | sam local invoke "HelloWorldFunction" --event -
