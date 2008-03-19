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

package com.volantis.mcs.xdime.response;

import com.volantis.mcs.protocols.response.attributes.ResponseFieldAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;

/**
 * Implementation of AJAX resposne for Progress widget.  
 */
public class ResponseFieldElement extends ResponseElement {
    private ResponseMessageElement messageElement;
    
    public ResponseFieldElement(XDIMEContextInternal context) {
        super(ResponseElements.FIELD, context);
        
        protocolAttributes = new ResponseFieldAttributes();
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(
        XDIMEContextInternal context, XDIMEAttributes attributes)
        throws XDIMEException {

        // Store this element in parent, so that it
        // can be accessed when parent is ended.
        if (parent instanceof ResponseValidationElement) {
            ResponseValidationElement validationElement = (ResponseValidationElement)parent;
            
            validationElement.addFieldElement(this);
        }

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        getFieldAttributes().setRef(attributes.getValue("", "ref"));
    }
    
    protected ResponseFieldAttributes getFieldAttributes() {
        return (ResponseFieldAttributes)protocolAttributes;
    }

    /**
     * @return Returns the messageElement.
     */
    protected ResponseMessageElement getMessageElement() {
        return messageElement;
    }

    /**
     * @param messageElement The messageElement to set.
     */
    protected void setMessageElement(ResponseMessageElement messageElement) {
        this.messageElement = messageElement;
    }
}
