
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
      Parent root = FXMLLoader.load(getClass().getResource("fxml/Hypermedia.fxml"));
      Scene scene = new Scene(root);
      stage.setTitle("Hypermedia Application");
      stage.setScene(scene);
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
