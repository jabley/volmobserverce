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
import com.volantis.mcs.protocols.widgets.attributes.AutocompleteAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xforms.StyledXFormsElement;
import com.volantis.mcs.xdime.xforms.XFormElements;

/**
 * Autocomplete widget element 
 */
public class AutocompleteElement extends WidgetElement {

    public AutocompleteElement(XDIMEContextInternal context) {
        super(WidgetElements.AUTOCOMPLETE, context);
        protocolAttributes = new AutocompleteAttributes();
    }
    
    // Javadoc inherited.    
    public void callCloseOnProtocol(XDIMEContextInternal context)
        throws XDIMEException {
        
        //parent element
        if (parent.getElementType() == XFormElements.INPUT) {
            String inputId = ((StyledXFormsElement)parent).getProtocolAttributes().getId();
            // set id (if is not set) for parent input XForm element 
            if(inputId == null) {
                inputId = getProtocol(context).getMarinerPageContext().generateUniqueFCID();
                ((StyledXFormsElement)parent).getProtocolAttributes().setId(inputId);
            }
            ((AutocompleteAttributes)protocolAttributes).setInputId(inputId);
        } else {
            throw new XDIMEException("widget:autocomplete element must be inside XForm input element");
        }
        super.callCloseOnProtocol(context);                
     }  
    
    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        ((AutocompleteAttributes) protocolAttributes).setSrc(getSrcAttributeValue(context, attributes));
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
