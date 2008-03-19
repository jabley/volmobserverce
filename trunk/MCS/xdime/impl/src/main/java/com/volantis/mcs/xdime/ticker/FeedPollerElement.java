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

package com.volantis.mcs.xdime.ticker;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.ticker.attributes.FeedPollerAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * FeedPoller element.
 */
public class FeedPollerElement extends TickerElement {
       
    /**
     * Used to retrieve localized exception messages.
     */
    protected static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    FeedPollerElement.class);

    /**
     * Creates and returns new instance of FeedPollerElement, 
     * initalised with empty attributes.
     * @param context
     */
    public FeedPollerElement(XDIMEContextInternal context) {
        super(TickerElements.FEED_POLLER, context);
        
        protocolAttributes = new FeedPollerAttributes();        
    }  

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {

        // Check, if this FeedPoller element is singleton.
        checkSingletonElement(context);
        
        return super.callOpenOnProtocol(context, attributes);
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        FeedPollerAttributes feedPollerAttributes = (FeedPollerAttributes) protocolAttributes;
        
        feedPollerAttributes.setUrl(getUrlAttributeValue(context, attributes));
        feedPollerAttributes.setMinPollingInterval(attributes.getValue("", "min-polling-interval"));
        feedPollerAttributes.setMaxPollingInterval(attributes.getValue("", "max-polling-interval"));
    }
    
    private void checkSingletonElement(XDIMEContextInternal context) throws XDIMEException {
        // Check, if this FeedPoller element is singleton.
        boolean added = context.addSingletonElement(this);
        
        if (!added) {
            throw new XDIMEException(EXCEPTION_LOCALIZER.format("ticker-singleton-element-error",
                    getElementType()));
        }
    }

    /**
     * Retrieves the value of the 'url' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'url' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected String getUrlAttributeValue(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        String url = attributes.getValue("","url");
        
        if (url != null) {
            url = rewriteURLWithPageURLRewriter(context, url, PageURLType.WIDGET);
        }
        
        return url;
    }
}
