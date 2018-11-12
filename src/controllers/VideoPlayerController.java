package controllers;

import enums.EFontAwesome;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "VideoPlayer.fxml";

   /**
    * Constructor
    */
   public VideoPlayerController()
   {
      super(FXML_NAME);

      // Update Icons of Buttons
      _playButton.setText(EFontAwesome.PLAY.getCode());
      _pauseButton.setText(EFontAwesome.PAUSE.getCode());
      _stopButton.setText(EFontAwesome.STOP.getCode());

      // Button Listeners
      handlePlayButton();
      handlePauseButton();
      handleStopButton();
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
         System.out.println("Play Button Selection");
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
         System.out.println("Pause Button Selection");
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
}