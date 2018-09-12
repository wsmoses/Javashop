   package gui;

   import java.awt.BorderLayout;
   import java.awt.event.WindowEvent;
   import java.awt.event.WindowListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JComponent;
   import javax.swing.JFrame;
   import javax.swing.JPanel;
import javax.swing.TransferHandler;
   import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import backbone.Transfers;

/**
 * Driver class for entire project
 * @author Ninja
 *
 */
   public class GUI {
      /**
       * Current GUI for, available for use by other classes
       */
      public static GUI main;
     /**
      * Main container JFrame
      */
      JFrame frame = new JFrame("JavaShop 1.2");
      /**
       * Content-filled container for all images and controls
       */
      Render render = new Render();
      /**
       * Top menu bar
       */
      Menu menu = new Menu();
      /**
       * Container for GUI
       */
      JPanel panel = new JPanel();
      /**
       * Initializes GUI
       */
      public GUI(){
         main = this;
 		panel.setTransferHandler(new Transfers());
 		setMappings(panel);
 		setMappings(render);
         frame.setSize(1350, 700);
         frame.setContentPane(panel);
         panel.setLayout(new BorderLayout());
         panel.add(menu, BorderLayout.NORTH);
         panel.add(render, BorderLayout.CENTER);
         panel.add(new LayerPanel(), BorderLayout.EAST);
         panel.add(new ToolPanel(), BorderLayout.WEST);
         frame.setVisible(true);
         frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         frame.addWindowListener(
               new WindowListener(){
                  public void windowActivated(WindowEvent arg0) {
                     render.requestFocus();
                  }
                  public void windowClosed(WindowEvent arg0) {}
               
                  public void windowClosing(WindowEvent arg0) {
                     System.exit(0);}
               
                  public void windowDeactivated(WindowEvent arg0) {}
               
                  public void windowDeiconified(WindowEvent arg0) {}
               
                  public void windowIconified(WindowEvent arg0) {}
               
                  public void windowOpened(WindowEvent arg0) {}
               
               });
      }
	public static void setMappings(JComponent list) {
          ActionMap map = list.getActionMap();
          map.put(TransferHandler.getCutAction().getValue(Action.NAME),
                  TransferHandler.getCutAction());
          map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
                  TransferHandler.getCopyAction());
          map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
                  TransferHandler.getPasteAction());
      }
      /**
       * Set Look and Feel
       */
      static{
         try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
               if ("Nimbus".equals(info.getName())) {
                  UIManager.setLookAndFeel(info.getClassName());
                  break;
               }
            }
         } 
            catch (Exception e) {
            }
      }
      public static void main(String[] args){
         new GUI();
      }
   }
