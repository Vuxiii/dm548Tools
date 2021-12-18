public class TypeSelecter {
    // public static final TypeSelecter Integer = new TypeSelecter();
    // public static final TypeSelecter Binary = new TypeSelecter(); 
    // public static final TypeSelecter SignedBinary = new TypeSelecter(); 
    // public static final TypeSelecter Hex = new TypeSelecter(); 
    // public static final TypeSelecter IEEE = new TypeSelecter(); 
    
    
    /**
     * This method returns the correct Enum based on the input t.
     * 1 -> Integer
     * 2 -> Binary
     * 3 -> Hex
     * @param t The Integer defining the selected Type
     * @return
     */
    public static Type get( int t ) {
        if ( t == 1 )
            return Type.Integer;
        else if ( t == 2 )
            return Type.Double;
        else if ( t == 3 ) 
            return Type.Binary;
        else if ( t == 4 ) 
            return Type.SignedBinary;
        else if ( t == 5 )
            return Type.Hex;
        else if ( t == 6 )
            return Type.IEEE;
        return null;
    }

    /**
     * This method prints all of the valid Types to the screen.
     */
    public static void printAll() {
        System.out.println( "1] Integer" );
        System.out.println( "2] Double" );
        System.out.println( "3] Binary" );
        System.out.println( "4] Signed Binary" );
        System.out.println( "5] Hex" );
        System.out.println( "6] IEEE" );
    }
}
