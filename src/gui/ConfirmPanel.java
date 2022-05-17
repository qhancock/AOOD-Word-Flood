package gui;

import javax.swing.*;

import game.Game;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.*;

public class ConfirmPanel extends JPanel implements MouseListener {
	
	public Game game = null;
	
	private static final String CONFIRMATION_ASSETS_BASE_PATH = "assets\\graphics\\confirmation\\";
	private static BufferedImage CONFIRM_LIT, CONFIRM_UNLIT, INVALID_LIT, INVALID_UNLIT;
	
	private BufferedImage currentImageState;
	
	public static Dimension dimensions;
	
	private boolean valid = true;
	private boolean hovered = false;
	
	public ConfirmPanel() {
		loadAssets();
		dimensions = new Dimension();
		dimensions.height = CONFIRM_LIT.getHeight();
		dimensions.width = CONFIRM_LIT.getWidth();
		
		this.setState(true, false);
		
		this.addMouseListener(this);
		this.setSize(dimensions);
		this.setPreferredSize(dimensions);
	}
	
	private static void loadAssets() {
		try {
			CONFIRM_LIT = ImageIO.read(new File(CONFIRMATION_ASSETS_BASE_PATH + "confirm_lit.png"));
			CONFIRM_UNLIT = ImageIO.read(new File(CONFIRMATION_ASSETS_BASE_PATH + "confirm_unlit.png"));
			INVALID_LIT = ImageIO.read(new File(CONFIRMATION_ASSETS_BASE_PATH + "invalid_lit.png"));
			INVALID_UNLIT = ImageIO.read(new File(CONFIRMATION_ASSETS_BASE_PATH + "invalid_unlit.png"));
		} catch (IOException e) {}
	}
	
	public void setValidity(boolean valid) {
		this.setState(valid, hovered);
	}
	
	public void setHovered(boolean hovered) {
		this.setState(valid, hovered);
	}
	
	private void setState(boolean valid, boolean hovered) {
		this.valid = valid;
		this.hovered = hovered;
		
		if(!this.valid) {
			if(!this.hovered) {
				currentImageState = INVALID_UNLIT;
			} else {
				currentImageState = INVALID_LIT;
			}
		} else {
			if(!this.hovered) {
				currentImageState = CONFIRM_UNLIT;
			} else {
				currentImageState = CONFIRM_LIT;
			}
		}
		this.repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(currentImageState, 0, 0, null);
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getComponent()==this) {
			this.setHovered(true);
			this.repaint();
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getComponent()==this) {
			this.setHovered(false);
			this.repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if(game == null) return;
		game.refillBar();
		game.window.deckPanel.updateDeckFrame();
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

	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
}