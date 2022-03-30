package game;

import java.util.ArrayList;

public class LetterDeck {
	private ArrayList<String> currentTiles;

	LetterDeck() {
		//Randomized but weighted towards common letters
	}

	public void fill() {
		//If currentTiles is less than 7
		//then fill the arrayList till it hits 7
		//Potentially use same random as in constructor,
		//Might have to use different one to avoid repeated
		//character overlap in reserve
		
		while (currentTiles.size() < 7) {
			currentTiles.add(new LetterTile());
		}
	}

	public void remove(int[] removed) {
		/*Remove tiles from currentTiles
		Probably need a parameter like an arraylist
		or array to detect
		what needs to be removed
		*/
		
		for (int x = 0; x < removed.length; x++ ) {
			currentTiles.remove(removed[x]);
		}
	}

}
