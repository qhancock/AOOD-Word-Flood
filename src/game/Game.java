package game;

public class Game {
	private TileDeck deck;
	private Board grid;
	public Game() {
		grid = new Board();
		deck = new TileDeck();
	}
	
	public TileDeck getDeck() {
		return deck;
	}
	
	public Board getBoard() {
		return grid;
	}
}
