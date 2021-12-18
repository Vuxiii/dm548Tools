import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Number{
    public static Scanner in = new Scanner( System.in );

    public static Map< String, Variable > variables = new HashMap<>();

    
    public static void main( String[] args ) {

        // System.out.println( intToTwos( -10 ) );
        // System.out.println( Arit.binAdd( "0111", "0001" ) );
        // /**
        System.out.println( Integer.parseInt( "-1" ) );
        Conversion.initHexMap();
        int choice = 0;
        do {
            menu();
            choice = in.nextInt();
            in.nextLine();

            switch (choice) {
                case 1: { // Make variable
                    newVariable();
                } break;
                case 2: { // List Variables
                    listVariables();
                } break;
                case 3: { // Alter variable
                    alterVariable();
                } break;
                case 4: { // Variable Math
                    variableMath();
                } break;
                case 5: { // Integer to Binary
                    integerToBinary();
                } break;
                case 6: {

                } break;
                default:
                    break;
            }

            System.out.println( "\n\n\n=======================================\n\n\n");
        } while ( choice != 0 );
        //  */
        in.close();
    }
    
    /**
     * This method prints the Menu to the user.
     */
    public static void menu() {
        System.out.println( "The following options are available:" );
        System.out.println( "0] Exit\n" );
        System.out.println( "1] Make Variable" );
        System.out.println( "2] List Variables" );
        System.out.println( "3] Edit Variable\n" );
        System.out.println( "4] Variable Math" );
        System.out.println( "5] Integer to Binary" );

        System.out.print( "> " );

    }

    /**
     * This method creates a new Variable defined by the user.
     */
    public static void newVariable() {
        System.out.print( "Enter a name for your variable:\n> " );
        String name = in.nextLine();
        System.out.println( "Select a type:" );
        TypeSelecter.printAll();

        System.out.print( "> " );
        int choice = in.nextInt();
        in.nextLine();
        Type type = TypeSelecter.get( choice );        
        
        System.out.print( "Enter your " + type + ":\n> " );
        String value = in.nextLine();
        variables.put( name, new Variable( name, value, type ) );
        System.out.println( "Made variable:\n" + variables.get( name ).toString() );
    }

    /**
     * This method prints all of the created Variables to the screen.
     */
    public static void listVariables() {
        System.out.println( "The stored variables are as follows:\n" );
        for ( String key : variables.keySet() )
            System.out.println( variables.get( key ).toString() );
    }

    public static Variable selectVariable() {
        System.out.println( "0] Back" );
        int i = 1;
        Map< Integer, String > variable = new HashMap<>();
        for ( String key : variables.keySet() ) {
            variable.put( i, key );
            System.out.println( i + "] " + variables.get( key ).toString() );
            ++i;
        }
        System.out.print( "> " );
        
        int choice = in.nextInt();
        in.nextLine();
        if ( choice == 0 )
            return null;
    
        
        System.out.println( "The chosen variable was\n> " + variables.get(variable.get( choice )).toString() );
        
        return variables.get( variable.get( choice ) );
    }

    /**
     * This changes an already existing variable.
     * The following options are available:
     * 1] Change value
     * 2] Change type
     * 3] Change value AND type
     * 4] Remove
     */
    public static void alterVariable() {
        System.out.println( "Which Variable do you wish to alter?\n" );
        Variable var = selectVariable();
        if ( var == null ) return;
        String variKey = var.name;

        System.out.println( "The following options are available:" );
        System.out.println( "0] Back" );
        System.out.println( "1] Change value" );
        System.out.println( "2] Change type" );
        System.out.println( "3] Change value AND type" );
        System.out.println( "4] Remove" );

        System.out.print( "> " );
        int choice = in.nextInt();
        in.nextLine();
        Variable original = var.copy();
        switch (choice) {
            case 1: { // Change value
                if ( !Variable.changeVal( var ) )
                    var = original;
                System.out.println( variKey + " has been changed:\n> " + var.toString() );
            } break;
            case 2: { // type
                if ( !Variable.changeType( var ) )
                    var = original;
                    System.out.println( variKey + " has been changed:\n> " + var.toString() + ": " + var.type );
            } break;
            case 3: { // Change value AND type
                if ( !(Variable.changeVal( var ) && Variable.changeType( var )) )
                    var = original;
                System.out.println( variKey + " has been changed:\n> " + var.toString() + ": " + var.type );
            } break;
            case 4: { // Remove
                System.out.print( "Are you sure you want to remove " + var.toString() + "\n> " );
                String correct = in.nextLine();
                if ( correct.toLowerCase().equals("n") || correct.toLowerCase().equals("no") )
                    return;
                variables.remove( variKey );
                System.out.println( var.toString() + " was removed!" );
            } break;
            default:
                return;
        }
    }

    public static void variableMath() {
        System.out.println( "Which operation do you wish to perform?" );
        System.out.println( "0] Back" );
        System.out.println( "1] Convert Variable into another type" );
        System.out.println( "2] Add two Variables together" );
        System.out.println( "3] Booth's Algorithm" );
        System.out.println( "4] Display Conversions" );

        System.out.print( "> " );
        int choice = in.nextInt();
        in.nextLine();
        switch (choice) {
            case 1: {
                // Select variable to convert.
                System.out.println( "Enter which Variable you wish to convert?\n" );
                Variable var = selectVariable();
                if ( var == null ) return;
                // Select the desired target type.
                System.out.println( "Enter which Type you wish to convert into:\n");
                TypeSelecter.printAll();
                System.out.print( "> " );
                int t = in.nextInt();
                in.nextLine();

                Type type = TypeSelecter.get( t );
                String newVal = convertVariable( var, type );
                // Variable newVar = new Variable(name, val, type)
                System.out.println( "Do you wish to override\n> " + var.toString() + "\nTo\n> " + var.name + " = " + newVal + " [" + type + "]" );
                System.out.print( "\n> " );
                String correct = in.nextLine();

                if ( correct.toLowerCase().equals("y") || correct.toLowerCase().equals("yes") ) {
                    var.type = type;
                    var.val = newVal;
                    System.out.println( "Changed " + var.name + " to:\n> " + var.toString() );
                }

            } break;
            case 2: {
                System.out.println( "Enter which Variable you would like to use for the addition:\n" );
                Variable var1 = selectVariable();
                System.out.println( "Enter the second Variable you would like to use for the addition:\n" );
                Variable var2 = selectVariable();
                boolean sameType = var1.type == var2.type;
                boolean binAdd = ( (var1.type == Type.Binary || var1.type == Type.SignedBinary) && (var2.type == Type.Binary || var2.type == Type.SignedBinary) );
                if ( sameType || binAdd ) {
                    int diff = Math.abs( var1.val.length() - var2.val.length() );
                    if ( var1.val.length() > var2.val.length() ) {

                        System.out.println( "\n " + var1.val );
                        System.out.println( "+" + " ".repeat( diff ) + var2.val );
                    } else {
                        System.out.println( " " + var2.val );
                        System.out.println( "+" + " ".repeat( diff ) + var1.val );
                    }
                    
                    System.out.println( "-" + "-".repeat( Math.max( var1.val.length(), var2.val.length() ) ) );

                    if ( binAdd )
                        System.out.println( " " + Arit.binAdd( var1, var2, false ) );
                    else if ( var1.type == Type.Integer )
                        System.out.println( " " + (Integer.parseInt( var1.val ) + Integer.parseInt( var2.val )) );
                    else if ( var1.type == Type.Double )
                        System.out.println( " " + (Double.parseDouble( var1.val ) + Double.parseDouble( var2.val )) );
                }
            } break;
            case 3: {
                System.out.println( "Booth's Algorithm\nEnter Binary number for M:\n" );
                Variable M = selectVariable();
                System.out.println( "Enter Binary number for Q:\n" );
                Variable Q = selectVariable();
                Arit.booths( M, Q );
            } break;
            case 4: {
                // Display All conversions
                System.out.println( "Enter which Variable you wish to Display?\n" );
                Variable var = selectVariable();
                if ( var == null ) return;

                System.out.println( "Can be converted as follows:\n" );
                if ( var.type != Type.Hex )
                    System.out.println( var.name + " = " + convertVariable( var, Type.Hex ) + " [" + Type.Hex + "]" );
                if ( var.type != Type.Integer )
                    System.out.println( var.name + " = " + convertVariable( var, Type.Integer ) + " [" + Type.Integer + "]" );
                if ( var.type != Type.Double )
                    System.out.println( var.name + " = " + convertVariable( var, Type.Double ) + " [" + Type.Double + "]" );
                if ( var.type != Type.Binary )
                    System.out.println( var.name + " = " + convertVariable( var, Type.Binary ) + " [" + Type.Binary + "]" );
                if ( var.type != Type.SignedBinary )
                    System.out.println( var.name + " = " + convertVariable( var, Type.SignedBinary ) + " [" + Type.SignedBinary + "]" );
                if ( var.type != Type.IEEE )
                    System.out.println( var.name + " = " + convertVariable( var, Type.IEEE ) + " [" + Type.IEEE + "]" );
                
            } break;
            default:
                break;
        }
    }

    public static String convertVariable( Variable var, Type targetType ) {
        // System.out.println( "From\t" + var.type + "\nTo\t" + targetType );
        if ( var.type == Type.Integer ) {
            if ( targetType == Type.Double )                return "" + Double.parseDouble( var.val );
            else if ( targetType == Type.Binary )           return Conversion.intToBinary( var.val  );
            else if ( targetType == Type.SignedBinary )     return (Integer.parseInt( var.val )  < 0 ) ? Conversion.intToBinary( var.val  ) : Conversion.binToTwos( Conversion.intToBinary( var.val  ) );
            else if ( targetType == Type.Hex )              return Conversion.intToHex( var.val  );
            else if ( targetType == Type.IEEE )             return null;
        } else if ( var.type == Type.Hex ) {
            if ( targetType == Type.Integer )               return "" + Long.parseLong( var.val.substring(2), 16 );
            else if ( targetType == Type.Double )           return "" + Double.parseDouble( "" + Long.parseLong( var.val.substring(2), 16 ) );
            else if ( targetType == Type.Binary )           return Conversion.hexToBinary( var.val );
            else if ( targetType == Type.SignedBinary )     return Conversion.binToTwos( Conversion.hexToBinary( var.val ) );
            else if ( targetType == Type.IEEE )             return null;
        } else if ( var.type == Type.Binary ) {
            if ( targetType == Type.Integer )               return Conversion.binToInt( var.val );
            else if ( targetType == Type.Double )           return "" + Double.parseDouble( Conversion.binToInt( var.val ) );
            else if ( targetType == Type.SignedBinary )     return Conversion.binToTwos( var.val );
            else if ( targetType == Type.Hex )              return Conversion.intToHex( Conversion.binToInt( var.val ) );
            else if ( targetType == Type.IEEE )             return null;
        } else if ( var.type == Type.SignedBinary ) {
            if ( targetType == Type.Integer )               return Conversion.twosToInt( var.val );
            else if ( targetType == Type.Double )           return "" + Double.parseDouble( Conversion.twosToInt( var.val ) );
            else if ( targetType == Type.Binary )           return Conversion.intToBinary( Conversion.twosToInt( var.val ) );
            else if ( targetType == Type.Hex )              return Conversion.intToHex( Conversion.twosToInt( var.val ) );
            else if ( targetType == Type.IEEE )             return null;
        } else if ( var.type == Type.IEEE ) {
            if ( targetType == Type.Integer )               return Conversion.doubleToInt( Conversion.IEEEtoDouble( var.val ) );
            else if ( targetType == Type.Double )           return Conversion.IEEEtoDouble( var.val );
            else if ( targetType == Type.Binary )           return Conversion.intToBinary( Conversion.doubleToInt( Conversion.IEEEtoDouble( var.val ) ) );
            else if ( targetType == Type.SignedBinary )     return Conversion.binToTwos( Conversion.intToBinary( Conversion.doubleToInt( Conversion.IEEEtoDouble( var.val ) ) ) );
            else if ( targetType == Type.Hex )              return Conversion.intToHex( Conversion.doubleToInt( Conversion.IEEEtoDouble( var.val ) ) );
        } else if ( var.type == Type.Double ) {
            if ( targetType == Type.Integer )               return Conversion.doubleToInt(  var.val );
            else if ( targetType == Type.Binary )           return Conversion.intToBinary( Conversion.doubleToInt( var.val ) );
            else if ( targetType == Type.SignedBinary )     return (Integer.parseInt( var.val )  < 0 ) ? Conversion.intToBinary( Conversion.doubleToInt( var.val ) ) : Conversion.binToTwos( Conversion.intToBinary( Conversion.doubleToInt( var.val ) ) );
            else if ( targetType == Type.Hex )              return Conversion.intToHex( Conversion.doubleToInt( var.val ) );
            else if ( targetType == Type.IEEE )             return null;
        }
        return null;
    }

    public static void integerToBinary() {
        System.out.println( "" );
    }

    
}