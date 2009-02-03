/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import java.io.PrintWriter;

public class NullPrintWriter
        extends PrintWriter {

    public static final PrintWriter INSTANCE = new NullPrintWriter();

    public NullPrintWriter() {
        super(NullWriter.INSTANCE);
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
