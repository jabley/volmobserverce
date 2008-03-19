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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom.output;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.xml.XMLDTDBuilder;
import com.volantis.mcs.dom.dtd.DTDBuilder;
import com.volantis.mcs.dom.impl.DocTypeImpl;

import java.io.IOException;
import java.io.StringWriter;

/**
 * XMLDocumentWriterTestCase
 */
public class XMLDocumentWriterTestCase
        extends DocumentWriterTestAbstract {

    private Element root;


    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        root = domFactory.createElement("root");
        root.setAttribute("height", "<18 inches>");

        Element child1 = domFactory.createElement();
        child1.setName("child1");
        root.addHead(child1);

        Element child2 = domFactory.createElement();
        child2.setName("child2");
        child2.setAttribute("depth", "'24 inches'");
        root.addTail(child2);

        Element child3 = domFactory.createElement();
        child3.setName("child3");
        child3.setAttribute("href", "24 inches");
        Text text = domFactory.createText();
        text.append("This is some text for \"Child3\"");
        child3.addHead(text);

        text = domFactory.createText();
        text.append("This <node> is \"Text\" and already encoded.");
        text.setEncoded(true);
        child3.addTail(text);

        child2.addHead(child3);
    }

    protected DTDBuilder createDTDBuilder() {
        return new XMLDTDBuilder();
    }


    public void testOutput() {
        StringWriter writer = new StringWriter();
        DOMDocumentOutputter outputter = new DOMDocumentOutputter(
            new XMLDocumentWriter(writer), encoder);
            
        try {
            outputter.output(root);
        } catch (IOException e) {
            fail( "Unexpected IOException.");
        }
        
        String output = writer.toString().trim();
        String expected = 
            // Attribute should be encoded
            "<root height=\"&lt;18 inches&gt;\">" +
                "<child1/>" +
                // Single quotes are fine
                "<child2 depth=\"'24 inches'\">" +
                    "<child3 href=\"24 inches\">" +
                    // Text should be encoded        
                    "This is some text for &quot;Child3&quot;" +
                    // Text should not be encoded
                    "This <node> is \"Text\" and already encoded." +
                    "</child3>" +
                "</child2>" +
            "</root>";
            
        assertEquals("Output not as expected", expected, output );            
    }

    public void testPublicIdOnly() throws Exception {

        doWriteTest("public", null, null, "<!DOCTYPE root PUBLIC \"public\">");
    }

    private void doWriteTest(
            String publicId, String systemId, String internalDTD,
            String expectedOutput) throws IOException {

        StringWriter writer = new StringWriter();

        DocType docType = new DocTypeImpl(
                "root", publicId, systemId, internalDTD, MarkupFamily.SGML);

        DocumentWriter documentWriter = new XMLDocumentWriter(writer);
        documentWriter.outputDocType(docType);
        assertEquals(expectedOutput, writer.getBuffer().toString());
    }

    public void testSystemIdOnly() throws Exception {

        doWriteTest(null, "system", null, "<!DOCTYPE root SYSTEM \"system\">");
    }

    public void testInternalDTDOnly() throws Exception {

        doWriteTest(null, null, "dtd", "<!DOCTYPE root [\ndtd\n]>");
    }

    public void testPublicAndSystemIds() throws Exception {

        doWriteTest("public", "system", null,
                "<!DOCTYPE root PUBLIC \"public\" \"system\">");
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/3	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Feb-04	2974/1	steve	VBM:2004020608 Added and Expanded Document Writer test cases.

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/
