import java.io.*;
import java.util.*;

public class Dictionary {
	
	private static ArrayList<String> dictionary = createDictionary();
	
	private static ArrayList<String> createDictionary() {
		
		//arraylist to be returned
		ArrayList<String> retDict = new ArrayList<String>();
		
		//file given by path to dictionary
		File dictFile = new File("dictionary.txt");
		
		//creates a bufferedReader from the file
		BufferedReader dictReader = null;
		try {
			dictReader = new BufferedReader(new FileReader(dictFile));
		} catch (FileNotFoundException e) {}
		
		//iterates over the reader and adds words to retDict
		String currentWord;
		try {
			while ((currentWord=dictReader.readLine())!=null) {
				retDict.add(currentWord.trim().toLowerCase());
			}
		} catch (IOException e) {}
		
		Collections.sort(retDict);
		
		return retDict;
	}
	
	/*
	 * checks if the passed "word" is valid in the
	 * dictionary using a binary-search implementation
	 */
	public static boolean validWord(String word) {
		
		//cleans the given word
		word = word.trim().toLowerCase();
		
		/*
		 * establishes initial indices for the
		 * lower search bound, upper search bound,
		 * and median pivot point
		 */
		int lBound = 0, uBound = dictionary.size()-1, pivot;
		
		//defines the word being held at the pivot
		String pivotWord;
		
		//while-loop binary search
		do {
			//defines pivot as midpoint
			pivot = (uBound-lBound)/2+lBound;
			
			//reassigns "pivotWord" to the dictionary word at the index
			pivotWord = dictionary.get(pivot);
			
			//if the pivot is the word, it exists
			if(pivotWord.compareTo(word)==0) {
				return true;
				
			/*
			 * if the pivot is higher alphabetically,
			 * halve the search range to the below the pivot
			 */
			} else if(pivotWord.compareTo(word)>0) {
				uBound = pivot-1;
				
			/*
			 * if the pivot is lower alphabetically,
			 * halve the search range to above the pivot
			 */
			} else if (pivotWord.compareTo(word)<0) {
				lBound = pivot+1;
			}
		
		//continue as long as the range can be searched
		} while (uBound-lBound>=0);
		
		return false;
	}
}