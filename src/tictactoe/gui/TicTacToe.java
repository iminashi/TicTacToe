package tictactoe.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import tictactoe.*;

/**
 * A game of Tic-Tac-Toe played with a GUI.
 *
 * @author Tapio Malmberg
 */
public class TicTacToe extends JFrame {
    /**
     * The game board panel.
     */
    private GameBoardPanel gamePanel;

    /**
     * The New Game button.
     */
    private JButton newGameBtn;

    /**
     * The Undo button.
     */
    private JButton undoBtn;

    /**
     * The TextField for the game board width.
     */
    private JTextField widthTextField;

    /**
     * The TextField for the game board height.
     */
    private JTextField heightTextField;

    /**
     * The TextField for the number pieces in a row needed to win.
     */
    private JTextField numToWinTextField;

    /**
     * The label for a message at the bottom of the window.
     */
    private JLabel messageLabel;

    /**
     * The Tic-Tac-Toe game.
     */
    private TicTacToeGame game;

    /**
     * The current state of the game.
     */
    private GameState gameState;

    /**
     * The width of the game board in number of squares.
     */
    private int gameWidth = TicTacToeGame.MIN_WIDTH;

    /**
     * The height of the game board in number of squares.
     */
    private int gameHeight = TicTacToeGame.MIN_HEIGHT;

    /**
     * The number of pieces in a row needed to win.
     */
    private int numInARowToWin = TicTacToeGame.MIN_IN_A_ROW_TO_WIN_SMALL;

    /**
     * Main program entry point.
     * @param args not used
     */
    public static void main(String[] args) {
        new TicTacToe();
    }

    /**
     * Panel that displays the game board and listens to user clicks.
     */
    private class GameBoardPanel extends JPanel {
        /**
         * The color of a game piece shadow.
         */
        private final Color SHADOW_COLOR = new Color(222, 207, 164);

        /**
         * A MouseAdapter for detecting where the user clicked.
         */
        private class ClickListener extends MouseAdapter {
            /**
             * Calculates the square the user clicked and
             * plays a round of Tic-Tac-Toe unless the game is over.
             * 
             * @param e the mouse event
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if(!isGameOver()) {
                    int squareX = (int)(e.getX() / getSquareWidth());
                    int squareY = (int)(e.getY() / getSquareHeight());

                    gameState = game.playARound(new Coordinate(squareX, squareY));
                    if(gameState != GameState.INVALID_PLAYER_MOVE) {
                        setComputerMoveMessage();
                        undoBtn.setEnabled(true);
                        repaint();
                    }
                }
            }
        }

        /**
         * Checks if the game is over.
         * @return true if the game is over
         */
        private boolean isGameOver() {
            return gameState == GameState.PLAYER_WON
                || gameState == GameState.COMPUTER_WON
                || gameState == GameState.DRAW_GAME;
        }

        /**
         * Creates a new GameBoardPanel.
         */
        public GameBoardPanel() {
            addMouseListener(new ClickListener());
            setBackground(new Color(247, 230, 184));
        }

        /**
         * Draws the game board, the game pieces and a possible game over message.
         * @param g the graphics context
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGrid(g);
            drawPieces(g, game.getGameBoard());

            if(isGameOver()) {
                drawGameOverMessage(g);
            }
        }

        /**
         * Draws the game board grid.
         * @param g the graphics context
         */
        private void drawGrid(Graphics g) {
            double squareWidth = getSquareWidth();
            double squareHeight = getSquareHeight();
            double x = squareWidth;
            double y = squareHeight;

            g.setColor(Color.DARK_GRAY);

            // Draw the vertical lines
            while (x < getWidth()) {
                int lineX = (int)Math.round(x);
                g.drawLine(lineX, 0, lineX, getHeight());
                x += squareWidth;
            }

            // Draw the horizontal lines
            while (y < getHeight()) {
                int lineY = (int)Math.round(y);
                g.drawLine(0, lineY, getWidth(), lineY);
                y += squareHeight;
            }
        }

