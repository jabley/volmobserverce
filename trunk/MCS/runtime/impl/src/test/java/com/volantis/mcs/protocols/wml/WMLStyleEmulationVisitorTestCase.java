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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitor;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitorTestAbstract;

/**
 * A test case for {@link StyleEmulationVisitor} when configured for WML
 * processing.
 */
public class WMLStyleEmulationVisitorTestCase
        extends StyleEmulationVisitorTestAbstract {

    // Javadoc inherited.
    protected ProtocolConfigurationImpl getProtocolConfiguration() {
        return new WMLRootConfiguration();
    }

    protected String getParagraphElement() {
        return "BLOCK";
    }

    // Javadoc inherited.
    protected String getExpectedPushMatchingParentElementDownSimple() {
        final String expected =
                "<b>textA</b>" +
                "<a>" +
                    "txt-1" +
                    "<ANTI-B>txt-3</ANTI-B>" +
                    "txt-2" +
                "</a>" +
                "<b>textE</b>";
        return expected;
    }

    // Javadoc inherited.
    protected String getExpectedPushMatchingParentElementDown() {
        final String expected =
                "<i>" +
                    "<b>textA</b>" +
                    "<a>" +
                        "txt-1" +
                        "<ANTI-B>txt-3</ANTI-B>" +
                        "txt-2" +
                    "</a>" +
                    "<b>textE</b>" +
                "</i>";
        return expected;
    }

    // Javadoc inherited.
    protected String getExpectedAtomicity() {
        String expected =
            "<b>" +
                "Block text before" +
                "<a href=\"...\">" +
                    "Bold link text 1" +
                    "Normal link Text" +
                    "Bold link text 2" +
                "</a>" +
                "Bold text after" +
            "</b>";
        return expected;
    }

    // Javadoc inherited.
    protected String getExpectedAtomicityNested() {
        String expected =
            "<b>" +
                "<i>" +
                    "Block text before" +
                    "<a href=\"...\">" +
                        "Bold link text 1" +
                        "Normal link Text" +
                        "Bold link text 2" +
                    "</a>" +
                    "Bold text after" +
                "</i>" +
            "</b>";
        return expected;
    }

    // Javadoc inherited
    protected String getExpectedDivisibleStyleElements() {
        return  "<sup>" +
                    "[not bold][bold]" +
                "</sup>" +
                "<BLOCK>" +
                    "<b>" +
                        "[bold-paragraph]" +
                    "</b>" +
                "</BLOCK>";
    }

    /**
     * Test the transformation using anti-size.
     */
    public void testTransformAntiSize() throws Exception {
        String input =
            "<BLOCK>" +
                "[normal paragraph-1]" +
                "<big>" +
                    "big text" +
                    "<ANTI-SIZE>" +
                        "normal-1" +
                    "</ANTI-SIZE>" +
                    "<small>" +
                        "small-1" +
                        "<ANTI-SIZE>" +
                            "normal-2" +
                        "</ANTI-SIZE>" +
                    "</small>" +
                "</big>" +
                "<small>" +
                    "small-2" +
                    "<ANTI-SIZE>" +
                        "normal-3" +
                    "</ANTI-SIZE>" +
                "</small>" +
                "[normal paragraph-2]" +
            "</BLOCK>";

        // note: big may contain small (is valid wml)
        String expected =
                "<BLOCK>[normal paragraph-1]<big>big text</big>normal-1<big>" +
                    "<small>small-1</small></big>normal-2<small>small-2" +
                "</small>normal-3[normal paragraph-2]</BLOCK>";

        doTest(input, expected);
    }

    /**
     * Test WML with unused anti-xxxx elements.
     */
    public void testVisitWML() throws Exception {
        String input =
            "<wml>" +
                "<card title='BBC Sport'>" +
                    "<BLOCK>" +
                        "<b>Olympics</b>" +
                    "</BLOCK>" +
                    "<BLOCK>" +
                        "<ANTI-I>" +
                        "</ANTI-I>" +
                        "<ANTI-SIZE>" +
                        "</ANTI-SIZE>" +
                    "</BLOCK>" +
                    "<BLOCK>" +
                        "<ANTI-SIZE>" +
                            "<a href='http://www.bbc.co.uk/mobile/olympics/top_stories/'>Top stories</a>" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/olympics/events/'>Sport-by-sport</a>" +
                            "<br></br>" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/olympics/latest/'>Latest results</a>" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/olympics/medals/'>Medal table</a>" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/olympics/yoursay/'>Have your say</a>" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/olympics/gallery/'>Photo gallery</a>" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/olympics/tv/'>BBC coverage</a>" +
                            "<br></br>" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/olympics/greek/'>Greek phrases</a>" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/olympics/quiz/'>Quiz</a>" +
                            "<br></br>" +
                        "</ANTI-SIZE>" +
                    "</BLOCK>" +
                    "<BLOCK>" +
                        "<ANTI-SIZE>" +
                            "<br></br>--------------------" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/bbc_sport/'>BBC Sport</a>" +
                            "<br></br>" +
                            "<a href='http://www.bbc.co.uk/mobile/'>BBC Home</a>" +
                        "</ANTI-SIZE>" +
                    "</BLOCK>" +
                "</card>" +
            "</wml>";

        String expected =
                "<wml><card title=\"BBC Sport\">" +
                "<BLOCK><b>Olympics</b></BLOCK><BLOCK/><BLOCK>" +
                "<a href=\"http://www.bbc.co.uk/mobile/olympics/top_stories/\">Top stories</a><br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/olympics/events/\">Sport-by-sport</a><br/><br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/olympics/latest/\">Latest results</a><br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/olympics/medals/\">Medal table</a><br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/olympics/yoursay/\">Have your say</a><br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/olympics/gallery/\">Photo gallery</a><br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/olympics/tv/\">BBC coverage</a><br/><br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/olympics/greek/\">Greek phrases</a><br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/olympics/quiz/\">Quiz</a><br/></BLOCK><BLOCK>" +
                    "<br/>--------------------<br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/bbc_sport/\">BBC Sport</a><br/>" +
                "<a href=\"http://www.bbc.co.uk/mobile/\">BBC Home</a></BLOCK></card>" +
                "</wml>";

        doTest(input, expected);
    }

    // todo: add tests equivalent to testTransformNestedFonts... for WML
    // this would involve big, small and anti-size elements

    /**
     * Test that a select element inside a big element works correctly.
     * <p>
     * This was created for VBM:2004100710.
     * <p>
     * In this case select is not a "permitted child" of big, but select cannot
     * contain big either so the big stays where it is.
     * <p>
     * Previous to this VBM select was not registered as not permitting
     * stylistic content so big was pushed down into it.
     */
    public void testBigWithSelect() throws Exception {
        String actual =
                "<BLOCK>" +
                    "<big>" +
                        "<select>" +
                            "<option/>" +
                        "</select>" +
                        "text" +
                    "</big>" +
                "</BLOCK>";

        // We don't expect any change.
        String expected =
                "<BLOCK>" +
                    "<select>" +
                        "<option/>" +
                    "</select>" +
                    "<big>text</big>" +
                "</BLOCK>";
        doTest(actual, expected);
    }

    /**
     * Test that a br element inside a big element works correctly.
     * <p>
     * This was created for VBM:2004100710.
     * <p>
     * In this case the not-permitted element is not a "permitted child" of any
     * of the stylistic elements. Thus, big ends up being pushed down into the
     * not-permitted element.
     * <p>
     * Previous to this VBM, br was not registered as not permitting stylistic
     * content so big was pushed down into it.
     */
    public void testBigWithBr() throws Exception {
        String actual =
                "<BLOCK>" +
                    "<big>" +
                        "<br/>" +
                        "<i>" +
                            "<do>" +
                                "not permitted text" +
                            "</do>" +
                            "text" +
                        "</i>" +
                        "text" +
                        "<br/>" +
                        " " +
                        "<card>text</card>" +
                    "</big>" +
                "</BLOCK>";

        String expected =
                "<BLOCK>" +
                    "<br/>" +
                    "<do>" +
                        "not permitted text" +
                    "</do>" +
                    "<big>" +
                        "<i>text</i>" +
                    "</big>" +
                    "<big>text</big>" +
                    "<br/>" +
                    "<big> </big>" +
                    "<card>text</card>" +
                "</BLOCK>";
        doTest(actual, expected);
    }

    /**
     * Test that a table element inside a big element works correctly.
     * <p>
     * This was created for VBM:2004100712.
     * <p>
     * In this case the not-permitted element is not a "permitted child" of any
     * of the stylistic elements. Thus, big ends up being pushed down into the
     * not-permitted element. Previous to this VBM, br was not registered as
     * not permitting stylistic content so big was pushed down into it.
     */
    public void testBigWithTable() throws Exception {
        String actual =
                "<BLOCK>" +
                    "<big>" +
                        "<table>" +
                            "<tr>" +
                                "<td>" +
                                    "<not-permitted>" +
                                        "not permitted text" +
                                    "</not-permitted>" +
                                "</td>" +
                            "</tr>" +
                            "<tr>" +
                                "<td>" +
                                    "text" +
                                "</td>" +
                            "</tr>" +
                        "</table>" +
                    "</big>" +
                "</BLOCK>";

        String expected =
                "<BLOCK>" +
                    "<big>" +
                        "<table>" +
                            "<tr>" +
                                "<td>" +
                                    "<not-permitted>" +
                                        "not permitted text" +
                                    "</not-permitted>" +
                                "</td>" +
                            "</tr>" +
                            "<tr>" +
                                "<td>" +
                                    "text" +
                                "</td>" +
                            "</tr>" +
                        "</table>" +
                     "</big>" +
                "</BLOCK>";
        doTest(actual, expected);
    }

    /**
     * Test that a list element inside a big element works correctly.
     * <p>
     * This was created for VBM:2004100712.
     * <p>
     * In this case the not-permitted element is not a "permitted child" of any
     * of the stylistic elements. Thus, big ends up being pushed down into the
     * not-permitted element. Previous to this VBM, br was not registered as
     * not permitting stylistic content so big was pushed down into it.
     */
    public void testBigWithList() throws Exception {
        String actual =
                "<BLOCK>" +
                    "<big>" +
                        "<ol>" +
                            "<li>" +
                                "<not-permitted>" +
                                    "not permitted text" +
                                "</not-permitted>" +
                            "</li>" +
                        "</ol>" +
                        "<ol>" +
                            "<li>" +
                                "text" +
                            "</li>" +
                        "</ol>" +
                    "</big>" +
                "</BLOCK>" +
                "";

        String expected =
                "<BLOCK>" +
                    "<ol>" +
                        "<li>" +
                            "<not-permitted>" +
                                "not permitted text" +
                            "</not-permitted>" +
                        "</li>" +
                    "</ol>" +
                    "<ol>" +
                        "<li>" +
                            "text" +
                        "</li>" +
                    "</ol>" +
                "</BLOCK>";
        doTest(actual, expected);
    }

    /**
     * Test that a PRE element is handled correctly.
     */
    public void testPre() throws Exception {
        String actual =
                "<pre>" +
                    "<BLOCK>" +
                        "<select>" +
                            "<option/>" +
                        "</select>" +
                        "text" +
                    "</BLOCK>" +
                    "<big>text</big>" +
                "</pre>";

        // We don't expect any change.
        String expected =
                "<pre>" +
                  "<BLOCK>" +
                    "<select>" +
                        "<option/>" +
                    "</select>" +
                    "text" +
                  "</BLOCK>" +
                  "<big>text</big>" +
                "</pre>";
        doTest(actual, expected);
    }

    // javadoc inherited
    protected String getNestedFont() {
        return
                "<BLOCK>" +
                    "<font size=\"3\">" +
                        "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" +
                            "<tr>" +
                                "<td align=\"center\" bgcolor=\"#c6d3de\">" +
                                    "<font color=\"#ffffff\">" +
                                        "<a href=\"/wps/portal/!ut/p/.scr/Login\">" +
                                            "<font color=\"#3366cc\">" +
                                                "<img alt=\"[Log in]\" border=\"0\" height=\"20\" src=\"/wps/themes/xdime/images/nav_login.gif\" width=\"20\"/>" +
                                            "</font>" +
                                        "</a>" +
                                        "<a href=\"/wps/portal/!ut/p/.scr/ForgotPassword\">" +
                                            "<font color=\"#000000\">" +
                                                "<img alt=\"[I forgot my password]\" border=\"0\" height=\"20\" src=\"/wps/themes/xdime/images/nav_forgot_password.gif\" width=\"23\"/>" +
                                            "</font>" +
                                        "</a>" +
                                    "</font>" +
                                "</td>" +
                            "</tr>" +
                        "</table>" +
                    "</font>" +
                "</BLOCK>";
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 03-Oct-05	9522/2	ibush	VBM:2005091502 no_save on images

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 09-Aug-05	9211/2	pabbott	VBM:2005080902 End to End CSS emulation test

 31-Dec-04	6433/2	matthew	VBM:2004120805 fix null pointer exception in pushCounterpartElementDown

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Oct-04	5877/9	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 27-Oct-04	5877/7	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 26-Oct-04	5877/5	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 19-Oct-04	5843/2	geoff	VBM:2004100710 Invalid WML is being generated since introduction of theme style options (R599)

 ===========================================================================
*/
