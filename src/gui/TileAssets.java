package gui;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

public class TileAssets {
	
	//base filepath for all assets
	private static final String assetsBase = "assets/tiling/scale=";
	
	//the different midsegments for asset types
	private static final String backingExt = "/tile_backings/tile_backing_";
	private static final String letterExt = "/tile_letters/tile_letter_";
	private static final String outlineExt = "/tile_outlines/tile_outline_";
	
	//extension suffix for a png file
	private static final String extSuf = ".png";
	
	//array of possible scale values that could be passed in
	public static final double[] scales = {0.5, 0.625, 0.75, 0.875, 1.0, 1.125, 1.25, 1.375, 1.5, 1.625, 1.75, 1.875, 2.0};
	
	//the possible constants for types of assets
	public static final int BACKING = 0, LETTER = 1, OUTLINE = 2;
	
	//possible keys for backings and outlines
	public static final String[] keys = {"", "a", "l", "b", "r", "lr", "ab", "al", "bl", "br", "ar", "abr", "alr", "abl", "blr", "ablr"};
	
	//letter constants
	public static final char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	
	//directory of triple-hashmaps (scaling double --> asset type constant --> letter or key specifier)
	private static HashMap<Double, HashMap<Integer, HashMap<String, BufferedImage>>> directory = new HashMap<Double, HashMap<Integer, HashMap<String, BufferedImage>>>();
	
	//tracks whether or not the asset buffer has been loaded yet
	private static boolean loaded = false;
	
	/*
	 * triple for loop to create new hashmaps at each layer
	 * with proper keys based on the previously defined 
	 * structure, loops through the possible keys and values
	 * at each layer to populate $directory
	 */
	public static void loadTileAssets() {
		
		if(loaded) return;
		
		//iterates over possible scales
		for(double scale : scales) {
			
			//adds a new hashmap to the directory at the specified scale key
			directory.put(scale, new HashMap<Integer, HashMap<String, BufferedImage>>());
			
			//iterates over possible asset types
			for(int type : new int[]{BACKING, LETTER, OUTLINE}) {
				
				//adds a new hashmap to the directory at the specified type key
				directory.get(scale).put(type, new HashMap<String, BufferedImage>());
				
				//decides which type folder to use based on the type constant
				String typeFolderExt = null;
				if(type==BACKING) {
					typeFolderExt = backingExt;
				} else if(type==LETTER) {
					typeFolderExt = letterExt;
				} else if(type==OUTLINE) {
					typeFolderExt = outlineExt;
				}
				
				if(type == LETTER) {
					
					//iterates over letters and adds a new buffered image for each letter
					for(char letter: letters) {
						File currentFile = new File(assetsBase + scale + typeFolderExt + letter + extSuf);
						
						BufferedImage currentImage = null;
						try {
							currentImage = ImageIO.read(currentFile);
						} catch (IOException e) {}
						directory.get(scale).get(type).put(Character.toString(letter), currentImage);
					}
				} else {
					
					//iterates over direction arguments and adds a new buffered image for each
					for(String key : keys) {
						File currentFile = new File(assetsBase + scale + typeFolderExt + key + extSuf);
						
						BufferedImage currentImage = null;
						try {
							currentImage = ImageIO.read(currentFile);
						} catch (IOException e) {}
						directory.get(scale).get(type).put(key, currentImage);
					}
				}
			}
		}
		
		//loading has completed
		loaded = true;
	}
	
	//returns an asset 
	public static BufferedImage getAsset(double scale, int type, String key) {
		if(!loaded) {
			loadTileAssets();
		}
		return directory.get(scale).get(type).get(key);
	}
}