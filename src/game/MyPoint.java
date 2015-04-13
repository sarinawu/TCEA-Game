package game;

import java.awt.Point;

public class MyPoint
{
  public float x;
  public float y;

  public MyPoint()
  {
    this.x = 0.0F;
    this.y = 0.0F;
  }

  public MyPoint(Point p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  public MyPoint(MyPoint p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  public MyPoint(float x2, float y2)
  {
    this.x = x2;
    this.y = y2;
  }

  public String toString()
  {
    return "x=" + this.x + " y=" + this.y;
  }
}