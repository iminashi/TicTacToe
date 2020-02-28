package tictactoe;

/**
 * Represents a 2D game board that can be edited.
 *
 * @author Tapio Malmberg
 */
class GameBoard extends ImmutableGameBoard {
    /**
     * Creates a new GameBoard with the given width and height.
     * @param width the width of the game board, must be greater than zero
     * @param height the height of the game board, must be greater than zero
     */
    public GameBoard(int width, int height) {
        super(width, height);
    }

    /**
     * Fills the whole game board with the given value.
     * @param value the value that the game board will be filled with
     */
    public void fillBoard(byte value) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                setSquare(x, y, value);
            }
        }
    }

    /**
     * Sets the contents of the game board square at the given coordinate to {@code value}.
     * @param pos the coordinate of the square to be changed
     * @param value the value that the square will be set to
     */
    public void setSquare(Coordinate pos, byte value) {
        setSquare(pos.x, pos.y, value);
    }

    /**
     * Sets the contents of the game board square at the given x, y coordinate to {@code value}.
     * @param x the x value of the game board square
     * @param y the y value of the game board square
     * @param value the value that the square will be set to
     */
    public void setSquare(int x, int y, byte value) {
        board[y][x] = value;
    }

    /**
     * Returns this game board as an immutable game board that cannot be edited.
     * @return this game board as an immutable game board
     */
    public ImmutableGameBoard asImmutable() {
        return new ImmutableGameBoard(this);
    }
}