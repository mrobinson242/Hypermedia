package controllers;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

   @FXML
   private Label _conversionLabel;

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

   /** Selected Folder for MP4 Conversion */
   private File _selectedFolder;

   /** Executor Service for Creating jpegs */
   ExecutorService _jpegService;

   /** Task for Making MP4 Video */
   Task _videoTask;

   /** Create Threads for making JPEGS */
   Runnable _t1, _t2, _t3, _t4, _t5, _t6, _t7, _t8;
   Runnable _t9, _t10, _t11, _t12, _t13, _t14, _t15, _t16, _t17, _t18;

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

      // Initialize Selected Folder
      _selectedFolder = new File("");

      // Hide conversion Label
      _conversionLabel.setVisible(false);

      // Initialize Threads
      initializeTheads();

      // Initialize Java Executor Service
      _jpegService = Executors.newCachedThreadPool();
   }

   /**
    * initializeThreads - Initializes the Threads
    *                     to create the JPEGS
    */
   private void initializeTheads()
   {
      // Initialize new Runnable
      _t1 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(1, 500);
         }
      };

      _t2 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(501, 1000);
         }
      };

      _t3 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(1001, 1500);
         }
      };

      _t4 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(1501, 2000);
         }
      };

      _t5 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(2001, 2500);
         }
      };

      _t6 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(2501, 3000);
         }
      };

      _t7 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(3001, 3500);
         }
      };

      _t8 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(3501, 4000);
         }
      };

      _t9 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(4001, 4500);
         }
      };

      _t10 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(4501, 5000);
         }
      };

      _t11 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(5001, 5500);
         }
      };

      _t12 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(5501, 6000);
         }
      };

      _t13 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(6001, 6500);
         }
      };

      _t14 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(6501, 7000);
         }
      };

      _t15 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(7001, 7500);
         }
      };

      _t16 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(7501, 8000);
         }
      };

      _t17 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(8001, 8500);
         }
      };

      _t18 = new Runnable()
      {
         @Override
         public void run() 
         {
            convertImages(8501, 9000);
         }
      };
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

         // Null Check Folder Selection
         if(rgbFolder != null)
         {
            // Update Selected Folder
            _selectedFolder = rgbFolder;

            // Update Conversion Label
            StringBuilder builder = new StringBuilder();
            builder.append("Converting ");
            builder.append(rgbFolder.getName());
            builder.append(" to MP4");
            _conversionLabel.setText(builder.toString());
            _conversionLabel.setVisible(true);

            // Restart Executor Service
            _jpegService = Executors.newCachedThreadPool();

            // Initialize the Generate Video Task
            Task t = new Task<Void>()
            {
               @Override
               protected Void call() throws Exception
               {
                  // Null Check RGB Folder
                  if(_selectedFolder != null)
                  {
                     // Kick off Image Creating Threads
                     _jpegService.execute(_t1); _jpegService.execute(_t2);
                     _jpegService.execute(_t3); _jpegService.execute(_t4);
                     _jpegService.execute(_t5); _jpegService.execute(_t6);
                     _jpegService.execute(_t7); _jpegService.execute(_t8);
                     _jpegService.execute(_t9); _jpegService.execute(_t10);
                     _jpegService.execute(_t11); _jpegService.execute(_t12);
                     _jpegService.execute(_t13); _jpegService.execute(_t14);
                     _jpegService.execute(_t15); _jpegService.execute(_t16);
                     _jpegService.execute(_t17); _jpegService.execute(_t18);

                     // Shutdown Executor
                     _jpegService.shutdown();

                     // Await Termination of Image Generating Threads
                     while(!_jpegService.isTerminated()){}

                     // Update Progress
                     updateProgress(7200, 9000);

                     // Generate the Video
                     generateVideo();

                     // Update Progress
                     updateProgress(9000, 9000);
                  }
                  return null;
               }
            };

            // Update the Progress Bar (up to 80%)
            _fileConversionProgressBar.progressProperty().bind(t.progressProperty());

            // Start Video Task
            Thread thread = new Thread(t);
            thread.start();
         }
      });
   }

   /**
    * convertImages
    */
   private void convertImages(final int startFrame, final int endFrame)
   {
      // Create BufferedImage
      BufferedImage img;

      // Get Name of Folder
      final File rgbFolder = _selectedFolder;

      // Get Folder Path
      StringBuilder path = new StringBuilder();
      path.append(rgbFolder.getAbsolutePath());
      String folderPath = path.toString();

      // Iterate over specified frames
      for (int i = startFrame; i <= endFrame; i++)
      {
         // Set the File Number
         double num = _frameNum.get() + 1.0;
         _frameNum.set(num);

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

         // Generate output JPEG File
         String s = file.getName();
         int position = s.lastIndexOf(".");
         s = s.substring(0, position);
         File outputfile = new File(rgbFolder + "/" + s + ".jpg");

         try 
         {
            // Write out JPEG File
            ImageIO.write(img, "jpg", outputfile);
         } 
         catch (IOException e) 
         {
            e.printStackTrace();
         }
      }
   }

   /**
    * generateVideo - Converts the Folder of ".jpg" images and a ".wav"
    *                 image into a usable ".mp4" file
    */
   private void generateVideo()
   {
      // Get Folder Name
      final File rgbFolder = _selectedFolder;
      final String folderName = _selectedFolder.getName();

      // Command Strings
      String ffmpeg = "C:\\Program Files\\ffmpeg\\bin\\ffmpeg";
      String filePath = rgbFolder.getAbsolutePath() + "\\" + folderName + "%04d.jpg ";
      String audioPath = rgbFolder.getAbsolutePath() + "\\" + folderName + ".wav";
      String tempVideoPath = rgbFolder.getAbsolutePath() + "\\" + "tempVideo.mp4";
      String videoPath = rgbFolder.getAbsolutePath() + "\\" + folderName + ".mp4";

      // Format Commands
      List<String> videoParams = Arrays.asList(ffmpeg, "-f", "image2", "-r", "30", "-i", filePath, tempVideoPath);
      List<String> audioParams = Arrays.asList(ffmpeg, "-i", tempVideoPath, "-i", audioPath, "-vcodec",  "copy", "-shortest", videoPath);

      try 
      {
         // Output the Video File (Images Only)
         ProcessBuilder pb = new ProcessBuilder(videoParams);
         pb.directory(rgbFolder);
         pb.redirectOutput(Redirect.INHERIT);
         pb.redirectError(Redirect.INHERIT);
         Process p = pb.start();
         p.waitFor();

         // Output the Video File with Audio (Audio Added)
         ProcessBuilder pb2 = new ProcessBuilder(audioParams);
         pb2.directory(rgbFolder);
         pb2.redirectOutput(Redirect.INHERIT);
         pb2.redirectError(Redirect.INHERIT);
         Process p2 = pb2.start();
         p2.waitFor();

         // Delete the JPEG Files
         deleteJpeg(rgbFolder);

         // OffLoad to Display Thread
         Platform.runLater(() ->
         {
            // Update Conversion Label
            StringBuilder builder = new StringBuilder();
            builder.append("Successful Conversion of ");
            builder.append(rgbFolder.getName());
            builder.append(" to MP4");
            _conversionLabel.setText(builder.toString());
         });
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

   /**
    * deleteJpeg - Deletes the JPEG Files
    *              in the Specified Folder
    */
   private void deleteJpeg(final File folder)
   {
      // Iterate over all the JPEG Files
      for(int i =1; i <= FRAMES; i++)
      {
         // Get JPEG Filename
         String filename = folder + "\\" + folder.getName() + String.format("%04d", i) + ".jpg";

         // Get JPEG File
         File file = new File(filename);

         // Check that file exists
         if(file.exists())
         {
            // Delete the File
            file.delete();
         }
      }
   }
}