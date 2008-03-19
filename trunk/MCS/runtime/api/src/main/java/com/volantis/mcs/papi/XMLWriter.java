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

package com.volantis.mcs.papi;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;

/**
 * Class that is intended to be used to generate XML from PAPIAttributes.
 */
public class XMLWriter {
    /**
     * Writer that we are delegating to
     */
    private Writer writer;

    /**
     * Creates a new XMLWriter instnace
     * @param writer
     */
    public XMLWriter(Writer writer) {
        if (writer == null) {
            throw new IllegalArgumentException("The Writer cannot be null");
        }
        this.writer = writer;
    }

    /**
     * Writes out the open element tag for XDIME element represented by the
     * PAPIAttributes parameter. All atttribute values written are escaped
     * using the appropriate XMLEntitites
     * @param attributes the PAPIAttributes that represent the tag to be
     * written
     */
    public void openElement(PAPIAttributes attributes) throws IOException {
        writer.write("<");
        writer.write(attributes.getElementName());
        for (Iterator i=attributes.getAttributeNames(null); i.hasNext();) {
            String name = (String) i.next();
            String value = attributes.getAttributeValue(null, name);
            // write out the attribute
            writer.write(' ');
            writer.write(name);
            writer.write("=\"");
            writer.write(encodeString(value));
            writer.write("\"");
        }

        writer.write(">");
    }

    /**
     * Writes out the close element tag for the XDIME element represented by
     * thea PAPIAttributes parameter.
     * @param attributes the PAPIAttributes that represent the tag to be
     * written
     */
    public void closeElement(PAPIAttributes attributes) throws IOException {
        writer.write("</");
        writer.write(attributes.getElementName());
        writer.write(">");
    }

    /**
     * Write character text out. This is not yet supported as we have no
     * need for it
     * @param text the text to write
     */
    public void writeText(String text) {
        throw new UnsupportedOperationException("XMLWriter#writeText is not supported yet");
    }

    /**
     * Escapes the < > " ' & characters using the appropriate XML Entities
     * @param value the value to be encoded
     * @return the encoded value
     */
    private String encodeString(String value) {
        return StringEscapeUtils.escapeXml(value);
    }
}
