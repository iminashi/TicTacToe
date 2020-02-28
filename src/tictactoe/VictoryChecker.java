package tictactoe;

/**
 * Used to check if a player has won the game with the last move they made.
 *
 * @author Tapio Malmberg
 */
class VictoryChecker {
    /**
     * The player (HUMAN or COMPUTER).
     */
    private byte player;

    /**
     * The last moved the player made.
     */
    private Coordinate playedMove;

    /**
     * The game board.
     */
    private ImmutableGameBoard gameBoard;

    /**
     * The number of pieces in a row needed to win.
     */
    private int numToWin;

    /**
     * Creates a new VictoryChecker with the given attributes.
     * @param gameBoard the game board
     * @param player the player who made the move
     * @param playedMove the move made by the player 
     * @param numToWin the number of pieces in a row needed to win
     */
    public VictoryChecker(ImmutableGameBoard gameBoard, byte player, Coordinate playedMove, int numToWin) {
        this.gameBoard = gameBoard;
        this.player = player;
        this.playedMove = playedMove;
        this.numToWin = numToWin;
    }

    /**
     * Checks if the player associated with this VictoryChecker has won.
     * @return true if the player has won with their last move
     */
    public boolean hasWon() {
        for (Direction dir : Directions.ALL) {
            if(isWinningRow(dir))
                return true;
        }
        return false;
    }

    /**
     * Checks if there are enough adjacent pieces
     * in the given direction required for winning.
     * @param dir the direction to check
     * @return true if there are enough pieces in a row required for victory
     */
    private boolean isWinningRow(Direction dir) {
        // Forwards, start position included
        int nAdjacent = gameBoard.getNumAdjacent(playedMove, dir, player, true);

        // Backwards, start position excluded
        nAdjacent += gameBoard.getNumAdjacent(playedMove, dir.reverse(), player, false);

        return nAdjacent >= numToWin;
    }
}