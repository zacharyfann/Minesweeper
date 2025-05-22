
public class MinesweeperLogic {
	
	
	private Tile[][] board;	
	private boolean[][] mines = new boolean[18][14]; 
	/*
	 * The constructor setups up the 2D array of Tiles for the chess board
	 * The constructor calls 3 helper methods with add chess pieces on the board
	 * A tile in the board either have a piece or not have a piece
	 */
	public MinesweeperLogic() {
		board = new Tile[18][14];
		
		for(int i = 0; i < board.length;i++) {
			for(int j = 0; j < board[0].length;j++) {
				board[i][j] = new Tile(i, j);
			}
		}	
		placeMines(40);
		
	}
	
	public void placeMines(int numMines) {
		int count = 0;
		while(count < numMines) {
			int row = (int)(Math.random() * 18)+1;
			int col = (int)(Math.random() * 14)+1;
			if(mines[row][col] == false) {
				mines[row][col] = true;
				count++;
			}
		}
		
		
	}
	
	public void setNumbers() {
		for (int i = 0; i < board.length;i++) {
			for (int j = 0; j < board[i].length;j++) {
				if (board[i][j].getPiece() == null) {
					int numMines = 0;
					for (int x = -1; x <=1; x++) {  //this checks the x direction one tile to the left and one tile to the right
						for(int y = -1; y<=1; y++) { //this checks the y direction one tile up and one tile down
							if (i+x >= 0 && i+x < board.length && j+y >= 0 && j+y < board[i].length) { //this checks if the tile is within the bounds of the board (no out of bounds error)
								if (mines[i+x][j+y] == true) { //this checks if the tile is a mine
									numMines++;
								}
								board[i][j].setPiece(new Number( numMines, i, j, board)); //set the tile to the number of mines around it
								board[i][j].getPiece().setLocation(i, j);
							}


						}

					}

				}

			}

		}

	}
	public Boolean isGameWon() {
		
		for(int i = 0; i < board.length;i++) {
			for(int j = 0; j < board[i].length;j++) {
				if (mines[i][j] == true && board[i][j].isFlagged() == false) {
					return false;
				}
				else if (board[i][j] == false && board[i][j].isFlagged() == true) {
					return false;
				}

			}
			
		}

		return true;
	}
	
	/*
	 * Getter for board
	 */
	public Tile[][] getBoard() {
		return this.board;
	}	
	
	
	
}
