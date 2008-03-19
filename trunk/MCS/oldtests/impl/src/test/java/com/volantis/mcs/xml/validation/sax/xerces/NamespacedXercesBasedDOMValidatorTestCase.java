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
package com.volantis.mcs.xml.validation.sax.xerces;

import com.volantis.xml.schema.W3CSchemata;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;

import java.io.IOException;

public class NamespacedXercesBasedDOMValidatorTestCase
        extends XercesBasedDOMValidatorTestCase{

    /**
     * The xml that will be used to test the validator
     */
    private static final String namespacedTestXML =
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
            "<t:shiporder xmlns:t=\"http://www.volantis.com/testcase\" " +
                         "xmlns:xsi=\"" + W3CSchemata.XSI_NAMESPACE + "\" " +
                         "xsi:schemaLocation=\"http://www.volantis.com/testcase http://fred.com\" " +
                         "orderid='889923'>" +
                "<t:orderperson>John Smith</t:orderperson>" +
                "<t:shipto>" +
                    "<t:name>Ola Nordmann</t:name>" +
                    "<t:address>Langgt 23</t:address>" +
                    "<t:city>4000 Stavanger</t:city>" +
                    "<t:country>Norway</t:country>" +
                "</t:shipto>" +
                "<t:item>" +
                    "<t:title>Empire Burlesque</t:title>" +
                    "<t:note>Special Edition</t:note>" +
                    "<t:quantity>1</t:quantity>" +
                    "<t:price>10.90</t:price>" +
                "</t:item>" +
                "<t:item>" +
                    "<t:title>Hide your heart</t:title>" +
                    "<t:quantity>1</t:quantity>" +
                    "<t:price>9.90</t:price>" +
                "</t:item>" +
            "</t:shiporder>";

    // javadoc inherited
    protected Document createDocument()
            throws JDOMException, IOException {
        return super.createDocumentFromString(namespacedTestXML);
    }

    // javadoc inherited
    protected Namespace getNamespace() {        
        return Namespace.getNamespace("t", "http://www.volantis.com/testcase");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Dec-03	2219/1	doug	VBM:2003121502 Added dom validation to the eclipse editors

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 09-Dec-03	2084/1	doug	VBM:2003120201 xerces based DOMValidator implementation

 ===========================================================================
*/
