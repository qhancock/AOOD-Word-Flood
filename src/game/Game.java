package game;
import java.util.*;

import game.Board.Position;

public class Game {
	//delete or comment out timer portion later
	private TileDeck deck;
	private Board board;
	private boolean play;
	private long end;

	public Game() {
		board = new Board();
		deck = new TileDeck();
		
	}

	/*
	public void startGame () {
		//Starts the timer for the game
		play = true;
		class Task extends TimerTask {
			public void run() {
				play = false;
			}
		}
		end = System.currentTimeMillis() + (2*60*1000);
		timer.schedule(new Task(), 5, 2*60*1000);
	}
	*/

	public void placeDeckTile(int index, Position position) {
		//Places a tile on the board
		LetterTile placed = deck.getTile(index);
		Board.Position place = board.new Position(position.row(), position.col());
		place.putTile(placed);
		deck.drop(placed);

	}
	
	public void swapGridTile (Position position1, Position position2) {
		//Swaps two grid tiles with each other
		LetterTile hold = position1.getTile();
		Board.Position place1 = board.new Position(position1.row(), position1.col());
		Board.Position place2 = board.new Position(position2.row(), position2.col());
		place1.putTile(position2.getTile());
		place2.putTile(hold);
	}

	public void swapDeckTile (int index, Position position) {
		//Swaps a tile on the board with a tile in the deck
		Board.Position place = board.new Position(position.row(), position.col());
		place.putTile(deck.swap(place.getTile(), index));
	}

	public void discard(int index) {
		//Drops and replaces a tile in the deck
		//Need to delete timer portion later
		deck.drop(deck.getTile(index));
		deck.fill();
	}

	public boolean checkValid() {
		//checks if all placed tiles are valid
		int size = board.getTiledPositions().size();
		for (int x = 0; x < size; x++) {
			if (!board.getTiledPositions().get(x).valid()) {
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
		}
	}

	public TileDeck getDeck() {
		//Retrieves the deck
		return deck;
	}

	public Board getBoard() {
		//Retrieves the board
		return board;
	}

	public int getTimeLeft() {
		//Retrieves the time left in seconds
		//should be deleted later
		return (int)(long)(end - System.currentTimeMillis()/1000);
	}

	public boolean gameContinue() {
		return play;
	}
}
