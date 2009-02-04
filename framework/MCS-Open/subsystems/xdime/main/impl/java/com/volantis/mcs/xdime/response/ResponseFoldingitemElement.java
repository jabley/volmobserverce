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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.response;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.response.attributes.ResponseFoldingitemAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.synergetics.log.LogDispatcher;


/**
 * Implementation of AJAX resposne for FoldingItem widget.  
 */
public class ResponseFoldingitemElement extends ResponseElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ResponseCarouselElement.class);

    public ResponseFoldingitemElement(XDIMEContextInternal context) {
        super(ResponseElements.FOLDING_ITEM, context);
        protocolAttributes = new ResponseFoldingitemAttributes();
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(
        XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context) {

        //NO-OP
    }


}
