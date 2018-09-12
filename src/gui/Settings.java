package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Class to adjust topLeftLabel of the image
 * @author Ninja
 *
 */
public class Settings extends JFrame {
	/**
	 * Serial ID of class
	 */
	private static final long serialVersionUID = 4113779275304830309L;
	/**
	 * JTextField containing the height information for the displayed image
	 */
final JTextField height = new JTextField(20);
/**
 * JTextField containing the width information for the displayed image
 */
final JTextField width = new JTextField(20);
/**
 * Main topLeftLabel for use by external classes
 */
	Settings main;
	/**
	 * Creation of GUI of Settings window
	 */
public Settings(){
	//Initialization
	super("Picture Settings");
setSize(200, 125);
setLocation(300, 25);
setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
main = this;

//Panel Initialization
JPanel topLeftLabelPanel = new JPanel();
topLeftLabelPanel.setLayout(new GridLayout(3, 2));

//Label Initialization
final JLabel topLeftLabel = new JLabel(" Press Enter"), topRightLabel = new JLabel(" to Update"), 
heightLabel = new JLabel(" Height:"), 
widthLabel  = new JLabel(" Width:");

//Set Color
topLeftLabel.setForeground(Color.ORANGE);
topRightLabel.setForeground(Color.ORANGE);
height.setForeground(Color.ORANGE);
width.setForeground(Color.ORANGE);
heightLabel.setForeground(Color.ORANGE);
widthLabel.setForeground(Color.ORANGE);
topLeftLabel.setBackground(Color.BLACK);
topRightLabel.setBackground(Color.BLACK);
height.setBackground(Color.BLACK);
width.setBackground(Color.BLACK);
heightLabel.setBackground(Color.BLACK);
widthLabel.setBackground(Color.BLACK);
topLeftLabelPanel.setBackground(Color.BLACK);
topLeftLabelPanel.setForeground(Color.BLACK);

//Add Components
topLeftLabelPanel.add(topLeftLabel);
topLeftLabelPanel.add(topRightLabel);
topLeftLabelPanel.add(widthLabel);
topLeftLabelPanel.add(width);
topLeftLabelPanel.add(heightLabel);
topLeftLabelPanel.add(height);

//Set Starting Text
height.setText(Render.main.getImage().getHeight()+"");
width.setText(Render.main.getImage().getWidth()+"");

//Add Listeners
height.addActionListener(new Change());
width.addActionListener(new Change());
setContentPane(topLeftLabelPanel);
addWindowListener(new WindowAdapter(){
             public void windowClosing(WindowEvent e)
             {
            			try{
            				Render.main.change(Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
            			  }
            			catch(Exception ea){
            				
            			}
                GUI.main.menu.settings = false;
                setVisible(false);
                main.dispose();
             }
          });

    setVisible(true);
}
/**
 * Class to update the Image's specifications based on the information in the JTextFields
 * @author Ninja
 *
 */
class Change implements ActionListener{
	/**
	 * Updates the Image's specifications based on the information in the Settings window's JTextFields
	 */
	public void actionPerformed(ActionEvent a){
		try{
			Render.main.change(
					Integer.parseInt(width.getText()), 
					Integer.parseInt(height.getText()));
		  }
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
}