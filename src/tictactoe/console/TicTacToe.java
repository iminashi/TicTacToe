package tictactoe.console;

import tictactoe.*;
import java.util.Scanner;

/**
 * A game of Tic-Tac-Toe played in the console.
 *
 * @author Tapio Malmberg
 */
public class TicTacToe {
    /**
     * The maximum width allowed for the game board.
     */
    private static final int MAX_WIDTH = 80;

    /**
     * The maximum height allowed for the game board.
     */
    private static final int MAX_HEIGHT = 30;

    /**
     * Error message displayed when a string could not be converted to an integer.
     */
    private static String NUMBER_PARSE_ERROR = "Please give a number.";

    /**
     * The Scanner for reading the user input.
     */
    private static Scanner input = new Scanner(System.in);

    /**
     * The Tic-Tac-Toe game.
     */
    private static TicTacToeGame game;

    /**
     * The current state of the game.
     */
    private static GameState gameState;

    /**
     * Main program entry point.
     * @param args not used
     */
    public static void main(String[] args) {
        printInstructions();
        createGame();
        playUntilUserQuits();
    }

    /**
     * Prints the commands available in the game to the console.
     */
    private static void printInstructions() {
        System.out.println("***************************");
        System.out.println("*       Tic-Tac-Toe       *");
        System.out.println("***************************");
        System.out.println("u - Undo the previous move.");
        System.out.println("r - Reset the game.");
        System.out.println("c - Change settings.");
        System.out.println("q - Quit the game.");
        System.out.println();
    }

    /**
     * Creates the TicTacToeGame by asking the user for its settings.
     */
    private static void createGame() {
        int width = getNumberInRange("Set game board width", TicTacToeGame.MIN_WIDTH, MAX_WIDTH);
        int height = getNumberInRange("Set game board height", TicTacToeGame.MIN_HEIGHT, MAX_HEIGHT);
        int numInARowToWin = getValidNumInARowToWin(width, height);
        ComputerPlayer computer = getComputerDifficulty();
        game = new TicTacToeGame(width, height, numInARowToWin, computer, doesComputerMakeFirstMove());
    }

    /**
     * Gets a number in the given range from the user.
     * @param message the message to print
     * @param min the inclusive minimum value allowed
     * @param max the inclusive maximum value allowed
     * @return a number within the given range
     */
    private static int getNumberInRange(String message, int min, int max) {
        String range = min + "-" + max;
        System.out.print(message + " (" + range + "): ");
        return Utils.readInt(min, max, NUMBER_PARSE_ERROR, "Must be in the range " + range + ".");
    }

    /**
     * Gets a valid number of pieces in a row needed to win
     * for the selected game board size.
     * @param width the width of the game board
     * @param height the height of the game board
     * @return a valid number of pieces in a row needed to win
     */
    private static int getValidNumInARowToWin(int width, int height) {
        int min = TicTacToeGame.MIN_IN_A_ROW_TO_WIN_SMALL;
        if(TicTacToeGame.isLargeGameBoard(width, height)) {
            min = TicTacToeGame.MIN_IN_A_ROW_TO_WIN_LARGE;
        }
        int max = Math.max(width, height);
        int numInARowToWin = min;
        if(max > min) {
            numInARowToWin = getNumberInRange("Set the number of O's or X's in a row needed to win", min, max);
        }

        return numInARowToWin;
    }

    /**
     * Gets the computer difficulty from the user.
     * @return a ComputerPlayer with the difficulty the user selected
     */
    private static ComputerPlayer getComputerDifficulty() {
        String options = "1. Random, 2. Smart";
        System.out.print("Select computer difficulty (" + options + "): ");
        int choice = Utils.readInt(1, 2, NUMBER_PARSE_ERROR, options);
        return choice == 1 ? new RandomComputer() : new SmartComputer();
    }

    /**
     * Randomizes whether the computer starts and prints a message to the console.
     * @return true if the computer makes the first move
     */
    private static boolean doesComputerMakeFirstMove() {
        boolean computerStarts = Utils.getRandom(0, 1) == 1;
        System.out.print("After a fair coin toss, ");
        if (computerStarts)
            System.out.println("the computer gets to make the first move.");
        else
            System.out.println("you get to make the first move.");
        return computerStarts;
    }

    /**
     * Keeps playing games of Tic-Tac-Toe until the user chooses to quit.
     */
    private static void playUntilUserQuits() {
        boolean playAnotherGame = true;

        while (playAnotherGame) {
            playAGame();
            printGameResult();
            playAnotherGame = (gameState != GameState.USER_QUIT) && confirmAnotherGame();
        }
    }

