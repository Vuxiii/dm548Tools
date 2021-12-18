import java.util.Scanner;

public class Variable {
    private static Scanner in = new Scanner( System.in );


    final String name;
    String val;
    Type type;
    public Variable( String name, String val, Type type ) {
        this.name = name;
        this.val = val;
        this.type = type;
    }

    public Variable copy() {
        return new Variable( name, val, type );
    }

    public String toString() {
        String s = name + " = ";
        if ( type == Type.IEEE ) {
            s += val.charAt(0) + " ";
            s += val.substring( 1, 9 ) + " ";
            for ( int i = 9; i < 31; i = i + 2 )
                s += val.substring( i, i + 2 ) + " ";
            s += val.charAt( val.length() - 1 );
        } else if ( type == Type.Hex )
            s = "0x" + val.toUpperCase();
        else 
            s += val;
        return s + " [" + type + "]";
        // return name + " = " + val + " [" + type + "]";
    }

    /**
     * This method changes the Type of the given Variable defined by the User.
     * @param var the Variable to be changed.
     * @return True if the change was successful.
     */
    public static boolean changeType( Variable var ) {
        System.out.println( "What type do you want to change " + var.toString() + " to?" );
        System.out.println( "0] Back" );
        TypeSelecter.printAll();

        System.out.print( "> " );
        int newType = in.nextInt();
        if ( newType == 0 )
            return false;

        System.out.println( "Was\n> " + TypeSelecter.get( newType ) + "\nCorrect?\n> " );
        String correct = in.nextLine();
        
        if ( correct.toLowerCase().equals("n") || correct.toLowerCase().equals("no") ) {
            System.out.println( "Please try again then." );
            return false;
        }

        var.type = TypeSelecter.get( newType );
        return true;
    }

    /**
     * This method changes the Value of the given Variable defined by the User.
     * @param var the Variable to be changed
     * @return True if the change was successful.
     */
    public static boolean changeVal( Variable var ) {
        System.out.print( "Enter " + var.name + "'s new value\n> ");
            
        String newVal = in.nextLine();
        System.out.print( "Was\n> " + newVal + "\nCorrect?\n> " );
        String correct = in.nextLine();
        
        if ( correct.toLowerCase().equals("n") || correct.toLowerCase().equals("no") ) {
            System.out.println( "Please try again then." );
            return false;
        }

        var.val = newVal;
        return true;
    }



}
