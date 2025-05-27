import java.util.Random;;

public class MinesweeperLogic {
	
	
	private Tile[][] board;
	private boolean[][] mines;
	private int boardSize;
	private int mineCount;
	private int revealedFlags;
	private int revealedTiles;
	private int flagCount;
	private Random random;
	
	public MinesweeperLogic(int size, int mineCount) {
		this.boardSize = size;
		this.mineCount = mineCount;
		this.revealedFlags = 0;
		this.flagCount = 0;
		this.random = new Random();

		initializeBoard();
	}

	private void initializeBoard() {
		board = new Tile[boardSize][boardSize];
		mines = new boolean[boardSize][boardSize];

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = new Tile(i, j);
			}
		}

		placeMines();
	}

	public void ensureSafety(int row, int col) {
		while (mines[row][col] || getAdjacentMineCount(row, col) > 0) {
			initializeBoard();
		}
	}
	
	private void placeMines() {
		int placedMines = 0;

		while (placedMines < mineCount) {
			int x = random.nextInt(boardSize);
			int y = random.nextInt(boardSize);

			if (!mines[x][y]) {
				mines[x][y] = true;
				placedMines++;
			}
		}
	}
	
	public boolean revealTile(int row, int col){
		if (!isValidPosition(row, col) || board[row][col].isRevealed()) {
			return false;
		}

		board[row][col].setRevealed(true);
		revealedTiles++;
		// System.out.println(isMine(row, col));
		return isMine(row, col);
	}

	public int getAdjacentMineCount(int row, int col) {
		if (!isValidPosition(row, col)) {
			return 0;
		}

		int count = 0;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue;
				int newRow = row + i;
				int newCol = col + j;

				if (isValidPosition(newRow, newCol) && mines[newRow][newCol]) {
					count++;
				}
			}
		}

		return count;
	}

	public boolean isValidPosition(int row, int col) {
		return row >= 0 && row < boardSize && col >= 0 && col < boardSize;
	}

	public boolean isMine(int row, int col) {
		return isValidPosition(row, col) && mines[row][col];
	}

	public Tile[][] getBoard() {
		return board;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public int getMineCount() {
		return mineCount;
	}

	public int getFlagCount() {
		flagCount = 0;

		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j].isFlagged()) {
					flagCount++;
				}
			}
		}
		return flagCount;
	}

	public int getRevealedTiles() {
		return revealedTiles;
	}	

	public boolean isGameWon() {
        int totalTiles = boardSize * boardSize;
        int expectedRevealed = totalTiles - mineCount;
        return revealedTiles == expectedRevealed;
    }
	public void testMineLocations(){
		for(int i = 0; i < boardSize; i++){
			for(int j = 0; j < boardSize; j++){
				if(isMine(i, j)) System.out.print("B ");
				else System.out.print(". ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
