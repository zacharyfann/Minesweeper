
public class MinesweeperLogic {
	
	
	private Tile[][] board;
	boolean playerTurn = true; //true = white, false = black
	
	/*
	 * The constructor setups up the 2D array of Tiles for the chess board
	 * The constructor calls 3 helper methods with add chess pieces on the board
	 * A tile in the board either have a piece or not have a piece
	 */
	public MinesweeperLogic() {
		board = new Tile[18][14];
		
		for(int i =0; i < board.length;i++) {
			for(int j = 0; j < board[0].length;j++) {
				board[i][j] = new Tile(i, j);
			}
		}	
		
		
		//setup other pieces
		setupPieces(COLOR.BLACK);
		setupPieces(COLOR.WHITE);
	}
	
	public void setupPieces(COLOR color) {
		
		String prefix = color == COLOR.BLACK ? "b_" : "w_";
		
		
		//Queen & King
		board[color == COLOR.BLACK? 0 : board.length-1][3].setPiece(new Queen(prefix+"_queen.png", color,board.length-2,3, board ));
		board[color == COLOR.BLACK? 0 : board.length-1][4].setPiece(new King(prefix+"_king.png", color,board.length-2,3, board ));

		//Bishops
		board[color == COLOR.BLACK? 0 : board.length-1][2].setPiece(new Bishop(prefix+"_bishop.png", color,board.length-2,3, board));
		board[color == COLOR.BLACK? 0 : board.length-1][5].setPiece(new Bishop(prefix+"_bishop.png", color,board.length-2,3, board ));

		//Knights
		board[color == COLOR.BLACK? 0 : board.length-1][1].setPiece(new Knight(prefix+"_knight.png", color,board.length-2,3, board ));
		board[color == COLOR.BLACK? 0 : board.length-1][6].setPiece(new Knight(prefix+"_knight.png", color,board.length-2,3, board ));

		//Rooks
		board[color == COLOR.BLACK? 0 : board.length-1][0].setPiece(new Rook(prefix+"_rook.png", color,board.length-2,3, board ));
		board[color == COLOR.BLACK? 0 : board.length-1][7].setPiece(new Rook(prefix+"_rook.png", color,board.length-2,3, board ));

	}
	
	
	/*
	 * Getter for board
	 */
	public Tile[][] getBoard() {
		return this.board;
	}	
	
	
	
}
