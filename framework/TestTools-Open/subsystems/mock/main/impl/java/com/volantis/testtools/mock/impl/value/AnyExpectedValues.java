/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.value;

import com.volantis.testtools.mock.impl.value.AbstractCompositeValue;
import com.volantis.testtools.mock.value.ExpectedValue;

import java.util.Iterator;

/**
 * Matches if any of the contained values match.
 *
 * <p>This is equivalent to the || operator in that it succeeds as soon as the
 * first contained value succeeds, otherwise it fails. It always fails if it is
 * empty.</p>
 */
public class AnyExpectedValues
        extends AbstractCompositeValue {

    /**
     * Matches if all the values in this composite match, otherwise it fails.
     */
    public boolean matches(Object object) {

        // Iterate over the values testing each in turn. If any of the values
        // fail to match then this method fails immediately, otherwise this
        // method will succeed.
        if (values != null) {
            for (Iterator i = values.iterator(); i.hasNext();) {
                ExpectedValue value = (ExpectedValue) i.next();
                if (value.matches(object)) {
                    return true;
                }
            }
        }

        return false;
    }
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
