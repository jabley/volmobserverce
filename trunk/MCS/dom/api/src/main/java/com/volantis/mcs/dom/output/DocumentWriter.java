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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.output;

import com.volantis.mcs.dom.XMLDeclaration;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.Comment;
import com.volantis.mcs.dom.Element;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface DocumentWriter {

    Writer getUnderlyingWriter();

    void outputXMLDeclaration(XMLDeclaration declaration)
            throws IOException;

    void outputDocType(DocType docType)
                    throws IOException;

    /**
     * Output a Text node to a Writer.
     *
     * @param text    the {@link com.volantis.mcs.dom.Text} node to write
     * @param encoder a CharacterEncoder that may be used to encode the text
     *                before it is written.
     * @throws java.io.IOException if an output error occurs
     */
    void outputText(Text text, CharacterEncoder encoder)
            throws IOException;

    /**
     * Output a Comment node to a Writer.
     *
     * @param comment the {@link com.volantis.mcs.dom.Comment} node to write
     * @param encoder a CharacterEncoder that may be used to encode the comment
     *                before it is written.
     * @throws java.io.IOException if an output error occurs
     */
    void outputComment(Comment comment, CharacterEncoder encoder)
            throws IOException;

    /**
     * Output the open tag associated with the element. If the method returns
     * true, a close element will be required otherwise, an empty tag will
     * have been written, or the element does not need to be closed.
     *
     * @param element the {@link Element} to write.
     * @param encoder a CharacterEncoder that will be used to encode element
     *                attribute values.
     * @return true if a close tag is needed and false otherwise.
     * @throws java.io.IOException if an output error occurs
     */
    boolean outputOpenTag(
            Element element,
            CharacterEncoder encoder)
            throws IOException;

    /**
     * Output the close tag associated with the element. This method should be
     * called if outputOpenTag() returns true.
     * @param element the com.volantis.mcs.dom.Element to close
     * @throws java.io.IOException if an output error occurs
     */
    void outputCloseTag(Element element )
        throws IOException;

    /**
     * Writes any buffered content that has not yet been written.
     *
     * @throws java.io.IOException if an IO error occurs during the flush.
     */
    void flush() throws IOException;

}
