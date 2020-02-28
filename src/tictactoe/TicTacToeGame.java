package tictactoe;

import java.util.*;

/**
 * A game of Tic-Tac-Toe where the size of the game board and
 * the number of game pieces in a row needed to win can be set.
 * 
 * @author Tapio Malmberg
 */
public class TicTacToeGame {
    /**
     * The minimum width allowed for the game board.
     */
    public static final int MIN_WIDTH = 3;

    /**
     * The minimum height allowed for the game board.
     */
    public static final int MIN_HEIGHT = 3;

    /**
     * The minimum allowed number of pieces in a row required to win on a small game board.
     */
    public static final int MIN_IN_A_ROW_TO_WIN_SMALL = 3;

    /**
     * The minimum allowed number of pieces in a row required to win on a large game board.
     */
    public static final int MIN_IN_A_ROW_TO_WIN_LARGE = 5;

    /**
     * The size of the side of a game board from which a game board is considered large.
     */
    public static final int LARGE_GAME_BOARD_SIZE = 10;

    /**
     * Represents an empty square.
     */
    public static final byte EMPTY = 0;

    /**
     * Represents the human player.
     */
    public static final byte PLAYER = 1;

    /**
     * Represents the computer player.
     */
    public static final byte COMPUTER = 2;
    
    /**
     * The game board.
     */
    private GameBoard gameBoard;

    /**
     * The number of pieces needed in a row to win.
     */
    private int numInARowToWin;

    /**
     * The maximum number of moves possible on the game board.
     */
    private int maxMoves;

    /**
     * The total number of moves that have been played by both players.
     */
    private int playedMoves;

    /**
     * The computer player
     */
    private ComputerPlayer computerPlayer;

    /**
     * A stack of MovePairs for undoing moves.
     */
    private Stack<MovePair> undoStack;

    /**
     * Checks whether the given size is considered a large game board.
     * @param width the width to test
     * @param height the height to test
     * @return true if the game board is considered large
     */
    public static boolean isLargeGameBoard(int width, int height) {
        return width >= LARGE_GAME_BOARD_SIZE && height >= LARGE_GAME_BOARD_SIZE;
    }

    /**
     * Creates a new TicTacToeGame with the given attributes.
     * @param width the width of the game board
     * @param height the height of the game board
     * @param numInARowToWin the number of pieces in a row needed to win
     * @param computer the computer player
     * @param computerStarts decides whether the computer gets to make the first move
     */
    public TicTacToeGame(int width, int height, int numInARowToWin, ComputerPlayer computer, boolean computerStarts) {
        if(width < MIN_WIDTH)
            throw new IllegalArgumentException("Width must be " + MIN_WIDTH + " or greater.");
        if(height < MIN_HEIGHT)
            throw new IllegalArgumentException("Height must be " + MIN_HEIGHT + " or greater.");
        if(numInARowToWin < MIN_IN_A_ROW_TO_WIN_SMALL)
            throw new IllegalArgumentException("The number of game pieces in a row needed to win has to be at least " + MIN_IN_A_ROW_TO_WIN_SMALL +".");

        this.gameBoard = new GameBoard(width, height);
        this.undoStack = new Stack<MovePair>();
        this.computerPlayer = computer;
        this.numInARowToWin = numInARowToWin;
        this.maxMoves = height * width;

        if(computerStarts)
            makeComputerMove();
    }

    /**
     * Plays a round of Tic-Tac-Toe with moves by both the human player and the computer.
     * @param playerMove the move made by the human player
     * @return the current state of the game
     */
    public GameState playARound(Coordinate playerMove) {
        if(!trySetPlayerMove(playerMove)) {
            return GameState.INVALID_PLAYER_MOVE;
        }

        if(hasWon(PLAYER, playerMove) || isDrawGame()) {
            addUndo(playerMove, null);
            return isDrawGame() ? GameState.DRAW_GAME : GameState.PLAYER_WON;
        }

        Coordinate computerMove = makeComputerMove();
        addUndo(playerMove, computerMove);

        if(hasWon(COMPUTER, computerMove) || isDrawGame()) {
            return isDrawGame() ? GameState.DRAW_GAME : GameState.COMPUTER_WON;
        }

        return GameState.READY_FOR_NEXT_MOVE;
    }

    /**
     * Resets the game.
     * @param computerStarts decides whether the computer gets to make the first move
     */
    public void reset(boolean computerStarts) {
        playedMoves = 0;
        undoStack.clear();
        gameBoard.fillBoard(EMPTY);

        if(computerStarts)
            makeComputerMove();
    }

    /**
     * Checks if there are any undoable moves.
     * @return true if a move can be undone
     */
    public boolean canUndo() {
        return !undoStack.empty();
    }

    /**
     * Undoes the previous moves made by the human and the computer.
     * @return returns {@code INVALID_PLAYER_MOVE} if no undo is available
     */
    public GameState undo() {
        if(!canUndo())
            return GameState.INVALID_PLAYER_MOVE;

        MovePair moves = undoStack.pop();
        if(moves.playerMove != null) {
            gameBoard.setSquare(moves.playerMove, EMPTY);
            playedMoves--;
        }
        if(moves.computerMove != null) {
            gameBoard.setSquare(moves.computerMove, EMPTY);
            playedMoves--;
        }
        
        return GameState.READY_FOR_NEXT_MOVE;
    }

    /**
     * Returns the current game board as read-only.
     * @return the game board of this game
     */
    public ImmutableGameBoard getGameBoard() {
        return gameBoard.asImmutable();
    }

    /**
     * Returns the last move made by the computer.
     * @return the last move made by the computer or null if no move has been made
     */
    public Coordinate getLastComputerMove() {
        if(canUndo())
            return undoStack.peek().computerMove;
        else
            return null;
    }

    /**
     * Adds a move pair to the undo stack.
     * @param playerMove the move made by the human player
     * @param computerMove the move made by the computer player
     */
    private void addUndo(Coordinate playerMove, Coordinate computerMove) {
        undoStack.push(new MovePair(playerMove, computerMove));
    }

    /**
     * Checks if the game is a draw.
     * @return true if the game is a draw
     */
    private boolean isDrawGame() {
        return playedMoves == maxMoves;
    }

    /**
     * Tries to set the player move at the given Coordinate.
     * @param playerMove the Coordinate of the player's move
     * @return true if the given move was valid, false otherwise
     */
    private boolean trySetPlayerMove(Coordinate playerMove) {
        if (playerMove == null || !gameBoard.isWithinBounds(playerMove) || gameBoard.getSquare(playerMove) != EMPTY)
            return false;

        gameBoard.setSquare(playerMove, PLAYER);
        ++playedMoves;
        return true;
    }

    /**
     * Checks if a player has won the game with the given move.
     * @param player HUMAN or COMPUTER
     * @param playedMove the Coordinate of the played move
     * @return true if the given player has won the game
     */
    private boolean hasWon(byte player, Coordinate playedMove) {
        VictoryChecker victoryChecker = new VictoryChecker(getGameBoard(), player, playedMove, numInARowToWin);
        return victoryChecker.hasWon();
    }

    /**
     * Gets a move from the computer player and sets it on the game board.
     * @return the move the computer player made
     */
    private Coordinate makeComputerMove() {
        Coordinate computerMove = computerPlayer.getMove(getGameBoard(), numInARowToWin);
        if(gameBoard.getSquare(computerMove) != EMPTY)
            throw new IllegalStateException("The computer player returned a position that is already taken.");
            
        ++playedMoves;
        gameBoard.setSquare(computerMove, COMPUTER);
        return computerMove;
    }
}