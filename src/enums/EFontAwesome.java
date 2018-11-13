package enums;

/**
 * EFontAwesome - Enumeration for Getting Font aWesome Values
 */
public enum EFontAwesome
{
   FILE_CODE("\uf1c9"),    // Regular
   FILE_VIDEO("\uf1c8"),   // Regular
   LINK("\uf0c1"),         // Regular
   NEW_FILE("\uf15b"),     // Regular
   PAUSE("\uf04c"),        // Regular
   PLAY("\uf04b"),         // Regular
   PLAY_CIRCLE("\uf144"),  // Regular
   SAVE("\uf0c7"),         // Regular
   STOP("\uf04d"),         // Regular
   STOP_CIRCLE("\uf28d"),  // Regular
   TRASH("\uf1f8");        // Regular

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