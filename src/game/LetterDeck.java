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
	}

	public void remove() {
		//Remove tiles from currentTiles
		//Probably need a parameter or something to detect
		//what needs to be removed
	}
}
