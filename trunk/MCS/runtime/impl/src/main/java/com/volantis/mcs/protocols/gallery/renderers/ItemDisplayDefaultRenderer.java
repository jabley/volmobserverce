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

package com.volantis.mcs.protocols.gallery.renderers;

import java.io.IOException;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.attributes.BlockAttributes;

/**
 * Renderer for ItemDisplay element 
 */
public class ItemDisplayDefaultRenderer extends ElementDefaultRenderer {
    /**
     * Attributes of the div element enclosing the widget content.
     */
    private BlockAttributes blockAttributes;

    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }        

        // Require libraries
        requireStandardLibraries(protocol);
        
        requireLibrary("/vfc-gallery.mscr",protocol);
        
        // Render Presenter opening.
        blockAttributes = new BlockAttributes();
        
        blockAttributes.copy(attributes);
        
        blockAttributes.setId(protocol.getMarinerPageContext().generateUniqueFCID());
        
        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(blockAttributes)
            .renderOpen(protocol, blockAttributes);
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if(!isWidgetSupported(protocol)) {
            return;
        }                

        getWidgetDefaultModule(protocol)
            .getWidgetRenderer(blockAttributes)
            .renderClose(protocol, blockAttributes);

        StringBuffer scriptBuffer = new StringBuffer();

        String id = attributes.getId();
        
        if (id == null) {
            id = protocol.getMarinerPageContext().generateUniqueFCID();
        }
        
        scriptBuffer.append("Widget.register(")
            .append(createJavaScriptString(id))
            .append(",")
            .append("new Gallery.ItemDisplay(")
            .append(createJavaScriptWidgetReference(blockAttributes.getId()))
            .append(", ")
            .append(createJavaScriptString(getGalleryDefaultModule(protocol).getItemDisplayMode()))
            .append("))");
        
        addCreatedWidgetId(id);
        
        addUsedWidgetId(blockAttributes.getId());

        // Write JavaScript content to DOM.
        try {
            getJavaScriptWriter().write(scriptBuffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }

        getGalleryDefaultModule(protocol).itemDisplayRendered(id);
    }
    
    // Javadoc inherited
    public boolean shouldRenderContents(VolantisProtocol protocol, MCSAttributes attributes) {
        return isWidgetSupported(protocol);
    }
}
