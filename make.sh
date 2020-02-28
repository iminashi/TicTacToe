#!/bin/sh
if [ "$1" = "doc" ]; then
    javadoc -author -private -d doc -sourcepath src -subpackages tictactoe
fi

javac -d bin -sourcepath src src/tictactoe/*.java
javac -d bin -sourcepath src src/tictactoe/console/*.java
javac -d bin -sourcepath src src/tictactoe/gui/*.java