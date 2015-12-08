package game;

import engine.render.Drawer;
import engine.render.Screen;
import game.gameObjects.moveableObjects.JumpingEnemy;
import game.gameObjects.moveableObjects.Player;
import game.gameObjects.stationaryObjects.DisappearingPlatform;
import game.gameObjects.stationaryObjects.EndObject;
import game.gameObjects.stationaryObjects.StationaryPlatform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.Timer;

/**
 * Handles movement, animation, and collision.
 */
public class GameEngine implements KeyListener, ActionListener {

	private static final int MARGIN_OF_ERROR = 4;

	private Player player;
	private Screen screen;
	private Runner runner;
	private Timer mainTimer;

	private static int up;
	private static int left;
	private static int right;
	private static int restart;
	private static int pause;
	private boolean endLevel;
	private boolean paused;

	private int notMoveTime;

	/**
	 * Creates a new GameEngine
	 * 
	 * @param s
	 *            the map to draw on
	 * @param p
	 *            the player to control
	 * @param r
	 *            the runner that controls levels
	 */
	public GameEngine(Screen s, Player p, Runner r) {
		screen = s;
		player = p;
		runner = r;
		endLevel = false;
		paused = false;
		notMoveTime = 0;
		up = KeyEvent.VK_UP;
		left = KeyEvent.VK_LEFT;
		right = KeyEvent.VK_RIGHT;
		restart = KeyEvent.VK_R;
		pause = KeyEvent.VK_ESCAPE;
		mainTimer = new Timer(Player.ANIMATION_DELAY, this);
		// eventually loading screens

	}

	/**
	 * Gives this GameEngine a new level
	 * 
	 * @param s
	 *            the new Screen
	 */
	public void setScreen(Screen s) {
		screen = s;
		player = s.getPlayer();
	}

	/**
	 * Registers the key presses to move, pause, and restart.
	 * 
	 * @param e
	 *            the event
	 */
	public void keyPressed(KeyEvent e) {
		if (!paused) {
			if (e.getKeyCode() == restart) {
				notMoveTime = 0;
				restart();
			}
			if (e.getKeyCode() == up) {
				notMoveTime = 0;
				player.hitJump();
			}
			if (e.getKeyCode() == left) {
				notMoveTime = 0;
				player.hitLeft();
				player.setXVel(Player.MOVE_SPEED * -1);
			}
			if (e.getKeyCode() == right) {
				notMoveTime = 0;
				player.hitRight();
				player.setXVel(Player.MOVE_SPEED);
			}
		}
		if (e.getKeyCode() == pause) {
			pause(!paused);
			player.releaseLeft();
			player.releaseRight();
			player.releaseJump();
		}
	}

	/**
	 * Registers when the player no longer wants to move.
	 * 
	 * @param e
	 *            the event
	 */
	public void keyReleased(KeyEvent e) {
		if (!paused) {
			if (e.getKeyCode() == up) {
				notMoveTime = 0;
				player.releaseJump();
				// System.out.println("reljump");
			}
			if (e.getKeyCode() == left) {
				notMoveTime = 0;
				player.releaseLeft();
			}
			if (e.getKeyCode() == right) {
				notMoveTime = 0;
				player.releaseRight();
			}
		}
	}

	/**
	 * Unused
	 * 
	 * @param e
	 *            unused
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Used by the timer, advances the animation one step
	 * 
	 * @param e
	 *            unused
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!paused) {
			runner.repaintGameFrame();
			notMoveTime += 1;
			if (notMoveTime >= 100) {
				screen.notMove();
			} else {
				screen.move();
			}
			if (!player.getIsAlive()) {
				restart();
			}
//			checkMapBoundaryCollisions();
//			checkEndObjectCollisions();
//			checkOtherCollisions();
//			checkStationaryPlatformCollisions();
		}

	}

	/**
	 * The method to restart. This is specifically called when the level needs
	 * to be restarted.
	 */
	public void restart() {
		screen.restart();
		runner.updatePlayer();
		player = screen.getPlayer();
	}

	/**
	 * Checks to see if the snail has hit the boundaries of the map.
	 */
	public void checkMapBoundaryCollisions() {
		if (player.getXVel() < 0 && player.getCoords()[0] <= MARGIN_OF_ERROR || player.getXVel() > 0
				&& Drawer.widthOfMap() - player.getCoords()[0] - player.getWidth() <= MARGIN_OF_ERROR) {
			player.setXVel(0);
		}
	}

	/**
	 * pauses or unpauses the animation
	 * 
	 * @param b
	 *            whether the pause should be turned on or off
	 */
	public void pause(boolean b) {
		if (!paused && b) {
			mainTimer.stop();
			// open up the gamemenu
			runner.switchToGameMenu();
		}

		if (paused && !b) {
			if (!mainTimer.isRunning())
				mainTimer.start();
		}

		paused = b;

	}

