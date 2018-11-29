package controllers;

import java.util.ArrayList;
import java.util.List;
import data.Point;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;

/**
 * LinkBox - Object Class for a Link Bounding Box
 */
public class LinkBox 
{
   /** Bounding Group for the Link */
   private Group _linkGroup;

   /** Bounding Box for the Link */
   private Polygon _boundingBox;

   /** Bounding Box Anchors for Link */
   private ObservableList<BoxAnchor> _boxAnchors;

   /** Bounding Box Editable Indicator */
   private Boolean _isEditable;

   /**
    * Constructor
    */
   public LinkBox()
   {
      // Initialize Editable Indicator
      _isEditable = false;

      // Initialize Polygon for Bounding Box
      _boundingBox = createBoundingArea();

      // Initialize Bounding Box Anchors
      _boxAnchors = createBoxAnchors(_boundingBox.getPoints());

      // Initialize Bounding 
      _linkGroup = new Group();
      _linkGroup.getChildren().add(_boundingBox);
      _linkGroup.getChildren().addAll(_boxAnchors);
   }

   /**
    * getVertices - Gets the Vertices of the Polygon
    *
    * @return List<Point>
    */
   public List<Point> getVertices()
   {
      // Initialize List of Vertices
      final List<Point> vertices = new ArrayList<Point>();

      // Iterate over all the points in the Polygon
      for(int i=0; i < _boundingBox.getPoints().size(); i+=2)
      {
         // Get X/Y of Vertex from Polygon Bounding Box
         double x = _boundingBox.getPoints().get(i);
         double y = _boundingBox.getPoints().get(i+1);

         // Create new Point
         Point p = new Point(x,y);

         // Add Point to List
         vertices.add(p);
      }

      return vertices;
   }

   /**
    * updateBoxAnchors - Updates the CSS of the Bounding Box Anchors
    */
   public void updateBoxAnchors()
   {
      // Iterate over each Bounding Box Anchor
      for(final BoxAnchor anchor : _boxAnchors)
      {
         // Check if Bounding Box can be Edited
         if(_isEditable)
         {
            // Update CSS of Bounding Box Anchor
            anchor.setId("linkEditable");
         }
         else
         {
            // Update CSS of Bounding Box Anchor
            anchor.setId("linkUneditable");
         }
      }
   }

   /**
    * getBoundingBox - Gets the Bounding Box associated
    *                  with the Link
    *
    * @return Polygon
    */
   public Polygon getBoundingBox()
   {
      return _boundingBox;
   }

   /**
    * getBoundingGroup - Gets the Bounding Group (Box and Anchors)
    *                    associated with the Bounding Box
    *
    * @return Group - The Bounding Box
    */
   public Group getBoundingGroup()
   {
      return _linkGroup;
   }

   /**
    * setIsEditable - Updates if Bounding Box is Editable
    *
    * @param isEditable
    */
   public void setIsEditable(final boolean isEditable)
   {
      _isEditable = isEditable;
   }

   /**
    * createBoundingArea - Creates a Default Bounding Box
    *                      in the center of the Primary
    *                      Video View
    * @return
    */
   private Polygon createBoundingArea()
   {
      // Initialize Polygon
      Polygon polygon = new Polygon();

      // Initialize Points of Rectangle
      final Point p1 = new Point(126.0, 94.0);
      final Point p2 = new Point(226.0, 94.0);
      final Point p3 = new Point(226.0, 194.0);
      final Point p4 = new Point(126.0, 194.0);

      // Add Initial Points to Polygon
      polygon.getPoints().addAll(p1.getX(), p1.getY(),
                                 p2.getX(), p2.getY(),
                                 p3.getX(), p3.getY(),
                                 p4.getX(), p4.getY());

      // Initialize Polygon Attributes
      polygon.setStroke(Color.FORESTGREEN);
      polygon.setStrokeWidth(2);
      polygon.setFill(Color.TRANSPARENT);

      return polygon;
   }

   /**
    * 
    * @param pos
    * @return
    */
   private Polygon createBoundingArea(ArrayList<Double> pos) 
   {
      Polygon polygon = new Polygon();
      for (Double p : pos) {
        polygon.getPoints().add(p);
      }
      polygon.setStroke(Color.FORESTGREEN);
      polygon.setStrokeWidth(2);
      polygon.setFill(Color.TRANSPARENT);
      
      return polygon;
   }

