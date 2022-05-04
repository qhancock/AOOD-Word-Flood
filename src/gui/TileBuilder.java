package gui;

import java.awt.*;
import java.awt.image.*;

public class TileBuilder {
	public static BufferedImage getTile(double scale, char letter, String sides) {
		int sideLength = (int)(TileAssets.UNSCALED_SIDE * scale);
		
		BufferedImage backingImage = TileAssets.getAsset(scale, TileAssets.BACKING, sides);
		BufferedImage letterImage = TileAssets.getAsset(scale, TileAssets.LETTER, Character.toString(letter));
		BufferedImage outlineImage = TileAssets.getAsset(scale, TileAssets.OUTLINE, sides);
		
		BufferedImage newImage = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_ARGB);
		Graphics newImageGraphics = newImage.getGraphics();
		
		newImageGraphics.drawImage(backingImage, 0, 0, null);
		newImageGraphics.drawImage(letterImage, 0, 0, null);
		newImageGraphics.drawImage(outlineImage, 0, 0, null);
		newImageGraphics.dispose();
		
		return newImage;
	}
}
