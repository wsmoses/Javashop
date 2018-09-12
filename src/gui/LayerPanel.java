package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import backbone.Layer;
import backbone.Transfers;

/**
 * JPanel designed to allow the user to interact with the image with different effects and GUI components
 * @author Ninja
 *
 */
public class LayerPanel extends JPanel {

	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Main LayerPanel for use by external classes
	 */
	public static LayerPanel main;
	/**
	 * Red adjustment components
	 */
	public final Bar red;
	/**
	 * Green adjustment components
	 */
	public final Bar green;
	/**
	 * Blue adjustment components
	 */
	public final Bar blue;
	/**
	 * Brightness adjustment components
	 */
	public final Bar overall;
	/**
	 * Contrast adjustment components
	 */
	public final Bar contrast;
	/**
	 * ListModel to fcreate the JList of Layers
	 */
    public DefaultListModel<Layer> listModel = new DefaultListModel<Layer>();
    /**
     * JList of the Layers
     */
    public JList<Layer> list;
    /**
     * JPanel containing all the effects and filters for the layer
     */
	JPanel effectsPanel = new JPanel();
	/**
	 * JPanel containing the layer information for the picture
	 */
	JPanel layerPanel = new JPanel();
	/**
	 * JCheckBox to determine grayscale
	 */
	public final JCheckBox grayscale;
	/**
	 * JCheckBox to determine color inversion
	 */
	public final JCheckBox invert;
	/**
	 * JCheckBox to determine whether or not the alpha should be inverted
	 */
	public final JCheckBox alpha;
	/**
	 * JButton to scale the current layer according to its aspect
	 */
	JButton scaleAspect = new JButton("Scale with Aspect");
	/**
	 * JButton to scale the current layer
	 */
	JButton scale = new JButton("Scale to Frame Size");
/**
 * JButton to delete the current layer
 */
JButton delete = new JButton("Delete");
/**
 * JButton to move the current layer to the top
 */
JButton totop = new JButton("To Top");
/**
 * JButton to to move the current layer one position higher (if possible)
 */
JButton up = new JButton("Up");
/**
 * JButton to to move the current layer one position lower (if possible)
 */
JButton down = new JButton("Down");
/**
 * JButton to move the current layer to the bottom
 */
JButton tobottom = new JButton("To Bottom");
/**
 * Creates the LayerPanel
 * Makes the layout, puts all the components together, adds all listeners, and sets default values.
 */
public LayerPanel(){
	setPreferredSize(new Dimension(300, 900));
	setLayout(new GridLayout(2, 1));
	add(effectsPanel);
	add(layerPanel);
	effectsPanel.setLayout(new GridLayout(6, 1));
	
	red = new Bar("Red:", 255);
	red.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(Mouse.selected !=Layer.nulle){
				Mouse.selected.red = red.getValue()/255;
	        	Mouse.selected.update();
			}
		}
	});
	effectsPanel.add(red);

	green = new Bar("Green:", 255);
	green.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(Mouse.selected !=Layer.nulle){
				Mouse.selected.green = green.getValue()/255;
	        	Mouse.selected.update();
			}
		}
	});
	effectsPanel.add(green);

	blue = new Bar("Blue:", 255);
	blue.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(Mouse.selected !=Layer.nulle){
				Mouse.selected.blue = blue.getValue()/255;
	        	Mouse.selected.update();
			}
		}
	});
	effectsPanel.add(blue);
	

	overall = new Bar("Bright:", 100);
	overall.exchange = 1.275;
	overall.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(Mouse.selected !=Layer.nulle){
				Mouse.selected.overall = overall.getValue()/100;
	        	Mouse.selected.update();
			}
		}
	});
	effectsPanel.add(overall);
	
	contrast = new Bar("Contrast:", 0);
	contrast.shift = 1;
	contrast.exchange = -51.;
	contrast.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			if(Mouse.selected !=Layer.nulle){
				Mouse.selected.contrast = contrast.getValue();
	        	Mouse.selected.update();
			}
		}
	});
	effectsPanel.add(contrast);
	
	JPanel checks = new JPanel();
	checks.setLayout(new GridLayout(1, 3));
	grayscale = new JCheckBox("GrayScale");
	grayscale.addActionListener( new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	    	 JCheckBox abstractButton = (JCheckBox) actionEvent.getSource();
	 		Render.main.requestFocusInWindow();
	        if(Mouse.selected != Layer.nulle){
	        	Mouse.selected.grayscale = abstractButton.getModel().isSelected();
	        	Mouse.selected.update();
	        }
	      }
	    });
	checks.add(grayscale);
	
	invert = new JCheckBox("Invert");
	invert.addActionListener( new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	    	 JCheckBox abstractButton = (JCheckBox) actionEvent.getSource();
	 		Render.main.requestFocusInWindow();
	        if(Mouse.selected != Layer.nulle){
	        	Mouse.selected.inverted = abstractButton.getModel().isSelected();
	        	Mouse.selected.update();
	        }
	      }
	    });
	checks.add(invert);

	alpha = new JCheckBox("Invert Alpha");
	alpha.addActionListener( new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	    	 JCheckBox abstractButton = (JCheckBox) actionEvent.getSource();
	 		Render.main.requestFocusInWindow();
	        if(Mouse.selected != Layer.nulle){
	        	Mouse.selected.invertedalpha = abstractButton.getModel().isSelected();
	        	Mouse.selected.update();
	        }
	      }
	    });
	checks.add(alpha);
	effectsPanel.add(checks);
