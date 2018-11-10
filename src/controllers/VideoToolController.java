package controllers;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaView;

/**
 * VideoToolController - Controls the User Interaction on the
 *                       HyperLinking Video Tool Page
 */
public class VideoToolController extends AbstractController
{
   @FXML
   private MediaView _primaryVideoView;

   @FXML
   private MediaView _secondaryVideoView;

   @FXML
   private Slider _primaryVideoSlider;

   @FXML
   private Slider _secondaryVideoSlider;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "VideoTool.fxml";

   /** Primary Video File */
   private File _primaryVideo;

   /** Secondary Video File */
   private File _secondaryVideo;

   /**
    * Constructor
    */
   public VideoToolController()
   {
      super(FXML_NAME);

      // Initialize Video Views to Hidden
      _primaryVideoView.setVisible(false);
      _secondaryVideoView.setVisible(false);

      // Initialize Video Files to be null
      _primaryVideo = new File("");
      _secondaryVideo = new File("");
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
}