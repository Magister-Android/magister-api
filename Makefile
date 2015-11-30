release:
	git stash
	git checkout -b release
	rm -rf build .idea
	rm src/main/java/eu/magisterapp/magisterapi/Main.java
	git add . --all
	git commit -m "Create a new release version"