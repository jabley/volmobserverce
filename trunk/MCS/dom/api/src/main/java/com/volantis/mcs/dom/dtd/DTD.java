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

package com.volantis.mcs.dom.dtd;

import com.volantis.mcs.dom.output.DocumentWriter;

import java.io.Writer;

/**
 * Encapsulates information that is common to XML and SGML DTDs.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface DTD {

    /**
     * Get the maximum line length in characters.
     *
     * <p>0 signifies that there is no maximum length.</p>
     *
     * @return The maximum line length in characters.
     */
    int getMaximumLineLength();

    /**
     * Check to see whether the attribute with the specified name is non
     * standard in that it contains non replaceable characters.
     *
     * @param name The attribute name.
     * @return True if the attribute is non replaceeable, false otherwise.
     */
    boolean isNonReplaceableAttribute(String name);

    /**
     * Create a {@link DocumentWriter} that will serialize a document according
     * to this DTD.
     *
     * @param writer The underlying IO writer.
     * @return The newly created {@link DocumentWriter}.
     */
    DocumentWriter createDocumentWriter(Writer writer);

    /**
     * Check to see whether empty elements require a space
     * @return
     */
    boolean getEmptyTagRequiresSpace();

    boolean isElementIgnoreable(String name);
}
