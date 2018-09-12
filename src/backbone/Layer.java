package backbone;

import java.awt.Image;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

/**
 * A class the holds all of the information about a "layer" or piece of the image.
 * This includes the image before filtering, the image after filtering, and how the image should be filtered.
 * This layer also has a specific position and size on the display.
 * Additionally, layers can be placed in a specific order.
 * @author Ninja
 *
 */
public class Layer implements Serializable{
	/**
	 * Generated Serial-ID
	 */
	private static final long serialVersionUID = 3954124190863354091L;
	/**
	 * String representation of layer.
	 * Defaultly the memory address, but when instantiated changes to filename
	 */
	public String myString = super.toString();
	/**
	 * Returns name of the layer
	 */
	public String toString(){
		return myString;
	}
	/**
	 * Null layer
	 * Reference for other classes
	 */
	public static final Layer nulle = new Layer(0, 0, 0, 0, new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB), "NULL LAYER");
	/**
 	* The size of the selection bars around the image when the layer is selected
 	*/
	public static final double len = .01;
	/**
	 * The amount of red being shown
	 * 1 indicates that all the red is being shown while 0 indicates no red is being shown
	 */
	public double red = 1;
	/**
	 * The amount of green being shown
	 * 1 indicates that all the green is being shown while 0 indicates no green is being shown
	 */
	public double green = 1;
	/**
	 * The amount of blue being shown
	 * 1 indicates that all the blue is being shown while 0 indicates no blue is being shown
	 */
	public double blue = 1;
	/**
	 * The brightness being shown
	 * 1 indicates that the image is at full brightness while 0 indicates no brightness
	 */
	public double overall = 1;
	/**
	 * The amount of contrast in the image
	 * 0 represents the amount of contrast being shown regularly, 1 represents no contrast, and a negative number indicates increased contrast over the initial image.
	 */
	public double contrast = 0;
	/**
	 * A boolean representing whether or not the image should be grayscaled.
	 */
	public boolean grayscale;
	/**
	 * A boolean representing whether or not the colors of the image should be inverted
	 */
	public boolean inverted;
	/**
	 * A boolean representing whether or not the alpha channel of the image should be inverted
	 */
	public boolean invertedalpha;
	/**
	 * A boolean array which determines for each thread updating the image of whether or not to run.
	 */
	boolean[] updates;
	/**
	 * A short matrix containing the red color values for the original image.
	 * The first argument represents width or "x" while the second represents height or "y".
	 */
	public short[][] redarray;
	/**
	 * A short matrix containing the green color values for the original image.
	 * The first argument represents width or "x" while the second represents height or "y".
	 */
	public short[][] greenarray;
	/**
	 * A short matrix containing the blue color values for the original image.
	 * The first argument represents width or "x" while the second represents height or "y".
	 */
	public short[][] bluearray;
	/**
	 * A short matrix containing the alpha color values for the both the original and filtered images.
	 * The first argument represents width or "x" while the second represents height or "y".
	 */
	short[][] alphaarray;
	/**
	 * A short matrix containing the red color values for the filtered image.
	 * The first argument represents width or "x" while the second represents height or "y".
	 */
	short[][]  filteredRed;
	/**
	 * A short matrix containing the green color values for the filtered image.
	 * The first argument represents width or "x" while the second represents height or "y".
	 */
	short[][]  filteredGreen;
	/**
	 * A short matrix containing the blue color values for the filtered image.
	 * The first argument represents width or "x" while the second represents height or "y".
	 */
	short[][] filteredBlue;
	/**
	 * The leftmost coordinate of the image.
	 * The bounds are from [0, 1]
	 */
	public double x;
	/**
	 * The topmost coordinate of the image.
	 * The bounds are from [0, 1]
	 */
	public double y;
	/**
	 * The width of the image.
	 * A size of 1 indicates that if centered, it would occupy the entire image.
	 */
	public double xsize;
	/**
	 * The height of the image.
	 * A size of 1 indicates that if centered, it would occupy the entire image.
	 */
	public double ysize;
	/**
	 * The filtered image
	 */
	private BufferedImage filtered;
	/**
	 * The filtered image
	 * @return a BufferedImage containing the image as corresponding to the current filter.
	 */
	public BufferedImage getFilteredImage(){
		return filtered;
	}
