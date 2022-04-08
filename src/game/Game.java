package game;
import java.util.*;

import game.Board.Position;

public class Game {
	private TileDeck deck;
	private Board secondaryGrid;
	private Board grid;
	private boolean time;
	private long end;
	private Timer timer = new Timer();
	
	public Game() {
		grid = new Board();
		secondaryGrid = new Board();
		deck = new TileDeck();
	}
	
	public void startGame () {
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
		LetterTile placed = deck.getTile(index);
		Board.Position place = position;
		place.putTile(placed);
		deck.drop(placed);
		
	}
	
	public void swapDeckTile (int index, Position position) {
		Board.Position place = position;
		place.putTile(deck.swap(position.getTile(), index));
	}
	
	public void discard(int index) {
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
		//Make something to go through and check validity for board
		secondaryGrid.valid();
		return true;
	}
	
	public void confirmPlacement() {
		grid = secondaryGrid;
		deck.fill();
	}

	public TileDeck getDeck() {
		return deck;
	}

	public Board getBoard() {
		return grid;
	}



}
