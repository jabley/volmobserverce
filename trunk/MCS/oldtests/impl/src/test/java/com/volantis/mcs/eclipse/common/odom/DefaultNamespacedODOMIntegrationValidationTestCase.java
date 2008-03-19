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
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.devrep.repository.accessors.DefaultNamespaceAdapterFilter;
import com.volantis.xml.schema.W3CSchemata;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import java.io.IOException;

/**
 * Integratin test that ensures ODOMObservable objects perform validation
 * when they are modified.
 */
public class DefaultNamespacedODOMIntegrationValidationTestCase
        extends ODOMIntegrationValidationTestCase {

    // javadoc inherited
    protected Document createDocument() throws JDOMException, IOException {
        return createDocumentFromString(
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                 "<shiporder xmlns=\"" + getNamespace().getURI() + "\"" +
                            " xmlns:xsi=\"" + W3CSchemata.XSI_NAMESPACE + "\" " +
                            "xsi:schemaLocation=\"" + getNamespace().getURI() + " " +
                            getAbsoluteSchemaLocation() + "\" orderid='889923'>" +
                    "<orderperson>John Smith</orderperson>" +
                    "<shipto>" +
                        "<name>Ola Nordmann</name>" +
                        "<address>Langgt 23</address>" +
                        "<city>4000 Stavanger</city>" +
                        "<country>Norway</country>" +
                    "</shipto>" +
                    "<item>" +
                        "<title>Empire Burlesque</title>" +
                        "<note>Special Edition</note>" +
                        "<quantity>1</quantity>" +
                        "<price>10.90</price>" +
                    "</item>" +
                    "<item>" +
                        "<title>Hide your heart</title>" +
                        "<quantity>1</quantity>" +
                        "<price>9.90</price>" +
                    "</item>" +
                "</shiporder>");
    }

    // javadoc inherited
    protected Namespace getNamespace() {
        return MCSNamespace.LPDM;
    }

    // javadoc inherited
    protected SAXBuilder createSAXBuilder() {
        SAXBuilder builder = super.createSAXBuilder();
        // hook in a DefaultNamespaceAdapterFilter so that default namespace
        // declarations are replaced with explicit prefix to namespace bindings
        builder.setXMLFilter(
                new DefaultNamespaceAdapterFilter(getNamespace().getPrefix()));
        // return the builder.
        return builder;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 17-May-04	4413/1	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 ===========================================================================
*/
