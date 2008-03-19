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

package com.volantis.mcs.xdime.ticker.response;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ticker.response.TickerResponseModule;
import com.volantis.mcs.protocols.ticker.response.attributes.DescriptionAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.ticker.TickerResponseElements;

/**
 * FeedPoller response element.  
 */
public class DescriptionElement extends TickerResponseElement {

    public DescriptionElement(XDIMEContextInternal context) {
        super(TickerResponseElements.DESCRIPTION, context);
        
        protocolAttributes = new DescriptionAttributes();
    }

    // Javadoc inherited
    public void callOpenOnModule(TickerResponseModule module) throws ProtocolException {
        module.openDescription((DescriptionAttributes) protocolAttributes);
    }

    // Javadoc inherited
    public void callCloseOnModule(TickerResponseModule module) throws ProtocolException {
        module.closeDescription((DescriptionAttributes) protocolAttributes);
    }
}
