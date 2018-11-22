package controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import data.Link;
import data.Point;
import dialogs.ImportVideoDialog;
import dialogs.LinkCreationDialog;
import dialogs.interfaces.IDialog;
import enums.EFontAwesome;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.PolygonUtil;

/**
 * VideoToolController - Controls the User Interaction on the
 *                       HyperLinking Video Tool Page
 */
public class VideoToolController extends AbstractController
{
   @FXML
   private Pane _primaryVideoPane;

   @FXML
   private MediaView _primaryVideoView;

   @FXML
   private MediaView _secondaryVideoView;

   @FXML
   private Slider _primaryVideoSlider;

   @FXML
   private Slider _secondaryVideoSlider;

   @FXML
   private ProgressBar _primaryVideoProgressBar;

   @FXML
   private ProgressBar _secondaryVideoProgressBar;

   @FXML
   private Button _importFileButton;

   @FXML
   private Button _importVideoButton;

   @FXML
   private Button _newFileButton;

   @FXML
   private Button _saveButton;

   @FXML
   private Button _createLinkButton;

   @FXML
   private Button _deleteLinkButton;

   @FXML
   private Label _primaryVideoFrameLabel;

   @FXML
   private Label _primaryVideoFrame;

   @FXML
   private Label _secondaryVideoFrameLabel;

   @FXML
   private Label _secondaryVideoFrame;

   @FXML
   private ListView<Link> _selectLinkView;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "VideoTool.fxml";
   
   /** Frame Rate of Imported Videos */
   private static final int FPS = 30;

   /** Home Page Controller */
   private HomePageController _homePageController;

   /** Primary Video File */
   private File _primaryVideo;

   /** Secondary Video File */
   private File _secondaryVideo;

   /** Current HyperLink File */
   private File _currentHyperlinkFile;

   /** Primary Video Media Player */
   private MediaPlayer _primaryMediaPlayer;

   /** Secondary Video Media Player */
   private MediaPlayer _secondaryMediaPlayer;

   /** Dialog Window for Link Creation */
   private IDialog _linkCreationDialog;

   /** Dialog Window for Importing Videos */
   private IDialog _importVideoDialog;

   /** Current List of HyperLinks */
   private List<Link> _linkList;

   /** Polygon Math Utility Helper */
   private PolygonUtil _polygonUtil;

