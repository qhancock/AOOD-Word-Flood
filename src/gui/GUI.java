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
	private Timer timer = new Timer();


	class ChangeScreen extends TimerTask {
		public void run() {
			
			if (startScreen.changeScreen() == true) {
				frame.remove(panel);
				gameScreen = new GameScreen(new Game());
				startScreen.revert();
				panel = gameScreen;
				frame.getContentPane().add(panel);
				frame.validate();
				frame.repaint();
				frame.pack();
			} 
			/*
			else if (gameScreen.changeScreen() == true) {
				endScreen = new EndScreen();
				frame.repaint();
			} else if (screenNum != 1 && endScreen.changeScreen() == true) {
				timer.cancel();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
			*/
		}
	}
	ChangeScreen task = new ChangeScreen();


	public GUI() {
		//Issue when adding panel, need
		//A minimum dimension size or else things get clipped
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
