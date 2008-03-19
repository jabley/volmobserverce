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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.OptionAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * Class for XDIME2 widget:option element, specify item in select element 
 * - equivalent to item from XForms 
 */

public class OptionElement extends WidgetElement {
    public OptionElement(XDIMEContextInternal context) {
        super(WidgetElements.OPTION, context);
        protocolAttributes = new OptionAttributes();
    }
    
    /**
     * value - specifies value for option element
     */
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        
        ((OptionAttributes) protocolAttributes).setValue(attributes.getValue("", "value"));
        ((OptionAttributes) protocolAttributes).setSelected(attributes.getValue("", "selected"));
    }
    
    
}
