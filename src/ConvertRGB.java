import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.util.*;

import javax.imageio.ImageIO;


public class ConvertRGB {
	int width = 352;
	int height = 288;
	BufferedImage img;

	public void doIt(String[] args) throws Exception {
		for (int i = 1; i < 9001; i++) {
			String sa = args[0] + String.format("%04d", i) + ".rgb";
			System.out.println(sa);
			File file = new File(sa);
			
			int length = (int) file.length();
			byte[] imageData = new byte[length];
			DataInputStream dstream;
			try {
				dstream = new DataInputStream(new FileInputStream(file));
			}
			catch (FileNotFoundException e) {
				return;
			}
			try {
				dstream.readFully(imageData);
				dstream.close();
			}
			catch (IOException io) {
				return;
			}
			int [][][] rgb_vals = new int[height][width][3];

			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			int ind = 0;
			for(int y = 0; y < height; y++){

				for(int x = 0; x < width; x++){

					// byte a = (byte) 255;
					byte r = imageData[y * width + x];
					rgb_vals[y][x][0] = r & 0xff;
					byte g = imageData[y * width + x + width*height];
					rgb_vals[y][x][1] = g & 0xff;
					byte b = imageData[y * width + x + 2*width*height];
					rgb_vals[y][x][2] = b & 0xff;

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					img.setRGB(x,y,pix);
					ind++;
				}
			}
			String s = file.getName();
			int position = s.lastIndexOf(".");
			s = s.substring(0, position);
			File outputfile = new File(s + ".jpg");
			ImageIO.write(img, "jpg", outputfile);
			
		}
		String cmd = "ffmpeg -framerate 30 -i " + args[0] + "%04d.jpg out" + args[0] + ".mp4";
		Process p = Runtime.getRuntime().exec(cmd);
		p.waitFor();
		String cmd2 = "ffmpeg -i out" + args[0] + ".mp4 -i " + args[0] + ".wav -vcodec copy " + args[0] + ".mp4";
		Runtime.getRuntime().exec(cmd2);
	}

	public static void main(String[] args) throws Exception{
		ConvertRGB ren = new ConvertRGB();
		ren.doIt(args);

	}

}