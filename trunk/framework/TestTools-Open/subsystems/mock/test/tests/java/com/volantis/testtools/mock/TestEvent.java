/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock;

import com.volantis.testtools.mock.impl.EventImpl;

public class TestEvent
        extends EventImpl {

    private static final Object DUMMY_SOURCE = new Object();

    private final String value;

    public TestEvent(String value) {
        super(DUMMY_SOURCE);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
