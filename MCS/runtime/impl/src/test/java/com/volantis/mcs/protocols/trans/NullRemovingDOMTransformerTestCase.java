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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class NullRemovingDOMTransformerTestCase extends TestCaseAbstract {

    /**
     * Verifies that when null named elements are encountered, their children
     * should be inserted in their parents list of children at the point in the
     * list that they were, and then the null named element itself should be
     * removed.
     * <p/>
     * This test verifies this by testing that:
     * <body>
     *     <one/>
     *     <null><twoOne/><null/></null>
     *     <three>
     *          <null>threeOneOne</null>
     *          <null><threeTwoOne/></null>
     *          <threeThree/>
     *     </three>
     * </body>
     * is transformed to:
     * <body>
     *     <one/>
     *     <twoOne/>
     *     <three>threeOneOne<threeTwoOne/><threeThree/></three>
     * </body>
     * @throws IOException if there was a problem
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void testTransform() throws IOException, SAXException,
            ParserConfigurationException {

        String inputXML =
"<body>" +
    "<one/>" +
    "<two><twoOne/><twoTwo/></two>" +
    "<three>" +
        "<threeOne>threeOneOne</threeOne>" +
        "<threeTwo>" +
            "<threeTwoOne/>" +
        "</threeTwo>" +
        "<threeThree/>" +
    "</three>" +
"</body>";

        String expectedXML =
"<body>" +
    "<one/>" +
    "<twoOne/>" +
    "<three>" +
        "threeOneOne" +
        "<threeTwoOne/>" +
        "<threeThree/>" +
    "</three>" +
"</body>";

        Document document = StyledDOMTester.createStyledDom(inputXML);
        Element body = document.getRootElement();
        Element two = (Element)body.getHead().getNext();
        two.setName(null);
        ((Element)two.getTail()).setName(null);
        Element three = (Element)two.getNext();
        final Element threeOne = ((Element)three.getHead());
        threeOne.setName(null);
        ((Element)threeOne.getNext()).setName(null);

        // @todo I would rather pass a DOMProtocolMock than null, but mocks are
        // currently not accessible from integration tests, and StyledDOMTester
        // is not accessible from unit tests.... so move this to unit tests and
        // use a DOMProtocolMock when that becomes possible.
        NullRemovingDOMTransformer transformer = new NullRemovingDOMTransformer();
        transformer.transform(null, document);

        String canonicalExpectedXML = StyledDOMTester.renderStyledDOM(
                StyledDOMTester.createStyledDom(expectedXML));

        String actualXML = StyledDOMTester.renderStyledDOM(document);

        assertXMLEquals("Actual XML does not match expected XML",
                canonicalExpectedXML, actualXML);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 ===========================================================================
*/
