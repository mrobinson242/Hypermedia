package data;

public class Point 
{
   /** X Coordinate Point */
   private double _x;

   /** Y Coordinate Point */
   private double _y;

   /**
    * Constructor
    */
   public Point()
   {
     this(0.0, 0.0);
   }

   /**
    * Constructor
    *
    * @param x - X Coordinate of the Point
    * @param y - Y Coordinate of the Point
    */
   public Point(final double x, final double y)
   {
      // Initialize Coordinate Point
      _x = x;
      _y = y;
   }

   /**
    * setX - Sets the Value of the X Coordinate
    *
    * @param x - New X Coordinate Value
    */
   public void setX(final double x)
   {
      _x = x;
   }

   /***
    * setY - Sets the Value of the Y Coordinate
    *
    * @param y - New Y Coordinate Value
    */
   public void setY(final double y)
   {
      _y  = y;
   }

   /**
    * getX - Gets the X Coordinate of the Point
    *
    * @return double
    */
   public double getX()
   {
      return _x;
   }

   /**
    * getY - Gets the Y Coordinate of the Point
    *
    * @return double
    */
   public double getY()
   {
      return _y;
   }
}