package gui;

import backbone.Layer;
import backbone.Transfers;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/**
 * Main class to display all layers with effects on-screen
 * @author Ninja
 */
public class Render extends JPanel implements ClipboardOwner {
	/**
	 * Default Serial ID
	 */
	private static final long serialVersionUID = 3267100904423267882L;
	/**
	 * Main Render for use by external classes
	 */
    public static Render main;
    /**
     * Width of main image
     */
    public static int wid = 500;
    /**
     * Height of main image
     */
    public static int hei = 500;
    /**
     * Main image to store all layers
     */
    BufferedImage image = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
    /**
     * Graphics for drawing the layers quickly onto the image
     */
    Graphics2D g = (Graphics2D)image.getGraphics();
    /**
     * LinkedList containing all layers
     * Layer at front-most position is on top and back-most position is on bottom
     */
    public LinkedList<Layer> layer = new LinkedList<Layer>();
    /**
     * The variable that determines whether it should update itself or not
     */
    boolean refresh = true;
    /**
     * Initialize the Render
     * Assign all listeners
     * Start all updating Threads
     */
	public Render(){
		initialize();
		
		// Add any default layers
		layer.addFirst(Layer.nulle);
	}
	/**
     * Assign all listeners
     * Start all updating Threads
     */
	private void initialize(){
		setTransferHandler(new Transfers());
		
		//Set all graphics settings
	     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	     
	     //Add all listeners
		 addKeyListener(new Keys());
		 addMouseListener(new Mouse());
		 addMouseMotionListener(new Mouse());
		 setFocusable(true);
		 requestFocusInWindow();
		 
		 //Set as main Render
	     main = this;
	     
	     //Create update thread
	          new Thread(){
            @Override
	             public void run(){
	                new Timer(50, 
	                      new ActionListener(){
	                         public void actionPerformed(ActionEvent e){
	                            if(refresh){
	                            	if(ToolPanel.main != null)
	                            	ToolPanel.main.tools.repaint();
	                            	//Draw all layers in the correct order
	                               g.setColor(Color.BLACK);
	                               g.fillRect(0, 0, wid, hei);
	                               for(Iterator<Layer> it = layer.descendingIterator(); it.hasNext();){
	                                  Layer temp = it.next();
	                                  g.drawImage(temp.getFilteredImage(), (int)(wid*temp.x), (int)(hei*temp.y), (int)(wid*temp.xsize), (int)(hei*temp.ysize), null);
	                               }
	                               
	                               //Draw the toolbars if necessary
	                               if(Mouse.selected !=Layer.nulle){
	                            	   if(Mouse.selected.xsize>0)
	                            	   {
	                            		   if(Mouse.selected.ysize>0){
	                                  g.setColor(Color.BLUE);
	                                  g.fillRect((int)(Mouse.selected.x*wid), (int)(hei*Mouse.selected.y), (int)(wid*Layer.len), (int)(hei*Mouse.selected.ysize));
	                                  g.fillRect((int)(Mouse.selected.x*wid), (int)(hei*Mouse.selected.y), (int)(wid*Mouse.selected.xsize), (int)(hei*Layer.len));
	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x-Layer.len)*wid), (int)(hei*Mouse.selected.y), (int)(wid*Layer.len), (int)(hei*Mouse.selected.ysize));
	                                  g.fillRect((int)(Mouse.selected.x*wid), (int)((Mouse.selected.ysize+Mouse.selected.y-Layer.len)*hei), (int)(wid*Mouse.selected.xsize), (int)(hei*Layer.len));
	                                  g.setColor(Color.YELLOW);
	                                  g.fillRect((int)(Mouse.selected.x*wid), (int)(hei*Mouse.selected.y), (int)(wid*Layer.len), (int)(hei*Layer.len));
	                                  g.fillRect((int)(Mouse.selected.x*wid), (int)((Mouse.selected.ysize+Mouse.selected.y-Layer.len)*hei), (int)(wid*Layer.len), (int)(hei*Layer.len));
	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x-Layer.len)*wid), (int)(hei*Mouse.selected.y), (int)(wid*Layer.len), (int)(hei*Layer.len));
	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x-Layer.len)*wid), (int)((Mouse.selected.ysize+Mouse.selected.y-Layer.len)*hei), (int)(wid*Layer.len), (int)(hei*Layer.len));
	                               }
	                            	   else{
	                            		   g.setColor(Color.BLUE);
	                            		   g.fillRect((int)(Mouse.selected.x*wid), (int)(hei*(Mouse.selected.y-Layer.len)), (int)(wid*Mouse.selected.xsize), (int)(hei*Layer.len));
		 	                                  g.fillRect((int)(Mouse.selected.x*wid), (int)((Mouse.selected.ysize+Mouse.selected.y)*hei), (int)(wid*Mouse.selected.xsize), (int)(hei*Layer.len));           
	 	                                  g.fillRect((int)(Mouse.selected.x*wid), (int)(hei*(Mouse.selected.y+Mouse.selected.ysize)), (int)(wid*Layer.len), (int)(-hei*Mouse.selected.ysize));
	 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x-Layer.len)*wid), (int)(hei*(Mouse.selected.y+Mouse.selected.ysize)), (int)(wid*Layer.len), (int)(-hei*Mouse.selected.ysize));
	 	                                  g.setColor(Color.YELLOW);
	 	                                  g.fillRect((int)(Mouse.selected.x*wid), (int)(hei*(Mouse.selected.y-Layer.len)), (int)(wid*Layer.len), (int)(hei*Layer.len));
	 	                                  g.fillRect((int)(Mouse.selected.x*wid), (int)((Mouse.selected.ysize+Mouse.selected.y)*hei), (int)(wid*Layer.len), (int)(hei*Layer.len));
	 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x-Layer.len)*wid), (int)(hei*(Mouse.selected.y-Layer.len)), (int)(wid*Layer.len), (int)(hei*Layer.len));
	 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x-Layer.len)*wid), (int)((Mouse.selected.ysize+Mouse.selected.y)*hei), (int)(wid*Layer.len), (int)(hei*Layer.len));
	                            	   }
	                               }
	                            	   else{
	                            		   if(Mouse.selected.ysize>0){

		                            		   g.setColor(Color.BLUE);
		                            		   g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)(hei*(Mouse.selected.y)), (int)(-wid*Mouse.selected.xsize), (int)(hei*Layer.len));
			 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)((Mouse.selected.ysize+Mouse.selected.y-Layer.len)*hei), (int)(-wid*Mouse.selected.xsize), (int)(hei*Layer.len));           
		 	                                  g.fillRect((int)((Mouse.selected.x-Layer.len)*wid), (int)(hei*(Mouse.selected.y)), (int)(wid*Layer.len), (int)(hei*Mouse.selected.ysize));
		 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)(hei*(Mouse.selected.y)), (int)(wid*Layer.len), (int)(hei*Mouse.selected.ysize));
		 	                                  g.setColor(Color.YELLOW);
		 	                                  g.fillRect((int)((Mouse.selected.x-Layer.len)*wid), (int)(hei*(Mouse.selected.y)), (int)(wid*Layer.len), (int)(hei*Layer.len));
		 	                                  g.fillRect((int)((Mouse.selected.x-Layer.len)*wid), (int)((Mouse.selected.ysize+Mouse.selected.y-Layer.len)*hei), (int)(wid*Layer.len), (int)(hei*Layer.len));
		 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)(hei*(Mouse.selected.y)), (int)(wid*Layer.len), (int)(hei*Layer.len));
		 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)((Mouse.selected.ysize+Mouse.selected.y-Layer.len)*hei), (int)(wid*Layer.len), (int)(hei*Layer.len));
		                            	     
	                            		   }
	                            		   else{
		                            		   g.setColor(Color.BLUE);
		                            		   g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)(hei*(Mouse.selected.y-Layer.len)), (int)(-wid*Mouse.selected.xsize), (int)(hei*Layer.len));
			 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)((Mouse.selected.ysize+Mouse.selected.y)*hei), (int)(-wid*Mouse.selected.xsize), (int)(hei*Layer.len));           
		 	                                  g.fillRect((int)((Mouse.selected.x-Layer.len)*wid), (int)(hei*(Mouse.selected.y+Mouse.selected.ysize)), (int)(wid*Layer.len), (int)(-hei*Mouse.selected.ysize));
		 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)(hei*(Mouse.selected.y+Mouse.selected.ysize)), (int)(wid*Layer.len), (int)(-hei*Mouse.selected.ysize));
		 	                                  g.setColor(Color.YELLOW);
		 	                                  g.fillRect((int)((Mouse.selected.x-Layer.len)*wid), (int)(hei*(Mouse.selected.y-Layer.len)), (int)(wid*Layer.len), (int)(hei*Layer.len));
		 	                                  g.fillRect((int)((Mouse.selected.x-Layer.len)*wid), (int)((Mouse.selected.ysize+Mouse.selected.y)*hei), (int)(wid*Layer.len), (int)(hei*Layer.len));
		 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)(hei*(Mouse.selected.y-Layer.len)), (int)(wid*Layer.len), (int)(hei*Layer.len));
		 	                                  g.fillRect((int)((Mouse.selected.xsize+Mouse.selected.x)*wid), (int)((Mouse.selected.ysize+Mouse.selected.y)*hei), (int)(wid*Layer.len), (int)(hei*Layer.len));
		                            	     
	                            		   }
	                            	   }
	                            }
	                               repaint();
	                             //  refresh = false;
	                            }
	                         }
	                      }).start();
	             }
	          }.start();
	 }
	/**
	 * Default way to add an image
	 */
	public void add(Image im, String s){
		double hei = im.getHeight(null)*.5/im.getWidth(null);
		layer.addFirst(new Layer(.25, (1-hei)/2, .5, hei, im, s));
		LayerPanel.main.updateList();
	}
	/**
	 * Paint the JPanel with the current image
	 */
	@Override
	public void paintComponent(Graphics g){
		g.drawImage(getImage(), 0, 0, getWidth(), getHeight(), null);
	}
	/**
	 * Update the Render
	 */
    public void update(){
  	  refresh = true;
    }
	/**
	 * Returns the BufferedImage representation of the currently-being editted image
	 * @return BufferedImage view of all layers
	 */
    public BufferedImage getImage(){
       return image;
    }
    /**
     * Change the size of the current image
     * @param x New width of the image
     * @param y New Height of the image
     */
    public void change(int x, int y){
       image = new BufferedImage(wid = x, hei = y, BufferedImage.TYPE_INT_ARGB);
       g = (Graphics2D)image.getGraphics();
       g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       refresh = true;
    }
    /**
     * Gives the top layer at a given point
     * Returns Layer.nulle if none
     * @param x The x coordinate being monitored [0, 1]
     * @param y The y coordinate being monitored [0, 1]
     * @return The top layer at that coordinate
     */
    public Layer getLayer(double x, double y){
       for(ListIterator<Layer> it = layer.listIterator(); it.hasNext();){
          Layer temp = it.next();
          if(temp.at(x, y)){
             return temp;
          }
       }
       return Layer.nulle;
    }
    /**
     * Creates a version of what's currently on-screen, but at different dimensions
     * @param wid The exported width
     * @param hei The exported height
     * @return A BufferedImage of the currently displayed layers
     */
    public BufferedImage preRender(final int wid, final int hei){
       setCursor(new Cursor(Cursor.WAIT_CURSOR));
       final BufferedImage img = new BufferedImage(wid, hei, BufferedImage.TYPE_INT_ARGB);
       Graphics2D g = (Graphics2D)img.getGraphics();
       g.fillRect(0, 0, wid, hei);
       for(Iterator<Layer> it = layer.descendingIterator(); it.hasNext();){
          Layer templ = it.next();
          g.drawImage(templ.getFilteredImage(), (int)(wid*templ.x), (int)(hei*templ.y), (int)(wid*templ.xsize), (int)(hei*templ.ysize), null);
       }
       setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
       return img;
    }
    /**
     * Deletes a layer
     */
    public void delete(Layer l){
    	if(Mouse.selected != Layer.nulle){
    	Render.main.layer.remove(Mouse.selected);
    	Mouse.deleted.add(Mouse.selected);
    	Mouse.selected = layer.peekFirst();
    	LayerPanel.main.updateList();
    }
    }
	public void lostOwnership(Clipboard paramClipboard,
			Transferable paramTransferable) {	
	}
}
