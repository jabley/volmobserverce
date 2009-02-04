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

package com.volantis.mcs.xdime;

import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xdime.ElementOutputState;
import com.volantis.mcs.xdime.XDIMEElement;

/**
 * The internal representation of an XDIME element.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface XDIMEElementInternal
        extends XDIMEElement {

    /**
     * Return the {@link ElementOutputState} that is associated with this
     * element. This determines whether the element is currently suppressing
     * output, and if not, calculates where the output should appear. Calling
     * this method will create the state if it does not alreadt exit.
     *
     * @return ElementOutputState   describes if and where this element's
     *                              output markup should appear.
     *                              Will never be null.
     */
    ElementOutputState getOutputState();

    ElementType getElementType();

    /**
     * Get the parent element.
     *
     * @return The parent element.
     */
    XDIMEElementInternal getParent();
}
