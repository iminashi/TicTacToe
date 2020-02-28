package tictactoe;

/**
 * Represents an immutable direction in a two dimensional coordinate system.
 * 
 * @author Tapio Malmberg
 */
class Direction {
    /**
     * The x vector.
     */
    public final int xVec;

    /**
     * The y vector.
     */
    public final int yVec;

    /**
     * Creates a new Direction with the given x and y vectors.
     * @param xVec the x vector
     * @param yVec the y vector
     */
    public Direction(int xVec, int yVec) {
        this.xVec = xVec;
        this.yVec = yVec;
    }

    /**
     * Returns a {@code Direction} with the x and y vectors reversed.
     * @return the reversed direction
     */
    public Direction reverse() {
        return new Direction(-xVec, -yVec);
    }
}