package tictactoe;

/**
 * Represents an immutable pair of moves
 * made by the human player and the computer.
 *
 * @author Tapio Malmberg
 */
class MovePair {
    /**
     * The Coordinate of the move the player has made.
     */
    public final Coordinate playerMove;
    
    /**
     * The Coordinate of the move the computer has made.
     */
    public final Coordinate computerMove;

    /**
     * Creates a new MovePair with the two given Coordinates.
     * @param playerMove the Coordinate of the move the player has made
     * @param computerMove the Coordinate of the move the computer has made
     */
    public MovePair(Coordinate playerMove, Coordinate computerMove) {
        this.playerMove = playerMove;
        this.computerMove = computerMove;
    }
}