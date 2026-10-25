@echo off
javac -cp bin;lib/* -d bin src/com/github/jkarw7n/barahspas/*.java
java -cp bin;lib/* com.github.jkarw7n.barahspas.Main