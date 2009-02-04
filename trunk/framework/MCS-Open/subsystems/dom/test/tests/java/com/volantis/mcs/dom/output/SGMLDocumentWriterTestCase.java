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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.dtd.DTDBuilder;
import com.volantis.mcs.dom.sgml.SGMLDTDBuilder;
import com.volantis.mcs.dom.sgml.ElementModel;

import java.io.IOException;
import java.io.StringWriter;

/**
 * SGMLDocumentWriterTestCase
 */
public class SGMLDocumentWriterTestCase
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
        child1.setName("base");
        root.addHead(child1);

        child1 = domFactory.createElement("p");
        root.addTail(child1);

        child1 = domFactory.createElement("DIVIDE HINT");
        root.addTail(child1);

        child1 = domFactory.createElement("KEEPTOGETHER");
        root.addTail(child1);

        Element child2 = domFactory.createElement();
        child2.setName("child2");
        child2.setAttribute("depth", "'24 inches'");
        root.addTail(child2);

        Element child3 = domFactory.createElement();
        child3.setName("child3");
        child3.setAttribute("href", "<24 inches>");
        Text text = domFactory.createText();
        text.append("This is some text for \"Child3\"");
        child3.addHead(text);
        child2.addHead(child3);

        child3 = domFactory.createElement("script");
        text = domFactory.createText();
        text.append("This is some text for <script>");
        child3.addHead(text);
        child2.addTail(child3);

        child3 = domFactory.createElement("style");
        text = domFactory.createText();
        text.append("This is some text for <style>");
        child3.addHead(text);
        child2.addTail(child3);

        child3 = domFactory.createElement("p");
        text = domFactory.createText();
        text.append("This is some text for <paragraph>");
        child3.addHead(text);
        child2.addTail(child3);

        child3 = domFactory.createElement("p");
        text = domFactory.createText();
        text.append("This <node> is \"Text\" and already encoded.");
        text.setEncoded(true);
        child3.addHead(text);
        child2.addTail(child3);


    }

    protected DTDBuilder createDTDBuilder() {
        return new SGMLDTDBuilder();
    }

    /**
     * Test the SGMLDocumentWriter with a wrapped XMLDocumentWriter
     * this should not expand the special elements but should turn off encoding
     * for the script and style elements
     */
    public void testXMLBasedWriter() {
        StringWriter writer = new StringWriter();
        SGMLDTDBuilder builder = new SGMLDTDBuilder();

        String[] elements = new String[] {"script","style"};
        builder.setElementModel(elements,  ElementModel.CDATA);

        builder.setElementModel("base", ElementModel.EMPTY);
        builder.setElementModel("DIVIDE HINT", ElementModel.EMPTY);
        builder.setElementModel("KEEPTOGETHER", ElementModel.EMPTY);

        DTD dtd = builder.buildDTD();

        DocumentWriter dw = dtd.createDocumentWriter(writer);

        DOMDocumentOutputter outputter = new DOMDocumentOutputter(
                dw, encoder);

        try {
            outputter.output(root);
        } catch (IOException e) {
            fail( "Unexpected IOException.");
        }

        String output = writer.toString().trim();
        String expected =
            // Attribute should be encoded
            "<root height=\"&lt;18 inches&gt;\">" +
                // All elements with an empty content model should be closed
                // and special elements not recognised.
                "<base/>" +
                "<p></p>" +
                "<DIVIDE HINT/>" +
                "<KEEPTOGETHER/>" +
                // Single quotes are fine
                "<child2 depth=\"'24 inches'\">" +
                    // href should be encoded
                    "<child3 href=\"&lt;24 inches&gt;\">" +
                    // Text should be encoded
                    "This is some text for &quot;Child3&quot;" +
                    "</child3>" +
                    "<script>" +
                    // Text should NOT be encoded
                    "This is some text for <script>" +
                    "</script>" +
                    "<style>" +
                    // Text should NOT be encoded
                    "This is some text for <style>" +
                    "</style>" +
                    "<p>" +
                    // Text should be encoded
                    "This is some text for &lt;paragraph&gt;" +
                    "</p>" +
                    "<p>" +
                    // Text should not be encoded
                    "This <node> is \"Text\" and already encoded." +
                    "</p>" +
                "</child2>" +
            "</root>";

        assertEquals("Output not as expected", expected, output );
    }

    public void testNonReplaceableAttributes() throws IOException {
        Element element = domFactory.createElement("a");
        element.setAttribute("href", "B&Q");
        element.setAttribute("other", "A<B");

        SGMLDTDBuilder builder = new SGMLDTDBuilder();
        builder.addNonReplaceableAttribute("href");
        DTD dtd = builder.buildDTD();

        checkOutput(dtd, element, "<a href=\"B&Q\" other=\"A&lt;B\"></a>");
    }

    public void testEmptyElementWithOptionalEndTag() throws IOException {
        Element root = domFactory.createElement("div");
        Element br = domFactory.createElement("br");
        root.addTail(br);
        Element empty = domFactory.createElement("empty");
        root.addTail(empty);

        SGMLDTDBuilder builder = new SGMLDTDBuilder();
        builder.setElementModel("br", ElementModel.EMPTY);
        builder.setElementModel("empty", ElementModel.EMPTY);
        builder.setEndTagOptional("br");
        DTD dtd = builder.buildDTD();

        checkOutput(dtd, root, "<div><br><empty/></div>");
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/4	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Feb-04	2974/1	steve	VBM:2004020608 Added and Expanded Document Writer test cases.

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 ===========================================================================
*/
