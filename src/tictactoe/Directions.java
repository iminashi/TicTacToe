package tictactoe;

/**
 * Contains four different directions:
 * Vertical, horizontal, diagonal and reverse diagonal
 * 
 * @author Tapio Malmberg
 */
class Directions {
    /**
     * Vertical direction: x value is increased.
     */
    public static final Direction VERTICAL = new Direction(1, 0);

    /**
     * Horizontal direction: y value is increased.
     */
    public static final Direction HORIZONTAL = new Direction(0, 1);

    /**
     * Downwards diagonal direction (from upper left to lower right): x and y values are increased.
     */
    public static final Direction DIAGONALLY_DOWNWARDS = new Direction(1, 1);

    /**
     * Upwards diagonal direction (from lower left to upper right): x is increased, y is decreased.
     */
    public static final Direction DIAGONALLY_UPWARDS = new Direction(1, -1);

    /**
     * An array that contains all the four directions.
     */
    public static final Direction[] ALL = { VERTICAL, HORIZONTAL, DIAGONALLY_DOWNWARDS, DIAGONALLY_UPWARDS };
}