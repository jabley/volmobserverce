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

package com.volantis.mcs.dom.sgml;

import com.volantis.mcs.dom.dtd.DTDImpl;
import com.volantis.mcs.dom.output.DocumentWriter;
import com.volantis.mcs.dom.output.SGMLDocumentWriter;

import java.io.Writer;
import java.util.Map;
import java.util.Set;

/**
 * Represents information about an SGML DTD.
 */
public class SGMLDTDImpl
        extends DTDImpl
        implements SGMLDTD {

    /**
     * The default ETD used if no information is available.
     */
    private static final ETD DEFAULT_ETD = new ETDImpl();

    /**
     * Map from {@link String} to {@link ETD}.
     */
    private final Map element2ETD;

    /**
     * Set of {@link String} attributes names that are not replaceable.
     */
    private final Set nonReplaceableAttributes;

    /**
     * Initialise.
     *
     * @param builder The builder from which this is built.
     */
    public SGMLDTDImpl(SGMLDTDBuilder builder) {
        super(builder);

        this.element2ETD = builder.getElement2ETD();
        this.nonReplaceableAttributes = builder.getNonReplaceableAttributes();
    }

    // Javadoc inherited.
    public boolean isNonReplaceableAttribute(String name) {
        return nonReplaceableAttributes.contains(name);
    }

    // Javadoc inherited.
    public ETD getETD(String name) {
        ETD etd = (ETD) element2ETD.get(name);
        if (etd == null) {
            etd = DEFAULT_ETD;
        }
        return etd;
    }

    // Javadoc inherited.
    public DocumentWriter createDocumentWriter(Writer writer) {
        return new SGMLDocumentWriter(writer, this);
    }
}
