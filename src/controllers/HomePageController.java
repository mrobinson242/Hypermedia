package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * HomePageController - Controls the UI on the Default Home Page
 *                      of the HyperMedia Application
 */
public class HomePageController extends VBox
{
   @FXML
   private Pane _contentPane;

   @FXML
   private ToggleButton _videoToolButton;

   @FXML
   private ToggleButton _videoPlayerButton;

   /** Video Tool Controller */
   private VideoToolController _videoToolController;

   /** Video Player Controller */
   private VideoPlayerController _videoPlayerController;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "HomePage.fxml";

   /**
    * Constructor
    */
   public HomePageController()
   {
      //super(FXML_NAME, new VBox());

      // Initialize String Builder
      StringBuilder builder = new StringBuilder();

      // Create Path to FXML File
      builder.append("../fxml/");
      builder.append(FXML_NAME);
      String fxmlPath = builder.toString();

      // Create Path to FXML File
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      loader.setController(this);
      loader.setRoot(this);
      try 
      {
         loader.load();
      } 
      catch (IOException e) 
      {
         e.printStackTrace();
      }

      // Initialize Controllers
      _videoToolController = new VideoToolController();
      _videoPlayerController = new VideoPlayerController();

      // Default to Video Tool Button Selected
      _videoToolButton.setSelected(true);

      // Set Video Tool Pane First
      _contentPane.getChildren().add(_videoToolController.getPane());

      // Handle Button Listeners
      handleVideoToolButtonSelection();
      handleVideoPlayerButtonSelection();
   }

   /**
    * handleVideoToolButtonSelection - Handles the Selection of
    *                                  the Video Tool Toggle Button
    */
   private void handleVideoToolButtonSelection()
   {
      // Button Listener
      _videoToolButton.setOnAction(event ->
      {
         // Clear Content Pane
         _contentPane.getChildren().clear();

         // Add Video Tool to Content Pane
         _contentPane.getChildren().add(_videoToolController.getPane());
      });
   }

   /**
    * handleVideoPlayerButtonSelection - Handles the Selection of
    *                                    the Video Player Toggle Button
    */
   private void handleVideoPlayerButtonSelection()
   {
      // Button Listener
      _videoPlayerButton.setOnAction(event ->
      {
         // Clear Content Pane
         _contentPane.getChildren().clear();
      });
   }

   public Pane getPane()
   {
      return this;
   }
}
