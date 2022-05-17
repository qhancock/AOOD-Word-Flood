package gui;

import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;

public class TileAssets {
	
	//constant height/width ratios
	public static final int UNSCALED_SIDE = 144;
	
	//base filepath for all assets
	private static final String assetsBase = "assets/tiling/scale=";
	
	//the different midsegments for asset types
	private static final String backingExt = "/tile_backings/tile_backing_";
	private static final String letterExt = "/tile_letters/tile_letter_";
	private static final String outlineExt = "/tile_outlines/tile_outline_";
	
	//extension suffix for a png file
	private static final String extSuf = ".png";
	
	//array of possible scale values that could be passed in
	public static final double scaleIncrement = 0.125;
	public static final double[] scales = {0.5, 0.625, 0.75, 0.875, 1.0, 1.125, 1.25, 1.375, 1.5, 1.625, 1.75, 1.875, 2.0};
	
	//the possible constants for types of assets
	public static final int BACKING = 0, LETTER = 1, OUTLINE = 2;
	
	//possible keys for backings and outlines
	public static final String[] keys = {"", "a", "l", "b", "r", "lr", "ab", "al", "bl", "br", "ar", "abr", "alr", "abl", "blr", "ablr"};
	
	//letter constants
	public static final char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	
	//the color representing a backing for an invalid tile
	public static final Color INVALIDITY_COLOR = new Color(255,32,32);
	
	//the default color of a tile backing (that pale yellow/brown)
	public static final Color DEFAULT_TILE_COLOR = new Color(255,255,210);
	
	//the default color of a blank square (that gray blue)
	public static final Color DEFAULT_SQUARE_COLOR = new Color(0x5476AA);
	
	/*
	 * colors that can be used as possible
	 * colors for the tile background when
	 * it is selected
	 */
	public static final Color SELECTOR_MINT = new Color(0x00FFC8);
	public static final Color SELECTOR_GOLD = new Color(0xFFEB64);
	public static final Color SELECTOR_SNOW = new Color(0xBEFFFF);
	public static final Color SELECTOR_AQUA = new Color(0x69A5F0);
	public static final Color SELECTOR_LILY = new Color(0xB9A0FF);
	public static final Color SELECTOR_ROSE = new Color(0xFFB6E8);
	
	//directory of triple-hashmaps (scaling double --> asset type constant --> letter or key specifier)
	private static HashMap<Double, HashMap<Integer, HashMap<String, BufferedImage>>> directory = new HashMap<Double, HashMap<Integer, HashMap<String, BufferedImage>>>();
	
	//tracks whether or not the asset buffer has been loaded yet
	private static boolean loaded = loadTileAssets();
	
	public static int getScaledSide(double scale) {
		return (int)(UNSCALED_SIDE * scale);
	}
	
	public static double scaleIn(double currentScale) {
		if(currentScale!=TileAssets.scales[TileAssets.scales.length-1]) {
			return currentScale + TileAssets.scaleIncrement;
		} else {
			return currentScale;
		}
	}
	public static double scaleOut(double currentScale) {
		if(currentScale!=TileAssets.scales[0]) {
			return currentScale - TileAssets.scaleIncrement;
		} else {
			return currentScale;
		}
	}
	
	/*
	 * triple for loop to create new hashmaps at each layer
	 * with proper keys based on the previously defined 
	 * structure, loops through the possible keys and values
	 * at each layer to populate $directory
	 */
	public static boolean loadTileAssets() {
		
		if(loaded) return true;
		
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
		return true;
	}
	
	//returns an asset 
	public static BufferedImage getTileAsset(double scale, int type, String key) {
		if(!loaded) {
			loadTileAssets();
		}
		
		return directory.get(scale).get(type).get(key);
	}
	
	public static BufferedImage getBoardSquare(double scale) {
		File boardSquareFile = new File(assetsBase+scale+"\\board_square.png");
		BufferedImage ret = null;
		try {
			ret = ImageIO.read(boardSquareFile);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return ret;
	}
}