package game;
import java.util.*;

import game.Board.Position;

public class Game {
	private TileDeck deck;
	private Board grid;
	private boolean time;
	private Timer timer = new Timer();
	
	public Game() {
		grid = new Board();
		deck = new TileDeck();
	}
	
	public void startGame () {
		time = true;
		class Task extends TimerTask {
			public void run() {
				time = false;
			}
		}

		timer.schedule(new Task(), 5, 2*60*1000);
	}
	
	public void placeDeckTile(int index, Position position) {
		LetterTile placed = deck.getTile(index);
		Board.Position place = position;
		place.putTile(placed);
		
	}

	public TileDeck getDeck() {
		return deck;
	}

	public Board getBoard() {
		return grid;
	}



}
