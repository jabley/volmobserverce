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

import java.io.IOException;

/**
 * Integration testcase to test WML 1.3 CSS emulation.
 */
public class WMLCSSEmulationTestCase extends CSSEmulatorTestAbstract {

    /**
     * Return an instance of the Protocol class WML 1.3.
     *
     * @return WML 1.3 Protocol class
     */
    protected DOMProtocol createProtocol() {
        // todo: use test version, requires moving all test protocols to runtime.
        ProtocolBuilder builder = new ProtocolBuilder();

        ProtocolRegistry.WMLVersion1_3Factory protocolFactory =
                new ProtocolRegistry.WMLVersion1_3Factory();

        final DefaultDevice defaultDevice = (DefaultDevice) device.getDevice();
        defaultDevice.setPolicyValue(
            DevicePolicyConstants.SUPPORTS_JAVASCRIPT, "true");
        defaultDevice.setPolicyValue(
            DevicePolicyConstants.DEVICE_HONOURS_ALIGN_WHEN_MODE_NOWRAP, "true");

        //device.setProtocolConfiguration(protocolFactory.createConfiguration(device));
        device.setProtocolConfiguration(null);
        

        DOMProtocol protocol = (DOMProtocol) builder.build(
                protocolFactory, device);
        return protocol;
    }

    // Javadoc inherited
    protected String getProtocolName() {
        return "WMLVersion1_3";
    }


