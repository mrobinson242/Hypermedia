package data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;

/**
 * Link - Implementation of a HyperLink Object
 */
public class Link 
{
   /** Name of the Link Object */
   private final SimpleStringProperty _linkName;

   /** Start Frame of the Link */
   private final SimpleIntegerProperty _startFrame;

   /** End Frame of the Link */
   private final SimpleIntegerProperty _endFrame;

   /** Link is Selected in Table Indicator */
   private Boolean _isSelected;

   /** File of the Video the Hyperlink points from */
   private File _fromVideo;

   /** File of the Video the Hyperlink points from */
   private File _toVideo;

   /** Frame of the Video the Hyperlink points to */
   private int _toFrame;

   /** Current Selected Frame in Primary Video */
   private int _currentFrame;

   /** Mapping of Frames to Bounding Box */
   private Map<Integer, LinkBox> _frameToBoxMap;

   /**
    * Constructor
    *
    * @param linkName - The name of the Link
    * @param startFrame - The Start Frame of the Link
    * @param endFrame - The End Frame of the Link
    */
   public Link(final String linkName, final int startFrame, final int endFrame, final int currentFrame)
   {
      // Initialize Link Name
      _linkName = new SimpleStringProperty(linkName);

      // Initialize Start/End Frame
      _startFrame = new SimpleIntegerProperty(startFrame);
      _endFrame = new SimpleIntegerProperty(endFrame);

      // Initialize the Current Frame
      _currentFrame = currentFrame;

      // Initialize To Frame
      _toFrame = 0;

      // Initialize To/From Videos
      _toVideo = new File("");
      _fromVideo = new File("");

      // Initialize Selected Indicator
      _isSelected = false;

      // Initialize Frame to Link Map
      _frameToBoxMap = new HashMap<Integer, LinkBox>();

      // Iterate over each Frame
      for(int i = startFrame; i <= endFrame; ++i)
      {
         // Create a New Link Bounding Box
         LinkBox linkBox = new LinkBox(this);

         // Add Bounding Box to Map
         _frameToBoxMap.put(i, linkBox);
      }
   }

   /**
    * Constructor
    *
    * @param name
    * @param startFrame
    * @param endFrame
    * @param fromVideo
    * @param toVideo
    * @param toFrame
    * @param pos
    */
   public Link(String name, final int startFrame, final int endFrame, final int currentFrame, String fromVideo, String toVideo, int toFrame, HashMap<Integer, ArrayList<Double>> pos)
   {
      // Initialize Link Name
      _linkName = new SimpleStringProperty(name);

      // Initialize Start/End Frame
      _startFrame = new SimpleIntegerProperty(startFrame);
      _endFrame = new SimpleIntegerProperty(endFrame);

      _currentFrame = currentFrame;

      // Initialize To/From Videos
      _toVideo = new File(toVideo);
      _fromVideo = new File(fromVideo);

      // Initialize To Frame
      _toFrame = toFrame;

      _isSelected = false;

      // Initialize Frame to Link Map
      _frameToBoxMap = new HashMap<Integer, LinkBox>();

      // Iterate over each Frame
      for(int i = startFrame; i <= endFrame; ++i)
      {
         // Create a New Link Bounding Box
         LinkBox linkBox = new LinkBox(this, pos.get(i));

         // Add Bounding Box to Map
         _frameToBoxMap.put(i, linkBox);
      }
   }

