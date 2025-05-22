import java.awt.Color;

import javax.swing.JButton;

public class Tile extends JButton{
	private Piece piece;
	public static int num = 0;
	private int row, col;
	
	public void setRow(int row) {
		this.row = row;
	}

	public void setCol(int col) {
		this.col = col;
	}

	//color for white and black is a differed hue
	private Color black =  Color.decode("#62cbe7");  //change to darker green
	private Color white = Color.decode("#104373");  //change to lighter green
	
	public Tile(int r, int c) {
	    this.setFocusPainted(false);
	    this.setOpaque(true); // Ensure the background is painted
		this.setBackground((r+c)%2==0 ? black : white);
		this.setBorderPainted(false);
		this.row = r;
		this.col = c;

	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public void setPiece(Piece piece) {
		if(piece==null) {
			this.piece = null;
			this.setIcon(null);
			return;
		}
		this.piece = piece;
		this.setIcon(piece.getIcon());
		piece.setLocation(row, col);
	}
	
	
	public Piece getPiece() {
		return piece;
	}
	public boolean isFlagged() {
		if(piece instanceof Flag) {
			return true;
		}
		return false;
	}
	
}
