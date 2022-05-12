package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class StartScreen extends JPanel{
	JPanel panel;
	JButton startButton;
	JLabel instructions;
	boolean change = false;
	
	public StartScreen() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		instructions = new JLabel("This is the instructions");
		panel.add(instructions);
		startButton = new JButton("Start Game");
		startButton.addActionListener(changeScreen);
		panel.add(startButton);
		add(panel);
	}
	
	private final ActionListener changeScreen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			change = true;
		}

	};
	
	public boolean changeScreen() {
		return change;
	}
	
	public void reset() {
		change = false;
	}
}
