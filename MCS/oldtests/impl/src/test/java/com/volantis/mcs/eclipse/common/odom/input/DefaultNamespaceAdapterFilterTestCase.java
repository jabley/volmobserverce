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
package com.volantis.mcs.eclipse.common.odom.input;

import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayInputStream;
import java.io.Writer;
import java.io.StringWriter;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.devrep.repository.accessors.DefaultNamespaceAdapterFilter;

/**
 * Integration test for the {@link com.volantis.devrep.prof.accessors.DefaultNamespaceAdapterFilter}
 */
public class DefaultNamespaceAdapterFilterTestCase extends TestCaseAbstract {

    /**
     * Some XML that declares a default namespace.
     */
    private static final String input =
        "<root xmlns=\"http://foo.com\" a=\"a\">" +
            "<a b=\"b\">" +
               "<b xmlns=\"http://bar.com\">" +
                    "<c c=\"c\"/>" +
                "</b>" +
            "</a>" +
        "</root>";

    /**
     * The same xml but with a prefix bound to the default namespaces.
     */
    private static final String expected =
        "<x:root xmlns:x=\"http://foo.com\" a=\"a\">" +
            "<x:a b=\"b\">" +
                "<x1:b xmlns:x1=\"http://bar.com\">" +
                    "<x1:c c=\"c\"/>" +
                "</x1:b>" +
            "</x:a>" +
        "</x:root>";

    /**
     * Tests the DefaultNamespaceAdapterFilter class
     * @throws Exception if an error occurs
     */
    public void testFilter() throws Exception {
        SAXBuilder builder = new SAXBuilder(false);
        builder.setXMLFilter(new DefaultNamespaceAdapterFilter("x"));

        // build a dom representation of the input xml string
        Document adaptedInputDoc = builder.build(
                new ByteArrayInputStream(input.getBytes()));

        builder = new SAXBuilder(false);
        // build a dom representation of the expected xml string
        Document expectedDoc = builder.build(
                new ByteArrayInputStream(expected.getBytes()));

        XMLOutputter outputer = new XMLOutputter();

        Writer actual = new StringWriter();
        // output the input dom to a writer
        outputer.output(adaptedInputDoc, actual);

        Writer expected = new StringWriter();
        // output the expected dom to a writer
        outputer.output(expectedDoc, expected);

        assertEquals("Default namespaces were not handled correctly",
                     expected.toString(),
                     actual.toString());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4413/1	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 ===========================================================================
*/
