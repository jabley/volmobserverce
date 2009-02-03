/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.value;

import com.volantis.testtools.mock.value.ExpectedValue;
import com.volantis.testtools.mock.value.ValueHelper;

/**
 * Matches if the objects are equal.
 *
 * <p>Equal means that they are either both null, or the {@link Object#equals}
 * method returns true.</p>
 */
public class ExpectedEqual
        implements ExpectedValue {

    /**
     * The object to match against.
     */
    private final Object value;

    /**
     * Initialise new instance.
     * @param value The value to match against.
     */
    public ExpectedEqual(Object value) {
        this.value = value;
    }

    // Javadoc inherited.
    public boolean matches(Object object) {
        return ValueHelper.deepEquals(value, object);
    }

    // Javadoc inherited.
    public String toString() {
        return String.valueOf(value);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
