package controllers;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * AbstractController - Common Implementation of
 *                      the Controller Class
 */
public class AbstractController extends Pane
{
   /** Name of FXML File */
   private final String _fileName;

   /**
    * Constructor
    *
    * @param fileName - Name of the FXML file to load in
    */
   public AbstractController(final String fileName)
   {
      // Set Parameters
      _fileName = fileName;

      // Load the FXML
      loadFxml();
   }

   /**
    * getPane - Gets the Root Pane of the FXML File
    *
    * @return Parent
    */
   public Parent getPane()
   {
      return this;
   }

   /**
    * loadFxml
    *
    * @param name - The name of the FXML file to load in
    */
   protected void loadFxml()
   {
      try
      {
         // Initialize String Builder
         StringBuilder builder = new StringBuilder();

         // Create Path to FXML File
         builder.append("../fxml/");
         builder.append(_fileName);
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