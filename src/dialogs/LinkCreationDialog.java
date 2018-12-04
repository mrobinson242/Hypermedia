package dialogs;

import controllers.VideoToolController;
import data.Link;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.NumericTextField;

/**
 * LinkCreationDialog - Dialog Window to Create 
 *                      a New HyperLink
 */
public class LinkCreationDialog extends AbstractDialog
{
   @FXML
   private TextField _linkTextField;

   @FXML
   private TextField _startFrameTextField;
   private NumericTextField _numericStartFrameTextField;

   @FXML
   private TextField _endFrameTextField;
   private NumericTextField _numericEndFrameTextField;

   @FXML
   private Button _createLinkButton;

   @FXML
   private Button _closeButton;

   /** Default Min/Max VALUE for Start/End Frame Text Fields */
   private static int DEFAULT_MIN = 1;
   private static int DEFAULT_MAX = 1;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "LinkCreationDialog.fxml";

   /** Video Tool Controller */
   private VideoToolController _videoToolController;

   /**
    * Constructor
    *
    * @param primaryStage        - The Primary Stage of the Application
    * @param videoToolController - The Controller for the Video Tool Tab
    * @param loader              - The FXML Loader Utility
    */
   public LinkCreationDialog(final Stage primaryStage, final VideoToolController videoToolController, final FXMLLoader loader)
   {
      super(primaryStage, FXML_NAME, loader);

      // Set Title of Dialog
      _dialogStage.setTitle("Link Creator Dialog");

      // Initialize Controller
      _videoToolController = videoToolController;

      // Initially Disable Create Link Button
      _createLinkButton.setDisable(true);

      // Initialize Numeric TextFields
      _numericStartFrameTextField = new NumericTextField(_startFrameTextField, DEFAULT_MIN, DEFAULT_MAX);
      _numericEndFrameTextField = new NumericTextField(_endFrameTextField, DEFAULT_MIN, DEFAULT_MAX);

      // Handle Listeners
      handleCreateLinkButton();
      handleCancelButton();
      handleLinkTextField();
      handleStartFrameTextField();
      handleEndFrameTextField();
      handleDialogVisibility();
   }

   /**
    * updateMinFrame - Updates the Min Frame
    *
    * @param minFrame - The Minimum Frame
    */
   public void updateMinFrame(final int minFrame)
   {
      // Update the Text Fields
      _numericStartFrameTextField.setMinValue(minFrame);
      _numericEndFrameTextField.setMinValue(minFrame);
   }

   /**
    * updateMaxFrame - Updates the Max Frame
    *
    * @param maxFrame - The Maximum Frame
    */
   public void updateMaxFrame(final int maxFrame)
   {
      // Update the Text Fields
      _numericStartFrameTextField.setMaxValue(maxFrame);
      _numericEndFrameTextField.setMaxValue(maxFrame);
   }

   /**
    * handleCreateLinkButton - Handles the Listener on 
    *                          the Create Link Button
    */
   private void handleCreateLinkButton()
   {
      // Handle Selection of Create Link Button
      _createLinkButton.setOnAction(event ->
      {
         // Get Name from Text Field
         String linkName = _linkTextField.getText();

         // Verify that the Link is Unique
         if(_videoToolController.verifyUniqueLink(linkName))
         {
            // Get Start/End Frame from Text Field
            final int startFrame = _numericStartFrameTextField.getIntValue();
            final int endFrame = _numericEndFrameTextField.getIntValue();

            // Create a new HyperLink
            final Link link = new Link(linkName, startFrame, endFrame, _videoToolController.getCurrentPrimaryFrame());
            _videoToolController.createHyperlink(link);
         }
         else
         {
            // Display the Error Dialog
            _videoToolController.displayErrorDialog("A Link with this name already exists");
         }

         // Close the Dialog
         hideDialog();
      });
   }