/**
	 * Initializes the layer and all of its components from a URL.
	 * The image is read into the component matrixes and the updater Threads are initialized.
	 * @param a starting "x" position on-screen
	 * @param b starting "y" position on-screen
	 * @param c starting width
	 * @param d starting height
	 * @param meh original image stored in a URL
 */
	public Layer(double a, double b,double c,double d, URL meh){
		this(a, b, c, d, FileOps.getImage(meh), meh.toString());
	}
/**
	 * Initializes the layer and all of its components from a String.
	 * The image is read into the component matrixes and the updater Threads are initialized.
	 * @param a starting "x" position on-screen
	 * @param b starting "y" position on-screen
	 * @param c starting width
	 * @param d starting height
	 * @param meh original image's location stored in a String
 */
	public Layer(double a, double b,double c,double d, String meh){
		this(a, b, c, d, FileOps.getImage(meh), meh);
	}
	/**
	 * Initializes the layer and all of its components from a BufferedImage.
	 * The image is read into the component matrixes and the updater Threads are initialized.
	 * @param a starting "x" position on-screen
	 * @param b starting "y" position on-screen
	 * @param c starting width
	 * @param d starting height
	 * @param meh original image
	 */
	public Layer(double a, double b,double c,double d, Image r, String name){
		//Store the original size, position and name
		x = a; y = b; xsize = c; ysize = d;myString = name;
		BufferedImage meh = null;
		if(! (r instanceof BufferedImage)){
			BufferedImage temp = new BufferedImage(r.getWidth(null), r.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			temp.getGraphics().drawImage(r, 0, 0, temp.getWidth(), temp.getHeight(), null);
		}
		else
			meh = (BufferedImage)r;
		if(meh.getWidth()>800){
			Image temp = meh.getScaledInstance(800, (int)(meh.getHeight()*800/meh.getWidth()),Image.SCALE_SMOOTH);
			meh = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		meh.getGraphics().drawImage(temp, 0, 0, meh.getWidth(), meh.getHeight(), null);
		}

		//Create the color matrixes
		redarray = new short[meh.getWidth()][meh.getHeight()];
		greenarray = new short[meh.getWidth()][meh.getHeight()];
		bluearray = new short[meh.getWidth()][meh.getHeight()];
		alphaarray = new short[meh.getWidth()][meh.getHeight()];
		filteredRed = new short[meh.getWidth()][meh.getHeight()];
		filteredGreen = new short[meh.getWidth()][meh.getHeight()];
		filteredBlue = new short[meh.getWidth()][meh.getHeight()];
		
		//Store the color values of the image into the color matrixes
		for(int i = 0; i<meh.getWidth(); i++)
			for(int j = 0; j<meh.getHeight(); j++){
				int pixel = meh.getRGB(i, j);
            	alphaarray[i][j] = (short)((pixel >> 24) & 0xff);
            	filteredRed[i][j] = redarray[i][j] = (short)((pixel >> 16) & 0xff);
            	filteredGreen[i][j] = greenarray[i][j] = (short)((pixel >> 8) & 0xff);
            	filteredBlue[i][j] = bluearray[i][j] = (short)((pixel) & 0xff);
			}
		
		//Create the filtered image
		filtered = new BufferedImage(meh.getWidth(), meh.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		//Create the update array
		updates = new boolean[meh.getHeight()];
		
		//Create the update Threads
		for(int h = 0; h<meh.getHeight(); h++)
			new Runner(h).start();
		
		//Update the image
		update();
}
	/**
	 * Sets the entire image's alpha value
	 * @param num the new alpha value
	 */
	public void applyAlpha(int num){

		for(int i = 0; i<redarray.length; i++)
			for(int j = 0; j<redarray[0].length; j++)
					alphaarray[i][j] = (short) num;
		update();
	}
	/**
	 * Sets the alpha value of one specific color
	 * @param r Red value of the color
	 * @param g Green value of the color
	 * @param b Blue value of the color
	 * @param num Alpha value to be applied
	 */
	public void applyAlphaMask(int r, int g, int b, int num){

		for(int i = 0; i<redarray.length; i++)
			for(int j = 0; j<redarray[0].length; j++){
				if(filteredRed[i][j] == r && filteredGreen[i][j] == g && filteredBlue[i][j] == b)
					alphaarray[i][j] = (short) num;
			}
		update();
	}
	/**
	 * Calls all of the update threads to update by setting each value in the update array to true
	 */
	public void update(){
		for(int i = 0; i<updates.length; i++)
			updates[i] = true;
	}
	/**
	 * Sets the RGB at a specific point on the actual image
	 * @param w The "x" position of the pixel on the image
	 * @param height The "y" position of the pixel on the image
	 * @param pixel The color of the new pixel
	 */
	public void setRGB(int w, int height,  int pixel){
		setRGB(w, height, (pixel >> 16) & 0xff, ((pixel >> 8) & 0xff), ((pixel) & 0xff));
	}
	/**
	 * Sets the RGB at a specific point on the actual image
	 * @param w The "x" position of the pixel on the image
	 * @param height The "y" position of the pixel on the image
	 * @param r The red portion of the color
	 * @param g The green portion of the color
	 * @param b The blue portion of the color
	 */
	public void setRGB(int w, int height,  int r, int g, int b){
redarray[w][height] = (short)r;
		redarray[w][height] = (short)g;
		redarray[w][height] = (short)(b);
	}
	/**
	 * Gets the RGB at a specific point on the actual image
	 * @param w The "x" position of the pixel on the image
	 * @param height The "y" position of the pixel on the image
	 * @return
	 */
	public int getRGB(int w, int height){
		int rgb = alphaarray[w][height];
		rgb = (rgb << 8) + redarray[w][height];
		rgb = (rgb << 8) + greenarray[w][height];
		rgb = (rgb << 8) + bluearray[w][height];
		return rgb;
	}
	/**
	 * The class which updates the image with its effects into the filtered image.
	 * Each instance updates one line (only updates one height).
	 * By working in parallel, they update the entire image.
	 * @author Ninja
	 *
	 */
	class Runner extends Thread{
		/**
		 * The line (height) which this thread updates.
		 */
		int height;
		/**
	 	* Initializes the Runner
	 	* @param h The line to update
	 	*/
		public Runner(int h){
			height = h;
		}
		@Override
		public void run(){
			new Timer(50, new ActionListener(){
				public void actionPerformed(ActionEvent e){
					if(updates[height]){
						for(int w = 0; w<redarray.length; w++){
							//Apply both individual color and brightness effects
							filteredRed[w][height] = (short)(redarray[w][height]*red*overall);
							filteredGreen[w][height] = (short)(greenarray[w][height]*green*overall);
							filteredBlue[w][height] = (short)(bluearray[w][height]*blue*overall);
							
							//Apply contrast effect
							filteredRed[w][height]+=(128-filteredRed[w][height])*contrast;
							filteredGreen[w][height]+=(128-filteredGreen[w][height])*contrast;
							filteredBlue[w][height]+=(128-filteredBlue[w][height])*contrast;
					
							//Ensure to avoid out of bounds exceptions that may have been created by the effects
							if(filteredRed[w][height]<0)
								filteredRed[w][height] = 0;
							else if (filteredRed[w][height]>255)
								filteredRed[w][height] = 255;
							if(filteredGreen[w][height]<0)
								filteredGreen[w][height] = 0;
							else if (filteredGreen[w][height]>255)
								filteredGreen[w][height] = 255;
							if(filteredBlue[w][height]<0)
								filteredBlue[w][height] = 0;
							else if (filteredBlue[w][height]>255)
								filteredBlue[w][height] = 255;
					
							//Apply the inversion effect
							if(inverted){
								filteredRed[w][height] = (short)(255-filteredRed[w][height]);
								filteredGreen[w][height] = (short)(255-filteredGreen[w][height]);
								filteredBlue[w][height] = (short)(255-filteredBlue[w][height]);
							}
					
							//Apply the grayscale effect
							//Write the color to the image
							int rgb;
							if(!invertedalpha)
								rgb = alphaarray[w][height];
							else 
								rgb = 255-alphaarray[w][height];
							if(!grayscale){
								rgb = (rgb << 8) + filteredRed[w][height];
								rgb = (rgb << 8) + filteredGreen[w][height];
								rgb = (rgb << 8) + filteredBlue[w][height];
								filtered.setRGB(w, height, rgb);
							}
							else{
								int i = (int)(0.3*filteredRed[w][height]+0.59*filteredGreen[w][height]+0.11*filteredBlue[w][height]);
								rgb = (rgb << 8) + i;
								rgb = (rgb << 8) + i;
								rgb = (rgb << 8) + i;
								filtered.setRGB(w, height, rgb);
							}
							updates[height] = false;
							}
						}}
					}).start();
	}
}
	/**
	 * Checks to see if this shape exists at a specific coordinate
	 * @param a The "x" piece of the coordinate
	 * @param b The "y" piece of the coordinate
	 * @return
	 */
	public boolean at(double a, double b){
		if(xsize>0){
			if(!(a>=x && a<x+xsize))
				return false;
			if(ysize>0)
				return b>=y  && b<y+ysize;
			else
				return b<=y  && b>y+ysize;
		}
		else{
			if(!(a<=x && a>x+xsize))
				return false;
			if(ysize>0)
				return b>=y  && b<y+ysize;
			else
				return b<=y  && b>y+ysize;
		}
	}
	/**
	 * Determines the type of transformation that will be applied when started at a coordinate (move left, scale, etc)
	 * @param a The "x" piece of the coordinate
	 * @param b The "y" piece of the coordinate
	 * @return
	 */
	public int moveType(double a, double b){
		a-=x;
		b-=y;
		if(xsize<0)
			a-=xsize;
		if(ysize<0)
			b-=ysize;
		if(a<0 || b<0 || a>Math.abs(xsize) || b>Math.abs(ysize))
			return -1;
		if(a<len){
			if(b<len)
				return 1;
			if(b>Math.abs(ysize)-len)
				return 2;
			return 3;
		}
		if(a>Math.abs(xsize)-len){
			if(b<len)
				return 4;
			if(b>Math.abs(ysize)-len)
				return 5;
			return 6;
		}
		if(b<len)
			return 7;
		if(b>Math.abs(ysize)-len)
			return 8;
		return 0;
}
	/**
	 * Returns a copy of this Layer
	 */
	@Override
public Layer clone(){
		return new Layer(x+.01, y+.01, xsize, ysize, getFilteredImage(), myString+" (copy)");
}
	/**
	 * Returns the color at a point on the image
	 * @param a The x position on-screen
	 * @param b The y position on-screen
	 * @return The integer RGB representation of the color
	 */
	public int colorAt(double a, double b){
		a-=x;
		b-=y;
		if(xsize<0)
			a-=xsize;
		if(ysize<0)
			b-=ysize;
		a/=Math.abs(xsize);
		b/=Math.abs(ysize);
		if(xsize>0){
			if(ysize>0)
				return filtered.getRGB((int)(a*filtered.getWidth()), (int)(b*filtered.getHeight()));
			else
				return filtered.getRGB((int)(a*filtered.getWidth()), (int)((1-b)*filtered.getHeight()));
		}
		else{
			if(ysize>0)
				return filtered.getRGB((int)((1-a)*filtered.getWidth()), (int)(b*filtered.getHeight()));
			else
				return filtered.getRGB((int)((1-a)*filtered.getWidth()), (int)((1-b)*filtered.getHeight()));
		}
	}
/**
 * Scales the layer to the screen size
 */
	public void scale(){
		x = y = 0;
		xsize = ysize = 1;
	}
	/**
	 * Scales the layer to the screen size, maintaining aspect ratio
	 */
		public void scaleAspect(){
			if(filtered.getHeight()<=filtered.getWidth()){
			x = 0;
			xsize = 1;
			ysize = (double)filtered.getHeight()/filtered.getWidth();
			y = (1-ysize)/2;
			}
			else{
				y = 0;
				ysize = 1;
				xsize = (double)filtered.getWidth()/filtered.getHeight();
				x = (1-xsize)/2;
			}
		}
}
