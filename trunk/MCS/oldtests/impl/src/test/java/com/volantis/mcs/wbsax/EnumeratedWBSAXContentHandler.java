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
 * 26-May-03    Mat             VBM:2003042907 - Created to provide a way to 
 *                              test that the correct events are being called.
 * 29-May-03    Geoff           VBM:2003042905 - Updated to handle the new 
 *                              WBSAX test structure.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import java.util.ArrayList;

import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.AttributeValueCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;

/**
 * WBSAXContentHandler which records the events that are called and
 * the parameters to those events.  
 * @author mat
 * 
 * @deprecated use {@link com.volantis.mcs.wbsax.io.TestDebugProducer} instead,
 *      it is much easier to use. 
 */
public class EnumeratedWBSAXContentHandler extends WBSAXTerminalHandler {
        
        private ArrayList events = new ArrayList();
        private ArrayList parameters = new ArrayList();
        
        public static final int ADD_ATTRIBUTE_START_CODE = 0;
        public static final int ADD_ATTRIBUTE_STRING_REF = 1;
        public static final int ADD_ATTRIBUTE_VALUE_VALUE_CODE = 2;
        public static final int ADD_ATTRIBUTE_VALUE_STRING_REF = 3;
        public static final int ADD_ATTRIBUTE_VALUE_SAX_STRING = 4;
        public static final int ADD_ATTRIBUTE_VALUE_ENTITY_ENTITY_CODE = 5;
        public static final int ADD_ATTRIBUTE_VALUE_EXTENSION_STRING_REF = 6;
        public static final int ADD_ATTRIBUTE_VALUE_EXTENSION_SAX_STRING = 7;
        public static final int ADD_ATTRIBUTE_VALUE_EXTENSION_CODE = 8;
        public static final int ADD_CONTENT_VALUE_STRING_REF = 9;
        public static final int ADD_CONTENT_VALUE_SAX_STRING = 10;
        public static final int ADD_CONTENT_VALUE_ENTITY_ENTITY_CODE = 11;
        public static final int ADD_CONTENT_VALUE_EXTENSION_STRING_REF = 12;
        public static final int ADD_CONTENT_VALUE_EXTENSION_SAX_STRING = 13;
        public static final int ADD_CONTENT_VALUE_EXTENSION_CODE = 14;
        public static final int END_ATTRIBUTES = 15;
        public static final int END_CONTENT = 16;
        public static final int END_DOCUMENT = 17;
        public static final int END_ELEMENT = 18;
        public static final int START_ATTRIBUTES = 19;
        public static final int START_CONTENT = 20;
        public static final int START_DOCUMENT_PUBLIC_ID_CODE = 21;
        public static final int START_DOCUMENT_PUBLIC_ID_STRING_REF = 22;
        public static final int START_ELEMENT_ELEMENT_CODE = 23;
        public static final int START_ELEMENT_STRING_REF = 24;
        
    /**
     * Reset the lists
     *
     */
    public void reset() {
        events.clear();
        parameters.clear();
    }
    
    /**
     * Get the events list
     * @return The events list
     */
    public ArrayList getEvents() {
        return events;
    }
    
    /**
     * Get the parameters list
     * @return The parameters list
     */
    public ArrayList getParameters() {
        return parameters;
    }
    
    public void addAttribute(AttributeStartCode start)
        throws WBSAXException {
            events.add(new Integer(ADD_ATTRIBUTE_START_CODE));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(start);
            parameters.add(pc);
    }

    public void addAttribute(StringReference name) throws WBSAXException {
        events.add(new Integer(ADD_ATTRIBUTE_STRING_REF));
        ParameterContainer pc = new ParameterContainer();
        pc.addParameter(name);
        parameters.add(pc);
    }

    public void addAttributeValue(AttributeValueCode part)
        throws WBSAXException {
            events.add(new Integer(ADD_ATTRIBUTE_VALUE_VALUE_CODE));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(part);
            parameters.add(pc);
    }

    public void addAttributeValue(StringReference part)
        throws WBSAXException {
            events.add(new Integer(ADD_ATTRIBUTE_VALUE_STRING_REF));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(part);
            parameters.add(pc);
    }

    public void addAttributeValue(WBSAXString part) throws WBSAXException {
        events.add(new Integer(ADD_ATTRIBUTE_VALUE_SAX_STRING));
        ParameterContainer pc = new ParameterContainer();
        pc.addParameter(part);
        parameters.add(pc);

    }

    public void addAttributeValueEntity(EntityCode entity)
        throws WBSAXException {
            events.add(new Integer(ADD_ATTRIBUTE_VALUE_ENTITY_ENTITY_CODE));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(entity);
            parameters.add(pc);   

    }

    public void addAttributeValueOpaque(OpaqueValue part) throws WBSAXException {
        // @todo implement me
    }

    public void addAttributeValueExtension(
        Extension code,
        StringReference value)
        throws WBSAXException {
            events.add(new Integer(ADD_ATTRIBUTE_VALUE_EXTENSION_STRING_REF));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(code);
            pc.addParameter(value);
            parameters.add(pc);

    }

