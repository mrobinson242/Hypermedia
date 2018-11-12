
import java.io.InputStream;

import controllers.HomePageController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Main
 */
public class Main extends Application
{
   @Override
   public void start(final Stage stage) throws Exception
   {
      // Load Font Awesome
      InputStream font = Main.class.getResourceAsStream("/resources/fontawesome-webfont.ttf");
      Font fontAwesome = Font.loadFont(font, 10);

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