        /**
         * Draws the game pieces of the player and the computer.
         * @param g the graphics context
         * @param gameBoard the game board
         */
        private void drawPieces(Graphics g, ImmutableGameBoard gameBoard) {
            double squareWidth = getSquareWidth();
            double squareHeight = getSquareHeight();
            int margin = (int)Math.min(squareWidth, squareHeight) / 20;

            for (int boardX = 0; boardX < gameWidth; boardX++) {
                for (int boardY = 0; boardY < gameHeight; boardY++) {
                    byte content = gameBoard.getSquare(boardX, boardY);
                    if(content != TicTacToeGame.EMPTY) {
                        int x = (int)Math.round(boardX * squareWidth + margin);
                        int y = (int)Math.round(boardY * squareHeight + margin);
                        int pieceWidth = (int)Math.round(squareWidth - margin*2);
                        int pieceHeight = (int)Math.round(squareHeight - margin*2);

                        // Draw a shadow, unless the pieces are very small
                        if(margin > 1) {
                            g.setColor(SHADOW_COLOR);
                            g.fillOval(x + margin - 1, y + margin - 1, pieceWidth, pieceHeight);
                        }

                        // Draw the game piece
                        g.setColor(content == TicTacToeGame.COMPUTER ? Color.WHITE : Color.BLACK);
                        g.fillOval(x, y, pieceWidth, pieceHeight);
                    }
                }
            }
        }

        /**
         * Draws an appropriate game over message at the center of the panel.
         * @param g the graphics context
         */
        private void drawGameOverMessage(Graphics g) {
            String message;
            Color textColor = Color.LIGHT_GRAY;
            switch(gameState) {
                case PLAYER_WON:
                    message = "You Win!";
                    textColor = Color.GREEN;
                    break;
                case COMPUTER_WON:
                    message = "You Lose!";
                    textColor = Color.RED;
                    break;
                case DRAW_GAME:
                    message = "Draw Game";
                    break;
                default: message = "";
            }
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
            int x = getWidth() / 2 - g.getFontMetrics().stringWidth(message) / 2;
            int y = getHeight() / 2;
            g.setColor(Color.BLACK);
            g.drawString(message, x, y);
            g.setColor(textColor);
            g.drawString(message, x - 2, y - 2);
        }

        /**
         * Calculates the width of a square on the game board panel.
         * @return the width of a square on the game board panel
         */
        private double getSquareWidth() {
            return (double)getWidth() / gameWidth;
        }

        /**
         * Calculates the height of a square on the game board panel.
         * @return the height of a square on the game board panel
         */
        private double getSquareHeight() {
            return (double)getHeight() / gameHeight;
        }
    }

    /**
     * Creates a new TicTacToe game window.
     */
    public TicTacToe() {
        newGameBtn = new JButton("New Game");
        newGameBtn.addActionListener(this::newGameClicked);

        undoBtn = new JButton("Undo");
        undoBtn.setEnabled(false);
        undoBtn.addActionListener(this::undoClicked);

        widthTextField = new JTextField(Integer.toString(gameWidth), 3);
        heightTextField = new JTextField(Integer.toString(gameHeight), 3);
        numToWinTextField = new JTextField(Integer.toString(numInARowToWin), 3);
        widthTextField.addActionListener(this::gameSizeChanged);
        heightTextField.addActionListener(this::gameSizeChanged);
        numToWinTextField.addActionListener(this::gameSizeChanged);
        
        JPanel controlsPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(6);
        flowLayout.setVgap(8);
        controlsPanel.setLayout(flowLayout);
        controlsPanel.add(newGameBtn);
        controlsPanel.add(widthTextField);
        controlsPanel.add(new JLabel("X"));
        controlsPanel.add(heightTextField);
        controlsPanel.add(new JLabel(":"));
        controlsPanel.add(numToWinTextField);
        controlsPanel.add(new JLabel("in a row wins."));
        controlsPanel.add(undoBtn);

        gamePanel = new GameBoardPanel();
        messageLabel = new JLabel(" ");

        add(controlsPanel, BorderLayout.NORTH);
        add(messageLabel, BorderLayout.SOUTH);
        add(gamePanel, BorderLayout.CENTER);

        createGame();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setTitle("N-In-A-Row");
        setVisible(true);
    }

