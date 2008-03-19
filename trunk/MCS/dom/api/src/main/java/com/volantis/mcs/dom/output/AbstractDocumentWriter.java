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
 * 19-May-03    Mat             VBM:2003042907 - Created as an interface for
 *                              all MCSDOM outputters to implement.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom.output;

import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.Comment;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.XMLDeclaration;
import com.volantis.mcs.dom.dtd.DTD;

import java.io.IOException;
import java.io.Writer;


/**
 * Abstract DocumentWriter which must be extended by all classes that
 * output the MCS DOM
 */
public abstract class AbstractDocumentWriter
        implements DocumentWriter {

    /**
     * The DTD that describes the document structure that this writer will
     * write.
     */
    private final DTD dtd;

    /**
     * The output writer.
     */
    protected final Writer writer;

    /**
     * Initialise.
     *
     * <p>Will automatically wrap the writer in a
     * {@link LineLengthRestrictingWriter} if the maximum line length parameter
     * is greater than 0.</p>
     *
     * @param writer an {@link Writer} to write the contents of the text node to
     * @param dtd    Encapsulates information about the DTD.
     */
    public AbstractDocumentWriter(Writer writer, DTD dtd) {
        this.dtd = dtd;
        int maximumLineLength = dtd.getMaximumLineLength();
        if (maximumLineLength < 1) {
            this.writer = writer;
        } else {
            this.writer = new LineLengthRestrictingWriter(
                    writer, maximumLineLength);
        }
    }

    public Writer getUnderlyingWriter() {
        return writer;
    }

    public void outputXMLDeclaration(XMLDeclaration declaration)
            throws IOException {

        String encoding = declaration.getEncoding();
        if (encoding != null) {
            writer.write("<?xml version=\"1.0\" encoding=\"");
            writer.write(encoding);
            writer.write("\"?>");
        }
    }

    public void outputDocType(DocType docType)
            throws IOException {

        writer.write(docType.getAsString());
    }

    public void outputText(Text text, CharacterEncoder encoder)
            throws IOException {
        if ((encoder == null) || (text.isEncoded())) {
            char[] chars = text.getContents();
            int length = text.getLength();
            if (length > 0) {
                writer.write(chars, 0, length);
            }
        } else {
            encoder.encode(text.getContents(), 0, text.getLength(), writer);
        }
    }

    public void outputComment(Comment comment, CharacterEncoder encoder)
            throws IOException {

        writer.write("<!--");
        if (encoder == null) {
            char[] chars = comment.getContents();
            int length = comment.getLength();
            if (length > 0) {
                writer.write(chars, 0, length);
            }
        } else {
            encoder.encode(comment.getContents(), 0, comment.getLength(),
                    writer);
        }
        writer.write("-->");
    }

    public boolean outputOpenTag(Element element, CharacterEncoder encoder)
            throws IOException {

        String name = element.getName();
        if (dtd.isElementIgnoreable(name)) {
            return false;
        } else {
            return outputOpenTagImpl(element, encoder);
        }
    }

    protected abstract boolean outputOpenTagImpl(
            Element element, CharacterEncoder encoder)
            throws IOException;

    /**
     * Output the open tag associated with the element. If the method returns
     * true, a close element will be required otherwise, the close element will
     * already have been written so no further output is required.
     * @param element the com.volantis.mcs.dom.Element to write
     * @param childless true if the element has no children, false otherwise
     * @param allowsEmptyTag if true and childless is true, the element will be
     * closed and false returned to signify that no close element is required
     * later.
     * @param encoder a CharacterEncoder that will be used to encode element
     * attribute values.
     * @return true if a close tag is needed and false otherwise.
     * @throws java.io.IOException if an output error occurs
     */
    protected boolean outputOpenTag(
            Element element,
            boolean childless, boolean allowsEmptyTag,
            CharacterEncoder encoder) throws IOException {

        writer.write("<");
        String name = element.getName();
        writer.write(name);

        outputAttributes(element, encoder);

        // If the element has no children then it could be written out as an
        // empty element but this is dependent on the language.
        if (childless && allowsEmptyTag) {
            if (dtd.getEmptyTagRequiresSpace()) {
                writer.write(" ");
            }

            writer.write("/>");
            return false;
        }

        writer.write(">");
        return true;
    }

    /**
     * Output the attributes of the element.
     *
     * @param element the com.volantis.mcs.dom.Element to write
     * @param encoder a CharacterEncoder that will be used to encode element
     *                attribute values.
     * @throws IOException if an output error occurs
     */
    protected void outputAttributes(
            Element element,
            CharacterEncoder encoder) throws IOException {

        for (Attribute attribute = element.getAttributes();
             attribute != null; attribute = attribute.getNext()) {
            writer.write(" ");
            String name = attribute.getName();
            writer.write(name);
            writer.write("=\"");
            if (encoder == null || dtd.isNonReplaceableAttribute(name)) {
                writer.write(attribute.getValue());
            } else {
                encoder.encode(attribute.getValue(), writer);
            }
            writer.write("\"");
        }
    }

    public void outputCloseTag(Element element)
            throws IOException {
        writer.write("</");
        writer.write(element.getName());
        writer.write(">");
    }

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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/2	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/
