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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2003030405 - Created. ContentHandler that 
 *                              writes XML content out to a Writer.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.Writer;

import junitx.framework.Assert;
import com.volantis.xml.sax.ExtendedSAXException;

/**
 * ContentHandler that writes XML content out to a Writer. 
 */ 
public class ContentWriter extends DefaultHandler {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";
         
    /**
     * The Writer that will be used to write out the content
     */ 
    private Writer writer;

    /**
     * Flag that indicates whether a startDocument() event has been received. 
     */ 
    private boolean startDocumentEventReceived;
    
    /**
     * Flag that indicates whether a endDocument() event has been received. 
     */
    private boolean endDocumentEventReceived;
    
    /**
     * Create a new ContentWriter instance.
     * @param writer a Writer object 
     */ 
    public ContentWriter(Writer writer) {
        this.writer = writer;
        this.startDocumentEventReceived = false;
        this.endDocumentEventReceived = false;       
    }

    // javadoc inherited 
    public void startDocument() throws SAXException {
        if(startDocumentEventReceived) {
            Assert.fail("More than one startDocument() event received");            
        }
        startDocumentEventReceived = true;
    }

    // javadoc inherited
    public void endDocument() throws SAXException {
        if(endDocumentEventReceived) {
            Assert.fail("More than one endDocument() event received");            
        }
        endDocumentEventReceived = true;
    }

    // javadoc inherited from superclass
    public void startElement(String namespaceURI,
                             String lName,
                             String qName,
                             Attributes attrs) throws SAXException {
        write("<");
        writeElementName(lName, qName);
        writeNamespaceDeclaration();
        writeAttributes(attrs);
        write(">");
    }

    protected void writeAttributes(Attributes attrs) throws SAXException {
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i);
                if ("".equals(aName)) {
                    aName = attrs.getQName(i);
                }
                write(" " + aName + "=\"" + attrs.getValue(i) + "\"");
            }
        }
    }

    // javadoc inherited from superclass
    public void endElement(String namespaceURI,
                           String lName,
                           String qName) throws SAXException {
        write("</");
        writeElementName(lName, qName);
        write(">");
    }

    // javadoc inherited from superclass
    public void characters(char buf[], int offset, int len) throws SAXException {
        String s = new String(buf, offset, len);
        write(s);
    }

    /**
     * Write a String out to the Writer
     * @param s the String to write
     * @throws SAXException if error occurs
     */ 
    protected void write(String s) throws SAXException {

        try {
            writer.write(s);
        } catch (IOException e) {
            throw new ExtendedSAXException(e);
        }

    }
    
    /**
     * Write out the element name in a startElement/endElement event
     * By default any prefix is not written out
     * Subclasses can override this if required 
     * @param localName the localName of the element
     * @param qName the prefixed name of the element
     * @throws SAXException if an error occurs
     */ 
    protected void writeElementName(String localName, String qName) 
            throws SAXException {
        
        String element = localName;
        if("".equals(element)) {
            element = qName;
        }        
        write(element);        
    }
    
    /**
     * Subclasses should override this method if the wish to declare
     * namespaces when a startElement() event is being generated
     * @throws SAXException if error occurs 
     */ 
    protected void writeNamespaceDeclaration() throws SAXException {
        // do nothing        
    }    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	238/1	byron	VBM:2003072309 Create the adapter process for parent task v4

 27-Jun-03	127/3	doug	VBM:2003062306 Column Conditioner Modifications

 27-Jun-03	127/1	doug	VBM:2003062306 Column Conditioner Modifications

 23-Jun-03	95/3	doug	VBM:2003061605 Document Event Filtering changes

 ===========================================================================
*/
