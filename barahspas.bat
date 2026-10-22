@echo off
javac -d bin src/com/github/jkarw7n/barahspas/*.java
java -cp bin;lib/sqlite-jdbc-3.53.4.0.jar com.github.jkarw7n.barahspas.Main