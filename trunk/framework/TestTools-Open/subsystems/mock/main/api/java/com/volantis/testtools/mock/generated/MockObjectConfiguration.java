/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generated;

/**
 * Contains information 
 */
public interface MockObjectConfiguration {
    
    /**
     * If true, causes the {@link Object#equals(Object)} method to check
     * expectations if it is not being used within the mock framework.
     * <p/>
     * <p>If this is true and the method is not being invoked from within
     * the mock framework then it checks the expectations, otherwises it
     * uses the default implementation.</p>
     * <p/>
     * <p>The reason for using the default implementation from within the
     * mock framework is to prevent infinite recursion if the framework
     * invokes the method to check an expectation.</p>
     *
     * @param check if true, should check expectations.
     */
    void setEqualsShouldCheckExpectations(boolean check);

    /**
     * Check whether the mocked equals method should check expectations.
     *
     * @return True if it should, false if it must not.
     */
    boolean equalsShouldCheckExpectations();

    /**
     * If true, causes the {@link Object#toString} method to check expectations
     * if it is not being used within the mock framework.
     *
     * @see #setEqualsShouldCheckExpectations
     * @param check if true, should check expectations.
     */
    void setToStringShouldCheckExpectations(boolean check);

    /**
     * Check whether the mocked toString method should check expectations.
     *
     * @return True if it should, false if it must not.
     */
    boolean toStringShouldCheckExpectations();

    /**
     * If true, causes the {@link Object#hashCode} method to check expectations
     * if it is not being used within the mock framework.
     *
     * @see #setEqualsShouldCheckExpectations
     * @param check if true, should check expectations.
     */
    void setHashCodeShouldCheckExpectations(boolean check);

    /**
     * Check whether the mocked hashCode method should check expectations.
     *
     * @return True if it should, false if it must not.
     */
    boolean hashCodeShouldCheckExpectations();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 ===========================================================================
*/
