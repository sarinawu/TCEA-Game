package game.gameObjects.moveableObjects;

import java.awt.Rectangle;

import game.gameObjects.SuperObject;

/**
 * Creates an enemy that jumps around and destroys snails. Snails and
 * JumpingEnemies have long been in a feud that has made all family reunions
 * awkward.
 */
public class JumpingEnemy implements SuperObject, Cloneable {
	private static final long serialVersionUID = 9192371371616695461L;
	public static final int ANIMATION_DELAY = 30;
	public static final int STOPPED = -1;
	public static final int STANDING = 0;
	public static final int WALKING = 2;
	public static final int MAX_VELOCITY = 12;
	public static final int JUMP_SPEED = -17;
	public static final int MOVE_SPEED = 10;
	private static final int COLLISION_OFFSET = 7;

	private boolean inAir;
	private int width;
	private int height;
	private Rectangle collisionBox;
	private int x, y;
	private int xVel, yVel;
	private int slimeJumpCount;
	private int slimeJumpTime;
	private boolean slimeFall;

	private boolean isRight;
	private boolean isAlive;
	private boolean isConscious;
	private int pathTime;
	private int unconsciousTime;

	/**
	 * Creates a new JumpingEnemy at a given coordinate with given dimensions
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
	public JumpingEnemy(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		
		xVel = 1;
		yVel = 0;
		inAir = false;
		pathTime = 120;
		slimeJumpCount = 2;
		slimeJumpTime = 25;
		slimeFall = true;
		
		collisionBox = new Rectangle(x + COLLISION_OFFSET, y + h / 2, w - COLLISION_OFFSET * 2+xVel, h / 2+yVel);
		
		isRight = true;
		isAlive = true;
		isConscious = true;
		unconsciousTime = 25;
	}

	/**
	 * Checks to see if this enemy has collided with anything.
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
	 * Returns this enemy's hit box
	 * 
	 * @return this enemy's hit box
	 */
	public Rectangle getCollisionBox() {

		return collisionBox;
	}

	/**
	 * Sets new coordinates for this enemy
	 * 
	 * @param ex
	 *            the new x coordinate
	 * @param why
	 *            the new y coordinate
	 */
	public void setCoords(int ex, int why) {
		x = ex;
		y = why;
		collisionBox.setBounds(x + COLLISION_OFFSET, y + height / 2, width - COLLISION_OFFSET * 2+xVel, height / 2+yVel);
	}

	/**
	 * Returns an array with the coordinates, in the format {x, y}
	 * 
	 * @return the array with the coordinates of this enemy
	 */
	public int[] getCoords() {
		return new int[] { x, y };
	}

	/**
	 * Returns the width of this enemy
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of this enemy
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Allows this enemy to move around.
	 */
	public void update() {
		if (isAlive) {
			if (isConscious) {
				if (pathTime <= 20) {
					xVel = 0;
				}
				if (pathTime <= 0 && !inAir) {
					pathTime = 120;
					jump();
					xVel = 5;
					slimeFall = false;
					slimeJumpCount -= 1;
				}
				if (isRight)
					x += xVel;
				else
					x -= xVel;


				if (inAir) {
					slimeJumpTime -= 1;
					if (y > 530) {
						inAir = false;
						yVel = 0;
						y = 530;
						slimeFall = true;
						xVel = 1;
					} else {
						if (!slimeFall)
							yVel += 2;
						else
							yVel -= 2;
					}

					if (slimeJumpTime <= 0 && !inAir) {

						slimeFall = false;
						slimeJumpTime = 50;
					}

				} else {
					pathTime -= 1;
					xVel = 1;
				}

				if (slimeJumpCount <= 0) {
					slimeJumpCount = 2;
					isRight = !isRight;
				}
				y += yVel;
			} else {
				xVel = 0;
				if (inAir) {
					xVel = 5;
					slimeJumpTime -= 1;
					if (y > 530) {
						inAir = false;
						yVel = 0;
						y = 530;
						
						slimeFall = true;
						xVel = 1;
					} else {
						if (!slimeFall)
							yVel += 2;
						else
							yVel -= 2;
					}

					if (slimeJumpTime <= 0 && !inAir) {

						slimeFall = false;
						slimeJumpTime = 50;
					}

				}
				if (isRight)
					x += xVel;
				else
					x -= xVel;

				y += yVel;
				unconsciousTime -= 1;
				if (unconsciousTime == 0) {
					unconsciousTime = 25;
					conscious();
				}
			}

			collisionBox.setBounds(x + COLLISION_OFFSET, y + height / 2, width - COLLISION_OFFSET * 2+xVel, height / 2+yVel);
		}
	}

	/**
	 * Makes this enemy jump
	 */
	public void jump() {
		if (!inAir) {
			inAir = true;
			yVel = JUMP_SPEED;
		}
	}

	/**
	 * Sets how fast this enemy moves horizontally, as a vector
	 * 
	 * @param v
	 *            the horizontal speed of this enemy
	 */
	public void setXVel(int v) {
		xVel = v;
	}

	/**
	 * Sets how fast this enemy moves vertically, as a vector
	 * 
	 * @param v
	 *            the vertical speed of this enemy
	 */
	public void setYVel(int v) {
		yVel = v;
	}

	/**
	 * Gets how fast this enemy moves horizontally, as a vector
	 * 
	 * @return the horizontal speed of this enemy
	 */
	public int getXVel() {
		return xVel;
	}

	/**
	 * Gets how fast this enemy moves vertically, as a vector
	 * 
	 * @return the vertical speed of this enemy
	 */
	public int getYVel() {
		return yVel;
	}

	/**
	 * Sets whether this enemy is in the air
	 * 
	 * @param b
	 *            whether this enemy is in the air
	 */
	public void setInAir(boolean b) {
		inAir = b;
	}

	/**
	 * Returns whether this enemy is in the air
	 * 
	 * @return whether this enemy is in the air
	 */
	public boolean getInAir() {
		return inAir;
	}

	/**
	 * A given time that the JumpingEnemy moves before changing movements.
	 * Allows calculating images.
	 * 
	 * @return the time before changing movements
	 */
	public int getPathTime() {
		return pathTime;
	}

	/**
	 * Returns whether this enemy is alive
	 * 
	 * @return whether this enemy is alive
	 */
	public boolean getIsAlive() {
		return isAlive;
	}

	/**
	 * The sequence of events that must happen in order for this enemy to die.
	 */
	public void die() {
		isAlive = false;
	}

	/**
	 * The sequence of events that must happen when this enemy is one hit away
	 * from dying
	 */
	public void unconscious() {
		isConscious = false;
	}

	/**
	 * Allows this enemy to come back from sleep
	 */
	public void conscious() {
		isConscious = true;
	}

	/**
	 * Checks whether this enemy is asleep
	 * 
	 * @return
	 */
	public boolean getIsConscious() {
		return isConscious;

	}

	/**
	 * Checks whether this enemy is facing right
	 * 
	 * @return whether this enemy is facing right
	 */
	public boolean getIsRight() {
		return isRight;
	}

	/**
	 * Makes a clone of this enemy
	 * 
	 * @return a new JumpingEnemy with the same stats
	 */
	public JumpingEnemy clone() {
		return new JumpingEnemy(x, y, width, height);
	}
}
