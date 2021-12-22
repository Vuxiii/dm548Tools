public class Arit {
    private static class Booth {
        private static final String BOOTHS_ADD = "A=A+M";
        private static final String BOOTHS_SUBTRACT = "A=A-M";
        private static final String BOOTHS_SHIFT = "Shift";
        public static String calc( Variable varM, Variable varQ ) {
            String M = varM.val;
            String M_neg = Conversion.binToTwos( M );
            String A = "0".repeat(varM.val.length());
            String Q = varQ.val;
            String Q_1 = "0";
            String event = "Inital";
            int totalIterations = varM.val.length();
            String tabs = (M.length() > 4 ? "\t\t" : "\t" );
            String lineBreak = "-".repeat( 8*5 + (M.length() > 4 ? 2*8 : 0));
            System.out.println( "\n================BOOTH'S================");
            System.out.println( " M\t" + M );
            System.out.println( "-M\t" + M_neg );
            System.out.println( "\nCycle\tA" + tabs + "Q" + tabs + "Q_1\tEvent");
            System.out.println( 0 + "\t" + A + "\t" + Q + "\t" + Q_1 + "\t" + event );
            System.out.println( lineBreak );
            for ( int cycle = 1; cycle != totalIterations+1; ++cycle ) {
    
                event = BoothsGetAction( Q.charAt( Q.length()-1 ) + Q_1 );
                System.out.println( cycle + "\t" + A + "\t" + Q + "\t" + Q_1 + "\tQQ_1=" + (Q.charAt( Q.length()-1 ) + Q_1) );
    
                
                switch (event) {
                    case BOOTHS_SUBTRACT: {
                        A = BoothsAdd( M_neg, A ).val;
                        System.out.println( "\t" + A + "\t" + Q + "\t" + Q_1 + "\t" + event );
                    } break;
                    case BOOTHS_ADD: {
                        A = BoothsAdd( M, A ).val;
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
    
        private static BinaryResult BoothsAdd( String binary, String A ) {
            System.out.println( "\t" + binary );
            System.out.println( "\t" + "-".repeat(binary.length()) );        
            return binAdd( new Variable("", binary, Type.Binary ), new Variable("", A, Type.Binary ) );
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
    
    }
    
    public static String booths( Variable varM, Variable varQ ) {
        return Booth.calc( varM, varQ );
    }

    public static BinaryResult binAdd( Variable var1, Variable var2 ) {
        String bin1 = var1.val;
        String bin2 = var2.val;
        // System.out.println( "BinsB4 Sign\n" + bin1 + "\n" + bin2 );
        if ( bin1.length() > bin2.length() ) bin2 = binSignExtend( bin2, bin1.length(), var2.type == Type.SignedBinary );
        if ( bin2.length() > bin1.length() ) bin1 = binSignExtend( bin1, bin2.length(), var1.type == Type.SignedBinary );
        // System.out.println( "Adding\n" + bin1 + "\n" + bin2 );
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
        // if ( showOverflow &&  ) System.out.println( "Binary Overflow caused by addition\n " + bin1 + "\n+" + bin2 );
        boolean overflow = bin1.charAt(0) == bin2.charAt(0) && bin1.charAt(0) != out.charAt(0);
        return new BinaryResult( out, overflow ? OverUnderFlow.Overflow : null ) ;
    }

    public static String binSignExtend( String bin, int len, boolean isSigned ) {
        char sign = bin.charAt( 0 );
        if ( !isSigned ) sign = '0';
        int bitsToAdd = len - bin.length();
        for ( int i = 0; i != bitsToAdd; ++i )
            bin = sign + bin;
        return bin;
    }


    public static String IEEEaddv3( Variable varX, Variable varY ) {

        // Check for one of the two's being 0
        if ( !varX.val.contains( "1" ) ) return varY.val;
        if ( !varY.val.contains( "1" ) ) return varX.val;


        // Step one. Align exponents & significand.
        String[] vars = IEEEalignExponents( varX, varY );
        if ( vars[1] == null ) return vars[0]; 
        String Z_exponent = vars[0].substring(0, 8);

        // Step two. Add significands.
        // adding the sign bit to the front. Making them twos complement.
        String correctXSig = varX.val.charAt(0) == '1' ? Conversion.binToTwos( "0" + vars[0].substring(8) ) : "0" + vars[0].substring(8);
        String correctYSig = varY.val.charAt(0) == '1' ? Conversion.binToTwos( "0" + vars[1].substring(8) ) : "0" + vars[1].substring(8);

        Variable X = new Variable( "X", correctXSig, Type.SignedBinary );
        Variable Y = new Variable( "Y", correctYSig, Type.SignedBinary );
        BinaryResult rs = binAdd( X, Y );
        String Z_sign = rs.val.charAt(0) + "";
        
        String Z_significand = rs.val.substring( 1 );
        
        // Overflow?
        if ( rs.overflow == OverUnderFlow.Overflow ) {
            IEEEshiftSignificand( Z_significand, true );
            BinaryResult ts = IEEEincrementExponent( Z_exponent, 1 );
            if ( ts.overflow == OverUnderFlow.Overflow ) return "EXPONENT_OVERFLOW";
        }

        // Step three. Normalize the significand.
        while ( !Z_significand.startsWith( "1" ) ) {
            
            Z_significand = IEEEshiftSignificand( Z_significand, false );
            BinaryResult ts = IEEEincrementExponent( Z_exponent, -1 );
            Z_exponent = ts.val;

            if ( ts.overflow == OverUnderFlow.Underflow ) return "EXPONENT_UNDERFLOW";
        }
        // System.out.println( "Sign\t" + Z_sign );
        // System.out.println( "Exp\t" + Z_exponent );
        // System.out.println( "Sign\t" + Z_significand );
        return Z_sign + Z_exponent + Z_significand.substring(1);

    }

    /**
     * This method aligns the two given IEEE's exponents, shifting the corresponding significand.
     * This method adds the leading "1" to the significands.
     * The first 8 bits are the exponents, the remaining is the significand.
     * @param X
     * @param Y
     * @return corrected X and corrected Y in that order.
     */
    public static String[] IEEEalignExponents( Variable X, Variable Y ) {
        String X_exponent = X.val.substring( 1, 9 );
        String Y_exponent = Y.val.substring( 1, 9 );
        String X_significand = "1" + X.val.substring( 9 );
        String Y_significand = "1" + Y.val.substring( 9 );
        
        if ( Integer.parseInt( Conversion.binToInt( X_exponent ) ) < Integer.parseInt( Conversion.binToInt( Y_exponent ) ) ) {
            // Increase x exponent
            // Decrease x significand
            while ( !X_exponent.equals( Y_exponent ) ) {
                X_exponent = IEEEincrementExponent( X_exponent, 1 ).val;
                X_significand = IEEEshiftSignificand( X_significand, true );
                if ( !X_significand.contains( "1" ) ) return new String[] { Y.val, null };
            }
        } else if ( Integer.parseInt( Conversion.binToInt( X_exponent ) ) > Integer.parseInt( Conversion.binToInt( Y_exponent ) ) ) {
            // Increase y exponent
            // Decrease y significand
            while ( !Y_exponent.equals( X_exponent ) ) {
                Y_exponent = IEEEincrementExponent( Y_exponent, 1 ).val;
                Y_significand = IEEEshiftSignificand( Y_significand, true );
                if ( !Y_significand.contains( "1" ) ) return new String[] { X.val, null };
            }
        }
        return new String[] { X_exponent + X_significand, Y_exponent + Y_significand };
    } 

    public static String IEEEaddv2( Variable varX, Variable varY ) {
        System.out.println( "IEEEv2");
        System.out.println( varX.val );
        System.out.println( varY.val );
        String X_sign = varX.val.substring( 0, 1 );
        String X_exponent = varX.val.substring( 1, 9 );
        String X_significand = "1" + varX.val.substring( 9 ); // OBS the implicit 1 has been made explicit!
        String Y_sign = varY.val.substring( 0, 1 );
        String Y_exponent = varY.val.substring( 1, 9 );
        String Y_significand = "1" + varY.val.substring( 9 ); // OBS the implicit 1 has been made explicit!

        if ( !varX.val.contains("1") ) return varY.val;
        if ( !varY.val.contains("1") ) return varX.val;

        boolean X_expIsLowest = Integer.parseInt( Conversion.binToInt( X_exponent ) ) < Integer.parseInt( Conversion.binToInt( Y_exponent ) );
        System.out.println( (X_expIsLowest ? "X" : "Y") + " is lowest!");
        System.out.println( (X_expIsLowest ? X_exponent : Y_exponent ) );
        System.out.println( (X_expIsLowest ? X_significand : Y_significand ) );
        // Exponent equal?
        while ( !X_exponent.equals( Y_exponent ) ) {
            if ( X_expIsLowest ) {
                // Increment smaller exponent
                X_exponent = IEEEincrementExponent( X_exponent, 1 ).val;
                // Shift significand right.
                X_significand = IEEEshiftSignificand( X_significand, true );
                // Significand == 0? return the other value
                if ( !X_significand.contains( "1" ) ) return varY.val;
            } else {
                // Increment smaller exponent
                Y_exponent = IEEEincrementExponent( Y_exponent, 1 ).val;
                // Shift significand right.
                Y_significand = IEEEshiftSignificand( Y_significand, true );
                // Significand == 0? return the other value
                if ( !Y_significand.contains( "1" ) ) return varX.val;
            }
            System.out.println( (X_expIsLowest ? X_exponent : Y_exponent ) );
            System.out.println( (X_expIsLowest ? X_significand : Y_significand ) );
        }
        String Z_exponent = X_exponent;
        System.out.println( "The final exponent\t" + Z_exponent );
        // Add signed significands.
        Variable X_significandTwos = new Variable( "X", ( X_sign.equals( "1" ) ? Conversion.binToTwos( X_significand ) : X_significand ), X_sign.equals( "1" ) ? Type.SignedBinary : Type.Binary );
        Variable Y_significandTwos = new Variable( "Y", ( Y_sign.equals( "1" ) ? Conversion.binToTwos( Y_significand ) : Y_significand ), Y_sign.equals( "1" ) ? Type.SignedBinary : Type.Binary );

        BinaryResult Z_significandResult = Arit.binAdd( X_significandTwos, Y_significandTwos );
        String Z_significand = Z_significandResult.val;
        
        System.out.println( " " + X_significandTwos.val );
        System.out.println( "+" + Y_significandTwos.val );
        System.out.println( " " + Z_significand );

        if ( !Z_significand.contains( "1" ) ) return "0".repeat(32);

        if ( Z_significandResult.overflow == OverUnderFlow.Overflow ) {
            // Shift Zsignificand right
            Z_significand = IEEEshiftSignificand( Z_significand, true );
            // Increment Zexponent
            BinaryResult res = IEEEincrementExponent( Z_exponent, 1 );
            Z_exponent = res.val;

            if ( res.overflow == OverUnderFlow.Overflow ) {
                System.out.println( "FOUND OVERFLOW:...");
                return "Exponent Overflow\n" + Z_exponent;
            }
        }
        System.out.println( "Checking normalized." );
        String Z_sign = Z_significand.substring( 0, 1 );
        Z_significand = Z_significand.substring( 1 );

        // Result Normalized?
        while ( Z_significand.charAt(0) != '1' ) {
            // Shift significand left
            Z_significand = IEEEshiftSignificand( Z_significand, false );
            // Decrease Z_exponent 1
            BinaryResult res = IEEEincrementExponent( Z_exponent, -1 );
            if ( res.overflow == OverUnderFlow.Underflow ) {
                System.out.println( "Found Underflow......");
                return ("Exponent Underflow\n" + Z_exponent);
            }
            Z_exponent = res.val;
            System.out.println( "New Sig\t" + Z_significand );
            System.out.println( "New Exp\t" + Z_exponent );
        } 
        System.out.println( "Final");
        System.out.println( Z_exponent );
        System.out.println( Z_significand );
        return Z_sign + Z_exponent + Z_significand;

    }

    private static BinaryResult IEEEincrementExponent( String exponent, int val ) {
        // int exponentInInt = Integer.parseInt( Conversion.binToInt( exponent ) ) + val;
        // return Conversion.intToBinary( "" + exponentInInt );
        Variable exp = new Variable ( "", exponent, Type.Binary );
        Variable valBin = new Variable( "", Conversion.intToBinary( "" + val ), val < 0 ? Type.SignedBinary : Type.Binary );
        return binAdd( exp, valBin );
    }

    private static String IEEEshiftSignificand( String significand, boolean shiftRight ) {
        if ( shiftRight )
            return "0" + significand.substring(0, significand.length()-1);
        else
            return significand.substring( 1 ) + "0";
    }

    public static String IEEEadd( Variable varA, Variable varB ) {
        if ( !varA.val.contains( "1" ) ) return varB.val;
        if ( !varB.val.contains( "1" ) ) return varA.val;
        String A_sign = varA.val.charAt(0) + "";
        String B_sign = varB.val.charAt(0) + "";
        String A_exponent = varA.val.substring( 1, 9 );
        String B_exponent = varB.val.substring( 1, 9 );
        String A_significand = "1" + varA.val.substring( 9 );
        String B_significand = "1" + varB.val.substring( 9 );

        if ( varA.val.equals( varB.val ) ) {
            A_exponent = Conversion.intToBinary( "" + (Integer.parseInt(Conversion.binToInt( A_exponent )) + 1) );
            return A_sign + A_exponent + A_significand.substring( 1 );
        }
        // Find common exponent. Smaller should be changed to become the greater
        int AInt_exp = Integer.parseInt( Conversion.binToInt( A_exponent ) );
        int BInt_exp = Integer.parseInt( Conversion.binToInt( B_exponent ) );
        String newExponent = "";
        if ( AInt_exp != BInt_exp ) {
            int changes = 0;
            if ( AInt_exp < BInt_exp ) {
                newExponent = A_exponent;
                do {
                    newExponent = Conversion.intToBinary( "" + (AInt_exp + (++changes)) );
                } while ( !newExponent.equals( B_exponent ) );
                A_significand = ("0".repeat( changes ) + A_significand).substring(0, 24);
                // System.out.println( "Shifted significand\t" + A_significand );
                // System.out.println( "New exponent for A\t" + newExponent );
                
            } else if ( BInt_exp < AInt_exp ) {
                newExponent = B_exponent;
                do {
                    newExponent = Conversion.intToBinary( "" + (BInt_exp + (++changes)) );
                } while ( !newExponent.equals( A_exponent ) );
                B_significand = ("0".repeat( changes ) + B_significand).substring(0, 24);
                // System.out.println( "Shifted significand\t" + B_significand );
                // System.out.println( "New exponent for B\t" + newExponent );
            }
        } else 
            newExponent = A_exponent;
        // Add significands
        // System.out.println( "Adding significands" );
        Variable sigA = new Variable( "A", (A_sign.equals("1") ? Conversion.binToTwos( (A_significand) ) : (A_significand) ), A_sign.equals("1") ? Type.SignedBinary : Type.Binary );
        Variable sigB = new Variable( "B", (B_sign.equals("1") ? Conversion.binToTwos( (B_significand) ) : (B_significand) ), A_sign.equals("1") ? Type.SignedBinary : Type.Binary );
        System.out.println( sigA );
        System.out.println( sigB );
        String newSignificand = binAdd( sigA, sigB ).val;
        
        System.out.println( "D = " + newSignificand );
        // Shift significand to right & increase exponent
        String newSign = newSignificand.charAt(0) + "";
        newSignificand = newSignificand.substring( 1 );
        // System.out.println( "New Sign\t" + newSign );
        // System.out.println( "New Sig\t" + newSignificand );
        Integer shifts = null;
        // Search through significand.
        for ( int i = 0; i < newSignificand.length() && shifts == null; ++i ) {
            if ( newSignificand.charAt(i) == '1' )
                shifts = (i + 1); 
        }
        if (shifts == null ) shifts = 0;
        // System.out.println( "Total shifts\t" + (shifts) );
        newExponent = Conversion.intToBinary( (Integer.parseInt( Conversion.binToInt( newExponent ) ) - (shifts) + "") );
        // System.out.println( "Final exponent\t" + newExponent );
        String shiftedSignificand = newSignificand.substring( shifts );
        return newSign + newExponent + shiftedSignificand + "0".repeat(23-shiftedSignificand.length());
    }




}
