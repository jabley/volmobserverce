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
package com.volantis.xml.pipeline.sax.drivers.web.conditioners;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An XMLFilter that removes nested <a> tags.
 */
public class HTMLNestedAnchorFilter extends XMLFilterImpl {
    /**
     * Count the nesting level.
     */
    int nestedDepth = 0;

    /**
     * Override startElement to filter nested anchor tags
     */
    // rest of javadoc inherited
    public void startElement(String namespace, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

        boolean isAnchor = localName.equals("a");

        if (!isAnchor || nestedDepth == 0) {
            super.startElement(namespace, localName, qName, attributes);
        }

        if (isAnchor) {
            nestedDepth++;
        }
    }

    /**
     * Override endElement to unwind the anchor nestedDepth and filter out
     * nested anchor end tags.
     */
    // rest of javadoc inherited
    public void endElement(String namespace, String localName, String qName)
            throws SAXException {

        boolean isAnchor = localName.equals("a");

        if (isAnchor) {
            nestedDepth--;
        }

        if (!isAnchor || nestedDepth == 0) {
            super.endElement(namespace, localName, qName);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	217/1	allan	VBM:2003071702 Filter nested anchors.

 ===========================================================================
*/
