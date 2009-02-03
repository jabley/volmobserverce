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
 * $Header: /src/voyager/com/volantis/mcs/gui/policyobject/PolicyObjectChooser.java,v 1.1 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-May-03    Adrian          VBM:2003030509 - Created this stub to allow 
 *                              easy implementation of simple ContentHandlers 
 *                              in testcases 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.testtools.stubs.sax;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import org.xml.sax.Attributes;

/**
 * This class allows us to easily create and use anonymous inner ContentHandler
 * implementations overriding only those methods we are interested in.
 */
public class ContentHandlerStub implements ContentHandler {
    // Javadoc inherited from ContentHandler interface
    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {        
    }

    // Javadoc inherited from ContentHandler interface
    public void ignorableWhitespace(char ch[],
                                    int start,
                                    int length) throws SAXException {        
    }

    // Javadoc inherited from ContentHandler interface
    public void endDocument() throws SAXException {        
    }

    // Javadoc inherited from ContentHandler interface
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {        
    }

    // Javadoc inherited from ContentHandler interface
    public void endPrefixMapping(String prefix) throws SAXException {        
    }

    // Javadoc inherited from ContentHandler interface
    public void processingInstruction(String target, String data)
            throws SAXException {        
    }

    // Javadoc inherited from ContentHandler interface
    public void setDocumentLocator(Locator locator) {        
    }

    // Javadoc inherited from ContentHandler interface
    public void skippedEntity(String name) throws SAXException {        
    }

    // Javadoc inherited from ContentHandler interface
    public void startDocument() throws SAXException {        
    }

    // Javadoc inherited from ContentHandler interface
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {        
    }

    // Javadoc inherited from ContentHandler interface
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {        
    }
}

/*
 -------------------------------------------------------------------------
 Change History
 -------------------------------------------------------------------------
 $Log

 04-Jun-03	allan	VBM:2003060201 Add ContextInternals and testtools to testsuite

 -------------------------------------------------------------------------
*/

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
