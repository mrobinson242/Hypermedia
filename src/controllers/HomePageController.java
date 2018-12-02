package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import data.Link;
import enums.EHypermediaTab;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
   private MenuItem _newFileMenuItem;

   @FXML
   private MenuItem _openFileMenuItem;

   @FXML
   private MenuItem _importPrimaryVideoItem;

   @FXML
   private MenuItem _importSecondaryVideoItem;

   @FXML
   private MenuItem _saveMenuItem;

   @FXML
   private MenuItem _saveAsMenuItem;

   @FXML
   private MenuItem _createLinkMenuItem;

   @FXML
   private MenuItem _deleteLinkMenuItem;

   @FXML
   private MenuItem _playVideoMenuItem;

   @FXML
   private MenuItem _pauseVideoMenuItem;

   @FXML
   private MenuItem _stopVideoMenuItem;

   @FXML
   private MenuItem _exitMenuItem;

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

   /** Hyperlink File Chooser */
   private FileChooser _hyperlinkFileChooser;

   /** Primary Video File Chooser */
   private FileChooser _videoFileChooser;

   /** Save File File Chooser */
   private FileChooser _saveFileChooser;

   /** Current Video Tool File */
   private File _currentVideoToolFile;

   /** Current Video Player File */
   private File _currentVideoPlayerFile;

   /** Desktop File on a System's Computer */
   private File _desktopPath;

   /** Selected Hypermedia Application Tab */
   private EHypermediaTab _selectedTab;

   /** The Start Frame of the Videos */
   private static final int MIN_FRAME = 1;

   /** Boolean Indicator if Create Link is Available */
   private boolean _createLinkAvailable;

   /** Boolean Indicator if Delete Link is Available */
   private boolean _deleteLinkAvailable;

   /**
    * Constructor
    *
    * @param primaryStage - The primary stage of the application
    * @param loader - FXML Loader Utility
    */
   public HomePageController(final Stage primaryStage, final FXMLLoader loader)
   {
      super(FXML_NAME, loader);

      // Initialize Stage
      _stage = primaryStage;

      // Initialize Controllers
      _videoToolController = new VideoToolController(primaryStage, loader, this);
      _videoPlayerController = new VideoPlayerController(primaryStage, loader, this);

      // Default to Video Tool Button Selected
      _selectedTab = EHypermediaTab.VIDEO_TOOL;
      _videoToolButton.setSelected(true);

      // Default Menu Item Enabled/Disabled Indicators
      _createLinkAvailable = false;
      _deleteLinkAvailable = false;

      // Disable Unavailable Video Tool Menu Items
      _createLinkMenuItem.setDisable(true);
      _deleteLinkMenuItem.setDisable(true);

      // Disable Video Player Menu Items
      _playVideoMenuItem.setDisable(true);
      _pauseVideoMenuItem.setDisable(true);
      _stopVideoMenuItem.setDisable(true);

      // Set Video Tool Pane First
      _contentPane.getChildren().add(_videoToolController.getPane());

      // Initialize Current Video Files to be null
      _currentVideoToolFile = new File("");
      _currentVideoPlayerFile = new File("");

      // Initialize the Desktop Path
      _desktopPath = new File(System.getProperty("user.home"), "Desktop/");

      // Initialize the File Choosers
      initVideoFileChooser();
      initSaveFileChooser();
      initHyperlinkFileChooser();

      // Handle Button Listeners
      handleVideoToolButtonSelection();
      handleVideoPlayerButtonSelection();

      // Handle Menu Item Listeners
      importPrimaryVideoSelection();
      importSecondaryVideoSelection();
      handleCreateLinkSelection();
      handleDeleteLinkSelection();
      handleNewFileSelection();
      handleOpenFileSelection();
      handleSaveSelection();
      handleSaveAsSelection();
      handleExitSelection();

      // Set File Menu Accelerators
      _newFileMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
      _openFileMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
      _importPrimaryVideoItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
      _importSecondaryVideoItem.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
      _saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
      _exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

      // Set Option Menu Accelerators
      _createLinkMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
      _deleteLinkMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
      _playVideoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN));
      _pauseVideoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.ALT_DOWN));
      _stopVideoMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_DOWN));
   }

   /**
    * setCreateLinkState - Updates the Enabled/Disable State
    *                         of the Create Link Option
    *
    * @param isEnabled - Is Menu Item Enabled
    */
   public void setCreateLinkState(final boolean isEnabled)
   {
      // Update Create Link Availability State
      _createLinkAvailable = isEnabled;

      // Check that current tab is Hypermedia Tab
      if(EHypermediaTab.VIDEO_TOOL.equals(_selectedTab))
      {
         // Update Enable/Disable State of Item
         _createLinkMenuItem.setDisable(!isEnabled);
      }
      else
      {
         // Disable Create Link Menu Option
         _createLinkMenuItem.setDisable(true);
      }
   }

   /**
    * setDeleteLinkState - Updates the Enabled/Disabled State
    *
    * @param isEnabled - Is Menu Item Enabled
    */
   public void setDeleteLinkState(final boolean isEnabled)
   {
      // Update Delete Link Availability State
      _deleteLinkAvailable = isEnabled;

      // Check that current tab is Hypermedia Tab
      if(EHypermediaTab.VIDEO_TOOL.equals(_selectedTab))
      {
         // Update Enable/Disable State of Item
         _deleteLinkMenuItem.setDisable(!isEnabled);
      }
      else
      {
         // Disable Create Link Menu Option
         _deleteLinkMenuItem.setDisable(true);
      }
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
         _videoToolController.setPrimaryVideo(primaryVideo, MIN_FRAME);
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
         _videoToolController.setSecondaryVideo(secondaryVideo, MIN_FRAME);
      }
   }

   /**
    * openHyperlinkFile - Processes the Open File Functionality
    */
   public void openHyperlinkFile()
   {
      // Import Hyperlink File
      final File hyperlinkFile = _hyperlinkFileChooser.showOpenDialog(_stage);

      // Null Check Hyperlink File
      if(hyperlinkFile != null)
      {
         // Upload Data from File
         final ArrayList<Link> linkData = uploadDataFromFile(hyperlinkFile);

         // Check if Video Tool Tab Selected
         if(EHypermediaTab.VIDEO_TOOL.equals(_selectedTab))
         {
            // Open up the Hyperlink File in the Video Tool
             _videoToolController.openHyperlinkFile(hyperlinkFile, linkData);
         }
         else
         {
            _videoPlayerController.loadVideo(hyperlinkFile, linkData);
         }
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
    * setCurrentVideoToolFile - Sets the Current Video Tool File
    *
    * @param videoToolFile - The File being modified in the Video Tool
    */
   public void setCurrentVideoToolFile(final File videoToolFile)
   {
      _currentVideoToolFile = videoToolFile;
   }

   /**
    * setCurrentVideoPlayerFile - Sets the Current Video Player File
    *
    * @param videoPlayerFile - The File being played in the Video Player
    */
   public void setCurrentVideoPlayerFile(final File videoPlayerFile)
   {
      _currentVideoPlayerFile = videoPlayerFile;
   }

   /**
    * getCurrentVideoToolFile - Gets the Current Video Tool File
    *
    * @return File 
    */
   public File getCurrentVideoToolFile()
   {
      return _currentVideoToolFile;
   }

   /**
    * getCurrentVideoPlayerFile - Gets the Current Video Player File
    *
    * @return File
    */
   public File getCurrentVideoPlayerFile()
   {
      return _currentVideoPlayerFile;
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
         // Updated Selected Tab
         _selectedTab = EHypermediaTab.VIDEO_TOOL;

         // Clear Content Pane
         _contentPane.getChildren().clear();

         // Add Video Tool to Content Pane
         _contentPane.getChildren().add(_videoToolController.getPane());

         // DeSelect Video Player Button
         _videoToolButton.setSelected(true);
         _videoPlayerButton.setSelected(false);

         // Enable Menu Items
         setCreateLinkState(_createLinkAvailable);
         setDeleteLinkState(_deleteLinkAvailable);
         _saveMenuItem.setDisable(false);
         _saveAsMenuItem.setDisable(false);

         // Disable Menu Items
         _playVideoMenuItem.setDisable(true);
         _pauseVideoMenuItem.setDisable(true);
         _stopVideoMenuItem.setDisable(true);
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
         // Updated Selected Tab
         _selectedTab = EHypermediaTab.VIDEO_PLAYER;

         // Clear Content Pane
         _contentPane.getChildren().clear();

         // Add Video Tool to Content Pane
         _contentPane.getChildren().add(_videoPlayerController.getPane());

         // DeSelect Video Tool Button
         _videoToolButton.setSelected(false);
         _videoPlayerButton.setSelected(true);

         // Enable Menu Items
         if(!_currentVideoPlayerFile.getName().equals(""))
         {
            // Enable Menu Items
            _playVideoMenuItem.setDisable(false);
            _pauseVideoMenuItem.setDisable(false);
            _stopVideoMenuItem.setDisable(false);
         }

         // Disable Menu Items
         _createLinkMenuItem.setDisable(true);
         _deleteLinkMenuItem.setDisable(true);
         _saveMenuItem.setDisable(true);
         _saveAsMenuItem.setDisable(true);
      });
   }

   /**
    * handleCreateLinkSelection - Handles the Selection of
    *                             the Create Link Menu Item
    */
   private void handleCreateLinkSelection()
   {
      // Process Selection
      _createLinkMenuItem.setOnAction(event ->
      {
         //  Show the Create Link Dialog
         _videoToolController.showCreateLinkDialog();
      });
   }

   /**
    * handleDeleteLinkSelection - Handles the Selection of
    *                             the Delete Link Menu Item
    */
   private void handleDeleteLinkSelection()
   {
      // Process Selection
      _deleteLinkMenuItem.setOnAction(event ->
      {
         // Delete the Link
         _videoToolController.deleteHyperlink();
      });
   }

   /**
    * handleOpenFileSelection - Handles the Selection of the Open
    *                           File Menu Item
    */
   private void handleOpenFileSelection()
   {
      // Process Selection of the Open File Menu Item
      _openFileMenuItem.setOnAction(event ->
      {
         // Open up File Selector for Hyperlink File
         openHyperlinkFile();
      });
   }

   /**
    * handleNewFileSelection - Handles the Selection of the New
    *                          File Menu Item
    */
   private void handleNewFileSelection()
   {
      // Process Selection of the New File Menu Item
      _newFileMenuItem.setOnAction(event ->
      {
         // TODO: Implement
      });
   }

   /**
    * handleSaveSelection - Handles the Selection of
    *                       the Save Menu Item
    */
   private void handleSaveSelection()
   {
      // Process Selection of the Save Menu Item
      _saveMenuItem.setOnAction(event ->
      {
         // Save the Hyperlink File
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
         // Save the Hyperlink File
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
      _exitMenuItem.setOnAction(event ->
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

      //final File videoFile = new File(home);
      _videoFileChooser.setInitialDirectory(_desktopPath);
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

      // Determine the Type of Extension we want on our file
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
      _saveFileChooser.getExtensionFilters().add(extFilter);

      // Set Path to Hyperlink Save Files
      _saveFileChooser.setInitialDirectory(_desktopPath);
   }

   /**
    * initHyperlinkFileChooser - Initializes the Hyperlink File Chooser
    */
   private void initHyperlinkFileChooser()
   {
      // Initialize File Chooser
      _hyperlinkFileChooser = new FileChooser();

      // Set Title of Hyperlink File Chooser Window
      _hyperlinkFileChooser.setTitle("Import Hyperlink Video File");

      // Determine the Type of Extension we want on our file
      FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
      _hyperlinkFileChooser.getExtensionFilters().add(extFilter);

      // Set Path to Hyperlink Files
      _hyperlinkFileChooser.setInitialDirectory(_desktopPath);
   }

   /**
    * uploadDataFromFile - Reads in Data from Hyperlink File
    */
   private ArrayList<Link> uploadDataFromFile(final File file)
   {
      // Initialize List of links
      ArrayList<Link> linkData = new ArrayList<Link>();

      // Initialize JSON Parser
      JSONParser parser = new JSONParser();

      try 
      {
         Object obj = parser.parse(new FileReader(file));
         JSONObject jsonObject = (JSONObject) obj;
         Iterator<String> names = jsonObject.keySet().iterator();

         // Iterate over all the Links
         while(names.hasNext())
         {
            // Get all information about each link including toVideo, fromVideo, and frame numbers.
            String name = names.next();
            JSONObject linkInfo = (JSONObject) jsonObject.get(name);
            String toVideo = (String) linkInfo.get("toVideo");
            String fromVideo = (String) linkInfo.get("fromVideo");
            int startFrame = ((Long) linkInfo.get("startFrame")).intValue();
            int endFrame = ((Long) linkInfo.get("endFrame")).intValue();
            int toFrame = ((Long) linkInfo.get("toFrame")).intValue();

            // Get info about the bouding box coordinates for each frame in the link.
            JSONObject frameInfo = (JSONObject) linkInfo.get("boxInfo");
            Iterator<String> frames = frameInfo.keySet().iterator();
            HashMap<Integer, ArrayList<Double>> framePoints = new HashMap<Integer, ArrayList<Double>>();
            while (frames.hasNext())
            {
               String frame = (String) frames.next();
               JSONArray pointInfo = (JSONArray) frameInfo.get(frame);
               
               Iterator points = pointInfo.iterator();
               ArrayList<Double> pList = new ArrayList<Double>();

               // Iterate over the Points
               while (points.hasNext())
               {
                  pList.add((Double) points.next());
               }

               framePoints.put(Integer.parseInt(frame), pList);
            }
            // Reconstruct links given the information retrieved from the file and add them to the list data structure.
            Link now = new Link(name, startFrame, endFrame, startFrame, fromVideo, toVideo, toFrame, framePoints);
            linkData.add(now);
         }
      } 
      catch (FileNotFoundException e)
      {
         // Log Error
         e.printStackTrace();
      }
      catch (IOException e)
      {
         // Log Error
         e.printStackTrace();
      }
      catch (ParseException e)
      {
         // Log Error
         e.printStackTrace();
      }
      // Return a list of all the links that hve been constructed.
      return linkData;
   }
}