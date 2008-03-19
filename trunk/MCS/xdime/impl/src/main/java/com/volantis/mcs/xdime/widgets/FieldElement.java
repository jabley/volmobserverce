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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.FieldAttributes;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * Field widget element.
 */
public class FieldElement extends WidgetElement {
    /**
     * Creates and returns new instance of FieldElement, initalised with empty
     * attributes.
     * @param context
     */
    public FieldElement(XDIMEContextInternal context) {
        // This element is unstyled, and does not require validation.
        super(WidgetElements.FIELD, UnstyledStrategy.STRATEGY,
                context);

        protocolAttributes = new FieldAttributes();
    }

    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {
        FieldAttributes fieldAttributes = (FieldAttributes) protocolAttributes;

        fieldAttributes.setRef(attributes.getValue("", "ref"));

        fieldAttributes.setMessageArea(attributes.getValue("", "message-area"));
    }
}
