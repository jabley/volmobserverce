/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.generated.MockObjectConfiguration;

public class MockObjectConfigurationImpl
        implements MockObjectConfiguration {

    private boolean enableEqualsExpectations;
    private boolean enableToStringExpectations;
    private boolean enableHashCodeExpectations;

    // Javadoc inherited.
    public void setEqualsShouldCheckExpectations(boolean check) {
        enableEqualsExpectations = check;
    }

    // Javadoc inherited.
    public boolean equalsShouldCheckExpectations() {
        return enableEqualsExpectations;
    }

    // Javadoc inherited.
    public void setToStringShouldCheckExpectations(boolean check) {
        enableToStringExpectations = check;
    }

    // Javadoc inherited.
    public boolean toStringShouldCheckExpectations() {
        return enableToStringExpectations;
    }

    // Javadoc inherited.
    public void setHashCodeShouldCheckExpectations(boolean check) {
        enableHashCodeExpectations = check;
    }

    // Javadoc inherited.
    public boolean hashCodeShouldCheckExpectations() {
        return enableHashCodeExpectations;
    }
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
