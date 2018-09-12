package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import backbone.FileOps;

/**
 * GUI to display the help window
 * @author Ninja
 *
 */
public class About extends JFrame {
	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 4113779275304830309L;
	/**
	 * Creates the GUI and adds text from File
	 */
public About(){
	super("About");
setSize(550, 200);
setLocation(300, 125);
JTextArea a = new JTextArea("");
a.setBackground(Color.lightGray);
//a.setForeground(Color.ORANGE);
a.setEditable(false);
a.setLineWrap(true);
a.setWrapStyleWord(true);
JScrollPane scroll = new JScrollPane(a);
scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
try {
	BufferedReader f = new BufferedReader(new InputStreamReader(FileOps.get("About.txt").openStream()));
	while(f.ready())
	a.append(f.readLine()+"\n");
} catch (IOException e1) {
    dispose();
    Menu.main.abouts = false;
}
JPanel pa = new JPanel();
pa.setLayout(new BorderLayout());
pa.add(scroll, BorderLayout.CENTER);
setContentPane(pa);
    addWindowListener(
          new WindowAdapter()
          {
             public void windowClosing(WindowEvent e)
             {
                dispose();
                Menu.main.helps = false;
             }
          });

    setVisible(true);
a.setCaretPosition(0);
}
}