   /**
    * Constructor
    *
    * @param primaryStage - The primary stage of the application
    * @param homePageController - The Controller for the Home Page
    */
   public VideoToolController(final Stage primaryStage, final HomePageController homePageController)
   {
      super(FXML_NAME);

      // Initialize Controllers
      _homePageController = homePageController;

      // Initialize Video Views to Hidden
      _primaryVideoView.setVisible(false);
      _secondaryVideoView.setVisible(false);

      // Initialize List of HyperLinks
      _linkList = new ArrayList<Link>();

      // Initialize Polygon Utility Helper();
      _polygonUtil = new PolygonUtil();

      // Initialize Dialogs
      _linkCreationDialog = new LinkCreationDialog(primaryStage, this);
      _importVideoDialog = new ImportVideoDialog(primaryStage, homePageController);

      // Initialize Video Files to be null
      _primaryVideo = new File("");
      _secondaryVideo = new File("");

      // Initialize Current Hyperlink File to be null
      _currentHyperlinkFile = new File("");

      // Update Icons of Buttons
      _importFileButton.setText(EFontAwesome.FILE_CODE.getCode());
      _importVideoButton.setText(EFontAwesome.FILE_VIDEO.getCode());
      _newFileButton.setText(EFontAwesome.NEW_FILE.getCode());
      _createLinkButton.setText(EFontAwesome.LINK.getCode());
      _saveButton.setText(EFontAwesome.SAVE.getCode());
      _deleteLinkButton.setText(EFontAwesome.TRASH.getCode());

      // Create ToolTips for Buttons
      Tooltip importVideoTooltip = new Tooltip("Import Video Button");

      // Set Tooltips for Buttons
      _importVideoButton.setTooltip(importVideoTooltip);

      // Initialize Button States
      _newFileButton.setDisable(true);
      _deleteLinkButton.setDisable(true);
      _createLinkButton.setDisable(true);

      // Initialize Slider States
      _primaryVideoSlider.setDisable(true);
      _secondaryVideoSlider.setDisable(true);
      _primaryVideoProgressBar.setDisable(true);
      _secondaryVideoProgressBar.setDisable(true);

      // Initialize Label States
      _primaryVideoFrameLabel.setVisible(false);
      _primaryVideoFrame.setVisible(false);
      _secondaryVideoFrameLabel.setVisible(false);
      _secondaryVideoFrame.setVisible(false);

      // Button Listeners
      handleImportVideoButton();
      handleCreateLinkButton();
      handleDeleteLinkButton();
      handleSaveButton();

      // Slider Listeners
      handlePrimarySlider();
      handleSecondarySlider();

      // Select Link View Listeners
      handleSelectLinkView();

      // Mouse Press Listener
      _primaryVideoPane.setOnMousePressed(new EventHandler<MouseEvent>()
      {
        @Override public void handle(final MouseEvent mouseEvent)
        {
           // Get Mouse Event
           Point mousePoint = new Point(mouseEvent.getX(), mouseEvent.getY());

           // Iterate over the Current Links
           for(Link link : _linkList)
           {
              // Check if Mouse Press is inside Polygon
              boolean isInsidePolygon = _polygonUtil.isInsidePolygon(mousePoint, link.getVertices());

              // TODO: Remove Debug Stmt
              System.out.println("Inside Polygon " + link.getName() + ": " + isInsidePolygon);
           }
        }
      });
   }

