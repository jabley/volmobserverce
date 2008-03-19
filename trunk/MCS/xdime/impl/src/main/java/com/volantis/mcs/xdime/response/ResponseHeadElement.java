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

import com.volantis.mcs.protocols.response.attributes.ResponseHeadAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;

/**
 * Implementation of head element from widgets-response namespace.
 * 
 * Elements from this namespace are used for building responses to
 * AJAX requests in a device-independent way.
 */
public class ResponseHeadElement extends ResponseElement {
        
    public ResponseHeadElement(XDIMEContextInternal context) {
        super(ResponseElements.HEAD, context);
        protocolAttributes = new ResponseHeadAttributes();
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        //NO-OP
        return XDIMEResult.PROCESS_ELEMENT_BODY;

    }

    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context) {

        //NO-OP
    }        
}
