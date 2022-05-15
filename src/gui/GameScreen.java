package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import game.Game;

public class GameScreen extends JPanel {
	Game game;
	boolean change = false;
	private int score = 0;
	private Timer timer = new Timer();
	private long start;
	private long end;
	private long timeLeft = 120;
	Object previous;
	JPanel panel2;
	JPanel panel;
	JLabel timerDisplay;
	JButton confirmButton;
	JLabel scoreDisplay;
	JButton gridPlacehold;
	JButton discardDisplay;
	MyButton firstDeckTile;
	MyButton secondDeckTile;
	MyButton thirdDeckTile;
	MyButton fourthDeckTile;
	MyButton fifthDeckTile;
	MyButton sixthDeckTile;
	MyButton seventhDeckTile;

	MyButton[] deckTiles = new MyButton[7];
	//Refreshes the screen/repaints it to keep the timer
	//and score updated
	//also auto ends the game once time runs out
	class Refresh extends TimerTask {
		public void run() {
			timeLeft = end - System.currentTimeMillis();
			if (timeLeft < 0) {
				timeLeft = 0;
				timerDisplay.setText("Time left: " + String.valueOf(timeLeft/1000));
				repaint();
			} else {
				timeLeft = end - System.currentTimeMillis();
				timerDisplay.setText("Time left: " + String.valueOf(timeLeft/1000));
				repaint();
			}
			if (timeLeft == 0 || timeLeft < 0) {
				timer.cancel();
				change = true;
			}
		}
	}
	Refresh refresh = new Refresh();
	
	class MyButton extends JToggleButton {
		int deckIndex = 0;
		public MyButton(Icon icon, int index) {
			super(icon);
			deckIndex = index;
		}
		
		public int getIndex() {
			return deckIndex;
		}
	}



	public GameScreen (Game gameRep) {
		//When displaying deck tiles,
		//retrieve from the instance of game

		//Same for grid display
		game = gameRep;
		System.out.println(game.getDeck().toString());
		for (int x = 0; x < 7; x++) {
			System.out.println(game.getDeck().getTile(x).getLetter());
		}
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		timerDisplay = new JLabel("Time left: " + 120);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel.add(timerDisplay, c);
		c.gridwidth = 0;

		scoreDisplay = new JLabel("Score: 0");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.ipadx = 100;
		panel.add(scoreDisplay, c);
		c.ipadx = 0;

		gridPlacehold = new JButton ("Test");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 7;
		c.gridheight = 2;
		c.weightx = 0.0;
		//c.ipadx = 300;
		c.ipady = 500;
		panel.add(gridPlacehold, c);
		c.ipady = 0;
		c.ipadx = 0;

		discardDisplay = new JButton ("Discard");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(discardDisplay, c);

		/*
		firstDeckTile = new JToggleButton(new ImageIcon(TileBuilder.getTile(0.5, Character.toLowerCase(game.getDeck().getTile(0).getLetter()), "", true)));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(firstDeckTile, c);
		deckTiles.add(firstDeckTile);

		secondDeckTile = new JToggleButton(new ImageIcon(TileBuilder.getTile(0.5, Character.toLowerCase(game.getDeck().getTile(1).getLetter()), "", true)));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(secondDeckTile, c);
		deckTiles.add(firstDeckTile);

		thirdDeckTile = new JToggleButton(new ImageIcon(TileBuilder.getTile(0.5, Character.toLowerCase(game.getDeck().getTile(2).getLetter()), "", true)));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(thirdDeckTile, c);
		deckTiles.add(firstDeckTile);

		fourthDeckTile = new JToggleButton(new ImageIcon(TileBuilder.getTile(0.5, Character.toLowerCase(game.getDeck().getTile(3).getLetter()), "", true)));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(fourthDeckTile, c);
		deckTiles.add(firstDeckTile);

		fifthDeckTile = new JToggleButton(new ImageIcon(TileBuilder.getTile(0.5, Character.toLowerCase(game.getDeck().getTile(4).getLetter()), "", true)));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(fifthDeckTile, c);
		deckTiles.add(firstDeckTile);

		sixthDeckTile = new JToggleButton(new ImageIcon(TileBuilder.getTile(0.5, Character.toLowerCase(game.getDeck().getTile(5).getLetter()), "", true)));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 6;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(sixthDeckTile, c);
		deckTiles.add(firstDeckTile);

		seventhDeckTile = new JToggleButton(new ImageIcon(TileBuilder.getTile(0.5, Character.toLowerCase(game.getDeck().getTile(6).getLetter()), "", true)));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 7;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(seventhDeckTile, c);
		deckTiles.add(firstDeckTile);
		 */
		for (int x=0; x < 7; x++) {
			MyButton deckTile;
			deckTile = new MyButton(new ImageIcon(TileBuilder.getTile(0.5, Character.toLowerCase(game.getDeck().getTile(x).getLetter()), "", true)), x);
			deckTile.addActionListener(tileDeckSelect);
			deckTiles[x] = deckTile;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = x + 1;
			c.gridheight = 1;
			c.gridy = 2;
			c.gridwidth = 1;
			panel.add(deckTile, c);
		}
		confirmButton = new JButton("Confirm Placement");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridheight = 1;
		c.gridy = 3;
		c.gridwidth = 8;
		panel.add(confirmButton, c);

		discardDisplay.addActionListener(discard);
		confirmButton.addActionListener(confirm);

		add(panel);

		start = System.currentTimeMillis();
		end = start + (2*60*1000);
		timeLeft = (end - System.currentTimeMillis());
		timer.scheduleAtFixedRate(refresh, 0, 100);
	}

