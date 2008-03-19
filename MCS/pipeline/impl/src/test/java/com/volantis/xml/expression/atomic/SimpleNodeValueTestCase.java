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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Node;

/**
 * Test case for {@link NodeValue}.
 */
public class SimpleNodeValueTestCase extends AtomicValueTestAbstract {

    /**
     * Transformer factory.
     */
    private static final TransformerFactory factory =
            TransformerFactory.newInstance();

    /**
     * Document fragment string to be used to initialize NodeValue.
     */
    private static final String NODE_STRING_VALUE_EXPECTED =
            "\n" +
            "    test-string\n" +
            "    second-test-string\n";

    // javadoc inherited
    protected AtomicValue createValue() {
        Node node = null;
        try {
            Transformer transformer = factory.newTransformer();
            Source source = new StreamSource(getClass().getResourceAsStream(
                    "SimpleNodeValueTestCase-input.xml"));
            node = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .newDocument().createElement("test");
            Result result = new DOMResult(node);
            transformer.transform(source, result);
        } catch (Exception e) {
            fail("Could not create NodeValue instance");
        }
        return expressionFactory.createNodeValue(node);
    }

    // javadoc inherited
    protected String getStringValue() {
        return NODE_STRING_VALUE_EXPECTED;
    }

}
