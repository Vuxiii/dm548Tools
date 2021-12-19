public class BinaryResult {
    public final String val;
    public final OverUnderFlow overflow;
    public BinaryResult( String val, OverUnderFlow overflow ) {
        this.val = val;
        this.overflow = overflow;
    }
}
