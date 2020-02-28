package tictactoe.console;

import tictactoe.*;

/**
 * Used for printing a GameBoard to the console.
 *
 * <p>Can print game boards up to the size of 99x99.</p>
 *
 * @author Tapio Malmberg
 */
class GameBoardPrinter {
    /**
     * The symbols for empty, human player and computer player.
     */
    private static final String[] SYMBOLS = { " ", "X", "O" };

    /**
     * Prints a game board to the console.
     * @param gameBoard the game board to print
     */
    public static void print(ImmutableGameBoard gameBoard) {
        int width = gameBoard.getWidth();
        printXCoordinates(width);

        for (int y = 0; y < gameBoard.getHeight(); y++) {
            printPadding();
            printLine(width * 2 + 1);
            printYCoordinate(y);
            printRow(y, gameBoard);
        }
        printPadding();
        printLine(width * 2 + 1);
    }

    /**
     * Prints the X coordinates at the top of the game board.
     * @param width the width of the game board
     */
    private static void printXCoordinates(int width) {
        // First line: tenths
        printPadding();
        for (int x = 1; x <= width; x++) {
            int n = x / 10;
            // Do not print zeroes for the tenths
            String text = " " + (n == 0 ? " " : Integer.toString(n));
            System.out.print(text);
        }
        System.out.println();

        // Second line: ones
        printPadding();
        for (int x = 1; x <= width; x++) {
            int n = x % 10;
            System.out.print(" " + n);
        }
        System.out.println();
    }

    /**
     * Prints a Y coordinate at the left side of the game board.
     * @param y the y value to print
     */
    private static void printYCoordinate(int y) {
        System.out.print(y + 1);
        if (y < 9)
            System.out.print(" ");
    }

    /**
     * Prints a row of the game board.
     * @param y the y coordinate of the row
     * @param gameBoard the game board
     */
    private static void printRow(int y, ImmutableGameBoard gameBoard) {
        for (int x = 0; x < gameBoard.getWidth(); x++) {
            System.out.print("|");
            printTicTac(gameBoard.getSquare(x, y));
        }
        System.out.println("|");
    }

    /**
     * Prints a symbol depending on the content of the square.
     * 
     * <p>Green color is used for the player, red for the computer.</p>
     * @param squareValue the value at the square on the game board
     */
    private static void printTicTac(byte squareValue) {
        // [31m = red
        // [32m = green
        // [37m = white
        String colorWhite = (char) 27 + "[37m";
        String colorStr = (char) 27 + (squareValue == TicTacToeGame.PLAYER ? "[32m" : "[31m");
        if (squareValue == TicTacToeGame.EMPTY)
            colorStr = colorWhite;

        System.out.print(colorStr + SYMBOLS[squareValue] + colorWhite);
    }

    /**
     * Prints a line that separates the rows on the game board.
     * @param width the width of the line to print
     */
    private static void printLine(int width) {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Prints the padding for the left margin that contains the Y coordinates.
     */
    private static void printPadding() {
        System.out.print("  ");
    }
}