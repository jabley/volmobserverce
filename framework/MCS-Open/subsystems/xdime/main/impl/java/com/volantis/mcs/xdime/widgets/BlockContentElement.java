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

import com.volantis.mcs.protocols.widgets.attributes.BlockContentAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.response.ResponseBodyElement;

/**
 * Slide XDIME element.
 */
public class BlockContentElement extends WidgetElement {

    /**
     * Creates and initialises new instance of Container element.
     * @param context
     */
    public BlockContentElement(XDIMEContextInternal context) {
        // Initialise superclass.
        super(WidgetElements.BLOCK_CONTENT, context);

        // Create an instance of Container attributes.
        // It'll be initialised later in initialiseAttributes() method.
        protocolAttributes = new BlockContentAttributes();
    }
    
    /**
     * isForResponse - indicates if element exist inside response:body element  
     */
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        
        if(parent instanceof ResponseBodyElement) {
            ((BlockContentAttributes) protocolAttributes).setForResponse(true);            
        } 
    }    
    
    // Javadoc inherited
    protected XDIMEResult doFallbackOpen(XDIMEContextInternal context, XDIMEAttributes attributes) {
        return XDIMEResult.SKIP_ELEMENT_BODY;
    }
}
