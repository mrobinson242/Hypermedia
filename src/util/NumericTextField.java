package util;

import javafx.scene.control.TextField;

/**
 * NumericTextField - Only allows for Numeric Inputs
 */
public class NumericTextField extends TextField
{
   /** The Text Field */
   private TextField _textField;

   /** Min/Max Value of Text Field */
   private int _minValue;
   private int _maxValue;

   /**
    * Constructor
    */
   public NumericTextField(final TextField textField, final int min, final int max)
   {
      super();

      // Initialize the Text Field
      _textField = textField;

      // Initialize the Min/Max Values of the Text Field
      _minValue = min;
      _maxValue = max;

      textField.textProperty().addListener((ovservable, oldVal, text) ->
      {
         // Check if Numeric Text
         boolean isNumeric = text.matches("[0-9]*");

         // Check if Empty String
         if(text.isEmpty())
         {
            textField.setText("");
         }
         else
         {
            // Check if Numeric Text
            if (isNumeric)
            {
               // Get the Integer Value of the Text
               Integer val = Integer.parseInt(text);

               // If Text falls between the Min and Max
               if(val >= _minValue && val <= _maxValue)
               {
                  // Update the Text Field
                  textField.setText(text);
               }
               else
               {
                  // Keep Text Field as old value
                  textField.setText(oldVal);
               }
            }
            else
            {
               // Keep Text Field as old value
               textField.setText(oldVal);
            }
         }

         // Update TextField
         _textField = textField;
      });
   }


   /**
    * setMinValue - Sets the Minimum Integer Value
    *               for the Text Field
    *
    * @param minValue - The Minimum Value
    */
   public void setMinValue(final int minValue)
   {
      // Set the Minimum Value
      _minValue = minValue;
   }

   /**
    * setMaxValue - Sets the Maximum Value of the Text Field
    *
    * @param maxValue - The Maximum Value
    */
   public void setMaxValue(final int maxValue)
   {
      // Set the Maximum Value
      _maxValue = maxValue;
   }

   /**
    * getIntValue - Returns the Text as an Integer Value
    *
    * @return int
    */
   public int getIntValue()
   {
      // Get Text Input from TextField
      String text = _textField.getText();

      // Convert to Integer
      Integer textAsInt = Integer.parseInt(text);

      return textAsInt;
   }
}