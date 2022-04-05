package data;

import java.util.HashMap;

public class LetterFactory {
	
	/*
	 * a hashmap that matches a letter to the 
	 * percent chance that is is chosen as the
	 * random letter I.E. weighting percentage
	 */
	private static HashMap<Character,Integer> 
	weightedLetterPercents = createLetterPercentMap();
	
	private static final char[] alphabet = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	
	/*
	 * returns a random letter from the weighted set
	 */
	public static char weightedRandomLetter() {
		
		/*
		 * generates a random number from 1 to 100
		 * that will be used to pick a random letter
		 * from the weighted set
		 */
		int random = Utilities.boundedRandom(1, 100);
		
		/*
		 * iterates over the alphabet, determining of 
		 * which letter's range "random" is a part
		 */
		for(char possibleChoice : alphabet) {
			
			/*
			 * sees how far from the start of the possible
			 * letter's range it is
			 */
			int distanceFromRangeStart = random-letterRangeStartIndex(possibleChoice);
			
			//gets how large the possible letter's range is
			int rangeSize = weightedLetterPercents.get(possibleChoice);
			
			/*
			 * if the distance to the start of the range is
			 * less than the size of the range, it's in the range
			 */
			boolean withinRange = distanceFromRangeStart<rangeSize;
			
			//returns the letter
			if(withinRange) {
				return possibleChoice;
			}
		}
		return 0;
	}
	
	/*
	 * returns a pure random letter, with
	 * no weighting whatsoever (A-Z)
	 */
	public static char pureRandomLetter() {
		return alphabet[Utilities.boundedRandom(0, alphabet.length-1)];
	}
	
	/*
	 * creates weighting hashmap, by defining "ret"
	 * and populating it with char keys and percent
	 * values. These percent values are hand-picked.
	 * 
	 * PERCENT VALUES MUST ADD TO 100%
	 */
	private static HashMap<Character,Integer> createLetterPercentMap() {
		HashMap<Character,Integer> percents = new HashMap<Character,Integer>();
		percents.put('A', 8); percents.put('B', 4);
		percents.put('C', 4); percents.put('D', 4);
		percents.put('E', 7); percents.put('F', 3);
		percents.put('G', 3); percents.put('H', 4);
		percents.put('I', 5); percents.put('J', 2);
		percents.put('K', 3); percents.put('L', 3);
		percents.put('M', 4); percents.put('N', 5);
		percents.put('O', 6); percents.put('P', 4);
		percents.put('Q', 1); percents.put('R', 5);
		percents.put('S', 6); percents.put('T', 5);
		percents.put('U', 5); percents.put('V', 2);
		percents.put('W', 2); percents.put('X', 1);
		percents.put('Y', 3); percents.put('Z', 1);
		
		return percents;
	}
	
	/*
	 * returns the start index of the range from  1-100
	 * that embodies the picking area for the passed char.
	 * Adds the size of the ranges from all prior letters
	 * to "1" such that the start index is the next number
	 * available outside of any other letters' ranges
	 */
	private static int letterRangeStartIndex(char c) {
		int start = 1;
		
		/*
		 * adds the area ranges for all prior letters,
		 * breaks after finding the letter
		 */
		for(char priorLetter : alphabet) {
			if(priorLetter==c) {
				break;
			}
			start+=weightedLetterPercents.get(priorLetter);
		}
		return start;
	}
}