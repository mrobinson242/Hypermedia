package controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
   private MenuItem _createLinkMenuItem;

   @FXML
   private MenuItem _deleteLinkMenuItem;

   @FXML
   private MenuItem _exitMenuItem;

   @FXML
   private ToggleButton _videoToolButton;

   @FXML
   private ToggleButton _videoPlayerButton;

   @FXML
   private ToggleButton _mp4ConverterButton;

   @FXML
   private Stage _stage;

   /** Video Tool Controller */
   private VideoToolController _videoToolController;

   /** Video Player Controller */
   private VideoPlayerController _videoPlayerController;

   /** MP4 Converter Controller */
   private MP4ConverterController _mp4ConverterController;

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
      _mp4ConverterController = new MP4ConverterController(primaryStage, loader);

      // Default to Video Tool Button Selected
      _selectedTab = EHypermediaTab.VIDEO_TOOL;
      _videoToolButton.setSelected(true);

      // Default Menu Item Enabled/Disabled Indicators
      _createLinkAvailable = false;
      _deleteLinkAvailable = false;

      // Disable Unavailable Video Tool Menu Items
      _createLinkMenuItem.setDisable(true);
      _deleteLinkMenuItem.setDisable(true);
      _saveMenuItem.setDisable(true);

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
      handleMP4ConverterButtonSelection();

      // Handle Menu Item Listeners
      importPrimaryVideoSelection();
      importSecondaryVideoSelection();
      handleCreateLinkSelection();
      handleDeleteLinkSelection();
      handleNewFileSelection();
      handleOpenFileSelection();
      handleSaveSelection();
      handleExitSelection();

      // Set File Menu Accelerators
      _newFileMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
      _openFileMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
      _importPrimaryVideoItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
      _importSecondaryVideoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
      _saveMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
      _exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

      // Set Option Menu Accelerators
      _createLinkMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
      _deleteLinkMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
   }

   /**
    * addKeyboardListener - Adds a Keyboard Event Listener
    */
   public void addKeyboardListener()
   {
      _videoPlayerController.addKeyboardListener();
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
    * setSaveState - Sets the Save Button State
    *
    * @param isEnabled
    */
   public void setSaveState(final boolean isEnabled)
   {
      // Update the Save Menu Item State
      _saveMenuItem.setDisable(!isEnabled);
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

         // Check if the File already Exists
         _videoToolController.findHyperlinkFile();
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
         // Allow Saving
         _saveMenuItem.setDisable(false);

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
            _videoPlayerController.loadVideo(linkData);
         }
      }
   }

   /**
    * openHyperlinkFile - Processes the Open File Functionality
    */
   public void openHyperlinkFile(final File hyperlinkFile)
   {
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
            _videoPlayerController.loadVideo(linkData);
         }
      }
   }

   /**
    * saveHyperlinkFile - Processes the Save/Save As Functionality
    */
   public void saveHyperlinkFile()
   {
      // Get Hyperlink Video File
      File hyperlinkFile = _videoToolController.getHyperlinkFile();

      // Null Check Hyperlink File
      if(hyperlinkFile != null)
      {
         // Write out Hyperlink Information to File
         _videoToolController.saveDataToFile(hyperlinkFile);
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

         // Select/DeSelect Video Tool Button
         _videoToolButton.setSelected(true);
         _videoPlayerButton.setSelected(false);
         _mp4ConverterButton.setSelected(false);

         // Enable Menu Items
         setCreateLinkState(_createLinkAvailable);
         setDeleteLinkState(_deleteLinkAvailable);
         _saveMenuItem.setDisable(false);
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

         // Select/DeSelect Video Tool Button
         _videoToolButton.setSelected(false);
         _videoPlayerButton.setSelected(true);
         _mp4ConverterButton.setSelected(false);

         // Disable Menu Items
         _createLinkMenuItem.setDisable(true);
         _deleteLinkMenuItem.setDisable(true);
         _saveMenuItem.setDisable(true);
      });
   }

   /**
    * handleMP4ConverterButtonSelection - Handles the Selection of
    *                                     the MP4 Converter Toggle Button
    */
   private void handleMP4ConverterButtonSelection()
   {
      _mp4ConverterButton.setOnAction(event ->
      {
         // Updated Selected Tab
         _selectedTab = EHypermediaTab.VIDEO_PLAYER;

         // Clear Content Pane
         _contentPane.getChildren().clear();

         // Add Video Tool to Content Pane
         _contentPane.getChildren().add(_mp4ConverterController.getPane());

         // Select/DeSelect Video Tool Button
         _videoToolButton.setSelected(false);
         _videoPlayerButton.setSelected(false);
         _mp4ConverterButton.setSelected(true);
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
         saveHyperlinkFile();
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