   /**
    * performLinearInterpolation - Performs Linear Interpolation
    */
   public void performLinearInterpolation()
   {
      // Amount of Frames the Link is in
      final int dFrameTime = _endFrame.get() - _startFrame.get();

      synchronized (_frameToBoxMap)
      {
         // Get the Link Box Associated with the Start/End Frame
         LinkBox startLinkBox = _frameToBoxMap.get(_startFrame.get());
         LinkBox endLinkBox = _frameToBoxMap.get(_endFrame.get());

         // Get Vertices for Start/End Link Box
         List<Double> startVertices = startLinkBox.getPoints();
         List<Double> endVertices = endLinkBox.getPoints();

         // Iterate over Points in BoundingBox
         for(int i = 0; i < startVertices.size(); i+=2)
         {
            // Get X/Y of Start Vertex
            final double startX = startVertices.get(i);
            final double startY = startVertices.get(i+1);

            // Get X/Y of End Vertex
            final double endX = endVertices.get(i);
            final double endY = endVertices.get(i+1);

            // Calculate Change in X/Y Value of the Vertex per Frame
            final double dx = (endX - startX) / dFrameTime;
            final double dy = (endY - startY) / dFrameTime;

            // Initialize Change in X/Y Value per frame
            double fX = 0;
            double fY = 0;

            // Iterate over all the Frames the Link is in
            for(int j = _startFrame.get(); j <= _endFrame.get(); ++j)
            {
               // Get the Link Box associated with the Frame
               LinkBox linkBox = _frameToBoxMap.get(j);

               // Get X/Y of the Vertex
               final double x = linkBox.getPoints().get(i);
               final double y = linkBox.getPoints().get(i+1);

               // Update X/Y of the Vertex
               linkBox.getPoints().set(i, startX+fX);
               linkBox.getPoints().set(i+1, startY+fY);

               // Increment Values
               fX += dx;
               fY += dy;
            }
         }
      }
   }

   /**
    * containsFrame - Checks if Link exists in Current Frame
    *
    * @param frameNum - The Currently Displayed Frame
    * @return boolean
    */
   public boolean containsFrame(final int frameNum)
   {
      // Initialize Indicator
      boolean isInFrame = false;

      // Check if Link is within bounds of frame
      if((_startFrame.intValue() <= frameNum) && (frameNum <= _endFrame.intValue()))
      {
         // Link exists inside current frame
         isInFrame = true;
      }

      return isInFrame;
   }

   /**
    * getLinkName - Get the Link Name
    *
    * @return String
    */
   public String getLinkName()
   {
      return _linkName.get();
   }

   /**
    * getStartFrame - Gets the Start Frame
    *                 for the Link
    * @return Integer
    */
   public Integer getStartFrame()
   {
      return _startFrame.get();
   }

   /**
    * getEndFrame - Gets the End Frame
    *               for the Link
    * @return Integer
    */
   public Integer getEndFrame()
   {
      return _endFrame.get();
   }

   /**
    * getFromVideo - Get's the Path of the video
    *                the Hyperlink is from
    * @return file
    */
   public File getFromVideo()
   {
      return _fromVideo;
   }

   /**
    * getToVideo - Gets the Path of the video
    *              the Hyperlink points toward
    * @return File
    */
   public File getToVideo()
   {
      return _toVideo;
   }

   /**
    * getToFrame - Gets the Frame of the video
    *              the Hyperlink points toward
    * @return
    */
   public int getToFrame()
   {
      return _toFrame;
   }


   /**
    * getBoundingGroup - Get the Bounding Group for the Link's
    *                    Bounding Box at the associated Frame
    *                    
    * @return Group
    */
   public Group getBoundingGroup(final int frameNum)
   {
      // Initialize Bounding Group
      Group boundingGroup = new Group();

      synchronized (_frameToBoxMap)
      {
         // Get the Link Box Associated with the Frame
         LinkBox linkBox = _frameToBoxMap.get(frameNum);

         // Null Check Link Box
         if(linkBox != null)
         {
            boundingGroup = linkBox.getBoundingGroup();
         }
      }

      return boundingGroup;
   }

   /**
    * getVertices - Gets the Vertices of the Polygon
    *
    * @return List<Point>
    */
   public List<Point> getVertices(final int frameNum)
   {
      // Initialize List of Vertices
      List<Point> vertices = new ArrayList<Point>();

      synchronized (_frameToBoxMap)
      {
         // Get the Link Box Associated with the Frame
         LinkBox linkBox = _frameToBoxMap.get(frameNum);

         // Null Check Link Box
         if(linkBox != null)
         {
            vertices = linkBox.getVertices();
         }
      }

      return vertices;
   }

