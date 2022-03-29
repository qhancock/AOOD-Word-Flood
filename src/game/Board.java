package game;

public class Board {
	
	private LetterTile[][] board = new LetterTile[256][256];
	public LetterTile getTile(byte row, byte col) {		
		return board[row+128][col+128];
	}
	
	
	
	public class Position {
		private byte row, col;
		
		public Position(byte row, byte col) {
			boolean validRow = !(row>127 || row<-128);
			boolean validCol = !(col>127 || col<-128);
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
		
		public byte row() {
			return row;
		}
		public byte col() {
			return col;
		}
		
		public LetterTile getTile() {
			return Board.this.getTile(this.row(),this.col());
		}
	}

}
