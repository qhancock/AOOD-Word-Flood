// >:(
//NOT BEING USED

package gui;

import java.awt.image.BufferedImage;
import game.*;

public class BoardFrameCache {
// speed
	
	//mainPosition is initially null until first updated
	private Board.Position mainPosition = null;
	
	//mainScale is initally zero until updated
	private double mainScale = 0;
	
	//holds the mainFrame and its generator
	private BufferedImage mainFrame;
	private BoardFrameGenerator mainFrameGenerator;
	private FrameGeneratorAction mainFrameGeneratorAction;
	
	//holds the zoom frames and their generators
	private BufferedImage inFrame;
	private BoardFrameGenerator inFrameGenerator;
	private FrameGeneratorAction inFrameGeneratorAction;
	private BufferedImage outFrame;
	private BoardFrameGenerator outFrameGenerator;
	private FrameGeneratorAction outFrameGeneratorAction;
	
	//array with indices for each direction that hold adjacent frames and their generators
	BoardFrameGenerator[] adjacentFrameGenerators = new BoardFrameGenerator[Board.directions.length];
	BufferedImage[] adjacentFrames = new BufferedImage[Board.directions.length];
	FrameGeneratorAction[] adjacentFrameGeneratorActions = new FrameGeneratorAction[Board.directions.length];
	
	/*
	 * creates a new BoardFrameCache from a board, a number of maximum threads
	 * allocatable for the mainFrameDrawer and the max for each adjFrame
	 */
	public BoardFrameCache(Board refBoard, int maxMainFrameThreads, int maxAdjFrameThreads) {
		mainFrameGenerator = new BoardFrameGenerator(refBoard, maxMainFrameThreads);
		inFrameGenerator = new BoardFrameGenerator(refBoard, maxAdjFrameThreads);
		outFrameGenerator = new BoardFrameGenerator(refBoard, maxAdjFrameThreads);
	
		for(int dir : Board.directions) {
			adjacentFrameGenerators[dir] = new BoardFrameGenerator(refBoard, maxAdjFrameThreads);
		}
		
		createFrameGeneratorThreads();
	}
	
	public BufferedImage getMainFrame() {
		return mainFrame;
	}
	
	public void createFrameGeneratorThreads() {
		mainFrameGeneratorAction = new FrameGeneratorAction() {
			public void run() {
				mainFrameGenerator.setScale(scale);
				mainFrame = mainFrameGenerator.generateTileFrame(pos, scale);
			}
		};
		inFrameGeneratorAction = new FrameGeneratorAction() {
			public void run() {
				inFrameGenerator.setScale(scale);
				inFrame = inFrameGenerator.generateTileFrame(pos, scale);
			}
		};
		outFrameGeneratorAction = new FrameGeneratorAction() {
			public void run() {
				outFrameGenerator.setScale(scale);
				outFrame = outFrameGenerator.generateTileFrame(pos, scale);
			}
		};
		for(int dir : Board.directions) {
			adjacentFrameGeneratorActions[dir] = new FrameGeneratorAction() {
				public void run() {
					adjacentFrameGenerators[dir].setScale(scale);
					adjacentFrames[dir] = adjacentFrameGenerators[dir].generateTileFrame(pos, scale);
				}
			};
 		}
	}
	
	/*
	 * Main updater method that updates all frames given
	 * new information about the main positioning and scale.
	 * 
	 * Calls different helper methods depending on what's changed
	 */
	public void updateFrames(Board.Position newPos, double newScale) {
		
		System.out.println("updating frames");
		
		//if the main position hasn't changed with the new update
		boolean sameMainPos = mainPosition!=null && newPos.equals(mainPosition);
		
		//if the scale hasn't changed with the new update
		boolean sameScale = newScale==mainScale;
		
		//both the scale and position have changed
		if(!sameMainPos && !sameScale) {
			updateByScaleAndPosition(newPos, newScale);
			
		//only the position has changed	
		} else if (!sameMainPos) {
			updateByPosition(newPos);
			
		//only the scale has changed	
		} else if (!sameScale) {
			updateByScale(newScale);
		}
		
		/*
		 * ----------------------------------
		 * must always render new in/out images
		 */
		
		//whether or not it zooms to the maximum
		boolean maxIn = newScale==TileAssets.scaleIn(newScale);
		boolean maxOut = newScale==TileAssets.scaleOut(newScale);
		
		/*
		 * if it's zoomed in to the max, the new
		 * inFrame is the mainFrame, otherwise
		 * generates a new inFrame synchronously
		 */
		if(maxIn) {
			inFrame = mainFrame;
		} else {
			inFrameGeneratorAction.setArgs(newPos, TileAssets.scaleIn(newScale));
			new Thread(inFrameGeneratorAction).start();
		}
		
		/*
		 * if it's zoomed out to the max, the new
		 * outFrame is the mainFrame, otherwise
		 * generates a new inFrame synchronously
		 */
		if(maxOut) {
			outFrame = mainFrame;
		} else {
			outFrameGeneratorAction.setArgs(newPos, TileAssets.scaleOut(newScale));
			new Thread(outFrameGeneratorAction).start();
		}
		
	}
	
	/*
	 * updates frames by both scale and position changes. Assumes
	 * that both the scale and position have changed
	 */
	private void updateByScaleAndPosition(Board.Position newPos, double newScale) {
		
		//updates the main frame synchronously
		mainFrameGeneratorAction.setArgs(newPos, newScale);
		Thread mainFrameGeneratorThread = new Thread(mainFrameGeneratorAction);
		mainFrameGeneratorThread.start();
		
		/*
		 * iterates over different directions and updates
		 * the frames, synchronously
		 */
		for(int dir : Board.directions) {
			adjacentFrameGeneratorActions[dir].setArgs(newPos.getAdjacent(dir), newScale);
			new Thread(adjacentFrameGeneratorActions[dir]).start();
		}
		
		//updates the data with changes
		mainPosition = newPos;
		mainScale = newScale;
		
		if(mainFrameGeneratorThread!=null) {			
			try {
				mainFrameGeneratorThread.join();
			} catch (InterruptedException e) {}
		}
	}
	
