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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.output;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.sgml.SGMLDTD;
import com.volantis.mcs.dom.sgml.ElementModel;
import com.volantis.mcs.dom.sgml.ETD;

import java.io.IOException;
import java.io.Writer;


/**
 * SGMLDocumentWriter is a class to output a DOM Document with encoding unless
 * an element is a CDATA element in which case the content will not be encoded.
 * <p>CDATA elements are registered with the writer via the AddCDATAElement
 * method or through one of the two constructors. The element is simply the
 * name. When the DOM traversal encounters one of these elements, any text
 * node children are written unencoded. If the element is not registered,
 * the text node is simply passed on to the wrapped writer to encode it as
 * it wishes.</p>
 */
public final class SGMLDocumentWriter
        extends AbstractDocumentWriter {

    /**
     * Boolean denoting whether or not we are in a CDATA element. As these are
     * not likely to have child elements ( Currently only 'script' and 'style'
     * are defined in the HTML dtd ) other than text nodes, there is no need to
     * have a stack of states.
     */
    private boolean inCDATAElement;

    private final SGMLDTD dtd;

    /**
     * Creates a new instance of SGMLDocumentWriter with known CDATA elements.
     *
     * @param writer the writer
     * @param dtd    encapsulates information from the SGML DTD.
     */
    public SGMLDocumentWriter(Writer writer, SGMLDTD dtd) {

        super(writer, dtd);
        this.dtd = dtd;
    }

    // Javadoc inherited
    public void outputText(Text text, CharacterEncoder encoder)
            throws IOException {
        // If we are inside a CDATA element, do not encode text. Otherwise
        // let the wrapped document writer encode it if it has not already been
        // encoded. This way, we do not care whether the wrapped writer is for
        // a protocol or simply XML.
        if (inCDATAElement) {
            writer.write(text.getContents(), 0, text.getLength());
            ;
        } else {
            // If the text has already been encoded, don't do it again
            if (text.isEncoded()) {
                writer.write(text.getContents(), 0, text.getLength());
            } else {
                encoder.encode(text.getContents(), 0,
                        text.getLength(), writer);
            }
        }
    }

    protected boolean outputOpenTagImpl(
            Element element, CharacterEncoder encoder)
            throws IOException {

        if (inCDATAElement) {
            throw new IllegalArgumentException(
                    "Cannot have elements within elements that have a CDATA " +
                            "content model");
        }

        String name = element.getName();
        ETD etd = dtd.getETD(name);

        ElementModel model = etd.getElementModel();
        inCDATAElement = (model == ElementModel.CDATA);

        boolean childless = element.isEmpty();
        boolean elementHasEmptyModel = (model == ElementModel.EMPTY);

        // If the element has no children, is always empty and has an optional
        // end tag then the end tag must not be written out and the element
        // must not be closed in any way.
        //
        // See http://www.w3.org/TR/html4/intro/sgmltut.html for more details.
        boolean allowClose = true;
        if (childless && elementHasEmptyModel && etd.isEndTagOptional()) {
            allowClose = false;

            // Override the closeEmpty value to prevent an empty tag from being
            // written out.
            elementHasEmptyModel = false;
        }

        boolean closeRequired = super.outputOpenTag(element, childless,
                elementHasEmptyModel, encoder);

        // Prevent the tag from being closed if it is not allowed.
        closeRequired = closeRequired && allowClose;

        return closeRequired;
    }

    // Javadoc inherited
    public void outputCloseTag(Element element)
            throws IOException {

        inCDATAElement = false;
        super.outputCloseTag(element);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/
