/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.value;

import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * Matches if the object is not null.
 */
public class ExpectedNotNull
        implements ExpectedValue {

    /**
     * A preconstructed instance of this class that should be used in
     * preference to creating a new one.
     */
    public static final ExpectedValue INSTANCE = new ExpectedNotNull();

    // Javadoc inherited.
    public boolean matches(Object object) {
        return (object != null);
    }

    // Javadoc inherited.
    public String toString() {
        return "<not null>";
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

 08-Apr-04	3514/4	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