main = this;

layerPanel.setLayout(new GridLayout(1, 3));
JPanel layerControls = new JPanel();
layerControls.setLayout(new GridLayout(7, 1));
scaleAspect.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle){
		Mouse.selected.scaleAspect();
	}
	}
});
layerControls.add(scaleAspect);
scale.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle){
		Mouse.selected.scale();
	}
	}
});
layerControls.add(scale);
delete.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	Render.main.delete(Mouse.selected);
	}
});
layerControls.add(delete);
totop.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle){
	Render.main.layer.remove(Mouse.selected);
	Render.main.layer.addFirst(Mouse.selected);
	updateList();
	}
	}
});
layerControls.add(totop);
up.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle && Mouse.selected != Render.main.layer.peekFirst()){
		int ind = Render.main.layer.indexOf(Mouse.selected);
	Render.main.layer.remove(Mouse.selected);
	Render.main.layer.add(ind-1, Mouse.selected);
	updateList();
	}
	}
});
layerControls.add(up);
down.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle && Mouse.selected != Render.main.layer.get(Render.main.layer.size()-2)){
		int ind = Render.main.layer.indexOf(Mouse.selected);
	Render.main.layer.remove(Mouse.selected);
	Render.main.layer.add(ind+1, Mouse.selected);
	updateList();
	}
	}
});
layerControls.add(down);
tobottom.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle){
	Render.main.layer.remove(Mouse.selected);
	Render.main.layer.addLast(Mouse.selected);
	Render.main.layer.remove(Layer.nulle);
	Render.main.layer.addLast(Layer.nulle);
	updateList();
	}
	}
});
layerControls.add(tobottom);
layerPanel.add(layerControls);
for( Layer l:  Render.main.layer)
   listModel.addElement(l);
   list = new JList<Layer>(listModel);
   JScrollPane listScrollPane = new JScrollPane(list);
   list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   list.setSelectedIndex(listModel.indexOf(Mouse.selected));
   layerPanel.add(listScrollPane);
   list.addListSelectionListener(new Sel());
   list.setDragEnabled(true);
   list.setTransferHandler(new Transfers());
   list.setDropMode(DropMode.ON_OR_INSERT);
   
}
/**
 * Updates the currently displayed list if the layers are changed
 */
public void updateList(){
	listModel = new DefaultListModel<Layer>();
for( Layer l:  Render.main.layer)
   listModel.addElement(l);
list.setModel(listModel);
list.setSelectedIndex(listModel.indexOf(Mouse.selected));
}
/**
 * Updates all Effects GUI values if a different layer is selected
 */
public void update(){
if(Mouse.selected!=Layer.nulle){
	red.setValue(Mouse.selected.red*255);
	blue.setValue(Mouse.selected.blue*255);
	green.setValue(Mouse.selected.green*255);
	overall.setValue(Mouse.selected.overall*100);
	contrast.setValue(Mouse.selected.contrast);
	grayscale.setSelected(Mouse.selected.grayscale);
	invert.setSelected(Mouse.selected.inverted);
	alpha.setSelected(Mouse.selected.invertedalpha);
}
}
/**
 * Selection Listener which changes the currently selected layer by the Mouse if the currently selected Layer in the JList changes
 * @author Ninja
 *
 */
class Sel implements ListSelectionListener
{
   public void valueChanged(ListSelectionEvent e) 
   {
      if (list.getSelectedIndex() != -1) 
      {
         Mouse.selected = list.getSelectedValue();
         update();
         ToolPanel.main.tools.repaint();
	 		Render.main.requestFocusInWindow();
      }
   }
} 
}

