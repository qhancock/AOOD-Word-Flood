package game;

import data.Dictionary;
import java.util.ArrayList;

public class Board {

	private LetterTile[][] board = new LetterTile[256][256];
	
	public static final int RIGHT = 0, ABOVE = 1, LEFT = 2, BELOW = 3;
	public static final int[] directions = new int[] {0,1,2,3};
	public static final boolean HORIZONTAL = true, VERTICAL = false;
	public static final boolean[] dimensions = {HORIZONTAL, VERTICAL};

	public int getScore() {
		int score = 0;
		
		for(LetterTile[] row : board) {
			for(LetterTile tile : row) {
				score+=(tile!=null)?1:0;
			}
		}
		
		return score;
	}

	public ArrayList<Board.Position> largestDisconnectedTileBody() {
		
	}
	
	

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
		private int row, col;

		/*
		 * creates a new position from a row and column,
		 * throws an illegal argument exception if the
		 * byte is not between -128 and 127 (valid byte range)
		 */
		public Position(int row, int col) {
			boolean validRow = (row<=127 && row>=-128);
			boolean validCol = (col<=127 && col>=-128);
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
			this.row=row;
			this.col=col;
		}

		/*
		 * returns the row held by this
		 * instance of Position
		 */
		public int row() {
			return row;
		}

		/*
		 * returns the column held by this
		 * instance of Position
		 */
		public int col() {
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
			return Board.this.board[this.row()+128][this.col()+128];
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
			Board.this.board[this.row()+128][this.col()+128] = newTile;
			return swappedTile;
		}

		/*
		 * gets the word around this position in 
		 * either the horizontal or vertical direction
		 * based on the boolean passed in.
		 */
		public String getTileString(boolean dir) {

			/*
			 * if there's no tile at this position,
			 * there can't be a word in either direction:
			 * returns null
			 */
			if(this.getTile()==null) return null;

			//defines the word to be returned
			String word = "";

			/*
			 * variables to monitor the current
			 * positions to the left/right/up/down
			 * that are being appended to 
			 */
			Position currentPosition = this;
			LetterTile currentTile;

			/*
			 * travels backwards from the starting position
			 * and appends the found letters to the front of
			 * the word-string
			 */
			do {
				/*
				 * sets currentPosition to the position
				 * above/left where it is now, traveling
				 * that direction
				 */
				currentPosition = (dir==VERTICAL)?
				currentPosition.above():currentPosition.left();
				
				currentTile = currentPosition.getTile();
				
				/*
				 * continues adding to the word in
				 * the specified direction (backwards,
				 * because it's going up or left) as
				 * long as the tile's letter isn't null
				 */
				if(currentTile!=null) word = currentTile.getLetter()+word;
			} while (currentTile!=null);

			//adds this tile to the word
			word+=this.getTile().getLetter();

			//resets the position back to this
			currentPosition = this;

			/*
			 * travels forwards from the starting position
			 * and appends the found letters to the end of the
			 * word-string
			 */
			do {
				/*
				 * sets currentPosition to the position
				 * below/right where it is now, traveling
				 * that direction
				 */
				currentPosition = (dir==VERTICAL)?
				currentPosition.below():currentPosition.right();

				currentTile = currentPosition.getTile();
				
				/*
				 * continues adding to the word in
				 * the specified direction (forwards,
				 * because it's going down or right) as
				 * long as the tile's letter isn't null
				 */
				if(currentTile!=null) word = word+currentTile.getLetter();
			} while (currentTile!=null);

			/*
			 * returns null if the tile has no letter
			 * or is a single letter (not a valid word)
			 */
			return word.length()<=1?null:word;
		}

		public boolean valid() {
			
			/*
			 * gets the words in vertical and horizontal
			 * directions, then checks if either one is 
			 * a valid word. If so, it's part of a word.
			 */
			String verticalWord = this.getTileString(Board.VERTICAL);
			String horizontalWord = this.getTileString(Board.HORIZONTAL);
			
			/*
			 * true if either word is a string of characters
			 * that isn't in the dictionary
			 */
			boolean eitherInvalid =
			(verticalWord!=null && !Dictionary.validWord(verticalWord)) ||
			(horizontalWord!=null && !Dictionary.validWord(horizontalWord));
			
			return !eitherInvalid && !this.isolated();
		}
		
		/*
		 * true if there are letters
		 * touching it in any direction
		 */
		public boolean isolated() {
			boolean connected = 
			this.getTileString(Board.HORIZONTAL)!=null ||
			this.getTileString(Board.VERTICAL)!=null;
						
			return !connected;
		}
		
		public Position getAdjacent(int direction) {
			switch (direction) {
			
			case Board.RIGHT : {
				return this.below();
			}
			
			case Board.ABOVE : {
				return this.above();
			}
				
			case Board.LEFT : {
				return this.left();
			}
			
			case Board.BELOW : {
				return this.below();
			}
			}
			
			return null;
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
		
		/*
		 * returns LetterTiles that are adjacent to
		 * this one as an ArrayList<Position>.
		 * 
		 * Throws an IllegalArgumentException if this
		 * Position itself has no LetterTile on it.
		 */
		public ArrayList<Board.Position> getAdjacentPositions() throws IllegalArgumentException {
			if(this.getTile()==null) {
				throw new IllegalArgumentException(this.row() + "-" + this.col() + " has no tile on it.");
			}
			
			ArrayList<Board.Position> ret = new ArrayList<Board.Position>();
			for(int direction : Board.directions) {
				ret.add(this.getAdjacent(direction));
			}
			
			return ret;
		}
	}
}