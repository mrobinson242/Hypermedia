package data;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import util.PolygonUtil;

/**
 * LinkBox - Object Class for a Link Bounding Box
 */
public class LinkBox 
{
   /** The Link the LinkBox is associated with */
   private Link _parent;

   /** Bounding Group for the Link */
   private Group _linkGroup;

   /** Bounding Box for the Link */
   private Polygon _boundingBox;

   /** Bounding Box Anchors for Link */
   private ObservableList<BoxAnchor> _boxAnchors;

   private ObservableList<Double> _vertices;

   /** Bounding Box Editable Indicator */
   private Boolean _isEditable;

   /** Width/Height of Video Pane */
   private static int WIDTH = 352;
   private static int HEIGHT = 288;

   /** Mouse Position */
   private ObjectProperty<Point2D> _mousePosition;
   private List<Double> _moveVertexList;

   private Double _layoutX;
   private Double _layoutY;

   /** Polygon Math Utility Helper */
   private PolygonUtil _polygonUtil;

   /**
    * Constructor
    */
   public LinkBox(Link parent)
   {
      // Initialize Parent Link
      _parent = parent;

      // Initialize Editable Indicator
      _isEditable = false;

      // Initialize Polygon for Bounding Box
      _boundingBox = createBoundingArea();

      // Initialize Bounding Box Layout
      _layoutX = _boundingBox.getLayoutX();
      _layoutY = _boundingBox.getLayoutY();

      // Initialize Observable List of Points Vertices
      _vertices = _boundingBox.getPoints();

      // Initialize Bounding Box Anchors
      _boxAnchors = createBoxAnchors(_boundingBox, _vertices);

      // Initialize Bounding 
      _linkGroup = new Group();
      _linkGroup.getChildren().add(_boundingBox);
      _linkGroup.getChildren().addAll(_boxAnchors);

      // Initialize Mouse Position
      _mousePosition = new SimpleObjectProperty<>();
      _moveVertexList = new ArrayList<Double>();

      // Initialize Polygon Utility Helper();
      _polygonUtil = new PolygonUtil();

      // Handle Listener on a Link Drag
      handleLinkDrag();
   }

   /**
    * Constructor
    * @pos
    */
   public LinkBox(Link parent, ArrayList<Double> pos)
   {
      // Initialize Parent Link
      _parent = parent;

      // Initialize Editable Indicator
      _isEditable = false;

      // Initialize Polygon for Bounding Box
      _boundingBox = createBoundingArea(pos);

      // Initialize Bounding Box Layout
      _layoutX = _boundingBox.getLayoutX();
      _layoutY = _boundingBox.getLayoutY();

      // Initialize Observable List of Points Vertices
      _vertices = _boundingBox.getPoints();

      // Initialize Bounding Box Anchors
      _boxAnchors = createBoxAnchors(_boundingBox, _vertices);

      // Initialize Bounding 
      _linkGroup = new Group();
      _linkGroup.getChildren().add(_boundingBox);
      _linkGroup.getChildren().addAll(_boxAnchors);

      // Initialize Mouse Position
      _mousePosition = new SimpleObjectProperty<>();
      _moveVertexList = new ArrayList<Double>();

      // Initialize Polygon Utility Helper();
      _polygonUtil = new PolygonUtil();

      // Handle Listener on a Link Drag
      handleLinkDrag();
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
    * getPoints
    *
    * @return
    */
   public ObservableList<Double> getPoints()
   {
      return _vertices;
   }

   /**
    * updateBoxAnchors - Updates the CSS of the Bounding Box Anchors
    */
   public void updateBoxAnchors(final boolean isSelected)
   {
      // Iterate over each Bounding Box Anchor
      for(final BoxAnchor anchor : _boxAnchors)
      {
         // Check if Bounding Box can be Edited
         if(_isEditable && isSelected)
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
   private ObservableList<BoxAnchor> createBoxAnchors(Polygon polygon, final ObservableList<Double> points) 
   {
      ObservableList<BoxAnchor> anchors = FXCollections.observableArrayList();

      // Iterate over Vertices in Bounding Box
      for (int i = 0; i < points.size(); i+=2)
      {
         // Initialize Coordinate Indices
         final int xCoord = i;
         final int yCoord = i+1;

         // X/Y Vertex Properties
         DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
         DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

         // X Coordinate Listener of the Anchor
         xProperty.addListener(new ChangeListener<Number>()
         {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x)
            {
               // Check if Bounding Box can be Edited
               if(_isEditable)
               {
                  points.set(xCoord, (double) x);
               }
            }
         });

         // Y Coordinate Listener of the Anchor
         yProperty.addListener(new ChangeListener<Number>()
         {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y)
            {
               // Check if Bounding Box can be Edited
               if(_isEditable)
               {
                  points.set(yCoord, (double) y);
               }
            }
         });

         // Create new Anchor
         BoxAnchor anchor = new BoxAnchor(xProperty, yProperty);
         //anchor.layoutXProperty().bind(xProperty);
         //anchor.layoutYProperty().bind(yProperty);

         // Add Anchor to List
         anchors.add(anchor);
      }

      return anchors;
   }

   /**
    * handleLinkDrag - Handles a Drag of a Link
    */
   public void handleLinkDrag()
   {
      // Mouse Pressed Listener
      _boundingBox.setOnMousePressed(new EventHandler<MouseEvent>()
      {
         @Override
         public void handle(final MouseEvent event)
         {
            // Check if Bounding Box can be edited
            if(_isEditable)
            {
               // Update Mouse Position
               _mousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));

               // Iterate over the Box Anchors
               for(BoxAnchor anchor : _boxAnchors)
               {
                  // Hide the Anchors
                  anchor.setVisible(false);
               }

               // Get the Latest Layout
               _layoutX = _boundingBox.getLayoutX();
               _layoutY = _boundingBox.getLayoutY();

               // Clear Move Vertex List
               _moveVertexList.clear();

               // Iterate over Vertex List
               for(Double val : _vertices)
               {
                  _moveVertexList.add(val);
               }

               // Update Cursor to Move Cursor
               _boundingBox.getScene().setCursor(Cursor.MOVE);
            }
         }
      });

