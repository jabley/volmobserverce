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

/**
 * Element Type Definition.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ETD {

    /**
     * Check to see if the element with the specified name requires an end tag,
     * or not.
     *
     * <p>An element that has an optional end tag and also has an empty content
     * model must not be closed, either by an explicit close tag, or as part
     * of an empty tag. See http://www.w3.org/TR/html4/intro/sgmltut.html for
     * more details.</p>
     *
     * @return True if the end tag is optional for this element, false
     *         otherwise.
     */
    boolean isEndTagOptional();

    /**
     * Get the content model for the specified element.
     *
     * <p>The content of an element with a content model of CDATA must not
     * have special characters (&amp;, &lt;, etc) replaced with character
     * references.</p>
     *
     * <p>See {@link #isEndTagOptional()}.</p>
     *
     * @return The element model.
     */
    ElementModel getElementModel();
}
