/**
 * Connect Four Game Logic
 * This class handles the game state, moves, and win detection
 */
public class ConnectFourGame {
    private static final int ROWS = 4;
    private static final int COLS = 4;
    private static final int WIN_LENGTH = 4;
    
    private int[][] board;
    private int currentPlayer; // 1 for player 1, 2 for player 2
    private int winner;
    private boolean gameOver;
    private int movesCount;
    
    public ConnectFourGame() {
        board = new int[ROWS][COLS];
        currentPlayer = 1;
        winner = 0;
        gameOver = false;
        movesCount = 0;
    }
    
    public int getRows() {
        return ROWS;
    }
    
    public int getCols() {
        return COLS;
    }
    
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public int getWinner() {
        return winner;
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    /**
     * Attempts to drop a token in the specified column
     * @param col Column index (0-based)
     * @return true if the move is valid and successful, false otherwise
     */
    public boolean dropToken(int col) {
        if (gameOver || col < 0 || col >= COLS) {
            return false;
        }
        
        // Find the lowest available row in the column
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = currentPlayer;
                movesCount++;
                checkGameState(row, col);
                if (!gameOver) {
                    currentPlayer = (currentPlayer == 1) ? 2 : 1;
                }
                return true;
            }
        }
        
        return false; // Column is full
    }
    
    /**
     * Checks if the game is over (win or tie)
     */
    private void checkGameState(int row, int col) {
        if (checkWin(row, col)) {
            gameOver = true;
            winner = currentPlayer;
        } else if (movesCount == ROWS * COLS) {
            gameOver = true;
            winner = 0; // Tie
        }
    }
    
    /**
     * Checks if the last move resulted in a win
     * Checks horizontal, vertical, and both diagonals
     */
    private boolean checkWin(int row, int col) {
        return checkHorizontal(row, col) || 
               checkVertical(row, col) || 
               checkDiagonal(row, col) || 
               checkAntiDiagonal(row, col);
    }
    
    /**
     * Check for horizontal win (5 consecutive tokens)
     */
    private boolean checkHorizontal(int row, int col) {
        int count = 1;
        
        // Check to the right
        for (int c = col + 1; c < COLS && board[row][c] == currentPlayer && count < WIN_LENGTH; c++) {
            count++;
        }
        
        // Check to the left
        for (int c = col - 1; c >= 0 && board[row][c] == currentPlayer && count < WIN_LENGTH; c--) {
            count++;
        }
        
        return count >= WIN_LENGTH;
    }
    
    /**
     * Check for vertical win (5 consecutive tokens)
     */
    private boolean checkVertical(int row, int col) {
        int count = 1;
        
        // Check downward
        for (int r = row + 1; r < ROWS && board[r][col] == currentPlayer && count < WIN_LENGTH; r++) {
            count++;
        }
        
        // Check upward
        for (int r = row - 1; r >= 0 && board[r][col] == currentPlayer && count < WIN_LENGTH; r--) {
            count++;
        }
        
        return count >= WIN_LENGTH;
    }
    
    /**
     * Check for diagonal win (5 consecutive tokens) - top-left to bottom-right
     */
    private boolean checkDiagonal(int row, int col) {
        int count = 1;
        
        // Check down-right
        for (int r = row + 1, c = col + 1; r < ROWS && c < COLS && board[r][c] == currentPlayer && count < WIN_LENGTH; r++, c++) {
            count++;
        }
        
        // Check up-left
        for (int r = row - 1, c = col - 1; r >= 0 && c >= 0 && board[r][c] == currentPlayer && count < WIN_LENGTH; r--, c--) {
            count++;
        }
        
        return count >= WIN_LENGTH;
    }
    
    /**
     * Check for anti-diagonal win (5 consecutive tokens) - top-right to bottom-left
     */
    private boolean checkAntiDiagonal(int row, int col) {
        int count = 1;
        
        // Check down-left
        for (int r = row + 1, c = col - 1; r < ROWS && c >= 0 && board[r][c] == currentPlayer && count < WIN_LENGTH; r++, c--) {
            count++;
        }
        
        // Check up-right
        for (int r = row - 1, c = col + 1; r >= 0 && c < COLS && board[r][c] == currentPlayer && count < WIN_LENGTH; r--, c++) {
            count++;
        }
        
        return count >= WIN_LENGTH;
    }
    
    /**
     * Resets the game to initial state
     */
    public void reset() {
        board = new int[ROWS][COLS];
        currentPlayer = 1;
        winner = 0;
        gameOver = false;
        movesCount = 0;
    }
    
    /**
     * Get the token at the specified position
     */
    public int getToken(int row, int col) {
        return board[row][col];
    }
}

