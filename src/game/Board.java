package game;

public class Board {
	
	private LetterTile[][] board = new LetterTile[256][256];
	
	
	
	
	
	
	
	/*
	 * holds rows and columns as bytes.
	 * Internal to Board, so can look at data
	 * in the board without a reference to the 
	 * board itself.
	 */
	public class Position {
		/*
		 * stores row and column positions
		 * as bytes
		 */
		private byte row, col;
		
		/*
		 * creates a new position from a row and column,
		 * throws an illegal argument exception if the
		 * byte is not between -128 and 127 (valid byte range)
		 */
		public Position(byte row, byte col) {
			boolean validRow = (row<127 && row>-128);
			boolean validCol = (col<127 && col>-128);
			String errorMessage = "";
			if(!validRow || !validCol) {
				if(!validRow) {
					errorMessage+="row argument <" + row + "> out of range for <-128 ~ 127>";
				}
				if (!validCol) {
					errorMessage+="col argument <" + col + "> out of range for <-128 ~ 127>";
				}
				
				throw new IllegalArgumentException(errorMessage);
			}
			this.row=(byte)(row+128);
			this.col=(byte)(col+128);
		}
		
		/*
		 * returns the row held by this
		 * instance of Position
		 */
		public byte row() {
			return row;
		}
		
		/*
		 * returns the column held by this
		 * instance of Position
		 */
		public byte col() {
			return col;
		}
		
		/*
		 * Returns the LetterTile at
		 * this Position on the Board
		 * holding this Position.
		 * 
		 * Returns null if empty
		 */
		public LetterTile getTile() {
			return Board.this.board[this.row()][this.col()];
		}
		
		/*
		 * Places the passed LetterTile
		 * on the board, at the position
		 * on which this method was called.
		 * If this position already holds
		 * a LetterTile, it returns the 
		 * previous LetterTile.
		 */
		public LetterTile putTile(LetterTile newTile) {
			LetterTile swappedTile = this.getTile();
			Board.this.board[this.row][this.col] = newTile;
			return swappedTile;
		}
		
		/*
		 * returns the Position directly to the left
		 * of the Position on which this method is called
		 */
		public Position left() {
			return new Position(this.row(),(byte)(this.col()-1));
		}
		
		/*
		 * returns the Position directly to the right
		 * of the Position on which this method is called
		 */
		public Position right() {
			return new Position(this.row(),(byte)(this.col()+1));
		}
		
		/*
		 * returns the Position directly above the
		 * Position on which this method is called
		 */
		public Position above() {
			return new Position((byte)(this.row()-1),this.col());
		}
		
		/*
		 * returns the Position directly below the
		 * Position on which this method is called
		 */
		public Position below() {
			return new Position((byte)(this.row()+1),this.col());
		}
	}

	public boolean valid(LetterTile tile) {
		//startRow = row;
		//startColumn = tile.column();
		//ArrayList<char> word;
		
		//Need to check above or below
		if () {
			while (getTile(startRow - 1, tile.column()).getLetter() != null ) {
				startRow -= 1;
			}
		}
		if () {

		}

		return true;
	}

}
