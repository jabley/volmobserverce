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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbsax.AttributeValueCode;
import com.volantis.mcs.wbsax.AttributeValueVisitor;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.ReferenceResolver;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;

import java.util.Stack;

public class WBSAXAttributeValueSerialiser implements AttributeValueVisitor {

    private Stack handlerStack;
    
    private WBSAXContentHandler handler;
    
    private ReferenceResolver resolver;

    public WBSAXAttributeValueSerialiser(WBSAXContentHandler handler, 
            ReferenceResolver resolver) {
        this.handler = handler;
        this.resolver = resolver;
    }

    /**
     * Push a output handler onto the output handler stack, making it the
     * current handler.
     * <p> 
     * NOTE: This allows the client to set the handler we are serialising to 
     * as we are processing. This is generally a dodgy thing to do and should 
     * only be done if you know exactly what you are doing. Currently it is 
     * used to allow us to collect attribute values for sending off as URL 
     * Attribute events.
     * 
     * @param handler the content handler to use from this point forward
     */ 
    public void pushHandler(WBSAXContentHandler handler) {
        if (handlerStack == null) {
            handlerStack = new Stack();
        }
        handlerStack.push(this.handler);
        this.handler = handler;
    }

    /**
     * Pop the current output handler off the output handler stack, making the
     * previous handler the current handler.
     * 
     * @return the handler we were using up to this point. 
     */ 
    public WBSAXContentHandler popHandler() {
        WBSAXContentHandler handler = this.handler; 
        this.handler = (WBSAXContentHandler) handlerStack.pop();
        return handler;
    }

    public void visitString(WBSAXString string) throws WBSAXException {
        handler.addAttributeValue(string);
    }

    public void visitReference(StringReference reference) 
            throws WBSAXException {
        handler.addAttributeValue(resolver.resolve(reference));
    }

    public void visitValue(AttributeValueCode value) 
            throws WBSAXException {
        handler.addAttributeValue(value);
    }

    public void visitEntity(EntityCode entity) throws WBSAXException {
        handler.addAttributeValueEntity(entity);
    }

    public void visitExtension(Extension extension) 
            throws WBSAXException {
        handler.addAttributeValueExtension(extension);
    }

    public void visitExtensionString(Extension extension, 
            WBSAXString string) throws WBSAXException {
        handler.addAttributeValueExtension(extension, string);
    }

    public void visitExtensionReference(Extension extension, 
            StringReference reference) throws WBSAXException {
        handler.addAttributeValueExtension(extension, 
                resolver.resolve(reference));
    }

    public void visitOpaque(OpaqueValue opaque) throws WBSAXException {
        handler.addAttributeValueOpaque(opaque);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 12-Jun-03	385/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
