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

package com.volantis.mcs.protocols.gallery;

import java.util.HashMap;
import java.util.Map;

import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.gallery.attributes.EndItemNumberAttributes;
import com.volantis.mcs.protocols.gallery.attributes.GalleryAttributes;
import com.volantis.mcs.protocols.gallery.attributes.ItemAttributes;
import com.volantis.mcs.protocols.gallery.attributes.ItemDisplayAttributes;
import com.volantis.mcs.protocols.gallery.attributes.ItemNumberAttributes;
import com.volantis.mcs.protocols.gallery.attributes.ItemsAttributes;
import com.volantis.mcs.protocols.gallery.attributes.ItemsCountAttributes;
import com.volantis.mcs.protocols.gallery.attributes.PageNumberAttributes;
import com.volantis.mcs.protocols.gallery.attributes.PagesCountAttributes;
import com.volantis.mcs.protocols.gallery.attributes.SlideshowAttributes;
import com.volantis.mcs.protocols.gallery.attributes.StartItemNumberAttributes;
import com.volantis.mcs.protocols.gallery.renderers.ElementRenderer;
import com.volantis.mcs.protocols.gallery.renderers.EndItemNumberDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.GalleryDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.ItemDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.ItemDisplayDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.ItemNumberDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.ItemsCountDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.ItemsDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.PageNumberDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.PagesCountDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.SlideshowDefaultRenderer;
import com.volantis.mcs.protocols.gallery.renderers.StartItemNumberDefaultRenderer;

/**
 * Implementation of Gallery module suitable for XHTMLBasic protocol.
 * 
 * Internally stores instances of renderers. The lifetime of this
 * class MUST be the same as the lifetime of request. The instance
 * MUST NOT be shared between request.
 * 
 */
public class GalleryDefaultModule implements GalleryModule {

     /**
     * Static immutable map specifying what renderer class should 
     * be used for each element. Actual instances of renderers are
     * created lazily, only when needed, and have lifetime of 
     * request.
     */  
    private static final Map renderersClassMap = new HashMap();     
    static {
        renderersClassMap.put(GalleryAttributes.class, GalleryDefaultRenderer.class);
        renderersClassMap.put(SlideshowAttributes.class, SlideshowDefaultRenderer.class);
        renderersClassMap.put(StartItemNumberAttributes.class, StartItemNumberDefaultRenderer.class);
        renderersClassMap.put(ItemAttributes.class, ItemDefaultRenderer.class);
        renderersClassMap.put(ItemDisplayAttributes.class, ItemDisplayDefaultRenderer.class);
        renderersClassMap.put(ItemNumberAttributes.class, ItemNumberDefaultRenderer.class);
        renderersClassMap.put(ItemsCountAttributes.class, ItemsCountDefaultRenderer.class);
        renderersClassMap.put(PageNumberAttributes.class, PageNumberDefaultRenderer.class);
        renderersClassMap.put(PagesCountAttributes.class, PagesCountDefaultRenderer.class);
        renderersClassMap.put(ItemsAttributes.class, ItemsDefaultRenderer.class);
        renderersClassMap.put(EndItemNumberAttributes.class, EndItemNumberDefaultRenderer.class);
    }
   
    /**
     * Instances of renderers
     */
    private Map renderersMap = new HashMap();
    
    /**
     * Returns a renderer for a gallery element. If this method is called several times
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
     * Returns a renderer for a gallery element. If this method is called several times
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
    
    /**
     * Called when item display widget is rendered.
     * 
     * @param id The ID of the widget.
     */
    public void itemDisplayRendered(String id) throws ProtocolException {
        ((GalleryDefaultRenderer) getElementRenderer(GalleryAttributes.class)).renderItemDisplay(id);
        ((SlideshowDefaultRenderer) getElementRenderer(SlideshowAttributes.class)).renderItemDisplay(id);
    }
    
    public String getItemDisplayMode() throws ProtocolException {
        if (((SlideshowDefaultRenderer) getElementRenderer(SlideshowAttributes.class)).isRendering()) {
            return "detail";
        } else {
            return "summary";
        }
    }
}
 
