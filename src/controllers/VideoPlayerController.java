package controllers;

import java.io.File;
import java.util.ArrayList;
import enums.EFontAwesome;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import data.Link;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import util.PolygonUtil;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import data.Point;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.HashMap;
import java.io.FileReader;

/**
 * VideoPlayerController - Controls the User Interaction on the
 *                         Interactive Video Player Page
 */
public class VideoPlayerController extends AbstractController
{
   @FXML
   private Button _playButton;

   @FXML
   private Button _pauseButton;

   @FXML
   private Button _stopButton;

   @FXML
   private Button _openVideoButton;

   @FXML
   private Slider _videoSlider;

   @FXML
   private ProgressBar _videoProgressBar;

   @FXML
   private Pane _videoPane;

   @FXML
   private MediaView _videoView;

   @FXML
   private Label _videoFrameLabel;

   @FXML
   private Label _videoFrameNumLabel;

   @FXML
   private Label _hyperlinkFilename;

   /** Current Frame of Video */
   private int _currentFrame;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "VideoPlayer.fxml";

   /** Frame Rate of Imported Videos */
   private static final int FPS = 30;

   /** The Start Frame of the Videos */
   private static final int MIN_FRAME = 1;

   /** Video Scale Factor */
   private static final double VIDEO_SCALE_FACTOR = 1.5;

   /** Hyperlink File Chooser */
   private FileChooser _hyperlinkFileChooser;

   /** Stage of the Application Window */
   private final Stage _primaryStage;

   /** Home Page Controller */
   private HomePageController _homePageController;

   /** Observable List of Link Data */
   private ObservableList<Link> _linkData;

   /** Primary Video File */
   private File _video;

   /** Primary Video Media Player */
   private MediaPlayer _mediaPlayer;

   /** Polygon Math Utility Helper */
   private PolygonUtil _polygonUtil;

   /** Path to Hyperlink Files on a System's Computer */
   private File _hyperlinkFilePath;

   /** Links Visibility Indicator */
   private Boolean _linksVisible;

   /**
    * Constructor
    *
    * @param primaryStage - The primary stage of the application
    * @param loader - The FXML Loader Utility
    * @param homePageController - The Controller for the Home Page
    */
   public VideoPlayerController(final Stage primaryStage, final FXMLLoader loader, final HomePageController homePageController)
   {
      super(FXML_NAME, loader);

      // Initialize the Stage of the Primary Window
      _primaryStage = primaryStage;

      _homePageController = homePageController;

      _videoView.setVisible(false);

      // Update Icons of Buttons
      _playButton.setText(EFontAwesome.PLAY.getCode());
      _pauseButton.setText(EFontAwesome.PAUSE.getCode());
      _stopButton.setText(EFontAwesome.STOP.getCode());
      _openVideoButton.setText(EFontAwesome.FILE_CODE.getCode());

      // Initialize Button State
      _playButton.setDisable(true);
      _pauseButton.setDisable(true);
      _stopButton.setDisable(true);

      // Initialize Video Frame Label
      _videoFrameLabel.setVisible(false);
      _videoFrameNumLabel.setVisible(false);
      _hyperlinkFilename.setVisible(false);

      // Initialize Polygon Utility Helper();
      _polygonUtil = new PolygonUtil();

      // Initialize Path to Hyperlink File
      _hyperlinkFilePath = new File(System.getProperty("user.home"), "Desktop/Hypermedia");

      // Button Listeners
      handlePlayButton();
      handlePauseButton();
      handleStopButton();
      handleOpenVideoButton();
      handleSlider();
      handleProgress();
      _linkData = FXCollections.observableArrayList();

      // Initialize Slider States
      _videoSlider.setDisable(true);
      _videoProgressBar.setDisable(true);

      // Initialize Links Visibility Indicator
      _linksVisible = true;

      // Handle Selection of HyperLinks
      handleHyperlinkSelection();
   }

   /**
    * addKeyboardListener - Adds a Keyboard Event Listener
    */
   public void addKeyboardListener()
   {
      // Key Pressed Listener
      _primaryStage.getScene().setOnKeyPressed(event ->
      {
         // Update Link Visibility Indicator
         if(_linksVisible)
         {
            _linksVisible = false;
         }
         else
         {
            _linksVisible = true;
         }

         // Check if L Key is Pressed
         if(event.getCode().equals(KeyCode.L))
         {
            // Check if Link Data is Empty
            if(!_linkData.isEmpty())
            {
               // Display Links
               displayLinks(_currentFrame, _linksVisible);
            }
         }
      });
   }

