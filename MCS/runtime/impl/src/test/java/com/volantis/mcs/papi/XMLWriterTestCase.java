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
package com.volantis.mcs.papi;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.io.StringWriter;

/**
 * Test Case for the {@link XMLWriter}
 */
public class XMLWriterTestCase extends TestCaseAbstract {

    /**
     * Instance of XMLWriter being tested
     */
    private XMLWriter writer;

    /**
     * The xml will be written via this
     */
    private StringWriter strWriter;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        strWriter = new StringWriter();
        writer = new XMLWriter(strWriter);
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        writer = null;
        strWriter = null;
        super.tearDown();
    }

    /**
     * test to ensure the writer generated simple elements correctly
     * @throws Exception if an error occurs
     */
    public void testSimpleElmentNoAtts() throws Exception {
        PAPIAttributes anchorAtts = new AnchorAttributes();
        PAPIAttributes canvasAtts = new CanvasAttributes();

        writer.openElement(canvasAtts);
        writer.openElement(anchorAtts);
        writer.closeElement(anchorAtts);
        writer.closeElement(canvasAtts);

        assertEquals("XMLWriter failed to write simple markup ",
                     "<canvas><a></a></canvas>",
                     strWriter.toString());
    }

    /**
     * Test that elements and atts are written out correctly
     * @throws Exception if an error occurs
     */
    public void testSimpleElmentWithAtts() throws Exception {
        AnchorAttributes anchorAtts = new AnchorAttributes();
        anchorAtts.setHref("http://fred.com");
        CanvasAttributes canvasAtts = new CanvasAttributes();
        canvasAtts.setLayoutName("myLayout");


        writer.openElement(canvasAtts);
        writer.openElement(anchorAtts);
        writer.closeElement(anchorAtts);
        writer.closeElement(canvasAtts);
        assertEquals("XMLWriter failed to write simple markup with attributes",
                     "<canvas layoutName=\"myLayout\"><a href=\"http://fred.com\"></a></canvas>",
                     strWriter.toString());
    }

    /**
     * Test that attributes are encoded correctly
     * @throws Exception if an error occurs
     */
    public void testSimpleElmentWithEscapedAtts() throws Exception {
        AnchorAttributes anchorAtts = new AnchorAttributes();
        anchorAtts.setHref("http://fred.com/<test>");
        CanvasAttributes canvasAtts = new CanvasAttributes();
        canvasAtts.setLayoutName("\"myLayout's\"");


        writer.openElement(canvasAtts);
        writer.openElement(anchorAtts);
        writer.closeElement(anchorAtts);
        writer.closeElement(canvasAtts);

        assertEquals("XMLWriter failed to encode attribute values correctly ",
                     "<canvas layoutName=\"&quot;myLayout&apos;s&quot;\"><a href=\"http://fred.com/&lt;test&gt;\"></a></canvas>",
                     strWriter.toString());
    }
}
