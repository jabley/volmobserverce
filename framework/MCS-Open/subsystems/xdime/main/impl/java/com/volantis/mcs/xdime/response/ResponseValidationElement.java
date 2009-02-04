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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.response.attributes.ResponseValidationAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.XDIMEResult;
import com.volantis.mcs.xdime.widgets.ResponseElements;

/**
 * Implementation of AJAX resposne for Progress widget.
 */
public class ResponseValidationElement extends ResponseElement {
    /**
     * Child message element.
     */
    private ResponseMessageElement messageElement;
    
    /**
     * List of child field elements.
     */
    private ArrayList fieldElements = new ArrayList();
    
    /**
     * Initialisation
     * @param context
     */
    public ResponseValidationElement(XDIMEContextInternal context) {
        super(ResponseElements.VALIDATION, context);

        protocolAttributes = new ResponseValidationAttributes();
    }

    // Javadoc inherited
    public XDIMEResult callOpenOnProtocol(XDIMEContextInternal context,
            XDIMEAttributes attributes) throws XDIMEException {

        return XDIMEResult.PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited
    public void callCloseOnProtocol(XDIMEContextInternal context)
            throws XDIMEException {
        try {
            VolantisProtocol protocol = getProtocol(context);

            ResponseValidationAttributes validationAttributes = getValidationAttributes();
            
            // Open script element
            ScriptAttributes scriptAttributes = new ScriptAttributes();
            
            scriptAttributes.setLanguage("JavaScript");
            
            scriptAttributes.setType("text/javascript");
            
            protocol.writeOpenScript(scriptAttributes);
            
            StringWriter scriptWriter = new StringWriter();
            
            // Write script content
            scriptWriter.write("Widget.processValidatorResponse(");

            boolean validationPassed = validationAttributes.getResult().equals("passed");

            scriptWriter.write(validationPassed ? "true" : "false");
            
            if (!validationPassed) {
                // Write failure message (if not specified, write default message.
                ResponseMessageElement messageElement = getMessageElement();
                
                if (messageElement != null) {
                    scriptWriter.write(", '" + messageElement.getMessageAttributes().getId() + "', ");
                } else {
                    scriptWriter.write(", null, ");
                }
                
                // Write all failed fields
                scriptWriter.write("{");

                Iterator iterator = fieldElementsIterator();

                while (iterator.hasNext()) {
                    ResponseFieldElement fieldElement = (ResponseFieldElement) iterator.next();

                    String fieldId = fieldElement.getFieldAttributes().getRef();
                    
                    scriptWriter.write("'" + fieldId + "': ");
                    
                    // Write failure message (if not specified, write default message.
                    ResponseMessageElement fieldMessageElement = fieldElement.getMessageElement();

                    if (fieldMessageElement != null) {
                        scriptWriter.write("'" + fieldMessageElement.getMessageAttributes().getId() + "'");
                    } else {
                        scriptWriter.write("null");
                    }
                    
                    if (iterator.hasNext()) {
                        scriptWriter.write(", ");
                    }
                }

                scriptWriter.write("}");
            }

            scriptWriter.write(")");

            protocol.getContentWriter().write(scriptWriter.toString());

            protocol.writeCloseScript(scriptAttributes);
        } catch (IOException e) {
            throw new XDIMEException(e);
        }
    }

    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(
            XDIMEContextInternal context, XDIMEAttributes attributes)
            throws XDIMEException {
        getValidationAttributes().setResult(attributes.getValue("", "result"));
    }

    /**
     * Returns response validation attributes.
     * 
     * @return the attributes.
     */
    protected ResponseValidationAttributes getValidationAttributes() {
        return (ResponseValidationAttributes) protocolAttributes;
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
    
    /**
     * Adds specified field element.
     * 
     * @param fieldElement The field element to add.
     */
    protected void addFieldElement(ResponseFieldElement fieldElement) {
        fieldElements.add(fieldElement);
    }
    
    /**
     * Returns iterator over all added field elements.
     * 
     * @return iterator over all added field elements.
     */
    protected Iterator fieldElementsIterator() {
        return fieldElements.iterator();
    }
}
