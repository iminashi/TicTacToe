package tictactoe;

/**
 * A {@code ComputerPlayer} that tries to block the player
 * and to make moves that are somewhat intelligent.
 *
 * <p>A priority is calculated for each square on the game board,
 * based on the number of game pieces in a row in each direction.
 * The square that has the highest priority will be selected.</p>
 *
 * @author Tapio Malmberg
 */
public class SmartComputer implements ComputerPlayer {
    /**
     * The game board.
     */
    private ImmutableGameBoard gameBoard;

    /**
     * The number of pieces in a row needed to win.
     */
    private int numInARowToWin;

    /**
     * The number of pieces in a row one move away from victory.
     */
    private int oneMoveToWin;

    /**
     * The number of pieces in a row two moves away from victory.
     */
    private int twoMovesToWin;

    /**
     * The calculated priority for each square on the game board.
     */
    private int[][] priorities;

    /**
     * The maximum calculated priority.
     */
    private int maxPriority;

    /**
     * Returns a Coordinate with the highest calculated priority.
     * @param gameBoard the game board
     * @param numInARowToWin the number of pieces in a row needed to win
     * @return a Coordinate with the highest calculated priority
     */
    @Override
    public Coordinate getMove(ImmutableGameBoard gameBoard, int numInARowToWin) {
        this.maxPriority = 0;
        this.gameBoard = gameBoard;
        this.numInARowToWin = numInARowToWin;
        this.oneMoveToWin = numInARowToWin - 1;
        this.twoMovesToWin = oneMoveToWin - 1;
        this.priorities = new int[gameBoard.getHeight()][gameBoard.getWidth()];

        addPrioritiesForBlockingMoves();
        addPrioritiesForPossibleMoves();

        // If maxPriority is zero, this is either the first move,
        // or winning the game is no longer possible
        if(maxPriority > 0)
            return getHighestPriorityMove();
        else
            return getMiddlePositionOrRandomMove();
    }

    /**
     * Returns the Coordinate of the square that has the highest priority.
     * 
     * <p>If there are many squares that have the highest priority,
     * the first one will be selected.</p>
     * @return the Coordinate of the square with the highest priority
     */
    private Coordinate getHighestPriorityMove() {
        // Could be randomized when there are many squares with maxPriority
        for (int x = 0; x < priorities[0].length; x++) {
            for (int y = 0; y < priorities.length; y++) {
                if(priorities[y][x] == maxPriority)
                    return new Coordinate(x, y);
            }
        }

        return null;
    }

    /**
     * Calculates the priorities for moves that block the player.
     */
    private void addPrioritiesForBlockingMoves() {
        for (Direction direction : Directions.ALL)
            calculatePriorities(direction, TicTacToeGame.PLAYER, 100, 20);
    }

    /**
     * Calculates the priorities for moves that help the computer win.
     */
    private void addPrioritiesForPossibleMoves() {
        // Uses higher "boost" values because if a winning move is available,
        // the computer should take it and not try to block the player.
        for (Direction direction : Directions.ALL)
            calculatePriorities(direction, TicTacToeGame.COMPUTER, 300, 40);
    }

    /**
     * Calculates a priority for each square based on the number of pieces in a row in
     * the given direction for the given player.
     * @param dir the direction for counting the adjacent pieces
     * @param player the human player or the computer player
     * @param largeBoost the value added to the priority of a square that is one move away from victory
     * @param smallBoost the value added to the priority of a square that is two moves away from victory
     */
    private void calculatePriorities(Direction dir, byte player, int largeBoost, int smallBoost) {
        for (int y = 0; y < gameBoard.getHeight(); y++) {
            for (int x = 0; x < gameBoard.getWidth(); x++) {
                if(!isAvailable(x, y))
                    continue;

                Coordinate thisPos = new Coordinate(x, y);
                Direction reverseDir = dir.reverse();

                // Calculate the number of adjacent pieces, forwards and backwards
                int adjacentForward = gameBoard.getNumAdjacent(thisPos, dir, player, false);
                int adjacentReverse = gameBoard.getNumAdjacent(thisPos, reverseDir, player, false);
                int priority = adjacentForward + adjacentReverse;
                if(priority == 0)
                    continue;

                // Calculate the space available after the pieces, forwards and backwards
                int spaceAvailableFw = gameBoard.getNumAdjacent(thisPos.move(dir, adjacentForward), dir, TicTacToeGame.EMPTY, false);
                int spaceAvailableRev = gameBoard.getNumAdjacent(thisPos.move(reverseDir, adjacentReverse), reverseDir, TicTacToeGame.EMPTY, false);

                // Discard this move if there is not enough space available for a winning row
                if(1 + spaceAvailableFw + spaceAvailableRev + adjacentForward + adjacentReverse < numInARowToWin)
                    continue;

                if(priority >= oneMoveToWin) {
                    priority += largeBoost;
                } else if(priority == twoMovesToWin && (spaceAvailableFw != 0 && spaceAvailableRev != 0)) {
                    // Boost the priority if both ends of the row of pieces are open
                    // In this case it is vital that the human player is blocked
                    priority += smallBoost;
                }

                increaseSquarePriority(x, y, priority);
            }
        }
    }

    /**
     * Increases the priority of the square at the (x, y) coordinate by the given amount
     * and keeps track of the current maximum priority.
     * @param x the x value of the square on the game board
     * @param y the y value of the square on the game board
     * @param priority the amount the priority of the square will be increased.
     */
    private void increaseSquarePriority(int x, int y, int priority) {
        int newPriority = priorities[y][x] + priority;
        priorities[y][x] = newPriority;
        if(newPriority > maxPriority)
            maxPriority = newPriority;
    }

    /**
     * Tests whether a square on the game board is available.
     * @param x the x value of the square on the game board
     * @param y the y value of the square on the game board
     * @return true if the square at (x, y) is within the game board and it is empty
     */
    private boolean isAvailable(int x, int y) {
        return gameBoard.isWithinBounds(x, y) && gameBoard.getSquare(x, y) == TicTacToeGame.EMPTY;
    }

    /**
     * Either returns the middle square of the game board,
     * or if it is taken, a random square utilizing RandomComputer.
     * @return the middle square or a random square
     */
    private Coordinate getMiddlePositionOrRandomMove() {
        Coordinate middle = new Coordinate(gameBoard.getWidth() / 2, gameBoard.getHeight() / 2);

        if (isAvailable(middle.x, middle.y))
            return middle;
        else
            return new RandomComputer().getMove(gameBoard, numInARowToWin);
    }
}