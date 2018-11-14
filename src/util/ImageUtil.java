package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil
{
    /**
     * convertRGBImage - Converts a RGB image into a BufferedImage
     *
     * @param filePath
     * @return BufferedImage
     */
    public static BufferedImage convertRGBImage(final String filePath, final int width, final int height)
    {
        // Initialize Raw Image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Initialize File
        File file = new File(filePath);

        // Initialize Pixel Buffer (RGB)
        int[] buffer = new int[width * height * 3];

        try
        {
            // Initialize 
            int reader = 0;
            int count = 0;

            // Create File Reader
            final InputStream inputStream = new FileInputStream(file);

            // Read until End of File
            while((reader = inputStream.read()) != -1)
            {
                buffer[count] = reader;
                count++;
            }

            // Reset Counter to 0;
            count = 0;

            // Iterate over Y Values
            for(int y = 0; y < height; y++)
            {
                // Iterate over X Values
                for(int x = 0; x < width; x++)
                {
                    // Get Pixel Value from Buffer
                    int redVal = buffer[count];
                    int greenVal = buffer[count+(height*width)];
                    int blueVal = buffer[count+(2*height*width)];

                    // Get the Grayscale Byte Value
                    byte r = (byte) redVal;
                    byte g = (byte) greenVal;
                    byte b = (byte) blueVal;

                    // Convert to Pixel Value
                    final int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);

                    // Set Pixel in Buffered Image
                    image.setRGB(x, y, pix);

                    // Increment Count
                    count++;
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return image;
    }
}