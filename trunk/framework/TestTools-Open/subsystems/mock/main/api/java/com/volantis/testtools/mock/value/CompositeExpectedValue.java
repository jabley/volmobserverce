/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.value;

public interface CompositeExpectedValue extends ExpectedValue {
    void addExpectedValue(ExpectedValue value);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
