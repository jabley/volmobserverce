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

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.widgets.attributes.RefreshAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElementInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

public class RefreshElement extends WidgetElement {

    public RefreshElement(XDIMEContextInternal context) {
        super(WidgetElements.REFRESH, context);
        protocolAttributes = new RefreshAttributes();
    }

    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }    

    public void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {

        XDIMEElementInternal xdimeElement = context.getCurrentElement();
        if (xdimeElement instanceof Refreshable) {
            ((Refreshable) xdimeElement)
                    .setRefreshAttributes(getRefreshAttributes());
        }
    }

    public RefreshAttributes getRefreshAttributes() {
        return (RefreshAttributes) protocolAttributes;
    }

    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        ((RefreshAttributes)protocolAttributes).setSrc(getSrcAttributeValue(context, attributes));
        ((RefreshAttributes)protocolAttributes).setInterval(attributes.getValue("","interval"));
    }
    
    /**
     * Retrieves the value of the 'src' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'src' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected String getSrcAttributeValue(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        String src = attributes.getValue("","src");
        
        if (src != null) {
            src = rewriteURLWithPageURLRewriter(context, src, PageURLType.WIDGET);
        }
        
        return src;
    }
}
