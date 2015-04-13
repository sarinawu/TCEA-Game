package game;

public class Motion
{
  public MyPoint loc;
  public MyPoint v;
  public float rotation;
  public boolean inAir;
  public float groundSlope;

  public Motion()
  {
    this.loc = new MyPoint();
    this.v = new MyPoint();
    this.rotation = 0.0F;
    this.inAir = false;
    this.groundSlope = 0.0F;
  }

  public Motion(Motion m)
  {
    this.loc = new MyPoint(m.loc.x, m.loc.y);
    this.v = new MyPoint(m.v.x, m.v.y);
    this.rotation = m.rotation;
    this.inAir = m.inAir;
    this.groundSlope = m.groundSlope;
  }

  public Motion(MyPoint location, MyPoint velocity, float r)
  {
    this.loc = location;
    this.v = velocity;
    this.rotation = r;
    this.inAir = false;
    this.groundSlope = 0.0F;
  }

  public Motion(MyPoint location, MyPoint velocity, float r, boolean a)
  {
    this.loc = location;
    this.v = velocity;
    this.rotation = r;
    this.inAir = a;
    this.groundSlope = 0.0F;
  }

  public Motion(MyPoint location, MyPoint velocity, float r, float g)
  {
    this.loc = location;
    this.v = velocity;
    this.rotation = r;
    this.inAir = false;
    this.groundSlope = g;
  }

  public void addMotion(Motion m)
  {
    this.loc.x += m.loc.x;
    this.loc.y += m.loc.y;
    this.v.x += m.v.x;
    this.v.y += m.v.y;
    this.rotation += m.rotation;
  }

  public void subtractMotion(Motion m)
  {
    this.loc.x -= m.loc.x;
    this.loc.y -= m.loc.y;
    this.v.x -= m.v.x;
    this.v.y -= m.v.y;
    this.rotation -= m.rotation;
  }

  public String toString()
  {
    return "loc=" + this.loc.x + " " + this.loc.y + " v=" + this.v.x + " " + this.v.y + " rotation=" + this.rotation;
  }
}
