package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import data.Link;
import data.Point;
import dialogs.LinkCreationDialog;
import dialogs.interfaces.IDialog;
import enums.EFontAwesome;
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
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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
   private Button _saveButton;

   @FXML
   private Button _linkButton;

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

      // Initialize List of Hyperlinks
      _linkList = new ArrayList<Link>();

      // Initialize Polygon Utility Helper();
      _polygonUtil = new PolygonUtil();

      // Initialize Link Creation Dialog
      _linkCreationDialog = new LinkCreationDialog(primaryStage, this);

      // Initialize Video Files to be null
      _primaryVideo = new File("");
      _secondaryVideo = new File("");

      // Update Icons of Buttons
      _linkButton.setText(EFontAwesome.LINK.getCode());
      _saveButton.setText(EFontAwesome.SAVE.getCode());
      _deleteLinkButton.setText(EFontAwesome.TRASH.getCode());

      // Initialize Button States
      _deleteLinkButton.setDisable(true);

      // Button Listeners
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
              // System.out.println("Inside Polygon " + link.getName() + ": " + isInsidePolygon);
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
    * handleLinkButtonSelection - Handles the Selection of the
    *                             Create Link Button
    */
   private void handleCreateLinkButton()
   {
      // Process Selection of Create Link Button
      _linkButton.setOnAction(event ->
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
}