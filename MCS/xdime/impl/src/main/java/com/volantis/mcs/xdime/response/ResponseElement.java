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

package com.volantis.mcs.xdime.response;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.xdime.StylableXDIMEElement;
import com.volantis.mcs.xdime.StyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xml.schema.model.ElementType;

/**
 * Base class for all elements from widgets-response namespace.
 * 
 * Elements from this namespace are used for building responses to AJAX requests
 * in a device-independent way.
 */
public abstract class ResponseElement extends StylableXDIMEElement {

    public ResponseElement(ElementType type, XDIMEContextInternal context) {
        super(type, StyledStrategy.STRATEGY,
                context);
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {
    }

    /**
     * Creates and returns new output buffer on specified context.
     * 
     * @param context The context to create buffer on.
     * @return created buffer
     */
    protected OutputBuffer createOutputBuffer(XDIMEContextInternal context) {
        return getProtocol(context).getOutputBufferFactory()
                .createOutputBuffer();
    }
}
