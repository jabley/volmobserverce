/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface CallUpdater {

    /**
     * Specifies the exception that should be thrown when the expected method
     * call represented by this object is received.
     *
     * @param throwable The throwable that should be thrown.
     *
     * @return An object to set the number of occurrences of this call.
     */
    public Occurrences fails(Throwable throwable);

    /**
     * Specify an arbitrary action to take when the expected method call
     * represented by this object is received.
     *
     * @param action The action that should be taken.
     *
     * @return An object to set the number of occurrences of this call.
     */
    public Occurrences does(MethodAction action);
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
