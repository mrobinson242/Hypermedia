package controllers;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Mp4ConversionController - Controls the User Interaction on the
 *                           Mp4 Conversion Page
 */
public class MP4ConverterController extends AbstractController
{
   @FXML
   private Button _selectFolderButton;

   @FXML
   private ProgressBar _fileConversionProgressBar;

   /** FXML filename associated with this Controller */
   private static final String FXML_NAME = "Mp4Converter.fxml";

   /** Stage of the Application Window */
   private final Stage _primaryStage;

   /** Select Folder Chooser */
   private DirectoryChooser _selectFolderChooser;

   /** Path to the Desktop */
   private File _desktopPath;

   /** Width/Height of Video */
   private static int VIDEO_WIDTH = 352;
   private static int VIDEO_HEIGHT = 288;

   /** Number of Frames */
   private static int FRAMES = 9000;

   /** Current Conversion Frame Number  */
   private AtomicReference<Double> _frameNum;

   /**
    * Constructor
    *
    * @param primaryStage - The primary stage of the application
    * @param loader - The FXML Loader Utility
    */
   public MP4ConverterController(final Stage primaryStage, final FXMLLoader loader)
   {
      super(FXML_NAME, loader);

      // Initialize the Stage of the Primary Window
      _primaryStage = primaryStage;

      // Handle Button Listeners
      handleSelectFolderButton();

      // Initialize the Desktop Path
      _desktopPath = new File(System.getProperty("user.home"), "Desktop/");

      // Initialize Select Folder Chooser
      _selectFolderChooser = new DirectoryChooser();
      _selectFolderChooser.setTitle("Select (.rgb) Folder");
      _selectFolderChooser.setInitialDirectory(_desktopPath);

      // Initialize the Frame Number
      _frameNum = new AtomicReference<Double>(0.0);
   }

   /**
    * handleSelectFolderButton - Handles The Button Selection
    *                            of the Select Folder Button
    */
   private void handleSelectFolderButton()
   {
      // Select Folder Button
      _selectFolderButton.setOnAction(event ->
      {
         // Display the Select Folder Chooser Dialog
         final File rgbFolder = _selectFolderChooser.showDialog(_primaryStage);

         // Null Check RGB Folder
         if(rgbFolder != null)
         {
            // Initialize new Runnable
            Runnable r = new Runnable()
            {
               @Override
               public void run() 
               {
                  convertFolder(rgbFolder);
               }
            };

            // Start Conversion in Separate Thread
            new Thread(r).start();
         }
      });
   }