   /**
    * loadVideo
    */
   public void loadVideo(ArrayList<Link> linkData)
   {
      // Update with Latest Data
      _linkData.clear();

      // Check that there is Link Data
      if(!linkData.isEmpty())
      {
         // Update Link Data
         _linkData.setAll(linkData);

         // Update Button States
         _pauseButton.setDisable(true);
         _stopButton.setDisable(true);

         // Update the Vertices for the Video Player
         for(Link link : _linkData)
         {
            link.scaleVertices(VIDEO_SCALE_FACTOR);
         }

         // Get First Link in Hyperlink File
         final Link link = _linkData.get(0);

         // Update Videos
         setVideo(link.getFromVideo(), 1);

         // Update Links
         displayLinks(_currentFrame, _linksVisible);

         // Give Focus to Video Slider
         _videoSlider.requestFocus();
      }
   }

   /**
    * setVideo - Sets the Current Primary Video
    *
    * @param file     - The Primary Video
    * @param frameNum - The Current Frame
    */
   public void setVideo(final File primaryVideo, int frameNum)
   {
      // Update the Primary Video
      _video = primaryVideo;
      _videoView.setVisible(true);

      // Update Button States
      _playButton.setDisable(false);
      _pauseButton.setDisable(true);

      // Show Video Frame Labels
      _hyperlinkFilename.setText(FilenameUtils.getBaseName(primaryVideo.getName()));
      _videoFrameNumLabel.setText(String.valueOf(frameNum));
      _videoFrameLabel.setText("Paused Frame " );
      _videoFrameLabel.setVisible(true);
      _videoFrameNumLabel.setVisible(true);
      _hyperlinkFilename.setVisible(true);

      try 
      {
         // Initialize Media
         final Media primaryMedia = new Media(_video.toURI().toURL().toString());

         // Initialize the Primary Media Player
         _mediaPlayer = new MediaPlayer(primaryMedia);

         // Attach the Media Player to the Media View
         _videoView.setMediaPlayer(_mediaPlayer);

         // Wait until Media Player Ready to update Slider
         _mediaPlayer.setOnReady(new Runnable()
         {
            @Override
            public void run() 
            {
               // Update the Primary Slider
               updateSlider();

               // Update Current Primary Frame
               _currentFrame = frameNum;

               // Get the Current Frame Time (Seconds)
               final double frameTime = (frameNum/FPS) * 1000;

               // Update the Progress Bar
               final double maxVal = _videoSlider.getMax();
               final double progress = frameNum/maxVal;
               _videoProgressBar.setProgress(progress);

               // Update the Primary Video Media Player
               _mediaPlayer.seek(new Duration(frameTime));

               // Set Current Primary Slider Value
               _videoSlider.setValue(_currentFrame);

               // Update Displayed Links
               displayLinks(_currentFrame, _linksVisible);

               // Handle Progress
               handleProgress();
            }
         });
      }
      catch (final Exception e)
      {
         // Log Error
         e.printStackTrace();
      }
   }

   /**
    * pauseVideo - Pauses the Video
    */
   private void pauseVideo()
   {
      // Pause The Video
      _mediaPlayer.pause();

      // Update Video Frame Label
      _videoFrameLabel.setText("Paused Frame ");

      // Disable the Pause Button
      _pauseButton.setDisable(true);

      // Check if Final Frame
      if(_currentFrame == (int)Math.round(_videoSlider.getMax()))
      {
         // Disable the Play Button
         _playButton.setDisable(true);
      }
      else
      {
         // Enable the Play Button
         _playButton.setDisable(false);
      }

      // Give Focus to Video Slider
      _videoSlider.requestFocus();
   }

