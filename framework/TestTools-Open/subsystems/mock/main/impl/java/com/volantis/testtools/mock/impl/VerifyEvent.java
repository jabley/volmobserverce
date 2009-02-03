/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;


/**
 * A simple event that is used to verify expectations are satisfied.
 *
 * <p>This event must never be matched by any expectation.</p>
 */
public class VerifyEvent
        extends EventImpl {

    /**
     * A dummy event source.
     */
    private static final Object DUMMY_SOURCE = new Object();

    /**
     * An instance of this that can be shared across all usages.
     *
     * <p>This must appear after {@link #DUMMY_SOURCE}, otherwise the super
     * class's constructor will fail.</p>
     */
    public static final VerifyEvent INSTANCE = new VerifyEvent();

    /**
     * Initialise
     */
    public VerifyEvent() {
        super(DUMMY_SOURCE);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
