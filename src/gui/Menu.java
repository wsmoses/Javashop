package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;

import backbone.FileOps;
import backbone.Layer;
import backbone.Transfers;

/**
 * The menu bar at the top of the screen
 * @author Ninja
 *
 */
public class Menu extends JMenuBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5617155576631422259L;
	protected static Menu main;
	/**
	 * A boolean denoting whether or not the settings window is open
	 */
public boolean settings = false;
/**
 * A boolean denoting whether or not the help window is open
 */
public boolean helps = false;
/**
 * A boolean denoting whether or not the about window is open
 */
public boolean abouts = false;
/**
 * The file menu
 */
JMenu file = new JMenu("File");
/**
 * The edit menu
 */
JMenu edit = new JMenu("Edit");
/**
 * The layer menu
 */
JMenu layer = new JMenu("Layer");
/**
 * The settings menu
 */
JMenu setting = new JMenu("Settings");
/**
 * The about menu
 */
JMenu about = new JMenu("About");
/**
 * The help menu
 */
JMenu help = new JMenu("Help");
/**
 * Adjusts quality of image
 */
JMenuItem sett = new JMenuItem("Image Settings");
/**
 * Saves image
 */
JMenuItem save = new JMenuItem("Save");
/**
 * Saves Currently selected Layer
 */
JMenuItem saveLayer = new JMenuItem("Save Layer");
/**
 * Saves image at specific quality (export)
 */
JMenuItem export = new JMenuItem("Export");
/**
 * Opens an image
 */
JMenuItem open = new JMenuItem("Open");
/**
 * Close the application
 */
JMenuItem quit = new JMenuItem("Quit");


JMenu neww = new JMenu("Add Layer");
JMenuItem fromurl = new JMenuItem("From URL");
JMenuItem fromfile = new JMenuItem("From File");
JMenuItem fromcolor = new JMenuItem("From Color");


JMenuItem aboutl = new JMenuItem("About");
JMenuItem helpl = new JMenuItem("Help");

/**
 * JButton to scale the current layer according to its aspect
 */
JMenuItem scaleAspect = new JMenuItem("Scale with Aspect");
/**
 * JButton to scale the current layer
 */
JMenuItem scale = new JMenuItem("Scale to Frame Size");
/**
 * JMenuItem to move the current layer to the top
 */
JMenuItem totop = new JMenuItem("To Top");
/**
 * JMenuItem to to move the current layer one position higher (if possible)
 */
JMenuItem up = new JMenuItem("Up");
/**
 * JMenuItem to to move the current layer one position lower (if possible)
 */
JMenuItem down = new JMenuItem("Down");
/**
 * JMenuItem to move the current layer to the bottom
 */
JMenuItem tobottom = new JMenuItem("To Bottom");
/**
 * Creates the LayerPanel
 * Makes the layout, puts all the components together, adds all listeners, and sets default values.
 */

/**
 * Add all sub-menus into the main bar
 */
