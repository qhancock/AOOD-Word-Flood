package gui;

import java.awt.*;
import java.awt.image.*;

public class TileBuilder {
	
	//the color representing a backing for an invalid tile
	public static final Color INVALIDIIY_COLOR = new Color(255,32,32);
	public static final Color DEFAULT_TILE_COLOR = new Color(255,255,210);
	
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
	
	public static BufferedImage getTileOnly(double scale, char letter, boolean selected) {
		return getTile(scale, false, letter, true, selected, "");
	}
	
	public static BufferedImage getDefaultTile(double scale, char letter, boolean valid, boolean selected, String sides) {
		return getTile(scale, true, letter, valid, selected, sides);
	}
	
	//generates an asset with the specified scale, letter, and sides
	private static BufferedImage getTile(double scale, boolean background, char letter, boolean valid, boolean selected, String sides) {
		
		//gets the dimension (scale times base distance)
		int sideLength = (int)(TileAssets.UNSCALED_SIDE * scale);
		
		BufferedImage boardSquare = null;
		if(background) {			
			//gets the board square at proper scale
			boardSquare = TileAssets.getBoardSquare(scale);
		}
		
		//grabs component images from the TileAssets class
		BufferedImage backingImage = TileAssets.getTileAsset(scale, TileAssets.BACKING, sides);
		if (selected) backingImage = recolorBacking(backingImage, SELECTOR_LILY);
		else if(!valid) backingImage = recolorBacking(backingImage, INVALIDIIY_COLOR);
		else backingImage = recolorBacking(backingImage, DEFAULT_TILE_COLOR);
		
		BufferedImage letterImage = TileAssets.getTileAsset(scale, TileAssets.LETTER, Character.toString(letter));
		BufferedImage outlineImage = TileAssets.getTileAsset(scale, TileAssets.OUTLINE, sides);
		
		//creates a new image with the specified dimensions
		BufferedImage newImage = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_ARGB);
		Graphics newImageGraphics = newImage.getGraphics();
		
		//draws the images on the graphics for the image to be returned
		
		if(background) newImageGraphics.drawImage(boardSquare, 0, 0, null);
		newImageGraphics.drawImage(backingImage, 0, 0, null);
		newImageGraphics.drawImage(letterImage, 0, 0, null);
		newImageGraphics.drawImage(outlineImage, 0, 0, null);
		newImageGraphics.dispose();
		
		return newImage;
	}
	
	/*
	 * recolors a backing with the stated color
	 */
	private static BufferedImage recolorBacking(BufferedImage backing, Color color) {
		
		//creates new BufferedImage asset of same dimensions as given asset
		BufferedImage recoloredImage = new BufferedImage(backing.getWidth(), backing.getHeight(), BufferedImage.TYPE_INT_ARGB);

		//iterates over all rgb pixels
		for(int x = 0; x<recoloredImage.getWidth(); x++) {
			for(int y = 0; y<recoloredImage.getHeight(); y++) {
				
				/*
				 * if the rgb is equal to white, sets the rgb
				 * of the new, recolored, returned image
				 * to the rgb of the passed color
				 */
				if(backing.getRGB(x, y)==Color.white.getRGB()) {
					recoloredImage.setRGB(x, y, color.getRGB());
				}
			}
		}
		
		return recoloredImage;
	}
}
