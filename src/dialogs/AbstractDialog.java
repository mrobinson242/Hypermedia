package dialogs;

import java.io.IOException;

import dialogs.interfaces.IDialog;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Abstract Dialog - Creates a Common Implementation 
 *                   of the Dialog Interface
 */
public class AbstractDialog extends Pane implements IDialog
{
   /** Stage of the Dialog Window */
   protected final Stage _dialogStage;

   /**
    * Constructor
    *
    * @param primaryStage - The primary stage of the application
    * @param fileName - Name of the FXML file to load in
    */
   public AbstractDialog(final Stage primaryStage, final String fileName)
   {
      // Load the FXML File
      loadFxml(fileName);

      // Initialize the Dialog
      _dialogStage = new Stage();
      _dialogStage.initModality(Modality.APPLICATION_MODAL);
      _dialogStage.initOwner(primaryStage);

      // Get Width/Height of Dialog
      final double width = getPrefWidth();
      final double height = getPrefHeight();

      // Create Scene for the Dialog
      Scene dialogScene = new Scene(this, width, height);

      // Add CSS
      dialogScene.getStylesheets().add("/css/common.css");

      // Attach Scene to Dialog Stage
      _dialogStage.setScene(dialogScene);
   }

   @Override
   public void hideDialog()
   {
      _dialogStage.hide();
   }

   @Override
   public void showDialog()
   {
      _dialogStage.show();
   }

   @Override
   public Parent getPane() 
   {
      return this;
   }

   /**
    * loadFxml
    *
    * @param name - The name of the FXML file to load in
    */
   private void loadFxml(final String name)
   {
      try
      {
         // Initialize String Builder
         StringBuilder builder = new StringBuilder();

         // Create Path to FXML File
         builder.append("../fxml/");
         builder.append(name);
         String fxmlPath = builder.toString();

         // Create Path to FXML File
         FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
         loader.setController(this);
         loader.setRoot(this);
         loader.load();
      } 
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}