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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.gallery.attributes.ItemsAttributes;
import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;

/**
 * Renderer for ItemsElement.
 * 
 * Works both for XDIME, and response.
 * 
 * It also performs actual rendering of following elements:
 *  - ItemElement
 *  - SummaryElement
 *  - DefailElement
 */
public class ItemsDefaultRenderer extends ElementDefaultRenderer {
    private List itemIds;
    
    // Javadoc inherited
    public void doRenderOpen(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {
        
        if (!isWidgetSupported(protocol)) {
            return;
        }

        ItemsAttributes itemsAttributes = (ItemsAttributes) attributes;

        boolean insideWidgetResponse = (itemsAttributes.getCount() != null);
        
        if (!insideWidgetResponse) {
            renderOpenForWidget(protocol, itemsAttributes);
        } else {
            renderOpenForResponse(protocol, itemsAttributes);
        }
        
        itemIds = new ArrayList();
    }

    // Javadoc inherited
    public void doRenderClose(VolantisProtocol protocol, MCSAttributes attributes)
            throws ProtocolException {

        if (!isWidgetSupported(protocol)) {
            return;
        }

        ItemsAttributes itemsAttributes = (ItemsAttributes) attributes;

        boolean insideWidgetResponse = (itemsAttributes.getCount() != null);

        if (!insideWidgetResponse) {
            renderCloseForWidget(protocol, itemsAttributes);
        } else {
            renderCloseForResponse(protocol, itemsAttributes);
        }

        itemIds = null;
    }

    private void renderOpenForWidget(VolantisProtocol protocol, ItemsAttributes attributes)
        throws ProtocolException {
        
        // Require libraries
        requireStandardLibraries(protocol);
        
        requireLibrary("/vfc-transitions.mscr", protocol);
        requireLibrary("/vfc-gallery.mscr", protocol);
    }
    
    private void renderCloseForWidget(VolantisProtocol protocol, ItemsAttributes attributes)
        throws ProtocolException {
        LoadAttributes loadAttributes = attributes.getLoadAttributes();

        // Finally, render the JavaScript part.
        String itemsId = attributes.getId();
        
        if (itemsId == null) {
            itemsId = protocol.getMarinerPageContext().generateFCID("GALLERY_ITEMS");
        }
        
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("Widget.register(")
            .append(createJavaScriptString(itemsId))
            .append(", ")
            .append("Gallery.createItems({");
        
        addCreatedWidgetId(itemsId);

        // There were no items rendered.
        if (loadAttributes != null) {
            // If items are to be loaded, render items as null.
            buffer.append("loadURL:" + createJavaScriptString(loadAttributes.getSrc()));

            if (loadAttributes.getWhen() != null) {
                if (loadAttributes.getWhen().equals("defer")) {
                    buffer.append(",loadOnDemand:true");
                } if (loadAttributes.getWhen().equals("onload")) {
                    buffer.append(",loadOnDemand:false");                        
                }
            }
                
        } else {
            // If items are not to be loaded, it means that the set of
            // items is empty. Render empty array in that case.
            buffer.append("items:[");
            
            Iterator iterator = itemIds.iterator();
            
            boolean firstItemId = true;
            
            while (iterator.hasNext()) {
                String itemId = (String) iterator.next();
                
                if (firstItemId) {
                    firstItemId = false;
                } else {
                    buffer.append(", ");
                }
                
                buffer.append(createJavaScriptWidgetReference(itemId));
                
                addUsedWidgetId(itemId);
            }
            
            buffer.append("]");
        }
        
        buffer.append("}))");
        
        try {
            getJavaScriptWriter().write(buffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
        
        itemIds = null;
    }

    private void renderOpenForResponse(VolantisProtocol protocol, ItemsAttributes attributes)
        throws ProtocolException {

    }

    private void renderCloseForResponse(VolantisProtocol protocol, ItemsAttributes attributes)
        throws ProtocolException {
        // Finally, render the JavaScript part.
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("Gallery.itemsRequest.setResponse(")
            .append("new Gallery.Response([");
        
        Iterator iterator = itemIds.iterator();
            
        boolean firstItemId = true;
            
        while (iterator.hasNext()) {
            String itemId = (String) iterator.next();
                
            if (firstItemId) {
                firstItemId = false;
            } else {
                buffer.append(", ");
            }
                
            buffer.append(createJavaScriptWidgetReference(itemId));
                
            addUsedWidgetId(itemId);
        }
            
        buffer.append("], ")
            .append(attributes.getCount())
            .append("))");
        
        try {
            getJavaScriptWriter().write(buffer.toString());
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
        
        itemIds = null;
    }
    
    public void addItemId(String id) {
        itemIds.add(id);
    }
}
