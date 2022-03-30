package game;

public class Game {
	
	//Take score, need a getter for board
	public int score() {
		int score = 0;
		Board grid = getBoard();
		for (int x = 0; x < 250; x++) {
			for (int y = 0; y < 250; y++) {
				if (grid[y][x] != null) {
					score += 1;
				}
			}
		}
		
		return score;
	}
}