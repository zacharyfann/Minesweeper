import java.util.Random;;

public class MinesweeperLogic {
	
	
	private Tile[][] board;
	private boolean[][] mines;
	private int rows;
	private int cols;
	private int revealedFlags;
	private int revealedTiles;
	private int flagCount;
	private Random random;
	private int mineCount;
	
	public MinesweeperLogic(int rows, int cols, int mineCount) {
		this.rows = rows;
		this.cols = cols;
		this.mineCount = mineCount;
		this.revealedFlags = 0;
		this.flagCount = 0;
		this.random = new Random();

		initializeBoard();
	}

	private void initializeBoard() {
		board = new Tile[rows][cols];
		mines = new boolean[rows][cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
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

		while (placedMines < this.mineCount) {
			int x = random.nextInt(rows);
			int y = random.nextInt(cols);

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

	public int getAdjacentFlagCount(int row, int col) {
		if (!isValidPosition(row, col)) {
			return 0;
		}

		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) continue;
				int newRow = row + i;
				int newCol = col + j;


				if (isValidPosition(newRow, newCol) && board[newRow][newCol].isFlagged()) {
					count++;
				}
			}
		}

		return count;
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

	public void setFlagged(int row, int col, boolean flagged) {
		if (isValidPosition(row, col)) {
			board[row][col].setFlagged(flagged);
			if (flagged) {
				revealedFlags++;
			} else {
				revealedFlags--;
			}
		}
	}

	public boolean isValidPosition(int row, int col) {
		return row >= 0 && row < rows && col >= 0 && col < cols;
	}

	public boolean isMine(int row, int col) {
		return isValidPosition(row, col) && mines[row][col];
	}

	public Tile[][] getBoard() {
		return board;
	}

	// public int getBoardSize() {
	// 	return boardSize;
	// }

	public int getMineCount() {
		return this.mineCount;
	}

	public int getFlagCount() {
		flagCount = 0;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
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
        int totalTiles = rows * cols;
        int expectedRevealed = totalTiles - this.mineCount;
        return revealedTiles == expectedRevealed;
    }
	public void testMineLocations(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				if(isMine(i, j)) System.out.print("B ");
				else System.out.print(". ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
