package engine.menu;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import engine.fileSystem.Resource;
import engine.mapLoader.Map;
import game.GameEngine;
import game.gameObjects.moveableObjects.Player;

import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class Menu extends JFrame
{
	private static final int FRAME_WIDTH = 1024;
	private static final int FRAME_HEIGHT = 768;
	private File background;
	private BufferedImage backgroundPic;
	private Map map;
	public Menu(Map m, Player p)
	{
		background = null;
		map = m;
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		add(map);
		repaint();
		setVisible(true);
		addKeyListener(new GameEngine(map,p));
	//	setLayout();
	}

	public void setBackground(String bckground)
	 {
	    this.background = new File(bckground);
	 }
	public BufferedImage getBackgroundImage()
	  {
	    if ((this.background != null) && (this.backgroundPic == null)) {
	      try
	      {
	        this.backgroundPic = ImageIO.read(this.background);
	      }
	      catch (IOException localIOException)
	      {
	      }
	    }
	    return this.backgroundPic;
	  }
	public Map getMap()
	{
		return map;
	}
	
}