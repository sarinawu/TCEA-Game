package game;

import java.awt.Toolkit;
import java.awt.event.*;

import engine.mapLoader.Map;
import engine.menu.Menu;
import game.gameObjects.*;
import game.gameObjects.moveableObjects.Player;

public class Runner {
	private Menu gameMenu;
	public static Player player;
	private GameEngine gameEngine;
	private Map map;
	public Runner()
	{
		//making menus
		//eventually have intro menu
		Map map = new Map();
		
		player = new Player(100, 640); //eventually, for loading screens & collisions & movement
		
		
		gameMenu = new Menu(map, player);
		gameMenu.setBackground("full-background.png");
	}
	
}
