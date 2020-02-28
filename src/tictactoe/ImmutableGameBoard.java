package tictactoe;

/**
 * Represents a read-only 2D game board.
 *
 * @author Tapio Malmberg
 */
public class ImmutableGameBoard {
    /**
     * The squares of the game board.
     */
    protected byte[][] board;

    /**
     * Creates a new ImmutableGameBoard with the given width and height.
     * @param width the width of the game board, must be greater than zero
     * @param height the height of the game board, must be greater than zero
     */
    public ImmutableGameBoard(int width, int height) {
        if(width <= 0)
            throw new IllegalArgumentException("Width must be greater than zero.");
        if(height <= 0)
            throw new IllegalArgumentException("Height must be greater than zero.");

        board = new byte[height][width];
    }

    /**
     * Creates a new ImmutableGameBoard from an ImmutableGameBoard
     * subclass without copying data.
     * @param other the other ImmutableGameBoard
     */
    protected ImmutableGameBoard(ImmutableGameBoard other) {
        board = other.board;
    }

    /**
     * Returns the width of the game board.
     * @return the width of the game board
     */
    public int getWidth() {
        return board[0].length;
    }

    /**
     * Returns the height of the game board.
     * @return the height of the game board
     */
    public int getHeight() {
        return board.length;
    }

    /**
     * Checks if the given {@code Coordinate} is within the game board.
     * @param pos the coordinate to test
     * @return true if the given coordinate is within the game board
     */
    public boolean isWithinBounds(Coordinate pos) {
        return isWithinBounds(pos.x, pos.y);
    }

    /**
     * Checks if the given (x, y) coordinate is within the game board.
     * @param x the x value of the coordinate
     * @param y the y value of the coordinate
     * @return true if the given coordinate is within the game board
     */
    public boolean isWithinBounds(int x, int y) {
        return (x >= 0 && x < getWidth()) && (y >= 0 && y < getHeight());
    }

    /**
     * Returns the content of the square at the given {@code Coordinate}.
     * @param pos the coordinate of the square to get
     * @return the content of the square at the coordinate
     */
    public byte getSquare(Coordinate pos) {
        return getSquare(pos.x, pos.y);
    }

    /**
     * Returns the content of the square at the given (x, y) coordinate.
     * @param x the x value of the coordinate
     * @param y the y value of the coordinate
     * @return the content of the square at the coordinate
     */
    public byte getSquare(int x, int y) {
        return board[y][x];
    }

    /**
     * Counts the number of adjacent pieces of the given type in the given direction,
     * starting from the given coordinate.
     * 
     * <p>If {@code includeStartPos} is set to true, the starting position
     * is included in the count.</p>
     * @param startPos the Coordinate of the starting position
     * @param dir the direction that will be moved to
     * @param type the type of square contents that must be matched
     * @param includeStartPos whether the starting position is included
     * @return the number of adjacent pieces in the given direction
     */
    public int getNumAdjacent(Coordinate startPos, Direction dir, byte type, boolean includeStartPos) {
        int nAdjacent = 0;
        Coordinate pos = includeStartPos ? startPos : startPos.move(dir);

        while(isWithinBounds(pos) && getSquare(pos) == type) {
            ++nAdjacent;
            pos = pos.move(dir);
        }

        return nAdjacent;
    }
}