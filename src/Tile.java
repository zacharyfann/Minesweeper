import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.BorderFactory;

public class Tile extends JButton{
	private int row;
	private int col;
	private boolean revealed;
	private boolean flagged;

	private Color[] startColors = {
		new Color(11196241), new Color(7574834)
	};
	private Color[] endColors = {
		new Color(15057567), new Color(14137497)
	};
	
	public Tile(int row, int col) {
	    this.row = row;
		this.col = col;
		this.revealed = false;
		this.flagged = false;

		setupTile();
	}

	private void setupTile() {
		this.setPreferredSize(new Dimension(30, 30));
		this.setBackground(startColors[(row + col) % 2]);
		// this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		this.setFocusPainted(false);
		this.setFont(getFont().deriveFont(12.0f));
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isRevealed() {
		return revealed;
	}
	
	public void setRevealed(boolean revealed) {
		this.revealed = revealed;
		if (revealed) {
			setEnabled(false);
		}
	}
	public boolean isFlagged() {
		return flagged;
	}
	public void setFlagged(boolean flagged){
		this.flagged = flagged;
	}
	
	public void toggleFlag() {
		this.flagged = !this.flagged;
	}

	public void reset() {
		this.revealed = false;
		this.flagged = false;
		this.setText("");
		this.setBackground(startColors[(row + col) % 2]);
		this.setEnabled(true);
		// this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}

}
