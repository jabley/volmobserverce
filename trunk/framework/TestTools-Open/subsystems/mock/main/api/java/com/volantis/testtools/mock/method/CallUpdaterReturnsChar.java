/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

/**
 * An {@link ExpectedCall} that returns a character.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface CallUpdaterReturnsChar
        extends CallUpdater {

    /**
     * Sets the return value when the expected method call represented by this
     * object is received.
     *
     * <p>This is syntactic sugar to make it easier to use primitive character
     * values.</p>
     *
     * @param returnValue The return value.
     *
     * @return An object to set the number of occurrences of this call.
     */
    public Occurrences returns(char returnValue);

    /**
     * Specify a description for this expectation.
     *
     * @param description The description of this expectation.
     *
     * @return This object to allow initialisation to be chained.
     */
    public CallUpdaterReturnsChar description(String description);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 ===========================================================================
*/