   /**
    * updateSlider - Updates the Primary Slider based
    *                       on the Primary Video
    */
   private void updateSlider()
   {
      // Enable Primary Video Slider
      _videoSlider.setDisable(false);
      _videoProgressBar.setDisable(false);

      // Get Number of Frames
      Duration duration = _mediaPlayer.getTotalDuration();
      int totalSeconds = (int) Math.round(duration.toSeconds());
      int totalFrames = totalSeconds * FPS;

      // Set Slider Properties
      _videoSlider.setMin(MIN_FRAME);
      _videoSlider.setMax(totalFrames);
      _videoSlider.setValue(1);
      _videoSlider.setBlockIncrement(1.0);
      _videoSlider.setMajorTickUnit(1.0);
      _videoSlider.setSnapToTicks(true);
   }

   /**
    * handleProgress - Updates Progress Bar and Frame Number based on time passed in video.
    */
   private void handleProgress()
   {
      // Slider Change Listener
      if(_mediaPlayer != null) 
      {
         // Media Player Time Listener
         _mediaPlayer.currentTimeProperty().addListener((observableValue , oldTime, newTime) ->
         {
            // Null Check Primary Video and video is playing
            if(_video != null && !_pauseButton.isDisable())
            {
               // Display Links associated with Current Frame
               displayLinks(_currentFrame, _linksVisible);

               // Update the Current Frame
               int timeElapsed = (int) Math.round(newTime.toSeconds());
               _currentFrame = timeElapsed * FPS;

               // Check if Current Frame Value is 0
               if(_currentFrame == 0)
               {
                  // Default Current Frame Min to 1
                  _currentFrame = 1;
               }

               // Update Frame Num Label
               _videoFrameNumLabel.setText(String.valueOf(_currentFrame));

               // Only Update if Video Slider is not being Pressed
               if(!_videoSlider.isPressed())
               {
                  // Update the Slider
                  _videoSlider.setValue(_currentFrame);

                  // Update the Progress Bar
                  final double maxVal = _videoSlider.getMax();
                  _videoProgressBar.setProgress(_currentFrame/maxVal);

                  // Check if End of the Video
                  final int theEnd = (int) Math.round(maxVal);
                  if(_currentFrame == theEnd)
                  {
                     pauseVideo();
                  }
               }
            }
         });
      }
   }

   /**
    * handleSlider- Handles the Listeners for the Primary Slider
    */
   private void handleSlider()
   {
      // Slider Change Listener
      _videoSlider.valueProperty().addListener(new ChangeListener<Number>()
      {
         public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
         {
            // Null Check Primary Video and ensure Video is not playing
            if(_video != null && (_videoSlider.isPressed() || !_playButton.isDisabled()))
            {
               // Update Current Primary Frame
               _currentFrame = newVal.intValue();

               // Check if Current Frame Value is 0
               if(_currentFrame == 0)
               {
                  // Default Current Frame Min to 1
                  _currentFrame = 1;

                  // Disable Stop Button
                  _stopButton.setDisable(true);
               }
               else if(_currentFrame == 1)
               {
                  // Disable Stop Button
                  _stopButton.setDisable(true);
               }
               // Check if Current Frame is Greater than initial frame
               else if(_currentFrame > 1)
               {
                  // Enable Stop Button
                  _stopButton.setDisable(false);
               }

               // Update the Progress Bar
               final double maxVal = _videoSlider.getMax();

               // Update Frame Number Label
               _videoFrameNumLabel.setText(String.valueOf(_currentFrame));

               // Get the Current Frame Time (Seconds)
               final double frameTime = (newVal.doubleValue()/FPS) * 1000;

               // Update Progress of Slider
               _videoProgressBar.setProgress(newVal.doubleValue()/maxVal);

               // Update the Primary Video Media Player
               _mediaPlayer.seek(new Duration(frameTime));

               // Display Links associated with Current Frame
               displayLinks(_currentFrame, _linksVisible);
            }
         }
      });

      // Mouse Pressed Listener
      _videoSlider.setOnMousePressed(event ->
      {
         _mediaPlayer.pause();
      });

      // Mouse Release Listener
      _videoSlider.setOnMouseReleased(event ->
      {
         // Check if in Play Mode and Pause Button is not Disabled
         if(_playButton.isDisable() && !_pauseButton.isDisable())
         {
            // Play the Video
            _mediaPlayer.play();
         }

         // Check if Play Button is Disabled and Paused Button is Disabled
         if(_playButton.isDisable() && _pauseButton.isDisable())
         {
            // Disable the Play Button
            _playButton.setDisable(false);
         }

         // Check if Final Frame
         if(_currentFrame == (int)Math.round(_videoSlider.getMax()))
         {
            // Pause The Video
            pauseVideo();

            // Disable the Play Button
            _playButton.setDisable(true);
         }
      });
   }

