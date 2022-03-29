package data;

public class Utilities {
	public static int boundedRandom(int lowerBound, int upperBound) {
		return (int)(Math.random()*(upperBound-lowerBound+1) + lowerBound); 
	}
	
	public static void centerJFrame(javax.swing.JFrame frame) {
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		int windowWidth = frame.getSize().width;
		int windowHeight = frame.getSize().height;
		frame.setLocation(
				(screenWidth-windowWidth)/2,
				(screenHeight-windowHeight)/2
				);
	}
}
