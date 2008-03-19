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

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.attributes.TabsAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;
import com.volantis.mcs.protocols.widgets.renderers.TabsRenderer;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Class for tabs XDIME2 element
 */
public class TabsElement extends WidgetElement {

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
        
    /**
     * The container instance used as the tabs content container
     */
    private RegionInstance labelsRegionInstance;        
    private RegionInstance contentsRegionInstance;        
    
    
    public TabsElement(XDIMEContextInternal context) {
        super(WidgetElements.TABS, context);
        protocolAttributes = new TabsAttributes();
    }
    
    public XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes) 
            throws XDIMEException {

        WidgetModule widgetModule = getWidgetModule(context);
        // Do nothing if widgets are not supported at all
        if (null == widgetModule) {
            // Do fallback if widget is not supported by the protocol
            return doFallbackOpen(context, attributes);        
        }
        try {
            TabsRenderer tabsRenderer = widgetModule.getTabsRenderer();
            if (null == tabsRenderer){
                // Do fallback if widget is not supported by the protocol
                return doFallbackOpen(context, attributes);        
            }
            // Send page context to the renderer via attributes.
            // This is necessary to switch page buffers for contents/labels
            // in the renderer.
            ((TabsAttributes)protocolAttributes)
                    .setPageContext(getPageContext(context));
            
            tabsRenderer.renderOpen(
                    getProtocol(context), 
                    (WidgetAttributes)protocolAttributes);
            
            MarinerPageContext pageContext = getPageContext(context);        

            //create anonymous layout for tab's contents
            contentsRegionInstance = createAnonymousRegion(pageContext);

            //create anonymous layout for tab's labels 
            labelsRegionInstance = createAnonymousRegion(pageContext);
            
            //set created anonymous layouts in tabs renderer 
            tabsRenderer.setContentsRegionInstance(contentsRegionInstance);            
            tabsRenderer.setLabelsRegionInstance(labelsRegionInstance);
            
            // Open tabs elemen            
            tabsRenderer.renderTabsOpen(
                    getProtocol(context), (WidgetAttributes)protocolAttributes);
            
            if (!tabsRenderer.shouldRenderContents(getProtocol(context), 
            		(WidgetAttributes)protocolAttributes)) {
                return XDIMEResult.SKIP_ELEMENT_BODY; 
            }
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
            
            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }    
        return XDIMEResult.PROCESS_ELEMENT_BODY;        
    }
    
    public void callCloseOnProtocol(XDIMEContextInternal context) 
            throws XDIMEException {
        
        WidgetModule widgetModule =  getWidgetModule(context);        
        // Do nothing if widgets are not supported at all
        if (null == widgetModule){
            // Do fallback if widget is not supported by the protocol
            doFallbackClose(context);
            return;       
        }

        try {        
            TabsRenderer tabsRenderer = widgetModule.getTabsRenderer();
            if (null == tabsRenderer) {
                // Do fallback if widget is not supported by the protocol
                doFallbackClose(context);
                return;
            }                                    
            // close tabs element
            tabsRenderer.renderTabsClose(getProtocol(context), 
                    (WidgetAttributes)protocolAttributes);
            tabsRenderer.renderClose(getProtocol(context), 
                    (WidgetAttributes)protocolAttributes);
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
    
            throw new XDIMEException(exceptionLocalizer.format(
                "rendering-error", getTagName()), e);
        }    
    }

}
