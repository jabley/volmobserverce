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
package com.volantis.mcs.dissection.dom.impl;

import com.volantis.mcs.dissection.dom.DissectableDocumentTestAbstract;
import com.volantis.mcs.dissection.dom.DissectableDocumentBuilder;
import com.volantis.mcs.dissection.dom.OutputDocument;
import com.volantis.mcs.dissection.dom.DissectedContentHandler;
import com.volantis.mcs.dissection.dom.TextOutputDocument;

/**
 * An implementation of {@link DissectableDocumentTestAbstract} which tests
 * dissection for the "TDOM" DOM implementation.
 */ 
public class TDissectableDocumentTestCase 
        extends DissectableDocumentTestAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public TDissectableDocumentTestCase(String name) {
        super(name);
    }

    protected DissectableDocumentBuilder createBuilder() {
        return new TDissectableDocumentBuilder(new TCalculatorOldWayFactory());
    }

    protected OutputDocument createOutputDocument() throws Exception {
        return new TextOutputDocument();
    }

    protected DissectedContentHandler createDissectedContentHandler(
            OutputDocument output) throws Exception {
        return new TDissectedContentHandler((TextOutputDocument) output);
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