   /**
    * handlePlayButton - Listener for the Play Button Selection
    */
   private void handlePlayButton()
   {
      // Process Click of Play Button
      _playButton.setOnAction(event ->
      {
         // Play The Video
         _mediaPlayer.play();

         // Update Video Frame Label
         _videoFrameLabel.setText("Playing Frame ");

         // Disable the Play Button
         _playButton.setDisable(true);

         // Enable the Pause Button
         _pauseButton.setDisable(false);
         _stopButton.setDisable(false);
      });
   }

   /**
    * handlePauseButton - Listener for the Pause Button Selection
    */
   private void handlePauseButton()
   {
      // Process Click of Pause Button
      _pauseButton.setOnAction(event ->
      {
         // Pause the Video
         pauseVideo();
      });
   }

   /**
    * handleStopButton - Listener for the Stop Button Selection
    */
   private void handleStopButton()
   {
      // Process Click of Stop Button
      _stopButton.setOnAction(event ->
      {
         // Disable Stop/Pause Button
         _pauseButton.setDisable(true);
         _stopButton.setDisable(true);

         // OffLoad to Display Thread
         Platform.runLater(()->
         {
            // Stop Media Player
            _mediaPlayer.pause();

            // Reset to Start of Video
            _mediaPlayer.seek(new Duration(0.0));

            // Update Slider/Progress to Start of Video
            _videoSlider.setValue(0.0);
            _videoProgressBar.setProgress(0.0);

            // Update Video Frame Label
            _videoFrameLabel.setText("Paused Frame ");
            _videoFrameNumLabel.setText("1");

            // Update Current Frame
            _currentFrame = 1;

            // Display Links associated with Current Frame
            displayLinks(_currentFrame, _linksVisible);

            // Enable the Play Button
            _playButton.setDisable(false);

            // Give Focus to Video Slider
            _videoSlider.requestFocus();
         });
      });
   }

   /**
    * handleOpenVideoButton - Listener for the Open Video Button Selection
    */
   private void handleOpenVideoButton()
   {
      // Process Click of Open Video Button
      _openVideoButton.setOnAction(event ->
      {
         // Check if Video is Playing
         if(_playButton.isDisabled() && !_pauseButton.isDisabled() && !_stopButton.isDisabled())
         {
            // Pause the Current Video
            pauseVideo();
         }

         // Get the Hyperlink Video File from the File Chooser
         _homePageController.openHyperlinkFile();
      });
   }

   /**
    * displayLinks - Displays all the Links associated
    *                with the current Frame
    *
    * @param frameNum - The Currently Displayed Frame
    */
   private void displayLinks(final int frameNum, final boolean linksVisible)
   {
      // Clear existing Links
      _videoPane.getChildren().clear();

      // Re-Add the Primary Video View
      _videoPane.getChildren().add(_videoView);

      // Null Check Link List
      if(_linkData != null)
      {
         // Iterate over all Links associated with the frame
         for(Link link : _linkData)
         {
            // Update Link Visibility Accordingly
            link.setVisible(linksVisible);

            // Ensure Link is Associated with the Video
            if(link.getFromVideo().equals(_video))
            {
               // Update Current Display Frame in Primary Video View
               link.updateCurrentFrame(_currentFrame);

               // Check if Link exists in Frame
               if(link.containsFrame(frameNum))
               {
                  // Get Bounding Box associated with the Link
                  Group boundingBox = link.getBoundingGroup(_currentFrame);

                  // Null Check Bounding Box
                  if(boundingBox != null)
                  {
                     // Add the Link's Bounding Box to the Video Pane
                     _videoPane.getChildren().add(boundingBox);
                  }
               }
            }
         }
      }
   }

