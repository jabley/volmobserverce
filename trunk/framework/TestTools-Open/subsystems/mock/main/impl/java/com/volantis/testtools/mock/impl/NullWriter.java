/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import java.io.IOException;
import java.io.Writer;

public class NullWriter extends Writer {
    
    public static final Writer INSTANCE = new NullWriter();

    public void write(int c) throws IOException {
    }

    public void write(char cbuf[]) throws IOException {
    }

    public void write(String str) throws IOException {
    }

    public void write(String str, int off, int len) throws IOException {
    }

    public void close() throws IOException {
    }

    public void flush() throws IOException {
    }

    public void write(char cbuf[], int off, int len) throws IOException {
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
