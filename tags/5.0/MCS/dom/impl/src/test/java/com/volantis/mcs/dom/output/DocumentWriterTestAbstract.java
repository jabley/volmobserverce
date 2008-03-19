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

package com.volantis.mcs.dom.output;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.dom.dtd.DTDBuilder;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;

import java.io.Writer;
import java.io.IOException;
import java.io.StringWriter;

public abstract class DocumentWriterTestAbstract
        extends TestCaseAbstract {

    private String [] quoteTable = new String[255];
    protected MyCharacterEncoder encoder = new MyCharacterEncoder();
    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    protected void setUp() throws Exception {
        super.setUp();

        quoteTable['<'] = "&lt;";
        quoteTable['>'] = "&gt;";
        quoteTable['\"'] = "&quot;";
        quoteTable['&'] = "&amp;";
        quoteTable['@'] = "&#64;";
        quoteTable[163] = "&#163;";
        quoteTable[172] = "&not;";
    }

    protected void checkOutput(DTD dtd, Element element, String expectedOutput) throws IOException {
        StringWriter writer = new StringWriter();
        DocumentWriter dw = dtd.createDocumentWriter(writer);
        DocumentOutputter outputter = new DOMDocumentOutputter(
                dw, encoder);
        outputter.output(element);

        assertEquals(expectedOutput,
                writer.getBuffer().toString());
    }

    class MyCharacterEncoder
            extends AbstractCharacterEncoder {

        /**
         * Return the entity reference to use for the character, or null if
         * there isn't one.
         *
         * @param c The character whose entity reference is required.
         * @return The name of the entity reference, or null.
         */
        protected String getEntityRef(int c) {
            // Map from the character to some replacement text.
            if (c < quoteTable.length && quoteTable[c] != null) {
                return quoteTable[c];
            } else {
                return null;
            }
        }

        /**
         * Convert some characters into entity references.
         */
        public void encode(int c, Writer out)
                throws IOException {

            String entityRef = getEntityRef(c);
            if (entityRef != null) {
                out.write(entityRef);
            } else {
                out.write(c);
            }
        }

    }

    protected abstract DTDBuilder createDTDBuilder();

    public void testIgnoreableElement() throws IOException {
        DTDBuilder builder = createDTDBuilder();
        builder.addIgnorableElement("ignoreable");
        DTD dtd = builder.buildDTD();

        Element root = domFactory.createElement("root");
        Element normal = domFactory.createElement("normal");
        root.addTail(normal);
        normal.addTail(domFactory.createText("hello"));
        Element ignoreable = domFactory.createElement("ignoreable");
        root.addTail(ignoreable);
        ignoreable.addTail(domFactory.createText("world"));

        checkOutput(dtd, root, "<root><normal>hello</normal>world</root>");
    }
}
