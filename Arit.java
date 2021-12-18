public class Arit {
    private static final String BOOTHS_ADD = "A=A+M";
    private static final String BOOTHS_SUBTRACT = "A=A-M";
    private static final String BOOTHS_SHIFT = "Shift";
    public static String booths( Variable varM, Variable varQ ) {
        String out = "";

        String M = varM.val;
        String M_neg = Conversion.binToTwos( M );
        String A = "0".repeat(varM.val.length());
        String Q = varQ.val;
        String Q_1 = "0";
        String event = "Inital";
        int totalIterations = varM.val.length();
        String lineBreak =    "---------------------------------------";
        System.out.println( "\n================BOOTH'S================");
        System.out.println( " M\t" + M );
        System.out.println( "-M\t" + M_neg );
        System.out.println( "\nCycle\tA\tQ\tQ_1\tEvent");
        System.out.println( 0 + "\t" + A + "\t" + Q + "\t" + Q_1 + "\t" + event );
        System.out.println( lineBreak );
        for ( int cycle = 1; cycle != totalIterations+1; ++cycle ) {

            event = BoothsGetAction( Q.charAt( Q.length()-1 ) + Q_1 );
            System.out.println( cycle + "\t" + A + "\t" + Q + "\t" + Q_1 + "\tQQ_1=" + (Q.charAt( Q.length()-1 ) + Q_1) );

            
            switch (event) {
                case BOOTHS_SUBTRACT: {
                    A = BoothsAdd( M_neg, A );
                    System.out.println( "\t" + A + "\t" + Q + "\t" + Q_1 + "\t" + event );
                } break;
                case BOOTHS_ADD: {
                    A = BoothsAdd( M, A );
                    System.out.println( "\t" + A + "\t" + Q + "\t" + Q_1 + "\t" + event );
                } break;
            
                default: break; // Perform shift.  
            }
            

            // Perform shift.
            String comb = BoothsShift( A, Q, Q_1 );
            A = BoothsGetA( comb, A.length() );
            Q = BoothsGetQ( comb, Q.length() );
            Q_1 = BoothsGetQ_( comb );
            event = "Shift";
            System.out.println( "\t" + A + "\t" + Q + "\t" + Q_1 + "\t" + event );
            System.out.println( lineBreak );
        }
        System.out.println( "Result\t" + A + " " + Q);
        System.out.println( "Decimal\t" + Conversion.twosToInt( A + Q ) );
        return A + Q;
    }

    private static String BoothsAdd( String binary, String A ) {
        System.out.println( "\t" + binary );
        System.out.println( "\t" + "-".repeat(binary.length()) );        
        return binAdd( new Variable("", binary, Type.Binary ), new Variable("", A, Type.Binary ), false );
    }

    private static String BoothsShift( String A, String Q, String Q_ ) {
        String combined = A + Q + Q_;
        String out = "";
        for ( int i = combined.length()-2; i >= 0; --i ) {
            char c = combined.charAt( i );
            out = c + out;
        }
        out = out.charAt(0) + out;

        return out;
    }

    

    private static String BoothsGetA( String comb, int size ) {
        return comb.substring( 0, size );
    }
    private static String BoothsGetQ( String comb, int size ) {
        return comb.substring( size, size * 2 );
    }
    private static String BoothsGetQ_( String comb ) {
        return "" + comb.charAt( comb.length() - 1 );
    }

    private static String BoothsGetAction( String reg ) {
        switch (reg) {
            case "10":  return BOOTHS_SUBTRACT;
            case "01":  return BOOTHS_ADD; 
            default:    return BOOTHS_SHIFT; 
        }
    }

    public static String binAdd( Variable var1, Variable var2, boolean showOverflow ) {
        String bin1 = var1.val;
        String bin2 = var2.val;
        if ( bin1.length() > bin2.length() ) bin2 = binSignExtend( bin2, bin1.length(), var1.type == Type.SignedBinary );
        if ( bin2.length() > bin1.length() ) bin1 = binSignExtend( bin1, bin2.length(), var1.type == Type.SignedBinary );

        String out = "";
        boolean carry = false;

        for ( int i = bin1.length()-1; i >= 0; --i ) {
            char b1 = bin1.charAt( i );
            char b2 = bin2.charAt( i );
            char bResult = '?';
            if ( b1 == '1' ) {
                if ( b2 == '1' ) {
                    if ( carry ) {              // 1 + 1 + 1
                        carry = true;
                        bResult = '1';
                    } else {                    // 1 + 1 + 0
                        carry = true;
                        bResult = '0';
                    }
                } else {
                    if ( carry ) {              // 1 + 0 + 1
                        carry = true;
                        bResult = '0';
                    } else {                    // 1 + 0 + 0
                        carry = false;
                        bResult = '1';
                    }
                }
            } else {
                if ( b2 == '1' ) {
                    if ( carry ) {              // 0 + 1 + 1
                        carry = true;
                        bResult = '0';
                    } else {                    // 0 + 1 + 0
                        carry = false;
                        bResult = '1';
                    }
                } else {
                    if ( carry ) {              // 0 + 0 + 1
                        carry = false;
                        bResult = '1';
                    } else {                    // 0 + 0 + 0
                        carry = false;
                        bResult = '0';
                    }
                }
            }
            out = bResult + out;
        }
        if ( showOverflow && bin1.charAt(0) == bin2.charAt(0) && bin1.charAt(0) != out.charAt(0) ) System.out.println( "Binary Overflow caused by addition\n " + bin1 + "\n+" + bin2 );
        return out;
    }

    public static String binSignExtend( String bin, int len, boolean isSigned ) {
        char sign = bin.charAt( 0 );
        if ( !isSigned ) sign = '0';
        int bitsToAdd = len - bin.length();
        for ( int i = 0; i != bitsToAdd; ++i )
            bin = sign + bin;
        return bin;
    }
}
