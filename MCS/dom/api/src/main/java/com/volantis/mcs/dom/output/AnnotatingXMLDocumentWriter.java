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
package com.volantis.mcs.dom.output;

import com.volantis.mcs.dom.Element;

import java.io.IOException;
import java.io.Writer;

/**
 * A debugging XML document writer which additionally dumps element annotations
 * out as fake attributes.
 */
public class AnnotatingXMLDocumentWriter extends XMLDocumentWriter {

    /**
     * Constructor.
     * @param writer the writer
     */
    public AnnotatingXMLDocumentWriter(Writer writer) {
        super(writer);
    }

    // Javadoc inherited.
    protected void outputAttributes(Element element,
            CharacterEncoder encoder) throws IOException {
        super.outputAttributes(element, encoder);

        // If this element has an annotation.
        if (element.getAnnotation() != null) {
            // Dump it out as a fake attribute for testing.
            writer.write(" annotation=\"");
            writer.write(element.getAnnotation().toString());
            writer.write("\"");
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 21-Jun-05	8856/2	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/6	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-Mar-05	7357/2	pcameron	VBM:2005030906 Fixed node annotation for dissection

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
