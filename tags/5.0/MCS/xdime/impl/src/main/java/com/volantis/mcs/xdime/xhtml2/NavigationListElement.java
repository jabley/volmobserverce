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
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.renderers.DynamicMenuWidgetRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * XHTML V2 NavigationList element object.
 */
public class NavigationListElement extends GenericListElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(NavigationListElement.class);

    public NavigationListElement(XDIMEContextInternal context) {
        super(XHTML2Elements.NL, context);

        protocolAttributes = new NavigationListAttributes();
    }

    /**
     * Check if element should be rendered like dynamic menu or like standard XDIME 2 navigation list 
     * @param protocol VolantisProtocol
     * @return state of rendering element as dynamic menu element 
     */
    protected boolean isDynamicMenu(VolantisProtocol protocol) {
        
        if(!protocol.getProtocolConfiguration().isFrameworkClientSupported() || !protocol.supportsJavaScript()) {
            return false;
        }

        StyleValue menuTypeValue = null;           
        Styles styles = ((NavigationListAttributes) protocolAttributes).getStyles();
        if(styles != null) {
            menuTypeValue = styles.getPropertyValues().getComputedValue(StylePropertyDetails.MCS_MENU_STYLE);
        }
            
        return (menuTypeValue != null
                && menuTypeValue.getStandardCSS() == "dynamic");
    }
    
    
    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);

        try {            
            if (isDynamicMenu(protocol)) {

                WidgetModule widgetModule = protocol.getWidgetModule();
                // Do nothing if widgets are not supported at all
                if (null == widgetModule) {
                    return XDIMEResult.SKIP_ELEMENT_BODY;
                }

                DynamicMenuWidgetRenderer dynamicMenuRenderer 
                    = widgetModule.getDynamicMenuRenderer();
                
                if (null == dynamicMenuRenderer) {
                    return XDIMEResult.SKIP_ELEMENT_BODY;
                }
                // render nl as menu's submenu
                dynamicMenuRenderer.renderNlOpen(getProtocol(context),
                        (NavigationListAttributes) protocolAttributes);
            } else {
                protocol.writeOpenUnorderedList((UnorderedListAttributes) protocolAttributes);
            }
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);

            throw new XDIMEException(exceptionLocalizer.format(
                    "rendering-error", getTagName()), e);
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;

    }

    // Javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);        

        if (isDynamicMenu(protocol)) {

            WidgetModule widgetModule = protocol.getWidgetModule();

            if (null == widgetModule) {
                return;
            }

            try {
                DynamicMenuWidgetRenderer dynamicMenuRenderer 
                    = widgetModule.getDynamicMenuRenderer();

                if (null == dynamicMenuRenderer) {
                    return;
                }
                // render nl as menu's submenu
                dynamicMenuRenderer.renderNlClose(getProtocol(context),
                        (NavigationListAttributes) protocolAttributes);
            } catch (ProtocolException e) {
                logger.error("rendering-error", getTagName(), e);
                throw new XDIMEException(exceptionLocalizer.format(
                        "rendering-error", getTagName()), e);
            }
        } else {
            protocol.writeCloseUnorderedList((UnorderedListAttributes) protocolAttributes);
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
 * 21-Sep-05 9128/4 pabbott VBM:2005071114 Review feedback for XHTML2 elements
 * 
 * 20-Sep-05 9128/2 pabbott VBM:2005071114 Add XHTML 2 elements
 * 
 * ===========================================================================
 */
