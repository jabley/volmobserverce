/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

/**
 * Provides control over the maximum number of occurrences of an expectation.
 */
public interface MaxOcurrences {

    /**
     * Set the maximum number of expectations expected.
     *
     * @param maximum The maximum.
     */
    public void max(int maximum);

    /**
     * The number of occurrences is unlimited.
     *
     * <p>Currently equivalent to <code>max(Integer.MAX_INT)</code>
     */
    public void unbounded();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 ===========================================================================
*/
