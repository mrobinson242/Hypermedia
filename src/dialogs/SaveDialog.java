package dialogs;

import controllers.HomePageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * SaveDialog - Dialog Window to Save a Hyperlink File
 *              before Exiting or Starting a new File
 *
 */
public class SaveDialog extends AbstractDialog
{
   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "SaveDialog.fxml";

   @FXML
   private Button _saveButton;

   @FXML
   private Button _doNotSaveButton;

   @FXML
   private Button _cancelButton;

   /** Home Page Controller */
   private HomePageController _homePageController;

   /**
    * Constructor
    */
   public SaveDialog(final Stage primaryStage, final HomePageController homePageController)
   {
      super(primaryStage, FXML_NAME);

      // Initialize Controller
      _homePageController = homePageController;

      // Handle Button Listeners
      handleSaveButton();
      handleDoNotSaveButton();
      handleCancelButton();
   }

   /**
    * handleSaveButton - Handles the Listener on the Save Button
    */
   private void handleSaveButton()
   {
      // Handle Selection of the Save Button
      _saveButton.setOnAction(event ->
      {
         // TODO: Implement
      });
   }

   /**
    * handleDoNotSaveButton - Handles the Listener on the
    *                         Do Not Save Button
    */
   private void handleDoNotSaveButton()
   {
      // Handle Selection of the Do Not Save Button
      _saveButton.setOnAction(event ->
      {
         // TODO: Implement
      });
   }

   /**
    * handleCancelButton - Handles the Listener on the Cancel Button
    */
   private void handleCancelButton()
   {
      // Handle Selection of Cancel Button
      _cancelButton.setOnAction(event ->
      {
         // Close the Dialog
         hideDialog();
      });
   }
}