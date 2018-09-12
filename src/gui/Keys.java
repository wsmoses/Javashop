package gui;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;

import javax.swing.*;

import backbone.Layer;
import backbone.Transfers;

/**
 * Class to perform any key-based operations
 * @author Ninja
 *
 */
public  class Keys extends KeyAdapter{
	/**
	 * Boolean designating whether the up button is being pressed
	 */
	 static boolean up;
		/**
		 * Boolean designating whether the down button is being pressed
		 */
	 static boolean down;
		/**
		 * Boolean designating whether the left button is being pressed
		 */
	 static boolean left;
		/**
		 * Boolean designating whether the right button is being pressed
		 */
	 static boolean right;
	 static Transfers transfers = new Transfers();
	    public static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		/**
		 * Thread to update the selected component's position
		 */
	 static{
		 new Thread(){
			 public void run(){
				 new Timer(25,
					 new ActionListener(){
					 public void actionPerformed(ActionEvent e){
						 if(Mouse.selected!=Layer.nulle){
					if(up)
						Mouse.selected.y-=.005;
					if(down)
						Mouse.selected.y+=.005;
					if(left)
						Mouse.selected.x-=.005;
					if(right)
						Mouse.selected.x+=.005;
					 }
					 }
				 }
				 ).start();
			 }
		 }.start();
	 }
	 /**
	  * Adjust booleans based on key pressed
	  * Delete current layer if delete key pressed
	  */
	 @Override
	 public void keyPressed(KeyEvent e){
		 switch(e.getKeyCode()){
		 case KeyEvent.VK_UP:
			 up = true;
			 return;
		 case KeyEvent.VK_DOWN:
			 down = true;
			 return;
		 case KeyEvent.VK_LEFT:
			 left = true;
			 return;
		 case KeyEvent.VK_RIGHT:
			 right = true;
			 return;
		 }
	 }
	 /**
	  * Adjust booleans based on key released
	  */
	 @Override
	 public void keyReleased(KeyEvent e){
		 switch(e.getKeyCode()){
		 case KeyEvent.VK_UP:
			 up = false;
			 return;
		 case KeyEvent.VK_DOWN:
			 down = false;
			 return;
		 case KeyEvent.VK_LEFT:
			 left = false;
			 return;
		 case KeyEvent.VK_RIGHT:
			 right = false;
			 return;
		 }
	 }
}
