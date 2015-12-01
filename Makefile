release: stash cleanup
	git add . --all
	git commit -m "Create a new release version"

compile:
	gradle build
	@# Fuck regex
	java -jar build/libs/magisterapi-$(shell grep "version '" < build.gradle | sed -r "s/version '(.*)'/\1/" | tr -d '\n').jar
	# "Je moeder"
	# -Menno 2015

stash:
	git stash
	git checkout -b release

cleanup:
	rm -rf build .idea
	rm src/main/java/eu/magisterapp/magisterapi/Main.java