	/*
	 * Updates frames by only a change in position. 
	 * Assumes that there *has* been a change in position.
	 */
	private void updateByPosition(Board.Position newPos) {
		
		//for readability
		Board.Position oldPos = mainPosition;
		
		/*
		 * holds frames for new adjacent images that
		 * have already been rendered from a different
		 * previous adjacent frame/main frame
		 */
		BufferedImage[] newPreRenderedAdjacents = new BufferedImage[adjacentFrames.length];
		
		/*
		 * holds the frame for the new main image, if
		 * it has been discovered to already have been
		 * rendered from a previous adjacent frame
		 */
		BufferedImage preRenderedMain = null;
		
		/*
		 * iterates over each adjacent position of the
		 * old mainPosition, to see if one or more might
		 * match the new adjacent positions and reuse their frames
		 */
		for(int oldPosDir : Board.directions) {
			
			/*
			 * if the new main position has already been rendered
			 * in the form of an old adjacent frame, saves the
			 * old adjacent frame to the preRenderedMain image
			 */
			if(newPos.equals(oldPos.getAdjacent(oldPosDir))) {
				preRenderedMain = adjacentFrames[oldPosDir];
			}
			
			/*
			 * iterates over each adjacent position of the new
			 * mainPosition, to see if one or more matches the
			 * currently checked adjacent position of the old mainPos
			 */
			for (int newPosDir : Board.directions) {
				if(oldPos.getAdjacent(oldPosDir).equals(newPos.getAdjacent(newPosDir))) {
					newPreRenderedAdjacents[newPosDir] = adjacentFrames[oldPosDir];
				}
			}
		}
		
		/*
		 * checks if any of the new adjacents are equal
		 * to the former main frame, and if it can be 
		 * reused
		 */
		for(int dir : Board.directions) {
			if(newPos.getAdjacent(dir).equals(oldPos)) {
				newPreRenderedAdjacents[dir] = mainFrame;
			}
		}
		
		/*
		 * updates the main frame synchronously if
		 * it wasn't found
		 */
		Thread mainFrameGeneratorThread = null;
		if(preRenderedMain!=null) {
			mainFrame = preRenderedMain;
		} else {
			mainFrameGeneratorAction.setArgs(newPos, mainScale);
			mainFrameGeneratorThread = new Thread(mainFrameGeneratorAction);
			mainFrameGeneratorThread.start();
		}
		
		/*
		 * updates the adjacent frames synchronously if they
		 * weren't found
		 */
		for(int dir : Board.directions) {
			if(newPreRenderedAdjacents[dir]!=null) {
				adjacentFrames[dir] = newPreRenderedAdjacents[dir];
			} else {
				adjacentFrameGeneratorActions[dir].setArgs(newPos.getAdjacent(dir), mainScale);
				new Thread(adjacentFrameGeneratorActions[dir]).start();
			}
		}
		
		/*
		 * if the main frame had to be newly
		 * generated, waits for it to be completed
		 * so it can be used to potentially
		 * generate the new in/out threads
		 */
		if(mainFrameGeneratorThread!=null) {
			try {
				mainFrameGeneratorThread.join();
			} catch (InterruptedException e) {}
		}
		
		mainPosition = newPos;
	}
	
	/*
	 * called if the position doesn't change but the scale does
	 */
	private void updateByScale(double newScale) {
		
		boolean oneOut = newScale==TileAssets.scaleOut(mainScale);
		boolean oneIn = newScale==TileAssets.scaleIn(mainScale);
			
		Thread mainFrameGeneratorThread = null;
		if(oneIn) {
			/*
			 * if the frame change zooms one in, can use the
			 * previous inFrame as the mainFrame and the prevous
			 * mainFrame as the outFrame.
			 */
			inFrame = mainFrame;
			mainFrame = outFrame;
		} else if(oneOut) {
			/*
			 * if the frame change zooms one out, can use the
			 * previous outFrame as the mainFrame and the prevous
			 * mainFrame as the inFrame.
			 */
			outFrame = mainFrame;
			mainFrame = inFrame;
		} else {
			/*
			 * otherwise, a whole new mainFrame
			 * needs to be created synchronously
			 */
			mainFrameGeneratorAction.setArgs(mainPosition, newScale);
			mainFrameGeneratorThread = new Thread(mainFrameGeneratorAction);
			mainFrameGeneratorThread.start();
		}
		
		/*
		 * generates new adjacent frames, each on a new thread
		 * (synchronous with other operations)
		 */
		for(int dir : Board.directions) {
			adjacentFrameGeneratorActions[dir].setArgs(mainPosition.getAdjacent(dir), newScale);
			new Thread(adjacentFrameGeneratorActions[dir]).start();
		}
		
		/*
		 * if the main frame had to be newly
		 * generated, waits for it to be completed
		 * so it can be used to potentially
		 * generate the new in/out threads
		 */
		if(mainFrameGeneratorAction!=null) {
			try {
				mainFrameGeneratorThread.join();
			} catch (InterruptedException e) {}
		}
		
		mainScale = newScale;
	}
	
	private class FrameGeneratorAction implements Runnable{
		Board.Position pos;
		double scale;
		
		public void setArgs(Board.Position pos, double scale) {
			this.pos = pos;
			this.scale = scale;
		}

		@Override
		public void run() {}
	}
}