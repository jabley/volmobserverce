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

package com.volantis.styling.device;

import com.volantis.styling.engine.Attributes;

/**
 * A styling engine that is used to determine the styles that the device would
 * apply to the output document.
 *
 * <p>This is a very limited version of the standard styling engine and only
 * supports the following features:</p>
 * <ul>
 * <li>Single compiled style sheet.</li>
 * <li>A partial cascade as it does not fully populate the styles based off
 * initial and inherited values.</li>
 * <li>Preservation of priority in the resulting styles.</li>
 * </ul> 
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface DeviceStylingEngine {

    /**
     * Called before processing an element and its children.
     *
     * @param localName  The name of the element.
     * @param attributes The attributes of the element.
     * @return The {@link DeviceStyles} which will never be null.
     */
    DeviceStyles startElement(String localName, Attributes attributes);

    /**
     * Called after processing an element and its children.
     *
     * @param localName The name of the element.
     */
    void endElement(String localName);
}
