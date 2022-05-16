package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.Board;

/*
 * class that can generate bufferedImages
 * of an area of a board
 */
public class BoardFrameDrawer {
	
	//maximum number of threads that this instance is allowed to allocate
	private int maxThreads;
	
	//tracks the number of threads that this instance is currently operating
	private int runningThreads = 0;
	
	//holds all currently operating threads
	private ArrayList<TileDrawerThread> threads = new ArrayList<TileDrawerThread>();
	
	//holds the BoardPanel using this BFG
	private BoardPanel userPanel;
	
	//holds the board that is viewed when creating frames
	private Board drawnBoard;
	
	//holds the scale at which this instance renders frames
	private double scale;
	private int scaledSide;
	
	public BoardFrameDrawer(BoardPanel userPanel, int maxThreads) {
		this.userPanel = userPanel;
		this.drawnBoard = userPanel.getBoard();
		setMaxThreads(maxThreads);
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		scaledSide = TileAssets.getScaledSide(scale);
	}
	
	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}
	
	public void joinThreads() {
		/*
		 * waits for all currently drawing threads to
		 * die and synchronize before returning
		 */

		for(Thread t : this.threads) {
			try {
				t.join();
			} catch (InterruptedException e) {}
		}
		threads.clear();
	}
	
	/*
	 * generates a tile frame (all tiles that would appear in the image)
	 * from the top left tile on the board
	 */
	public BufferedImage generateTileFrame(Board.Position TLT, double scale) {
		setScale(scale);
		
		/*
		 * the horizontal and vertical tiles rendered are
		 * both equal to the number that can fit on the screen,
		 * plus two for good measure (could be any value)
		 */
		int horizontalTiles = userPanel.window.width/scaledSide+2;
		int verticalTiles = userPanel.window.height/scaledSide+2;
		
		//the frame to be returned
		BufferedImage tileFrame = new BufferedImage(horizontalTiles*scaledSide, verticalTiles*scaledSide, BufferedImage.TYPE_INT_ARGB);

		//creates the graphics object to draw on
		Graphics drawingTileFrame = tileFrame.getGraphics();

		//iterates over the number of vertical and horizontal tiles (2D)
		for(int row = 0; row<verticalTiles; row++) {
			for(int col = 0; col<horizontalTiles; col++) {

				/*
				 * stores the current position that is being rendered
				 * (the position that is <row> below TLT and <col> to
				 * the left of TLT
				 */
				Board.Position currentPos = drawnBoard.new Position(TLT.row() + row, TLT.col() + col);
				
				/*
				 * constantly attempts to create and
				 * deploy a new thread until it succeeds
				 * (will fail if max threads are already
				 * running)
				 */
				boolean threadCreateSuccess = false;
				do {
					try {
						new TileDrawerThread(currentPos, row, col, drawingTileFrame).start();
						threadCreateSuccess = true;
					} catch(Exception e) {}
				} while (!threadCreateSuccess);
			}
		}
		
		joinThreads();

		//returns the result of the render
		return tileFrame;
	}
	
	/*
	 * extends thread and draws on a graphics object
	 * with a tile for its task
	 */
	private class TileDrawerThread extends Thread {
		
		//the given position on the board
		Board.Position boardPos;
		
		//the row in the frame given
		int row;
		
		//the col in the frame given
		int col;
		
		//the graphics object on which it's drawing
		Graphics on;
		
		/*
		 * constructor for new TileDrawerThread:
		 * requires its position on the board,
		 * row, col, and graphics on which to draw
		 */
		public TileDrawerThread(Board.Position tileBoardPos, int row, int col, Graphics on) throws Exception {
			
			/*
			 * throws an exception if creating
			 * this thread would exceed the maximum
			 * number of threads allowed on this
			 * BoardFrameGenerator
			 */
			if(runningThreads+1 > maxThreads) throw new Exception("Maximum threads already running");
			
			//if it's allowed, passes this point and inits
			
			//increments the number of running threads
			runningThreads++;
			
			//adds this thread to the arrayList of operating threads
			threads.add(this);
			
			//initializes instance variables with constructor values
			this.boardPos = tileBoardPos;
			this.row = row;
			this.col = col;
			this.on = on;
			
		}
		
		/*
		 * primary task for a drawer thread
		 */
		public void run() {
			
			//the asset it needs to draw
			BufferedImage asset;
			
			/*
			 * sets the asset to the blank square
			 * if there's no tile, otherwise, get
			 * the proper asset from the TileBuilder
			 */
			if(boardPos.getTile()==null) {
				asset = TileAssets.getBoardSquare(scale);
			} else {
				asset = TileBuilder.getDefaultTile(scale, boardPos.getTile().getLetter(), boardPos.valid(), boardPos.getTile().selected(), boardPos.getSides());
			}
			
			/*
			 * draws the asset on the graphics object
			 * with the top left corner in the proper
			 * place (its row/col values which start
			 * at zero, multiplied by the increment)
			 */
			on.drawImage(asset, col*scaledSide, row*scaledSide, null);
			
			//decrements runningThreads b/c task is done
			runningThreads--;
		}
	}
}