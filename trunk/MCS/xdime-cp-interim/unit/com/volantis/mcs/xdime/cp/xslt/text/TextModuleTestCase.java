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

package com.volantis.mcs.xdime.cp.xslt.text;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the text-module.xsl stylesheet.
 */
public class TextModuleTestCase extends XSLTTestAbstract {

    /**
     * Tests that the ordinary text elements (that is, non-emulated ones) are
     * transformed according to AN062 section 3.6.
     * @throws Exception
     */
    public void testOrdinaryTextElements() throws Exception {
        doTransform("Non-emulated text element transformations failed",
                getInputSourceForClassResource("text-module-test.xsl"),
                getInputSourceForString(
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS + ">" +
                "<cite class=\"cite-class\">Paul Duffin</cite>" +
                "<code class=\"code-class\" access=\"ignoreme\">String s;</code>" +
                "<em class=\"em-class\">important</em>" +
                "<kbd class=\"kbd-class\" href=\"one.html\" access=\"access-me.html\">QUIT</kbd>" +
                "<samp class=\"samp-class\">Quit (Y/N) ?</samp>" +
                "<span class=\"span-class\">something</span>" +
                "<strong class=\"strong-class\" href=\"two.html\">very important</strong>" +
                "<sub class=\"sub-class\">low</sub>" +
                "<sup class=\"sup-class\">high</sup>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<cite class=\"cite-class\" pane=\"cite-pane\">Paul Duffin</cite>" +
                "<code class=\"code-class\" pane=\"code-pane\">String s;</code>" +
                "<em class=\"em-class\" pane=\"em-pane\">important</em>" +
                "<a href=\"one.html\" shortcut=\"access-me.html\"><kbd class=\"kbd-class\" pane=\"kbd-pane\">QUIT</kbd></a>" +
                "<samp class=\"samp-class\" pane=\"samp-pane\">Quit (Y/N) ?</samp>" +
                "<span class=\"span-class\" pane=\"span-pane\">something</span>" +
                "<a href=\"two.html\"><strong class=\"strong-class\" pane=\"strong-pane\">very important</strong></a>" +
                "<sub class=\"sub-class\" pane=\"sub-pane\">low</sub>" +
                "<sup class=\"sup-class\" pane=\"sup-pane\">high</sup>" +
                "</unit>"));
    }

    /**
     * Tests that the emulated text elements are transformed according to
     * AN062 section 3.6.
     * @throws Exception
     */
    public void testEmulatedTextElements() throws Exception {
        doTransform("Emulated text element transformations failed",
                getInputSourceForClassResource("text-module-test.xsl"),
                getInputSourceForString(
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS + ">" +
                "<div>" +
                "<abbr class=\"paned\">MCS</abbr>" +
                "<quote class=\"paned\">Able was I ere I saw Elba</quote>" +
                "<quote class=\"funny\">" +
                "Twas brillig, and the slithy toves</quote>" +
                "<quote class=\"hilarious\">" +
                "Did gyre and gimble in the wabe</quote>" +
                "The <dfn class=\"paned\">hypotenuse</dfn> " +
                "is the long side of a right angle triangle." +
                "<var class=\"paned\">i</var>" +
                "</div>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<div>" +
                "<span pane=\"abbr-pane\" class=\"mcs-emulated-abbr-paned\">MCS</span>" +
                "<span pane=\"quote-pane\" class=\"mcs-emulated-quote-paned\">" +
                "Able was I ere I saw Elba</span>" +
                "<span class=\"mcs-emulated-quote-funny\">" +
                "Twas brillig, and the slithy toves</span>" +
                "<span class=\"mcs-emulated-quote-hilarious\">" +
                "Did gyre and gimble in the wabe</span>" +
                "The <span pane=\"dfn-pane\" class=\"mcs-emulated-dfn-paned\">hypotenuse</span> " +
                "is the long side of a right angle triangle." +
                "<span pane=\"var-pane\" class=\"mcs-emulated-var-paned\">i</span>" +
                "</div>" +
                "</unit>"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/17	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/15	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/13	pcameron	VBM:2004060306 Fixed test cases after integration

 11-Jun-04	4645/11	pcameron	VBM:2004060306 Commit changes for integration

 11-Jun-04	4645/9	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
*/