    public void addAttributeValueExtension(
        Extension code,
        WBSAXString value)
        throws WBSAXException {
            events.add(new Integer(ADD_ATTRIBUTE_VALUE_EXTENSION_SAX_STRING));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(code);
            pc.addParameter(value);
            parameters.add(pc);          

    }

    public void addAttributeValueExtension(Extension code)
        throws WBSAXException {
            events.add(new Integer(ADD_ATTRIBUTE_VALUE_EXTENSION_CODE));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(code);
            parameters.add(pc);   
    }

    public void addContentValue(StringReference part)
        throws WBSAXException {
            events.add(new Integer(ADD_CONTENT_VALUE_STRING_REF));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(part);
            parameters.add(pc);    


    }

    public void addContentValue(WBSAXString part) throws WBSAXException {
        events.add(new Integer(ADD_CONTENT_VALUE_SAX_STRING));
        ParameterContainer pc = new ParameterContainer();
        pc.addParameter(part);
        parameters.add(pc);                      
    }

    public void addContentValueEntity(EntityCode entity)
        throws WBSAXException {
            events.add(new Integer(ADD_CONTENT_VALUE_ENTITY_ENTITY_CODE));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(entity);
            parameters.add(pc);                           


    }

    public void addContentValueOpaque(OpaqueValue part) throws WBSAXException {
        // @todo implement me
    }

    public void addContentValueExtension(
        Extension code,
        StringReference value)
        throws WBSAXException {
            events.add(new Integer(ADD_CONTENT_VALUE_EXTENSION_STRING_REF));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(code);
            pc.addParameter(value);
            parameters.add(pc);          

    }

    public void addContentValueExtension(Extension code, WBSAXString value)
        throws WBSAXException {
            events.add(new Integer(ADD_CONTENT_VALUE_EXTENSION_SAX_STRING));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(code);
            pc.addParameter(value);
            parameters.add(pc);                  

    }

    public void addContentValueExtension(Extension code)
        throws WBSAXException {
            events.add(new Integer(ADD_CONTENT_VALUE_EXTENSION_CODE));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(code);
            parameters.add(pc);                        

    }

    public void endAttributes() throws WBSAXException {
        events.add(new Integer(END_ATTRIBUTES));
          parameters.add(null);                       

    }

    public void endContent() throws WBSAXException {
        events.add(new Integer(END_CONTENT));
        parameters.add(null);                        

    }

    public void endDocument() throws WBSAXException {
        events.add(new Integer(END_DOCUMENT));
        parameters.add(null);                         

    }

    public void endElement() throws WBSAXException {
        events.add(new Integer(END_ELEMENT));
        parameters.add(null);                        

    }

    public void startAttributes() throws WBSAXException {
        events.add(new Integer(START_ATTRIBUTES));
        parameters.add(null);                       

    }

    public void startContent() throws WBSAXException {
        events.add(new Integer(START_CONTENT));
        parameters.add(null);                      

    }

    public void startDocument(
        VersionCode version,
        PublicIdCode publicId,
        Codec codec,
        StringTable stringTable, StringFactory strings)
        throws WBSAXException {
            events.add(new Integer(START_DOCUMENT_PUBLIC_ID_CODE));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(version);
            pc.addParameter(publicId);
            pc.addParameter(codec);
            pc.addParameter(stringTable);
            parameters.add(pc);                  
                         

    }

    public void startDocument(
        VersionCode version,
        StringReference publicId,
        Codec codec,
        StringTable stringTable, StringFactory strings)
        throws WBSAXException {
            events.add(new Integer(START_DOCUMENT_PUBLIC_ID_STRING_REF));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(version);
            pc.addParameter(publicId);
            pc.addParameter(codec);
            pc.addParameter(stringTable);
            parameters.add(pc);                                    

    }

    public void startElement(
        ElementNameCode name,
        boolean attributes,
        boolean content)
        throws WBSAXException {
            events.add(new Integer(START_ELEMENT_ELEMENT_CODE));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(name);
            pc.addParameter(new Boolean(attributes));
            pc.addParameter(new Boolean(content));
            parameters.add(pc);                                      

    }

    public void startElement(
        StringReference name,
        boolean attributes,
        boolean content)
        throws WBSAXException {
            events.add(new Integer(START_ELEMENT_STRING_REF));
            ParameterContainer pc = new ParameterContainer();
            pc.addParameter(name);
            pc.addParameter(new Boolean(attributes));
            pc.addParameter(new Boolean(content));
            parameters.add(pc);
    }

    public void startElement(OpaqueElementStart element, boolean content)
            throws WBSAXException {
        // @todo implement me
    }

    /**
     * Container class to hold a list of parameters.
     */
    public class ParameterContainer {
        private ArrayList parameters = new ArrayList();
            
        public void addParameter(Object parameter) {
            parameters.add(parameter);
        }
            
        public ArrayList getParameters() {
            return parameters;
        }
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
