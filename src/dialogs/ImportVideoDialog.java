package dialogs;

import controllers.HomePageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * ImportVideoDialog - Dialog Window to Import Videos
 */
public class ImportVideoDialog extends AbstractDialog
{
   @FXML
   private Button _importPrimaryVideoButton;
   
   @FXML
   private Button _importSecondaryVideoButton;

   @FXML
   private Button _cancelButton;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "ImportVideoDialog.fxml";

   /** Home Page Controller */
   private HomePageController _homePageController;

   /**
    * Constructor
    *
    * @param primaryStage - The Primary Stage of the Application
    * @param homePageController - The Controller for the Home Page
    * @param loader - The FXML Loader Utility
    */
   public ImportVideoDialog(final Stage primaryStage, final HomePageController homePageController, final FXMLLoader loader)
   {
      super(primaryStage, FXML_NAME, loader);

      // Set Title of Dialog
      _dialogStage.setTitle("Import Video Dialog");

      // Initialize Controller
      _homePageController = homePageController;

      // Handle Button Listeners
      handleImportPrimaryVideoButton();
      handleImportSecondaryVideoButton();
      handleCancelButton();
   }

   /**
    * handleImportPrimaryVideoButton - Handles the Listener on the
    *                                  Import Primary Video Button
    */
   private void handleImportPrimaryVideoButton()
   {
      // Process Selection of the Import Primary Video Button
      _importPrimaryVideoButton.setOnAction(event ->
      {
         // Close the Dialog
         hideDialog();

         // Opens up a File Chooser to Select the Primary Video
         _homePageController.importPrimaryVideo();
      });
   }

   /**
    * handleImportSecondaryVideoButton - Handles the Listener on the
    *                                    Import Secondary Video Button
    */
   private void handleImportSecondaryVideoButton()
   {
      // Process Selection of the Import Secondary Video Button
      _importSecondaryVideoButton.setOnAction(event ->
      {
         // Close the Dialog
         hideDialog();

         // Opens up a File Chooser to Select the Secondary Video
         _homePageController.importSecondaryVideo();
      });
   }

   /**
    * handleCancelButton - Handles the Listener on the Cancel Button
    */
   private void handleCancelButton()
   {
      // Handle Selection of Close Button
      _cancelButton.setOnAction(event ->
      {
         // Close the Dialog
         hideDialog();
      });
   }
}