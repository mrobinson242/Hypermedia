package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import data.Link;
import data.Point;
import dialogs.LinkCreationDialog;
import dialogs.interfaces.IDialog;
import enums.EFontAwesome;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
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
   private ListView<Link> _selectLinkView;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "VideoTool.fxml";

   /** Primary Video File */
   private File _primaryVideo;

   /** Secondary Video File */
   private File _secondaryVideo;

   /** Primary Video Media Player */
   private MediaPlayer _mediaPlayer1;

   /** Secondary Video Media Player */
   private MediaPlayer _mediaPlayer2;

   /** Dialog Window for Link Creation */
   private IDialog _linkCreationDialog;

   /** Current List of HyperLinks */
   private List<Link> _linkList;

   /** Polygon Math Utility Helper */
   private PolygonUtil _polygonUtil;

   /**
    * Constructor
    *
    * @param primaryStage - The primary stage of the application
    */
   public VideoToolController(final Stage primaryStage)
   {
      super(FXML_NAME);

      // Initialize Video Views to Hidden
      _primaryVideoView.setVisible(false);
      _secondaryVideoView.setVisible(false);

      // Initialize List of HyperLinks
      _linkList = new ArrayList<Link>();

      // Initialize Polygon Utility Helper();
      _polygonUtil = new PolygonUtil();

      // Initialize Link Creation Dialog
      _linkCreationDialog = new LinkCreationDialog(primaryStage, this);

      // Initialize Video Files to be null
      _primaryVideo = new File("");
      _secondaryVideo = new File("");

      // Update Icons of Buttons
      _importFileButton.setText(EFontAwesome.FILE_CODE.getCode());
      _importVideoButton.setText(EFontAwesome.FILE_VIDEO.getCode());
      _newFileButton.setText(EFontAwesome.NEW_FILE.getCode());
      _createLinkButton.setText(EFontAwesome.LINK.getCode());
      _saveButton.setText(EFontAwesome.SAVE.getCode());
      _deleteLinkButton.setText(EFontAwesome.TRASH.getCode());

      // Initialize Button States
      _newFileButton.setDisable(true);
      _deleteLinkButton.setDisable(true);
      //_createLinkButton.setDisable(true);

      // Button Listeners
      handleImportVideoButton();
      handleCreateLinkButton();
      handleDeleteLinkButton();

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
         _mediaPlayer1 = new MediaPlayer(primaryMedia);

         // Attach the Media Player to the Media View
         _primaryVideoView.setMediaPlayer(_mediaPlayer1);
      }
      catch (final Exception e)
      {
         // Log Error
         e.printStackTrace();
      }

      // OffLoad to the Display Thread
      Platform.runLater(() ->
      {
         // Update the Primary Slider
         setPrimarySlider();
      });
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
         _mediaPlayer2 = new MediaPlayer(secondaryMedia);

         // Attach the Media Playe rto the Media View
         _secondaryVideoView.setMediaPlayer(_mediaPlayer2);
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
    * handleImportVideoButton - Handles the Selection of the
    *                           Import Video Button
    */
   private void handleImportVideoButton()
   {
      _importVideoButton.setOnAction(event ->
      {
         // TODO - Implement
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
    * setPrimarySlider - Updates the Primary Slider based on the Primary Video
    */
   private void setPrimarySlider()
   {
     // Update the Primary Video
      _primaryVideoSlider.setMin(1);
      _primaryVideoSlider.setMax(9000);
      _primaryVideoSlider.setValue(1);
      _primaryVideoSlider.setMajorTickUnit(1);
      _primaryVideoSlider.setSnapToTicks(true);

      // Slider Change Listener
      _primaryVideoSlider.valueProperty().addListener(new ChangeListener<Number>()
      {
         public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val)
         {
            int val = (int) _primaryVideoSlider.getValue();
            int start = (int) Math.round((100./3) * val);
             System.out.println(start);
            _mediaPlayer1.seek(new Duration(start));
         }
      });
   }
}