package game.gameObjects.stationaryObjects;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Timer;

import game.gameObjects.moveableObjects.Player;

/**
 * A fragile platform that disappears shortly after being stepped on.
 */
public class DisappearingPlatform extends StationaryPlatform implements ActionListener {
	private static final long serialVersionUID = 280249299178977142L;
	private boolean collideable;
	private BufferedImage tile, original;
	private float alpha;
	private Timer timer;

	/**
	 * Makes a new DisappearingPlatform.
	 * 
	 * @param x
	 *            the x coordinate of the platform
	 * @param y
	 *            the y coordinate of the platform
	 * @param w
	 *            the width of the platform
	 * @param h
	 *            the height of the platform
	 * @param g
	 *            the gid
	 * @param t
	 *            the Image of the tile
	 */
	public DisappearingPlatform(int x, int y, int w, int h, int g, BufferedImage t) {
		super(x, y, w, h, g);
		collideable = true;
		tile = original = t;
		alpha = 1f;
		timer = new Timer(Player.ANIMATION_DELAY * 3, this);
	}

	/**
	 * Gets the collision box, or one of impossible location and proportion if
	 * this platform has disappeared.
	 */
	public Rectangle getCollisionBox() {
		if (collideable)
			return super.getCollisionBox();
		return new Rectangle(-100, -100, 0, 0);
	}

	/**
	 * Restarts the platform's properties and sets the platform's abilities to
	 * collide with things.
	 * 
	 * @param collide
	 *            whether this platform is collideable.
	 */
	public void setCollideable(boolean collide) {
		collideable = collide;
		timer.stop();
		alpha = 1f;
		tile = original;
	}

	/**
	 * Returns whether this platform can be collided with
	 * 
	 * @return whether this platform can be collided with
	 */
	public boolean getCollideable() {
		return collideable;
	}

	/**
	 * Called when the Player steps onto this platform. Begins the process of
	 * erosion and death.
	 */
	public void steppedOn() {
		if (!timer.isRunning())
			timer.start();
	}

	/**
	 * Gets the Image of this platform
	 * 
	 * @return the Image of this platform
	 */
	public BufferedImage getTile() {
		return tile;
	}

	/**
	 * Used by the timer. When this method is called, the platform becomes more
	 * and more transparent. When it reaches a certain transparency, the Timer
	 * will terminate and the platform will no longer be able to be collided
	 * with
	 * 
	 * @param e
	 *            unused
	 */
	public void actionPerformed(ActionEvent e) {
		if (alpha <= .2) {
			setCollideable(false);
		}
		tile = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) tile.getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g.drawImage(original, 0, 0, original.getWidth(), original.getHeight(), null);
		alpha *= .9f;
	}
}