    /**
     * Asks the user if they want to play another game.
     * 
     * <p>The user can also choose to change the settings of the game
     * or undo the last move.</p>
     * @return true if the user wants to keep playing
     */
    private static boolean confirmAnotherGame() {
        System.out.print("Play another game (c - change settings, u - undo) [y]/n? ");
        String answer = input.nextLine().trim();
        if (answer.length() == 0) {
            answer = "y";
        }
        switch (Character.toLowerCase(answer.charAt(0))) {
            case 'y':
                game.reset(doesComputerMakeFirstMove());
                return true;
            case Commands.CHANGE_SETTINGS:
                createGame();
                return true;
            case Commands.UNDO:
                gameState = game.undo();
                return true;
            default:
                return false;
        }
    }

    /**
     * Plays a game of Tic-Tac-Toe until the end or until the user quits.
     * 
     * <p>Keeps printing the game board using {@code GameBoardPrinter} and playing rounds
     * until the game ends.</p>
     */
    private static void playAGame() {
        do {
            GameBoardPrinter.print(game.getGameBoard());
            getPlayerMoveOrCommand();
        } while (gameState == GameState.READY_FOR_NEXT_MOVE);

        // Print the game board at the end of a game unless the user quit
        if (gameState != GameState.USER_QUIT)
            GameBoardPrinter.print(game.getGameBoard());
    }

    /**
     * Prompts the user for a move until they give a valid one
     * or a valid command.
     */
    private static void getPlayerMoveOrCommand() {
        do {
            System.out.print("Make your move (row column): ");
            processUserInput(input.nextLine().trim());
        } while (gameState == GameState.INVALID_PLAYER_MOVE);
    }

    /**
     * Processes the input from the user.
     * 
     * <p>Either processes a command or parses a coordinate
     * from the user input and plays a round of Tic-Tac-Toe.</p>
     * @param userInput a string got from the user
     */
    private static void processUserInput(String userInput) {
        if (userInput.length() == 1) {
            processCommand(userInput);
        } else {
            gameState = game.playARound(parseCoordinate(userInput));
            if (gameState == GameState.INVALID_PLAYER_MOVE)
                System.out.println("That is not a valid move.");
        }
    }

    /**
     * Processes a single character command got from the user.
     * 
     * <p>Changes {@code gameState} accordingly.<p>
     * @param userInput a string got from the user
     */
    private static void processCommand(String userInput) {
        switch (Character.toLowerCase(userInput.charAt(0))) {
            case Commands.CHANGE_SETTINGS:
                createGame();
                gameState = GameState.READY_FOR_NEXT_MOVE;
                break;

            case Commands.QUIT:
                gameState = GameState.USER_QUIT;
                break;

            case Commands.RESET:
                System.out.println("Game reset.");
                game.reset(doesComputerMakeFirstMove());
                gameState = GameState.READY_FOR_NEXT_MOVE;
                break;

            case Commands.UNDO:
                gameState = game.undo();
                if (gameState == GameState.INVALID_PLAYER_MOVE)
                    System.out.println("No undo available.");
                break;

            default:
                System.out.println("Unknown command.");
                gameState = GameState.INVALID_PLAYER_MOVE;
            }
    }

    /**
     * Parses a {@code Coordinate} from the user input.
     * 
     * <p>The coordinate can be input in two formats:
     * "y x" or
     * "y,x"</p>
     * @param input a string got from the user
     * @return the parsed coordinate or null if the parsing failed
     */
    private static Coordinate parseCoordinate(String input) {
        String[] yx = input.split("[ ,]");

        try {
            int y = Integer.parseInt(yx[0]) - 1;
            int x = Integer.parseInt(yx[1]) - 1;

            return new Coordinate(x, y);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Prints an appropriate game over message to the console.
     */
    private static void printGameResult() {
        switch (gameState) {
            case PLAYER_WON:
                System.out.println(":D Congratulations, you won!");
                break;

            case COMPUTER_WON:
                System.out.println(":( Too bad, the computer beat you!");
                break;

            case DRAW_GAME:
                System.out.println(":| The game ended in a draw.");
                break;

            case USER_QUIT:
                System.out.println("Thank you for playing.");
                break;

            default:
                System.out.println("ERROR: Unexpected end state.");
        }
    }
}