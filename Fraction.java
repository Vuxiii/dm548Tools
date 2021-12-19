class Fraction {

    private int numerator;
    private int denominator;

    /**
     * Constructs the fraction 1 / 1.
     */
    Fraction() {
        this( 1, 1 );
    }

    /**
     * Constructs the fraction numerator / 1.
     */
    Fraction( int numerator ) {
        this( numerator, 1 );
    }

    /**
     * Construct a new Fraction with the given numerator and denominator.
     */
    Fraction ( int numerator, int denominator ) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    /**
     * This method returns the numerator of this Fraction.
     */
    public int numerator() {
        return numerator;
    }

    /**
     * This method returns the denominator of this Fraction.
     */
    public int denominator() {
        return denominator;
    }

    /**
     * Computes the greatest common divisor.
     */
    private int gcd( int m, int n ) {
        while ( m != n ) {
            if ( m > n )
                m = m - n;
            else
                n = n - m;
        }
        return m;
    }

    /**
     * Computes the least common multiplum.
     */
    private int lcm( int m, int n ) {
        return ( m / gcd( m, n ) * n );
    }

    /**
     * Returns a new Fraction based on the addition of this Fraction and the given Fraction.
     */
    public Fraction add( Fraction other ) {
        int newD = lcm( other.denominator, denominator );
        int newN = numerator * ( newD / denominator ) + other.numerator * ( newD / other.denominator );
        
        return new Fraction( newN, newD );
    } 

    /**
     * Returns a new Fraction based on the subtraction of this Fraction and the given Fraction.
     */
    public Fraction subtract( Fraction other ) {
               
        int newD = lcm( other.denominator, denominator );
        int newN = numerator * ( newD / denominator ) - other.numerator * ( newD / other.denominator );
        
        return new Fraction( newN, newD );
    }

    /**
     * Returns a new Fraction based on the multiplum of this Fraction and the given Fraction.
     */
    public Fraction multiply( Fraction other ) {
        return new Fraction( numerator * other.numerator, denominator * other.denominator );
    }

    /**
     * Returns a new Fraction based on the division of this fraction and the given fraction.
     * Precondition: other's numerator != 0.
     */
    public Fraction divide( Fraction other ) {
        return new Fraction( numerator * other.denominator, denominator * other.numerator );
    }

    /**
     * Computes an approximation of this Fraction.
     */
    public double value() {
        return (double) numerator / denominator;
    }

    /**
     * Simplifies this Fraction.
     */
    public void simplify() {
        int gcd = gcd( numerator, denominator );

        numerator = numerator / gcd;
        denominator = denominator / gcd;

        if ( denominator < 0 ) {
            denominator = -denominator;
            numerator = -numerator;
        }

    }

    /**
     * Returns the integer part of this fraction.
     */
    public int integerPart() {
        return numerator/denominator;
    }
    
    /**
     * Returns the proper part of this fraction.
     */
    public Fraction properPart() {
        return new Fraction(numerator%denominator,denominator);
    }

    /**
     * Returns the hashcode of this Fraction.
     */
    public int hashcode() {
        return denominator + numerator * 31;
    }

    /**
     * Returns a copy of this Fraction.
     */
    public Fraction copy() {
        return new Fraction( numerator, denominator );
    }

    /**
     * Checks whether other is equal to this Fraction.
     */
    public boolean equals( Object other ) {
        if ( other == null )
            return false;
        if ( other == this )
            return true;
        if ( !(other instanceof Fraction ) )
            return false;
        
        Fraction o = (Fraction) other;
        
        Fraction othC = o.copy();
        Fraction oriC = copy();
        oriC.simplify();
        othC.simplify();
        return oriC.numerator == othC.numerator && oriC.denominator == othC.denominator;
    } 

    /**
     * Computes a String representation of this Fraction.
     */
    public String toString() {
        return numerator + " / " + denominator;
    }

} 