    /**
     * Sets the value of {@code numInARowToWin} and its TextField.
     * @param value the value to set
     */
    private void setNumInARowToWin(int value) {
        numInARowToWin = value;
        numToWinTextField.setText(Integer.toString(value));
    }

    /**
     * Sets the value of {@code gameHeight} and its TextField.
     * @param value the value to set
     */
    private void setGameHeight(int value) {
        gameHeight = value;
        heightTextField.setText(Integer.toString(value));
    }

    /**
     * Sets the value of {@code gameWidth} and its TextField.
     * @param value the value to set
     */
    private void setGameWidth(int value) {
        gameWidth = value;
        widthTextField.setText(Integer.toString(value));
    }

    /**
     * Event handler for the new game button click.
     * @param e the event
     */
    private void newGameClicked(ActionEvent e) {
        newGame();
    }

    /**
     * Event handler for the game board size text fields.
     * @param e the event
     */
    private void gameSizeChanged(ActionEvent e) {
        newGame();
    }

    /**
     * Event handler for the undo button click.
     * @param e the event
     */
    private void undoClicked(ActionEvent e) {
        if(game.canUndo()) {
            gameState = game.undo();
            undoBtn.setEnabled(game.canUndo());
            gamePanel.repaint();
        }
    }

    /**
     * Initializes a new game.
     */
    private void newGame() {
        validateGameSettings();
        createGame();
        undoBtn.setEnabled(false);
        gamePanel.repaint();
    }

    /**
     * Updates and validates the game board settings
     * (width, height and number of pieces in a row to win).
     */
    private void validateGameSettings() {
        try {
            gameWidth = Integer.parseInt(widthTextField.getText());
            gameHeight = Integer.parseInt(heightTextField.getText());
            numInARowToWin = Integer.parseInt(numToWinTextField.getText());
        } catch(NumberFormatException ex) { }
        
        if(gameHeight < TicTacToeGame.MIN_HEIGHT) {
            setGameHeight(TicTacToeGame.MIN_HEIGHT);
        }
        if(gameWidth < TicTacToeGame.MIN_WIDTH) {
            setGameWidth(TicTacToeGame.MIN_WIDTH);
        }

        validateNumInARowToWin();
    }

    /**
     * Validates the value of the number of pieces in a row needed to win.
     */
    private void validateNumInARowToWin() {
        if(numInARowToWin > Math.max(gameWidth, gameHeight)) {
            // The number cannot be larger than the longest side of the game board
            setNumInARowToWin(Math.max(gameWidth, gameHeight));
        } else if(TicTacToeGame.isLargeGameBoard(gameWidth, gameHeight) && numInARowToWin < TicTacToeGame.MIN_IN_A_ROW_TO_WIN_LARGE) {
            // The number cannot be smaller than the minimum allowed (large game board)
            setNumInARowToWin(TicTacToeGame.MIN_IN_A_ROW_TO_WIN_LARGE);
        } else if(numInARowToWin < TicTacToeGame.MIN_IN_A_ROW_TO_WIN_SMALL) {
            // The number cannot be smaller than the minimum allowed (small game board)
            setNumInARowToWin(TicTacToeGame.MIN_IN_A_ROW_TO_WIN_SMALL);
        }
    }

    /**
     * Creates the TicTacToeGame and randomizes the starting player.
     */
    private void createGame() {
        boolean computerStarts = Utils.getRandom(0, 1) == 1;
        game = new TicTacToeGame(gameWidth, gameHeight, numInARowToWin, new SmartComputer(), computerStarts);
        gameState = GameState.READY_FOR_NEXT_MOVE;
        if(computerStarts) {
            setMessage("The computer gets to make the first move.");
        } else {
            setMessage("You get to make the first move.");
        }
    }

    /**
     * Shows the last move the computer made in the message at the bottom of the window.
     */
    private void setComputerMoveMessage() {
        Coordinate compMove = game.getLastComputerMove();
        if(compMove != null) {
            setMessage("The computer makes a move at " + (compMove.x+1) + ", " + (compMove.y+1));
        }
    }

    /**
     * Sets a message to be shown at the bottom of the window.
     * @param message the message
     */
    private void setMessage(String message) {
        messageLabel.setText(message);
    }
}