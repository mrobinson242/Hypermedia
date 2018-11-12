package enums;

/**
 * EFontAwesome - Enumeration for Getting Font aWesome Values
 */
public enum EFontAwesome
{
   LINK("\uf0c1"),
   PAUSE("\uf04c"),
   PLAY("\uf04b"),
   PLAY_CIRCLE("\uf144"),
   SAVE("\uf0c7"),
   STOP("\uf04d"),
   STOP_CIRCLE("\uf28d"),
   TRASH("\uf1f8");

   /** Unicode Value of Font Awesome Icon */
   private String _unicode;

   /**
    * Private Constructor
    *
    * @param 
    */
   private EFontAwesome(String s)
   {
      _unicode = s;
   }

   /**
    * getCode - Gets the Code associated with
    *           the Font Awesome Icon
    *
    * @return String
    */
   public String getCode()
   {
      return _unicode;
   }
}