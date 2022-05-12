package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class EndScreen extends JPanel {
	JPanel panel;
	JLabel endScore;
	JLabel sessionScore;
	JButton retry;
	JButton exit;
	private boolean end = false;
	private boolean playAgain = false;
	
	public Dimension getPreferredSize() {
		return new Dimension(300, 200);
	}
	public EndScreen(int score, int highScore) {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	
		endScore = new JLabel("Your score was: " + score);
		endScore.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(endScore);
		sessionScore = new JLabel("Your highest score this session was: " 
		+ highScore);
		sessionScore.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(sessionScore);
		panel.add(Box.createRigidArea(new Dimension(0, 30)));
		JButton retry = new JButton("Play Again");
		retry.addActionListener(reset);
		retry.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(retry);
		panel.add(Box.createRigidArea(new Dimension(0, 30)));
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
