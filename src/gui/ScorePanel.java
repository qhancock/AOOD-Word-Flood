package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ScorePanel extends JPanel {

	private static final File scoreIndicatorAsset = new File("assets\\graphics\\scoring\\score_base.png");
	private BufferedImage indicatorBase;
	private BufferedImage scoreIndicator;
	
	private static final String scoreNumberFileBase= "assets\\graphics\\scoring\\scoring_numbers\\";
	BufferedImage[] scoreNumbers = new BufferedImage[10];
	
	public static Dimension dimensions;
	
	public ScorePanel() {
		
		for(int i = 0; i<scoreNumbers.length; i++) {
			try {
				scoreNumbers[i] = ImageIO.read(new File(scoreNumberFileBase + i + ".png"));
			} catch (IOException e) {
				System.out.println("yo");
			}
		}
		
		try {
			indicatorBase = ImageIO.read(scoreIndicatorAsset);
		} catch (IOException e) {}
		
		this.dimensions = new Dimension(indicatorBase.getWidth(), indicatorBase.getHeight());
		this.update(0000);
		
		this.setSize(this.dimensions);
		this.setPreferredSize(dimensions);
	}
	
	public void update(int score) {
		updateIndicator(score);
		this.repaint();
	}
	
	private void updateIndicator(int score) {
		
		String scoreString = Integer.toString(score);
		
		int leadingZeroes = 4 - scoreString.length();
		for(int i = 0; i<(leadingZeroes); i++) {
			scoreString = "0" + scoreString;
		}
		
		BufferedImage drawnScoreIndicator = new BufferedImage(indicatorBase.getWidth(), indicatorBase.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Graphics scoreDrawnImageGraphics = drawnScoreIndicator.getGraphics();
		
		scoreDrawnImageGraphics.drawImage(indicatorBase, 0, 0, null);
		scoreDrawnImageGraphics.drawImage(getScoreNumberImage(scoreString), 47, 80, null);
		
		this.scoreIndicator = drawnScoreIndicator;
	}

	private BufferedImage getScoreNumberImage(String score) {
		
		int maxWidth = 85;
		int maxHeight = 25;
		
		BufferedImage bi = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics big = bi.getGraphics();
		
		for(int i = 0; i<score.length(); i++) {
			big.drawImage(scoreNumbers[Integer.parseInt(Character.toString(score.charAt(i)))], i * (22), 0, null);
		}
		
		return bi;
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(this.scoreIndicator, 0, 0, null);
	}
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		ScorePanel sp = new ScorePanel();
		frame.add(sp);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		for(int i = 0; i<9999; i++) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {}
			
			sp.update(i);
		}
		
	}
	
}
