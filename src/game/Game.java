package game;
import java.util.*;

import game.Board.Position;

public class Game {
	private TileDeck deck;
	private Board grid;
	private boolean time;
	private long end;
	private Timer timer = new Timer();

	public Game() {
		grid = new Board();
		deck = new TileDeck();
	}

	public void startGame () {
		//Starts the timer for the game
		time = true;
		class Task extends TimerTask {
			public void run() {
				time = false;
			}
		}
		end = System.currentTimeMillis() + (2*60*1000);
		timer.schedule(new Task(), 5, 2*60*1000);
	}

	public void placeDeckTile(int index, Position position) {
		//Places a tile on the board
		LetterTile placed = deck.getTile(index);
		Board.Position place = position;
		place.putTile(placed);
		deck.drop(placed);

	}

	public void swapDeckTile (int index, Position position) {
		//Swaps a tile on the board with a tile in the deck
		Board.Position place = position;
		place.putTile(deck.swap(position.getTile(), index));
	}

	public void discard(int index) {
		//Drops and replaces a tile in the deck
		deck.drop(deck.getTile(index));
		deck.fill();
		timer.cancel();
		class Task extends TimerTask {
			public void run() {
				time = false;
			}
		}

		end = end - 8000;
		timer.schedule(new Task(), 5, end);
	}

	public boolean checkValid() {
		//checks if all placed tiles are valid
		int size = grid.getTiledPositions().size();
		for (int x = 0; x < size; x++) {
			if (!grid.getTiledPositions().get(x).valid()) {
				return false;
			}
		}
		
		return true;
	}

	public void confirmPlacement() {
		//Run when confirm button is pressed
		//Make a repaint/popup appear if its false
		if (checkValid()) {
			deck.fill();
		} else {
			System.out.print("Not valid placement");
		}
	}

	public TileDeck getDeck() {
		return deck;
	}

	public Board getBoard() {
		return grid;
	}



}
