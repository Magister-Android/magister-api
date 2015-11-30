release: stash cleanup
	git add . --all
	git commit -m "Create a new release version"

stash:
	git stash
	git checkout -b release

cleanup:
	rm -rf build .idea
	rm src/main/java/eu/magisterapp/magisterapi/Main.java
