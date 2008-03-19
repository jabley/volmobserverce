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

package com.volantis.mcs.protocols.ticker;

import java.util.HashMap;
import java.util.Map;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ticker.attributes.ChannelsCountAttributes;
import com.volantis.mcs.protocols.ticker.attributes.FeedAttributes;
import com.volantis.mcs.protocols.ticker.attributes.FeedPollerAttributes;
import com.volantis.mcs.protocols.ticker.attributes.ItemChannelAttributes;
import com.volantis.mcs.protocols.ticker.attributes.ItemDescriptionAttributes;
import com.volantis.mcs.protocols.ticker.attributes.ItemDisplayAttributes;
import com.volantis.mcs.protocols.ticker.attributes.ItemIconAttributes;
import com.volantis.mcs.protocols.ticker.attributes.ItemPlainDescriptionAttributes;
import com.volantis.mcs.protocols.ticker.attributes.ItemTitleAttributes;
import com.volantis.mcs.protocols.ticker.attributes.ItemsCountAttributes;
import com.volantis.mcs.protocols.ticker.attributes.UpdateStatusAttributes;
import com.volantis.mcs.protocols.ticker.renderers.ChannelsCountDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.ElementRenderer;
import com.volantis.mcs.protocols.ticker.renderers.FeedDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.FeedPollerDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.ItemChannelDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.ItemDescriptionDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.ItemDisplayDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.ItemIconDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.ItemPlainDescriptionDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.ItemTitleDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.ItemsCountDefaultRenderer;
import com.volantis.mcs.protocols.ticker.renderers.UpdateStatusDefaultRenderer;

/**
 * Implementation of Ticker module suitable for XHTMLBasic protocol.
 * 
 * Internally stores instances of renderers. The lifetime of this
 * class MUST be the same as the lifetime of request. The instance
 * MUST NOT be shared between request.
 * 
 */
public class TickerDefaultModule implements TickerModule {

     /**
     * Static immutable map specifying what renderer class should 
     * be used for each element. Actual instances of renderers are
     * created lazily, only when needed, and have lifetime of 
     * request.
     */  
    private static final Map renderersClassMap = new HashMap();     
    static {
        renderersClassMap.put(FeedPollerAttributes.class, FeedPollerDefaultRenderer.class);
        renderersClassMap.put(UpdateStatusAttributes.class, UpdateStatusDefaultRenderer.class);
        renderersClassMap.put(ItemsCountAttributes.class, ItemsCountDefaultRenderer.class);
        renderersClassMap.put(ChannelsCountAttributes.class, ChannelsCountDefaultRenderer.class);
        renderersClassMap.put(ItemDisplayAttributes.class, ItemDisplayDefaultRenderer.class);
        renderersClassMap.put(ItemTitleAttributes.class, ItemTitleDefaultRenderer.class);
        renderersClassMap.put(ItemIconAttributes.class, ItemIconDefaultRenderer.class);
        renderersClassMap.put(ItemDescriptionAttributes.class, ItemDescriptionDefaultRenderer.class);
        renderersClassMap.put(ItemPlainDescriptionAttributes.class, ItemPlainDescriptionDefaultRenderer.class);
        renderersClassMap.put(ItemChannelAttributes.class, ItemChannelDefaultRenderer.class);
        renderersClassMap.put(FeedAttributes.class, FeedDefaultRenderer.class);
}
   
    /**
     * Instances of renderers
     */
    private Map renderersMap = new HashMap();
    
    /**
     * Returns a renderer for a ticker element. If this method is called several times
     * during a single request, the same instance of renderer is returned each time.
     *
     * @throws ProtocolException 
     */
    public ElementRenderer getElementRenderer(MCSAttributes attributes) 
            throws ProtocolException {
        return (null != attributes) ? 
                getElementRenderer(attributes.getClass()) : null;
    }

    /**
     * Returns a renderer for a ticker element. If this method is called several times
     * during a single request, the same instance of renderer is returned each time.
     *
     * @throws ProtocolException 
     */
    public ElementRenderer getElementRenderer(Class attributesClass) 
            throws ProtocolException {

        // First, try to get already instantiated instance of the renderer.
        ElementRenderer renderer = (ElementRenderer)renderersMap.get(attributesClass);
        
        // If it wasn't instantiated yet, do it now.
        if (null == renderer) {
            Class rendererClass = (Class)renderersClassMap.get(attributesClass);
            
            if (null != rendererClass) {
                try {
                    renderer = (ElementRenderer)rendererClass.newInstance();
                    renderersMap.put(attributesClass, renderer);
                    // If we are unable to instantiate a renderer, we report a protocol error 
                    // as this is not something that we expect to happen in normal circumastances.
                } catch (InstantiationException e) {
                    throw new ProtocolException(e);
                } catch (IllegalAccessException e) {
                    throw new ProtocolException(e);
                }
            }
        }
        
        return renderer;
    }
}
 
