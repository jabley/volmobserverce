/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.method;

import com.volantis.testtools.mock.method.MaxOcurrences;
import com.volantis.testtools.mock.method.Occurrences;

public class OccurrencesImpl
        implements Occurrences {

    /**
     * Indicates whether the minimum value has been explicitly set.
     *
     * <p>This is used to detect attempts to set the minimum multiple
     * times.</p>
     */
    private boolean minimumHasBeenSet;

    private int minimum;

    /**
     * Indicates whether the maximum value has been explicitly set.
     *
     * <p>This is used to detect attempts to set the maximum multiple
     * times.</p>
     */
    private boolean maximumHasBeenSet;

    private int maximum;

    public OccurrencesImpl(int minimum, int maximum) {

        if (minimum < 0) {
            throw new IllegalArgumentException(
                    "minimum (" + minimum + ") must be >= 0");
        }
        if (maximum < minimum) {
            throw new IllegalArgumentException(
                    "maximum (" + maximum + ") must be >= minimum ("
                    + minimum + ")");
        }

        checkThenSetMinimum(minimum);
        checkThenSetMaximum(maximum);
    }

    public OccurrencesImpl() {
        this.minimum = 1;
        this.maximum = 1;
    }

    // Javadoc inherited.
    public void any() {
        checkThenSetMinimum(0);
        checkThenSetMaximum(Integer.MAX_VALUE);
    }

    // Javadoc inherited.
    public void atLeast(int minimum) {
        checkThenSetMinimum(minimum);
        checkThenSetMaximum(Integer.MAX_VALUE);
    }

    // Javadoc inherited.
    public MaxOcurrences min(int minimum) {
        checkThenSetMinimum(minimum);
        return this;
    }

    // Javadoc inherited.
    public void optional() {
        checkThenSetMinimum(0);
        checkThenSetMaximum(1);
    }

    // Javadoc inherited.
    public void fixed(int number) {
        checkThenSetMinimum(number);
        checkThenSetMaximum(number);
    }

    // Javadoc inherited.
    public void unbounded() {
        checkThenSetMaximum(Integer.MAX_VALUE);
    }

    // Javadoc inherited.
    public void max(int maximum) {
        checkThenSetMaximum(maximum);
    }

    // Javadoc inherited.
    private void checkThenSetMinimum(int minimum) {
        if (minimumHasBeenSet) {
            throw new IllegalStateException("minimum has already been set");
        }
        minimumHasBeenSet = true;
        setMinimum(minimum);
    }

    // Javadoc inherited.
    private void checkThenSetMaximum(int maximum) {
        if (maximumHasBeenSet) {
            throw new IllegalStateException("maximum has already been set");
        }
        maximumHasBeenSet = true;
        setMaximum(maximum);
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int getMaximum() {
        return maximum;
    }
//
//    protected final RepeatingExpectation repeating;
//
//    public OccurrencesImpl(RepeatingExpectation repeating) {
//        this.repeating = repeating;
//    }
//
//    public void setMaximum(int maximum) {
//        repeating.setMaximum(maximum);
//    }
//
//    public void setMinimum(int minimum) {
//        repeating.setMinimum(minimum);
//    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 14-Jun-05	7997/1	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 02-Jun-05	7995/3	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrencess of a method call

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrencess of a method call

 ===========================================================================
*/
