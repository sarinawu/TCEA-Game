package game.gameObjects.moveableObjects;
import java.awt.Rectangle;

import game.gameObjects.SuperObject;

/**
 * Makes an adorable snail that needs a new home.
 */
public class Player implements SuperObject {
	private static final long serialVersionUID = -4626375505245586143L;
	public static final int ANIMATION_DELAY = 30;
	public static final int STOPPED = -1;
	public static final int STANDING = 0;
	public static final int WALKING = 2;
	public static final int MAX_VELOCITY = 12;
	public static final int JUMP_SPEED = -27;
	public static final int MOVE_SPEED = 10;
	private static final int COLLISION_OFFSET = 10;
	private static final int ACCELERATION = 3;
	private boolean inAir;
	private boolean hitLeft;
	private boolean hitRight;
	private boolean hitJump;
	private int width;
	private int height;
	private Rectangle collisionBox;
	private int x, y;
	private int xVel, yVel;
	private int pathTime;
	private boolean isAlive;

	/**
	 * Creates a new Player at a given coordinate with given dimensions
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param w
	 *            the width
	 * @param h
	 *            the height
	 */
	public Player(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		collisionBox = new Rectangle(x + COLLISION_OFFSET, y, width - COLLISION_OFFSET * 2, height);
		xVel = 0;
		yVel = 0;
		hitLeft = false;
		hitRight = false;
		hitJump = false;
		inAir = false;
		pathTime = 600;
		isAlive = true;
	}

	/**
	 * Checks to see if our guy has collided with anything.
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
		collisionBox = new Rectangle(x + COLLISION_OFFSET, y, width - COLLISION_OFFSET * 2+xVel, height+yVel);
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
	 * Returns the width of our guy
	 * 
	 * @return the width of our guy
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of our guy
	 * 
	 * @return the height of our guy
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Moves our guy in the direction that he is coerced into
	 */
	public void update() {
		if (pathTime == 0) {
			pathTime = 600;
		}

		if (xVel > MAX_VELOCITY)
			xVel = MAX_VELOCITY;
		if (yVel > MAX_VELOCITY)
			yVel = MAX_VELOCITY;
		x += xVel;
		y += yVel;
		updateJump();
		if (y >= 520) {
			inAir = false;
			yVel = 0;
			hitRight = false;
			y = 500;
		}
		if (inAir) {
			yVel += ACCELERATION;
		}
		collisionBox = new Rectangle(x + COLLISION_OFFSET, y, width - COLLISION_OFFSET * 2+xVel, height+yVel);
		pathTime -= 1;
	}

	/**
	 * Indicates that left key is pressed
	 */
	public void hitLeft() {
		hitLeft = true;
		updateXVel();
	}

	/**
	 * Indicates that left key is released
	 */
	public void releaseLeft() {
		hitLeft = false;
		updateXVel();
	}

	/**
	 * Indicates that right key is pressed
	 */
	public void hitRight() {
		hitRight = true;
		updateXVel();
	}

	/**
	 * Indicates that right key is released
	 */
	public void releaseRight() {
		hitRight = false;
		updateXVel();
	}

	/**
	 * Indicates that the jump key is pressed
	 */
	public void hitJump() {
		hitJump = true;
		updateJump();
	}

	/**
	 * Indicates that the jump key is released
	 */
	public void releaseJump() {
		hitJump = false;
		updateJump();
	}

	/**
	 * Updates changes in input for moving left or right
	 */
	public void updateXVel() {
		if (hitLeft && !hitRight)
			xVel = MOVE_SPEED * -1;
		else if (hitRight && !hitLeft)
			xVel = MOVE_SPEED;
		else
			xVel = 0;
	}

	/**
	 * Makes our guy jump
	 */
	public void updateJump() {
		if (hitJump && !inAir) {
			inAir = true;
			yVel = JUMP_SPEED;
		}
		if (!hitJump && !inAir) {
			inAir = false;
			yVel = 0;
		}

	}

	/**
	 * Gets the animation delay of our guy
	 * 
	 * @return the animation delay
	 */
	public int getAnimationDelay() {
		return ANIMATION_DELAY;
	}

	/**
	 * Sets how fast our guy moves horizontally, as a vector
	 * 
	 * @param v
	 *            the horizontal speed of our guy
	 */
	public void setXVel(int v) {
		xVel = v;
	}

	/**
	 * Sets how fast our guy moves vertically, as a vector
	 * 
	 * @param v
	 *            the vertical speed of our guy
	 */
	public void setYVel(int v) {
		yVel = v;
	}

	/**
	 * Gets how fast our guy moves horizontally, as a vector
	 * 
	 * @return the horizontal speed of our guy
	 */
	public int getXVel() {
		return xVel;
	}

	/**
	 * Gets how fast our guy moves vertically, as a vector
	 * 
	 * @return the vertical speed of our guy
	 */
	public int getYVel() {
		return yVel;
	}

	/**
	 * Sets whether our guy is in the air
	 * 
	 * @param b
	 *            whether our guy is in the air
	 */
	public void setInAir(boolean b) {
		inAir = b;
	}

	/**
	 * Returns whether our guy is in the air
	 * 
	 * @return whether our guy is in the air
	 */
	public boolean getInAir() {
		return inAir;
	}

	/**
	 * A given time that our guy moves before changing movements. Allows
	 * calculating images.
	 * 
	 * @return the time before changing movements
	 */
	public int getPathTime() {
		return pathTime;
	}

	/**
	 * Does stuff when our guy dies.
	 */
	public void die() {
		isAlive = false;
	}

	/**
	 * Returns whether our guy is alive.
	 * 
	 * @return whether our guy is alive
	 */
	public boolean getIsAlive() {
		return isAlive;
	}
}
