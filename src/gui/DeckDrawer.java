package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.*;

public class DeckDrawer {
	
	private double tileScale;
	private int outerPadding, innerPadding, verticalPadding;
	private int tileSide;
	
	private static final int numInnerPaddings = (TileDeck.MAX_TILES - 1);
	
	public DeckDrawer(double tileScale, int outerPadding, int innerPadding, int verticalPadding) {
		setDrawerParams(tileScale, outerPadding, innerPadding, verticalPadding);
	}
	
	public DeckDrawer(double tileScale, Dimension requestedDims, boolean clumped) {
		setDrawerParams(tileScale, requestedDims, clumped);
	}
	
	public int getOuterPadding() {
		return outerPadding;
	}
	
	public int getInnerPadding() {
		return innerPadding;
	}

	public int getVerticalPadding() {
		return verticalPadding;
	}
	
	public double getTileScale() {
		return tileScale;
	}
	
	public int getTileSide() {
		return tileSide;
	}
	
	private void setDrawerParams(double tileScale, int outerPadding, int innerPadding, int verticalPadding) {
		this.tileScale = tileScale;
		this.tileSide=TileAssets.getScaledSide(this.tileScale);
		
		this.outerPadding = outerPadding;
		this.innerPadding = innerPadding;
		this.verticalPadding = verticalPadding;
	}
	
	private void setDrawerParams(double tileScale, Dimension requestedDims, boolean clumped) {
		
		this.tileScale = tileScale;
		this.tileSide=TileAssets.getScaledSide(this.tileScale);
		
		int requestedWidth = requestedDims.width;
		int requestedHeight = requestedDims.height;
		
		int totalInnerTileWidth = (TileDeck.MAX_TILES * tileSide);
		
		int requestedOuterPadding;
		int requestedInnerPadding;
		int requestedVerticalPadding = (requestedHeight - tileSide) / 2 ;
		int totalRequestedInnerPadding;
		int totalRequestedOuterPadding;
		int totalRequestedInnerWidth;
				
		if(clumped) {
			requestedInnerPadding = requestedVerticalPadding;
			totalRequestedInnerPadding = (numInnerPaddings) * requestedInnerPadding;
			totalRequestedInnerWidth = totalInnerTileWidth + totalRequestedInnerPadding;
			totalRequestedOuterPadding = requestedWidth-totalRequestedInnerWidth;
			requestedOuterPadding = totalRequestedOuterPadding/2;
		} else {
			requestedOuterPadding = requestedVerticalPadding;
			totalRequestedOuterPadding = requestedOuterPadding * 2;
			totalRequestedInnerPadding = requestedWidth - totalRequestedOuterPadding - totalInnerTileWidth;
			requestedInnerPadding = totalRequestedInnerPadding / (numInnerPaddings);
			totalRequestedInnerWidth = requestedInnerPadding + totalInnerTileWidth;
		}
				
		//cannot fit requested tile size in proper height
		if(tileSide > requestedHeight) {
			throw new IllegalArgumentException("Cannot fit tiles of requested scale in image of requested height.");
		}
		
		//cannot accomodate requested tile size and padding with side padding
		if(totalRequestedOuterPadding + totalRequestedInnerWidth > requestedWidth) {
			throw new IllegalArgumentException("Cannot fit tiles of requested scale in image of requested width.");
		}
		
		for (int arg : new int[] {
			tileSide, requestedInnerPadding, requestedOuterPadding, 
			requestedVerticalPadding, totalRequestedInnerPadding, totalRequestedOuterPadding,
			totalRequestedInnerWidth, totalInnerTileWidth
		}) {
			if(arg<=0) throw new IllegalArgumentException("Illegal construction parameters.");
		}
		
		innerPadding = requestedInnerPadding;
		outerPadding = requestedOuterPadding;
		verticalPadding = requestedVerticalPadding;
		
	}
	
	public int getDeckFrameHeight() {
		int totalVerticalPadding = 2*verticalPadding;
		
		int totalHeight = totalVerticalPadding+tileSide;
		
		return totalHeight;
	}
	
	public int getDeckFrameWidth() {
		
		int totalOuterPadding = 2*outerPadding;
		int totalTileWidths = TileDeck.MAX_TILES * tileSide;
		int totalInnerPaddings = numInnerPaddings * innerPadding;
		
		int totalWidth = totalOuterPadding + totalTileWidths + totalInnerPaddings;
		
		return totalWidth;
	}
	
	public Dimension getDeckFrameDimensions() {
		
		int deckFrameWidth = getDeckFrameWidth();
		int deckFrameHeight = getDeckFrameHeight();
		
		return new Dimension(deckFrameWidth, deckFrameHeight);
		
	}
	
	public BufferedImage drawTileDeck(TileDeck tileDeck) {
		
		Dimension dims = this.getDeckFrameDimensions();
		
		BufferedImage drawnDeck = new BufferedImage(dims.width, dims.height, BufferedImage.TYPE_INT_ARGB);
		Graphics drawnDeckGraphics = drawnDeck.getGraphics();
		
		for(int tileIndex = 0; tileIndex<TileDeck.MAX_TILES; tileIndex++) {
			int startX = outerPadding + (tileIndex * (innerPadding + tileSide));
			int startY = verticalPadding;
			
			LetterTile currentTile = tileDeck.getTile(tileIndex);
			BufferedImage currentTileImage = TileBuilder.getTileOnly(tileScale, currentTile.getLetter(), currentTile.selected());
			
			drawnDeckGraphics.drawImage(currentTileImage, startX, startY, null);
		}
		
		return drawnDeck;
	}
		
}







