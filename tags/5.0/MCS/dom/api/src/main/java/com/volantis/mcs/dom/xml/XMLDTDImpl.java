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

package com.volantis.mcs.dom.xml;

import com.volantis.mcs.dom.dtd.DTDImpl;
import com.volantis.mcs.dom.dtd.DTDBuilder;
import com.volantis.mcs.dom.output.DocumentWriter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;

import java.io.Writer;
import java.util.Set;

/**
 * Implementation of {@link XMLDTD}.
 */
public class XMLDTDImpl
        extends DTDImpl
        implements XMLDTD {

    /**
     * Initialise.
     *
     * @param builder The builder from which this is constructed.
     */
    public XMLDTDImpl(DTDBuilder builder) {
        super(builder);
    }

    // Javadoc inherited.
    public boolean isNonReplaceableAttribute(String name) {
        // Override as XML should not have any non replaceable attributes.
        return false;
    }

    // Javadoc inherited.
    public DocumentWriter createDocumentWriter(Writer writer) {
        return new XMLDocumentWriter(writer, this);
    }
}
