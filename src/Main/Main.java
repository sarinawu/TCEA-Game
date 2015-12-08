package Main;
import fileSystem.Sound;

/**
 * Begins the game.
 */
public class Main {
	
	public static Sound mainSound = new Sound("SummerDay.wav");

	/**
	 * Makes a menu, the portal to the game.
	 * @param args unused
	 */	
	public static void main(String[] args) {
		new engine.menu.Menu();
	}
}