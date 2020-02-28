package tictactoe;

/**
 * Represents an immutable coordinate in a two dimensional system.
 * 
 * @author Tapio Malmberg
 */
public class Coordinate {
    /**
     * The X value.
     */
    public final int x;
    
    /**
     * The Y value.
     */
    public final int y;

    /**
     * Creates a new Coordinate at ({@code x}, {@code y}).
     * @param x the x value of the coordinate
     * @param y the y value of the coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns true if the coordinates have the same x and y values.
     * @param other the coordinate to compare to
     * @return true if the coordinates are the same
     */
    public boolean equals(Coordinate other) {
        if (other == null)
            return false;
        else
            return this.x == other.x && this.y == other.y;
    }

    /**
     * Moves a coordinate into the given direction by one x and/or y value.
     * @param dir the direction to move to
     * @return the coordinate of the new position
     */
    public Coordinate move(Direction dir) {
        return move(dir, 1);
    }

    /**
     * Moves a coordinate into the given direction {@code times} times.
     * @param dir the direction to move to
     * @param times how many steps the coordinate is moved
     * @return the coordinate of the new position
     */
    public Coordinate move(Direction dir, int times) {
        return new Coordinate(x + dir.xVec * times, y + dir.yVec * times);
    }
}