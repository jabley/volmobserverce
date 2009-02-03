/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

public class SatisfactionLevel {
    public static final SatisfactionLevel UNSATISFIED = new SatisfactionLevel(
            "UNSATISFIED");
    public static final SatisfactionLevel PARTIAL = new SatisfactionLevel(
            "PARTIAL");
    public static final SatisfactionLevel COMPLETE = new SatisfactionLevel(
            "COMPLETE");

    private final String myName; // for debug only

    private SatisfactionLevel(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 ===========================================================================
*/
