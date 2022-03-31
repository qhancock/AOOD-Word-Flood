package game;

import java.util.ArrayList;

import data.Dictionary;

public class Board {

	private LetterTile[][] board = new LetterTile[256][256];
	public final boolean HORIZONTAL = true, VERTICAL = false;
	public final boolean[] directions = {HORIZONTAL, VERTICAL};





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
		 * gets the word around this position in 
		 * either the horizontal or vertical direction
		 * based on the boolean passed in.
		 */
		public String getWord(boolean dir) {

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
				currentPosition = (dir==VERTICAL)?
						currentPosition.above():currentPosition.left();

				currentTile = currentPosition.getTile();
				word = currentTile.getLetter()+word;
			} while (currentTile!=null);

			//adds the tile to the word
			word+=this.getTile().getLetter();

			//resets the position back to this
			currentPosition = this;

			/*
			 * travels forwards from the starting position
			 * and appends the found letters to the end of the
			 * word-string
			 */
			do {
				currentPosition = (dir==VERTICAL)?
						currentPosition.below():currentPosition.right();

				currentTile = currentPosition.getTile();
				word = currentTile.getLetter()+word;
			} while (currentTile!=null);

			/*
			 * returns null if the tile has no letter
			 * or is a single letter (not a valid word)
			 */
			return word.length()<1?null:word;
		}

		public boolean valid(Position placed) {
			boolean vert;
			boolean hori;
			if (placed.getWord(VERTICAL) != null || placed.getWord(HORIZONTAL) != null) {
				vert = placed.getWord(VERTICAL) != null;
				hori = placed.getWord(HORIZONTAL) != null;

				if (vert){
					if (Dictionary.validWord(placed.getWord(VERTICAL))) {
						if (!hori) {
							return true;
						} else {
							return Dictionary.validWord(placed.getWord(HORIZONTAL));
						}
					} else {
						return false;
					}
				} else if (hori) {
					if (Dictionary.validWord(placed.getWord(HORIZONTAL))) {
						if (!vert) {
							return true;
						} else {
							return Dictionary.validWord(placed.getWord(VERTICAL));
						}
					} else {
						return false;
					}
				}
			} 
			return false;

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
}