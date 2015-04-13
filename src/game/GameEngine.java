package game;

import engine.mapLoader.Map;
import game.gameObjects.moveableObjects.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameEngine implements KeyListener { //should handle all movement and collisions
	Player player;
	Map map;
	public GameEngine(Map m, Player p)
	{
		map = m;
		player = p;
		//eventually loading screens
		
	}
	public void keyPressed(KeyEvent e)
	{
		int[] coords = player.getCoords();
		if(e.getKeyCode()==KeyEvent.VK_UP||e.getKeyCode()==KeyEvent.VK_W)
		{
			player.setCoords(coords[0], coords[1]+Player.JUMP_SPEED);
		}
		
		if(e.getKeyCode()==KeyEvent.VK_LEFT||e.getKeyCode()==KeyEvent.VK_A)
		{
			player.setCoords(coords[0]-Player.MOVE_SPEED, coords[1]);
		}
		if(e.getKeyCode()==KeyEvent.VK_RIGHT||e.getKeyCode()==KeyEvent.VK_D)
		{
			player.setCoords(coords[0]+Player.MOVE_SPEED, coords[1]);
		}
		update();
	}
	public boolean update() //eventually updating everything
	{
		map.repaint();
		//System.out.println("Updated");
		return true;
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	//updating objects and stuff
}
