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

package com.volantis.mcs.xdime.response;

import java.util.Stack;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.response.attributes.ResponseLinkAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.widgets.ResponseElements;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.QName;

/**
 * Implementation of link element from widgets-response namespace.
 * 
 * Elements from this namespace are used for building responses to AJAX requests
 * in a device-independent way.
 */

public class ResponseLinkElement extends ResponseElement {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger = LocalizationFactory
            .createLogger(ResponseLinkElement.class);

    /**
     * Immutable Expanded name used for comparing values specified for the
     * elements 'rel' attribute for layouts
     */
    private final static ExpandedName LAYOUT = new ImmutableExpandedName(
            XDIMESchemata.XDIME2_MCS_NAMESPACE, "layout");

    /**
     * Immutable Expanded name used for comparing values specified for the
     * elements 'rel' attribute for themes
     */
    private final static ExpandedName THEME = new ImmutableExpandedName(
            XDIMESchemata.XDIME2_MCS_NAMESPACE, "theme");

    public ResponseLinkElement(XDIMEContextInternal context) {
        super(ResponseElements.LINK, context);
        protocolAttributes = new ResponseLinkAttributes();
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        // get the value of the rel and href attributes for the link
        ExpandedName linkType = getAsExpandedName(attributes
                .getValue("", "rel"), context);
        String linkValue = attributes.getValue("", "href");

        if (logger.isDebugEnabled()) {
            logger.debug("Processing; rel=" + linkType + " href=" + linkValue);
        }

        // find the root html element
        Stack elementStack = ((XDIMEContextImpl) context).getStack();
        if (elementStack.size() >= 3) {
            ResponseResponseElement widgetResponseElement = (ResponseResponseElement) elementStack
                    .get(elementStack.size() - 3);

            // Add the href value of this link to the root response element if the
            // relAttribute is a layout or a theme
            if (linkValue != null) {
                if (LAYOUT.equals(linkType)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Link layout: " + linkValue);
                    }
                    widgetResponseElement.setLayoutName(linkValue);
                } else if (THEME.equals(linkType)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Link theme: " + linkValue);
                    }
                    MarinerPageContext pageContext = getPageContext(context);
                    CompiledStyleSheet themeStyleSheet = pageContext
                            .retrieveThemeStyleSheet(linkValue);
                    if (themeStyleSheet != null) {
                        widgetResponseElement
                                .addLinkStyleSheet(themeStyleSheet);
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Unrecognisable link element, discarding.");
                }
            }
        } else {
            throw new IllegalStateException("Element stack does not contain "
                    + "required 'response' element.");
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context) {

        // NO-OP
    }

    // Javadoc inherited
    public XDIMEResult exprElementStart(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        super.exprElementStart(context, attributes);
        // Skip contents
        return XDIMEResult.SKIP_ELEMENT_BODY;
    }

    /**
     * get a ExpandedName representation of the provided name
     * 
     * @param string -
     *            to be converted to a ExpandedName
     * @param context
     * @return
     */
    private ExpandedName getAsExpandedName(final String string,
            XDIMEContextInternal context) {
        ExpandedName expandedName = null;
        if (string != null && string.length() > 0) {
            QName qName = new ImmutableQName(string);
            expandedName = context.getExpressionContext()
                    .getNamespacePrefixTracker().resolveElementQName(qName);
        }
        return expandedName;
    }

}
