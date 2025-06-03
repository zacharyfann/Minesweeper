import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.SwingConstants;

public class Board extends JPanel implements MouseListener{
	
	private MinesweeperLogic mineLogic;
	private Tile[][] board;
	private JFrame frame;
	private Timer gameTimer;
	private int timeElapsed = 0;
	private boolean gameStarted = false;
	private boolean gameOver = false;
	
	//size of the frame
	private int rows = 14;
	private int cols = 18;
	private int mineCount = 40;
	private int width 	= cols*50;
	private int height 	= rows*50 + 50; //menu bar

	private Color[] startColors = {
		new Color(11196241), new Color(10670409)
	};
	private Color[] endColors = {
		new Color(15057567), new Color(14137497)
	};

	private ImageIcon flagIcon;

	public Board() {
		frame = new JFrame("Minesweeper");
		mineLogic = new MinesweeperLogic(rows, cols, mineCount);
		showTitleScreen();
		String absPath = System.getProperty("user.dir") + "\\bin\\images\\flag.png";
		flagIcon = new ImageIcon(new ImageIcon(absPath).getImage().getScaledInstance(45, 45, java.awt.Image.SCALE_SMOOTH));		
	}

	private void showTitleScreen() {
		frame.getContentPane().removeAll();
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new java.awt.BorderLayout());
		