   /**
    * setPrimaryVideo - Sets the Current Primary Video
    *
    * @param file - The Primary Video
    */
   public void setPrimaryVideo(final File primaryVideo)
   {
      // Update the Primary Video
      _primaryVideo = primaryVideo;
      _primaryVideoView.setVisible(true);

      try 
      {
         // Initialize Media
         final Media primaryMedia = new Media(_primaryVideo.toURI().toURL().toString());

         // Initialize the Primary Media Player
         _primaryMediaPlayer = new MediaPlayer(primaryMedia);

         // Attach the Media Player to the Media View
         _primaryVideoView.setMediaPlayer(_primaryMediaPlayer);

         // Wait until Media Player Ready to update Slider
         _primaryMediaPlayer.setOnReady(new Runnable()
         {
            @Override
            public void run() 
            {
               // Update the Primary Slider
               updatePrimarySlider();
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
    * setSecondaryVideo - Sets the Current Secondary Video
    *
    * @param file - The Secondary Video
    */
   public void setSecondaryVideo(final File secondaryVideo)
   {
      // Update the Secondary Video
      _secondaryVideo = secondaryVideo;
      _secondaryVideoView.setVisible(true);

      try 
      {
         // Create a Media Object
         final Media secondaryMedia = new Media(_secondaryVideo.toURI().toURL().toString());

         // Initialize the Secondary Media Player
         _secondaryMediaPlayer = new MediaPlayer(secondaryMedia);

         // Attach the Media Player to the Media View
         _secondaryVideoView.setMediaPlayer(_secondaryMediaPlayer);

         // Wait until Media Player Ready to update Slider
         _secondaryMediaPlayer.setOnReady(new Runnable()
         {
            @Override
            public void run() 
            {
               // Update the Secondary Slider
               updateSecondarySlider();
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
    * createHyperlink - Creates a new Hyperlink
    *
    * @param link - The new Hyperlink object
    */
   public void createHyperlink(final Link link)
   {
      // Add Link to ListView
      _selectLinkView.getItems().add(link);

      // Add Link to Stored List
      _linkList.add(link);

      // Add the Link's Bounding Box to the Video Pane
      _primaryVideoPane.getChildren().add(link.getBoundingGroup());
   }

   /**
    * getHyperLinkFile - Gets the Current HyperLink File
    *                    being edited in the Video Hyperlink Tool
    * @return File
    */
   public File getHyperlinkFile()
   {
      return _currentHyperlinkFile;
   }

   /**
    * saveDataToFile - Saves the Hyperlinks/Video Information
    *                  to the Data File
    *
    * @param file - file to save data to
    */
   public void saveDataToFile(File file)
   {
      // TODO: IMPLEMENT (Only Dummy Writer at the moment)
      System.out.println("Process Writing Data to File");

      try 
      {
         PrintWriter writer;
         writer = new PrintWriter(file);
         writer.println("This is a Test");
         writer.close();
      } 
      catch (IOException exception) 
      {
         // Log Error
         exception.printStackTrace();
      }
   }

   /**
    * handleImportVideoButton - Handles the Selection of the
    *                           Import Video Button
    */
   private void handleImportVideoButton()
   {
      // Process Selection of Import Video Button
      _importVideoButton.setOnAction(event ->
      {
         // Show the Import Video Dialog
         _importVideoDialog.showDialog();
      });
   }

   /**
    * handleLinkButtonSelection - Handles the Selection of the
    *                             Create Link Button
    */
   private void handleCreateLinkButton()
   {
      // Process Selection of Create Link Button
      _createLinkButton.setOnAction(event ->
      {
         // Show the Link Creation Dialog
         _linkCreationDialog.showDialog();
      });
   }

   /**
    * handleSaveButton - Handles the Selection
    *                    of the Save Button
    */
   private void handleSaveButton()
   {
      // Process the Selection of the Save Button
      _saveButton.setOnAction(event ->
      {
         // Save Hyperlink File as a New File
         _homePageController.saveHyperlinkFile(true);
      });
   }

   /**
    * handleDeleteLinkButtonSelection - Handles the Selection of the
    *                                   Delete Link Button
    */
   private void handleDeleteLinkButton()
   {
      // Process 
      _deleteLinkButton.setOnAction(event ->
      {
         // Get the Selected Link
         final Link selectedLink = _selectLinkView.getSelectionModel().getSelectedItem();

         // Remove Link from Primary Video Pane
         _primaryVideoPane.getChildren().remove(selectedLink.getBoundingGroup());

         // Remove Link from Stored List
         _linkList.remove(selectedLink);

         // Remove Link from Select Link View
         _selectLinkView.getItems().remove(selectedLink);

         // Select View
         _selectLinkView.getSelectionModel().clearSelection();

         // Disable Delete Link Button
         _deleteLinkButton.setDisable(true);
      });
   }

   /**
    * handleSelectLinkView - Handles the Listeners for the Select Link View
    */
   private void handleSelectLinkView()
   {
      // Update Display of Select Link View
      _selectLinkView.setCellFactory(param -> new ListCell<Link>()
      {
         @Override
         protected void updateItem(Link link, boolean empty)
         {
            super.updateItem(link, empty);

            if(empty || link == null || link.getName() == null)
            {
               setText(null);
            }
            else
            {
               setText(link.getName());
            }
         }
      });

      // Selection Listener for Select Link View
      _selectLinkView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Link>()
      {
         @Override
         public void changed(ObservableValue<? extends Link> arg0, Link unselectedLink, Link selectedLink)
         {
            // Null Check Selected Link
            if(selectedLink != null)
            {
               // Enable Delete Link Button
               _deleteLinkButton.setDisable(false);

               // Update Link Editable State
               selectedLink.setIsEditable(true);

               // Update Color of Link's Bounding Box to be Selected Color
               selectedLink.getBoundingBox().setStroke(Color.BLUE);
            }

            for(Link link : _selectLinkView.getItems())
            {
               // If link no longer selected
               if(!link.equals(_selectLinkView.getSelectionModel().getSelectedItem()))
               {
                  link.setIsEditable(false);
                  link.getBoundingBox().setStroke(Color.FORESTGREEN);
               }
            }
         }
      });
   }

   /**
    * handlePrimarySlider - Handles the Listeners for the Primary Slider
    */
   private void handlePrimarySlider()
   {
      // Slider Change Listener
      _primaryVideoSlider.valueProperty().addListener(new ChangeListener<Number>()
      {
         public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
         {
            // Null Check Primary Video
            if(_primaryVideo != null)
            {
                // Update Frame Count Label
                _primaryVideoFrame.setText(String.valueOf(newVal.intValue()));

                // Get the Current Frame Time (ms)
                final double frameTime = (newVal.doubleValue()/1) * 1000;

                // Update the Progress Bar
                final double maxVal = _primaryVideoSlider.getMax();
                _primaryVideoProgressBar.setProgress(newVal.doubleValue()/maxVal);

                // Update the Primary Video Media Player
               _primaryMediaPlayer.seek(new Duration(frameTime));
            }
         }
      });
   }

   /**
    * handleSecondarySlider - Handles the Listeners for the Secondary Slider
    */
   private void handleSecondarySlider()
   {
      // Slider Change Listener
      _secondaryVideoSlider.valueProperty().addListener(new ChangeListener<Number>()
      {
         public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
         {
            // Null Check Primary Video
            if(_secondaryVideo != null)
            {
                // Update Frame Count Label
                _secondaryVideoFrame.setText(String.valueOf(newVal.intValue()));

                // Get the Current Frame Time (ms)
                final double frameTime = (newVal.doubleValue()/FPS) * 1000;

                // Update the Progress Bar
                final double maxVal = _secondaryVideoSlider.getMax();
                _secondaryVideoProgressBar.setProgress(newVal.doubleValue()/maxVal);

                // Update the Primary Video Media Player
               _secondaryMediaPlayer.seek(new Duration(frameTime));
            }
         }
      });
   }

   /**
    * updatePrimarySlider - Updates the Primary Slider based
    *                       on the Primary Video
    */
   private void updatePrimarySlider()
   {
      // Enable Primary Video Slider
      _primaryVideoSlider.setDisable(false);
      _primaryVideoProgressBar.setDisable(false);

      // Get Number of Frames
      Duration duration = _primaryMediaPlayer.getTotalDuration();
      int totalSeconds = (int) Math.round(duration.toSeconds());
      int totalFrames = totalSeconds * 30;

      // Set Slider Properties
      _primaryVideoSlider.setMin(1);
      _primaryVideoSlider.setMax(totalFrames);
      _primaryVideoSlider.setValue(1);
      _primaryVideoSlider.setBlockIncrement(1.0);
      _primaryVideoSlider.setMajorTickUnit(1.0);
      _primaryVideoSlider.setSnapToTicks(true);

      // Show Primary Video Frame Labels
      _primaryVideoFrame.setVisible(true);
      _primaryVideoFrameLabel.setVisible(true);
   }

   /**
    * updateSecondarySlider - Updates the Secondary Slider based
    *                         on the Secondary Video
    */
   private void updateSecondarySlider()
   {
      // Enable Primary Video Slider
      _secondaryVideoSlider.setDisable(false);
      _secondaryVideoProgressBar.setDisable(false);

      // Get Number of Frames
      Duration duration = _secondaryMediaPlayer.getTotalDuration();
      int totalSeconds = (int) Math.round(duration.toSeconds());
      int totalFrames = totalSeconds * FPS;

      // Set Slider Properties
      _secondaryVideoSlider.setMin(1);
      _secondaryVideoSlider.setMax(totalFrames);
      _secondaryVideoSlider.setValue(1);
      _secondaryVideoSlider.setBlockIncrement(1.0);
      _secondaryVideoSlider.setMajorTickUnit(1.0);
      _secondaryVideoSlider.setSnapToTicks(true);

      // Show Secondary Video Frame Labels
      _secondaryVideoFrame.setVisible(true);
      _secondaryVideoFrameLabel.setVisible(true);
   }
}