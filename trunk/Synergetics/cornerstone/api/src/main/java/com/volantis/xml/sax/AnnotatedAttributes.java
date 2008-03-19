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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.sax;

import org.xml.sax.Attributes;

/**
 * An extended version of {@link Attributes} that support an additional per
 * attribute annotation.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface AnnotatedAttributes
        extends Attributes {

    /**
     * Look up an attribute's annotation by index.
     *
     * @param index The attribute index (zero-based).
     * @return The annotation, or null if the index is out of range.
     * @see #getLength
     */
    public abstract Object getAnnotation(int index);

    /**
     * Look up an attribute's annotation by Namespace name.
     *
     * @param uri       The Namespace URI, or the empty String if the
     *                  name has no Namespace URI.
     * @param localName The local name of the attribute.
     * @return The annotation, or null if the index is out of range.
     */
    public abstract Object getAnnotation(String uri, String localName);


    /**
     * Look up an attribute's annotation by XML 1.0 qualified name.
     *
     * @param qName The XML 1.0 qualified name.
     * @return annotation, or null if the index is out of range.
     */
    public abstract Object getAnnotation(String qName);
}
