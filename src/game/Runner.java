package game;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import engine.menu.Menu;
import engine.render.Screen;
import game.gameObjects.moveableObjects.Player;
import engine.menu.GameMenu;
import fileSystem.ResourceLoader;

/**
 * Controls the frame and level traversal.
 */
public class Runner {
	public static final String[] LEVELS = { "summer1.tmx", "summer2.tmx", "summer3.tmx", "autumn1.tmx", "autumn2.tmx",
			"autumn3.tmx", "winter1.tmx", "winter2.tmx", "winter3.tmx", "spring1.tmx", "spring2.tmx", "spring3.tmx" };
	private static int frameWidth = 800;
	private static int frameHeight = 590;
	private static JFrame gameFrame;
	private Screen screen;
	private Player player;
	private static GameEngine gameEngine;
	private GameMenu gameMenu;
	private static int levelIndex;
	private static int levelMAX;

	/**
	 * Creates a Runner that starts at the first level
	 */
	public Runner() {
		levelIndex = 0;
		levelMAX = 1;

		screen = new Screen(frameWidth, frameHeight, LEVELS[levelIndex++]);

		// open a gameFrame
		gameFrame = new JFrame();
		gameFrame.add(screen);
		gameFrame.setTitle("Snail Tale");
		player = screen.getPlayer();
		gameEngine = new GameEngine(screen, player, this);
		gameFrame.addKeyListener(gameEngine);
		gameFrame.setSize(frameWidth, frameHeight);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		gameFrame.setVisible(false);

		// open a gameMenuFrame
		gameMenu = new GameMenu(this, gameEngine);

		screen.addGameEngine(gameEngine);
	}

	/**
	 * Creates a Runner with the all levels up to the given int unlocked
	 * 
	 * @param i
	 *            the last level unlocked
	 */
	public Runner(int i) {
		levelIndex = 0;
		levelMAX = levelMAX < i ? i : levelMAX;

		screen = new Screen(frameWidth, frameHeight, LEVELS[levelIndex++]);

		// open a gameFrame
		gameFrame = new JFrame();
		gameFrame.add(screen);
		gameFrame.setTitle("Snail Tale");
		player = screen.getPlayer();
		gameEngine = new GameEngine(screen, player, this);
		gameFrame.addKeyListener(gameEngine);
		gameFrame.setSize(frameWidth, frameHeight);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setResizable(false);
		gameFrame.setVisible(false);

		// open a gameMenuFrame
		gameMenu = new GameMenu(this, gameEngine);

		screen.addGameEngine(gameEngine);
	}

	/**
	 * Shows a specific level.
	 * 
	 * @param i
	 *            the index of the level to show.
	 */
	public void showLevel(int i) {
		levelIndex = i;
		gameFrame.setVisible(false);
		gameFrame.remove(screen);
		screen = new Screen(frameWidth, frameHeight, LEVELS[levelIndex++], levelIndex);

		gameFrame.add(screen);
		player = screen.getPlayer();

		gameEngine.setScreen(screen);
		
		startGameEngineTimer();

		gameFrame.revalidate();

		screen.addGameEngine(gameEngine);
		gameFrame.setVisible(true);
	}

	/**
	 * Called when a level ends. Either goes on to the next level, or shows the
	 * end game animation.
	 */
	public void endLevel() {
		if (levelIndex == LEVELS.length) {
			gameEngine.stopMainTimer();
			levelIndex++;
			levelMAX = levelIndex + 1;

			gameFrame.setVisible(false);
			gameFrame.remove(screen);

			gameFrame.removeKeyListener(gameEngine);
			gameFrame.setSize(1000, frameHeight);
			EndGameComponent egc = new EndGameComponent();
			gameFrame.add(egc);
			final EndGameRunnable egr = new EndGameRunnable(egc);

			Thread t = new Thread(egr);
			t.start();

			gameFrame.revalidate();

			gameFrame.setVisible(true);

		} else if (levelIndex < LEVELS.length) {
			if (levelMAX < LEVELS.length)
				levelMAX = levelIndex + 1;
			gameFrame.setVisible(false);
			gameFrame.remove(screen);

			screen = new Screen(frameWidth, frameHeight, LEVELS[levelIndex++], levelIndex);

			gameFrame.add(screen);
			player = screen.getPlayer();

			gameEngine.setScreen(screen);

			startGameEngineTimer();

			gameFrame.revalidate();
			screen.addGameEngine(gameEngine);
			gameFrame.setVisible(true);

		}

	}

	/**
	 * Gets rid of the game frame.
	 */
	public void exit() {
		gameFrame.dispose();
	}

	/**
	 * Shows the game menu instead of the map.
	 */
	public void switchToGameMenu() {
		gameMenu.setGameMenuVisible(true);
		gameFrame.setVisible(false);
	}

	/**
	 * Shows the map instead of the game menu.
	 */
	public void switchToGameFrame() {
		gameFrame.setVisible(true);
		gameMenu.setGameMenuVisible(false);
	}

	/**
	 * Repaints the game frame
	 */
	public void repaintGameFrame() {
		gameFrame.repaint();
	}

	/**
	 * Returns the GameEngine used
	 * 
	 * @return the GameEngine used
	 */
	public GameEngine getGameEngine() {
		return gameEngine;
	}

