package gui;

import game.*;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GameWindow extends JPanel {
	
	private Game game = new Game();
	
	protected JLabel title;
	
	protected BoardPanel boardPanel = new BoardPanel(game.getBoard(), new Dimension(1344,576));
	
	protected ScorePanel scorePanel = new ScorePanel();
	protected DeckPanel deckPanel = new DeckPanel(game.getDeck(), new DeckDrawer(0.75, new Dimension(984,144), true));
	protected ConfirmPanel confirmPanel = new ConfirmPanel();
	protected GameInterfacePanel gameInterfacePanel;

	private static final File titleImageFile = new File("assets\\graphics\\WordFlood Title.png");
	
	public GameWindow() {
		super(new GridBagLayout());
		BufferedImage titleImage = null;
		try {
			titleImage = ImageIO.read(titleImageFile);
		} catch (IOException e) {}
		this.title = new JLabel(new ImageIcon(titleImage));
		
		this.gameInterfacePanel = new GameInterfacePanel(scorePanel, deckPanel, confirmPanel);
		
		GridBagConstraints c1;
		c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;
		this.add(title, c1);
		
		GridBagConstraints c2;
		c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.gridy = 1;
		this.add(boardPanel, c2);
		
		GridBagConstraints c3;
		c3 = new GridBagConstraints();
		c3.gridx = 0;
		c3.gridy = 2;
		this.add(gameInterfacePanel, c3);
		
		game.window = this;
		confirmPanel.game = game;
	}
	
	public void updateAll() {
		this.deckPanel.updateDeckFrame();
		this.boardPanel.updateFrame();
		this.boardPanel.repaint();
	}
	
	public void updateConfirmPanel(boolean b) {
		confirmPanel.setValidity(b);
	}
	
	public void updateScorePanel(int score) {
		scorePanel.update(score);
	}
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("please");
		frame.setSize(new Dimension(1344, 864));
		GameWindow window = new GameWindow();
		frame.add(window);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		data.Utilities.centerJFrame(frame);
		
		frame.setVisible(true);
		
	}
	
}