	//Used when confirm button is pressed
	//Checks if the current board is valid, then updates timer
	//and updates board
	//displays popup otherwise
	private final ActionListener confirm = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (game.checkValid()) {
				game.confirmPlacement();
				scoreDisplay.setText("Score: " + String.valueOf(game.getBoard().getTiledPositions().size()));
			}
			
			for (int x = 0; x < 7; x++) {
				deckTiles[x].setSelected(false);
			}
			previous = null;
			repaint();
		}

	};

	//Used when discard button is pressed
	//Does nothing if a deck tile is not selected
	//Replaces a deck tile if selected and decreases the time
	private final ActionListener discard = new ActionListener() {
		//Should only work on deck tiles that are selected
		//need to find a way to identify index based on gui click
		@Override
		public void actionPerformed(ActionEvent e) {
			
			for (int x = 0; x < 7; x++) {
				if (deckTiles[x].isSelected()) {
					game.discard(x);
					end = end - 8000;
					timeLeft = end - System.currentTimeMillis();
					deckTiles[x].setIcon(new ImageIcon(TileBuilder.getTile(0.5, Character.toLowerCase(game.getDeck().getTile(x).getLetter()), "", true)));
					deckTiles[x].setSelected(false);
				}

				previous = null;
				repaint();
			}
		}

	};

	//Used when selecting a grid tile, 
	//Has different results depending on the previous object
	//and the object that is currently selected
	private final ActionListener tileGridSelect = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (previous.getClass().getName() == "gridTile") {
				game.swapGridTile();
			} else if (previous.getClass().getName() == "deckTile") {
				game.swapDeckTile();
			} else {
				previous = e.getSource();
			}
			repaint();
		}

	};

	//Used when selecting a deck tile, 
	//Has different results depending on the previous object
	//and the object that is currently selected
	private final ActionListener tileDeckSelect = new ActionListener()  {
		//Need to differentiate between deck and board tiles
		//Create a new jbutton/image class to differentiate?
		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			MyButton selected = (MyButton)obj;
			int index;

			if (previous instanceof MyButton) {
				index = ((MyButton) previous).getIndex();
				deckTiles[index].setSelected(false);
				previous = e.getSource();
			}

			/*
			if(previous.getClass().getName() == "deckTile") {
				previous = e.getSource();
			} else if (previous.getClass().getName() == "gridTile") {
				game.swapGridTile();
			} else {
				previous = e.getSource();
			}
			repaint();
			 */

			repaint();
		}

	};

	//Returns boolean for changing screen
	public boolean changeScreen() {
		return change;
	}

	//Returns the score
	public int getScore() {
		return score;
	}

	//Sets the screen change to false so it can be looped back to
	public void reset() {
		change = false;
	}
}