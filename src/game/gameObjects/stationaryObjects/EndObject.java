package game.gameObjects.stationaryObjects;

import java.awt.Rectangle;

import game.gameObjects.SuperObject;

/**
 * Repersents a gem that the user needs to get in order to go to the next level.
 */
public class EndObject implements SuperObject {
	private static final long serialVersionUID = -2601098029186186158L;
	public static final int SUMMER = 1, WINTER = 2, SPRING = 4, FALL = 3, GENERIC = 5;
	private Rectangle collisionBox;
	private int x, y;
	private int gem;

	/**
	 * Creates a new EndObject top left of the given coordinate
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param w
	 *            the width of the EndObject
	 * @param h
	 *            the height of the EndObject
	 */
	public EndObject(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		collisionBox = new Rectangle(x, y, w, h);
		gem = GENERIC;
	}

	/**
	 * Creates a new EndObject top left of the given coordinate
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param w
	 *            the width of the EndObject
	 * @param h
	 *            the height of the EndObject
	 * @param gemNum
	 *            the gem it is
	 */
	public EndObject(int x, int y, int w, int h, int gemNum) {
		this.x = x;
		this.y = y;
		gem = gemNum;
		collisionBox = new Rectangle(x, y, w, h);
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
	 * Returns the EndObject's hit box
	 * 
	 * @return the EndObject's hit box
	 */
	public Rectangle getCollisionBox() {
		return collisionBox;
	}

	/**
	 * Returns an array with the coordinates, in the format {x, y}
	 * 
	 * @return the array with the coordinates of the gem
	 */
	public int[] getCoords() {
		return new int[] { x, y };
	}

	/**
	 * Returns the type of gem
	 * 
	 * @return the type of gem
	 */
	public int getGem() {
		return gem;
	}

}