	/**
	 * Checks if the player hits the end of the level
	 */
	public void checkEndObjectCollisions() {
		EndObject end = Drawer.getEndObject();
		if (player.collide(end) && !endLevel) {
			runner.endLevel();
		}
	}

	/**
	 * Checks the enemy collisions on platforms
	 */
	public void checkOtherCollisions() {
		Set<StationaryPlatform> sp = Drawer.getPlats();
		ArrayList<JumpingEnemy> jumpingEnemyList = screen.getJumpingEnemyList();

		for (StationaryPlatform platform : sp) {

			for (JumpingEnemy enemy : jumpingEnemyList) {
				if (enemy.collide(platform)) {
					double enemyX = enemy.getCollisionBox().getX();
					double enemyY = enemy.getCollisionBox().getY();
					double enemyWidth = enemy.getCollisionBox().getWidth();
					double enemyHeight = enemy.getCollisionBox().getHeight();
					double platformX = platform.getCollisionBox().getX();
					double platformY = platform.getCollisionBox().getY();
					double platformWidth = platform.getCollisionBox().getWidth();
					double platformHeight = platform.getCollisionBox().getHeight();

					// top
					if (enemyY + enemyHeight - platformY <= enemy.getYVel() + MARGIN_OF_ERROR + 1
							&& enemyY + enemyHeight - platformY > 0) {
						// if the enemy is falling down on top
						if (enemy.getYVel() > 0 && enemy.getInAir()) {
							// put it on the block
							enemy.setInAir(false);
							enemy.setYVel(0);
						}
						// if the enemy is stuck in the block
						if (Math.abs(enemyY + enemyHeight - platformY) >= MARGIN_OF_ERROR + 1 && !enemy.getInAir()) {
							// reset coords
							enemy.update();
							enemy.setCoords(enemy.getCoords()[0], platform.getCoords()[1] - enemy.getHeight());
							
						}
					}
					// bottom
					else if (platformY + platformHeight - enemyY <= Math.abs(enemy.getYVel() - MARGIN_OF_ERROR - 1)) {
						// don't go into the block!
						if (enemy.getYVel() < 0)
						{
							enemy.setYVel(0);
				//			enemy.update();
						}
					}
					// sides
					else {
						if (!(enemy.getInAir() && enemyY + enemyHeight - platformY > 0)) {
							// enemy going left
							if (enemy.getXVel() > 0 && platformX + platformWidth - enemyX - enemyWidth > 0
									&& platformX - enemyX - enemyWidth < 0) {
								// System.out.println("hit left");
								enemy.setXVel(0);
							}
							// enemy going right
							else if (enemy.getXVel() < 0 && platformX + platformWidth - enemyX > 0
									&& platformX - enemyX < 0) {
								// System.out.println("hit right");
								enemy.setXVel(0);
							}
						}

						else if (enemy.getInAir() && enemyY > platformY + platformHeight / 4
								&& enemyY < platformY + platformHeight * 3 / 4) {
							// System.out.println("topside");
							enemy.setXVel(0);
						} else if (enemy.getInAir() && enemyY + enemyHeight > platformY + platformHeight / 4
								&& enemyY + enemyHeight < platformY + platformHeight * 3 / 4) {
							// System.out.println("botside");
							enemy.setXVel(0);
						}
					}
				}
			}
		}
	}

