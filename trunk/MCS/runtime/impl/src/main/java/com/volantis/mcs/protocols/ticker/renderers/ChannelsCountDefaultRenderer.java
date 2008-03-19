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
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.ticker.attributes.ChannelsCountAttributes;

/**
 * Renderer for ChannelsCount element 
 */
public class ChannelsCountDefaultRenderer extends ElementDefaultRenderer {
    /**
     * Attributes of the rendered span element.
     */
    private SpanAttributes spanAttributes;
    
    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require libraries
        requireLibrary("/prototype.mscr",protocol);
        requireLibrary("/scriptaculous.mscr",protocol);

        requireLibrary("/vfc-base.mscr",protocol);
        requireLibrary("/vfc-ticker.mscr",protocol);
        
        spanAttributes = new SpanAttributes();
        
        spanAttributes.copy(attributes);

        if (spanAttributes.getId() == null) {
            spanAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        }
        
        protocol.writeOpenSpan(spanAttributes);
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        protocol.writeCloseSpan(spanAttributes);

        ChannelsCountAttributes channelsCountAttributes = (ChannelsCountAttributes) attributes;
       
        // Prepare Javascript content.
        StringWriter scriptWriter = new StringWriter();
        
        // Finally, render the JavaScript part.
        scriptWriter.write("Ticker.createChannelsCount({");
        scriptWriter.write("id:" + createJavaScriptString(spanAttributes.getId()));
        
        if (channelsCountAttributes.getRead() != null) {
            scriptWriter.write(",read:" + (channelsCountAttributes.getRead().equals("no") ? "false" : "true"));            
        }
        
        if (channelsCountAttributes.getFollowed() != null) {
            scriptWriter.write(",followed:" + (channelsCountAttributes.getFollowed().equals("no") ? "false" : "true"));            
        }
        
        scriptWriter.write("})");
        
        addUsedFeedPollerId(protocol);
        
        // Write JavaScript content to DOM.
        writeJavaScript(scriptWriter.toString());
    }    
}
