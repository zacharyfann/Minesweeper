//import java.awt.Color;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class Piece {

	// position of a Piece in the 8x8 board
	protected int row;
	protected int col;
	
	protected COLOR color; 
	protected static int num = 0;		//what might this be useful for?!
	protected ImageIcon icon;

	protected int moves = 0;	//has the piece moved? useful for pawns and castling
	
	protected Tile[][] board;			//Piece has a reference to the board

	public Piece(String fileName, COLOR color, int row, int col, Tile[][] board) {

 		this.color = color;
		this.row = row;
		this.col = col;
		setIcon(fileName);
		this.board = board;
		
	}

	//getters and setters for moves
	public int getMoves() {
		return moves;
	}

	public void addMove() {
		this.moves++;
	}
	
	public void setIcon(String fileName) {
	    //Icon setup
		icon = new ImageIcon("imgs/"+fileName);
		Image img = ((ImageIcon) icon).getImage() ; 
		Image newimg = img.getScaledInstance( 100, 100,  java.awt.Image.SCALE_FAST) ;
		icon = new ImageIcon( img );
		
	}
	

	 private static ImageIcon createScaledIcon(Image img, int width, int height) {
	        // Create a blank BufferedImage with desired dimensions
	        BufferedImage centeredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = centeredImage.createGraphics();

	        // Scale the image proportionally
	        int scaledWidth = width;
	        int scaledHeight = height;

	        int originalWidth = img.getWidth(null);
	        int originalHeight = img.getHeight(null);

	        if (originalWidth > 0 && originalHeight > 0) {
	            double aspectRatio = (double) originalWidth / originalHeight;

	            if (width / aspectRatio <= height) {
	                scaledHeight = (int) (width / aspectRatio);
	            } else {
	                scaledWidth = (int) (height * aspectRatio);
	            }
	        }

	        // Calculate the top-left corner for centering
	        int x = (width - scaledWidth) / 2;
	        int y = (height - scaledHeight) / 2;

	        // Draw the scaled image onto the BufferedImage
	        g2d.setColor(new Color(0, 0, 0, 0)); // Transparent background
	        g2d.fillRect(0, 0, width, height);  // Clear the canvas
	        g2d.drawImage(img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH), x, y, null);
	        g2d.dispose();

	        // Return the new image as an ImageIcon
	        return new ImageIcon(centeredImage);
	    }

 

	/*
	 * Return a 2D array of booleans indicating where on the board a particular
	 * piece can go
	 */
	public abstract boolean[][] moves(); 
	
	/**
	 * A chess piece needs to know if it is making a valid move from it's current tile location
	 * to a new tile location. 
	 * @param from originating tile of the chess piece
	 * @param to destination tile of the chess piece
	 * @return true if valid to move to the new location donated by to
	 */
	public abstract boolean validMove(Tile from, Tile to);

	/**
	 * Returns true if moving to the tile means you are capturing the piece
	 * @param curr the tile the piece is moving to
	 * @return true if a capture, false otherwise
	 */
	protected abstract boolean capture(Tile curr);

	
	public void setLocation(int row, int col) {
		this.row = row;
		this.col = col;		
	}

	public Icon getIcon() {
		return icon;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public COLOR getColor() {
		return color;
	}

	public void setColor(COLOR color) {
		this.color = color;
	}
}
