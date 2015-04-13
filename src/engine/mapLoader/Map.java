package engine.mapLoader;

import game.Runner;

import javax.swing.JComponent;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Toolkit;

/**
 *Credits to:
 *http://opengameart.org/content/bevouliin-free-game-background-for-game-developers for background
 */
public class Map extends JComponent
{
	//private game.gameObjects.moveableObjects.Player player;
	private static final double SCALE = .50;
	private static final double SNAIL_SCALE = 1;
	
	private Image currentMap;
	private Image snail;
	
	public Map()
	{
		
		currentMap = Toolkit.getDefaultToolkit().getImage("full-background.png");
		snail = Toolkit.getDefaultToolkit().getImage("snailWalk1.png");
	}
	
	public void paintComponent(Graphics g)
	{
		g.clearRect(0,0,(int)g.getClipBounds().getWidth(),(int)g.getClipBounds().getHeight());
		g.drawImage(currentMap,0,0,(int)(currentMap.getWidth(this)*SCALE),(int)(currentMap.getHeight(this)*SCALE),this);
		int[] coords = Runner.player.getCoords();
		g.drawImage(snail, coords[0], coords[1], (int)(snail.getWidth(this)*SNAIL_SCALE), (int)(snail.getHeight(this)*SNAIL_SCALE), this);
	}
}
