/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.value;

import com.volantis.testtools.mock.value.CompositeExpectedValue;
import com.volantis.testtools.mock.value.ExpectedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all collections of expected values.
 *
 * <p>The behaviour of this class, i.e. when it matches and when it does not,
 * is determined by derived classes.</p>
 */
public abstract class AbstractCompositeValue
        implements CompositeExpectedValue {

    /**
     * The list of values.
     */
    protected List values;

    public AbstractCompositeValue() {
    }

    public void addExpectedValue(ExpectedValue value) {
        if (values == null) {
            values = new ArrayList();
        }
        values.add(value);
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
