package gui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.*;

import javax.swing.Timer;

import backbone.Layer;

/**
 * Main class for all mouse-related functions
 * Also used for all selection, deletion pointers
 * @author Ninja
 *
 */
public  class Mouse extends MouseAdapter implements MouseMotionListener{
	/**
	 * Clipboard
	 */
	public static Layer clipBoard = Layer.nulle;
	/**
	 * The last deleted layer for undo purposes
	 */
	public static Stack<Layer> deleted = new Stack<Layer>();
	/**
	 * The type of operation to be performed on the selected component
	 */
	public static int moveType = 0;
	/**
	 * Currently selected layer
	 */
	public static Layer selected = Layer.nulle;
	/**
	 * Previously selected layer
	 */
	static Layer old = Layer.nulle;
	/**
	 * Boolean regarding updating the selected layers information
	 */
	static boolean moving = false;
	/**
	 * X position on the screen when the current layer was selected
	 * [0, 1]
	 */
	static volatile double startx;
	/**
	 * Y position on the screen when the current layer was selected
	 * [0, 1]
	 */
	static volatile double starty;
	/**
	 * Current x position on the screen
	 * [0, 1]
	 */
	static volatile double currentx;
	/**
	 * Current y position on the screen
	 * [0, 1]
	 */
	static volatile double currenty;
	/**
	 * Selected layer's x position on the screen when first selected
	 * [0, 1]
	 */
	static volatile double starterx;
	/**
	 * Selected layer's y position on the screen when first selected
	 * [0, 1]
	 */
	static volatile double startery;
	/**
	 * Selected layer's width when first selected
	 */
	static volatile double startxd;
	/**
	 * Selected layer's height when first selected
	 */
	static volatile double startyd;
	/**
	 * Method used to adjust the selected layer's position and size depending on move type.
	 * Each case represents the move type, while the if statements determine conditions depending on whether the image is flipped
	 */
	public static void move(){
		if(selected != Layer.nulle && moving){
			switch(moveType){
			case 1:
				if(startxd<0)
					selected.xsize = startxd+currentx-startx;
				else{
					selected.xsize = startxd-currentx+startx;
					selected.x = starterx+currentx-startx;
				}
				if(startyd<0)
					selected.ysize = startyd+currenty-starty;
					else{
					selected.ysize = startyd-currenty+starty;
					selected.y = startery+currenty-starty;	
					}
				break;
			case 0:
				selected.x = starterx+currentx-startx;
				selected.y = startery+currenty-starty;	
				break;
			case 2:
				if(startxd>0){
					selected.x = starterx+currentx-startx;
					selected.xsize = startxd-currentx+startx;
				}
				else
					selected.xsize = startxd+currentx-startx;
				if(startyd<0){
					selected.y = startery+currenty-starty;	
					selected.ysize = startyd-currenty+starty;
				}
				else
					selected.ysize = startyd+currenty-starty;
				break;
			case 3:
				if(startxd>0){
					selected.x = starterx+currentx-startx;
					selected.xsize = startxd-currentx+startx;
				}
				else
					selected.xsize = startxd+currentx-startx;
				break;
			case 4:
				if(startxd<0){
					selected.x = starterx+currentx-startx;
					selected.xsize = startxd-currentx+startx;
				}
				else
					selected.xsize = startxd+currentx-startx;
				if(startyd>0){
					selected.y = startery+currenty-starty;
					selected.ysize = startyd-currenty+starty;
				}
				else
					selected.ysize = startyd+currenty-starty;
				break;
			case 5:
				if(startxd<0){
					selected.x = starterx+currentx-startx;
				selected.xsize = startxd-currentx+startx;}
				else
				selected.xsize = startxd+currentx-startx;
				if(startyd<0){
					selected.y = startery+currenty-starty;
					selected.ysize = startyd-currenty+starty;
				}
				else
					selected.ysize = startyd+currenty-starty;
				break;
			case 6:
				if(startxd<0){
					selected.x = starterx+currentx-startx;
					selected.xsize = startxd-currentx+startx;
				}
				else
					selected.xsize = startxd+currentx-startx;
				break;
			case 7:
				if(startyd>0){
					selected.y = startery+currenty-starty;
					selected.ysize = startyd-currenty+starty;
				}
				else
					selected.ysize = startyd+currenty-starty;
				break;
			case 8:
				if(startyd<0){
					selected.y = startery+currenty-starty;	
					selected.ysize = startyd-currenty+starty;
				}
				else
					selected.ysize = startyd+currenty-starty;
				break;
			}
		}
	}
	/**
	 * Thread to update the selected component's position and size
	 */
	static {
		 new Thread(){
			 public void run(){
				 new Timer(10,
					 new ActionListener(){
					 public void actionPerformed(ActionEvent e){
					move();
					 }
				 }
				 ).start();
			 }
		 }.start();
		 
		 new Thread(){
			 public void run(){
				 new Timer(10,
					 new ActionListener(){
					 public void actionPerformed(ActionEvent e){
					if(selected != Layer.nulle){
						switch(selected.moveType(currentx, currenty)){
						case -1:
						Render.main.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						break;
						case 0:
						Render.main.setCursor(new Cursor(Cursor.MOVE_CURSOR));
						break;
						case 1:
						Render.main.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
						break;
						case 2:
						Render.main.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
						break;
						case 3:
						Render.main.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
						break;
						case 4:
						Render.main.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
						break;
						case 5:
						Render.main.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
						break;
						case 6:
						Render.main.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
						break;
						case 7:
						Render.main.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
						break;
						case 8:
						Render.main.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
						break;
						}
					}
					else
						Render.main.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					 }
				 }
				 ).start();
			 }
		 }.start();
	 }
	public void mouseClicked(MouseEvent e){
		currentx = (double)e.getX()/e.getComponent().getWidth();
		currenty = (double)e.getY()/e.getComponent().getHeight();
		if(selected != Layer.nulle){
		 int col = Render.main.getLayer(currentx, currenty).colorAt(currentx, currenty);
ToolPanel.main.red.setValue((col >> 16) & 0xff);
ToolPanel.main.green.setValue((col >> 8) & 0xff);
ToolPanel.main.blue.setValue(col & 0xff);
ToolPanel.main.update();
		}
	}
	/**
	 * Reassign all variables when the mouse is clicked
	 */
	@Override
	public void mousePressed(MouseEvent e){
		//Reassign current position
		currentx = (double)e.getX()/e.getComponent().getWidth();
		currenty = (double)e.getY()/e.getComponent().getHeight();
		
		//If another layer is on top of the current layer and one selects the adjustment tools, use that layer not the top layer
		if(selected != Layer.nulle && selected.at(currentx, currenty) && (moveType = selected.moveType(currentx, currenty)) != 0){
			startx = currentx;
			starty = currenty;
			starterx = selected.x;
			 startery = selected.y;
			 startxd = selected.xsize;
			 startyd = selected.ysize;
			 moving = true;
LayerPanel.main.list.setSelectedIndex(LayerPanel.main.listModel.indexOf(Mouse.selected));
ToolPanel.main.tools.repaint();			 
return ;
		}
		
		//Selected layer
		 Layer temp = Render.main.getLayer(startx = currentx, starty = currenty);
		 
		 //If nothing is selected
		 if(temp == Layer.nulle)
		 {
			 selected = temp;
			 LayerPanel.main.list.setSelectedIndex(LayerPanel.main.listModel.indexOf(Mouse.selected));
			 ToolPanel.main.tools.repaint();
			 return;
		 }
		 
		 //Assign starting positions
		 starterx = temp.x;
		 startery = temp.y;
		 
		 //Assign move type
		 moveType = temp.moveType(startx, starty);

		 if (temp == selected){
			 //If previously selected, deselect
			 if(moveType== 0){
			 old = selected;
			 selected = Layer.nulle;
			 }
			 //If previously selected and a tool is chosen, continue to use those tools
			 else{
				 selected = temp;
				 startxd = selected.xsize;
				 startyd = selected.ysize;
			 moving = true;
			 }
		 }
		 else{
			 selected = temp;
			 startxd = selected.xsize;
			 startyd = selected.ysize;
		 moving = true;
		 }
		 LayerPanel.main.update();
		 ToolPanel.main.tools.repaint();
		 LayerPanel.main.list.setSelectedIndex(LayerPanel.main.listModel.indexOf(Mouse.selected));
	 }
	
	/**
	 * Finalize all movements when the mouse is released.
	 * Stop updating the position
	 */
	 @Override
	public void mouseReleased(MouseEvent e){
		 currentx = (double)e.getX()/e.getComponent().getWidth();
		 currenty = (double)e.getY()/e.getComponent().getHeight();
		 move();
		 moving = false; 
	 }
	 /**
	  * Adjust the current mouse position if moved
	  */
	 @Override
	public void mouseMoved(MouseEvent e){
		 currentx = (double)e.getX()/e.getComponent().getWidth();
		 currenty =  (double)e.getY()/e.getComponent().getHeight();	
			 }
	 /**
	  * Adjust the current mouse position if dragged
	  */
	 @Override
	public void mouseDragged(MouseEvent e){
		 currentx = (double)e.getX()/e.getComponent().getWidth();
		 currenty =  (double)e.getY()/e.getComponent().getHeight();	 
		 if(selected == Layer.nulle && old != Layer.nulle && old.at(currentx, currenty) && Render.main.layer.contains(old)){
			 selected = old;
			 moving = true;
			 LayerPanel.main.list.setSelectedIndex(LayerPanel.main.listModel.indexOf(Mouse.selected));
		 }
	 }	 
}
