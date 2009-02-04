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
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.NavigationListItemAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.protocols.widgets.renderers.AutocompleteRenderer;
import com.volantis.mcs.protocols.widgets.renderers.DynamicMenuWidgetRenderer;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.response.ResponseAutocompleteElement;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * XHTML V2 ListItem element object.
 */
public class ListItemElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(ListItemElement.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory
            .createExceptionLocalizer(ListItemElement.class);

    public ListItemElement(XDIMEContextInternal context) {
        super(XHTML2Elements.LI, context);

        protocolAttributes = new NavigationListItemAttributes();
    }

    /**
     * Check if element should be rendered as dynamic menu item 
     * 
     * @param xdimeElement
     *            XDIMEElementInternal
     * @param protocol
     *            VolantisProtocol
     * @return state if rendering element is treated as dynamic menu item
     */
    private boolean isDynamicMenu(XDIMEElementInternal xdimeElement,
            VolantisProtocol protocol) {

        if (xdimeElement instanceof NavigationListElement) {
            return ((NavigationListElement) xdimeElement)
                    .isDynamicMenu(protocol);
        } else {
            return false;
        }
    }

    /**
     * Check if element should be rendered as autocompleter item
     */ 
    private boolean isAutocompleter(XDIMEElementInternal parent) {
        return (parent instanceof ResponseAutocompleteElement);
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        VolantisProtocol protocol = getProtocol(context);

        try {
            if (isDynamicMenu(parent, protocol)) {
                DynamicMenuWidgetRenderer dynamicMenuRenderer 
                    = getDynamicMenuRenderer(protocol);

                if (null == dynamicMenuRenderer) {
                    return XDIMEResult.SKIP_ELEMENT_BODY;
                }
                // render li element as div element - item dynamic menu
                dynamicMenuRenderer.renderLiOpen(
                        protocol, (ListItemAttributes) protocolAttributes);
                
            } else if (isAutocompleter(parent)) {
                // render LI element in special way if it is
                // response:autocomplete item
                // it generate unuque id attribute for parent element if is
                // needed and set default styles for rendered element
                AutocompleteRenderer autocompleteRenderer 
                    = getAutocompleteRenderer(protocol);
                
                if (null == autocompleteRenderer) {
                    return XDIMEResult.SKIP_ELEMENT_BODY;                    
                }
                    
                autocompleteRenderer.renderLiOpen(
                        protocol, (ListItemAttributes) protocolAttributes);
            } else {
                // default rendring
                protocol.writeOpenListItem((ListItemAttributes) protocolAttributes);
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

        try {
            if (isDynamicMenu(context.getCurrentElement(), protocol)) {
                DynamicMenuWidgetRenderer dynamicMenuRenderer 
                        = getDynamicMenuRenderer(protocol);
                if (null != dynamicMenuRenderer) {
                    // render li element as div element - item dynamic menu
                    dynamicMenuRenderer.renderLiClose(
                            protocol, (ListItemAttributes) protocolAttributes);
                }
            } else if (isAutocompleter(parent)) {
                // render as AutocompleteResponse
                // render javascript register active style method if is needed
                AutocompleteRenderer autocompleteRenderer 
                    = getAutocompleteRenderer(protocol);
                if (null != autocompleteRenderer) {
                    autocompleteRenderer.renderLiClose(
                            protocol, (ListItemAttributes) protocolAttributes);
                }
            } else {
                // default rendring
                protocol.writeCloseListItem((ListItemAttributes) protocolAttributes);                
            }            
        } catch (ProtocolException e) {
            logger.error("rendering-error", getTagName(), e);
            throw new XDIMEException(exceptionLocalizer.format(
                    "rendering-error", getTagName()), e);
        }
    }

    /** 
     * Convenience methiod for accessing DynamicMenuWidgetRenderer
     */
    private DynamicMenuWidgetRenderer getDynamicMenuRenderer(VolantisProtocol protocol) 
            throws ProtocolException {
        WidgetModule widgetModule = protocol.getWidgetModule();
        return (widgetModule != null) ?
            widgetModule.getDynamicMenuRenderer() : null;
    }

    /** 
     * Convenience methiod for accessing AutocompleteRenderer
     */
    private AutocompleteRenderer getAutocompleteRenderer(VolantisProtocol protocol) 
            throws ProtocolException {
        WidgetModule widgetModule = protocol.getWidgetModule();
        return (widgetModule != null) ?
            widgetModule.getAutocompleteRenderer() : null;        
    }
}

/*
 * ===========================================================================
 * Change History
 * ===========================================================================
 * $Log$
 * 
 * 12-Oct-05 9673/4 pduffin VBM:2005092906 Improved validation and fixed layout
 * formatting
 * 
 * 10-Oct-05 9673/2 pduffin VBM:2005092906 Improved validation and fixed layout
 * formatting
 * 
 * 21-Sep-05 9128/3 pabbott VBM:2005071114 Review feedback for XHTML2 elements
 * 
 * 20-Sep-05 9128/1 pabbott VBM:2005071114 Add XHTML 2 elements
 * 
 * ===========================================================================
 */
