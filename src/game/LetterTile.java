package game;

public class LetterTile {
	
	private static final char[][] ORDERED_ALPHABET =
	{{'e','t','a','o','i','s'},{'h','n','r','d','l',
	'c'},{'m','u','w','f','g','y','b'},{'v','k','j','x','q','z'}};
	private static final char[] vowels = {'a','e','i','o','u'};
	
	private char letter;
	
	private final static int[] CAT_WEIGHTS = {6,4,3,1};
	
	private static char skewedRandomLetter() {
		
		//arraylist of weighted letters like eeeetttt... etc
		java.util.ArrayList<Character> weightedAlphabet = new java.util.ArrayList<Character>();
		
		//iterates over each letter group
		for(int i = 0; i<ORDERED_ALPHABET.length; i++) {
			
			//iterates over each character in each letter group
			for(char c : ORDERED_ALPHABET[i]) {
				
				/*
				 * adds each letter to the weighted alphabet
				 * n times, where n is its category weight
				 */
				for(int weight = 1; weight<=CAT_WEIGHTS[i]; weight++) {
					weightedAlphabet.add(c);
				}
			}
		}
		
		//returns a random letter from the weighted alphabet
		return weightedAlphabet.get(data.Utilities.boundedRandom(0, weightedAlphabet.size()-1));
	}

	
	public static void main(String[] args) {
		while(true) {
			System.out.println(skewedRandomLetter());
		}
	}
}
