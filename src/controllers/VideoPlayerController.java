package controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import enums.EFontAwesome;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "VideoPlayer.fxml";

   /** Hyperlink File Chooser */
   private FileChooser _hyperlinkFileChooser;

   /** Stage of the Application Window */
   private final Stage _primaryStage;

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

      // Initialize Slider States
      _videoSlider.setDisable(true);
      _videoProgressBar.setDisable(true);

      // Initialize the Hyperlink File Selector
      initHyperlinkFileChooser();
   }

   /**
    * initHyperlinkFileChooser - Initializes the Hyperlink File Chooser
    */
   private void initHyperlinkFileChooser()
   {
      // Initialize File Chooser
      _hyperlinkFileChooser = new FileChooser();

      // Set Title of Hyperlink File Chooser Window
      _hyperlinkFileChooser.setTitle("Load Hyperlinked Video");

      // Determine the Type of Extension we want on our file
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
      _hyperlinkFileChooser.getExtensionFilters().add(extFilter);

      // Get Current Directory
      Path currentRelativePath = Paths.get("");
      String s = currentRelativePath.toAbsolutePath().toString();

      // Create File Path
      StringBuilder sb = new StringBuilder();
      sb.append(s);
      sb.append("/src/files");

      // Set Path to Video Files
      final File hyperlinkFile = new File(sb.toString());
      _hyperlinkFileChooser.setInitialDirectory(hyperlinkFile);
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

   /**
    * handleOpenVideoButton - Listener for the Open Video Button Selection
    */
   private void handleOpenVideoButton()
   {
      // Process Click of Open Video Button
      _openVideoButton.setOnAction(event ->
      {
         // Get the Hyperlink Video File from the File Chooser
         final File hyperlinkVideo = _hyperlinkFileChooser.showOpenDialog(_primaryStage);

         // Load the Video into the Video Player
         loadVideo();
      });
   }

   /**
    * loadVideo
    */
   private void loadVideo()
   {
      // TODO: Implement
   }
}