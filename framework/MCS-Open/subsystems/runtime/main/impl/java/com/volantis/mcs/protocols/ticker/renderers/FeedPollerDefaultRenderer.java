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

package com.volantis.mcs.protocols.ticker.renderers;

import java.io.IOException;
import java.io.StringWriter;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ticker.attributes.FeedPollerAttributes;

/**
 * Renderer for FeedPoller element 
 */
public class FeedPollerDefaultRenderer extends ElementDefaultRenderer {

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require script module
        require(WIDGET_TICKER, protocol, attributes);        

        FeedPollerAttributes feedPollerAttributes = (FeedPollerAttributes) attributes;
        
        // Prepare Javascript content.
        StringWriter scriptWriter = new StringWriter();
        
        scriptWriter.write("Ticker.createFeedPoller({");

        scriptWriter.write("url:" + createJavaScriptString(feedPollerAttributes.getUrl()));
        
        if (feedPollerAttributes.getMinPollingInterval() != null)
            scriptWriter.write(", minPollingInterval:" + feedPollerAttributes.getMinPollingInterval());
        
        if (feedPollerAttributes.getMaxPollingInterval() != null)
            scriptWriter.write(", maxPollingInterval:" + feedPollerAttributes.getMaxPollingInterval());
        
        scriptWriter.write("})");
        
        addCreatedWidgetId(protocol.getMarinerPageContext().generateFCID(FEED_POLLER_ID_SUFFIX));
        
        // Write JavaScript content to DOM.
        writeJavaScript(scriptWriter.toString());
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                
    }
}
