.PHONY: build-ui

build-ui:
	npm install
	npm run build
	mkdir -p ../src/main/resources/static
	cp -rv build/* ../src/main/resources/static/