    /**
     * Test nested Pane, Paragraph, Span structure with a simple bold text
     * message.
     *
     * Simulated input:
     * <canvas layoutName="/layout.mlyt" theme="/theme.mthm">
     *   <pane name="pane">
     *     <p>
     *       <span styleClass="bold">bold:A bold message</span>
     *     </p>
     *   </pane>
     * </canvas>
     */
    public void testSingleSpan()
        throws RepositoryException, PAPIException, IOException,
        PackagingException {

        open("canvas",
            new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt", null});

        open("pane", null, "pane");

        open("p");

        open("span", "bold");

        writeMessage("bold:A bold message");

        close("span");

        close("p");

        close("pane");

        close("canvas");

        assertEquals("Output WML incorrectly formatted.",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
            "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.3//EN\" "+
                           "\"http://www.wapforum.org/DTD/wml13.dtd\">"+
            "<wml>"+
              "<card title=\"Page Title\">"+
                "<p>"+
                  "<b>bold:A bold message</b>"+
                "</p>"+
              "</card>"+
            "</wml>",
            writer.toString());

    }

    /**
     * Test bold being pushed down into a heading.
     *
     * Simulated input:
     * <canvas layoutName="/layout.mlyt" theme="/theme.mthm">
     *   <pane name="pane" styleClass="bold">
     *     <p>
     *       <span>bold:Before heading message</span>
     *     </p>
     *     <h1>
     *       <span styleClass="normal">A normal heading</span>
     *     </h1>
     *     <p>
     *       <span>bold:After heading message</span>
     *     </p>
     *   </pane>
     * </canvas>
     */
    public void testNestedBold() throws RepositoryException, PAPIException,
        PackagingException, IOException {

        open("canvas",
            new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt", null});

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

        assertEquals("Output WML incorrectly formatted.",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
            "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.3//EN\" "+
                        "\"http://www.wapforum.org/DTD/wml13.dtd\">"+
            "<wml>"+
              "<card title=\"Page Title\">"+
                "<p>"+
                  "<b>"+
                    "bold:Before heading message"+
                    "<br/>"+
                    "<big>A normal heading</big>"+
                    "<br/>"+
                    "bold:After heading message"+
                  "</b>"+
                "</p>"+
              "</card>"+
            "</wml>",
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
             new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt", null});

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

        String expected =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.3//EN\" " +
                        "\"http://www.wapforum.org/DTD/wml13.dtd\">" +
                "<wml>" +
                    "<card title=\"Page Title\">" +
                        "<p>" +
                            "<br/><b><big>Heading 1</big></b><br/>" +
                            "<br/><b><i>Heading 2</i></b><br/>" +
                            "<br/><i>Heading 3</i><br/>" +
                            "<br/>Heading 4<br/>" +
                            "<br/><i><small>Heading 5</small></i><br/>" +
                            "<br/><small>Heading 6</small>" +
                        "</p>" +
                    "</card>" +
                "</wml>";

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
             new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt", null});

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

        assertEquals("Output HTML incorrectly formatted.",
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.3//EN\" \"http://www.wapforum.org/DTD/wml13.dtd\">" +
                "<wml>" +
                    "<card title=\"Page Title\">" +
                        "<p>" +
                            "<table columns=\"3\">" +
                                "<tr>" +
                                    "<td><big><small>Heading 1</small></big></td>" +
                                    "<td><big><small>Heading 2</small></big></td>" +
                                    "<td><big><small>Heading 3</small></big></td>" +
                                "</tr>" +
                                "<tr>" +
                                    "<td><small>Data 1</small></td>" +
                                    "<td><small>Data 2</small></td>" +
                                    "<td><small>Data 3</small></td>" +
                                "</tr>" +
                            "</table>" +
                        "</p>" +
                    "</card>" +
                "</wml>",
                     writer.toString());
    }

    /**
     * Test emulation of overall paragraph whitespace.
     * <p>
     * This tests that:
     *  - CSS white-space generates mode=wrap|nowrap attribute
     *  - paragraphs with equal styles are merged using <br>
     *  - blocks containing trailing brs are removed
     */
    public void testParagraphWhitespace()
        throws RepositoryException, PAPIException, IOException,
        PackagingException {

        open("canvas",
             new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt", null});

        open("pane", null, "pane");

        open("p");
        writeMessage("1: none");
        open("br");
        close("br");
        close("p");

        open("p", "wrap");
        writeMessage("2: wrap");
        open("br");
        close("br");
        close("p");

        open("p");
        writeMessage("3: none");
        open("br");
        close("br");
        close("p");

        open("p", "nowrap");
        writeMessage("4: nowrap");
        open("br");
        close("br");
        close("p");

        open("p");
        writeMessage("5: none");
        open("br");
        close("br");
        close("p");

        open("p");
        writeMessage("6: none");
        open("br");
        close("br");
        close("p");

        close("pane");

        close("canvas");

        assertEquals("Output WML incorrectly formatted.",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
            "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.3//EN\" "+
                           "\"http://www.wapforum.org/DTD/wml13.dtd\">"+
            "<wml>"+
              "<card title=\"Page Title\">"+
                "<p>" +
                    "1: none" +
                    "<br/>" +
                    "2: wrap" +
                    "<br/>" +
                    "3: none" +
                "</p>"+
                "<p mode=\"nowrap\">" +
                    "4: nowrap" +
                "</p>"+
                "<p mode=\"wrap\">" +
                    "5: none" +
                     "<br/>" +
                    "6: none" +
                "</p>"+
              "</card>"+
            "</wml>",
            writer.toString());

    }

    /**
     * Test emulation of paragraph alignment.
     * <p>
     * This tests that CSS text-align generates align= attribute.
     */
    public void testParagraphAlign()
        throws RepositoryException, PAPIException, IOException,
        PackagingException {

        open("canvas",
             new String[]{null, "Page Title", "/theme.mthm", "/layout.mlyt", null});

        open("pane", null, "pane");

        open("p");
        writeMessage("none");
        close("p");

        open("p", "left");
        writeMessage("left");
        close("p");

        open("p", "center");
        writeMessage("center");
        close("p");

        open("p", "right");
        writeMessage("right");
        close("p");

        close("pane");

        close("canvas");

        assertEquals("Output WML incorrectly formatted.",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
            "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.3//EN\" "+
                           "\"http://www.wapforum.org/DTD/wml13.dtd\">"+
            "<wml>"+
              "<card title=\"Page Title\">"+
                "<p>" +
                    "none"+
                    "<br/>" + 
                    "left" +
                "</p>"+
                "<p align=\"center\">center</p>"+
                "<p align=\"right\">right</p>"+
              "</card>"+
            "</wml>",
            writer.toString());

    }


}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10779/1	geoff	VBM:2005121202 MCS35: WML vertical whitespace fix does not handle mode settings (take 2)

 17-Nov-05	10330/2	pabbott	VBM:2005110907 Honour align with mode=nospace

 15-Nov-05	10326/5	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 03-Oct-05	9522/2	ibush	VBM:2005091502 no_save on images

 29-Sep-05	9600/3	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9184/4	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 ===========================================================================
*/
