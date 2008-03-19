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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.NavigationListAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.renderers.DynamicMenuWidgetRenderer;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * XHTML V2 List Label element object.
 */
public class ListLabelElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(AnchorElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory
            .createExceptionLocalizer(AnchorElement.class);

    public ListLabelElement(XDIMEContextInternal context) {
        super(XHTML2Elements.LABEL, context);
        protocolAttributes = new NavigationListAttributes();
    }
    
    /**
     * Check if element should be rendered like dynamic menu item or like
     * standard XDIME 2 navigation list item
     *
     * @param xdimeElement XDIMEElementInternal
     * @param protocol VolantisProtocol
     * @return state if rendering element is treated as dynamic menu item
     */
    private boolean isDynamicMenu(XDIMEElementInternal xdimeElement,
                                  VolantisProtocol protocol) {

        if (xdimeElement instanceof NavigationListElement) {
            return ((NavigationListElement)xdimeElement).isDynamicMenu(protocol);
        } else {
            return false;
        }
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);

        // The label should only render its body if it is not within a top
        // level list and the parent list is rendered as a dynamic menu. So by
        // default don't render out the label, as otherwise it will be
        // directly inside the containing list element which is invalid. 
        boolean shouldRenderBody = false;
        
        if (isDynamicMenu(parent, protocol)) {

            WidgetModule widgetModule = protocol.getWidgetModule();
            if (null == widgetModule) {
                return XDIMEResult.SKIP_ELEMENT_BODY;
            }

            try {
                DynamicMenuWidgetRenderer dynamicMenuRenderer 
                    = widgetModule.getDynamicMenuRenderer();
                
                if (null == dynamicMenuRenderer) {
                    return XDIMEResult.SKIP_ELEMENT_BODY;
                }                    
                shouldRenderBody = dynamicMenuRenderer.renderLabelOpen(
                        getProtocol(context), protocolAttributes);
                
            } catch (ProtocolException e) {
                logger.error("rendering-error", getTagName(), e);

                throw new XDIMEException(exceptionLocalizer.format(
                        "rendering-error", getTagName()), e);
            }
        }
        if(shouldRenderBody == true) {
            return XDIMEResult.PROCESS_ELEMENT_BODY;
        } else {
            return XDIMEResult.SKIP_ELEMENT_BODY;            
        }
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);

        if (isDynamicMenu(context.getCurrentElement(), protocol)) {
            WidgetModule widgetModule = protocol.getWidgetModule();
            // Do nothing if widgets are not supported at all
            if (null == widgetModule) {
                return;
            }
            
            try {
                DynamicMenuWidgetRenderer dynamicMenuRenderer 
                    = widgetModule.getDynamicMenuRenderer();
                
                if (null == dynamicMenuRenderer) {
                    return;
                }
                // render label as dynamic menu item which has submenu 
                dynamicMenuRenderer.renderLabelClose(
                        getProtocol(context), protocolAttributes);
                
            } catch (ProtocolException e) {
                logger.error("rendering-error", getTagName(), e);
                throw new XDIMEException(exceptionLocalizer.format(
                        "rendering-error", getTagName()), e);
            }
        }

    }

}

/*
 * ===========================================================================
 * Change History
 * ===========================================================================
 * $Log$
 * 
 * 12-Oct-05 9673/5 pduffin VBM:2005092906 Improved validation and fixed layout
 * formatting
 * 
 * 10-Oct-05 9673/3 pduffin VBM:2005092906 Improved validation and fixed layout
 * formatting
 * 
 * 30-Sep-05 9562/1 pabbott VBM:2005092011 Add XHTML2 Object element
 * 
 * 21-Sep-05 9128/6 pabbott VBM:2005071114 Review feedback for XHTML2 elements
 * 
 * 20-Sep-05 9128/4 pabbott VBM:2005071114 Add XHTML 2 elements
 * 
 * ===========================================================================
 */