      // Mouse Dragged Listener
      _boundingBox.setOnMouseDragged(new EventHandler<MouseEvent>()
      {
         @Override
         public void handle(final MouseEvent event) 
         {
            // Check if Bounding Box can be edited
            if(_isEditable)
            {
               // Get Mouse Position
               Point mousePos = new Point(event.getX(), event.getY());

               // Get Change in X/Y
               double dx = event.getSceneX() - _mousePosition.get().getX();
               double dy = event.getSceneY() - _mousePosition.get().getY();

               // Initialize Can Drag Indicator
               boolean canDrag = true;

               // Create Temporary List containing the Modified Vertex Values
               ArrayList<Double> tempList = new ArrayList<Double>(_moveVertexList);

               // Iterate over Vertices in Bounding Box
               for (int i = 0; i < _moveVertexList.size(); i+=2)
               {
                  // Initialize Coordinate Indices
                  final int xCoord = i;
                  final int yCoord = i+1;

                  // Get Vertex Point
                  double x = _moveVertexList.get(xCoord);
                  double y = _moveVertexList.get(yCoord);

                  // Calculate New Point
                  double newX = x+dx;
                  double newY = y+dy;

                  if(newX < 0 || newX > WIDTH)
                  {
                     canDrag = false;
                     break;
                  }
      
                  if(newY < 0 || newY > HEIGHT)
                  {
                     canDrag = false;
                     break;
                  }

                  _moveVertexList.set(xCoord, newX);
                  _moveVertexList.set(yCoord, newY);
               }

               if(canDrag)
               {
                  // Update Bounding Box Position
                  _boundingBox.setLayoutX(_boundingBox.getLayoutX() + dx);
                  _boundingBox.setLayoutY(_boundingBox.getLayoutY() + dy);

                  // Update Mouse Position
                  _mousePosition.set(new Point2D(event.getSceneX(), event.getSceneY()));
               }
               else
               {
                  _moveVertexList.clear();
                  _moveVertexList.addAll(tempList);
               }
            }
         }
      });

      // Mouse Release Listener
      _boundingBox.setOnMouseReleased(new EventHandler<MouseEvent>()
      {
         @Override public void handle(final MouseEvent mouseEvent)
         {
            // Check if Bounding Box can be edited
            if(_isEditable)
            {
               // Update the Bounding Box Layout
               _boundingBox.setLayoutX(_layoutX);
               _boundingBox.setLayoutY(_layoutY);

               // Iterate over Vertices in Bounding Box
               for (int i = 0; i < _boundingBox.getPoints().size(); i+=2)
               {
                  // Initialize Coordinate Indices
                  final int xCoord = i;
                  final int yCoord = i+1;

                  // Get Vertex Point
                  double x = _moveVertexList.get(xCoord);
                  double y = _moveVertexList.get(yCoord);

                  // Update the Vertex Values
                  _vertices.set(xCoord, x);
                  _vertices.set(yCoord, y);
               }

               // Create New Anchors based on new Position
               _boxAnchors.clear();
               _boxAnchors = createBoxAnchors(_boundingBox, _boundingBox.getPoints());
               _linkGroup.getChildren().addAll(_boxAnchors);

               // Update all Links
               _parent.performLinearInterpolation();

               // Update Mouse Cursor to be a Hand
               _boundingBox.getScene().setCursor(Cursor.HAND);
            }
         }
      });

      // Mouse Entered Listener
      _boundingBox.setOnMouseEntered(new EventHandler<MouseEvent>()
      {
         @Override public void handle(MouseEvent mouseEvent)
         {
            if(_isEditable)
            {
               if (!mouseEvent.isPrimaryButtonDown())
               {
                  _boundingBox.getScene().setCursor(Cursor.HAND);
               }
            }
         }
      });

      // Mouse Exited Listener
      _boundingBox.setOnMouseExited(new EventHandler<MouseEvent>()
      {
         @Override public void handle(MouseEvent mouseEvent)
         {
            if(_isEditable)
            {
               if (!mouseEvent.isPrimaryButtonDown())
               {
                  _boundingBox.getScene().setCursor(Cursor.DEFAULT);
               }
            }
         }
      });
   }

   /**
    * Box Anchor - A Draggable Point for the Bounding Box
    */
   private class BoxAnchor extends Circle
   {
      /** Position of the Circle */
      private final DoubleProperty _x;
      private final DoubleProperty _y;

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

         // Set Position of the Circle
         _x = x;
         _y = y;

         x.bind(centerXProperty());
         y.bind(centerYProperty());
         enableDrag();
      }

      /**
       * setVisible - Set the Visibility of the Box Anchor
       *
       * @param isVisible
       */
      public BoxAnchor getAnchor()
      {
         return this;
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
               // Check if Anchor is allowed to be Moved
               if(_isEditable)
               {
                  // Update Cursor to be a Hand
                  getScene().setCursor(Cursor.HAND);

                  // Update all Links
                  _parent.performLinearInterpolation();
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

         // Mouse Entered Listener
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

         // Mouse Exit Listener
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