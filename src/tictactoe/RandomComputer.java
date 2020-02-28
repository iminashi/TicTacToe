package tictactoe;

import java.util.*;

/**
 * A {@code ComputerPlayer} that generates random moves at available squares.
 *
 * @author Tapio Malmberg
 */
public class RandomComputer implements ComputerPlayer {
    /**
     * Returns a random Coordinate of an available square on the game board.
     * @param gameBoard the game board
     * @param numInARowToWin not used by this algorithm
     * @return a random Coordinate of an available square
     */
    @Override
    public Coordinate getMove(ImmutableGameBoard gameBoard, int numInARowToWin) {
        List<Coordinate> availableMoves = getAvailableMoves(gameBoard);
        int randomIndex = Utils.getRandom(0, availableMoves.size() - 1);

        return availableMoves.get(randomIndex);
    }

    /**
     * Returns a list of all the empty squares on the game board.
     * @param gameBoard the game board
     * @return a List of Coordinates of empty squares on the game board
     */
    private List<Coordinate> getAvailableMoves(ImmutableGameBoard gameBoard) {
        List<Coordinate> availableMoves = new ArrayList<Coordinate>();

        for (int x = 0; x < gameBoard.getWidth(); x++) {
            for (int y = 0; y < gameBoard.getHeight(); y++) {
                if(gameBoard.getSquare(x, y) == TicTacToeGame.EMPTY)
                    availableMoves.add(new Coordinate(x, y));
            }
        }

        return availableMoves;
    }
}