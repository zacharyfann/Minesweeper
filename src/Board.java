
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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

public class Board extends JPanel implements MouseListener, ActionListener{
	
	private MinesweeperLogic mineLogic = new MinesweeperLogic();
	
	JFrame frame;
	
	//size of the frame
	private int width 	= 800;
	private int height 	= 800;

	public Board() {
		frame = new JFrame("Minesweeper");
		setup();
	}

	public void setup() {
		
		
		frame.setSize(width, height);
		setupBoard();
		addMenus();
		
		//add action for x button for a JFrame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.setResizable(false);		
		frame.getContentPane().setCursor(new Cursor(Cursor.HAND_CURSOR)); 
		
		//show the frame
		frame.setVisible(true);	
		
	}
	
	public void setupBoard() {
		JPanel jp = new JPanel();

		GridLayout g = new GridLayout(18,14);
		jp.setLayout(g);	


		Tile[][] board = mineLogic.getBoard();
		for(int i = 0; i < board.length;i++) {
			for(int j = 0; j < board[0].length;j++) {
				jp.add(board[i][j]);
				board[i][j].addMouseListener(this);
			}
		}
		
		frame.add(jp);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
 		
	}

	
	@Override
	public void mousePressed(MouseEvent e) {
		firstClick = !firstClick;


		if (firstClick) {
			//empty tile
			firstClickPiece = (Tile) e.getComponent();
			if(firstClickPiece.getPiece() == null) return;
			
			System.out.println(firstClickPiece.getPiece().getClass());
			firstClickPiece.setBorder(BorderFactory.createLineBorder(Color.green,2));
			
			firstClickPiece.setBorderPainted(true);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
					((ImageIcon) ((Tile) e.getComponent()).getIcon()).getImage(), new Point(0, 0), "blank cursor");
			frame.getContentPane().setCursor(blankCursor);
			
			boolean[][] validMoves = firstClickPiece.getPiece().moves();
			highlightAllMoves(validMoves);
			
		} else {
 			Tile curr = (Tile) e.getComponent(); 			
			if(firstClickPiece.getPiece()!=null && firstClickPiece.getPiece().validMove(firstClickPiece, curr)){
				System.out.println("Placing");
				swap(firstClickPiece, curr, firstClickPiece.getPiece().capture(curr));
			}
			firstClickPiece.setBorderPainted(false);
			frame.getContentPane().setCursor(new Cursor(Cursor.HAND_CURSOR));
			resetHighlights();
 		}
		
 		 
		
	}	
	
	public void highlightAllMoves(boolean[][] moves) {
		System.out.println("highlight possible moves");
		
		if(moves == null){
			System.out.println("moves was null for the piece");
			return;
		}

		Tile[][] board = chessLogic.getBoard();
		for(int r = 0; r < moves.length; r++) {
			for(int c = 0; c < moves[r].length; c++) {
				if(moves[r][c]){
					board[r][c].setBorder(BorderFactory.createLineBorder(Color.green,2));
					board[r][c].setBorderPainted(true);
				}
			}
		}
	}
	
	public void resetHighlights() {
		System.out.println("reset highlights for possible moves");
		Tile[][] board = chessLogic.getBoard();
		for(int r = 0; r < board.length; r++) {
			for(int c = 0; c < board[r].length; c++) {
				board[r][c].setBorderPainted(false);
			}
		}
	}
	
    /* swap objects a and b in board. Be sure to swap icons as well an update locations */
    public void swap(Tile a, Tile b, boolean capture) {

        Piece temp = a.getPiece();
        temp.addMove();

        if (!capture) {
            a.setPiece(b.getPiece()); 
        }else {
            a.setPiece(null);
        }

        b.setPiece(temp);

    }
	
	public void reset() {
		 frame.dispose(); // Close the current JFrame
		 new Board();
	}
	
	public void paint(Graphics g) {
		System.out.println("paint");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		repaint();
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
	
	public static void main(String[] args) {
		// Create an instance of the board
		new Board();

	}
}
