package dialogs;

import enums.EFontAwesome;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * ErrorDialog - The Error Dialog
 */
public class ErrorDialog extends AbstractDialog
{
   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "ErrorDialog.fxml";

   @FXML
   private Label _errorIcon;

   @FXML
   private Label _errorMessage;

   @FXML
   private Button _closeButton;

   /**
    * Constructor
    *
    * @param primaryStage - The Primary Stage of the Application
    * @param loader - The FXML Loader Utility
    * @param errorMessage - The Error Message
    */
   public ErrorDialog(final Stage primaryStage, final FXMLLoader loader)
   {
      super(primaryStage, FXML_NAME, loader);

      // Set Title of Dialog
      _dialogStage.setTitle("Error Message");

      // Update Error Icon
      _errorIcon.setText(EFontAwesome.ERROR_CIRCLE.getCode());

      // Handle Listeners
      handleCloseButton();
   }

   /**
    * setErrorMessage - Updates the Error Message
    *
    * @param errorMessage
    */
   public void setErrorMessage(final String errorMessage)
   {
      // Update Error Message
      _errorMessage.setText(errorMessage);
   }

   /**
    * handleCloseButton - Handles the Listener on the Close Button
    */
   private void handleCloseButton()
   {
      // Handle Selection of Cancel Button
      _closeButton.setOnAction(event ->
      {
         // Close the Dialog
         hideDialog();
      });
   }
}