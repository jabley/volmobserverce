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

import com.volantis.mcs.dom.Comment;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.VisitorWrappingException;
import com.volantis.mcs.dom.XMLDeclaration;

import java.io.IOException;

/**
 * This class outputs a DOM Document or Element to a Writer via a DocumentWriter
 * object. The DocumentWriter is passed the Writer and CharacterEncoder objects
 * passed in the constructor so that these may be used to output the document.
 */
public class DOMDocumentOutputter
        extends RecursingDOMVisitor
        implements DocumentOutputter {

    /**
     * Factory to use to create DOM objects.
     */
    protected static DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * An element to output in place of elements with null names when
     * we are debugging them.
     */
    private static Element nullNameElement = domFactory.createElement();

    static {
        nullNameElement.setName("'NULL'");
    }

    /**
     * The writer that will output the DOM Document
     */
    private DocumentWriter writer;

    /**
     * A CharacterEncoder that may be used to encode attribute values and
     * text during output.
     */
    private CharacterEncoder encoder;

    /**
     * A flag to control whether we output a marker element to represent
     * elements with null names for debugging purposes (only).
     * <p>
     * NOTE: this variable and related code should be removed once
     * VBM:2005011307 has been addressed.
     */
    private boolean debugNullElementNames;

    /**
     * Create a DOMDocumentOutputter
     *
     * @param outputter the DocumentWriter that will output the document
     * @param encoder   a CharacterEncoder that may be used to encode attribute
     *                  values and text during output.
     */
    public DOMDocumentOutputter(
            DocumentWriter outputter,
            CharacterEncoder encoder) {
        writer = outputter;
        this.encoder = encoder;
    }

    /**
     * Sets the flag which controls whether we output a marker element to
     * represent elements with null names for debugging purposes (only).
     *
     * @param debugNullElementNames if true output a fake element in place of
     *                              elements with null elements.
     */
    public void setDebugNullElementNames(boolean debugNullElementNames) {
        this.debugNullElementNames = debugNullElementNames;
    }

    /**
     * Output a DOM Document.
     *
     * @param document the Document to output.
     * @throws IOException if an error occurs during output.
     */
    public void output(Document document) throws IOException {
        try {
            XMLDeclaration declaration = document.getDeclaration();
            if (declaration != null) {
                writer.outputXMLDeclaration(declaration);
            }

            DocType docType = document.getDocType();
            if (docType != null) {
                Element element = document.getRootElement();
                String rootElement = element.getName();

                MarkupFamily family = docType.getMarkupFamily();
                String expectedRoot = docType.getRoot();
                if (!family
                        .compareRootElementNames(rootElement, expectedRoot)) {
                    throw new IllegalStateException(
                            "Expected root element name" + expectedRoot +
                                    " does not match actual root element name " +
                                    rootElement);
                }

                writer.outputDocType(docType);
            }

            document.forEachChild(this);
        } catch (VisitorWrappingException e) {
            throw (IOException) e.getCause();
        }
    }

    /**
     * Output a DOM Element and any of its children.
     *
     * @param element the Element to output
     * @throws IOException if an output error occurs
     */
    public void output(Element element) throws IOException {
        try {
            outputElement(element);
        } catch (VisitorWrappingException e) {
            throw (IOException) e.getCause();
        }
    }

    // Javadoc inherited.
    public void visit(Element element) {
        try {
            outputElement(element);
        } catch (IOException e) {
            throw new VisitorWrappingException(e);
        }
    }

    // Javadoc inherited.
    public void visit(Text text) {
        try {
            writer.outputText(text, encoder);
        } catch (IOException e) {
            throw new VisitorWrappingException(e);
        }
    }


    // Javadoc inherited.
    public void visit(Comment comment) {
        try {
            writer.outputComment(comment, encoder);
        } catch (IOException e) {
            throw new VisitorWrappingException(e);
        }
    }

    /**
     * Output a DOM Element and any of its children.
     *
     * @param element the Element to output
     * @throws IOException if an output error occurs
     */
    private void outputElement(Element element) throws IOException {
        // Get the first child in the list of children.
        boolean closeRequired;

        // Output the open tag.
        if (element.getName() == null) {
            if (debugNullElementNames) {
                closeRequired = writer.outputOpenTag(
                        nullNameElement, encoder);
            } else {
                closeRequired = false;
            }
        } else {
            closeRequired = writer.outputOpenTag(element, encoder);
        }

        element.forEachChild(this);

        // Output the close tag.
        if (closeRequired) {
            if (element.getName() == null) {
                if (debugNullElementNames) {
                    writer.outputCloseTag(nullNameElement);
                }
            } else {
                writer.outputCloseTag(element);
            }
        }
    }

    // Javadoc inherited.
    public void flush() throws IOException {
        writer.flush();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 14-Jan-05	6346/2	geoff	VBM:2004110112 Certain form layouts generate invalid WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 06-Feb-04	2794/3	steve	VBM:2004012613 HTML Quote handling

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/
