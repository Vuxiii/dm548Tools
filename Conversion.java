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
            // if ( c != ' ' ) {
                if ( c == '1' ) {
                    if ( power != 0 )
                        out = out + " + ";
                    out = out + "2^" + power;
                }
                ++power;
            // }
        }
        return out;
    }

    public static String binToTwos( String bin ) {
        String out = "";
        for ( char c : bin.toCharArray() ) {
            // if ( c != ' ' )
                out = out + flipBit( c );
            // else 
            //     out = out + " ";
        }
        // add one to it.
        int i = out.length() -1;
        while ( i >= 0 && out.charAt( i ) != '0' ) {
            // if ( out.charAt( i ) != ' ' )
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

        
        // if ( bin.length() > 4 ) {
        //     String bCopy = bin;
        //     bin = "";
        //     for ( int i = 0; i < bCopy.length(); i = i + 4 )
        //         bin = bin + bCopy.substring( i, i + 4 ) + " ";
        // }
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
        int sign = ( val < 0 ) ? -1 : 1;
        String significand = "";
        if ( sign == -1 )
            dou = dou.substring( 1 );
        
        return significand;
        
    }

    public static String IEEEtoDouble( String IEEE ) {
        int sign = IEEE.charAt( 0 ) == '0' ? 1 : -1;
        int exponent = Integer.parseInt( binToInt( IEEE.substring( 1, 9 ) ) );
        int biasedExp = exponent - 127;
        double significand = 1.0;
        String sig = IEEE.substring( 9 );
        for ( int i = 0; i < sig.length(); i++ ) {
            if ( sig.charAt( i ) == '1' )
                significand += Math.pow( 2, -(i + 1) );
        }
        double ret = sign * Math.pow(2, biasedExp) * significand;
        return "" + ret;
    }
}
