package backbone;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.beans.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import gui.*;
/**
 * Main class to handle File operations
 * @author Ninja
 *
 */
public class FileOps {
/**
 * The height of the exported image file according to the user's specifications.
 */
static int height = 1000;
/**
 * The width of the exported image file according to the user's specifications.
 */
static int width = 1000;
/**
 * The JFrame to hold the export dialog where the user will enter his or her specifications for export.
 */
static JFrame size = new JFrame("Export");
/**
 * The main JPanel of the export dialog box.
 */
static JPanel mainpanel = new JPanel();
/**
 * The JPanel to hold all of the user's specifications for export.
 */
static JPanel specificationpanel = new JPanel();
/**
 * A GUI button where the user can indicate that the specifications are correct.
 * The user may also type enter on the "x" and "y" JTextFields to indicate so.
 */
static JButton button = new JButton("OK");
/**
 * The input area where the user will write his or her specifications for the exported image's width.
 */
static JTextField x = new JTextField();
/**
 * The input area where the user will write his or her specifications for the exported image's height.
 */
static JTextField y = new JTextField();
/**
 * The initialization methods necessary to create the Export Specification GUI.
 */
static {
	size.setContentPane(mainpanel);
	x.addActionListener(new Go());
	y.addActionListener(new Go());
	button.addActionListener(new Go());
	specificationpanel.setLayout(new GridLayout(2, 2));
	specificationpanel.add(new JLabel("Width:  "));
	specificationpanel.add(x);
	specificationpanel.add(new JLabel("Height: "));
	specificationpanel.add(y);
	mainpanel.setLayout(new BorderLayout());
	mainpanel.add(specificationpanel, BorderLayout.CENTER);
	mainpanel.add(button, BorderLayout.SOUTH);
    size.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	size.setSize(200, 175);
	size.setLocation(500, 100);
}
/**
 * Gives the image representation of a File
 * @param File location of Picture
 * @return BufferedImage representation of the Image File
 */
public static BufferedImage getImage(File in){
	try {
		return ImageIO.read(in);
	} catch (IOException e) {
		e.printStackTrace();
		return null;
	}
}
/**
	/**
	 * Gives the image representation of a URL
	 * @param URL location of Picture File
	 * @return BufferedImage representation of the Image File
	 */
	public static BufferedImage getImage(URL in){
		try {
			return ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Gives the image representation of an internal File represented by a string
	 * @param String representation of the File location of Picture File
	 * @return BufferedImage representation of the Image File
	 */
	public static BufferedImage getImage(String place){
		try {
			return ImageIO.read(FileOps.class.getResource("/pictures/"+place));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Returns the URL of the internal file
	 */
	public static URL get(String place){
		return FileOps.class.getResource("/pictures/"+place);
	}
static class ImagePreview extends JComponent  implements PropertyChangeListener {
/**
	 * The generated serial ID of the class.
	 */
	private static final long serialVersionUID = 3932843229494114522L;
/**
 * The currently selected File
 */
File file = null;
/**
 * The visual preview of the selected File
 */
ImageIcon thumbnail = null;
/**
 * Initializes the ImagePreview
 * @param FileChooser argument to apply previews for
 */
public ImagePreview(JFileChooser fc) {
setPreferredSize(new Dimension(100, 50));
fc.addPropertyChangeListener(this);
}
/**
 * Creates a preview of the currently selected File.
 */
public void loadImage() {
	// If the File is null, the preview should reflect that.
if (file == null) {
thumbnail = null;
return;
}
//The ImageIcon representation of the file.
ImageIcon imageicon = new ImageIcon(file.getPath());
//If the file is an image, create the preview
if (imageicon != null) {
//If the width is larger than 90 pixels in width, scale the image to conserve resources.
if (imageicon.getIconWidth() > 90)
thumbnail = new ImageIcon(imageicon.getImage().getScaledInstance(90, -1,Image.SCALE_FAST));
else
thumbnail = imageicon;
}
}
/**
 * Updates the file and preview if the user changes selection
 */
public void propertyChange(PropertyChangeEvent e) {
boolean update = false;
String prop = e.getPropertyName();

//If the directory changed, don't show an image.
if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
file = null;
update = true;

//If a file became selected, find out which one.
} else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
file = (File) e.getNewValue();
update = true;
}

//Update the preview accordingly.
if (update) {
thumbnail = null;
if (isShowing()) {
loadImage();
repaint();
}
}
}

protected void paintComponent(Graphics g) {
if (thumbnail == null) {
loadImage();
}
if (thumbnail != null) {
int x = getWidth()/2 - thumbnail.getIconWidth()/2;
int y = getHeight()/2 - thumbnail.getIconHeight()/2;

if (y < 0) {
y = 0;
}

if (x < 0) {
x = 0;
}
thumbnail.paintIcon(this, g, x, y);
}
}
}
/**
 * A class designed to allow the user to only select Image Files
 * @author Ninja
 *
 */
public static class ImageFilter extends FileFilter {
	/**
	 * A String array containing all the accepted file extensions
	 */
	 final static String[] good = {"jpeg", "jpg", "gif", "tiff", "tif", "png"};
	 /**
	  * A method designed to determine whether or not a File is an image File.
	  */
	 @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String e = f.getName();
            int ind = e.lastIndexOf(".");
            if (ind == -1) return false;
            String temp = e.substring(e.lastIndexOf(".")+1).toLowerCase().trim();
            for(String s:good)
            	if(temp.equals(s))
            		return true;
            return false;
    }
/**
 * Returns the description of the Filter
 */
	@Override
	public String getDescription() {
		return "Image Files";
	}
}
/**
 * A class designed to update the height and width of the user's specifications based on the dialog box and then save it.
 * If the specifications are illegal, the dialog box will not close.
 * @author Ninja
 *
 */
static class Go implements ActionListener{
	public void actionPerformed(ActionEvent e){
		try{
			width = Integer.parseInt(x.getText().trim());
			height = Integer.parseInt(y.getText().trim());
			size.setVisible(false);
			exportFile();
		}
		catch(Exception er){
			
		}
	}
}
/**
 * Opens up a dialog and saves the currently displayed image as a PNG file according to the user's specifications.
 * @return A boolean designating whether or not the save was successful.
 */
public static boolean exportFile(){
	try{
		File t = savePNGFile();
		ImageIO.write(Render.main.preRender(width, height), "png", t);
		JOptionPane.showMessageDialog(null, "Export Complete");
		return true;
	}
	catch(Exception e){
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Export Failed");
		return false;
	}
}
/**
 * Opens up a dialog that allows the user to choose the specifications of the Image file to be exported.
 */
public static void exportPNGFile(){
	x.setText(""+width);
	y.setText(""+height);
	size.setVisible(true);
}
/**
 * Opens up a dialog that allows the user to choose where to save an Image file.
 * @return the File representation of the selected Image to-be.
 */
public static File savePNGFile(){
	JFileChooser fc = new JFileChooser();
    fc.addChoosableFileFilter(new ImageFilter());
    fc.setAcceptAllFileFilterUsed(false);
   // fc.setFileView(new ImageFileView());
    fc.setAccessory(new ImagePreview(fc));
	fc.showSaveDialog(null);
    return fc.getSelectedFile();
}
/**
 * Opens up a dialog to open an Image file and returns that file as a BufferedImage.
 * @return The BufferedImage representation of the Image file.
 */
public static BufferedImage openImage(){
	try {
		return ImageIO.read(openImageFile());
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}
/**
 * Opens up a dialog that allows the user to choose an Image file to open.
 * @return the File representation of the selected Image.
 */
public static File openImageFile(){
	JFileChooser fc = new JFileChooser();
    fc.addChoosableFileFilter(new ImageFilter());
    fc.setAcceptAllFileFilterUsed(false);
   // fc.setFileView(new ImageFileView());
    fc.setAccessory(new ImagePreview(fc));
	if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
    return fc.getSelectedFile();
	return null;
}
/**
 * Opens up a dialog and saves the currently selected Layer as a PNG file.
 * @return A boolean designating whether or not the save was successful.
 */
public static boolean saveLayer(){
	String to = "";
	try{
		File t = savePNGFile();//choose("Save");
ImageIO.write(Mouse.selected.getFilteredImage(), "png", t);
		JOptionPane.showMessageDialog(null, "Saved: "+to);
		return true;
	}
	catch(Exception e){
		System.out.println(to);
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Save Failed");
		return false;
	}
}
/**
 * Opens up a dialog and saves the currently displayed image as a PNG file.
 * @return A boolean designating whether or not the save was successful.
 */
public static boolean save(){
	String to = "";
	try{
		File t = savePNGFile();//choose("Save");
ImageIO.write(Render.main.preRender(Render.wid, Render.hei), "png", t);
		JOptionPane.showMessageDialog(null, "Saved: "+to);
		return true;
	}
	catch(Exception e){
		System.out.println(to);
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, "Save Failed");
		return false;
	}
}
}
