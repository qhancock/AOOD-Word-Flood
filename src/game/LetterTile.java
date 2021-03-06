package game;

public class LetterTile {
	private char letter;
	private boolean selected = false;
	
	public LetterTile() {
		this.letter = Character.toLowerCase(data.LetterGenerator.weightedRandomLetter());
	}
	
	public LetterTile(char letter) {
		this.letter = letter;
	}
	
	public char getLetter() {
		return letter;
	}
	
	public boolean select() {
		return this.selected = !selected;
	}
	
	public boolean selected() {
		return this.selected;
	}
	
	public String toString() {
		return super.toString() + " > " + this.getLetter();
	}
}