package game.gameObjects.moveableObjects;

public class Player extends game.gameObjects.moveableObjects.MoveableObject{
	  private static final long serialVersionUID = 19L;
	  public static final double COLLISON_ANGLE = 0.7853981633974483D;
	  public static final int ANIMATION_DELAY = 6;
	  public static final int STOPPED = -1;
	  public static final int STANDING = 0;
	  public static final int WALKING = 2;
	  public static final int MAX_VELOCITY = 12;
	  public static final int JUMP_SPEED = -10;
	  public static final int MOVE_SPEED = 10;
	  private int animationDelay;
	  private int x, y;
//private SpriteSheet sprites;
	  public enum Direction
	  {
		  left, right, up, down
	  }
	  public Player(int x, int y)
	  {
		  super(x, y);
		  this.x = x; 
		  this.y = y;
		  
	  }
	  //private void checkGroundCollisions()
	  {
		  
	  }
	  public boolean collide(Object o)
	  {
		  return false;
	  }
	  
	  public int compareTo(game.gameObjects.SuperObject o)
	  {
		  return 0;
	  }
	  public void setCoords(int ex, int why)
	  {
		  x = ex;
		  y = why;
	  }
	  public int[] getCoords()
	  {
		  return new int[]{x,y};
	  }
}
