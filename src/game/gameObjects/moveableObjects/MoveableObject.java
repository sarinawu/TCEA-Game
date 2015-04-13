package game.gameObjects.moveableObjects;
/*
 * The abstract SUPERCLASS of Moveable Objects
 */
public abstract class MoveableObject implements game.gameObjects.SuperObject {
	// instance fields
	private static final int MAX_VELOCITY = 10;
	private double length;
	protected float angleChange;
	
	public MoveableObject(float x, float y)
	{
		
	}
	public void update()
	{
		
	}
	//public Area areaCovered()
	{
		
	}
	protected void checkGroundCollisions()
	{
		
	}
//	public boolean collide()
//	{
//		
//	}
	public void bounce()
	{
		
	}
}
