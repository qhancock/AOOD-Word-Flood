package game;

public class LetterTile {
	private char assignedLetter;
	/*Boolean is for the sake of placing before confirming,
	 not telling whether or not it is in reserve or on the board*/
	private boolean placed;
	LetterTile(char letter, boolean random, boolean biased) {
		assignedLetter = letter;
		placed = false;
	}
	
	//Random letter with bias towards more common
	private biasedRandom() {
		
	}
	
	//Pure random as the name entails
	private pureRandom() {
		
	}
	
	public Position getPosition() {
		//Retrieves position
	}
}
