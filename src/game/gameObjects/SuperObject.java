package game.gameObjects;

import java.awt.Rectangle;
import java.io.*;

/**
 * Mother of all objects, moveable and stationary.
 */
public interface SuperObject extends Serializable {
	/**
	 * Returns whether two Objects, preferably SuperObjects, collide.
	 * 
	 * @param o
	 *            the other object to check
	 * @return whether the two objects collide
	 */
	boolean collide(Object o);

	/**
	 * Returns the hitbox of the SuperObject
	 * 
	 * @return the hitbox
	 */
	Rectangle getCollisionBox();

}
