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
package com.volantis.mcs.runtime.css.emulator;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.packagers.PackagingException;
import org.apache.log4j.Category;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Integration testcase to test HTML 3.2 CSS emulation.
 */
public class HTMLVersion3_2CSSEmulationTestCase
    extends CSSEmulatorTestAbstract {

    protected void setUp() throws Exception {
        Category.getRoot().removeAllAppenders();
        // Sets up log4j with a Threshold of info - we need debug ...
        super.setUp();
        // Turn up log4j *Threshold* to debug.
        // Each test still needs to set a *Level* for it's Category
        enableLog4jDebug();
    }

    /**
     * Return an instance of the Protocol class HTMLVersion3_2.
     *
     * @return HTML 3.2 Protocol class
     */
    protected DOMProtocol createProtocol() {
        // todo: use test version, requires moving all test protocols to runtime.
        ProtocolBuilder builder = new ProtocolBuilder();

        ((DefaultDevice) device.getDevice()).setPolicyValue(
            DevicePolicyConstants.SUPPORTS_JAVASCRIPT, "true");
        device.setProtocolConfiguration(null);

        return (DOMProtocol) builder.build(
                new ProtocolRegistry.HTMLVersion3_2Factory(), device);
    }

    // javadoc inherited
    protected String getProtocolName() {
        return "HTMLVersion3_2";
    }


    /**
     * Test nested Pane, Paragraph, Span structure with a simple bold text
     * message.
     * <p/>
     * Simulated input: <canvas layoutName="/layout.mlyt" theme="/theme.mthm">
     * <pane name="pane">
     * <p/>
     * <span styleClass="bold">bold:A bold message</span> </p> </pane>
     * </canvas>
     *
     * @throws IOException
     * @throws PackagingException
     * @throws PAPIException
     * @throws ParserConfigurationException
     * @throws RepositoryException
     * @throws SAXException
     */
    public void testSingleSpan()
        throws RepositoryException, PAPIException, IOException,
        PackagingException, ParserConfigurationException, SAXException {


        open("canvas",
             new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt",
                          null});

        open("pane", null, "pane");

        open("p");

        open("span", "bold");

        writeMessage("bold:A bold message");

        close("span");

        close("p");

        close("pane");

        close("canvas");
        final String expected =
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">" +
                "<!--Test Page Heading Message-->" +
                "<html>" +
                    "<head>" +
                        "<title>Page Title</title>" +
                    "</head>" +
                    "<body>" +
                        "<p>" +
                            "<font size=\"4\">" +
                                "<b>" +
                                    "<font size=\"4\">" +
                                        "bold:A bold message" +
                                    "</font>" +
                                "</b>" +
                            "</font>" +
                        "</p>" +
                    "</body>" +
                "</html>";

        assertEquals("Output HTML incorrectly formatted.", expected,
                writer.toString());
    }

    /**
     * Test bold being pushed down into a heading.
     * <p/>
     * Simulated input: <canvas layoutName="/layout.mlyt" theme="/theme.mthm">
     * <pane name="pane" styleClass="bold">
     * <p/>
     * <span>bold:Before heading message</span> </p> <h1> <span
     * styleClass="normal">A normal heading</span> </h1>
     * <p/>
     * <span>bold:After heading message</span> </p> </pane> </canvas>
     *
     * @throws IOException
     * @throws PackagingException
     * @throws PAPIException
     * @throws RepositoryException
     */
    public void testNestedBold() throws RepositoryException, PAPIException,
        PackagingException, IOException {

        open("canvas",
             new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt",
                          null});

        open("pane", "bold", "pane");

        open("p");

        open("span");

        writeMessage("bold:Before heading message");

        close("span");

        open("h1", "normal");

        writeMessage("A normal heading");

        close("h1");

        open("span");

        writeMessage("bold:After heading message");

        close("span");

        close("p");

        close("pane");

        close("canvas");

        final String expected =
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">" +
                "<!--Test Page Heading Message-->" +
                "<html>" +
                    "<head>" +
                        "<title>Page Title</title>" +
                    "</head>" +
                    "<body>" +
                        "<p>" +
                            "<font size=\"4\">" +
                                "<b>" +
                                    "bold:Before heading message" +
                                "</b>" +
                            "</font>" +
                            "<h1>" +
                                "<b>" +
                                    "<font size=\"4\">" +
                                        "A normal heading" +
                                    "</font>" +
                                "</b>" +
                            "</h1>" +
                            "<b>" +
                                "<font size=\"4\">" +
                                    "bold:After heading message" +
                                "</font>" +
                            "</b>" +
                        "</p>" +
                    "</body>" +
                "</html>";

        assertEquals("Output HTML incorrectly formatted.", expected,
                writer.toString());
    }

    /**
     * Test the a style class on the canvas tag is honoured.
     * <p/>
     * Simulated input: <canvas styleClass="bold" layoutName="/layout.mlyt"
     * theme="/theme.mthm"> <pane name="pane">
     * <p/>
     * bold:A short message </p> </pane> </canvas>
     *
     * @throws RepositoryException
     * @throws PAPIException
     * @throws PackagingException
     * @throws IOException
     */
    public void testBoldCanvas() throws RepositoryException, PAPIException,
        PackagingException, IOException {

        open("canvas",
             new String[]{"bold", "Page Title", "/theme.mthm", "/layout.mlyt",
                          null});

        open("pane", null, "pane");

        open("p");

        writeMessage("bold:A short message");

        close("p");

        close("pane");

        close("canvas");

        final String expected =
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">" +
                "<!--Test Page Heading Message-->" +
                "<html>" +
                    "<head>" +
                        "<title>Page Title</title>" +
                    "</head>" +
                    "<body>" +
                        "<p>" +
                            "<b>" +
                                "<font size=\"4\">" +
                                    "bold:A short message" +
                                "</font>" +
                            "</b>" +
                        "</p>" +
                    "</body>" +
                "</html>";

        assertEquals("Output HTML incorrectly formatted.", expected,
                writer.toString());
    }

    /**
     * Test that all headers are correctly formatted.
     *
     * @throws PAPIException
     * @throws PackagingException
     * @throws IOException
     */
    public void testHeadingTags() throws PAPIException, PackagingException,
        IOException {

        open("canvas",
             new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt",
                          null});

        open("pane", null, "pane");

        open("h1");
        writeMessage("Heading 1");
        close("h1");

        open("h2", "bold");
        writeMessage("Heading 2");
        close("h2");

        open("h3");
        writeMessage("Heading 3");
        close("h3");

        open("h4");
        writeMessage("Heading 4");
        close("h4");

        open("h5");
        writeMessage("Heading 5");
        close("h5");

        open("h6");
        writeMessage("Heading 6");
        close("h6");

        close("pane");

        close("canvas");

        final String expected =
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">" +
                "<!--Test Page Heading Message-->" +
                "<html>" +
                    "<head>" +
                        "<title>Page Title</title>" +
                    "</head>" +
                    "<body>" +
                        "<h1>" +
                            "<font size=\"4\">" +
                                "Heading 1" +
                            "</font>" +
                        "</h1>" +
                        "<h2>" +
                            "<b>" +
                                "<font size=\"4\">" +
                                    "Heading 2" +
                                "</font>" +
                            "</b>" +
                        "</h2>" +
                        "<h3>" +
                            "<font size=\"4\">" +
                                "Heading 3" +
                            "</font>" +
                        "</h3>" +
                        "<h4>" +
                            "<font size=\"4\">" +
                                "Heading 4" +
                            "</font>" +
                        "</h4>" +
                        "<h5>" +
                            "<font size=\"4\">" +
                                "Heading 5" +
                            "</font>" +
                        "</h5>" +
                        "<h6>" +
                            "<font size=\"4\">" +
                                "Heading 6" +
                            "</font>" +
                        "</h6>" +
                    "</body>" +
                "</html>";

        assertEquals("Output HTML incorrectly formatted.", expected,
                writer.toString());
    }

    /**
     * Test that a simple table is rendered correctly.
     *
     * @throws PAPIException
     * @throws PackagingException
     * @throws IOException
     */
    public void testTableTags() throws PAPIException, PackagingException,
        IOException {

        open("canvas",
             new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt",
                          null});

        open("pane", null, "pane");

        open("table", "small");

        open("tr", "x-large");
        open("th", "large");
        writeMessage("Heading 1");
        close("th");
        open("th");
        writeMessage("Heading 2");
        close("th");
        open("th");
        writeMessage("Heading 3");
        close("th");
        close("tr");

        open("tr");
        open("td");
        writeMessage("Data 1");
        close("td");
        open("td");
        writeMessage("Data 2");
        close("td");
        open("td");
        writeMessage("Data 3");
        close("td");
        close("tr");

        close("table");

        close("pane");

        close("canvas");

        final String expected =
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">" +
                "<!--Test Page Heading Message-->" +
                "<html>" +
                    "<head>" +
                        "<title>Page Title</title>" +
                    "</head>" +
                    "<body>" +
                        "<table cellpadding=\"0\" cellspacing=\"0\">" +
                            "<tr>" +
                                "<th>" +
                                    "<font size=\"5\">" +
                                        "Heading 1" +
                                    "</font>" +
                                "</th>" +
                                "<th>" +
                                    "<font size=\"6\">" +
                                        "Heading 2" +
                                    "</font>" +
                                "</th>" +
                                "<th>" +
                                    "<font size=\"6\">" +
                                        "Heading 3" +
                                    "</font>" +
                                "</th>" +
                            "</tr>" +
                            "<tr>" +
                                "<td>" +
                                    "<font size=\"3\">" +
                                        "Data 1" +
                                    "</font>" +
                                "</td>" +
                                "<td>" +
                                    "<font size=\"3\">" +
                                        "Data 2" +
                                    "</font>" +
                                "</td>" +
                                "<td>" +
                                    "<font size=\"3\">" +
                                        "Data 3" +
                                    "</font>" +
                                "</td>" +
                            "</tr>" +
                        "</table>" +
                    "</body>" +
                "</html>";

        assertEquals("Output HTML incorrectly formatted.", expected,
                writer.toString());
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10581/3	pduffin	VBM:2005112407 Fixed pair rendering issue, valign="baseline" and also fixed string rendering as well

 05-Dec-05	10585/1	pduffin	VBM:2005112407 Fixed pair rendering issue, valign="baseline" and also fixed string rendering as well

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10493/1	geoff	VBM:2005102407 Pane width attributes rendered incorrectly

 29-Nov-05	10036/4	geoff	VBM:2005102407 Pane width attributes rendered incorrectly

 31-Oct-05	10036/1	geoff	VBM:2005102407 Pane width attributes rendered incorrectly

 23-Nov-05	10402/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Nov-05	10381/3	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10328/1	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 17-Nov-05	10330/2	pabbott	VBM:2005110907 Honour align with mode=nospace

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 07-Nov-05	10116/4	emma	VBM:2005103107 Supermerge

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 07-Nov-05	10166/1	geoff	VBM:2005102408 Backwards port: Pane style class renders layout rather than theme bgcolor

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 28-Oct-05	10024/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 28-Oct-05	10020/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 29-Nov-05	10036/4	geoff	VBM:2005102407 Pane width attributes rendered incorrectly

 31-Oct-05	10036/1	geoff	VBM:2005102407 Pane width attributes rendered incorrectly

 22-Nov-05	10381/3	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10330/2	pabbott	VBM:2005110907 Honour align with mode=nospace

 15-Nov-05	10326/16	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/14	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 07-Nov-05	10173/4	emma	VBM:2005103107 Supermerge required

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 07-Nov-05	10116/4	emma	VBM:2005103107 Supermerge

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 07-Nov-05	10166/1	geoff	VBM:2005102408 Backwards port: Pane style class renders layout rather than theme bgcolor

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 07-Nov-05	10166/1	geoff	VBM:2005102408 Backwards port: Pane style class renders layout rather than theme bgcolor

 01-Nov-05	10046/1	geoff	VBM:2005102408 Pane style class renders layout rather than theme bgcolor

 28-Oct-05	10024/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 28-Oct-05	10024/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 28-Oct-05	10020/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 28-Oct-05	10020/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 03-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9184/6	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 ===========================================================================
*/