   /**
    * handleCancelButton - Handles the Listener on the Cancel Button
    */
   private void handleCancelButton()
   {
      // Handle Selection of Close Button
      _closeButton.setOnAction(event ->
      {
         // Close the Dialog
         hideDialog();
      });
   }

   /**
    * handleDialogVisibility - Performs Actions based on Dialog Visibility
    */
   private void handleDialogVisibility()
   {
      // On Dialog Show Action
      _dialogStage.setOnShowing(event ->
      {
         // Get the Current Start Frame from the Video Tool Tab
         final int startFrame = _videoToolController.getCurrentPrimaryFrame();

         // Update Start/End Frame Text Field
         _startFrameTextField.setText(String.valueOf(startFrame));
         _endFrameTextField.setText(String.valueOf(startFrame));
      });

      // On Dialog Hide Action
      _dialogStage.setOnHidden(event->
      {
         // Clear Link Text Field
         _linkTextField.clear();
      });
   }

   /**
    * handleStartFrameTextField - Handles Listeners for Start Frame Text Field
    */
   private void handleStartFrameTextField()
   {
      // Text Listener on Start Frame Text Field
      _startFrameTextField.textProperty().addListener((observable, oldValue, newValue) ->
      {
         // Check if Link Text Field and End Frame Text Field also are not Empty
         if(!_linkTextField.getText().isEmpty() && !_endFrameTextField.getText().isEmpty())
         {
            // Enable/Disable Create Link Button 
            // based on if Text Field has input
            _createLinkButton.setDisable(newValue.isEmpty());
         }
         else
         {
            // Disable Create Link Button
            _createLinkButton.setDisable(true);
         }
      });

      // Focus Listener for Start Frame Text Field
      _startFrameTextField.focusedProperty().addListener((observable, oldValue, isFocused) ->
      {
         // Check if no longer focused
         if(!isFocused)
         {
            // Check if End Frame is less than Start Frame
             if(_numericStartFrameTextField.getIntValue() > _numericEndFrameTextField.getIntValue())
             {
                // Update End Frame Text Field
                _endFrameTextField.setText(_startFrameTextField.getText());
             }
         }
      });
   }

   /**
    * handleEndFrameTextField - Handles Listeners for End Frame Text Field
    */
   private void handleEndFrameTextField()
   {
      // Text Listener on End Frame Text Field
      _endFrameTextField.textProperty().addListener((observable, oldValue, newValue) ->
      {
         // Check if Link Text Field and Start Frame Text Field also are not Empty
         if(!_linkTextField.getText().isEmpty() && !_startFrameTextField.getText().isEmpty())
         {
            // Enable/Disable Create Link Button 
            // based on if Text Field has input
            _createLinkButton.setDisable(newValue.isEmpty());
         }
         else
         {
            // Disable Create Link Button 
            _createLinkButton.setDisable(true);
         }
      });

      // Focus Listener for Start Frame Text Field
      _endFrameTextField.focusedProperty().addListener((observable, oldValue, isFocused) ->
      {
         // Check if no longer focused
         if(!isFocused)
         {
            // Check if End Frame is less than Start Frame
             if(_numericEndFrameTextField.getIntValue() < _numericStartFrameTextField.getIntValue())
             {
                // Update End Frame Text Field
                _startFrameTextField.setText(_endFrameTextField.getText());
             }
         }
      });
   }

   /**
    * handleLinkTextField - Handles the Listener on the Link Text Field
    */
   private void handleLinkTextField()
   {
      // Text Listener on Link Text Field
      _linkTextField.textProperty().addListener((observable, oldValue, newValue) ->
      {
         if(!_startFrameTextField.getText().isEmpty() && !_endFrameTextField.getText().isEmpty())
         {
            // Enable/Disable Create Link Button 
            // based on if Text Field has input
            _createLinkButton.setDisable(newValue.isEmpty());
         }
         else
         {
            // Disable Create Link Button 
            _createLinkButton.setDisable(true);
         }
      });
   }
}