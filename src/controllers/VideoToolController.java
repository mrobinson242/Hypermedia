package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import data.Link;
import data.Point;
import dialogs.ImportVideoDialog;
import dialogs.LinkCreationDialog;
import dialogs.SaveDialog;
import dialogs.interfaces.IDialog;
import enums.EFontAwesome;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.PolygonUtil;
import java.util.List;

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
   private ProgressBar _primaryVideoProgressBar;

   @FXML
   private ProgressBar _secondaryVideoProgressBar;

   @FXML
   private Button _importFileButton;

   @FXML
   private Button _importVideoButton;

   @FXML
   private Button _newFileButton;

   @FXML
   private Button _saveButton;

   @FXML
   private Button _createLinkButton;

   @FXML
   private Button _deleteLinkButton;

   @FXML
   private Label _primaryVideoFrameLabel;

   @FXML
   private Label _primaryVideoFrame;

   @FXML
   private Label _secondaryVideoFrameLabel;

   @FXML
   private Label _secondaryVideoFrame;

   @FXML
   private Label _hyperlinkFilename;

   @FXML
   private TableView<Link> _linkTableView;

   @FXML
   private TableColumn<Link, String> _linkNameColumn;
   
   @FXML
   private TableColumn<Link, Integer> _startFrameColumn;
   
   @FXML
   private TableColumn<Link, Integer> _endFrameColumn;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "VideoTool.fxml";

   /** Frame Rate of Imported Videos */
   private static final int FPS = 30;

   /** The Start Frame of the Videos */
   private static final int MIN_FRAME = 1;

   /** Home Page Controller */
   private HomePageController _homePageController;

   /** Primary Video File */
   private File _primaryVideo;

   /** Secondary Video File */
   private File _secondaryVideo;

   /** Current HyperLink File */
   private File _currentHyperlinkFile;

   /** Primary Video Media Player */
   private MediaPlayer _primaryMediaPlayer;

   /** Secondary Video Media Player */
   private MediaPlayer _secondaryMediaPlayer;

   /** Dialog Window for Link Creation */
   private LinkCreationDialog _linkCreationDialog;

   /** Dialog Window for Importing Videos */
   private IDialog _importVideoDialog;

   /** Dialog Window for Saving a File */
   private IDialog _saveDialog;

   /** Polygon Math Utility Helper */
   private PolygonUtil _polygonUtil;

   /** Current Frame of Primary Video */
   private int _currentPrimaryFrame;

   /** Current Frame of Secondary Video */
   private int _currentSecondaryFrame;

   /** Observable List of Link Data */
   private final ObservableList<Link> _linkData;

   /** Map to Determine which Link to Highlight in Table */
   private final ObservableMap<Link, Boolean> _highlightLink;

   /** PseudoClass for Highlighting a Link in the Table */
   private final PseudoClass _highlightClass;

   /**
    * Constructor
    *
    * @param primaryStage - The primary stage of the application
    * @param loader - The FXML Loader Utility
    * @param homePageController - The Controller for the Home Page
    */
   public VideoToolController(final Stage primaryStage, final FXMLLoader loader, final HomePageController homePageController)
   {
      super(FXML_NAME, loader);

      // Initialize Controllers
      _homePageController = homePageController;

      // Initialize Video Views to Hidden
      _primaryVideoView.setVisible(false);
      _secondaryVideoView.setVisible(false);

      // Initialize Polygon Utility Helper();
      _polygonUtil = new PolygonUtil();

      // Initialize Dialogs
      _linkCreationDialog = new LinkCreationDialog(primaryStage, this, loader);
      _importVideoDialog = new ImportVideoDialog(primaryStage, homePageController, loader);
      _saveDialog = new SaveDialog(primaryStage, homePageController, loader);

      // Initialize Video Files to be null
      _primaryVideo = new File("");
      _secondaryVideo = new File("");

      // Initialize Current Hyperlink File to be null
      _currentHyperlinkFile = new File("");
      _hyperlinkFilename.setVisible(false);

      // Update Icons of Buttons
      _importFileButton.setText(EFontAwesome.FILE_CODE.getCode());
      _importVideoButton.setText(EFontAwesome.FILE_VIDEO.getCode());
      _newFileButton.setText(EFontAwesome.NEW_FILE.getCode());
      _createLinkButton.setText(EFontAwesome.LINK.getCode());
      _saveButton.setText(EFontAwesome.SAVE.getCode());
      _deleteLinkButton.setText(EFontAwesome.TRASH.getCode());

      // Create ToolTips for Buttons
      Tooltip importVideoTooltip = new Tooltip("Import Video Button");
      Tooltip saveToolTip = new Tooltip("Save Button");
      Tooltip createLinkToolTip = new Tooltip("Create Link Button");
      Tooltip deleteLinkToolTip = new Tooltip("Delete Link Button");
      Tooltip newFileTooltip = new Tooltip("New Hyperlink File Button");
      Tooltip importFileTooltip = new Tooltip("Import Hyperlink File Button");

      // Set ToolTips for Buttons
      _importFileButton.setTooltip(importFileTooltip);
      _importVideoButton.setTooltip(importVideoTooltip);
      _saveButton.setTooltip(saveToolTip);
      _newFileButton.setTooltip(newFileTooltip);
      _createLinkButton.setTooltip(createLinkToolTip);
      _deleteLinkButton.setTooltip(deleteLinkToolTip);

      // Initialize Button States
      _newFileButton.setDisable(true);
      _deleteLinkButton.setDisable(true);
      _createLinkButton.setDisable(true);

      // Initialize Slider States
      _primaryVideoSlider.setDisable(true);
      _secondaryVideoSlider.setDisable(true);
      _primaryVideoProgressBar.setDisable(true);
      _secondaryVideoProgressBar.setDisable(true);

      // Initialize Label States
      _primaryVideoFrameLabel.setVisible(false);
      _primaryVideoFrame.setVisible(false);
      _secondaryVideoFrameLabel.setVisible(false);
      _secondaryVideoFrame.setVisible(false);

      // Button Listeners
      handleImportFileButton();
      handleImportVideoButton();
      handleNewFileButton();
      handleCreateLinkButton();
      handleDeleteLinkButton();
      handleSaveButton();

      // Slider Listeners
      handlePrimarySlider();
      handleSecondarySlider();

      // Initialize List of Link Data */
      _linkData = FXCollections.observableArrayList();

      // Initialize Data of Link Table View
      _linkTableView.setItems(_linkData);
      _linkTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
      _linkTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

      // Set Cell Value Factory for the Link Table View
      _linkNameColumn.setCellValueFactory(new PropertyValueFactory<Link, String>("linkName"));
      _startFrameColumn.setCellValueFactory(new PropertyValueFactory<Link, Integer>("startFrame"));
      _endFrameColumn.setCellValueFactory(new PropertyValueFactory<Link, Integer>("endFrame"));

      // Link Selection Table View Listeners
      handleLinkSelectionTable();

      // Handle Hyperlink Selection
      handleHyperlinkSelection();

      // Initialize Observable Map that determines which Link to Highlight
      _highlightLink = FXCollections.observableHashMap();

      // Initialize PsuedoClass for Highlighting Links
      _highlightClass = PseudoClass.getPseudoClass("highlight");
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
      _primaryVideoView.setVisible(true);
      _currentHyperlinkFile = new File("");

      try 
      {
         // Initialize Media
         final Media primaryMedia = new Media(_primaryVideo.toURI().toURL().toString());

         // Initialize the Primary Media Player
         _primaryMediaPlayer = new MediaPlayer(primaryMedia);

         // Attach the Media Player to the Media View
         _primaryVideoView.setMediaPlayer(_primaryMediaPlayer);

         // Wait until Media Player Ready to update Slider
         _primaryMediaPlayer.setOnReady(new Runnable()
         {
            @Override
            public void run() 
            {
               // Update the Primary Slider
               updatePrimarySlider();

               // Update Current Primary Frame
               _currentPrimaryFrame = MIN_FRAME;

               // Null Check both Primary and Secondary Video Tool
               if(_primaryVideo.exists() && _secondaryVideo.exists())
               {
                  // Enable Linking Tool
                  _createLinkButton.setDisable(false);
               }

               // Update Displayed Links
               displayLinks(_currentPrimaryFrame);
            }
         });
      }
      catch (final Exception e)
      {
         // Log Error
         e.printStackTrace();
      }
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
      _secondaryVideoView.setVisible(true);

      try 
      {
         // Create a Media Object
         final Media secondaryMedia = new Media(_secondaryVideo.toURI().toURL().toString());

         // Initialize the Secondary Media Player
         _secondaryMediaPlayer = new MediaPlayer(secondaryMedia);

         // Attach the Media Player to the Media View
         _secondaryVideoView.setMediaPlayer(_secondaryMediaPlayer);

         // Wait until Media Player Ready to update Slider
         _secondaryMediaPlayer.setOnReady(new Runnable()
         {
            @Override
            public void run() 
            {
               // Update the Secondary Slider
               updateSecondarySlider();

               // Update Current Secondary Frame
               _currentSecondaryFrame = MIN_FRAME;

               // Null Check both Primary and Secondary Video Tool
               if(_primaryVideo.exists() && _secondaryVideo.exists())
               {
                  // Enable Linking Tool
                  _createLinkButton.setDisable(false);
               }
            }
         });
      }
      catch (final Exception e)
      {
         // Log Error
         e.printStackTrace();
      }
   }

   /**
    * createHyperlink - Creates a new Hyperlink
    *
    * @param link - The new Hyperlink object
    */
   public void createHyperlink(final Link link)
   {
      // Set To/From Video
      link.setFromVideo(_primaryVideo);
      link.setToVideo(_secondaryVideo);
      link.setToFrame(_currentSecondaryFrame);

      // Add Link to Observable List of Link Data
      _linkData.add(link);

      // Select the newly created link
      _linkTableView.getSelectionModel().select(link);

      // Display Links
      displayLinks(_currentPrimaryFrame);
   }

   /**
    * getHyperLinkFile - Gets the Current HyperLink File
    *                    being edited in the Video Hyperlink Tool
    * @return File
    */
   public File getHyperlinkFile()
   {
      return _currentHyperlinkFile;
   }

   /**
    * getCurrentPrimaryFrame - Gets the Current Frame of the Primary Video
    *
    * @return int
    */
   public int getCurrentPrimaryFrame()
   {
      return _currentPrimaryFrame;
   }

   /**
    * openHyperlinkFile - Opens up the Hyperlink File
    */
   public void openHyperlinkFile(File file, Map<Integer, ArrayList<Link>> frameToLinkMap)
   {
       // Check if current file matches Hyperlink File
       if(!_hyperlinkFilename.equals(file.getName()))
       {
          // Update Filename Label
          _hyperlinkFilename.setVisible(true);
          _hyperlinkFilename.setText(file.getName());

//          // Update Frame To Link Map
//          _frameToLinkMap = frameToLinkMap;
//
//          // Iterate over each Frame in Map
//          for(final Integer frame : _frameToLinkMap.keySet())
//          {
//             // Get the Link List
//             ArrayList<Link> linkList = _frameToLinkMap.get(frame);
//
//             // Iterate over the Link List
//             for(final Link link : linkList)
//             {
//                // Add Link to ListView
//                //_selectLinkView.getItems().add(link);
//             }
//          }

          // Update Links
          displayLinks(_currentPrimaryFrame);
       }
       else
       {
          // TODO: Handle Case where same file is open in Video Player
       }
   }

   /**
    * saveDataToFile - Saves the Hyperlinks/Video Information
    *                  to the Data File
    *
    * @param file - file to save data to
    */
   public void saveDataToFile(final File file)
   {
      // Write Data to Hyperlink File
      writeDataToFile(file);

      // Check if current file matches Hyperlink File
      if(!_hyperlinkFilename.equals(file.getName()))
      {
         // Update Filename Label
         _hyperlinkFilename.setVisible(true);
         _hyperlinkFilename.setText(file.getName());
      }

      // Enable New File Button
      _newFileButton.setDisable(false);
   }

   /**
    * displayLinks - Displays all the Links associated
    *                with the current Frame
    *
    * @param frameNum - The Currently Displayed Frame
    */
   private void displayLinks(final int frameNum)
   {
      // Clear existing Links
      _primaryVideoPane.getChildren().clear();

      // Re-Add the Primary Video View
      _primaryVideoPane.getChildren().add(_primaryVideoView);

      // Null Check Link List
      if(_linkData != null)
      {
         // Iterate over all Links associated with the frame
         for(Link link : _linkData)
         {
            // Ensure Link is Associated with the Video
            if(link.getFromVideo().equals(_primaryVideo))
            {
               // Update Current Display Frame in Primary Video View
               link.updateCurrentFrame(_currentPrimaryFrame);

               // Check if Link exists in Frame
               if(link.containsFrame(frameNum))
               {
                  // Get Bounding Box associated with the Link
                  Group boundingBox = link.getBoundingGroup(_currentPrimaryFrame);

                  // Null Check Bounding Box
                  if(boundingBox != null)
                  {
                     // Add the Link's Bounding Box to the Video Pane
                     _primaryVideoPane.getChildren().add(boundingBox);
                  }
               }
            }
         }
      }
   }

   /**
    * handleImportVideoButton - Handles the Selection of the
    *                           Import Video Button
    */
   private void handleImportVideoButton()
   {
      // Process Selection of Import Video Button
      _importVideoButton.setOnAction(event ->
      {
         // Show the Import Video Dialog
         _importVideoDialog.showDialog();
      });
   }

   /**
    * handleNewFileButton - Handles the Selection of the
    *                       New File Button
    */
   private void handleNewFileButton()
   {
      // Process Selection of the New File Button
      _newFileButton.setOnAction(event ->
      {
         // Show the Save File Dialog
         _saveDialog.showDialog();
      });
   }

   /**
    * handleImportFileButton - Handles the Selection of the
    *                          Import File Button
    */
   private void handleImportFileButton()
   {
      // Process Selection of the Import File Button
      _importFileButton.setOnAction(event ->
      {
         // Import Hyperlink File
         _homePageController.openHyperlinkFile();
      });
   }

   /**
    * handleLinkButtonSelection - Handles the Selection of the
    *                             Create Link Button
    */
   private void handleCreateLinkButton()
   {
      // Process Selection of Create Link Button
      _createLinkButton.setOnAction(event ->
      {
         // Show the Link Creation Dialog
         _linkCreationDialog.showDialog();
      });
   }

   /**
    * handleSaveButton - Handles the Selection
    *                    of the Save Button
    */
   private void handleSaveButton()
   {
      // Process the Selection of the Save Button
      _saveButton.setOnAction(event ->
      {
         // Save Hyperlink File as a New File
         _homePageController.saveHyperlinkFile(true);
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
         final Link selectedLink = _linkTableView.getSelectionModel().getSelectedItem();

         // Null Check Selected Link
         if(selectedLink != null)
         {
            // Get Start/End Frames for Link
            final int startFrame = selectedLink.getStartFrame();
            final int endFrame = selectedLink.getEndFrame();

            // Iterate over all the Frames the Link is in
            for(int i = startFrame; i <= endFrame; ++i)
            {
               // Remove Link from Primary Video Pane
               _primaryVideoPane.getChildren().remove(selectedLink.getBoundingGroup(i));
            }

            // Remove Link from Table
            _linkTableView.getItems().remove(selectedLink);

            // Select View
            _linkTableView.getSelectionModel().clearSelection();

            // Disable Delete Link Button
            _deleteLinkButton.setDisable(true);
         }
      });
   }

   /**
    * handleHyperlinkSelection
    */
   private void handleHyperlinkSelection()
   {
      // Mouse Press Listener
      _primaryVideoPane.setOnMousePressed(new EventHandler<MouseEvent>()
      {
         @Override public void handle(final MouseEvent mouseEvent)
         {
            // Get Mouse Event
            Point mousePoint = new Point(mouseEvent.getX(), mouseEvent.getY());

            // Iterate over the Current Links
            for(Link link : _linkData)
            {
               // Initialize Boolean Indicator
               boolean isInsidePolygon;

               // Get List of Vertices
               final List<Point> vertices = link.getVertices(_currentPrimaryFrame);
               
               // Ensure that there are Vertices
               if(!vertices.isEmpty())
               {
                  // Check if Mouse Press is inside Polygon
                  isInsidePolygon = _polygonUtil.isInsidePolygon(mousePoint, link.getVertices(_currentPrimaryFrame));
               }
               else
               {
                  // Default to not inside polygon
                  isInsidePolygon = false;
               }

               // TODO: Remove Debug Stmt
               // System.out.println("Inside Polygon " + link.getLinkName() + ": " + isInsidePolygon);
            }
         }
      });
   }

   /**
    * handleLinkSelectionTable - Handles the Listeners for the Select Link View
    */
   private void handleLinkSelectionTable()
   {
      // Handle Selection of a Row in the Link Selection Table
      _linkTableView.setRowFactory(rf ->
      {
         // Initialize Link Table Row
         TableRow<Link> link = new TableRow<Link>();

         // Create Highlight Binding
         ObjectBinding<Boolean> binding = Bindings.valueAt(_highlightLink, link.itemProperty());

         // Add Listener to binding Highlighting CSS to selected row
         binding.addListener((observable, oldValue, newValue) -> link.pseudoClassStateChanged(_highlightClass, newValue != null && newValue));

         return link;
      });

      // Selection Listener for Link Selection Table
      _linkTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Link>()
      {
         @Override
         public void changed(ObservableValue<? extends Link> arg0, Link unselectedLink, Link selectedLink)
         {
            // Null Check Selected Link
            if(selectedLink != null)
            {
               // Enable Delete Link Button
               _deleteLinkButton.setDisable(false);

               // Update Link Selected State
               selectedLink.setIsSelected(true);

               // Highlight the Link in the Selection Table
               _highlightLink.put(selectedLink, Boolean.TRUE);

               // Get From Video from Selected Link
               File fromVideo = selectedLink.getFromVideo();

               // If Link's From Video matches current Video
               if(fromVideo.equals(_primaryVideo))
               {
                  // Update Primary Video Slider to be at Link's Start Time
                  _primaryVideoSlider.setValue(selectedLink.getStartFrame());
               }
            }

            // Iterate over the Links
            for(Link link : _linkData)
            {
               // If link no longer selected
               if(!link.equals(_linkTableView.getSelectionModel().getSelectedItem()))
               {
                  // Update Link Selected State
                  link.setIsSelected(false);
               }
            }
         }
      });
   }

   /**
    * handlePrimarySlider - Handles the Listeners for the Primary Slider
    */
   private void handlePrimarySlider()
   {
      // Slider Change Listener
      _primaryVideoSlider.valueProperty().addListener(new ChangeListener<Number>()
      {
         public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
         {
            // Null Check Primary Video
            if(_primaryVideo != null)
            {
               // Update Frame Count Label
               _primaryVideoFrame.setText(String.valueOf(newVal.intValue()));

               // Update Current Primary Frame
               _currentPrimaryFrame = newVal.intValue();

               // Get the Current Frame Time (ms)
               final double frameTime = (newVal.doubleValue()/FPS) * 1000;

               // Update the Progress Bar
               final double maxVal = _primaryVideoSlider.getMax();
               _primaryVideoProgressBar.setProgress(newVal.doubleValue()/maxVal);

               // Update the Primary Video Media Player
               _primaryMediaPlayer.seek(new Duration(frameTime));

               // Display Links associated with Current Frame
               displayLinks(_currentPrimaryFrame);
            }
         }
      });
   }

   /**
    * handleSecondarySlider - Handles the Listeners for the Secondary Slider
    */
   private void handleSecondarySlider()
   {
      // Slider Change Listener
      _secondaryVideoSlider.valueProperty().addListener(new ChangeListener<Number>()
      {
         public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
         {
            // Null Check Primary Video
            if(_secondaryVideo != null)
            {
               // Update Frame Count Label
               _secondaryVideoFrame.setText(String.valueOf(newVal.intValue()));

               // Update Current Secondary Frame
               _currentPrimaryFrame = newVal.intValue();

               // Get the Current Frame Time (ms)
               final double frameTime = (newVal.doubleValue()/FPS) * 1000;

               // Update the Progress Bar
               final double maxVal = _secondaryVideoSlider.getMax();
               _secondaryVideoProgressBar.setProgress(newVal.doubleValue()/maxVal);

               // Update the Primary Video Media Player
               _secondaryMediaPlayer.seek(new Duration(frameTime));
            }
         }
      });
   }

   /**
    * updatePrimarySlider - Updates the Primary Slider based
    *                       on the Primary Video
    */
   private void updatePrimarySlider()
   {
      // Enable Primary Video Slider
      _primaryVideoSlider.setDisable(false);
      _primaryVideoProgressBar.setDisable(false);

      // Get Number of Frames
       Duration duration = _primaryMediaPlayer.getTotalDuration();
       int totalSeconds = (int) Math.round(duration.toSeconds());
       int totalFrames = totalSeconds * 30;

      // Set Slider Properties
      _primaryVideoSlider.setMin(MIN_FRAME);
      _primaryVideoSlider.setMax(totalFrames);
      _primaryVideoSlider.setValue(1);
      _primaryVideoSlider.setBlockIncrement(1.0);
      _primaryVideoSlider.setMajorTickUnit(1.0);
      _primaryVideoSlider.setSnapToTicks(true);

      // Show Primary Video Frame Labels
      _primaryVideoFrame.setVisible(true);
      _primaryVideoFrameLabel.setVisible(true);

      // Update Min/Max Frame in Link Creation Dialog
      _linkCreationDialog.updateMinFrame(MIN_FRAME);
      _linkCreationDialog.updateMaxFrame(totalFrames);
   }

   /**
    * updateSecondarySlider - Updates the Secondary Slider based
    *                         on the Secondary Video
    */
   private void updateSecondarySlider()
   {
      // Enable Primary Video Slider
      _secondaryVideoSlider.setDisable(false);
      _secondaryVideoProgressBar.setDisable(false);

      // Get Number of Frames
      Duration duration = _secondaryMediaPlayer.getTotalDuration();
      int totalSeconds = (int) Math.round(duration.toSeconds());
      int totalFrames = totalSeconds * FPS;

      // Set Slider Properties
      _secondaryVideoSlider.setMin(MIN_FRAME);
      _secondaryVideoSlider.setMax(9000);
      _secondaryVideoSlider.setValue(1);
      _secondaryVideoSlider.setBlockIncrement(1.0);
      _secondaryVideoSlider.setMajorTickUnit(1.0);
      _secondaryVideoSlider.setSnapToTicks(true);

      // Show Secondary Video Frame Labels
      _secondaryVideoFrame.setVisible(true);
      _secondaryVideoFrameLabel.setVisible(true);
   }

   /**
    * writeDataToFile
    * 
    * @param file - File to write data to
    */
   private void writeDataToFile(final File file)
   {
//      JSONObject frames = new JSONObject();
//      Iterator allLinks = _frameToLinkMap.entrySet().iterator();
//
//      // Iterate over all Links in Map
//      while (allLinks.hasNext())
//      {
//         Map.Entry frameInfo = (Map.Entry) allLinks.next();
//         int frameNum = (int) frameInfo.getKey();
//         ArrayList<Link> frameLinks = (ArrayList<Link>) frameInfo.getValue();
//         JSONArray links = new JSONArray();
//
//         // Iterate over each link
//         for (Link link : frameLinks)
//         {
//            // Create new Json Object for Link
//            JSONObject linkInfo = new JSONObject();
//
//            // Store Name of Link
//            linkInfo.put("name", link.getLinkName());
//
//            try 
//            {
//               // Store To/From Videos
//               linkInfo.put("fromVideo", link.getFromVideo().getAbsolutePath());
//               linkInfo.put("toVideo", link.getToVideo().getAbsolutePath());
//            }
//            catch (final Exception e)
//            {
//               // Log Error
//               e.printStackTrace();
//            }
//            linkInfo.put("toFrame", link.getToFrame());
//
//            // Create JsonArray to store Vertices
//            JSONArray points = new JSONArray();
//
//            // Get Vertex Array associated with Link
//            ArrayList<Point> pointArray = (ArrayList<Point>) link.getVertices();
//
//            // Iterate over all the Vertices
//            for (int i = 0; i < pointArray.size(); i++)
//            {
//               // Add the X/Y Vertex Location to the JsonArray
//               points.add(pointArray.get(i).getX());
//               points.add(pointArray.get(i).getY());
//            }
//
//            // 
//            linkInfo.put("points", points);
//            links.add(linkInfo);
//         }
//         frames.put(frameNum, links);
//      }
//
//      try 
//      {
//         // Create new File
//         file.createNewFile();
//
//         // Initialize File Writer
//         FileWriter f = new FileWriter(file);
//
//         // Create new Gson Object
//         Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//         // Parse Json Object
//         JsonParser jp = new JsonParser();
//         JsonElement je = jp.parse(frames.toJSONString());
//
//         // Convert to Formatted Json String
//         String prettyJsonString = gson.toJson(je);
//
//         // Write out Data to File
//         f.write(prettyJsonString);
//
//         // Log File Write Success
//         System.out.println("Success");
//
//         // Cleanup File Writer
//         f.flush();
//         f.close();
//      }
//      catch (IOException e)
//      {
//         // Log Error
//         e.printStackTrace();
//      }
   }
}