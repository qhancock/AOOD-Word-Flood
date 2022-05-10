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
	static int screenNum = 1;
	JFrame frame;
	JPanel screen;
	private Timer timer = new Timer();

	/*
	class ChangeScreen extends TimerTask {
		public void run() {
			if (screenNum == 1) {
				screen = new StartScreen();
				frame.add(screen);
			} else if (screenNum == 2) {
				screen = new GameScreen(new Game());
				frame.repaint();
			} else if (screenNum == 3) {
				screen = new EndScreen();
			} else {
				timer.cancel();
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		}
	}
	ChangeScreen task = new ChangeScreen();
	*/

	public GUI() {
		//Issue when adding panel, need
		//A minimum dimension size or else things get clipped
		frame = new JFrame("Word Flood");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screen = new GameScreen(new Game());
		frame.add(screen, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		

	}

	public static void changeScreenNum(int num) {
		screenNum = num;
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
