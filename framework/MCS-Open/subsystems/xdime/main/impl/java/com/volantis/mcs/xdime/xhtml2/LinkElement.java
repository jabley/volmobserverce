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

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xdime.UnstyledStrategy;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextImpl;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.mcs.xdime.XDIMEElementImpl;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.QName;

import java.util.Stack;
import java.util.Iterator;


public class LinkElement extends XHTML2Element {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(LinkElement.class);

    /**
     * Immutable Expanded name used for comparing values specified for the
     * elements 'rel' attribute for layouts
     */
    private final static ExpandedName LAYOUT =
            new ImmutableExpandedName(
                    XDIMESchemata.XDIME2_MCS_NAMESPACE, "layout");

    /**
     * Immutable Expanded name used for comparing values specified for the
     * elements 'rel' attribute for themes
     */
    private final static ExpandedName THEME =
            new ImmutableExpandedName(
                    XDIMESchemata.XDIME2_MCS_NAMESPACE, "theme");

    public LinkElement(XDIMEContextInternal context) {
        super(XHTML2Elements.LINK, UnstyledStrategy.STRATEGY, context);
    }

    // Javadoc inherited
    protected XDIMEResult callOpenOnProtocol(
        XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        //get the value of the rel and href attributes for the link
        ExpandedName linkType =
            getAsExpandedName(attributes.getValue("", "rel"), context);
        String linkValue = attributes.getValue("", "href");

        if (logger.isDebugEnabled()) {
            logger.debug("Processing; rel=" + linkType +
                " href=" + linkValue);
        }

        //find the root html element
        Stack elementStack = ((XDIMEContextImpl) context).getStack();

        Iterator i = elementStack.iterator();
        HtmlElement htmlElement = null;
        while (i.hasNext()) {
            XDIMEElementImpl e = (XDIMEElementImpl) i.next();
            if(XHTML2Elements.HTML.equals(e.getElementType())) {
                htmlElement = (HtmlElement) e;
            }
        }
        if (htmlElement != null) {
            // Add the href value of this link to the root html element if the
            // relAttribute is a layout or a theme
            if (linkValue != null) {
                if (LAYOUT.equals(linkType)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Link layout: " + linkValue);
                    }
                    htmlElement.setLayoutName(linkValue);
                } else if (THEME.equals(linkType)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Link theme: " + linkValue);
                    }
                    MarinerPageContext pageContext = getPageContext(context);
                    CompiledStyleSheet themeStyleSheet =
                            pageContext.retrieveThemeStyleSheet(linkValue);
                    if (themeStyleSheet != null) {
                        htmlElement.addLinkStyleSheet(themeStyleSheet);
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Unrecognisable link element, discarding.");
                }
            }
        } else {
            throw new IllegalStateException("Element stack does not contain " +
                "required 'html' element.");
        }
        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    protected void callCloseOnProtocol(XDIMEContextInternal context) {

        //NO-OP
    }

    // Javadoc inherited
    public XDIMEResult doElementStart(XDIMEContextInternal context,
                                        XDIMEAttributes attributes)
            throws XDIMEException {

        super.doElementStart(context, attributes);
        // Skip contents
        return XDIMEResult.SKIP_ELEMENT_BODY;
    }

    /**
     * get a ExpandedName representation of the provided name
     * @param string - to be converted to a ExpandedName
     * @param context
     * @return
     */
    private ExpandedName getAsExpandedName(final String string,
                                           XDIMEContextInternal context) {
        ExpandedName expandedName = null;
        if (string != null && string.length() > 0) {
            QName qName = new ImmutableQName(string);
            expandedName = context.getExpressionContext().
                    getNamespacePrefixTracker().resolveElementQName(qName);
        }        
        return expandedName;
    }

}
