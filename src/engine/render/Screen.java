package engine.render;

import fileSystem.ResourceLoader;
import game.GameEngine;
import game.gameObjects.moveableObjects.JumpingEnemy;
import game.gameObjects.moveableObjects.Player;
import game.gameObjects.stationaryObjects.DisappearingPlatform;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * Draws the full map that the user will see.
 */
public class Screen extends JComponent {
	private static final long serialVersionUID = 3353980361414032092L;
	private int width;
	private int height;
	private static final double SCALE = 1;
	private static final double SNAIL_SCALE = 1;
	public static final double PLATFORM_SCALE = .5;

	private static final int MARGIN_OF_ERROR = 4;
	private static final int SNAIL_WIDTH = 54;
	private static final int SNAIL_HEIGHT = 31;
	private static final int PLATFORM_WIDTH = 35;
	private static final int PLATFORM_HEIGHT = 35;
	public static final int SLIME_WIDTH = 50;
	public static final int SLIME_HEIGHT = 28;
	public static final int FLY_WIDTH = 75;
	public static final int FLY_HEIGHT = 31;

	private static final short FOLLOWING_DIST = 300;

	private int xOff;

	private Image currentMap;
	private Image snailLeft1, snailLeft2, snailRight1, snailRight2, snailDeadRight, snailDeadLeft;
	private Image slimeLeft1, slimeLeft2, slimeDeadLeft, slimeRight1, slimeRight2, slimeDeadRight;

	private boolean playerMove, enemyMove, isMove;
	private boolean right;
	private Player player;
	private ArrayList<JumpingEnemy> jumpingEnemyList;

	private boolean enemyHasDied;

	private GameEngine gameEngine;

	/**
	 * Loads pictures and the map data.
	 * 
	 * @param width
	 *            the width of the screen
	 * @param height
	 *            the height of the screen
	 * @param levelName
	 *            the name of the level to load
	 */
	public Screen(int width, int height, String levelName) {
		ResourceLoader res = new ResourceLoader();
		this.width = width;
		this.height = height;
		xOff = 0;
		LoadingScreen load = new LoadingScreen();
		load.paint(load.getGraphics());
				
		Drawer.readImageData(levelName);
		enemyHasDied = false;
		snailLeft1 = res.loadImage("snailWalk1l.png");
		snailLeft2 = res.loadImage("snailWalk2l.png");
		snailRight1 = res.loadImage("snailWalk1r.png");
		snailRight2 = res.loadImage("snailWalk2r.png");
		snailDeadRight = res.loadImage("snailDeadr.png");
		snailDeadLeft = res.loadImage("snailDeadl.png");

		slimeLeft1 = res.loadImage("slimeWalk1l.png");
		slimeLeft2 = res.loadImage("slimeWalk2l.png");
		slimeRight1 = res.loadImage("slimeWalk1r.png");
		slimeRight2 = res.loadImage("slimeWalk2r.png");
		slimeDeadRight = res.loadImage("slimeDeadr.png");
		slimeDeadLeft = res.loadImage("slimeDeadl.png");

		isMove = true;
		playerMove = true;
		enemyMove = false;

		player = new Player(100, 500, SNAIL_WIDTH, SNAIL_HEIGHT);

		jumpingEnemyList = new ArrayList<JumpingEnemy>();
		jumpingEnemyList.addAll(Drawer.getJumpingEnemies());

		right = true;
		currentMap = Drawer.getStaticPic();
		
		load.dispose();

		setVisible(true);
	}

	/**
	 * Loads pictures and the map data.
	 * 
	 * @param width
	 *            the width of the screen
	 * @param height
	 *            the height of the screen
	 * @param levelName
	 *            the name of the level to load
	 * @param levelIndex
	 *            the index of the level to load
	 */
	public Screen(int width, int height, String levelName, int levelIndex) {
		ResourceLoader res = new ResourceLoader();
		this.width = width;
		this.height = height;
		xOff = 0;
		LoadingScreen load = new LoadingScreen(levelIndex);
		load.paint(load.getGraphics());
				
		Drawer.readImageData(levelName);
		enemyHasDied = false;
		snailLeft1 = res.loadImage("snailWalk1l.png");
		snailLeft2 = res.loadImage("snailWalk2l.png");
		snailRight1 = res.loadImage("snailWalk1r.png");
		snailRight2 = res.loadImage("snailWalk2r.png");
		snailDeadRight = res.loadImage("snailDeadr.png");
		snailDeadLeft = res.loadImage("snailDeadl.png");

		slimeLeft1 = res.loadImage("slimeWalk1l.png");
		slimeLeft2 = res.loadImage("slimeWalk2l.png");
		slimeRight1 = res.loadImage("slimeWalk1r.png");
		slimeRight2 = res.loadImage("slimeWalk2r.png");
		slimeDeadRight = res.loadImage("slimeDeadr.png");
		slimeDeadLeft = res.loadImage("slimeDeadl.png");

		isMove = true;
		playerMove = true;
		enemyMove = false;

		player = new Player(100, 500, SNAIL_WIDTH, SNAIL_HEIGHT);

		jumpingEnemyList = new ArrayList<JumpingEnemy>();
		jumpingEnemyList.addAll(Drawer.getJumpingEnemies());

		right = true;
		currentMap = Drawer.getStaticPic();
		
		load.dispose();

		setVisible(true);
	}

