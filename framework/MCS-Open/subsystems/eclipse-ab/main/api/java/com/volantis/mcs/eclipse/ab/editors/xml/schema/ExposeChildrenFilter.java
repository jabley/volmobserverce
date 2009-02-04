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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.xml.schema;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.util.HashSet;
import java.util.Set;

/**
 * This filter is used to delete specifically identified nodes, allowing their
 * children to remain in the SAX event sequence in their place.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class ExposeChildrenFilter extends XMLFilterImpl {
    /**
     * Defines the set of nodes to be deleted.
     */
    private Set nodeNames = new HashSet();

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param nodeNames the set of names identifying the nodes to be deleted
     */
    public ExposeChildrenFilter(String[] nodeNames) {
        for (int i = 0; i < nodeNames.length; i++) {
            this.nodeNames.add(nodeNames[i]);
        }
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param nodeNames the set of names identifying the nodes to be deleted
     * @param xmlReader the parent XMLReader for this filter
     */
    public ExposeChildrenFilter(String[] nodeNames, XMLReader xmlReader) {
        super(xmlReader);
        for (int i = 0; i < nodeNames.length; i++) {
            this.nodeNames.add(nodeNames[i]);
        }
    }

    // javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {
        if (!(nodeNames.contains(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI))) {
            super.startElement(namespaceURI, localName, qName, attributes);
        }
    }

    // javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        if (!(nodeNames.contains(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI))) {
            super.endElement(namespaceURI, localName, qName);
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

 04-Jan-04	2309/1	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
