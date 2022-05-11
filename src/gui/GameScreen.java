package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	JButton label1;
	JButton discardDisplay;
	JButton button;
	JButton button1;
	JButton button2;
	JButton button3;
	JButton button4;
	
	class Refresh extends TimerTask {
		public void run() {
			//It is decreasing time correctly for the variable, 
			//Seems to throw an error but still runs?
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
				System.out.println("Game end");
			}
			System.out.println(timeLeft);
		}
	}
	Refresh refresh = new Refresh();

	

	public GameScreen (Game gameRep) {
		game = gameRep;

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

		label1 = new JButton ("Test");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 2;
		c.weightx = 0.0;
		c.ipadx = 300;
		c.ipady = 500;
		panel.add(label1, c);
		c.ipady = 0;
		c.ipadx = 0;

		discardDisplay = new JButton ("Discard");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(discardDisplay, c);

		button = new JButton("Second button");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(button, c);

		button1 = new JButton("Second button");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(button1, c);

		button2 = new JButton("Second button");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(button2, c);

		button3 = new JButton("Second button");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(button3, c);

		button4 = new JButton("Second button");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(button4, c);

		confirmButton = new JButton("Confirm Placement");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridheight = 1;
		c.gridy = 3;
		c.gridwidth = 3;
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
			/*
		if (game.checkValid()) {
			game.confirmPlacement();
			scoreDisplay.setText(String.valueOf(game.getBoard().getTiledPositions().size()));
			frame.repaint();
		}
			 */
			score = score + 1;
			scoreDisplay.setText("Score: " + score);
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
			//if (previous == deckTile) {
			//	game.discard(previous);
			//}
			end = end - 8000;
			timeLeft = end - System.currentTimeMillis();
			//timer.schedule(task, timeLeft);
		}

	};

	//Used when selecting a grid tile, 
	//Has different results depending on the previous object
	//and the object that is currently selected
	private ActionListener tileGridSelect = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (previous == gridTile) {
				game.swapGridTile();
			} else if (previous == deckTile) {
				game.swapDeckTile();
			} else {
				previous = e.getSource();
			}

		}

	};

	//Used when selecting a deck tile, 
	//Has different results depending on the previous object
	//and the object that is currently selected
	private final ActionListener tileDeckSelect = new ActionListener() {
		//Need to differentiate between deck and board tiles
		//Create a new jbutton/image class to differentiate?
		@Override
		public void actionPerformed(ActionEvent e) {
			if(previous == deckTile) {
				previous = e.getSource();
			} else if (previous == gridTile) {
				game.swapGridTile();
			} else {
				previous = e.getSource();
			}
		}

	};
	
	public boolean changeScreen() {
		return change;
	}

	public int getScore() {
		return score;
	}
}