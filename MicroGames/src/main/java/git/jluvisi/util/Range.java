package git.jluvisi.util;

/**
 * Repersents a mathmatical range in Java.
 */
public class Range {

    /** Lowest number allowed. */
    private int low;
    /** Highest number allowed. */
    private int high;

    /**
     * Makes a range object.
     *
     * @param low
     * @param high
     */
    public Range(int low, int high) {
        this.low = low;
        this.high = high;
    }

    /**
     * Tells if the value is in between the set range values. This method is
     * inclusive.
     *
     * @param value
     * @return boolean
     */
    public boolean contains(int value) {
        return (value >= low && value <= high);
    }

}