	/**
	 * Resets all moving components of the map. In order to fully restart the
	 * level, call the restart() method in GameEngine
	 */
	public void restart() {
		playerMove = true;
		enemyMove = false;
		xOff = 0;
		player = new Player(100, 500, SNAIL_WIDTH, SNAIL_HEIGHT);
		jumpingEnemyList = Drawer.getJumpingEnemies();
		right = true;
		for (DisappearingPlatform dp : Drawer.getDisappearingPlats()) {
			dp.setCollideable(true);
		}
		repaint();
	}

	/**
	 * Paints all moving and non-moving objects and checks when the player
	 * should bounce when jumping on an object.
	 * 
	 * @param g
	 *            the Graphics of the screen.
	 */
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, (int) g.getClipBounds().getWidth(), (int) g.getClipBounds().getHeight());

		if (enemyHasDied) {
			enemyHasDied = false;
			player.releaseJump();
		}
		gameEngine.checkMapBoundaryCollisions();
		gameEngine.checkEndObjectCollisions();
		gameEngine.checkStationaryPlatformCollisions();
		gameEngine.checkOtherCollisions();
		player.update();

		for (JumpingEnemy e : jumpingEnemyList) {
			if (e.getIsAlive())
				e.update();
		}

		width = currentMap.getWidth(this);
		height = currentMap.getHeight(this);
		int[] coords = player.getCoords();
		if (player.getXVel() > 0)
			right = true;
		else if (player.getXVel() < 0)
			right = false;

		if (coords[0] + xOff < FOLLOWING_DIST && player.getXVel() < 0) {
			xOff -= player.getXVel();
		} else if (coords[0] + xOff > this.getBounds().getWidth() - FOLLOWING_DIST && player.getXVel() > 0) {
			xOff -= player.getXVel();
		}

		if (xOff > 0)
			xOff = 0;
		int temp = xOff + Drawer.widthOfMap();
		if (temp < g.getClipBounds().getWidth())
			xOff = (int) (-1 * (Drawer.widthOfMap() - g.getClipBounds().getWidth()));
		// System.out.println(xOff);

		g.drawImage(currentMap, xOff, 0, (int) (width * SCALE), (int) (height * SCALE), this);

		for (DisappearingPlatform dp : Drawer.getDisappearingPlats()) {
			if (dp.getCollideable())
				g.drawImage(dp.getTile(), dp.getCoords()[0] + xOff, dp.getCoords()[1], (int) (PLATFORM_WIDTH),
						(int) (PLATFORM_HEIGHT), null);
		}

		boolean notJump = true;
		for (JumpingEnemy e : jumpingEnemyList) {

			if (e.getIsAlive()) {
				if (player.collide(e)) {
					double playerY = player.getCollisionBox().getY();
					double playerHeight = player.getCollisionBox().getHeight();
					double enemyY = e.getCollisionBox().getY();
					if (playerY + playerHeight - enemyY <= player.getYVel() + MARGIN_OF_ERROR
							&& playerY + playerHeight - enemyY > 0) {
						if (player.getYVel() > 0 && player.getInAir()) {
							player.hitJump();
							notJump = false;
							gameEngine.checkStationaryPlatformCollisions();
							if (e.getIsConscious()) {
								e.unconscious();
							} else {
								e.die();
							}
							enemyHasDied = true;
							player.setInAir(false);
							player.setYVel(0);
							player.hitJump();
						}
						if (Math.abs(playerY + playerHeight - enemyY) >= MARGIN_OF_ERROR && !player.getInAir()) {
							player.hitJump();
							player.setCoords(player.getCoords()[0], e.getCoords()[1] - player.getHeight());
							player.update();
						}
					} else {
						if (notJump)
							player.die();
					}
				}
				boolean enemyRight = false;
				enemyRight = e.getIsRight();

				if (e.getIsConscious()) {
					if (enemyRight) {
						if (enemyMove)
							g.drawImage(slimeRight1, e.getCoords()[0] + 1 + xOff, e.getCoords()[1],
									(int) (slimeRight1.getWidth(this)), (int) (slimeRight1.getHeight(this)), this);
						else
							g.drawImage(slimeRight2, e.getCoords()[0] + xOff, e.getCoords()[1] + 2,
									(int) (slimeRight2.getWidth(this)), (int) (slimeRight2.getHeight(this)), this);
					} else {
						if (enemyMove)
							g.drawImage(slimeLeft1, e.getCoords()[0] - 1 + xOff, e.getCoords()[1],
									(int) (slimeLeft1.getWidth(this)), (int) (slimeLeft1.getHeight(this)), this);
						else
							g.drawImage(slimeLeft2, e.getCoords()[0] + xOff, e.getCoords()[1] + 2,
									(int) (slimeLeft2.getWidth(this)), (int) (slimeLeft2.getHeight(this)), this);

					}
					if (e.getPathTime() % 20 == 0 && !e.getInAir())
						enemyMove = !enemyMove;
				} else {
					if (enemyRight)
						g.drawImage(slimeDeadRight, e.getCoords()[0] + 1 + xOff, e.getCoords()[1] + 16,
								(int) (slimeDeadRight.getWidth(this)), (int) (slimeDeadRight.getHeight(this)), this);
					else
						g.drawImage(slimeDeadLeft, e.getCoords()[0] + 1 + xOff, e.getCoords()[1] + 16,
								(int) (slimeDeadLeft.getWidth(this)), (int) (slimeDeadLeft.getHeight(this)), this);
				}
			}
		}

		if (isMove) {
			if (right) {
				if (playerMove)
					g.drawImage(snailRight1, coords[0] + xOff, coords[1],
							(int) (snailRight1.getWidth(this) * SNAIL_SCALE),
							(int) (snailRight1.getHeight(this) * SNAIL_SCALE), this);
				else
					g.drawImage(snailRight2, coords[0] + xOff, coords[1],
							(int) (snailRight2.getWidth(this) * SNAIL_SCALE),
							(int) (snailRight2.getHeight(this) * SNAIL_SCALE), this);
			} else {
				if (playerMove)
					g.drawImage(snailLeft1, coords[0] + xOff, coords[1],
							(int) (snailLeft1.getWidth(this) * SNAIL_SCALE),
							(int) (snailLeft1.getHeight(this) * SNAIL_SCALE), this);
				else
					g.drawImage(snailLeft2, coords[0] + xOff, coords[1],
							(int) (snailLeft2.getWidth(this) * SNAIL_SCALE),
							(int) (snailLeft2.getHeight(this) * SNAIL_SCALE), this);

			}
		} else {
			if (right)
				g.drawImage(snailDeadRight, coords[0] + xOff, coords[1],
						(int) (snailDeadRight.getWidth(this) * SNAIL_SCALE),
						(int) (snailDeadRight.getHeight(this) * SNAIL_SCALE), this);
			else
				g.drawImage(snailDeadLeft, coords[0] + xOff, coords[1],
						(int) (snailDeadLeft.getWidth(this) * SNAIL_SCALE),
						(int) (snailDeadLeft.getHeight(this) * SNAIL_SCALE), this);

		}
		if (player.getPathTime() % 5 == 0 && !player.getInAir())
			playerMove = !playerMove;

	}

	/**
	 * Gets the List of JumpingEnemies
	 * 
	 * @return the List of JumpingEnemies
	 */
	public ArrayList<JumpingEnemy> getJumpingEnemyList() {
		return jumpingEnemyList;
	}

	/**
	 * Gets the Player of the current level
	 * 
	 * @return the PLayer of the current level
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Save gameEngine
	 * 
	 * @param the
	 *            gameEngine of the game.
	 */
	public void addGameEngine(GameEngine ge) {
		gameEngine = ge;
	}

	/**
	 * Sets the player image to not moving
	 */
	public void notMove() {
		isMove = false;
	}

	/**
	 * Sets the player image to moving
	 */
	public void move() {
		isMove = true;
	}

}