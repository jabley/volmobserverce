/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dissection.dom;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * An implementation of {@link OutputDocument} for binary output.
 */ 
public class BinaryOutputDocument implements OutputDocument {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private ByteArrayOutputStream baos;

    public BinaryOutputDocument() {
        baos = new ByteArrayOutputStream();
    }

    public OutputStream getOutputStream() {
        return baos;
    }

    public int getSize() {
        return baos.size();
    }

    public void write(PrintStream out) {
        try {
            out.write(baos.toByteArray());
        } catch (IOException e) {
            // This will never happen since printstream doesn't throw 
            // IOExceptions, but just in case...
            // Note I'd use WrappedRuntimeException here but then i'd get
            // a merge failure on Mimas...
            throw new RuntimeException(e.getMessage());
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 ===========================================================================
*/
