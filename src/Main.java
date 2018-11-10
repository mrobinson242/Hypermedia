
import controllers.HomePageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main
 */
public class Main extends Application
{
   @Override
   public void start(final Stage stage) throws Exception
   {
      // Initialize Controllers
      HomePageController homePage = new HomePageController(stage);

      // Initialize the Scene
      Scene scene = new Scene(homePage.getPane());

      // Set Attributes of Application
      stage.setTitle("Hypermedia Application");
      stage.setScene(scene);

      // Add CSS
      scene.getStylesheets().add("/css/common.css");

      // Display Application
      stage.show();
   }

   /**
    * main
    *
    * @param args
    */
   public static void main(String[] args)
   {
      launch(args);
   }

}
