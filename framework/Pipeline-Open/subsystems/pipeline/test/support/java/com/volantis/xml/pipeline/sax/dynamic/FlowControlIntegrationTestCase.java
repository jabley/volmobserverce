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
package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import com.volantis.xml.pipeline.sax.ContentWriter;

import java.io.Writer;

/**
 * Integration test for flow control related markup
 */
public class FlowControlIntegrationTestCase extends PipelineTestAbstract {

    /**
     * The name of this class, without the package prefix. Used to (prefix)
     * name the input and expected XML files.
     */
    protected String name;

    /**
     * Initializes a <code>FlowControlIntegrationTestCase</code> instance
     * @param name the name of the test
     */
    public FlowControlIntegrationTestCase(String name) {
        super(name);

        String className = getClass().getName();
        this.name = className.substring(className.lastIndexOf('.') + 1);
    }

    /**
     * Test a transformation with flow control
     * @throws Exception if an error occurs
     */
    public void testTransform() throws Exception {

            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   name + "_Transform.input.xml",
                   name + "_Transform.expected.xml");
    }

    // javadoc inherited
    protected ContentHandler createContentHandler(Writer writer) {
        // return a ContentHandler that will suppress the body content of
        // every img tag
        return new ContentWriter(writer) {
            // javadoc inherited from superclass
            public void startElement(String namespaceURI,
                                     String lName,
                                     String qName,
                                     Attributes attrs) throws SAXException {
                if (lName == "img") {
                    pipeline.getPipelineContext().getFlowControlManager().exitCurrentElement();
                }
                super.startElement(namespaceURI, lName, qName, attrs);
            }
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8751/1	schaloner	VBM:2005060711 [Refactor - method signature] public DefaultHandler createDefaultHandler changed to public ContentHandler createContentHandler in PipelineTestAbstract

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Oct-03	440/1	doug	VBM:2003102911 Added Flow control process to tail of all pipelines

 ===========================================================================
*/
