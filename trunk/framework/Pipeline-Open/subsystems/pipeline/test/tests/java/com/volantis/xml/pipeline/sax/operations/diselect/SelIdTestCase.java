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
package com.volantis.xml.pipeline.sax.operations.diselect;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import com.volantis.xml.pipeline.sax.ContentWriter;
import com.volantis.xml.pipeline.sax.TestPipelineFactory;

import java.io.Writer;

/**
 * Integration test for flow control related markup
 */
public class SelIdTestCase extends PipelineTestAbstract {

    /**
     * Ensure that the default selid name is xml:id.
     */
    public void testDefaultSelidName() throws Exception {
        doTest(new TestPipelineFactory(),
                "default-selid-name-input.xml",
                "default-selid-name-expected.xml");
    }

    /**
     * Ensure that a prefixed selid name works correctly.
     */
    public void testPrefixedSelidName() throws Exception {
        doTest(new TestPipelineFactory(),
                "prefixed-selid-name-input.xml",
                "prefixed-selid-name-expected.xml");
    }

    /**
     * Ensure that a prefixed selid name with no valid namespace declaration
     * fails.
     */
    public void testPrefixedErrorSelidName() throws Exception {
        try {
        doTest(new TestPipelineFactory(),
                "prefixed-error-selid-name-input.xml",
                "prefixed-error-selid-name-expected.xml");
            fail("Did not detect missing namespace prefix declaration");
        } catch (IllegalStateException e) {
            assertEquals("Could not find a non empty prefix declared for namespace: http://acme.com/",
                    e.getMessage());
        }
    }

    /**
     * Ensure that an unprefixed selid name works correctly.
     */
    public void testUnprefixedSelidName() throws Exception {
        doTest(new TestPipelineFactory(),
                "unprefixed-selid-name-input.xml",
                "unprefixed-selid-name-expected.xml");
    }

    /**
     * Ensure that built in diselect elements initialise the selidname
     * correctly.
     */
    public void testDISelectElementsSelidName() throws Exception {
        doTest(new TestPipelineFactory(),
                "diselect-elements-selid-name-input.xml",
                "diselect-elements-selid-name-expected.xml");
    }
}