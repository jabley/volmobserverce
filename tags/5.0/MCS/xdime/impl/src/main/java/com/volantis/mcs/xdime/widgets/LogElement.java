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

import com.volantis.mcs.protocols.widgets.attributes.LogAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * Log XDIME element.
 */
public class LogElement extends WidgetElement {

    /**
     * Creates and initialises new instance of Log element.
     * @param context
     */
    public LogElement(XDIMEContextInternal context) {
        // Initialise superclass.
        super(WidgetElements.LOG, context);

        // Create an instance of Log attributes.
        // It'll be initialised later in initialiseAttributes() method.
        protocolAttributes = new LogAttributes();
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        LogAttributes logAttributes = (LogAttributes) protocolAttributes;
        
        logAttributes.setTopics(attributes.getValue("", "topics"));
    }
}
