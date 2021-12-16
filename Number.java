import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Number{
    public static Scanner in = new Scanner( System.in );

    public static Map< String, Variable > variables = new HashMap<>();

    public static Map<Character, String> hexMap = new HashMap<>();
    public static void main( String[] args ) {

        // System.out.println( intToTwos( -10 ) );

        // /**

        initHexMap();
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

            System.out.println( "\n\n\n====================================\n\n\n");
        } while ( choice != 0 );
        //  */
        in.close();
        // int num_decimal = Integer.parseInt( args[0] );
        // String num_hex = intToHex( num_decimal );
        // String num_bin = intToBinary( num_decimal );
        // String bin_calc = showCalcBin( num_bin );
        // String num_two = binToTwos( num_bin );
        // System.out.println( "Integer\t\t" + num_decimal );
        // System.out.println( "Hexidecimal\t" + num_hex );
        // System.out.println( "Binary\t\t" + num_bin + "\t" + bin_calc + " = " + num_decimal );
        // // System.out.println( "Binary\t\t" + Integer.toBinaryString( num_decimal ) );
        // System.out.println( "Two's Comp\t" + num_two );
    }
    
    /**
     * This method prints the Menu to the user.
     */
    public static void menu() {
        System.out.println( "The following options are available:" );
        System.out.println( "0] Exit" );
        System.out.println( "1] Make Variable" );
        System.out.println( "2] List Variables" );
        System.out.println( "3] Alter Variable" );
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
        if ( type == Type.Hex && !value.startsWith("0x") )
            value = "0x" + value.toUpperCase();
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

                if ( var1.type == var2.type ) {
                    int diff = Math.abs( var1.val.length() - var2.val.length() );
                    if ( var1.val.length() > var2.val.length() ) {

                        System.out.println( " " + var1.val );
                        System.out.println( "+" + " ".repeat( diff ) + var2.val );
                    } else {
                        System.out.println( " " + var2.val );
                        System.out.println( "+" + " ".repeat( diff ) + var1.val );
                    }
                    
                    System.out.println( "-" + "-".repeat( Math.max( var1.val.length(), var2.val.length() ) ) );
                }
            } break;
            case 3: {
                // booths();
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
            if ( targetType == Type.Binary )                return intToBinary( Integer.parseInt( var.val ) );
            else if ( targetType == Type.SignedBinary )     return (Integer.parseInt( var.val ) < 0 ) ? intToBinary( Integer.parseInt( var.val ) ) : binToTwos( intToBinary( Integer.parseInt( var.val ) ) );
            else if ( targetType == Type.Hex )              return intToHex( Integer.parseInt( var.val ) );
            else if ( targetType == Type.IEEE )             return null;
        } else if ( var.type == Type.Hex ) {
            if ( targetType == Type.Integer )               return "" + Long.parseLong( var.val.substring(2), 16 );
            else if ( targetType == Type.Binary )           return hexToBinary( var.val );
            else if ( targetType == Type.SignedBinary )     return binToTwos( hexToBinary( var.val ) );
            else if ( targetType == Type.IEEE )             return null;
        } else if ( var.type == Type.Binary ) {
            if ( targetType == Type.Integer )               return binToInt( var.val );
            else if ( targetType == Type.SignedBinary )     return binToTwos( var.val );
            else if ( targetType == Type.Hex )              return intToHex( Integer.parseInt( binToInt( var.val ) ) );
            else if ( targetType == Type.IEEE )             return null;
        } else if ( var.type == Type.SignedBinary ) {
            if ( targetType == Type.Integer )               return twosToInt( var.val );
            else if ( targetType == Type.Binary )           return intToBinary( Integer.parseInt( twosToInt( var.val ) ) );
            else if ( targetType == Type.Hex )              return intToHex( Integer.parseInt( twosToInt( var.val ) ) );
            else if ( targetType == Type.IEEE )             return null;
        } else if ( var.type == Type.IEEE ) {
            if ( targetType == Type.Integer )               return null;
            else if ( targetType == Type.Binary )           return null;
            else if ( targetType == Type.SignedBinary )     return null;
            else if ( targetType == Type.Hex )              return null;
        }
        return null;
    }

    public static void integerToBinary() {
        System.out.println( "" );
    }

    public static void initHexMap() {
        hexMap.put( '0', "0000" );
        hexMap.put( '1', "0001" );
        hexMap.put( '2', "0010" );
        hexMap.put( '3', "0011" );
        hexMap.put( '4', "0100" );
        hexMap.put( '5', "0101" );
        hexMap.put( '6', "0110" );
        hexMap.put( '7', "0111" );
        hexMap.put( '8', "1000" );
        hexMap.put( '9', "1001" );
        hexMap.put( 'A', "1010" );
        hexMap.put( 'B', "1011" );
        hexMap.put( 'C', "1100" );
        hexMap.put( 'D', "1101" );
        hexMap.put( 'E', "1110" );
        hexMap.put( 'F', "1111" );
    }

    public static String showCalcBin( String bin ) {
        // Reverse:
        String s = "";
        for ( char c : bin.toCharArray() )
            s = c + s;
        bin = s;
        // Compute the calc.
        int power = 0;
        String out = "";
        for ( char c : bin.toCharArray() ) {
            if ( c != ' ' ) {
                if ( c == '1' ) {
                    if ( power != 0 )
                        out = out + " + ";
                    out = out + "2^" + power;
                }
                ++power;
            }
        }
        return out;
    }

    public static String binToTwos( String bin ) {
        String out = "";
        for ( char c : bin.toCharArray() ) {
            if ( c != ' ' )
                out = out + flipBit( c );
            else 
                out = out + " ";
        }
        // add one to it.
        int i = out.length() -1;
        while ( i >= 0 && out.charAt( i ) != '0' ) {
            if ( out.charAt( i ) != ' ' )
                out = replaceChar( out, '0', i );
            --i;
        }
        
        out = replaceChar( out, '1', i );
        
        return out;
    }

    public static String replaceChar( String s, char c, int i ) {
        return s.substring( 0, i ) + c + s.substring( i + 1 );
    }

    public static char flipBit( char c ) {
        return c == '0' ? '1' : '0';
    }



    public static String intToBinary( int n ) {
        String bin = Integer.toBinaryString( n );
        while ( bin.length() % 4 != 0 ) {
            bin = "0" + bin;
        }
        if ( bin.length() > 4 ) {
            String bCopy = bin;
            bin = "";
            for ( int i = 0; i < bCopy.length(); i = i + 4 )
                bin = bin + bCopy.substring( i, i + 4 ) + " ";
        }
        return bin;
    }

    public static String intToHex( int num ) {
        String hex = "0x" + Integer.toHexString( num ).toUpperCase();
        return hex;        
    }

    public static String hexToBinary( String hex ) {
        String out = "";
        for ( Character c : hex.substring(2).toUpperCase().toCharArray() ) {
            out += hexMap.get( c ) + " ";
        }
        return out;
    }

    public static String twosToInt( String twos ) {
        int val = 0;
        int power = 0;
        for ( int i = twos.length()-1; i > 0; --i ) {
            val = val + ( twos.charAt( i ) == '1'  ? (int) Math.pow( 2, power ) : 0);
            power ++;
        }
        if ( twos.charAt(0) == '1' )
            val = val - (int) Math.pow( 2, power );
        
        return "" + val;
    }

    public static String binToInt( String bin ) {
        int val = 0;
        int power = 0;
        for ( int i = bin.length()-1; i >= 0; --i ) {
            val = val + ( bin.charAt( i ) == '1'  ? (int) Math.pow( 2, power ) : 0);
            power ++;
        }
        return "" + val;
    }
}