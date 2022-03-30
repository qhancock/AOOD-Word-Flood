package game;

public class LetterTile {
	private char letter;
	
	public LetterTile() {
		this.letter = data.LetterFactory.weightedRandomLetter();
	}
	
	public LetterTile(char letter) {
		this.letter = letter;
	}
	
	public char getLetter() {
		return letter;
	}
}