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
import com.volantis.xml.pipeline.sax.IntegrationTestHelper;
import com.volantis.xml.pipeline.sax.ContentWriter;
import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.pipeline.sax.XMLStreamingException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;

import java.io.Writer;

/**
 * Test case for the URL to URLC converter.
 */
public class URLToURLCConversionTestCase extends PipelineTestAbstract {
    public URLToURLCConversionTestCase(String name) {
        super(name);
    }

    public void testURLToURLCConversion() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
               "URLToURLCConversionTestCase.input.xml",
               "URLToURLCConversionTestCase.expected.xml");
    }

    public void testBadServer() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   "URLToURLCConversionTestCase.bad_server.xml",
                   "URLToURLCConversionTestCase.bad_server.xml");
            fail("Should have had an exception");
        } catch (XMLProcessingException e) {
            // Expected behaviour
        }
    }

    public void testConversionProblem() throws Exception {
        try {
            doTest(new IntegrationTestHelper().getPipelineFactory(),
                   "URLToURLCConversionTestCase.bad_convert.xml",
                   "URLToURLCConversionTestCase.bad_convert.xml");
            fail("Should have had an exception");
        } catch (XMLStreamingException e) {
            // Expected behaviour
            assertTrue("Cause exception not a URLConversionException (was " +
                       e.getCause().getClass().getName() + ")",
                       e.getCause() instanceof URLConversionException);
        }
    }

    /**
     * A factory method that creates a handler that encodes '&' characters in
     * attribute values.
     */
    protected ContentHandler createContentHandler(Writer writer) {
        return new ContentWriter(writer) {
            // javadoc inherited
            protected void writeAttributes(Attributes attrs) throws SAXException {
                if (attrs != null) {
                    for (int i = 0; i < attrs.getLength(); i++) {
                        String aName = attrs.getLocalName(i);
                        if ("".equals(aName)) {
                            aName = attrs.getQName(i);
                        }

                        write(" " + aName + "=\"" +
                              encodedValue(attrs.getValue(i)) + "\"");
                    }
                }
            }

            /**
             * Utility method that encodes '&' characters in the given string.
             *
             * @param value the string to be encoded
             * @return an encoded version of the given string
             */
            protected String encodedValue(String value) {
                StringBuffer buffer = new StringBuffer(value);

                for (int i = buffer.length() - 1;
                     i >= 0;
                     i--) {
                    if (buffer.charAt(i) == '&') {
                        buffer.replace(i, i + 1, "&amp;");
                    }
                }

                return buffer.toString();
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

 07-Aug-03	299/3	philws	VBM:2003080504 Remove the relativeWidth and maxFileSize attributes from the URL to URLC converter following architectural change

 06-Aug-03	299/1	philws	VBM:2003080504 Pipeline work for the DSB convertImageURLToDMS process

 ===========================================================================
*/
