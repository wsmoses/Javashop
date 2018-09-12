package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Component class to handle communications between labels, textboxes, and sliders
 * @author Ninja
 *
 */
public class Bar extends JPanel{
	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 3980539713923727333L;
	/**
	 * Formating for the JTextfield
	 */
	static DecimalFormat format = new DecimalFormat("#.###");
	/**
	 * Label description of the Bar
	 */
	JLabel lab;
	/**
	 * Edittable textfield
	 */
	JTextField field = new JTextField(5);
	/**
	 * The correspondance between a change in the slider and the textfield/value
	 * 1 means that each change will be the same
	 * 10 means that for every 10 changes in the slider, the textfield/value will move once
	 */
	public double exchange = 1;
	/**
	 * The shift between the the textfield/value and the slider
	 * 0 indicates no shift
	 * 10 indicates that the slider will be 10 less than the textField
	 */
	public double shift = 0;
	/**
	 * The currently stored value of the Box
	 */
	public double value = 255;
	/**
	 * The Slider GUI component
	 */
	JSlider slider = new JSlider(0, 255, 255);
	/**
	 * An external listener that will be fired when either the slider or textbox changes
	 */
	ActionListener listener;
	/**
	 * Boolean representation of whether or not the Bar should be updating
	 */
	boolean change = true;
	/**
	 * Initializes all the GUI components, layout, and values.
	 * @param s String label
	 * @param val Initial Value
	 */
public Bar(String s, double val){
		super();
		this.value = val;
		field.setPreferredSize(new Dimension(60, 60));
		slider.setValue((int) ((value-shift)*exchange)) ;
		field.setText(format.format(value));
		lab = new JLabel(s);
		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(1, 2));
		pan.add(lab);
		pan.add(field);
		setLayout(new BorderLayout());
		add(pan, BorderLayout.WEST);
		add(slider, BorderLayout.CENTER);
	}
	/**
	 * Returns the current value
	 * @return double value
	 */
	public double getValue(){
		return value;
	}
	/**
	 * Changes the current value in all the components
	 * @param d the new value
	 */
	public void setValue(double d){
		change = false;
		value = d;
		field.setText(""+value);
		slider.setValue((int) ((value-shift)*exchange));
		change = true;
	}
	/**
	 * Adds an action Listener if a value is changed
	 * @param l
	 */
	public void addActionListener(final ActionListener l){
		listener = l;
	field.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(change){
		slider.setValue((int) (((value = Double.parseDouble(field.getText()))-shift)*exchange)) ;
		l.actionPerformed(e);	
		Render.main.requestFocusInWindow();
		}
		}
	});

	slider.addChangeListener(new ChangeListener(){
		public void stateChanged(ChangeEvent arg0) {
			
			field.setText(format.format(value = (slider.getValue()/exchange+shift)));
			if(change){listener.actionPerformed(null);
			Render.main.requestFocusInWindow();
			}
		}
	}
	);
	}
}