package gui;

import gui.Bar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.event.*;

import backbone.FileOps;
import backbone.Layer;

/**
 * JPanel to contain basic editing tools
 * @author Ninja
 *
 */
public class ToolPanel extends JPanel {
	/**
	 * Generated Serial ID
	 */
	private static final long serialVersionUID = -2566433600482837360L;
	/**
	 * Main ToolPanel for use by external classes
	 */
	public static ToolPanel main;
	/**
	 * Alpha background
	 */
	static BufferedImage background = FileOps.getImage("check.jpg");
	/**
	 * Top JPanel Containing Tools
	 * Also displays currently selected image
	 */
	public JPanel tools = new JPanel(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 8942938271902483131L;
		@Override
		public void paintComponent(Graphics g){
			g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
			g.drawImage(Mouse.selected.getFilteredImage(), 0, 0, getWidth(), getHeight(), null);
		}
	};
	/**
	 * Panel containing RGB Sliders
	 */
	JPanel effectsPanel = new JPanel();
	/**
	 * Panel containing alpha buttons
	 */
	JPanel buttonPanel = new JPanel();
	/**
	 * Panel to display the currently selected Color
	 */
	JPanel colorPanel = new JPanel(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 8942938271902483131L;
		@Override
		public void paintComponent(Graphics g){
			g.setColor(color);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	};
	/**
	 * Updates the color in the color panel
	 */
	public void update(){
		color = new Color((int)(red.getValue()), (int)(green.getValue()), (int)(blue.getValue()));
		colorPanel.repaint();
	}
	/**
	 * Button to apply Emboss effect
	 */
	JButton emboss = new JButton("Emboss");
	/**
	 * Button to apply alpha mask on currently selected color and layer
	 */
	JButton alpha = new JButton("Apply Alpha Mask");
	/**
	 * Button to apply alpha mask on currently selected layer to all colors
	 */
	JButton wholealpha = new JButton("Apply Alpha Mask On All");
	/**
	 * Slider denoting how much opacity the image should have
	 * 255 means max opacity
	 * 0 means full alpha
	 */
	JSlider alphaslider = new JSlider(0, 255, 0);
	/**
	 * Currently selected Color
	 */
	Color color = new Color(255, 255, 255);
	/**
	 * GUI components for Red Slider
	 */
	public Bar red;
	/**
	 * GUI components for Green Slider
	 */
	public Bar green;
	/**
	 * GUI components for Blue Slider
	 */
	public Bar blue;
	/**
	 * Instantiates ToolPanel and puts everything in place
	 */
public ToolPanel(){
	setPreferredSize(new Dimension(300, 900));
//Tools
	tools.setLayout(new BorderLayout());
tools.add(emboss, BorderLayout.NORTH);
emboss.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle)
	emboss(Mouse.selected);	
}});
	
	
	alphaslider.addChangeListener(new ChangeListener(){
		public void stateChanged(ChangeEvent e){

			Render.main.requestFocusInWindow();
		}
	});
	

colorPanel.setLayout(new BorderLayout());
alpha.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle)
	Mouse.selected.applyAlphaMask((int)(red.getValue()), (int)(green.getValue()), (int)(blue.getValue()), alphaslider.getValue());	
}});
wholealpha.addActionListener(new ActionListener(){
	public void actionPerformed(ActionEvent e){
		Render.main.requestFocusInWindow();
	if(Mouse.selected != Layer.nulle)
	Mouse.selected.applyAlpha(alphaslider.getValue());	
}});
	effectsPanel.setLayout(new GridLayout(3, 1));
	buttonPanel.setLayout(new GridLayout(2, 1));
	buttonPanel.add(alpha);
	buttonPanel.add(wholealpha);
	colorPanel.add(buttonPanel, BorderLayout.SOUTH);
	colorPanel.add(alphaslider, BorderLayout.NORTH);
	red = new Bar("Red:", 255);
	red.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			color = new Color((int)(red.getValue()), (int)(green.getValue()), (int)(blue.getValue()));
			colorPanel.repaint();
		}
	});
	effectsPanel.add(red);

	green = new Bar("Green:", 255);
	green.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			color = new Color((int)(red.getValue()), (int)(green.getValue()), (int)(blue.getValue()));
			colorPanel.repaint();
		}
	});
	effectsPanel.add(green);
	
	blue = new Bar("Blue:", 255);
	blue.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			color = new Color((int)(red.getValue()), (int)(green.getValue()), (int)(blue.getValue()));
			colorPanel.repaint();
		}
	});
	effectsPanel.add(blue);
	
	main = this;
	setLayout(new GridLayout(3, 1));
	add(tools);
	add(effectsPanel);
	add(colorPanel);
}

//Emboss by Jerry Huxtable
private final static float pixelScale = 255.9f;
private float azimuth = 135.0f * (float)Math.PI / 180.0f, elevation = 30.0f * (float)Math.PI / 180f;
private float width45 = 3.0f;
void emboss(Layer l) {
	int[] bumpPixels;
	int bumpMapWidth, bumpMapHeight;
	int height, width;
	width = bumpMapWidth = l.getFilteredImage().getWidth();
	height = bumpMapHeight = l.getFilteredImage().getHeight();
	bumpPixels = new int[bumpMapWidth * bumpMapHeight];
int i = 0;
	for (int y = 0; y < height; y++)
		for (int x = 0; x < width; x++)
			bumpPixels[i++] = (l.redarray[x][y]+l.greenarray[x][y]+l.bluearray[x][y])/3;

	int Nx, Ny, Nz, Lx, Ly, Lz, Nz2, NzLz, NdotL;
	int shade, background;

	Lx = (int)(Math.cos(azimuth) * Math.cos(elevation) * pixelScale);
	Ly = (int)(Math.sin(azimuth) * Math.cos(elevation) * pixelScale);
	Lz = (int)(Math.sin(elevation) * pixelScale);

	Nz = (int)(6 * 255 / width45);
	Nz2 = Nz * Nz;
	NzLz = Nz * Lz;

	background = Lz;

	int bumpIndex = 0;
	
	for (int y = 0; y < height; y++, bumpIndex += bumpMapWidth) {
		int s1 = bumpIndex;
		int s2 = s1 + bumpMapWidth;
		int s3 = s2 + bumpMapWidth;

		for (int x = 0; x < width; x++, s1++, s2++, s3++) {
			if (y != 0 && y < height-2 && x != 0 && x < width-2) {
				Nx = bumpPixels[s1-1] + bumpPixels[s2-1] + bumpPixels[s3-1] - bumpPixels[s1+1] - bumpPixels[s2+1] - bumpPixels[s3+1];
				Ny = bumpPixels[s3-1] + bumpPixels[s3] + bumpPixels[s3+1] - bumpPixels[s1-1] - bumpPixels[s1] - bumpPixels[s1+1];

				if (Nx == 0 && Ny == 0)
					shade = background;
				else if ((NdotL = Nx*Lx + Ny*Ly + NzLz) < 0)
					shade = 0;
				else
					shade = (int)(NdotL / Math.sqrt(Nx*Nx + Ny*Ny + Nz2));
			} else
				shade = background;
 {
	 l.redarray[x][y] = (short) ((l.redarray[x][y]*shade) >> 7);
	 l.greenarray[x][y] = (short) ((l.greenarray[x][y]*shade) >> 7);
	 l.bluearray[x][y] = (short) ((l.bluearray[x][y]*shade) >> 7);
			}
		}
	}
	l.update();
}

}
