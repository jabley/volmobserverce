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
package com.volantis.xml.utilities.sax;

import org.xml.sax.XMLReader;
import org.xml.sax.XMLFilter;
import org.xml.sax.SAXException;

/**
 * Factory for creating XMLReaders
 */
public class XMLReaderFactory {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Creates an XMLReader
     * @param fragment true if the reader should be able to parse an xml
     * fragment
     * @return XMLReader instance
     */
    public static XMLReader createXMLReader(boolean fragment) {

        XMLReader reader = new com.volantis.xml.xerces.parsers.SAXParser();

        if (fragment) {
            XMLFilter filter = new DocumentFragmentFilter();
            filter.setParent(reader);
            reader = filter;
        }
        return reader;
    }

    /**
     * Creates an XMLReader that ensures all elements are reported with an
     * associated namespace. If an element does not specify a namespace the
     * defaultNamespaceURI and defaultNamespacePrefix will be used in the
     * elements corresponding <code>startElement</code> and
     * <code>endElement</code> events.
     * @param fragment true if the reader should be able to parse an xml
     * fragment
     * @param defaultNamespaceURI the namespace URI to use for those elements
     * that do not specify a namespace
     * @param defaultNamespacePrefix the namespace prefix to use for those
     * elements that do not specify a namespace
     * @return XMLReader instance
     */
    static public XMLReader createXMLReader(boolean fragment,
                                            String defaultNamespaceURI,
                                            String defaultNamespacePrefix) {

        // create an actual reader that will perfom the parsing
        XMLReader reader = createXMLReader(fragment);
        // create Filter that will fix up elements that are not associated
        // with a namespace.
        XMLFilter namespaceFilter =
                new NamespaceFilter(defaultNamespacePrefix,
                                    defaultNamespaceURI);
        namespaceFilter.setParent(reader);
        return namespaceFilter;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 27-Jun-03	127/2	doug	VBM:2003062306 Column Conditioner Modifications

 ===========================================================================
*/
