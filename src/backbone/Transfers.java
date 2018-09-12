   package backbone;

   import gui.Keys;
   import gui.LayerPanel;
   import gui.Mouse;
   import gui.Render;

   import java.awt.Image;
   import java.awt.datatransfer.*;
   import java.awt.image.BufferedImage;
   import java.io.File;
   import java.io.IOException;
   import java.util.LinkedList;
   import java.util.List;

   import javax.swing.JComponent;
   import javax.swing.TransferHandler;

   /**
    * Class to handle copy, cut, paste and drag and drop
    * @author Ninja
    *
    */
   public class Transfers extends TransferHandler {
   /**
    * Generated Serial ID
    */
      private static final long serialVersionUID = -1858500871518597376L;
      /**
       * Container for copied layers
       */
      public static LinkedList<Layer> copied = new LinkedList<Layer>();
      public Transfers() {
         super();
      }
    /**
     * Tests if the data can be inserted
     */
      public boolean canImport(TransferHandler.TransferSupport info) {
         return info.isDataFlavorSupported(DataFlavor.stringFlavor) || info.isDataFlavorSupported(DataFlavor.imageFlavor) || info.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
      }
      /**
       * Type of actions this handler can do
       */
      public int getSourceActions(JComponent c) {
         return TransferHandler.COPY_OR_MOVE;
      }
    /**
     * Imports the data into the program
     */
      public boolean importData(TransferHandler.TransferSupport info) {
         Transferable t = info.getTransferable();
         if(info.isDataFlavorSupported(DataFlavor.stringFlavor))
         {
            int i;
            try {
               String string = (String)t.getTransferData(DataFlavor.stringFlavor);
               i = Integer.parseInt(string.substring(2));
               Layer lay = null;
               for(Layer l:copied){
                  if(lay == null && l.hashCode() == i){
                     lay = l;
                     if(string.charAt(0) == '0'){
                        Render.main.layer.addFirst(Mouse.selected = l.clone());
                        LayerPanel.main.updateList();}
                     else{
                        Render.main.layer.addFirst(Mouse.selected = l);
                        Keys.clipboard.setContents(new StringSelection("0-"+Mouse.selected.hashCode()), Render.main);
                        LayerPanel.main.updateList();
                     }
                  }
               }
               if(lay != null){
                  copied.clear();
                  copied.add(lay);
                  return true;
               }
            } 
               catch (Exception e) {
               } 
            return false;
         }
         else if(info.isDataFlavorSupported(DataFlavor.imageFlavor)){
            try {
               Object o = t.getTransferData(DataFlavor.imageFlavor);
               if(o instanceof Image){
                  Render.main.add((Image)o, "Pasted");
                  return true;
               }
            } 
               catch (UnsupportedFlavorException e) {
                  e.printStackTrace();
               } 
               catch (IOException e) {
                  e.printStackTrace();
               }
            return false;
         }
         else
         {
            try {
               @SuppressWarnings("unchecked")
                  List<java.io.File> list = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
               for(File f:list){
                  BufferedImage img = FileOps.getImage(f);
                  if(img !=null)
                     Render.main.add(img, "Pasted");
               }
            } 
               catch (UnsupportedFlavorException e) {
                  e.printStackTrace();
               } 
               catch (IOException e) {
                  e.printStackTrace();
               }
            return false;
         }
      }
   }
