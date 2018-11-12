package util;

import java.util.List;

import data.Point;

/**
 * PolygonUtil - Polygon Utility Class
 */
public class PolygonUtil
{
   /**
    * Constructor
    */
   public PolygonUtil()
   {
      //N/A
   }

   /**
    * isInsidePolygon - Checks if a point p lies inside a Polygon
    *
    * @param polygon - The Polygon
    * @param p - The Point
    */
   public boolean isInsidePolygon(Point p, List<Point> polygon)
   {
      // Initialize return boolean
      boolean isInsidePolygon = false;

      // Ensure the Polygon is a Polygon
      if(polygon.size() >= 3)
      {
         // Create a Point for the line segment from p to infinity
         Point infinitePoint = new Point(Double.MAX_VALUE, p.getY());

         // Keep track of the number of intersections
         int intersectCount = 0;

         // Indicator to see if the IntersectionCount
         // needs to be checked
         boolean checkIntersections = true;

         // Iterate over the Polygon's Vertices
         for(int i = 0; i < polygon.size(); ++i)
         {
            // Check if the Last Point in the Polygon List
            if(i == polygon.size()-1)
            {
               // Check if Line Segment from Point p to the InfinitePoint intersects
               // with the line segment from the specified points on the Polygon
               if(isIntersection(polygon.get(i), polygon.get(0), p, infinitePoint))
               {
                  // Get the Orientation between Point p and the 
                  // current line segment on the Polygon
                  final int orientation = getOrientation(polygon.get(i), polygon.get(0), p);

                  // Check if Point P lies on the same Line
                  // of the Line Segment on the Polygon
                  if(orientation == 0)
                  {
                     // Check if Point P is on the Polygon's Line Segment
                     // or is just Colinear with the Segment
                     isInsidePolygon = onLineSegment(polygon.get(i), polygon.get(0), p);

                     // No need to Check Intersection Count
                     checkIntersections = false;
                     break;
                  }

                  // Increment Intersection Count
                  intersectCount++;
               }
            }
            else
            {
               // Check if Line Segment from Point p to the InfinitePoint intersects
               // with the line segment from the specified points on the Polygon
               if(isIntersection(polygon.get(i), polygon.get(i+1), p, infinitePoint))
               {
                  // Get the Orientation between Point p and the 
                  // current line segment on the Polygon
                  final int orientation = getOrientation(polygon.get(i), polygon.get(i+1), p);

                  // Check if Point P lies on the same Line
                  // of the Line Segment on the Polygon
                  if(orientation == 0)
                  {
                     // Check if Point P is on the Polygon's Line Segment
                     // or is just Colinear with the Segment
                     isInsidePolygon = onLineSegment(polygon.get(i), polygon.get(i+1), p);

                     // No need to Check Intersection Count
                     checkIntersections = false;
                     break;
                  }

                  // Increment Intersection Count
                  intersectCount++;
               }
            }
         }

         // Check if intersect count is odd
         if(((intersectCount % 2) == 1) && checkIntersections)
         {
            isInsidePolygon = true;
         }
      }

      return isInsidePolygon;
   }

   /**
    * getOrientation - Gets the Orientation between 3 Points
    *
    * @param p - Point A
    * @param q - Point B
    * @param r - Point C
    * @return int (0 = Colinear) (1 = Clockwise) (2 = Counterclockwise)
    */
   private int getOrientation(final Point p, final Point q, final Point r)
   {
      int pX = (int)Math.round(p.getX());
      int pY = (int)Math.round(p.getY());
      int qX = (int)Math.round(q.getX());
      int qY = (int)Math.round(q.getY());
      int rX = (int)Math.round(r.getX());
      int rY = (int)Math.round(r.getY());

      // Calculate the Orientation
      int orientation =  ((qY - pY) * (rX - qX) - 
                          (qX - pX) * (rY - qY));

      if(orientation == 0)
      {
         orientation = 0;
      }
      else if(orientation > 0)
      {
         orientation = 1;
      }
      else
      {
         orientation = 2;
      }

      return orientation;
   }

   /**
    * isIntersection
    *
    * @param p1
    * @param p2
    * @param q1
    * @param q2
    * @return boolean
    */
   private boolean isIntersection(Point p1, Point q1, Point p2, Point q2)
   {
      // Initialize return boolean
      boolean isIntersection = false;

      int o1 = getOrientation(p1, q1, p2);
      int o2 = getOrientation(p1, q1, q2);
      int o3 = getOrientation(p2, q2, p1);
      int o4 = getOrientation(p2, q2, q1);

      // General Case
      if((o1 != o2) && (o3 != o4))
      {
         isIntersection = true;
      }
      else if((o1 == 0) && onLineSegment(p1, p2, q1))
      {
         isIntersection = true;
      }
      else if((o2 == 0) && onLineSegment(p1, q2, q1))
      {
         isIntersection = true;
      }
      else if((o3 == 0) && onLineSegment(p2, p1, q2))
      {
         isIntersection = true;
      }
      else if((o4 == 0) && onLineSegment(p2, q1, q2))
      {
         isIntersection = true;
      }

      return isIntersection;
   }

   /**
    * onLineSegment - Checks if a point c lies
    *                 on a line segment ab
    *
    * @param p - Point A
    * @param q - Point B
    * @param r - Point C
    * @return boolean
    */
   private boolean onLineSegment(final Point p, final Point q, final Point r)
   {
      // Initialize return indicator
      boolean isOnLineSegment = false;
   
      // Check if Point q exists on line segment 
      if((q.getX() <= Math.max(p.getX(), r.getY())) &&
         (q.getX() >= Math.min(p.getX(), r.getX())) &&
         (q.getY() <= Math.max(p.getX(), r.getY())) &&
         (q.getY() >= Math.min(p.getY(), r.getY())))
      {
         isOnLineSegment = true;
      }
   
      return isOnLineSegment;
   }
}