   /**
    * handleHyperlinkSelection
    */
   private void handleHyperlinkSelection()
   {
      // Mouse Press Listener
      _videoPane.setOnMousePressed(new EventHandler<MouseEvent>()
      {
         @Override public void handle(final MouseEvent mouseEvent)
         {
            // Get Mouse Event
            Point mousePoint = new Point(mouseEvent.getX(), mouseEvent.getY());

            // Initialize Boolean Indicator
            boolean isInsidePolygon = false;

            // Create Selected Link
            Link clickedLink = new Link("", 1, 1, 1);

            // Iterate over the Current Links
            for(Link link : _linkData)
            {
               // Get List of Vertices
               final List<Point> vertices = link.getVertices(_currentFrame);

               // Ensure that there are Vertices
               if(!vertices.isEmpty())
               {
                  // Check if Mouse Press is inside Polygon
                  isInsidePolygon = _polygonUtil.isInsidePolygon(mousePoint, link.getVertices(_currentFrame));

                  // Update Selected Link
                  clickedLink = link;
                  break;
               }
            }

            // Based on a link that was clicked, import in the video that the
            // link points to and corresponding hyperlink file
            if (isInsidePolygon) 
            {
               // Stop the Current Video
               _mediaPlayer.pause();

               // Get the To Video from the Selected Link
               _video = clickedLink.getToVideo();

               // Get the Video Name of the To Video
               String videoName = FilenameUtils.getBaseName(_video.getName());

               // Format File Path to Video's Metadata File
               String filePath = _hyperlinkFilePath + "/" + videoName + ".json";

               // Create new Hyperlink File
               final File hyperlinkFile = new File(filePath);

               // Clear Previous Link DAta
               _linkData.clear();

               // Check if Hyperlink Metadata File Exists
               if(hyperlinkFile.exists())
               {
                  // Get New Link Information
                  _linkData.setAll(uploadDataFromFile(hyperlinkFile));

                  // Update the Vertices for the Video Player
                  for(Link link : _linkData)
                  {
                     link.scaleVertices(VIDEO_SCALE_FACTOR);
                  }
               }

               // Set the Video accordingly
               setVideo(clickedLink.getToVideo(), clickedLink.getToFrame());
            }
         }
      });
   }

   /**
    * uploadDataFromFile - Reads in Data from Hyperlink File
    */
   private ArrayList<Link> uploadDataFromFile(final File file)
   {
      // Initialize List of links
      ArrayList<Link> linkData = new ArrayList<Link>();

      // Initialize JSON Parser
      JSONParser parser = new JSONParser();

      try 
      {
         Object obj = parser.parse(new FileReader(file));
         JSONObject jsonObject = (JSONObject) obj;
         Iterator<String> names = jsonObject.keySet().iterator();

         // Iterate over all the Links
         while(names.hasNext())
         {
            // Get all information about each link including toVideo, fromVideo, and frame numbers.
            String name = names.next();
            JSONObject linkInfo = (JSONObject) jsonObject.get(name);
            String toVideo = (String) linkInfo.get("toVideo");
            String fromVideo = (String) linkInfo.get("fromVideo");
            int startFrame = ((Long) linkInfo.get("startFrame")).intValue();
            int endFrame = ((Long) linkInfo.get("endFrame")).intValue();
            int toFrame = ((Long) linkInfo.get("toFrame")).intValue();

            // Get info about the bouding box coordinates for each frame in the link.
            JSONObject frameInfo = (JSONObject) linkInfo.get("boxInfo");
            Iterator<String> frames = frameInfo.keySet().iterator();
            HashMap<Integer, ArrayList<Double>> framePoints = new HashMap<Integer, ArrayList<Double>>();
            while (frames.hasNext())
            {
               String frame = (String) frames.next();
               JSONArray pointInfo = (JSONArray) frameInfo.get(frame);

               Iterator points = pointInfo.iterator();
               ArrayList<Double> pList = new ArrayList<Double>();

               // Iterate over the Points
               while (points.hasNext())
               {
                  pList.add((Double) points.next());
               }

               framePoints.put(Integer.parseInt(frame), pList);
            }
            // Reconstruct links given the information retrieved from the file and add them to the list data structure.
            Link now = new Link(name, startFrame, endFrame, startFrame, fromVideo, toVideo, toFrame, framePoints);
            linkData.add(now);
         }
      } 
      catch (FileNotFoundException e)
      {
         // Log Error
         e.printStackTrace();
      }
      catch (IOException e)
      {
         // Log Error
         e.printStackTrace();
      }
      catch (ParseException e)
      {
         // Log Error
         e.printStackTrace();
      }
      // Return a list of all the links that hve been constructed.
      return linkData;
   }
}