   /**
    * createBoxAnchors
    *
    * @param points
    * @return ObservableList<BoxAnchor>
    */
   private ObservableList<BoxAnchor> createBoxAnchors(final ObservableList<Double> points) 
   {
      ObservableList<BoxAnchor> anchors = FXCollections.observableArrayList();

      // Iterate over Points
      for (int i = 0; i < points.size(); i+=2)
      {
        final int idx = i;

        DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
        DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

        xProperty.addListener(new ChangeListener<Number>()
        {
          @Override public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x)
          {
             if(_isEditable)
             {
                points.set(idx, (double) x);
             }
          }
        });

        yProperty.addListener(new ChangeListener<Number>()
        {
          @Override public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y)
          {
             if(_isEditable)
             {
                points.set(idx + 1, (double) y);
             }
          }
        });

        anchors.add(new BoxAnchor(xProperty, yProperty));
      }

      return anchors;
    }

   /**
    * Box Anchor - A Draggable Point for the Bounding Box
    */
   private class BoxAnchor extends Circle
   {
      /** X/Y Value of the Circle */
      private final DoubleProperty x, y;

      /**
       * Constructor
       */
      public BoxAnchor(final DoubleProperty x, final DoubleProperty y)
      {
         super(x.get(), y.get(), 10);
         setStrokeType(StrokeType.OUTSIDE);

         // Check if Box Anchor is Editable
         if(_isEditable)
         {
            // Update CSS of Box Anchor
            setId("linkEditable");
         }
         else
         {
            // Update CSS of Box Anchor
            setId("linkUneditable");
         }

         this.x = x;
         this.y = y;

         x.bind(centerXProperty());
         y.bind(centerYProperty());
         enableDrag();
      }

      /**
       * enableDrage
       */
      private void enableDrag()
      {
         final Point dragDelta = new Point();

         // Mouse Press Listener
         setOnMousePressed(new EventHandler<MouseEvent>()
         {
           @Override public void handle(final MouseEvent mouseEvent)
           {
              if(_isEditable)
              {
                 // record a delta distance for the drag and drop operation.
                 dragDelta.setX(getCenterX() - mouseEvent.getX());
                 dragDelta.setY(getCenterY() - mouseEvent.getY());
                 getScene().setCursor(Cursor.MOVE);
              }
           }
         });

         // Mouse Release Listener
         setOnMouseReleased(new EventHandler<MouseEvent>()
         {
           @Override public void handle(final MouseEvent mouseEvent)
           {
              if(_isEditable)
              {
                 getScene().setCursor(Cursor.HAND);
              }
           }
         });

         // Mouse Dragged Listener
         setOnMouseDragged(new EventHandler<MouseEvent>()
         {
           @Override public void handle(final MouseEvent mouseEvent)
           {
              // Check if Anchor is allowed to be Moved
              if(_isEditable)
              {
                 // Calculate new X/Y Position
                 double newX = mouseEvent.getX() + dragDelta.getX();
                 double newY = mouseEvent.getY() + dragDelta.getY();

                 // Check if Anchor is within confines
                 // of Primary Video Pane (X Dimension)
                 if (newX > 0 && newX < 352)
                 {
                    // Update X Position
                    setCenterX(newX);
                 }

                 // Check if Anchor is within confines
                 // of Primary Video Pane (Y Dimension)
                 if (newY > 0 && newY < 288)
                 {
                    // Update Y Position
                    setCenterY(newY);
                 }
              }
           }
         });

         setOnMouseEntered(new EventHandler<MouseEvent>()
         {
           @Override public void handle(MouseEvent mouseEvent)
           {
              if(_isEditable)
              {
                 if (!mouseEvent.isPrimaryButtonDown())
                 {
                   getScene().setCursor(Cursor.HAND);
                 }
              }
           }
         });

         setOnMouseExited(new EventHandler<MouseEvent>()
         {
           @Override public void handle(MouseEvent mouseEvent)
           {
              if(_isEditable)
              {
                 if (!mouseEvent.isPrimaryButtonDown())
                 {
                   getScene().setCursor(Cursor.DEFAULT);
                 }
              }
           }
         });
      }
   }
}