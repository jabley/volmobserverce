/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

/**
 * Provides control over the number of occurrences of an expectation.
 */
public interface Occurrences
        extends MaxOcurrences {

    /**
     * Set the minimum.
     *
     * @param minimum The minimum number of occurrences to allow.
     *
     * @return An object to set an upper limit on the number of occurrences.
     */
    public MaxOcurrences min(int minimum);

    /**
     * The expectation is optional.
     *
     * <p>This is equivalent to <code>min(0).max(1)</code>.</p>
     */
    public void optional();

    /**
     * There are a fixed number of the expectations.
     *
     * <p>This is equivalent to <code>min(number).max(number)</code>.</p>
     *
     * @param number The number of times it must occur.
     */
    public void fixed(int number);

    /**
     * Any number of expectations are allowed.
     *
     * <p>This is equivalent to <code>min(0).unbounded()</code>.</p>
     */
    public void any();

    /**
     * Occurs at least the minimum number of times.
     *
     * <p>This is equivalent to <code>min(number).unbounded()</code>.</p>
     *
     * @param minimum The minimum number of times that it occurs.
     */
    public void atLeast(int minimum);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 14-Jun-05	7997/1	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 ===========================================================================
*/
