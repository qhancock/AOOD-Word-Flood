package game;

import gui.GameWindow;

public class Game {
	public GameWindow window = null;
	
	private TileDeck deck;
	private Board board;
	
	private int score;

	public Game() {
		board = new Board();
		board.game = this;
		deck = new TileDeck();
		deck.game = this;
		deck.fill();
	}
	
	int selectedDeckIndex = -1;
	Board.Position selectedBoardPosition = null;
	
	public void deckSelect(int index) {
		
		if(selectedBoardPosition == null) {
			deck.select(index);
			selectedDeckIndex = deck.getSelectedIndex();
		} else {
			doSelectionInterchange(index, selectedBoardPosition);
		}
		this.window.updateConfirmPanel(this.checkValid());
	}
	
	public void boardSelect(Board.Position newSelectedPosition) {

		//nothing selected in the deck
		if(selectedDeckIndex == -1) {
			board.select(newSelectedPosition);
			selectedBoardPosition = board.getSelectedPosition();
		} else {
			doSelectionInterchange(selectedDeckIndex, newSelectedPosition);
		}
		this.window.updateConfirmPanel(this.checkValid());
		
	}
	
	private void doSelectionInterchange(int deckIndex, Board.Position position) {
		
		LetterTile positionTile = position.getTile();
		LetterTile indexTile = deck.getTile(deckIndex);
		
		board.deselect();
		deck.deselect();
		
		tileInterchange(deckIndex, position);
		selectedDeckIndex = deck.getSelectedIndex();
		selectedBoardPosition = board.getSelectedPosition();
		
		window.updateAll();
	}
	
	/*
	 * facilitates the transfer of a tile 
	 * from the deck at an index to the
	 * board at a position or vice versa
	 */
	public void tileInterchange(int deckIndex, Board.Position position) {
		deck.putTile(position.putTile(deck.getTile(deckIndex)), deckIndex);
	}

	//checks if all placed tiles are valid
	public boolean checkValid() {
		int size = board.getTiledPositions().size();
		for (int x = 0; x < size; x++) {
			if (!board.getTiledPositions().get(x).valid()) {
				return false;
			}
		}
		return true;
	}

	public TileDeck getDeck() {
		//Retrieves the deck
		return deck;
	}

	public Board getBoard() {
		//Retrieves the board
		return board;
	}
	
	public void refillBar() {
		if(this.checkValid())
		setScore(getScore()+ this.deck.fill());
	}
	
	public void setScore(int score) {
		this.score = score;
		this.window.updateScorePanel(this.score);
	}
	
	public int getScore() {
		return this.score;
	}
}