	/**
	 * Shows or does not show the game frame
	 * 
	 * @param b
	 *            whether the game frame should be shown
	 */
	public void setGameFrameVisible(boolean b) {
		gameFrame.setVisible(b);
	}

	/**
	 * Updates the player to the player currently being shown.
	 */
	public void updatePlayer() {
		player = screen.getPlayer();
	}

	/**
	 * Starts the animation timer.
	 */
	public void startGameEngineTimer() {
		gameEngine.startMainTimer();
	}

	/**
	 * Gets the index of the current level.
	 * 
	 * @return the index of the current level
	 */
	public int getLevelIndex() {
		return levelIndex;
	}

	/**
	 * Gets the index of the max level reached.
	 * 
	 * @return the index of the max level reached
	 */
	public int getLevelMAX() {
		return levelMAX;
	}

	/**
	 * Creates a component that shows the end level animation.
	 */
	private static class EndGameComponent extends JComponent {
		private static final long serialVersionUID = 8885174686379486132L;
		private int[] coords;
		private boolean right, stillGoing;

		/**
		 * Creates a new component
		 */
		public EndGameComponent() {
			coords = new int[] { 108, 322 };
			right = false;
			stillGoing = true;
		}

		/**
		 * updates the coordinate of the little snail.
		 * 
		 * @param c
		 *            the coordinates of the little snail
		 * @param r
		 *            whether the snail is facing right
		 */
		public void update(int[] c, boolean r) {
			coords = c;
			right = r;
			repaint();
		}

		/**
		 * Removes the snail from the picture
		 */
		public void noSnail() {
			stillGoing = false;
		}

		/**
		 * Paints stuff.
		 * 
		 * @param g
		 *            the graphics to draw on
		 */
		public void paintComponent(Graphics g) {
			g.clearRect(0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());
			ResourceLoader res = new ResourceLoader();
			g.drawImage(res.loadImage("victorybackground2.png"), 0, 0, this);
			if (stillGoing) {
				if (right)
					g.drawImage(res.loadImage("snailWalk1r.png"), coords[0], coords[1], 25, 14, this);
				else
					g.drawImage(res.loadImage("snailWalk1l.png"), coords[0], coords[1], 25, 14, this);
			}

		}
	}

	/**
	 * Controls the animation.
	 */
	private static class EndGameRunnable implements Runnable {
		private EndGameComponent comp;
		private int[] coords;
		private int xVel, yVel;
		private boolean right;
		int leg;

		/**
		 * Creates a new Runnable.
		 * 
		 * @param egc
		 *            the component that shows the animation
		 */
		public EndGameRunnable(EndGameComponent egc) {
			comp = egc;
			coords = new int[] { 108, 322 };
			xVel = -1;
			yVel = 0;
			right = false;
			leg = 1;
		}

		/**
		 * Prematurely ends the animation
		 */
		public void end() {
			leg = 6;
		}

		/**
		 * Moves the snail using a series of legs of the trip. Leg 1: snail
		 * moves off platform Leg 2: snail moves down and to the left Leg 3:
		 * snail moves down only. Leg 4: snail hits the floor, starts moving to
		 * the right Leg 5: snail moves up towards the door Leg 6: animation
		 * ends
		 */
		public void run() {

			while (!Thread.interrupted()) {
				try {
					Thread.sleep(40);
				} catch (InterruptedException ie) {
				}

				coords[0] += xVel;
				coords[1] += yVel;
				if (xVel > 0)
					right = true;
				else if (xVel < 0)
					right = false;
				comp.update(coords, right);
				boolean add = false;
				switch (leg) {
				case 1:
					if (coords[0] < 86)
						add = true;
					break;
				case 2:
					if (coords[0] < 70)
						add = true;
					//press any key to continue
					gameFrame.addKeyListener(new KeyListener() {
						public void keyPressed(KeyEvent e) {
							end();
						}

						public void keyReleased(KeyEvent e) {
						}

						public void keyTyped(KeyEvent e) {
							end();
						}
					});
					yVel += 1;
					xVel = -1;
					break;
				case 3:
					if (coords[1] > 400)
						add = true;
					yVel += 1;
					xVel = 0;
					break;
				case 4:
					if (coords[0] > 721 - (coords[1] - 400))
						add = true;
					yVel = 0;
					xVel = 1;
					break;
				case 5:
					yVel = -1;
					if (new Rectangle(coords[0], coords[1], 25, 14).intersects(new Rectangle(721, 386, 40, 10))) {
						add = true;
						xVel = 0;
						yVel = 0;
						comp.noSnail();

					}
					break;
				case 6:
					gameFrame.setVisible(false);
					gameFrame.setSize(frameWidth, frameHeight);
					int[] buttons = gameEngine.getBindings();
					gameFrame.dispose();
					Menu menu = new Menu(levelMAX);
					menu.gameEngine.setKeyBindingLeft(buttons[0]);
					menu.gameEngine.setKeyBindingUp(buttons[1]);
					menu.gameEngine.setKeyBindingRight(buttons[2]);
					menu.gameEngine.setKeyBindingRestart(buttons[3]);
					menu.gameEngine.setKeyBindingPause(buttons[4]);
					menu.updateButtons();

					Thread.currentThread().interrupt();
				}
				if (add)
					leg++;
			}
		}
	}

}