   /**
    * setFromVideo - Sets the Path of the video
    *                the Hyperlink is from
    *
    * @param fromVideo
    */
   public void setFromVideo(final File fromVideo)
   {
      _fromVideo = fromVideo;
   }

   /**
    * setToVideo - Sets the Path of the video
    *              the Hyperlink points toward
    * @param toVideo
    */
   public void setToVideo(final File toVideo)
   {
      _toVideo = toVideo;
   }

   /**
    * setLinkName - Sets the Name of the Link
    *
    * @param linkName - Name of the Link
    */
   public void setLinkName(final String linkName)
   {
      _linkName.set(linkName);
   }

   /**
    * setStartFrame
    *
    * @param startFrame - The Start Frame
    */
   public void setStartFrame(final int startFrame)
   {
      _startFrame.set(startFrame);
   }

   /**
    * setEndFrame
    *
    * @param endFrame
    */
   public void setEndFrame(final int endFrame)
   {
      _endFrame.set(endFrame);
   }

   /**
    * setToFrame - Sets the Frame of the video
    *              the Hyperlink points toward
    *
    * @param toFrame
    */
   public void setToFrame(final int toFrame)
   {
      _toFrame = toFrame;
   }

   /**
    * setIsSelected - Update whether the Link is Selected
    *
    * @param isSelected - Selection Indication
    */
   public void setIsSelected(final boolean isSelected)
   {
      // Update Selected Indicator
      _isSelected = isSelected;

      // Update Editable State of Link
      updateEditableState();

      // Update Style of Bounding Box
      updateBoundingBoxStyle(isSelected);
   }

   /**
    * setVisible - Set the Visibility of the Link
    *
    * @param isVisible - Is Visible Indicator
    */
   public void setVisible(final boolean isVisible)
   {
      synchronized (_frameToBoxMap)
      {
         // Iterate over each Frame
         for(int i = _startFrame.get(); i <= _endFrame.get(); ++i)
         {
            // Get the Link Box Associated with the Frame
            LinkBox linkBox = _frameToBoxMap.get(i);

            // Update the Visibility Property of the Link
            linkBox.setIsVisible(isVisible);
         }
      }
   }

   /**
    * scaleVertices
    *
    * @param scaleFactor
    */
   public void scaleVertices(final double scaleFactor)
   {
      synchronized (_frameToBoxMap)
      {
         // Iterate over each Frame
         for(int i = _startFrame.get(); i <= _endFrame.get(); ++i)
         {
            // Get the Link Box Associated with the Frame
            LinkBox linkBox = _frameToBoxMap.get(i);

            // Update the Vertices in the Video
            linkBox.scaleVertices(scaleFactor);
         }
      }
   }

   /**
    * updateCurrentFrame
    */
   public void updateCurrentFrame(final int frameNum)
   {
      // Updates the Current Frame
      _currentFrame = frameNum;

      // Update Editable State of Link
      updateEditableState();

      // Update Style of Bounding Box
      updateBoundingBoxStyle(_isSelected);
   }

   /**
    * updateBoundingBoxStyle - Updates the CSS of the Bounding Box
    */
   public void updateBoundingBoxStyle(final boolean isSelected)
   {
      synchronized (_frameToBoxMap)
      {
         // Get the Link Box Associated with the Frame
         LinkBox linkBox = _frameToBoxMap.get(_currentFrame);

         // Null Check Link Box
         if(linkBox != null)
         {
            // Check Link Selection Indicator
            if(isSelected)
            {
               // Update Bounding Box Style
               linkBox.getBoundingBox().setId("linkSelected");
            }
            else
            {
               // Update Bounding Box Style
               linkBox.getBoundingBox().setId("linkUnselected");
            }

            // Update Bounding Box Anchors Style
            linkBox.updateBoxAnchors(isSelected);
         }
      }
   }

