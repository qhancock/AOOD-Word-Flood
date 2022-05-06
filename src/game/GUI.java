package game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;


public class GUI extends JPanel{
	//Yes the variable names are bad I realize but I was lazy
	Game game;
	JFrame frame;
	JPanel panel;
	JLabel timerDisplay;
	JButton answer;
	JLabel input;
	JButton label1;
	JButton label2;
	JButton button;
	JButton button1;
	JButton button2;
	JButton button3;
	JButton button4;
	Object previous;
	private Timer timer = new Timer();
	private long start = System.currentTimeMillis();
	private long end = start + (2*60*1000);
	private long timeLeft = (end - System.currentTimeMillis());
	
	class endTask extends TimerTask {
		public void run() {
			JFrame f = new JFrame("End");
			PopupFactory pf = new PopupFactory();
			Popup pop = pf.getPopup(f, answer, ALLBITS, ABORT);
			timer.cancel();
		}
	}
	endTask task = new endTask();
	
	class Refresh extends TimerTask {
		public void run() {
			//It is decreasing time correctly for the variable, 
			//Odd issue with scheduling task,
			//Delay must be above a certain value to run,
			//at minimum, must be 350
			timeLeft = end - System.currentTimeMillis();
			System.out.println(timeLeft);
			timerDisplay.setText("Time left: " + String.valueOf(timeLeft/1000));
			frame.repaint();
		}
	}
	Refresh refresh = new Refresh();
	
	//Maybe should move timer to here for the sake of 
	//repainting/timer display
	//Need to add timer repainting
	//so score and time left are visible and correct
	//Attach actionListeners to button to modify their values
	public GUI(Game gameRep) {
		
		timer.schedule(task, 2 * 60 * 1000);
		
		timer.scheduleAtFixedRate(refresh, 350, 1000);
		
		game = gameRep;
		frame = new JFrame("DivisibleBy3");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		timerDisplay = new JLabel("Time left: " + String.valueOf(timeLeft/1000));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		panel.add(timerDisplay, c);
		c.gridwidth = 0;

		input = new JLabel("Score: 0");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.ipadx = 100;
		panel.add(input, c);
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

		label2 = new JButton ("Test2");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridheight = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		panel.add(label2, c);

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

		answer = new JButton("Bottom area");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridheight = 1;
		c.gridy = 3;
		c.gridwidth = 3;
		panel.add(answer, c);
		
		label1.addActionListener(paint);

		frame.setContentPane(panel);

		frame.pack();
		frame.setVisible(true);
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
				input.setText(String.valueOf(game.getBoard().getTiledPositions().size()));
				frame.repaint();
			}
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
			if (previous == deckTile) {
				game.discard(previous);
			}
			task.cancel();
			end = end - 8000;
			timeLeft = end - System.currentTimeMillis();
			timer.schedule(task, timeLeft);
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
	
	//Testing action listener that repaints the frame
	private final ActionListener paint = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.repaint();
			
		}
		
	};

	private static void runGUI() {
		//Used to start up the display, probably not necessary
		//Can be used if needing to test display
		JFrame.setDefaultLookAndFeelDecorated(true);

		GUI display = new GUI(new Game());
	}
	
	//Main method which runs the runGUI method
	public static void main (String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				runGUI();
			}
		});
	}

}