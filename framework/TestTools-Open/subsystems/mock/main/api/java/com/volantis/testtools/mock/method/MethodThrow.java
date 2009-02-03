/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

public class MethodThrow
        implements MethodAction {

    private final Throwable throwable;

    public MethodThrow(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object perform(MethodActionEvent event)
            throws Throwable {

        throwable.fillInStackTrace();
        throw throwable;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 ===========================================================================
*/
