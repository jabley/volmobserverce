/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.value;

import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Matches if the supplied object is an instance of the supplied class.
 *
 * <p>The matches method behaves as if it was written as follows with
 * <code>CLASS</code> replaced with the name of the supplied class.</p>
 * <pre>
 * public boolean matches(Object object) {
 *     return (object instanceof CLASS);
 * }
 * </pre>
 */
public class ExpectedInstanceOf
        implements ExpectedValue {

    /**
     * The class that the object is expected to be an instance of.
     */
    private final Class expectedClass;

    /**
     * Initialise.
     * @param expectedClass The class that the supplied object must be an
     * instance of.
     */
    public ExpectedInstanceOf(Class expectedClass) {
        this.expectedClass = expectedClass;
    }

    public boolean matches(Object object) {
        return expectedClass.isInstance(object);
    }

    public String toString() {
        return "ExpectedInstanceOf("+expectedClass+")";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
