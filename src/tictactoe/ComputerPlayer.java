package tictactoe;

/**
 * Interface for generating a move for the computer player.
 *
 * @author Tapio Malmberg
 */
public interface ComputerPlayer {
    /**
     * Calculates a valid move based on the game board and
     * the number of pieces in a row needed to win.
     * @param gameBoard the game board
     * @param numInARowToWin the number of pieces in a row needed to win
     * @return the coordinate of the calculated move
     */
    Coordinate getMove(ImmutableGameBoard gameBoard, int numInARowToWin);
}