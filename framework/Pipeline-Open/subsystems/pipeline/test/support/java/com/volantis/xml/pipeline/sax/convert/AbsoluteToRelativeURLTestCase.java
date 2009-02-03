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

package com.volantis.xml.pipeline.sax.convert;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.NamespaceContentWriter;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;

import java.io.Writer;

/**
 * Test the AbsoluteToRelatveURL conversion using xml input and expected xml
 * output.
 */
public class AbsoluteToRelativeURLTestCase
    extends PipelineTestAbstract {

    /**
     * Factory for creating pipeline objects.
     */
    protected XMLPipelineFactory pipelineFactory;

    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    public AbsoluteToRelativeURLTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        Category.getRoot().setPriority(Priority.DEBUG);
        pipelineFactory = new IntegrationTestHelper().getPipelineFactory();
    }

    // javadoc inherited from superclass
    protected void tearDown() throws Exception {
        Category.shutdown();
    }

    /**
     * Override this method in order to preserve the namespace markup in the
     * generated output.
     *
     * @param  writer the writer.
     * @return        a new DefaultHandler that includes the namespace values
     *                in the output.
     */
    protected ContentHandler createContentHandler(Writer writer) {

        return new NamespaceContentWriter(writer) {
            // javadoc inherited from superclass
            public void characters(char buf[], int offset, int len) throws SAXException {
                String s = new String(buf, offset, len);
                write(s.trim());
            }

            protected void writeAttributes(Attributes attrs) throws SAXException {
                if (attrs != null) {
                    for (int i = 0; i < attrs.getLength(); i++) {
                        String aName = attrs.getLocalName(i);
                        if ("".equals(aName) || (attrs.getQName(i) != null)) {
                            aName = attrs.getQName(i);
                        }
                        write(" " + aName + "=\"" + attrs.getValue(i) + "\"");
                    }
                }
            }
        };
    }

    /**
     * Test the simple adapter process on specific markup.
     *
     * @throws Exception if an error occurs.
     */
    public void testSimpleAbsoluteToRelativeURLConversion() throws Exception {
        doTest(pipelineFactory,
               "SimpleAbsoluteToRelativeURLTestCase.input.xml",
               "SimpleAbsoluteToRelativeURLTestCase.expected.xml");
    }

    /**
     * Test the simple adapter process on specific markup.
     *
     * @throws Exception if an error occurs.
     */
    public void testComplexAbsoluteToRelativeURLConversion() throws Exception {
        doTest(pipelineFactory,
               "ComplexAbsoluteToRelativeURLTestCase.input.xml",
               "ComplexAbsoluteToRelativeURLTestCase.expected.xml");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8751/1	schaloner	VBM:2005060711 [Refactor - method signature] public DefaultHandler createDefaultHandler changed to public ContentHandler createContentHandler in PipelineTestAbstract

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 ===========================================================================
*/
