package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import game.*;

public class DeckPanel extends JPanel implements MouseInputListener{
	
	//holds the tileDeck back-end that is referenced by this DeckPanel
	private TileDeck tileDeck;
	
	//px values for height and width of panel
	public Dimension dimensions;
	
	private BufferedImage deckFrame;
	public DeckDrawer deckDrawer;
	
	public DeckPanel(TileDeck tileDeck, DeckDrawer deckDrawer) {
		
		Dimension windowDimensions = deckDrawer.getDeckFrameDimensions();
		this.dimensions = windowDimensions;
		
		this.setPreferredSize(windowDimensions);
		
		this.tileDeck = tileDeck;
		this.deckDrawer = deckDrawer;
		
		this.updateDeckFrame();
		this.addMouseListener(this);
	}
	
	public void updateDeckFrame() {
		deckFrame = deckDrawer.drawTileDeck(tileDeck);
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(deckFrame, 0, 0, null);
	}
	
	/*
	 * --------------------------------------
	 * 	 INTERACTION AND EVENT LISTENERS
	 * --------------------------------------
	 */
	
	private int mousedIndex(Point p) {
		int innerAreaX = p.x - deckDrawer.getOuterPadding();
		int innerAreaY = p.y - deckDrawer.getVerticalPadding();
		
		int mousedSegment = innerAreaX/(deckDrawer.getTileSide() + deckDrawer.getInnerPadding());
		int segmentAreaX = innerAreaX - mousedSegment*(deckDrawer.getTileSide() + deckDrawer.getInnerPadding());
		
		boolean inInnerAreaX = innerAreaX>0 && (this.dimensions.width-p.x)>deckDrawer.getOuterPadding();
		boolean inInnerAreaY = innerAreaY>0 && (this.dimensions.height-p.y)>deckDrawer.getVerticalPadding();
		boolean inTile = segmentAreaX <= deckDrawer.getTileSide();
		
		if(inInnerAreaX && inInnerAreaY && inTile) {
			return mousedSegment;
		}
		return -1;
	}

	public void mouseClicked(MouseEvent e) {
		
		Point clickedPoint = new Point(e.getX(), e.getY());
		int mousedIndex = mousedIndex(clickedPoint);
		tileDeck.reportSelection(mousedIndex);
		
		this.updateDeckFrame();
		this.repaint();

	}
	
	
	
	
	/*
	 * determines if a significant click event has taken
	 * place, aka not a drag. If so, calls mouseClicked
	 */
	private Point mouseLocationPreDrag = null;
	public void mousePressed(MouseEvent e) {
		mouseLocationPreDrag = new Point(e.getX(), e.getY());
	}
	public void mouseReleased(MouseEvent e) {
		Point mouseLocationPostDrag = new Point(e.getX(), e.getY());
		int transactionDistanceX = (int) Math.abs(mouseLocationPreDrag.getX()-mouseLocationPostDrag.getX());
		int transactionDistanceY = (int) Math.abs(mouseLocationPreDrag.getY()-mouseLocationPostDrag.getY());

		boolean inAcceptableClickArea = transactionDistanceX<=75 && transactionDistanceY<=75; 
		boolean trueClick = transactionDistanceX==0 && transactionDistanceY==0;

		if(inAcceptableClickArea && !trueClick) {
			mouseClicked(e);
		}
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}

	public static void main(String[] args) {
		JFrame frame = new JFrame("test");
		TileDeck deck = new TileDeck();
		deck.fill();
		Dimension dims = new Dimension(1080,150);
		DeckDrawer drawer = new DeckDrawer(0.625, dims, false);
		DeckPanel dp = new DeckPanel(deck, drawer);
		dp.updateDeckFrame();
		dp.requestFocusInWindow();
		dp.requestFocus();
		
		frame.add(dp);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}