	/**
	 * Checks to see if the player has collided with any stationary platforms
	 * and acts accordingly
	 */
	public void checkStationaryPlatformCollisions() {
		// Set<StationaryPlatform> sp = Drawer.getPlats();
		ArrayList<StationaryPlatform> sp = new ArrayList<StationaryPlatform>();
		sp.addAll(Drawer.getPlats());
		sp.addAll(Drawer.getDisappearingPlats());

		boolean falling = true;
		boolean hitSide = false;
		for (StationaryPlatform platform : sp) {
			if (player.collide(platform)) {
				double playerX = player.getCollisionBox().getX();
				double playerY = player.getCollisionBox().getY();
				double playerWidth = player.getCollisionBox().getWidth();
				double playerHeight = player.getCollisionBox().getHeight();
				double platformX = platform.getCollisionBox().getX();
				double platformY = platform.getCollisionBox().getY();
				double platformWidth = platform.getCollisionBox().getWidth();
				double platformHeight = platform.getCollisionBox().getHeight();

				// top
				if (playerY + playerHeight - platformY <= player.getYVel() + MARGIN_OF_ERROR
						&& playerY + playerHeight - platformY > 0) {
					if (platform.isSpike())
						player.die();
					if (platform instanceof DisappearingPlatform) {
						((DisappearingPlatform) platform).steppedOn();
					}
					// if the player is falling down on top
					if (player.getYVel() > 0 && player.getInAir()) {
						// put it on the block
						player.setInAir(false);
						player.setYVel(0);
					}
					// if the player is stuck in the block
					if (Math.abs(playerY + playerHeight - platformY) >= MARGIN_OF_ERROR && !player.getInAir()) {
						// reset coords
						player.setCoords(player.getCoords()[0], platform.getCoords()[1] - player.getHeight());
						player.update();
					}
					falling = false;
				}
				// bottom
				else if (platformY + platformHeight - playerY <= Math.abs(player.getYVel() - MARGIN_OF_ERROR)) {
					// don't go into the block!
					if (player.getYVel() < 0)
						player.setYVel(0);
				}
				// sides
				else {
					if (!(player.getInAir() && playerY + playerHeight - platformY > 0)) {
						// player going left
						if (player.getXVel() > 0 && platformX + platformWidth - playerX - playerWidth > 0
								&& platformX - playerX - playerWidth < 0) {
							// System.out.println("hit left");
							player.setXVel(0);
							hitSide = true;
						}
						// player going right
						else if (player.getXVel() < 0 && platformX + platformWidth - playerX > 0
								&& platformX - playerX < 0) {
							// System.out.println("hit right");
							player.setXVel(0);
							hitSide = true;
						}
					}

					else if (player.getInAir() && playerY > platformY + platformHeight / 4
							&& playerY < platformY + platformHeight * 3 / 4) {
						// System.out.println("topside");
						player.setXVel(0);
						hitSide = true;
					} else if (player.getInAir() && playerY + playerHeight > platformY + platformHeight / 4
							&& playerY + playerHeight < platformY + platformHeight * 3 / 4) {
						// System.out.println("botside");
						player.setXVel(0);
						hitSide = true;
					}
				}
			}
		}
		if (falling) {
			player.setInAir(true);
		}
		if (!hitSide) {
			player.updateXVel();
			checkMapBoundaryCollisions();
		}
	}

	/**
	 * Starts the animation
	 */
	public void startMainTimer() {
		if (!mainTimer.isRunning())
			mainTimer.start();
	}
	
	/**
	 * Stops the animation
	 */
	public void stopMainTimer() {
		if (mainTimer.isRunning())
			mainTimer.stop();
	}


	/**
	 * Gets the number for the key binded to up
	 * 
	 * @return the KeyEvent constant for the key binded to up
	 */
	public int getKeyBindingUp() {
		return up;
	}

	/**
	 * Gets the number for the key binded to left
	 * 
	 * @return the KeyEvent constant for the key binded to left
	 */
	public int getKeyBindingLeft() {
		return left;
	}

	/**
	 * Gets the number for the key binded to right
	 * 
	 * @return the KeyEvent constant for the key binded to right
	 */
	public int getKeyBindingRight() {
		return right;
	}

	/**
	 * Gets the number for the key binded to restart
	 * 
	 * @return the KeyEvent constant for the key binded to restart
	 */
	public int getKeyBindingRestart() {
		return restart;
	}

	/**
	 * Gets the number for the key binded to pause
	 * 
	 * @return the KeyEvent constant for the key binded to pause
	 */
	public int getKeyBindingPause() {
		return pause;
	}

	/**
	 * Sets the number for the key binded to up
	 * 
	 * @param k
	 *            the KeyEvent constant to be binded to up
	 */
	public void setKeyBindingUp(int k) {
		up = k;
	}

	/**
	 * Sets the number for the key binded to left
	 * 
	 * @param k
	 *            the KeyEvent constant to be binded to left
	 */
	public void setKeyBindingLeft(int k) {
		left = k;
	}

	/**
	 * Sets the number for the key binded to right
	 * 
	 * @param k
	 *            the KeyEvent constant to be binded to right
	 */
	public void setKeyBindingRight(int k) {
		right = k;
	}

	/**
	 * Sets the number for the key binded to restart
	 * 
	 * @param k
	 *            the KeyEvent constant to be binded to restart
	 */
	public void setKeyBindingRestart(int k) {
		restart = k;
	}

	/**
	 * Sets the number for the key binded to pause
	 * 
	 * @param k
	 *            the KeyEvent constant to be binded to pause
	 */
	public void setKeyBindingPause(int k) {
		pause = k;
	}

	/**
	 * Returns an array of all key bindings, in the format {left, up, right,
	 * restart, pause}
	 * 
	 * @return an array of all the key bindings
	 */
	public int[] getBindings() {
		return new int[] { left, up, right, restart, pause };
	}
}
