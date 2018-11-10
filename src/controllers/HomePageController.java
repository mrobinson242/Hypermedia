package controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * HomePageController - Controls the UI on the Default Home Page
 *                      of the HyperMedia Application
 */
public class HomePageController extends AbstractController
{
   @FXML
   private Pane _contentPane;

   @FXML
   private MenuItem _importPrimaryVideoButton;

   @FXML
   private MenuItem _importSecondaryVideoButton;

   @FXML
   private ToggleButton _videoToolButton;

   @FXML
   private ToggleButton _videoPlayerButton;
   
   @FXML
   private Stage _stage;

   /** Video Tool Controller */
   private VideoToolController _videoToolController;

   /** Video Player Controller */
   private VideoPlayerController _videoPlayerController;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "HomePage.fxml";

   /** Primary Video File Chooser */
   private FileChooser _fileChooser;

   /**
    * Constructor
    */
   public HomePageController(final Stage stage)
   {
      super(FXML_NAME);

      // Set Stage
      _stage = stage;

      // Initialize Controllers
      _videoToolController = new VideoToolController();
      _videoPlayerController = new VideoPlayerController();

      // Default to Video Tool Button Selected
      _videoToolButton.setSelected(true);

      // Set Video Tool Pane First
      _contentPane.getChildren().add(_videoToolController.getPane());

      // Init the File Chooser
      initFileChooser();

      // Handle Button Listeners
      handleVideoToolButtonSelection();
      handleVideoPlayerButtonSelection();
      importPrimaryVideoSelection();
      importSecondaryVideoSelection();
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

   /**
    * importPrimaryVideoSelection - Handles the Selection of the
    *                               Import Primary Video Selection
    *                               Menu Item
    */
   private void importPrimaryVideoSelection()
   {
      // Process Selection of Import Primary Video
      _importPrimaryVideoButton.setOnAction(event ->
      {
         // Set Title of File Chooser Window
         _fileChooser.setTitle("Import Primary Video");

         // Get the Primary Video File from the File Chooser
         final File primaryVideo = _fileChooser.showOpenDialog(_stage);

         // Update Video Tool with Selected File
         _videoToolController.setPrimaryVideo(primaryVideo);
      });
   }

   /**
    * importSecondaryVideoSelection - Handles the Selection of the
    *                                 Import Secondary Video Selection
    *                                 Menu Item
    */
   private void importSecondaryVideoSelection()
   {
      // Process Selection of Import Secondary Video
      _importSecondaryVideoButton.setOnAction(event ->
      {
         // Set Title of File Chooser Window
         _fileChooser.setTitle("Import Secondary Video");

         // Get Secondary Video File from File Chooser
         final File secondaryVideo = _fileChooser.showOpenDialog(_stage);

         // Update Video Tool with Selected File
         _videoToolController.setSecondaryVideo(secondaryVideo);
      });
   }

   /**
    * initFileChooser - Initializes the File Chooser
    *                   to point to the Video File
    *                   Directory
    */
   private void initFileChooser()
   {
      // Initialize File Chooser
      _fileChooser = new FileChooser();

      // Get Current Directory
      Path currentRelativePath = Paths.get("");
      String s = currentRelativePath.toAbsolutePath().toString();

      // Create File Path
      StringBuilder sb = new StringBuilder();
      sb.append(s);
      sb.append("/src/videos");

      // Set Path to Video Files
      final File videoFile = new File(sb.toString());
      _fileChooser.setInitialDirectory(videoFile);
   }
}