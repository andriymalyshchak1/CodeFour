import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Connect Four UI - Drag and drop interface
 */
public class ConnectFourUI extends JFrame {
    private ConnectFourGame game;
    private JPanel boardPanel;
    private JLabel statusLabel;
    private JLabel[][] cellLabels;
    private JButton resetButton;
    private int hoveredColumn = -1;
    
    private static final Color BOARD_COLOR = new Color(0x1E88E5);
    private static final Color PLAYER1_COLOR = new Color(0xE53935); // Red
    private static final Color PLAYER2_COLOR = new Color(0x1976D2); // Blue
    private static final Color EMPTY_COLOR = new Color(0xFAFAFA);
    private static final Color GRID_COLOR = new Color(0x1565C0);
    
    public ConnectFourUI() {
        game = new ConnectFourGame();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Connect Four - Two Player Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Create board panel
        boardPanel = createBoardPanel();
        add(boardPanel, BorderLayout.CENTER);
        
        // Create control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        header.setBackground(Color.WHITE);
        
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Arial", Font.BOLD, 22));
        updateStatus();
        
        header.add(statusLabel);
        return header;
    }
    
    private JPanel createBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(game.getRows() + 1, game.getCols(), 5, 5));
        panel.setBackground(BOARD_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        cellLabels = new JLabel[game.getRows()][game.getCols()];
        
        // Create column headers for hover/drop
        for (int col = 0; col < game.getCols(); col++) {
            final int columnIndex = col;
            JLabel headerLabel = new JLabel("", SwingConstants.CENTER);
            headerLabel.setPreferredSize(new Dimension(90, 40));
            headerLabel.setBackground(new Color(0x0D47A1));
            headerLabel.setOpaque(true);
            headerLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
            headerLabel.setForeground(Color.WHITE);
            headerLabel.setText("Col " + columnIndex);
            
            // Add hover listeners
            headerLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!game.isGameOver()) {
                        hoveredColumn = columnIndex;
                        updateColumnHighlight();
                    }
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    hoveredColumn = -1;
                    updateColumnHighlight();
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleColumnClick(columnIndex);
                }
            });
            
            headerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.add(headerLabel);
        }
        
        // Create cell labels
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setPreferredSize(new Dimension(90, 90));
                cell.setBackground(EMPTY_COLOR);
                cell.setOpaque(true);
                cell.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 3));
                cell.setFont(new Font("Arial", Font.BOLD, 36));
                
                cellLabels[row][col] = cell;
                panel.add(cell);
            }
        }
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(Color.WHITE);
        
        resetButton = new JButton("New Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setPreferredSize(new Dimension(120, 40));
        resetButton.setBackground(new Color(0x4CAF50));
        resetButton.setForeground(Color.WHITE);
        resetButton.setBorderPainted(false);
        resetButton.setFocusPainted(false);
        
        resetButton.addActionListener(e -> resetGame());
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                resetButton.setBackground(new Color(0x45A049));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                resetButton.setBackground(new Color(0x4CAF50));
            }
        });
        
        panel.add(resetButton);
        
        JLabel infoLabel = new JLabel("Board: 4x4 • Win: 4 in a row • Hover over column headers to play");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        panel.add(infoLabel);
        
        return panel;
    }
    
    private void updateColumnHighlight() {
        // Reset all column headers
        for (int col = 0; col < game.getCols(); col++) {
            Component comp = boardPanel.getComponent(col);
            if (comp instanceof JLabel) {
                JLabel header = (JLabel) comp;
                if (col == hoveredColumn && !game.isGameOver()) {
                    // Highlight with current player's color
                    int currentPlayer = game.getCurrentPlayer();
                    Color highlightColor = (currentPlayer == 1) ? PLAYER1_COLOR : PLAYER2_COLOR;
                    header.setBackground(highlightColor);
                    header.setText("DROP " + currentPlayer);
                } else {
                    header.setBackground(new Color(0x0D47A1));
                    header.setText("Col " + col);
                }
            }
        }
        
        boardPanel.repaint();
    }
    
    private void handleColumnClick(int col) {
        if (game.isGameOver()) {
            return;
        }
        
        if (game.dropToken(col)) {
            updateBoard();
            updateStatus();
            hoveredColumn = -1;
            updateColumnHighlight();
        } else {
            // Show feedback that column is full
            Toolkit.getDefaultToolkit().beep();
        }
    }
    
    private void updateBoard() {
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                int token = game.getToken(row, col);
                JLabel cell = cellLabels[row][col];
                
                if (token == 1) {
                    // Player 1 - Red circle
                    cell.setBackground(EMPTY_COLOR);
                    cell.setForeground(PLAYER1_COLOR);
                    cell.setText("O");
                    cell.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 3));
                } else if (token == 2) {
                    // Player 2 - Blue circle
                    cell.setBackground(EMPTY_COLOR);
                    cell.setForeground(PLAYER2_COLOR);
                    cell.setText("O");
                    cell.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 3));
                } else {
                    cell.setBackground(EMPTY_COLOR);
                    cell.setText("");
                    cell.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 3));
                }
            }
        }
    }
    
    private void updateStatus() {
        if (game.isGameOver()) {
            int winner = game.getWinner();
            if (winner == 1) {
                statusLabel.setText("Player 1 wins!");
                statusLabel.setForeground(PLAYER1_COLOR);
            } else if (winner == 2) {
                statusLabel.setText("Player 2 wins!");
                statusLabel.setForeground(PLAYER2_COLOR);
            } else {
                statusLabel.setText("It's a Tie!");
                statusLabel.setForeground(Color.GRAY);
            }
        } else {
            int currentPlayer = game.getCurrentPlayer();
            Color playerColor = (currentPlayer == 1) ? PLAYER1_COLOR : PLAYER2_COLOR;
            statusLabel.setText(String.format("Player %d's Turn - Click column header to drop token", currentPlayer));
            statusLabel.setForeground(playerColor);
        }
    }
    
    private void resetGame() {
        game.reset();
        updateBoard();
        updateStatus();
        hoveredColumn = -1;
        updateColumnHighlight();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new ConnectFourUI();
        });
    }
}
