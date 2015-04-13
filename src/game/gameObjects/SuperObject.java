package game.gameObjects;
import java.util.*;
import java.io.*;
import java.awt.Polygon;
/*
 * Mother of all objects, moveable and stationary
 * Superclass, not to be implemented
 */
public interface SuperObject extends Comparable<SuperObject>, Serializable
{
//	  private static final long serialVersionUID = 1L;
//	  private static int numObjects = 0;
//	  private static final String IDENTIFIER_START = "obj";
//	  protected int objectNumber;
//	  protected String identifier;
//	  public float[] properties;
//	  protected int order;
//	  protected int state;
//	  protected Polygon collision;
//	  protected int frameNumber;
//	  protected int color;
//	  protected int originalHeight;
//	  protected int top;
//	  protected Polygon shape;
//	  protected float xchange;
//	  protected float ychange;
//	  protected int facing;
//	  protected int action;
	  
	  boolean equals(Object o);
	  
	  String toString();

	  @Override
	  int compareTo(SuperObject o);
	  
}
