package game;

import java.util.ArrayList;

public class TileDeck {
	public Game game = null;
	
	private ArrayList<LetterTile> deck = new ArrayList<LetterTile>(MAX_TILES);
	public static final int MAX_TILES = 7;
	
	private int selectedIndex = -1;
	
	public void reportSelection(int selectionIndex) {
		if(game==null) return;
		
		game.deckSelect(selectionIndex);
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}
	
	public void select (int selectionIndex) {
		
		//deselects all previous
		for(LetterTile tile : deck) {
			if(tile!=null && tile.selected() && (selectionIndex==-1 || tile!=deck.get(selectionIndex))) {
				tile.select();
			}
		}
		
		boolean deSelection = selectionIndex == -1;
		boolean newSelection = selectedIndex == -1 && !deSelection;
		boolean swapSelection = !deSelection && !newSelection;
		
		
		if(deSelection) {
			selectedIndex = -1;
		} else if(newSelection) {
			this.deck.get(selectionIndex).select();
			selectedIndex = selectionIndex;
		} else if(swapSelection) {
			this.putTile(this.putTile(this.getTile(selectionIndex), selectedIndex), selectionIndex);
			this.deselect();
		}
	}
	
	public void deselect() { 
		select(-1);
	}

	/*
	 * Creates a new TileDeck with blank indices
	 */
	public TileDeck() {
		for(int i = 0; i<MAX_TILES; i++) {
			this.deck.add(null);
		}
	}

	/*
	 * fills the deck with lettertiles
	 * until the deck's size is MAX_TILES.
	 * 
	 * returns the number of replaced
	 * tiles.
	 */
	public int fill() {
		int filled = 0;
		for(int i = 0; i<MAX_TILES; i++) {
			if(this.deck.get(i)==null) {
				this.deck.set(i, new LetterTile());
				filled++;
			}
		}
		return filled;
	}
	
	/*
	 * removes the specified LetterTile from this
	 * TileDeck. Returns the tile that was at
	 * that index formerly
	 */
	public LetterTile drop(int index) {
		return this.deck.set(index, null);
	}

	/*
	 * places a tile at the specified index. 
	 * Returns the tile formerly at the index.
	 */
	public LetterTile putTile(LetterTile tile, int index) {
		return this.deck.set(index, tile);
	}
	
	/*
	 * returns the contents of this TileDeck
	 * as an ArrayList
	 */
	public ArrayList<LetterTile> getContents() {
		return deck;
	}
	
	/*
	 * returns the LetterTile at a specified
	 * index in the TileDeck
	 */
	public LetterTile getTile(int index) {
		return deck.get(index);
	}
	
	/*
	 * returns the index of a specified LetterTile
	 * in this TileDeck. Returns -1 if not found.
	 */
	public int getIndex(LetterTile tile) {
		for(int index = 0; index<this.deck.size(); index++) {
			if(deck.get(index)==tile) {
				return index;
			}
		}
		return -1;
	}
	
	public String toString() {
		String ret = "";
		for(LetterTile t : this.deck) {
			ret+=(t + "\n");
		}
		return ret;
	}
}