package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class StartScreen extends JPanel{
	JPanel panel;
	JButton startButton;
	JTextArea instructions;
	boolean change = false;

	public Dimension getPreferredSize() {
		return new Dimension(300, 265);
	}

	public StartScreen() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		instructions = new JTextArea("Welcome to word flood. "
				+ "In this game, you have 2 minutes "
				+ "to place letter tiles on a grid "
				+ "and form as many words as possible. "
				+ "Created words are only valid if "
				+ "they are spelled from left to right "
				+ "or from top to bottom. "
				+ "The player has a deck of 7 tiles they can choose letters from. "
				+ "If the player does not like one of their tiles, "
				+ "they may discard it and have it replaced with a new one. "
				+ "This reduces the time left by 8 seconds however. "
				+ "The end score is calculated by the total number of letters "
				+ "the player uses.");
		instructions.setSize(250, 200);
		instructions.setLineWrap(true);
		instructions.setWrapStyleWord(true);
		instructions.setOpaque(false);
		instructions.setEditable(false);
		instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(instructions);
		panel.add(Box.createRigidArea(new Dimension(0, 30)));
		startButton = new JButton("Start Game");
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.addActionListener(changeScreen);
		panel.add(startButton);
		add(panel);
	}

	//Changes boolean for screen change
	private final ActionListener changeScreen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			change = true;
		}

	};

	//Returns boolean for screen change
	public boolean changeScreen() {
		return change;
	}

	//Not exactly necessary
	//but a precaution
	public void reset() {
		change = false;
	}
}
