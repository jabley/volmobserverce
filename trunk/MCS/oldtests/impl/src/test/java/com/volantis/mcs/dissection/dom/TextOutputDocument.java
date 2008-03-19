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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.dom.OutputDocument;

import java.io.StringWriter;
import java.io.PrintStream;
import java.io.Writer;

/**
 * An implementation of {@link OutputDocument} for text output.
 */ 
public class TextOutputDocument
    implements OutputDocument {

    private StringWriter writer;

    public TextOutputDocument() {
        writer = new StringWriter();
    }

    public Writer getWriter () {
        return writer;
    }

    public int getSize() {
        return writer.getBuffer().length();
    }

    public void write(PrintStream out) {
        out.print(writer.getBuffer().toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	774/2	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/2	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/5	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
