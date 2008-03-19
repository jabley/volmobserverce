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

import com.volantis.mcs.protocols.widgets.attributes.TableAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * Table Widget element.
 */
public class TableElement extends WidgetElement {
    /**
     * Creates and initialises new instance of Deck element.
     * @param context
     */
    public TableElement(XDIMEContextInternal context) {
        super(WidgetElements.TABLE, context);
        
        protocolAttributes = new TableAttributes();
    }

    public TableAttributes getTableAttributes() {
        return (TableAttributes) protocolAttributes;
    }

    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        TableAttributes tableAttributes = getTableAttributes();
        
        com.volantis.mcs.protocols.TableAttributes xhtml2Attributes =
            new com.volantis.mcs.protocols.TableAttributes();
        
        xhtml2Attributes.copy(tableAttributes);
        
        tableAttributes.setXHTML2Attributes(xhtml2Attributes);
    }

    // Javadoc inherited
    protected void styleElementStart(XDIMEContextInternal context, XDIMEAttributes attributes) {
        super.styleElementStart(context, attributes);
        
        getTableAttributes().getXHTML2Attributes().setStyles(protocolAttributes.getStyles());
    }    
}
