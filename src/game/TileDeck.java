package game;

import java.util.ArrayList;

public class TileDeck {
	private ArrayList<LetterTile> deck = new ArrayList<LetterTile>(MAX_TILES);
	private static final int MAX_TILES = 7;

	/*
	 * Fills a new TileDeck with
	 * random (weighed) LetterTiles
	 */
	public TileDeck() {
		for(int i = 0; i<MAX_TILES; i++) {
			this.deck.add(null);
		}
	}

	/*
	 * fills the deck with lettertiles
	 * until the deck's size is 7
	 */
	public void fill() {
		
		for(int i = 0; i<MAX_TILES; i++) {
			if(this.deck.get(i)==null) {
				this.deck.set(i, new LetterTile());
			}
		}
	}
	
	/*
	 * removes the specified LetterTile from this
	 * TileDeck. Throws an exception if the tile
	 * isn't found.
	 */
	public LetterTile drop(LetterTile tile) throws IllegalArgumentException{
		for(LetterTile check : this.deck) {
			if(check.equals(tile)) {
				this.deck.set(this.deck.indexOf(check),null);
				return tile;
			}
		}
		throw new IllegalArgumentException("Tile not in deck.");
	}

	/*
	 * places a tile at the specified index. 
	 * Returns the tile formerly at the index.
	 */
	public LetterTile swap(LetterTile tile, int index) {
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
	
	public String toString() {
		String ret = "";
		for(LetterTile t : this.deck) {
			ret+=(t + "\n");
		}
		return ret;
	}
}