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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.href;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolFactory;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;

/**
 * Test that the HrefTransformer correctly handles XHTML2 href
 * attributes by moving them to the correct location.
 *
 */
public class TestHrefTransformer extends TestCaseAbstract {
    private DOMFactory domFactory;


    protected void setUp() throws Exception {
        super.setUp();

        domFactory = DOMFactory.getDefaultInstance();
    }

    public void testHrefMakeChild() throws IOException {

        ProtocolFactory factory = new ProtocolRegistry.HTMLVersion4_0Factory();
        ProtocolBuilder builder = new ProtocolBuilder();

        DOMProtocol protocol = (DOMProtocol)builder.build(factory, null);

        Document document = domFactory.createDocument();

        Element element1 = domFactory.createElement();
        element1.setName("h1");
        element1.setAttribute("href", "http://www.volantis.com/target.html");
        element1.setStyles(StylesBuilder.getInitialValueStyles());

        document.addNode(element1);

        HrefTransformer transfromer = new HrefTransformer();

        document = transfromer.transform(protocol, document);

        String actual = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());

        String expected = "<h1><a href=\"http://www.volantis.com/target.html\"/></h1>";

        assertEquals("The href has not been moved down as expected.", expected, actual);

    }

    public void testHrefMakeParent() throws IOException {

        ProtocolFactory factory = new ProtocolRegistry.HTMLVersion4_0Factory();
        ProtocolBuilder builder = new ProtocolBuilder();

        DOMProtocol protocol = (DOMProtocol)builder.build(factory, null);

        Document document = domFactory.createDocument();

        Element element1 = domFactory.createElement();
        element1.setName("span");
        element1.setStyles(StylesBuilder.getInitialValueStyles());

        document.addNode(element1);

        Element element2 = domFactory.createElement();
        element2.setName("code");
        element2.setStyles(StylesBuilder.getInitialValueStyles());
        element2.setAttribute("href", "http://www.volantis.com/target.html");

        element1.addHead(element2);

        HrefTransformer transfromer = new HrefTransformer();

        document = transfromer.transform(protocol, document);

        String actual = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());

        String expected = "<span><a href=\"http://www.volantis.com/target.html\"><code/></a></span>";

        assertEquals("The href has not been moved up as expected.", expected, actual);

    }

    public void testHrefPushDown() throws IOException {

        ProtocolFactory factory = new ProtocolRegistry.HTMLVersion4_0Factory();
        ProtocolBuilder builder = new ProtocolBuilder();

        DOMProtocol protocol = (DOMProtocol)builder.build(factory, null);

        Document document = domFactory.createDocument();

        Element element1 = domFactory.createElement();
        element1.setName("tr");
        element1.setStyles(StylesBuilder.getInitialValueStyles());
        element1.setAttribute("href", "http://www.volantis.com/target.html");

        document.addNode(element1);

        Element element2 = domFactory.createElement();
        element2.setName("ul");
        element2.setStyles(StylesBuilder.getInitialValueStyles());

        element1.addHead(element2);

        HrefTransformer transfromer = new HrefTransformer();

        document = transfromer.transform(protocol, document);

        String actual = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());

        String expected = "<tr><ul href=\"http://www.volantis.com/target.html\"/></tr>";

        assertEquals("The href has not been moved down as expected.", expected, actual);

    }

    public void testHrefIgnore() throws IOException {

        ProtocolFactory factory = new ProtocolRegistry.HTMLVersion4_0Factory();
        ProtocolBuilder builder = new ProtocolBuilder();

        DOMProtocol protocol = (DOMProtocol)builder.build(factory, null);

        Document document = domFactory.createDocument();

        Element element1 = domFactory.createElement();
        element1.setName("span");
        element1.setStyles(StylesBuilder.getInitialValueStyles());

        document.addNode(element1);

        Element element2 = domFactory.createElement();
        element2.setName("a");
        element2.setStyles(StylesBuilder.getInitialValueStyles());
        element2.setAttribute("href", "http://www.volantis.com/target.html");

        element1.addHead(element2);

        Element element3 = domFactory.createElement();
        element3.setName("code");
        element3.setStyles(StylesBuilder.getInitialValueStyles());

        element2.addHead(element3);

        HrefTransformer transfromer = new HrefTransformer();

        document = transfromer.transform(protocol, document);

        String actual = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());

        String expected = "<span><a href=\"http://www.volantis.com/target.html\"><code/></a></span>";

        assertEquals("The href has been moved, this is not expected.", expected, actual);


    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
