package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
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

import game.Game;


public class GUI{
	JFrame frame;
	JPanel panel;
	StartScreen startScreen;
	GameScreen gameScreen;
	EndScreen endScreen;
	private int screenNum = 1;
	private int highScore = 0;
	private Timer timer = new Timer();

	//Task runs constantly to check if the screen
	//needs to be changed to the next one
	//also loops/exits on the exit screen as necessary
	class ChangeScreen extends TimerTask {
		public void run() {

			if (screenNum == 1 && startScreen.changeScreen() == true) {
				frame.remove(panel);
				gameScreen = new GameScreen(new Game());
				panel = gameScreen;
				frame.getContentPane().add(panel);
				frame.validate();
				frame.repaint();
				frame.pack();
				screenNum = 2;
			}

			if (screenNum == 2 && gameScreen.changeScreen() == true) {
				frame.remove(panel);
				if (gameScreen.getScore() > highScore) {
					highScore = gameScreen.getScore();
				}
				endScreen = new EndScreen(gameScreen.getScore(), highScore);
				panel = endScreen;
				frame.getContentPane().add(panel);
				frame.validate();
				frame.repaint();
				frame.pack();
				screenNum = 3;
				gameScreen.reset();
			} 
			if (screenNum == 3 && endScreen.end() == true) {
				timer.cancel();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			} else if (screenNum == 3 && endScreen.playAgain() == true) {
				screenNum = 1;
			}

		}
	}
	ChangeScreen task = new ChangeScreen();


	public GUI() {
		frame = new JFrame("Word Flood");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startScreen = new StartScreen();
		panel = startScreen;
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		timer.scheduleAtFixedRate(task, 0, 100);

	}

	//Testing action listener that repaints the frame
	private final ActionListener paint = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.repaint();

		}

	};

	public int highScore() {
		return highScore;
	}

	private static void runGUI() {
		//Used to start up the display, probably not necessary
		//Can be used if needing to test display
		JFrame.setDefaultLookAndFeelDecorated(true);

		GUI display = new GUI();
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
