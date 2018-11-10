package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * AbstractController - Common Implementation of
 *                      the Controller Class
 */
public class AbstractController
{
   /** Name of FXML File */
   private final String _fileName;
   
   /** Parent Pane */
   private Parent _parent;

   /**
    * Constructor
    *
    * @param fileName - Name of the FXML file to load in
    */
   public AbstractController(final String fileName, final Parent parent)
   {
      // Set Parameters
      _fileName = fileName;
      _parent = parent;

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
      return _parent;
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
         _parent = FXMLLoader.load(getClass().getResource(fxmlPath));

      } 
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
}