		String absPath = System.getProperty("user.dir") + "\\bin\\images\\title-screen.png";
		ImageIcon titleIcon = new ImageIcon(new ImageIcon(absPath).getImage().getScaledInstance(900, 700, java.awt.Image.SCALE_SMOOTH));
		JLabel imageLabel = new JLabel(titleIcon);
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titlePanel.add(imageLabel, java.awt.BorderLayout.CENTER);
		JButton startButton = new JButton("Start");
		startButton.setFont(new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 32));
		startButton.setFocusPainted(false);
		startButton.addActionListener(e -> {
			setup();
		});
		titlePanel.add(startButton,java.awt.BorderLayout.SOUTH);
		frame.setContentPane(titlePanel);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public void setup() {
		frame.getContentPane().removeAll();
		frame.setSize(width, height);
		setupBoard();
		addMenus();
		gameTimer = new Timer(1000, e -> {
			if (gameStarted && !gameOver) {
				timeElapsed++;
				updateTitle();
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
		updateTitle();
		frame.setVisible(true);	
		
	}
	
	public void setupBoard() {
		frame.getContentPane().removeAll();

		JPanel gamePanel = new JPanel();
		GridLayout gridLayout = new GridLayout(rows, cols);	
		gamePanel.setLayout(gridLayout);
		gamePanel.setBackground(Color.GRAY);

		board = mineLogic.getBoard();

		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				board[i][j].addMouseListener(this);
				board[i][j].setFont(getFont().deriveFont(12.0f));
				board[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				gamePanel.add(board[i][j]);
			}
		}
		
		frame.add(gamePanel);
		frame.revalidate();
		frame.repaint();
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		if (gameOver) return;
		// showAllMines(); //for testing
		// mineLogic.testMineLocations(); //for testing
 		
		Tile clickedTile = (Tile) e.getComponent();
		int row = clickedTile.getRow();
		int col = clickedTile.getCol();

		if(!gameStarted) {
			mineLogic.ensureSafety(row, col);
			gameStarted = true;
			gameTimer.start();
		}

		if(e.getButton() == MouseEvent.BUTTON1) {
			if (!clickedTile.isFlagged()) {
				revealTile(row, col);
			}
		} else if(e.getButton() == MouseEvent.BUTTON3 && gameStarted) {
			if(!clickedTile.isRevealed()){
				toggleFlag(clickedTile);
			}
		} else if(e.getButton() == MouseEvent.BUTTON2) {
			// System.out.println("Middle clicked" + row + ", " + col);
			if(clickedTile.isRevealed() && mineLogic.getAdjacentFlagCount(row, col) == mineLogic.getAdjacentMineCount(row, col)) {
				revealAdjacentTiles(row, col);
			}
		}
		checkWinCondition();
	}

	private void revealTile(int row, int col) {
		// System.out.println("asdfasdf");
		if(mineLogic.revealTile(row, col)){
			gameOver = true;
			gameTimer.stop();
			showAllMines();
			board[row][col].setBackground(Color.RED);
			JOptionPane.showMessageDialog(frame, "Game Over! You hit a mine.");
		} else {
			updateTileDisplay(row, col);
			if(mineLogic.getAdjacentMineCount(row, col) == 0){
				revealAdjacentTiles(row, col);
			}
		}
	}

	private void revealAdjacentTiles(int row, int col) {
		for(int i = -1; i <= 1; i++){
			for(int j = -1; j <= 1; j++){
				if(i == 0 && j == 0) continue;
				int newRow = row + i;
				int newCol = col + j;
				if(mineLogic.isValidPosition(newRow, newCol) && !board[newRow][newCol].isRevealed() && !board[newRow][newCol].isFlagged()){
					revealTile(newRow, newCol);
				}
			}
		}
	}

	private void toggleFlag(Tile tile) {
		// System.out.println(tile.getRow() + ", " + tile.getCol() + " flagged: " + tile.isFlagged());
		if(!tile.isFlagged()){
			tile.setIcon(flagIcon); 
			tile.setText("");
			tile.setFlagged(true);
			mineLogic.setFlagged(tile.getRow(), tile.getCol(), true);
		} else {
			tile.setIcon(null); 
			tile.setText("");
			tile.setFlagged(false);
			mineLogic.setFlagged(tile.getRow(), tile.getCol(), false);
		}
		updateTitle();
	}

	private void updateTileDisplay(int row, int col) {
		Tile tile = board[row][col];
		tile.setRevealed(true);
		int adjacentMines = mineLogic.getAdjacentMineCount(row, col);

		if(adjacentMines > 0){
			tile.setText(String.valueOf(adjacentMines));
			tile.setEnabled(true);
			tile.setForeground(getColorForNumber(adjacentMines));
			tile.setFont(tile.getFont().deriveFont(27.0f));
		}
		tile.setBackground(endColors[(row + col) % 2]);
		// tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	private Color getColorForNumber(int number) {
		switch(number){
			case 1: return Color.BLUE;
			case 2: return Color.GREEN;
			case 3: return Color.RED;
			case 4: return new Color(128, 0, 128);
			case 5: return new Color(128, 0, 0);
			case 6: return new Color(0, 128, 128);
			case 7: return new Color(0, 0, 128);
			case 8: return new Color(0, 0, 0);
			default: return Color.BLACK;
		}
	}
	private void showAllMines() {
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(mineLogic.isMine(i, j)){
					board[i][j].setText("💣");
					board[i][j].setBackground(Color.RED);
				}
			}
		}
	}

	private void checkWinCondition() {
		if(!gameOver && mineLogic.isGameWon()){
			gameOver = true;
			gameTimer.stop();

			for(int i = 0; i < board.length; i++){
				for(int j = 0; j < board[i].length; j++){
					if(mineLogic.isMine(i, j) && !board[i][j].isFlagged()){
						toggleFlag(board[i][j]);
					}
				}
			}

			JOptionPane.showMessageDialog(frame, "Congratulations! You won the game.");
		}
	}

	private void updateTitle() {
		int flagsRemaining = mineCount - mineLogic.getFlagCount();
		frame.setTitle("Minesweeper - Time: " + timeElapsed + "s | Flags Remaining: " + flagsRemaining);
	}

	private void reset() {
		gameOver = false;
		gameStarted = false;
		timeElapsed = 0;
		gameTimer.stop();

		mineLogic = new MinesweeperLogic(rows, cols, mineCount);
		setupBoard();
		updateTitle();
	}

	
	public void addMenus() {
		//Where the GUI is created:
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
		        "The only menu in this program that has menu items");
		menuBar.add(menu);

		//a group of JMenuItems
		menuItem = new JMenuItem("New Game");
 
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "Reset the game back to new game");
		menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle the click event for New Game
               reset();
            }
        });
		menu.add(menuItem);

		menuItem = new JMenuItem("Undo",
		                         new ImageIcon("images/middle.gif"));
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);

		menuItem = new JMenuItem("Quit", new ImageIcon("images/middle.gif"));
		menuItem.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Define an array of custom options for the dialog
		        Object[] options = { "Yes", "Cancel" };

		        // Display an option dialog with custom options
		        // The user's choice is stored in the 'choice'
		        // variable
		        int choice = JOptionPane.showOptionDialog(
		            null, // Parent component (null means center on screen)
		            "Do you want to proceed?", // Message to display
		            "Quit the Game", // Dialog title
		            JOptionPane.YES_NO_CANCEL_OPTION, // Option type (Yes, No, Cancel)
		            JOptionPane.QUESTION_MESSAGE, // Message type (question icon)
		            null, // Custom icon (null means no custom icon)
		            options, // Custom options array
		            options[1] // Initial selection (default is "Cancel")
		        );

		        // Check the user's choice and display a
		        // corresponding message
		        if (choice == JOptionPane.YES_OPTION) {
		            // If the user chose 'Yes'
		            // show a message indicating that they are
		            // proceeding
 		            System.exit(0);
		        }
		        else {
		            // If the user chose 'Cancel' or closed the
		            // dialog
		            // show a message indicating the operation is
		            // canceled
		            JOptionPane.showMessageDialog(null, "Operation canceled.");
		        }
			}	
		});
 		menu.add(menuItem);
		frame.setJMenuBar(menuBar);
	}

	@Override
    public void mousePressed(MouseEvent e) {
	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}


    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
       
    }
}