public Menu(){
	main = this;
	//File Menu
	add(file);

	file.add(save);
	save.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		new Thread(){
			public void run(){
				FileOps.save();
			}
		}.start();
	}});
	file.add(export);
	export.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		FileOps.exportPNGFile();	
		}
	});
	file.add(open);
	open.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			Render.main.layer.clear();
			try {
File f = FileOps.openImageFile();
				BufferedImage im = ImageIO.read(f);
						if(im !=null)
				Render.main.add(im, f.getName());
						LayerPanel.main.updateList();
			} catch (Throwable t) {
				JOptionPane.showMessageDialog(null, "Error in Loading Image");
			}
		}
	});
	file.add(quit);
	quit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			System.exit(0);
		}
	});
	////////////////////////////////////////////////////////////////////////////////////////////
	add(edit);
	JMenuItem copy = new JMenuItem("Copy");

	edit.add(copy);
    copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
          copy.setMnemonic(KeyEvent.VK_C);
	copy.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			 if(Mouse.selected != Layer.nulle){
				 Transfers.copied.add(Mouse.selected);
			 Keys.clipboard.setContents(new StringSelection("0-"+Mouse.selected.hashCode()), Render.main);
			 }
		}
	});
	
	JMenuItem cut = new JMenuItem("Cut");
	
	edit.add(cut);
    cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    cut.setMnemonic(KeyEvent.VK_X);
	cut.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			 if(Mouse.selected != Layer.nulle){
				 Transfers.copied.add(Mouse.selected);
			 Keys.clipboard.setContents(new StringSelection("1-"+Mouse.selected.hashCode()), Render.main);
			 Render.main.delete(Mouse.selected);
			 }
		}
	});
	
	JMenuItem paste = new JMenuItem("Paste");

	edit.add(paste);
    paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
    paste.setMnemonic(KeyEvent.VK_V);
	paste.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			 Transferable t = Keys.clipboard.getContents(this);
			 TransferHandler.TransferSupport info = new TransferHandler.TransferSupport(Render.main, t);
			 if(Keys.transfers.canImport(info)){
				 Keys.transfers.importData(info);
			 }
		}
	});
	
		JMenuItem undo = new JMenuItem("Undo");

		edit.add(undo);
	    undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
	    undo.setMnemonic(KeyEvent.VK_Z);
		undo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(Mouse.deleted.size()>0){
				 Render.main.layer.addFirst(Mouse.deleted.pop());
				 LayerPanel.main.updateList();
			 }
			}
		});
	////////////////////////////////////////////////////////////////////////////////////////////
	
	add(layer);
	layer.add(neww);
	neww.add(fromurl);
	fromurl.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			try{
			String s = JOptionPane.showInputDialog("URL", "http://").replace(" ", "");
			if(!s.equals("")){
			BufferedImage im = FileOps.getImage(new URL(s));
			if(s.indexOf("/") != -1)
				s = s.substring(s.lastIndexOf("/")+1);
			Render.main.add(im, s);
			LayerPanel.main.updateList();
				}
			}
			catch(Exception er){
				JOptionPane.showMessageDialog(null, "Error in Inserting Image");
			}
		}
	});
	
	neww.add(fromfile);
	fromfile.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			try {
File f = FileOps.openImageFile();
				BufferedImage im = ImageIO.read(f);
						if(im !=null)
							Render.main.add(im, f.getName());
						LayerPanel.main.updateList();
			} catch (Throwable t) {
				t.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error in Loading Image");
			}
		}
	});
	neww.add(fromcolor);

	fromcolor.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

			Color col = JColorChooser.showDialog(new JFrame("Frame"), "New Layer", Color.BLUE);
		  if (col != null){
			  Graphics g = bi.getGraphics();
			  g.setColor(col);
			  g.fillRect(0, 0, 100, 100);
				Render.main.add(bi, col.toString());
			LayerPanel.main.updateList();
		  }
		}
	});
	////////////////////////////////////////////////////////////////////////////////////////////

	JMenuItem delete = new JMenuItem("Delete");

	layer.add(delete);
    delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    delete.setMnemonic(KeyEvent.VK_DELETE);
	delete.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			 Render.main.delete(Mouse.selected);
		}
	});
	
	layer.add(saveLayer);
	saveLayer.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		new Thread(){
			public void run(){
				FileOps.saveLayer();
			}
		}.start();
	}});
	scaleAspect.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			Render.main.requestFocusInWindow();
		if(Mouse.selected != Layer.nulle){
			Mouse.selected.scaleAspect();
		}
		}
	});
	layer.add(scaleAspect);
scale.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle){
		Mouse.selected.x = Mouse.selected.y = 0;
		Mouse.selected.xsize = Mouse.selected.ysize = 1;
	}
	}
});
layer.add(scale);
	
	totop.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		if(Mouse.selected != Layer.nulle){
		Render.main.layer.remove(Mouse.selected);
		Render.main.layer.addFirst(Mouse.selected);
		LayerPanel.main.updateList();
		}
		}
	});
	layer.add(totop);
	up.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		if(Mouse.selected != Layer.nulle && Mouse.selected != Render.main.layer.peekFirst()){
			int ind = Render.main.layer.indexOf(Mouse.selected);
		Render.main.layer.remove(Mouse.selected);
		Render.main.layer.add(ind-1, Mouse.selected);
		LayerPanel.main.updateList();
		}
		}
	});
	layer.add(up);
	down.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		if(Mouse.selected != Layer.nulle && Mouse.selected != Render.main.layer.get(Render.main.layer.size()-2)){
			int ind = Render.main.layer.indexOf(Mouse.selected);
		Render.main.layer.remove(Mouse.selected);
		Render.main.layer.add(ind+1, Mouse.selected);
		LayerPanel.main.updateList();
		}
		}
	});
	layer.add(down);
	tobottom.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
		if(Mouse.selected != Layer.nulle){
		Render.main.layer.remove(Mouse.selected);
		Render.main.layer.addLast(Mouse.selected);
		Render.main.layer.remove(Layer.nulle);
		Render.main.layer.addLast(Layer.nulle);
		LayerPanel.main.updateList();
		}
		}
	});
	layer.add(tobottom);
	
	add(setting);
	////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//Settings
	setting.add(sett);
	sett.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(!settings)
				new Settings();
			settings = true;
		}
	});
	////////////////////////////////////////////////////////////////////////////////////////////
	
	//About
	add(about);
	about.add(aboutl);
	aboutl.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(!abouts)
				new About();
			abouts = true;
		}
	});
	////////////////////////////////////////////////////////////////////////////////////////////
	
	//Help
	add(help);
	help.add(helpl);
	helpl.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(!helps)
				new Help();
			helps = true;
		}
	});
}
}
