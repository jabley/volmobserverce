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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.DOMFactoryMock;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.styling.StylesBuilder;

import java.io.IOException;
import java.util.Arrays;

import org.xml.sax.SAXException;

/**
 * Test the DefaultContentInserter
 */
public class DefaultContentInserterTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * test that the processing of the 'contents' keyword works as assumed.
     */
    public void testContentsKeywordSimple() throws IOException, SAXException {

        final String input = "<link>content</link>";
        final String contentStyle = "'label ' 'label ' contents";
        final String expected = "<link>label label content</link>";
        checkInsert(input, contentStyle, expected);
    }

    public void testContentsKeywordComplexContent()
            throws IOException, SAXException {

        final String input = "<link>" +
                                 "<node1>" +
                                     "<child1/>child text<child2/>" +
                                 "</node1>" +
                                 "text" +
                                 "<node2/>" +
                             "</link>";
        final String contentStyle = "'label ' 'label ' contents";
        final String expected = "<link>label label <node1>" +
            "<child1/>child text<child2/></node1>text<node2/></link>";
        checkInsert(input, contentStyle, expected);
    }

    public void testContentsKeywordMultipleContentsKeywords()
            throws IOException, SAXException {

        final String input = "<link>" +
                                 "<node1>" +
                                     "<child1/>child text<child2/>" +
                                 "</node1>" +
                                 "text" +
                                 "<node2/>" +
                             "</link>";
        final String contentStyle =
            "'label ' 'label ' contents 'label ' contents";
        final String expected = "<link>label label <node1>" +
            "<child1/>child text<child2/></node1>text<node2/>label </link>";
        checkInsert(input, contentStyle, expected);
    }

    public void testContentsKeywordNoContent()
            throws IOException, SAXException {

        final String input = "<link/>";
        final String contentStyle = "'label ' 'label ' contents";
        final String expected = "<link>label label </link>";
        checkInsert(input, contentStyle, expected);
    }

    public void testContentsKeywordNoContentsKeyword()
            throws IOException, SAXException {

        final String input = "<link>" +
                                 "<node1>" +
                                     "<child1/>child text<child2/>" +
                                 "</node1>" +
                                 "text" +
                                 "<node2/>" +
                             "</link>";
        final String contentStyle = "'label ' 'label '";
        final String expected = "<link>label label </link>";
        checkInsert(input, contentStyle, expected);
    }

    /**
     * Ensure that the content inserter will handle a {@link StyleInteger}
     * value properly.
     *
     * <p>This is needed because sometimes the counter() function can return
     * a {@link StyleInteger}.</p>
     */ 
    public void testStyleInteger()
            throws IOException, SAXException {

        StyleInteger integer = STYLE_VALUE_FACTORY.getInteger(null, 6);
        StyleList list = STYLE_VALUE_FACTORY.getList(Arrays.asList(
            new StyleValue[]{integer}));

        final String input = "<root/>";
        final String expected = "<root>6</root>";

        checkInsert(input, list, expected);
    }

    private void checkInsert(
                final String input, final String style, final String expected)
            throws SAXException, IOException {

        final StyleValue styleValue =
            StylesBuilder.getStyleValue(StylePropertyDetails.CONTENT, style);

        checkInsert(input, styleValue, expected);

    }

    private void checkInsert(
            final String input, final StyleValue styleValue,
            final String expected) throws SAXException, IOException {
        //create the element
        final Element element = DOMUtilities.read(input).getRootElement();

        //create the mocks
        final ProtocolSupportFactoryMock psfMock =
            new ProtocolSupportFactoryMock("psfMock", expectations);
        final ProtocolConfigurationMock protocolConfigurationMock =
            new ProtocolConfigurationMock("protocolConfig", expectations);
        final DOMFactoryMock domFactoryMock =
                new DOMFactoryMock("domFactoryMock", expectations);
        psfMock.expects.getDOMFactory().returns(domFactoryMock);
        final DOMProtocolMock domProtocolMock = new DOMProtocolMock("protocol",
            expectations, psfMock, protocolConfigurationMock);

        final Inserter defaultContentInserter =
            new DefaultContentInserter(domProtocolMock, null, null);

        defaultContentInserter.insert(element, styleValue);

        assertEquals("Contents key word did not produce expected output",
                expected, DOMUtilities.toString(element));
    }
}
