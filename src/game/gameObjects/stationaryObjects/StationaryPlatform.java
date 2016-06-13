package game.gameObjects.stationaryObjects;

import java.awt.Rectangle;

import game.gameObjects.SuperObject;

/**
 * Creates a platform that does not move and may or may not have spikes.
 */
public class StationaryPlatform implements SuperObject {
	private static final long serialVersionUID = 6316929926691918043L;
	public static final int STOPPED = -1;
	public static final int MOVING = 0;
	public static final int MAX_VELOCITY = 12;
	private int width;
	private int height;
	private Rectangle collisionBox;
	private int x, y;
	private int gid;
	private boolean isSpike;

	/**
	 * Creates a new Platform top left of the given coordinate
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param w
	 *            the width of the tile
	 * @param h
	 *            the height of the tile
	 * @param g
	 *            the gid, which represents which tile picture to use
	 */
	public StationaryPlatform(int x, int y, int w, int h, int g) {
		this.x = x;
		this.y = y;
		gid = g;
		width = w;
		height = h;
		collisionBox = new Rectangle(x, y, w, h);
		isSpike = false;
	}

	/**
	 * Creates a new Platform top left of the given coordinate
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param w
	 *            the width of the tile
	 * @param h
	 *            the height of the tile
	 * @param g
	 *            the gid, which represents which tile picture to use
	 * @param sp
	 *            tells if this platform is a spike
	 */
	public StationaryPlatform(int x, int y, int w, int h, int g, boolean sp) {
		this.x = x;
		this.y = y;
		gid = g;
		width = w;
		height = h;
		collisionBox = new Rectangle(x, y, w, h);
		isSpike = sp;
	}

	/**
	 * Returns whether or not this platform is spiky
	 * 
	 * @return whether this platform has spikes
	 */
	public boolean isSpike() {
		return isSpike;
	}

	/**
	 * Checks to see if it has collided with anything.
	 * 
	 * @param o
	 *            the object to check for collision
	 * @return whether it has collided with said object
	 */
	public boolean collide(Object o) {
		SuperObject other = (SuperObject) o;
		return (collisionBox.intersects(other.getCollisionBox()));
	}

	/**
	 * Returns our guy's hit box
	 * 
	 * @return our guy's hit box
	 */
	public Rectangle getCollisionBox() {
		return collisionBox;
	}

	/**
	 * Returns the width of the platform
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the platform
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets new coordinates for our guy
	 * 
	 * @param ex
	 *            the new x coordinate
	 * @param why
	 *            the new y coordinate
	 */
	public void setCoords(int ex, int why) {
		x = ex;
		y = why;
	}

	/**
	 * Returns an array with the coordinates, in the format {x, y}
	 * 
	 * @return the array with the coordinates of our guy
	 */
	public int[] getCoords() {
		return new int[] { x, y };
	}

	/**
	 * Gets the animation delay of our guy
	 * 
	 * @return the animation delay
	 */

	/**
	 * Returns the gid of this platform
	 * 
	 * @return the gid of this platform
	 */
	public int getGid() {
		return gid;
	}

}
