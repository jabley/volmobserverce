/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.value;

import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Matches if the supplied object is an array of the supplied class.
 */
public class ExpectedArrayOf
        implements ExpectedValue {

    /**
     * The class that the object is expected to be an array of.
     */
    private final Class expectedComponentClass;

    /**
     * Initialise.
     * @param expectedComponentClass The class that the supplied object must be
     * an array of.
     */
    public ExpectedArrayOf(Class expectedComponentClass) {
        this.expectedComponentClass = expectedComponentClass;
    }

    public boolean matches(Object object) {
        Class actualClass = object.getClass();
        Class componentClass = actualClass.getComponentType();
        return (componentClass == expectedComponentClass);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
