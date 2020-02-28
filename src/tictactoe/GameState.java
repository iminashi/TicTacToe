package tictactoe;

/**
 * Represents the state of a Tic-Tac-Toe game.
 *
 * @author Tapio Malmberg
 */
public enum GameState {
    /**
     * A new move can be made.
     */
    READY_FOR_NEXT_MOVE,

    /**
     * The move the human player tried to make was invalid.
     */
    INVALID_PLAYER_MOVE,

    /**
     * The game is over and the human player won.
     */
    PLAYER_WON,

    /**
     * The game is over and the computer won.
     */
    COMPUTER_WON,

    /**
     * The game ended in a draw.
     */
    DRAW_GAME,

    /**
     * The user decided quit the game.
     */
    USER_QUIT
}