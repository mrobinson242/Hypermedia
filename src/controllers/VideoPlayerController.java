package controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import enums.EFontAwesome;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import data.Link;
import data.Point;
import dialogs.ErrorDialog;
import dialogs.ImportVideoDialog;
import dialogs.LinkCreationDialog;
import dialogs.SaveDialog;
import dialogs.interfaces.IDialog;
import enums.EFontAwesome;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.PolygonUtil;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.io.IOException;
import com.google.gson.JsonElement;

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

   /** Current Frame of Video */
   private int _currentFrame;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "VideoPlayer.fxml";

   /** Frame Rate of Imported Videos */
   private static final int FPS = 30;

   /** The Start Frame of the Videos */
   private static final int MIN_FRAME = 1;

   /** Hyperlink File Chooser */
   private FileChooser _hyperlinkFileChooser;

   /** Stage of the Application Window */
   private final Stage _primaryStage;

   /** Home Page Controller */
   private HomePageController _homePageController;

   private ObservableList<Link> _linkData;

   /** Primary Video File */
   private File _video;

   /** Primary Video Media Player */
   private MediaPlayer _mediaPlayer;

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

      // Button Listeners
      handlePlayButton();
      handlePauseButton();
      handleStopButton();
      handleOpenVideoButton();
      handleSlider();

      _linkData = FXCollections.observableArrayList();

      // Initialize Slider States
      _videoSlider.setDisable(true);
      _videoProgressBar.setDisable(true);

      // Initialize the Hyperlink File Selector
      
   }



   /**
    * handlePlayButton - Listener for the Play Button Selection
    */
   private void handlePlayButton()
   {
      // Process Click of Play Button
      _playButton.setOnAction(event ->
      {
         // TODO: Remove Debug Stmt
         _mediaPlayer.play();
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
         // TODO: Remove Debug Stmt
         _mediaPlayer.pause();
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
         // TODO: Remove Debug Stmt
         System.out.println("Stop Button Selection");
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
         // Get the Hyperlink Video File from the File Chooser
         _homePageController.openHyperlinkFile();

      });
   }

   /**
    * loadVideo
    */
   public void loadVideo(ArrayList<Link> linkData)
   {
       
      // Update with Latest Data
      _linkData.clear();
      _linkData.setAll(linkData);



      // Get First Link in Hyperlink File
      final Link link = _linkData.get(0);

      // Update Videos
      setVideo(link.getFromVideo(), 1);

      // Update Links
      displayLinks(_currentFrame);

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

               // Set Current Primary Slider Value
               _videoSlider.setValue(_currentFrame);

               // Update Displayed Links
               displayLinks(_currentFrame);

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
       int totalFrames = totalSeconds * 30;

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
      if(_mediaPlayer != null) {
         _mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>()
         {
            @Override
            public void changed(ObservableValue<? extends Duration> ov, Duration oldTime, Duration newTime) {

               // Null Check Primary Video
               if(_video != null)
               {

                  // Update Current Primary Frame
                  _currentFrame = (int) ((newTime.toMillis()/1000) * 30) + 1;

                  System.out.println(_currentFrame);

                  // Update the Progress Bar
                  final double maxVal = _videoSlider.getMax();
                  _videoProgressBar.setProgress((double) _currentFrame/maxVal);


                  // Display Links associated with Current Frame
                  displayLinks(_currentFrame);
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
            // Null Check Primary Video
            if(_video != null)
            {

               // Update Current Primary Frame
               _currentFrame = newVal.intValue();

               // Get the Current Frame Time (ms)
               final double frameTime = (newVal.doubleValue()/FPS) * 1000;

               // Update the Progress Bar
               final double maxVal = _videoSlider.getMax();
               _videoProgressBar.setProgress(newVal.doubleValue()/maxVal);

               // Update the Primary Video Media Player
               _mediaPlayer.seek(new Duration(frameTime));

               // Display Links associated with Current Frame
               displayLinks(_currentFrame);
            }
         }
      });
   }

      /**
    * displayLinks - Displays all the Links associated
    *                with the current Frame
    *
    * @param frameNum - The Currently Displayed Frame
    */
   private void displayLinks(final int frameNum)
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

}