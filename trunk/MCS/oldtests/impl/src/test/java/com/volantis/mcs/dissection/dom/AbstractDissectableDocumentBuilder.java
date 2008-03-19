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
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

import com.volantis.xml.sax.AnnotatedAttributes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AbstractDissectableDocumentBuilder
    implements DissectableDocumentBuilder {

    /**
     * Start of an element.
     * <p>
     * This is identical to the normal {@link #startElement} except that it
     * takes an ExtendedAttributes object that allows annotations to be
     * associated with individual attributes.
     */
    public abstract void startElement(String namespaceURI, String localName,
                                      String qName,
                                      AnnotatedAttributes attributes)
        throws SAXException;

    /**
     * Calls the extended {@link #startElement} after casting the attributes to
     * {@link AnnotatedAttributes}.
     */
    public final void startElement(String namespaceURI, String localName,
                                   String qName, Attributes attributes)
        throws SAXException {

        startElement(namespaceURI, localName, qName,
                     (AnnotatedAttributes) attributes);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
