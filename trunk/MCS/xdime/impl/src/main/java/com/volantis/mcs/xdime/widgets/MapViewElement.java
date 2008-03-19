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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.MapViewAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;
import com.volantis.mcs.protocols.widgets.renderers.MapRenderer;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

public class MapViewElement extends WidgetElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(WidgetElement.class);
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(WidgetElement.class);

    
    public MapViewElement(XDIMEContextInternal context){
        super(WidgetElements.MAP_VIEW, context);
        protocolAttributes = new MapViewAttributes();
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        try {
            MapRenderer mapRenderer = null;
            
            // First get the widget module, which is a part of protocol 
            // responsible for rendering widgets. Thec ask for renderer. 
            WidgetModule widgetModule =  getWidgetModule(context);        
            if (null != widgetModule){
                mapRenderer = widgetModule.getMapRenderer();
            }
       
            if (null != mapRenderer){
                mapRenderer.renderMapView(getProtocol(context), 
                        (WidgetAttributes)protocolAttributes);             
            }
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
    
            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }
        // Note: we return SKIP_ELEMENT_BODY which means call close on protocol is never called 
        return XDIMEResult.SKIP_ELEMENT_BODY;       
    }

    
    /**
     * Fallback behaviour for non-HTML protocols that do not support widgets 
     */
    protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes) {
        // Do nothing and skip body
        return XDIMEResult.SKIP_ELEMENT_BODY;
    }
}
