package controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;

/**
 * VideoToolController - Controls the User Interaction on the
 *                       HyperLinking Video Tool Page
 */
public class VideoToolController extends Pane
{
   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "VideoTool.fxml";

   @FXML
   private MediaView _primaryVideoView;

   @FXML
   private MediaView _secondaryVideoView;

   @FXML
   private Slider _primaryVideoSlider;

   @FXML
   private Slider _secondaryVideoSlider;

   /**
    * Constructor
    */
   public VideoToolController()
   {
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

      // Initialize Video Views to Hidden
      _primaryVideoView.setVisible(false);
      _secondaryVideoView.setVisible(false);
   }
   
   public Pane getPane()
   {
      return this;
   }
}
