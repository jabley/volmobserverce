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
import com.volantis.xml.pipeline.sax.NamespaceContentWriter;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;

import java.io.Writer;

/**
 * Integration test for the ElementCase conversion.
 */
public class ElementCaseConverterTestCase
    extends PipelineTestAbstract {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Default constructor.
     */
    public ElementCaseConverterTestCase(String name) {
        super(name);
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
     * Test the conversion to lower case.
     */
    public void testElementCaseConverterToLower() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
               "ElementCaseConverterToLowerTestCase.input.xml",
               "ElementCaseConverterToLowerTestCase.expected.xml");
    }

    /**
     * Test the conversion to upper case.
     */
    public void testElementCaseConverterToUpper() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
               "ElementCaseConverterToUpperTestCase.input.xml",
               "ElementCaseConverterToUpperTestCase.expected.xml");
    }

    /**
     * Test the conversion to lower case with mixed input.
     */
    public void testElementCaseConverterComplex() throws Exception {
        doTest(new IntegrationTestHelper().getPipelineFactory(),
               "ElementCaseConverterComplexTestCase.input.xml",
               "ElementCaseConverterComplexTestCase.expected.xml");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8751/1	schaloner	VBM:2005060711 [Refactor - method signature] public DefaultHandler createDefaultHandler changed to public ContentHandler createContentHandler in PipelineTestAbstract

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 12-Aug-03	323/1	byron	VBM:2003080802 Provide ConvertElementCase pipeline process

 ===========================================================================
*/
