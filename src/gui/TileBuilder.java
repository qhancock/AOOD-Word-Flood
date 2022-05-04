package gui;

import java.awt.*;
import java.awt.image.*;

public class TileBuilder {
	
	//generates an asset with the specified scale, letter, and sides
	public static BufferedImage getTile(double scale, char letter, String sides) {
		
		//gets the dimension (scale times base distance)
		int sideLength = (int)(TileAssets.UNSCALED_SIDE * scale);
		
		//grabs component images from the TileAssets class
		BufferedImage backingImage = TileAssets.getAsset(scale, TileAssets.BACKING, sides);
		BufferedImage letterImage = TileAssets.getAsset(scale, TileAssets.LETTER, Character.toString(letter));
		BufferedImage outlineImage = TileAssets.getAsset(scale, TileAssets.OUTLINE, sides);
		
		//creates a new image with the specified dimensions
		BufferedImage newImage = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_ARGB);
		Graphics newImageGraphics = newImage.getGraphics();
		
		//draws the images on the graphics for the image to be returned
		newImageGraphics.drawImage(backingImage, 0, 0, null);
		newImageGraphics.drawImage(letterImage, 0, 0, null);
		newImageGraphics.drawImage(outlineImage, 0, 0, null);
		newImageGraphics.dispose();
		
		return newImage;
	}
}
