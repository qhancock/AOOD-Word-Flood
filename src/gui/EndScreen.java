package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class EndScreen extends JPanel {
	JPanel panel;
	JLabel endScore;
	JButton retry;
	JButton exit;
	private boolean end = false;
	private boolean playAgain = false;
	
	public Dimension getPreferredSize() {
		return new Dimension(300, 300);
	}
	public EndScreen(int score) {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	
		endScore = new JLabel("Your score was: " + score);
		endScore.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(endScore);
		JButton retry = new JButton("Play Again");
		retry.addActionListener(reset);
		retry.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(retry);
		JButton exit = new JButton("Exit Game");
		exit.setAlignmentX(Component.CENTER_ALIGNMENT);
		exit.addActionListener(endGame);
		panel.add(exit);
		
		add(panel);
	}
	
	private final ActionListener reset = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			playAgain = true;
			end = false;
		}

	};
	
	private final ActionListener endGame = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			end = true;
			playAgain = false;
		}

	};
	
	public boolean end() {
		return end;
	}
	
	public boolean playAgain() {
		return playAgain;
	}
}
