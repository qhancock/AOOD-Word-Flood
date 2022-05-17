package gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.*;
import game.*;

public class GameInterfacePanel extends JPanel {
	
	public ScorePanel scorePanel;
	public DeckPanel deckPanel;
	public ConfirmPanel confirmPanel;
	
	public GameInterfacePanel(ScorePanel scorePanel, DeckPanel deckPanel, ConfirmPanel confirmPanel) {
		super(new GridBagLayout());
		
		this.add(scorePanel);
		this.scorePanel = scorePanel;
		this.scorePanel.setPreferredSize(ScorePanel.dimensions);
		this.scorePanel.setSize(ScorePanel.dimensions);
		
		this.add(deckPanel);
		this.deckPanel = deckPanel;
		this.deckPanel.setPreferredSize(deckPanel.dimensions);
		this.deckPanel.setSize(deckPanel.dimensions);

		this.add(confirmPanel);
		this.confirmPanel = confirmPanel;
		this.confirmPanel.setPreferredSize(ConfirmPanel.dimensions);
		this.confirmPanel.setSize(ConfirmPanel.dimensions);

	}
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("test");
		
		ScorePanel scorePanel = new ScorePanel();
		TileDeck tileDeck = new TileDeck();
		tileDeck.fill();
		Dimension deckDims = new Dimension(900, 144);
		DeckDrawer deckDrawer = new DeckDrawer(0.625, deckDims, false);
		DeckPanel deckPanel = new DeckPanel(tileDeck, deckDrawer);
		
		ConfirmPanel confirmPanel = new ConfirmPanel();
		
		GameInterfacePanel gip = new GameInterfacePanel(scorePanel, deckPanel, confirmPanel);
		frame.add(gip);
		frame.setVisible(true);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
}
