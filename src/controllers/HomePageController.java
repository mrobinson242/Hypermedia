package controllers;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
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
   private MenuItem _importPrimaryVideoItem;

   @FXML
   private MenuItem _importSecondaryVideoItem;

   @FXML
   private MenuItem _saveMenuItem;

   @FXML
   private MenuItem _saveAsMenuItem;

   @FXML
   private MenuItem _createLinkItem;

   @FXML
   private MenuItem _exitItem;

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
   private FileChooser _videoFileChooser;

   /** Save File File Chooser */
   private FileChooser _saveFileChooser;

   /**
    * Constructor
    * 
    * @param primaryStage - The primary stage of the application
    */
   public HomePageController(final Stage primaryStage)
   {
      super(FXML_NAME);

      // Initialize Stage
      _stage = primaryStage;

      // Initialize Controllers
      _videoToolController = new VideoToolController(primaryStage, this);
      _videoPlayerController = new VideoPlayerController();

      // Default to Video Tool Button Selected
      _videoToolButton.setSelected(true);

      // Set Video Tool Pane First
      _contentPane.getChildren().add(_videoToolController.getPane());

      // Initialize the File Choosers
      initVideoFileChooser();
      initSaveFileChooser();

      // Handle Button Listeners
      handleVideoToolButtonSelection();
      handleVideoPlayerButtonSelection();

      // Handle Menu Item Listeners
      importPrimaryVideoSelection();
      importSecondaryVideoSelection();
      createLinkSelection();
      handleSaveSelection();
      handleSaveAsSelection();
      handleExitSelection();

      // Set Accelerators
      _importPrimaryVideoItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
      _importSecondaryVideoItem.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
      _saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
   }

   /**
    * importPrimaryVideo - Opens up a File Chooser Dialog
    *                      to select the Primary Video
    */
   public void importPrimaryVideo()
   {
      // Set Title of File Chooser Window
      _videoFileChooser.setTitle("Import Primary Video");

      // Get the Primary Video File from the File Chooser
      final File primaryVideo = _videoFileChooser.showOpenDialog(_stage);

      // Null Check Primary Video
      if(null != primaryVideo)
      {
         // Update Video Tool with Selected File
         _videoToolController.setPrimaryVideo(primaryVideo);
      }
   }

   /**
    * importSecondaryVideo - Opens up a File Chooser Dialog
    *                        to select the Primary Video
    */
   public void importSecondaryVideo()
   {
      // Set Title of File Chooser Window
      _videoFileChooser.setTitle("Import Secondary Video");

      // Get Secondary Video File from File Chooser
      final File secondaryVideo = _videoFileChooser.showOpenDialog(_stage);

      // Null Check Secondary Video
      if(null != secondaryVideo)
      {
         // Update Video Tool with Selected File
         _videoToolController.setSecondaryVideo(secondaryVideo);
      }
   }

   /**
    * saveHyperlinkFile - Processes the Save/Save As Functionality
    */
   public void saveHyperlinkFile(final boolean isSaveAs)
   {
      // Check if Save As Function
      if(isSaveAs)
      {
         // Open up Save File Dialog
         File saveFile = _saveFileChooser.showSaveDialog(_stage);

         // Null Check Save File
         if(saveFile != null)
         {
            // Write out Hyperlink Information to File
            _videoToolController.saveDataToFile(saveFile);
         }
      }
      // Else Handle Save Functionality
      else
      {
         // Get Hyperlink Video File
         File hyperlinkFile = _videoToolController.getHyperlinkFile();

         // Check if valid Hyperlink File
         if(hyperlinkFile.getName() != "")
         {
            // Write out Hyperlink Information to File
            _videoToolController.saveDataToFile(hyperlinkFile);
         }
         else
         {
            // Open up Save File Dialog
            File saveFile = _saveFileChooser.showSaveDialog(_stage);

            // Null Check Save File
            if(saveFile != null)
            {
               // Write out Hyperlink Information to File
               _videoToolController.saveDataToFile(saveFile);
            }
         }
      }
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

         // DeSelect Video Player Button
         _videoToolButton.setSelected(true);
         _videoPlayerButton.setSelected(false);
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

         // Add Video Tool to Content Pane
         _contentPane.getChildren().add(_videoPlayerController.getPane());

         // DeSelect Video Tool Button
         _videoToolButton.setSelected(false);
         _videoPlayerButton.setSelected(true);
      });
   }

   /**
    * createLinkSelection - Handles the Selection of
    *                       the Create Link Menu Item
    */
   private void createLinkSelection()
   {
      // Process Selection
      _createLinkItem.setOnAction(event ->
      {
         // TODO: Implement
      });
   }

   /**
    * handleSaveSelection - Handles the Selection of
    *                 the Save Menu Item
    */
   private void handleSaveSelection()
   {
      // Process Selection of the Save Menu Item
      _saveMenuItem.setOnAction(event ->
      {
         saveHyperlinkFile(false);
      });
   }

   /**
    * handleSaveAsSelection - Handles the Selection of the 
    *                         Save As Menu Item
    */
   private void handleSaveAsSelection()
   {
      // Process Selection of the Save As Menu Item
      _saveAsMenuItem.setOnAction(event ->
      {
         saveHyperlinkFile(true);
      });
   }

   /**
    * handleExitSelection - Handles the Selection of the Exit
    *                       Menu Item
    */
   private void handleExitSelection()
   {
      // Process Selection of Exit Menu Item
      _exitItem.setOnAction(event ->
      {
         // Exit Application
         System.exit(0);
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
      _importPrimaryVideoItem.setOnAction(event ->
      {
         // Opens up File Chooser to Select Primary Video
         importPrimaryVideo();
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
      _importSecondaryVideoItem.setOnAction(event ->
      {
         // Opens up File Chooser to Select Secondary Video
         importSecondaryVideo();
      });
   }

   /**
    * initVideoFileChooser - Initializes the File Chooser
    *                        to point to the Video File
    *                        Directory
    */
   private void initVideoFileChooser()
   {
      // Initialize File Chooser
      _videoFileChooser = new FileChooser();

      // Get Current Directory
      Path currentRelativePath = Paths.get("");
      String s = currentRelativePath.toAbsolutePath().toString();

      // Create File Path
      StringBuilder sb = new StringBuilder();
      sb.append(s);
      sb.append("/src/videos");

      // Set Path to Video Files
      final File videoFile = new File(sb.toString());
      _videoFileChooser.setInitialDirectory(videoFile);
   }
   
   /**
    * initVideoFileChooser - Initializes the File Chooser
    *                        to point to the Hyperlink File
    *                        Directory
    */
   private void initSaveFileChooser()
   {
      // Initialize File Chooser
      _saveFileChooser = new FileChooser();

      // TODO: Determine the Type of Extension we want on our file
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
      _saveFileChooser.getExtensionFilters().add(extFilter);

      // Get Current Directory
      Path currentRelativePath = Paths.get("");
      String s = currentRelativePath.toAbsolutePath().toString();

      // Create File Path
      StringBuilder sb = new StringBuilder();
      sb.append(s);
      sb.append("/src/files");

      // Set Path to Video Files
      final File hyperlinkFile = new File(sb.toString());
      _saveFileChooser.setInitialDirectory(hyperlinkFile);
   }
}