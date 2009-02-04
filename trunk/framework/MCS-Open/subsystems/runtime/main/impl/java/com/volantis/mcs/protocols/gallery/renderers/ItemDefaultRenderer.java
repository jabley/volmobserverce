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
import com.volantis.mcs.protocols.gallery.attributes.ItemsAttributes;
import com.volantis.mcs.runtime.scriptlibrarymanager.WidgetScriptModules;

/**
 * Renderer for ItemsCount element 
 */
public class ItemDefaultRenderer extends BaseGalleryDefaultRenderer {
    private String summarySlideId;
    private String detailSlideId;
    
    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }
        require(WIDGET_GALLERY, protocol, attributes, true);
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }
        
        String itemId = attributes.getId();
        
        if (itemId == null) {
            itemId = protocol.getMarinerPageContext().generateUniqueFCID();
        }
        
        StringBuffer buffer = new StringBuffer();

        buffer.append("Widget.register(")
            .append(createJavaScriptString(itemId))
            .append(", new Gallery.Item(")
            .append(createJavaScriptWidgetReference(summarySlideId))
            .append(", ")
            .append(createJavaScriptWidgetReference(detailSlideId))
            .append("))");
        
        addCreatedWidgetId(itemId);
        
        addUsedWidgetId(summarySlideId);
        
        addUsedWidgetId(detailSlideId);
        
        ((ItemsDefaultRenderer)getGalleryDefaultModule(protocol)
            .getElementRenderer(ItemsAttributes.class))
            .addItemId(itemId);
        
        try {
            getJavaScriptWriter().write(buffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
    }

    /**
     * @param detailSlideId The detailWidgetId to set.
     */
    public void setDetailSlideId(String detailSlideId) {
        this.detailSlideId = detailSlideId;
    }

    /**
     * @param summarySlideId The summaryWidgetId to set.
     */
    public void setSummarySlideId(String summarySlideId) {
        this.summarySlideId = summarySlideId;
    }
}
