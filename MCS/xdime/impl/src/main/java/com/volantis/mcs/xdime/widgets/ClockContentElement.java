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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.widgets.attributes.BaseClockAttributes;
import com.volantis.mcs.protocols.widgets.attributes.ClockContentAttributes;
import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;

import java.util.List;

/**
 * ClockContent XDIME element.
 */
public class ClockContentElement extends WidgetElement {

    /**
     * Creates and initialises new instance of ClockContent element.
     * @param context
     */
    public ClockContentElement(XDIMEContextInternal context) {
        // Initialise superclass.
        super(WidgetElements.CLOCK_CONTENT, context);

        // Create an instance of ClockContent attributes.
        // It'll be initialised later in initialiseAttributes() method.
        protocolAttributes = new ClockContentAttributes();
    }
    
    // Javadoc inherited.
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
    	String type = getTypeAttributeValue(context, attributes);
    	if (type == null) {
    		throw new XDIMEException("\"type\" attribute of clock-content "
    				+ "element cannot be null");
    	}
    	
    	String value = getValueAttributeValue(context, attributes);
    	if (value == null) {
    		throw new XDIMEException("\"value\" attribute of clock-content "
    				+ "element cannot be null");
    	}
    	
        ((ClockContentAttributes) protocolAttributes).setType(type);
        ((ClockContentAttributes) protocolAttributes).setValue(value);
    }
    
    /**
     * Retrieves the value of the 'type' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'type' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected String getTypeAttributeValue(XDIMEContextInternal context, 
    		XDIMEAttributes attributes) throws XDIMEException {
        String type = attributes.getValue("","type");
        
        if (type != null) {
        	type = rewriteURLWithPageURLRewriter(context, type, PageURLType.WIDGET);
        }
        
        return type;
    }
    
    /**
     * Retrieves the value of the 'value' attribute and processes it to be
     * passed to protocol attributes.
     * 
     * @param context The XDIME context
     * @param attributes The XDIME attributes to read the 'value' attribute value
     * @return The attribute value ready to be passed to protocol attributes.
     * @throws XDIMEException
     */
    protected String getValueAttributeValue(XDIMEContextInternal context, 
    		XDIMEAttributes attributes) throws XDIMEException {
        String value = attributes.getValue("","value");
        
        if (value != null) {
        	value = rewriteURLWithPageURLRewriter(context, value, PageURLType.WIDGET);
        }
        return value;
    }      
    
    
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {
        
        //clock content should be in one of elements
        // which extends BaseClockAttributes that is:
        //digital-clock or timer or stopwatch
        WidgetAttributes parentWidgetAttributes = 
            (WidgetAttributes)((WidgetElement)parent).getProtocolAttributes();
        
        if (parentWidgetAttributes instanceof BaseClockAttributes) {
            List list = ((BaseClockAttributes)parentWidgetAttributes)
                .getContentAttributes();

            //add attributes to list of content attributes 
            list.add(getProtocolAttributes());
        } 
        //else not possible if correct validation
                
        return super.callOpenOnProtocol(context,attributes);      
    }

    protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes) {
        return XDIMEResult.SKIP_ELEMENT_BODY;
    }
}
