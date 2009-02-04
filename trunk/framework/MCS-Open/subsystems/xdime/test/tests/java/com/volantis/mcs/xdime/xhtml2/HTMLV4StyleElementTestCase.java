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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.runtime.packagers.PackagingException;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Integration testcase to test  XDIME 2 style element
 * with HTML 4 protocol.
 *
 * Moved to a separate test case form HTMLV4TestCase integration test case
 */
public class HTMLV4StyleElementTestCase extends HTMLV4TestCaseAbstract {
    /**
     * Test that an inline <style> element works correctly in an XDIME-CP page.
     */
    public void testInlineStyle() throws SAXException, PAPIException,
            PackagingException, IOException {

        // inline stylesheet requires activation
        marinerPageContextMock.expects.getCurrentProject().returns(null).any();
        marinerPageContextMock.expects.getBaseURL().returns(null).any();

        addCanvasExpectations();
        addElementOutputStateExpectations();

        openCanvas();

        openPane();

        startElement("html");

        startElement("head");
        startElement("title");
        endElement("title");
        startElement("style", new String[] { "type" },
                new String[] { "text/css" });
        writeMessage("p {background-color:red}");
        endElement("style");
        endElement("head");

        startElement("body");
        startElement("p");
        writeMessage("body content");
        endElement("p");
        closeDocument();

        String expected = "<p>" + "body content" + "</p>";

        assertBodyEqual(expected, "p{background:red}", getMarkup());
    }
}
