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

package com.volantis.mcs.xdime.cp.xslt.structure;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the structure-module.xsl stylesheet.
 */
public class StructuralModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that the structural block elements are transformed according to
     * AN062 section 3.3.
     * @throws Exception
     */
    public void testBlockElements() throws Exception {
        doTransform("Structure transforms failed",
                getInputSourceForClassResource("structural-module-test.xsl"),
                getInputSourceForString(
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS + ">" +
                "<div class=\"div-class\">" +
                "<address class=\"addr-class\" href=\"d\" id=\"blah\" >This is where I live</address>" +
                "<blockquote class=\"bq-class\">Able was <p>I ere I</p> saw elba.</blockquote>" +
                "<h1>Very Important Heading</h1>" +
                "<h2>Not So Important A Heading</h2>" +
                "<h3 class=\"ignored-class\">Less Important</h3>" +
                "<h4 class=\"h4-class\">Less And Less Important</h4>" +
                "<h5>Less And Less And Less Important</h5>" +
                "<h6 title=\"12\" class=\"qwerty\" href=\"blah.html\">" +
                "Less And Less And Less Important</h6>" +
                "<p class=\"para-class\">Optional <p>content</p> only required on foo devices.</p>" +
                "<pre class=\"pre-class\">  S  o  m  e       e  x  p  a  n  d  e  d" +
                "       t  e  x  t  . </pre>" +
                "</div>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<div class=\"div-class\" pane=\"div-pane\">" +
                "<address class=\"addr-class\" pane=\"addr-pane\" id=\"blah\"><a href=\"d\">This is where I live</a></address>" +
                "<blockquote class=\"bq-class\" pane=\"bq-pane\">Able was <p>I ere I</p> saw elba.</blockquote>" +
                "<h1>Very Important Heading</h1>" +
                "<h2>Not So Important A Heading</h2>" +
                "<h3 class=\"ignored-class\">Less Important</h3>" +
                "<h4 class=\"h4-class\" pane=\"h4-pane\">Less And Less Important</h4>" +
                "<h5>Less And Less And Less Important</h5>" +
                "<h6 title=\"12\" class=\"qwerty\"><a href=\"blah.html\">" +
                "Less And Less And Less Important</a></h6>" +
                "<p class=\"para-class\" pane=\"para-pane\">Optional <p>content</p> only required on foo devices.</p>" +
                "<pre class=\"pre-class\" pane=\"pre-pane\">  S  o  m  e       e  x  p  a  n  d  e  d" +
                "       t  e  x  t  . </pre>" +
                "</div>" +
                "</unit>"));
    }

    /**
     *
     * @throws Exception
     */
    public void testDivElementWithHref() throws Exception {
        boolean detected = false;
        try {
            doTransform("href attribute on div element not detected",
                    getInputSourceForClassResource("structural-module-test.xsl"),
                    getInputSourceForString(
                            "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                    " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS + ">" +
                    "<div href=\"blah\">" +
                    "<address b=\"d\" c=\"blah\">This is where I live</address>" +
                    "<blockquote>Able was <p>I ere I</p> saw elba.</blockquote>" +
                    "<h1>Very Important Heading</h1>" +
                    "<h2>Not So Important A Heading</h2>" +
                    "<h3>Less Important</h3>" +
                    "<h4 b=\"d\">Less And Less Important</h4>" +
                    "<h5>Less And Less And Less Important</h5>" +
                    "<h6 a=\"12\" b=\"qwerty\" href=\"blah.html\">" +
                    "Less And Less And Less Important</h6>" +
                    "<p>Optional content only required on foo devices.</p>" +
                    "<pre>  S  o  m  e       e  x  p  a  n  d  e  d" +
                    "       t  e  x  t  . </pre>" +
                    "</div>" +
                    "</mcs:unit>"),
                    getInputSourceForString(
                            "<unit></unit>"));
        } catch (RuntimeException re) {
            detected = true;
        }

        assertTrue("Did not detect href attribute on div element",
                detected);

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/21	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/19	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/17	pcameron	VBM:2004060306 Fixed test cases after integration

 11-Jun-04	4645/13	pcameron	VBM:2004060306 Commit changes for integration

 10-Jun-04	4645/8	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/5	pcameron	VBM:2004060306 Commit changes for integration

 08-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 08-Jun-04	4630/3	pduffin	VBM:2004060306 Added some constants

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
*/
