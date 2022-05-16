package data;

import java.util.HashMap;

public class LetterFactory {
	
	/*
	 * a hashmap that matches a letter to the 
	 * percent chance that is is chosen as the
	 * random letter I.E. weighting percentage
	 */
	private static HashMap<Character,Double> 
	weightedLetterChances = createLetterChanceMap();
	
	/*
	 * the alphabet, not much more to say
	 */
	private static final char[] alphabet = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

	/*
	 * a double that represents the sum of all the
	 * chance values, as they are relative to each
	 * other in weightedLetterChances
	 */
	private static double chanceTotal = getChanceTotal();
	
	/*
	 * returns a random letter from the weighted set
	 */
	public static char weightedRandomLetter() {
		
		/*
		 * generates a random number from 1 to 100
		 * that will be used to pick a random letter
		 * from the weighted set
		 */
		
		double random = Utilities.boundedRandomDouble(0, chanceTotal);
		
		/*
		 * iterates over the alphabet, determining of 
		 * which letter's range "random" is a part
		 */
		for(char possibleChoice : alphabet) {
			
			/*
			 * sees how far from the start of the possible
			 * letter's range it is
			 */
			double distanceFromRangeStart = random-letterRangeStartIndex(possibleChoice);
			
			//gets how large the possible letter's range is
			double rangeSize = weightedLetterChances.get(possibleChoice);
			
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
		return alphabet[Utilities.boundedRandomInt(0, alphabet.length-1)];
	}
	
	/*
	 * creates weighting hashmap, by defining "ret"
	 * and populating it with char keys and chance
	 * values. These chance values are hand-picked.
	 */
	private static HashMap<Character,Double> createLetterChanceMap() {
		HashMap<Character,Double> chances = new HashMap<Character,Double>();
		chances.put('A', 9.0); chances.put('B', 4.0);
		chances.put('C', 5.0); chances.put('D', 4.0);
		chances.put('E', 9.0); chances.put('F', 4.0);
		chances.put('G', 4.0); chances.put('H', 4.0);
		chances.put('I', 7.0); chances.put('J', 1.0);
		chances.put('K', 3.0); chances.put('L', 5.0);
		chances.put('M', 4.0); chances.put('N', 5.0);
		chances.put('O', 7.0); chances.put('P', 5.0);
		chances.put('Q', 1.0); chances.put('R', 5.0);
		chances.put('S', 7.0); chances.put('T', 5.0);
		chances.put('U', 6.5); chances.put('V', 2.0);
		chances.put('W', 2.0); chances.put('X', 1.0);
		chances.put('Y', 3.0); chances.put('Z', 1.0);
		
		return chances;
	}
	
	private static double getChanceTotal() {
		double total = 0;
		
		for(char c : alphabet) {
			total+=weightedLetterChances.get(c);
		}
		
		return total;
	}
	
	/*
	 * returns the start index of the range from  0 to
	 * the total chance value minus the chance of 'Z'
	 * that embodies the picking area for the passed char.
	 * Adds the size of the ranges from all prior letters
	 * to 0 such that the start index is the next number
	 * available outside of any other letters' ranges
	 */
	private static double letterRangeStartIndex(char c) {
		double start = 0;
		
		/*
		 * adds the area ranges for all prior letters,
		 * breaks after finding the letter
		 */
		for(char priorLetter : alphabet) {
			if(priorLetter==c) {
				break;
			}
			start+=weightedLetterChances.get(priorLetter);
		}
		return start;
	}
}