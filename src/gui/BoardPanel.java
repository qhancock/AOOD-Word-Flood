package gui;

import javax.swing.*;
import game.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class BoardPanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{

	//px values for height and width of jpanel
	public Dimension window = new Dimension(800,600);

	//holds the board back-end that is referenced by this BoardPanel
	private Board board;

	//the current scale of the BoardPanel
	private double scale;

	//holds the offset from the top left corner (always between -scaledSide and 0)
	private int offsetX = 0, offsetY = 0;
	
	//holds the current top left position of this board
	private Board.Position currentTopLeftPosition;
	
	//holds the current whole frame
	private BufferedImage frame;
	
	//holds the frame generator
	BoardFrameDrawer frameGenerator;

	//creates a new board and sets size and scaling and adds event listeners
	public BoardPanel(Board board, Dimension dims) {
		this.board = board;
		this.window = dims;
		
		frameGenerator = new BoardFrameDrawer(this, 100);
		this.currentTopLeftPosition = board.new Position(-6,-8);
		this.setScale(0.625);
		updateFrame();
		
		this.setPreferredSize(dims);
		this.addKeyListener((KeyListener)this);
		this.addMouseListener((MouseListener)this);
		this.addMouseMotionListener((MouseMotionListener)this);
		this.addMouseWheelListener((MouseWheelListener)this);
		this.setFocusable(true);
	}
	
	public Board getBoard() {
		return this.board;
	}
	
	/*
	 * assumes a change has taken place, updates the 
	 * frame image being sub-imaged by the painter
	 */
	public void updateFrame() {
		frame = frameGenerator.generateTileFrame(currentTopLeftPosition, scale);
	}

	/*
	 * methods for zooming in and out
	 */
	private boolean canZoomIn() {
		return scale!=TileAssets.scaleIn(scale);
	}
	private void zoomIn() {
		setScale(TileAssets.scaleIn(scale));
	}
	private boolean canZoomOut() {
		return scale!=TileAssets.scaleOut(scale);
	}
	private void zoomOut() {
		setScale(TileAssets.scaleOut(scale));
	}

	/*
	 * sets the scale of this BoardPanel
	 */
	private double setScale(double scale) {
		if(this.scale!=scale) {
			this.scale = scale;
			updateFrame();
		}
		return scale;
	}

	/*
	 * called whenever a panning event takes place, changes
	 * the offset and topLeftPosition, updates frame if the
	 * frame position changes outside the bounds of the 
	 * offsetX or offsetY
	 */
	private void panViewfinder(int x_px, int y_px) {
		offsetX += x_px;
		offsetY += y_px;
		
		int scaledSide = TileAssets.getScaledSide(scale);
		
		int xTiles = offsetX/scaledSide + ((offsetX>0)?1:0);
		int yTiles = offsetY/scaledSide + ((offsetY>0)?1:0);

		offsetX = ((offsetX%scaledSide)-scaledSide)%scaledSide;
		offsetY = ((offsetY%scaledSide)-scaledSide)%scaledSide;

		currentTopLeftPosition = board.new Position(currentTopLeftPosition.row()-yTiles, currentTopLeftPosition.col()-xTiles);
		if(xTiles!=0 || yTiles!=0) {
			updateFrame();
		}
	}
	
	public void paintComponent(Graphics g) {
		BufferedImage window = frame.getSubimage(-offsetX, -offsetY, this.window.width, this.window.height);
		g.drawImage(window, 0, 0, null);
	}
	
	/*
	 * ---------------
	 * EVENT LISTENERS
	 * ---------------
	 */
	
	//tracks whether or not the space key is held
	private boolean space_modifier = false;
	
	//tracks whether or not the ctrl ket is held
	private boolean ctrl_modifier = false;
	
	//tracks whether or not the shift key is held
	private boolean shift_modifier = false;
	
	//tracks whether the right click button is held
	private boolean right_click_modifier = false;
	
	
	//tracks the location of the mouse at the start of the drag call (small differences)
	private Point formerDraggingMouseLocation = null;
	
	public void mouseClicked(MouseEvent e) {
		
		Point mouseLocationPostDrag = new Point(e.getX(), e.getY());
		
		Board.Position mousedPosition = getMousedTilePosition(mouseLocationPostDrag);
		board.reportSelection(mousedPosition);

		updateFrame();
		repaint();
	}
	
	/*
	 * if the right click is held, pan around
	 */
	public void mouseDragged(MouseEvent e) {
		if(right_click_modifier || space_modifier) {
			if(formerDraggingMouseLocation == null) formerDraggingMouseLocation = new Point(e.getX(), e.getY());
			panViewfinder(e.getX()-formerDraggingMouseLocation.x, e.getY()-formerDraggingMouseLocation.y);
			formerDraggingMouseLocation.setLocation(e.getX(), e.getY());
		
			this.repaint();
		}
	}
	
	private Point mouseLocationPreDrag = null;
	
	public void mousePressed(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3)  {
			right_click_modifier = true;
		}
		
		formerDraggingMouseLocation = new Point(e.getX(), e.getY());
		mouseLocationPreDrag = new Point(e.getX(), e.getY());
	}
	
	/*
	 * when a mouse is released, must determine whether or
	 * not a significant drag event has taken place, to tell
	 * whether or not a selection event should occur.
	 * 
	 * also updates whether or not the right click button was
	 * held.
	 */
	public void mouseReleased(MouseEvent e) {
		formerDraggingMouseLocation = null;
		
		Point mouseLocationPostDrag = new Point(e.getX(), e.getY());
		if(!right_click_modifier && !space_modifier) {			
			int transactionDistanceX = (int) Math.abs(mouseLocationPreDrag.getX()-mouseLocationPostDrag.getX());
			int transactionDistanceY = (int) Math.abs(mouseLocationPreDrag.getY()-mouseLocationPostDrag.getY());
			
			boolean inAcceptableClickArea = transactionDistanceX<=45 && transactionDistanceY<=45; 
			boolean trueClick = transactionDistanceX==0 && transactionDistanceY==0;

			if(inAcceptableClickArea && !trueClick) {
				mouseClicked(e);
			}
		}
		
		if(e.getButton()==MouseEvent.BUTTON3) right_click_modifier = false;
	}
	
	/*
	 * truifies modifiers
	 */
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			space_modifier = true;
		} else if (e.getKeyCode()==KeyEvent.VK_CONTROL) {
			ctrl_modifier = true;
		} else if (e.getKeyCode()==KeyEvent.VK_SHIFT) {
			shift_modifier = true;
		}
	}
	
	/*
	 * falsifies modifiers
	 */
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_SPACE) {
			space_modifier = false;
		} else if (e.getKeyCode()==KeyEvent.VK_CONTROL) {
			ctrl_modifier = false;
		} else if (e.getKeyCode()==KeyEvent.VK_SHIFT) {
			shift_modifier = false;
			this.requestFocus();
			this.requestFocusInWindow();
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		boolean forwards = e.getWheelRotation()<0;
		final int scrollIncrementPx = 25;
		
		this.requestFocusInWindow();
		this.requestFocus();
		
		/*
		 * ctrl modifier for zooming, zooms about a point
		 */
		if(ctrl_modifier) {
			if(forwards) {
				if(this.canZoomIn()) {
					int pointZoomOffsetX = (int) Math.round(((e.getX()*(1.0-(TileAssets.scaleIn(scale)/scale)))));
					int pointZoomOffsetY = (int) Math.round(((e.getY()*(1.0-(TileAssets.scaleIn(scale)/scale)))));
					this.panViewfinder(pointZoomOffsetX, pointZoomOffsetY);
					this.zoomIn();
				}
			} else {
				if(this.canZoomOut()) {
					int pointZoomOffsetX = (int) Math.round(((e.getX()*(1.0-(TileAssets.scaleOut(scale)/scale)))));
					int pointZoomOffsetY = (int) Math.round(((e.getY()*(1.0-(TileAssets.scaleOut(scale)/scale)))));
					this.panViewfinder(pointZoomOffsetX, pointZoomOffsetY);
					this.zoomOut();
				}
			}
			
		/*
		 * shift modifier for side to side scrolling
		 */
		} else if(shift_modifier) {
			if(forwards) {
				this.panViewfinder(scrollIncrementPx, 0);
			} else {
				this.panViewfinder(-scrollIncrementPx, 0);
			}
			
		/*
		 * up/down scrolling
		 */
		} else {
			if(forwards) {
				this.panViewfinder(0, scrollIncrementPx);
			} else {
				this.panViewfinder(0, -scrollIncrementPx);
			}
		}
		
		this.repaint();
	}
	
	public void keyTyped(KeyEvent k) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	
	/*
	 * returns the board position on which the mouseEvent
	 * was triggered.
	 */
	private Board.Position getMousedTilePosition(Point p) {
		
		int pixelsToTheRightOfLeftColumn = (int)(p.getX()-offsetX);
		int pixelsBelowTopRow = (int) (p.getY()-offsetY);
		
		int tilesToTheRightOfLeftColumn = pixelsToTheRightOfLeftColumn/TileAssets.getScaledSide(scale);
		int tilesBelowTopRow = pixelsBelowTopRow/TileAssets.getScaledSide(scale);
		
		int col = tilesToTheRightOfLeftColumn + currentTopLeftPosition.col();
		int row = tilesBelowTopRow + currentTopLeftPosition.row();
		
		return board.new Position(row, col);
	}
	

	public static void main(String[] args) {
		JFrame frame = new JFrame("NineTiles");
		BoardPanel panel = new BoardPanel(new Board(), new Dimension(1080, 720));
		Board.Position base = panel.board.new Position(0,0);
		base.left().left().putTile(new LetterTile());
		base.left().putTile(new LetterTile());
		base.putTile(new LetterTile());
		base.above().putTile(new LetterTile());
		base.left().above().putTile(new LetterTile());
		base.left().left().above().putTile(new LetterTile());
		base.above().above().putTile(new LetterTile());
		base.left().above().above().putTile(new LetterTile());
		base.left().left().above().above().putTile(new LetterTile());
		
		frame.add(panel);
		frame.pack();
		data.Utilities.centerJFrame(frame);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
	}
}