   /**
    * updateStartFrameBoundingGroup - Updates Bounding Box Link Group
    */
   public void updateStartFrameBoundingGroup(final int oldValue)
   {
      // Initialize Set of Irrelevant Frames
      Set<Integer> irrelevantFrames = new HashSet<Integer>();

      synchronized (_frameToBoxMap)
      {
         // Remove irrelevant frame
         for(Integer frameNum : _frameToBoxMap.keySet())
         {
            // If less than start or end frames
            if(frameNum < _startFrame.get())
            {
               // add to list of irrelevant frames
               irrelevantFrames.add(frameNum);
            }
         }

         // Iterate over each Frame
         for(int i = _startFrame.get(); i <= _endFrame.get(); ++i)
         {
            // Create a New Link Bounding Box
            LinkBox linkBox = _frameToBoxMap.get(i);

            // Null Check Link Box
            if(linkBox == null)
            {
               // Get LinkBox of Old Start/End Value
               LinkBox oldBox = _frameToBoxMap.get(oldValue);

               // Null Check Old Box
               if(oldBox != null)
               {
                  // Create new LinkBox for Map
                  ArrayList<Double> points = new ArrayList<Double>(oldBox.getPoints());
                  LinkBox box = new LinkBox(this, points);

                  // Add Bounding Box to Map
                  _frameToBoxMap.put(i, box);
               }
            }
         }

         // Iterate over Irrelevant Frames
         for(Integer frame : irrelevantFrames)
         {
            _frameToBoxMap.remove(frame);
         }
      }
   }

   /**
    * updateEndFrameBoundingGroup - Updates Bounding Box Link Group
    */
   public void updateEndFrameBoundingGroup(final int oldValue)
   {
      // Initialize Set of Irrelevant Frames
      Set<Integer> irrelevantFrames = new HashSet<Integer>();

      synchronized (_frameToBoxMap)
      {
         // Remove irrelevant frame
         for(Integer frameNum : _frameToBoxMap.keySet())
         {
            // If less than start or end frames
            if(frameNum > _endFrame.get() && oldValue > _endFrame.get())
            {
               // add to list of irrelevant frames
               irrelevantFrames.add(frameNum);
            }
         }

         // Iterate over each Frame
         for(int i = _startFrame.get(); i <= _endFrame.get(); ++i)
         {
            // Create a New Link Bounding Box
            LinkBox linkBox = _frameToBoxMap.get(i);

            // Null Check Link Box
            if(linkBox == null)
            {
               // Get LinkBox of Old Start/End Value
               LinkBox oldBox = _frameToBoxMap.get(oldValue);

               // Null Check Old Box
               if(oldBox != null)
               {
                  // Create new LinkBox for Map
                  ArrayList<Double> points = new ArrayList<Double>(oldBox.getPoints());
                  LinkBox box = new LinkBox(this, points);

                  // Add Bounding Box to Map
                  _frameToBoxMap.put(i, box);
               }
            }
         }

         // Iterate over Irrelevant Frames
         for(Integer frame : irrelevantFrames)
         {
            _frameToBoxMap.remove(frame);
         }
      }
   }

   /**
    * updateEditableState - Updates if the Link can be Edited
    */
   private void updateEditableState()
   {
      synchronized (_frameToBoxMap)
      {
         // Get the Link Box Associated with the Frame
         LinkBox linkBox = _frameToBoxMap.get(_currentFrame);

         // Null Check Link Box
         if(linkBox != null)
         {
            // If Link is Selected and Current Frame is either Start/End Frame
            if(_isSelected && ((_startFrame.intValue() == _currentFrame) || (_endFrame.intValue() == _currentFrame)))
            {
               // Allow Link to be edited
               linkBox.setIsEditable(true);
               linkBox.getBoundingGroup().setMouseTransparent(false);
            }
            else
            {
               // Disallow Link Edits
               linkBox.setIsEditable(false);
               linkBox.getBoundingGroup().setMouseTransparent(true);
            }

            // Update Box Anchors of Link Box
            linkBox.updateBoxAnchors(_isSelected);
         }
      }
   }
}