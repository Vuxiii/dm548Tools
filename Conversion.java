import java.util.HashMap;
import java.util.Map;

public class Conversion {
    public static Map<Character, String> hexMap = new HashMap<>();

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
            if ( c == '1' ) {
                if ( power != 0 )
                    out = out + " + ";
                out = out + "2^" + power;
            }
            ++power;
        }
        return out;
    }

    public static String binToTwos( String bin ) {
        String out = "";
        for ( char c : bin.toCharArray() ) {
            out = out + flipBit( c );
        }
        // add one to it.
        int i = out.length() -1;
        while ( i >= 0 && out.charAt( i ) != '0' ) {
            out = replaceChar( out, '0', i );
            --i;
        }
        if ( i >= 0 )
            out = replaceChar( out, '1', i );
        
        return out;
    }

    public static String replaceChar( String s, char c, int i ) {
        return s.substring( 0, i ) + c + s.substring( i + 1 );
    }

    public static char flipBit( char c ) {
        return c == '0' ? '1' : '0';
    }



    public static String intToBinary( String n ) {
        int val = Integer.parseInt( n );
        String bin = Integer.toBinaryString( val );
        if ( val < 0 ) {
            int bits = 0;
            while ( Math.pow( 2, bits ) <= Math.abs( val ) ) bits++;

            bin = bin.substring( bin.length() - bits - 1 );
        }

        int size = bin.length();

        while ( size % 4 != 0 ) ++size;

        bin = Arit.binSignExtend( bin, size, val < 0 );

        return bin;
    }

    public static String intToHex( String num ) {
        String hex = Integer.toHexString( Integer.parseInt( num ) ).toUpperCase();
        return hex;        
    }

    public static String hexToBinary( String hex ) {
        String out = "";
        for ( Character c : hex.toUpperCase().toCharArray() ) {
            out += hexMap.get( c );
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

    public static String doubleToInt( String dou ) {
        return "" + (int) Double.parseDouble( dou );
    }

    public static String doubleToIEEE( String dou ) {
        double val = Double.parseDouble( dou );

        // Find the sign.
        int sign = ( val < 0 ) ? -1 : 1;
        
        if ( sign == -1 )
            dou = dou.substring( 1 );
        if ( val < 0 ) val = val * -1;

        String significandG = doubleToIEEEUpper( dou );
        
        String significandL = doubleToIEEELower( dou );

        // Find biased exponent
        Integer biasedExpG = null;
        // Search through significandG first.
        for ( int i = 0; i < significandG.length(); ++i ) {
            if ( significandG.charAt(i) == '1' ) {
                biasedExpG = significandG.length() - (i + 1); 
                break;
            }
        }

        Integer biasedExpL = null;
        // Search through significandL if no possible exponent were found.
        for ( int i = 0; i < significandL.length(); ++i ) {
            if ( significandL.charAt(i) == '1' ) {
                biasedExpL = -(i + 1); 
                break;
            }
        }

        if ( biasedExpG != null )
            significandG = significandG.substring( significandG.length() - biasedExpG  );
        while ( significandG.length() != 23 ) significandG += "0";
        if ( biasedExpL != null )
            significandL = significandL.substring( Math.abs( biasedExpL ) );
        while ( significandL.length() != 23 ) significandL += "0";

        String exponentG = biasedExpG == null ? "00000000" : Arit.binSignExtend(intToBinary( "" + (biasedExpG + 127) ), 8, false);
        String exponentL = biasedExpL == null ? "00000000" : Arit.binSignExtend(intToBinary( "" + (biasedExpL + 127) ), 8, false);
        
        System.out.println( );
        
        String ieeeG = "0" + exponentG + significandG;
        String ieeeL = "0" + exponentL + significandL;
        System.out.println( "Greater");
        System.out.println( ieeeG );
        System.out.println( "Lower");
        System.out.println( ieeeL );
        
        String combined = Arit.IEEEaddv2( new Variable( "", ieeeG, Type.IEEE ), new Variable( "", ieeeL, Type.IEEE ) );
        
        return (sign == -1 ? "1" : "0") + combined.substring( 1 );
        
    }

    private static String doubleToIEEEUpper( String upper ) {
        double val = Double.parseDouble( upper );
        // Find the significand for the value > 0
        int power = 0;
        while ( Math.pow( 2, power ) <= Math.abs( val ) ) power++;
        String significandG = "0".repeat( power );
        {
            int currentBit = 0;
            while ( power != 0 ) {
                String intVal = binToInt( replaceChar( significandG, flipBit( significandG.charAt(currentBit) ), currentBit ) );

                if ( Integer.parseInt( intVal ) <= (int) val )
                    significandG = replaceChar( significandG, '1', currentBit );
                else
                significandG = replaceChar( significandG, '0', currentBit );
                --power;
                ++currentBit;
            }
        }
        return significandG;
    }

    private static String doubleToIEEELower( String lower ) {
        double val = Double.parseDouble( lower );
        // Find the significand for the value < 0
        String significandL = "";
        
        lower = "" + (val - (int) val);
        System.out.println( "Lower is actually\t" + lower );
        int usedBits = 0;
        double current = Double.parseDouble( lower );
        System.out.println( current );
        while ( usedBits != 23 ) {
            // System.out.print( current + " * 2 = " );
            current = current * 2.0;
            // System.out.println( current );
            if ( current >= 1 )
                significandL += "1";
            else 
                significandL += "0";
            if ( current >= 1.0 ) current = current - 1.0;
            ++usedBits;
            System.out.println( significandL );
        }
        return significandL;
        
    }


    public static String IEEEtoDouble( String IEEE ) {
        int sign = IEEE.charAt( 0 ) == '0' ? 1 : -1;
        int exponent = Integer.parseInt( binToInt( IEEE.substring( 1, 9 ) ) );
        int biasedExp = exponent - 127;
        Fraction significand = new Fraction( 1, 1 );
        String sig = IEEE.substring( 9 );
        for ( int i = 0; i < sig.length(); i++ ) {
            if ( sig.charAt( i ) == '1' ) {
                significand = significand.add( new Fraction( 1, (int) Math.pow(2, i+1 ) ) );
                // System.out.println( "Adding new Frac " + new Fraction( 1, (int) Math.pow(2, i+1 ) ).toString() );
            }
        }

        // System.out.println( binToInt( IEEE.substring( 1, 9 ) ) );
        // System.out.println( significand.value() );
        double ret = sign * Math.pow(2, biasedExp) * significand.value();
        return "" + ret;
    }
}
