package dialogs.interfaces;

import javafx.scene.Parent;

/**
 * IDialog - Interface for Dialogs
 */
public interface IDialog
{
   /**
    * hideDialog - Hides the Dialog
    */
   void hideDialog();

   /**
    * showDialog - Shows the Dialog
    */
   void showDialog();

   /**
    * getPane
    *
    * @return Parent - The Dialog Pane
    */
   Parent getPane();
}
