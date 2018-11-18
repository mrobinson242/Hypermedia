package dialogs;

import controllers.VideoToolController;
import data.Link;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * LinkCreationDialog - Dialog Window to Create 
 *                      a New HyperLink
 */
public class LinkCreationDialog extends AbstractDialog
{
   @FXML
   private TextField _linkTextField;

   @FXML
   private Button _createLinkButton;

   @FXML
   private Button _closeButton;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "LinkCreationDialog.fxml";

   /** Video Tool Controller */
   private VideoToolController _videoToolController;

   /**
    * Constructor
    * 
    * @param primaryStage - The Primary Stage of the Application
    */
   public LinkCreationDialog(final Stage primaryStage, final VideoToolController videoToolController)
   {
      super(primaryStage, FXML_NAME);

      // Set Title of Dialog
      _dialogStage.setTitle("Link Creator Dialog");

      // Initialize Controller
      _videoToolController = videoToolController;

      // Initially Disable Create Link Button
      _createLinkButton.setDisable(true);

      // Handle Listeners
      handleCreateLinkButton();
      handleCancelButton();
      handleLinkTextField();
      handleDialogVisibility();
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

         // Create a new HyperLink
         final Link link = new Link(linkName);
         _videoToolController.createHyperlink(link);

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
      // On Dialog Hide Action
      _dialogStage.setOnHidden(event->
      {
         // Clear Link Text Field
         _linkTextField.clear();
      });
   }

   /**
    * handleLinkTextField - Handles the Listener on the Link Text Field
    */
   private void handleLinkTextField()
   {
      _linkTextField.textProperty().addListener((observable, oldValue, newValue) ->
      {
         // Enable/Disable Create Link Button 
         // based on if Text Field has input
         _createLinkButton.setDisable(newValue.isEmpty());
      });
   }
}