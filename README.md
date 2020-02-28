# Tic-Tac-Toe / N-In-A-Row

A configurable Tic-Tac-Toe game with both graphical and console UIs. Includes a simple AI and an undo feature. Made for a programming course in the Tampere University of Applied Sciences.

![Screenshot of the console UI](https://i.imgur.com/RbWSf2Z.png)
![Screenshot of the GUI](https://i.imgur.com/E6sA9lU.png) 


# Compiling and running
- ./make.sh

or

- javac -d bin -sourcepath src src/tictactoe/*.java
- javac -d bin -sourcepath src src/tictactoe/console/*.java
- javac -d bin -sourcepath src src/tictactoe/gui/*.java

Run GUI Version:

``java -cp bin tictactoe.gui.TicTacToe``

Run Console Version:

``java -cp bin tictactoe.console.TicTacToe``

Note: The colors used in the console version may not work in the standard Windows Command Prompt.