   /**
    * convertFolder - Converts the Folder of ".rgb" images and a ".wav"
    *                 image into a usable ".mp4" file
    */
   private void convertFolder(final File rgbFolder)
   {
      // Create BufferedImage
      BufferedImage img;

      // Get Name of Folder
      final String folderName = rgbFolder.getName();

      // Get Folder Path
      StringBuilder path = new StringBuilder();
      path.append(rgbFolder.getAbsolutePath());
      String folderPath = path.toString();

      double imageCount = 1.0;
      String soundFile = folderPath + "/" + rgbFolder.getName() + ".wav";

      // Iterate over Image Size
      for (int i = 1; i <= 500; i++)
      {
         // Set the File Number
         _frameNum.set(imageCount);
         imageCount += 1.0;

         // Get RGB File Name String
         String fileName = folderPath + "/" + rgbFolder.getName() + String.format("%04d", i) + ".rgb";


         // Create RGB File Object
         File file = new File(fileName);

         // Create Byte Array for (.rgb) Image Data
         int length = (int) file.length();
         byte[] imageData = new byte[length];

         try 
         {
            DataInputStream dstream = new DataInputStream(new FileInputStream(file));

            dstream.readFully(imageData);
            dstream.close();
         }
         catch (FileNotFoundException e)
         {
            return;
         } 
         catch (IOException e)
         {
            e.printStackTrace();
         }

         int [][][] rgb_vals = new int[VIDEO_HEIGHT][VIDEO_WIDTH][3];

         img = new BufferedImage(VIDEO_WIDTH, VIDEO_HEIGHT, BufferedImage.TYPE_INT_RGB);

         // Iterate over Y Pixels
         for(int y = 0; y < VIDEO_HEIGHT; y++)
         {
            // Iterate over X Pixels
            for(int x = 0; x < VIDEO_WIDTH; x++)
            {
               // Read in Red Pixel Value
               byte r = imageData[y * VIDEO_WIDTH + x];
               rgb_vals[y][x][0] = r & 0xff;

               // Read in Green Pixel Value
               byte g = imageData[y * VIDEO_WIDTH + x + VIDEO_WIDTH*VIDEO_HEIGHT];
               rgb_vals[y][x][1] = g & 0xff;

               // Read in Blue Pixel Value
               byte b = imageData[y * VIDEO_WIDTH + x + 2*VIDEO_WIDTH*VIDEO_HEIGHT];
               rgb_vals[y][x][2] = b & 0xff;

               // Create Pixel Value
               int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

               // Set Pixel Value in Buffered Image
               img.setRGB(x,y,pix);
            }
         }

         // OffLoad to Display Thread
         Platform.runLater(() ->
         {
            // Update the Progress Bar
            _fileConversionProgressBar.setProgress(_frameNum.get()/FRAMES);
         });

         String s = file.getName();
         int position = s.lastIndexOf(".");
         s = s.substring(0, position);
         File outputfile = new File(rgbFolder + "/" + s + ".jpg");

         try 
         {
            ImageIO.write(img, "jpg", outputfile);
         } 
         catch (IOException e) 
         {
            e.printStackTrace();
         }
      }

      // ffmpeg string
      String ffmpeg = "C:\\Program Files\\ffmpeg\\bin\\ffmpeg";
      String frameRate = " -framerate 30";
      String folderPath2 = rgbFolder.getAbsolutePath() + "\\";
      File file = new File(folderPath2);
      String filePath = rgbFolder.getAbsolutePath() + "\\" + folderName + "%04d.jpg ";
      String audioPath = rgbFolder.getAbsolutePath() + "\\" + folderName + ".wav";
      String videoPath = rgbFolder.getAbsolutePath() + "\\" + folderName + ".mp4";


      // Initialize MPG4 Creation Commands
      //String cmd0 = "copy " + audioPath + " .";
      String cmd = ffmpeg + "-i " + folderName + "%04d.jpg " + folderName + ".mp4";
      //String cmd2 = ffmpeg + " -i out" + videoPath +" -i " + audioPath + " -vcodec copy " + videoPath;
      //String cmd = ffmpeg + " -framerate 30 -i " + folderName + "%04d.jpg " + folderName + ".mp4";
      //String cmd2 = "ffmpeg -i out" + folderName + ".mp4 -i " + folderName + ".wav -vcodec copy " + folderName + ".mp4";
      //String cmd3 = "mv " + folderName + ".mp4 " + _desktopPath.getAbsolutePath();
      //String[] cmd4 = {"/bin/sh", "-c", "rm -rf ./*.jpg"};
      //String cmd5 = "rm " + folderName + ".wav";
      //String cmd6 = "rm out" + folderName + ".mp4";

      List<String> params = Arrays.asList("C:\\Program Files\\ffmpeg\\bin\\ffmpeg", " -i ", folderName + "%04d.jpg ", folderName + ".mp4");
      //String cmd1 = ffmpeg + " -framerate 30 -i " + folderName + "%04d.jpg " + folderName + ".mp4";
      try 
      {
         //System.out.println("Step 0" );
         //Process p0 = Runtime.getRuntime().exec(cmd0);
         //p0.waitFor();

         //System.out.println("(1): " + cmd1);
         ProcessBuilder pb = new ProcessBuilder(cmd);
         pb.directory(rgbFolder);

         // TODO: Remove Output
         pb.redirectOutput(Redirect.INHERIT);
         pb.redirectError(Redirect.INHERIT);


         Process p = pb.start();
         p.waitFor();

         //System.out.println("(2): " + cmd2);
         //Process p2 = Runtime.getRuntime().exec(cmd2);
         //p2.waitFor();

         //System.out.println("Step 3" );
         //Process p3 = Runtime.getRuntime().exec(cmd3);
         //p3.waitFor();

         //System.out.println("Step 4" );
         //Process p4 = Runtime.getRuntime().exec(cmd4);
         //p4.waitFor();

         //System.out.println("Step 5" );
         //Process p5 = Runtime.getRuntime().exec(cmd5);
         //p5.waitFor();

         //System.out.println("Step 6" );
         //Process p6 = Runtime.getRuntime().exec(cmd6);
         //p6.waitFor();

         System.out.println("Done");
      } 
      catch (IOException e) 
      {
         e.printStackTrace();
      } 
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
   }
}