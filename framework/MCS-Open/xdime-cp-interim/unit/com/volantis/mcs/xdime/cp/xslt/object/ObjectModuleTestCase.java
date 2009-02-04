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

package com.volantis.mcs.xdime.cp.xslt.object;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the object-module.xsl stylesheet.
 */
public class ObjectModuleTestCase extends XSLTTestAbstract {

    /**
     * The location of the schema file for these unit tests.
     */
    private static final String SCHEMA_FILE_LOCATION =
            "../../../../../../../../../architecture/built/architecture/xml-schema/xdime-cp/src/xdime-cp-mcs.xsd";

    /**
     * Tests that the object elements are transformed according to
     * AN062 section 3.8.
     */
    public void testValidExtensions() throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object class=\"ignored-class\" src=\"http://me/image.bmp\">bmp image</object>" +
                "<object class=\"ignored-class\" src=\"http://me/image.gif\">gif image</object>" +
                "<object class=\"object-class\" src=\"http://me/image.mimg\">mimg image</object>" +
                "<object href=\"obj.html\" src=\"http://me/image.mrsc\">mrsc image</object>" +
                "<object href=\"mylink.html\" src=\"http://me/image.my.mrsc\">linked mrsc image</object>" +
                "<object class=\"object-class\" src=\"http://me/wilma.blah.gif\">" +
                "<caption>A lovely picture of Wilma Flintstone</caption>" +
                "Wilma Flintstone</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" + "" +
                "<img pane=\"object-pane\" class=\"ignored-class\" urlc=\"http://me/image.bmp\" alt=\"bmp image\" />" +
                "<img pane=\"object-pane\" class=\"ignored-class\" urlc=\"http://me/image.gif\" alt=\"gif image\" />" +
                "<img pane=\"object-pane\" class=\"object-class\" src=\"http://me/image.mimg\" alt=\"mimg image\" />" +
                "<a href=\"obj.html\">" +
                "<img pane=\"object-pane\" src=\"http://me/image-mrsc.mimg\" alt=\"mrsc image\" />" +
                "</a>" +
                "<a href=\"mylink.html\">" +
                "<img pane=\"object-pane\" src=\"http://me/image.my-mrsc.mimg\" alt=\"linked mrsc image\" />" +
                "</a>" +
                "<img pane=\"object-pane\" class=\"object-class\" urlc=\"http://me/wilma.blah.gif\" alt=\"Wilma Flintstone\" />" +
                "<div>A lovely picture of Wilma Flintstone</div>" +
                "</unit>"));
    }

    /**
     * Tests that a specified aspect ratio and height are added correctly as
     * the only parameter of a urlc value.
     */
    public void testAspectRatioWidthAndHeightFirstParameter()
            throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object src=\"http://me/image.gif\">gif image" +
                "<param name=\"mcs-aspect-ratio-width\" value=\"16\"/>" +
                "<param name=\"mcs-aspect-ratio-height\" value=\"9\"/>" +
                "</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" + "" +
                "<img pane=\"object-pane\" urlc=\"http://me/image.gif?mcs.ar=16:9\" alt=\"gif image\" />" +
                "</unit>"));
    }

    /**
     * Tests that a specified aspect ratio and height are added correctly as
     * an additional parameter at the end of a urlc value.
     */
    public void testAspectRatioWidthAndHeightAdditionalParameter()
            throws Exception {
        doTransform("Object element transformation failed with " +
                "parameter list",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object " +
                "src=\"http://live.tf-nextmedia.de/framework/ir?image=/amicaneo/shared/" +
                "img/logo_fs24_01.jpg&amp;x=95&amp;y=-1&amp;mime=jpg\">" +
                "<param name=\"mcs-aspect-ratio-width\" value=\"4\"/>" +
                "hello" +
                "<param name=\"mcs-aspect-ratio-height\" value=\"3\"/>" +
                "</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" urlc=\"http://live.tf-nextmedia.de/framework/ir?image=/amicaneo/shared/" +
                "img/logo_fs24_01.jpg&amp;x=95&amp;y=-1&amp;mime=jpg&amp;mcs.ar=4:3\" alt=\"hello\"/>" +
                "</unit>"));
    }


    /**
     * Tests that a specified aspect ratio and height are added correctly as
     * the only parameter of a urlc value.
     */
    public void testAspectRatioHeightOnlyFirstParameter()
            throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object src=\"http://me/image.gif\">gif image" +
                "<param name=\"mcs-aspect-ratio-height\" value=\"5\"/>" +
                "</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" + "" +
                "<img pane=\"object-pane\" urlc=\"http://me/image.gif?mcs.ar=5:5\" alt=\"gif image\" />" +
                "</unit>"));
    }

    /**
     * Tests that a specified aspect ratio and height are added correctly as
     * the first parameter of a urlc value.
     */
    public void testAspectRatioWidthOnlyFirstParameter()
            throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object src=\"http://me/image.gif\">gif image" +
                "<param name=\"mcs-aspect-ratio-width\" value=\"8\"/>" +
                "</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" + "" +
                "<img pane=\"object-pane\" urlc=\"http://me/image.gif?mcs.ar=8:8\" alt=\"gif image\" />" +
                "</unit>"));
    }


    /**
     * Tests that an object with a parameter list on the value of its src
     * attribute (see AN062 section 3.8) passes through the attribute's value.
     */
    public void testParameterList() throws Exception {
        doTransform("Object element transformation failed with " +
                "parameter list",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object " +
                "src=\"http://live.tf-nextmedia.de/framework/ir?image=/amicaneo/shared/" +
                "img/logo_fs24_01.jpg&amp;x=95&amp;y=-1&amp;mime=jpg\">hello</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" urlc=\"http://live.tf-nextmedia.de/framework/ir?image=/amicaneo/shared/" +
                "img/logo_fs24_01.jpg&amp;x=95&amp;y=-1&amp;mime=jpg\" alt=\"hello\"/>" +
                "</unit>"));
    }

    /**
     * Tests that an object with an invalid extension on the value of its src
     * attribute (see AN062 section 3.8) passes the value through.
     */
    public void testInValidExtension() throws Exception {
        doTransform("Object element transformation succeeded with " +
                "invalid extension",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object " +
                "src=\"http://live.tf-nextmedia.de/framework/blah.invalid\">hello</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" urlc=\"http://live.tf-nextmedia.de/framework/blah.invalid\" alt=\"hello\"/>" +
                "</unit>"));
    }

    /**
     * Tests that an object with no extension on the value of its src
     * attribute (see AN062 section 3.8) passes the value through.
     */
    public void testNoExtension() throws Exception {
        doTransform("Object element transformation succeeded with " +
                "invalid extension",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object " +
                "src=\"http://live.tf-nextmedia.de/framework/blah\">hello</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" urlc=\"http://live.tf-nextmedia.de/framework/blah\" alt=\"hello\"/>" +
                "</unit>"));
    }

    /**
     * Tests that absolute URL resolution works as expected.
     */
    public void testAbsoluteURLResolution() throws Exception {
        doTransform("Absolute URL resolution failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<?xml version=\"1.0\" encoding=\"ISO-8859\"?>" +
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.CFG_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION + "\" " +
                "xml:base=\"http://www.volantis.com/peterca/somefile.xml\">" +
                "<object src=\"http://java.sun.com/docs/cafe.jpg\">hello</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" urlc=\"http://java.sun.com/docs/cafe.jpg\" alt=\"hello\" />" +
                "</unit>"));
    }

    /**
     * Tests that host relative URL resolution works as expected.
     */
    public void testHostRelativeURLResolution() throws Exception {
        doTransform("Host relative URL resolution failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<?xml version=\"1.0\" encoding=\"ISO-8859\"?>" +
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.CFG_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION + "\" " +
                "xml:base=\"http://www.volantis.com/peterca/somefile.xml\">" +
                "<object src=\"/java/docs/../cafe.jpg\">hello</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" urlc=\"http://www.volantis.com/java/docs/../cafe.jpg\" alt=\"hello\" />" +
                "</unit>"));
    }

    /**
     * Tests that document relative URL resolution with a prefix match works as
     * expected.
     */
    public void testDocumentRelativeURLResolutionWithPrefixMatch()
            throws Exception {
        doTransform("Document relative URL resolution with prefix match failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<?xml version=\"1.0\" encoding=\"ISO-8859\"?>" +
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.CFG_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION + "\" " +
                "xml:base=\"http://www.volantis.com/peterca/somefile.xml\">" +
                "<object src=\"./here/../././it/is/./maybe/.././.././../was//mypic.mimg\">hello</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\"  src=\"/was/mypic.mimg\" alt=\"hello\" />" +
                "</unit>"));
    }

    /**
     * Tests that document relative URL resolution without a prefix match works
     * as expected.
     */
    public void testDocumentRelativeURLResolutionWithNoPrefixMatch() throws Exception {
        doTransform("Document relative URL resolution with no prefix match failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<?xml version=\"1.0\" encoding=\"ISO-8859\"?>" +
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.CFG_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION + "\" " +
                "xml:base=\"http://www.volantis.com/peterca999/somefile.xml\">" +
                "<object src=\"./here/../././it/is/./maybe/.././.././../was//mypic.mimg\">hello</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" src=\"http://www.volantis.com/peterca999/was/mypic.mimg\" alt=\"hello\" />" +
                "</unit>"));
    }

    /**
     * Tests that the absence of an mcs-transcode parameter results in
     * "business as usual" i.e. a urlc attribute.
     */
    public void testTranscodingNoMcsTranscodeAttribute() throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<?xml version=\"1.0\" encoding=\"ISO-8859\"?>" +
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.CFG_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION + "\" " +
                "xml:base=\"http://www.volantis.com/somefile.xml\">" +
                "<object src=\"blah1.gif\">some text</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" urlc=\"http://www.volantis.com/blah1.gif\" alt=\"some text\" />" +
                "</unit>"));
    }

    /**
     * Tests that the presence of an mcs-transcode parameter with value true
     * results in "business as usual" i.e. a urlc attribute.
     */
    public void testTranscodingMcsTranscodeAttributeTrue() throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<?xml version=\"1.0\" encoding=\"ISO-8859\"?>" +
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.CFG_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION + "\" " +
                "xml:base=\"http://www.volantis.com/somefile.xml\">" +
                "<object src=\"blah1.gif\">some text" +
                "<param name=\"mcs-transcode\" value=\"true\"/>" +
                "</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" urlc=\"http://www.volantis.com/blah1.gif\" alt=\"some text\" />" +
                "</unit>"));
    }

    /**
     * Tests that the presence of an mcs-transcode parameter with value false
     * results in a url attribute rather than a urlc attribute.
     */
    public void testTranscodingMcsTranscodeFalse() throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<?xml version=\"1.0\" encoding=\"ISO-8859\"?>" +
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION + "\" " +
                "xml:base=\"http://www.volantis.com/somefile.xml\">" +
                "<object src=\"blah1.gif\">some text" +
                "<param name=\"mcs-transcode\" value=\"false\"/>" +
                "</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" url=\"http://www.volantis.com/blah1.gif\" alt=\"some text\" />" +
                "</unit>"));
    }

    /**
     * Tests that the presence of an mcs-transcode parameter with value rubbish
     * results in business as usual i.e. a urlc attribute.
     */
    public void testTranscodingMcsTranscodeRubbishValue() throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<?xml version=\"1.0\" encoding=\"ISO-8859\"?>" +
                "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION + "\" " +
                "xml:base=\"http://www.volantis.com/somefile.xml\">" +
                "<object src=\"blah1.gif\">some text" +
                "<param name=\"mcs-transcode\" value=\"rubbish\"/>" +
                "</object>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" +
                "<img pane=\"object-pane\" urlc=\"http://www.volantis.com/blah1.gif\" alt=\"some text\" />" +
                "</unit>"));
    }

    /**
     * Tests that image metadata is transformed with no interference from
     * the mcs-transcode parameter with value false.
     */
    public void testMetadataExtensionsWithMcsTranscodeFalse()
            throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object class=\"object-class\" src=\"http://me/image.mimg\">mimg image" +
                "<param name=\"mcs-transcode\" value=\"false\"/>" +
                "</object>" +
                "<object href=\"obj.html\" src=\"http://me/image.mrsc\">mrsc image" +
                "<param name=\"mcs-transcode\" value=\"false\"/>" +
                "</object>" +
                "<object href=\"mylink.html\" src=\"http://me/image.my.mrsc\">linked mrsc image" +
                "<param name=\"mcs-transcode\" value=\"false\"/>" +
                "</object>" +
                "<caption>A lovely picture of Wilma Flintstone</caption>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" + "" +
                "<img pane=\"object-pane\" class=\"object-class\" src=\"http://me/image.mimg\" alt=\"mimg image\" />" +
                "<a href=\"obj.html\">" +
                "<img pane=\"object-pane\" src=\"http://me/image-mrsc.mimg\" alt=\"mrsc image\" />" +
                "</a>" +
                "<a href=\"mylink.html\">" +
                "<img pane=\"object-pane\" src=\"http://me/image.my-mrsc.mimg\" alt=\"linked mrsc image\" />" +
                "</a>" +
                "<div>A lovely picture of Wilma Flintstone</div>" +
                "</unit>"));
    }

    /**
     * Tests that image metadata is transformed with no interference from
     * the mcs-transcode parameter with value true.
     */
    public void testMetadataExtensionsWithMcsTranscodeTrue()
            throws Exception {
        doTransform("Object element transformations failed",
                getInputSourceForClassResource("object-module-test.xsl"),
                getInputSourceForString(
                        getSystemIdForClassResource("object-module-test.xsl"),
                        "<mcs:unit " + XDIMECPConstants.XDIMECP_MCS_XMLNS +
                " " + XDIMECPConstants.XHTML2_DEFAULT_XMLNS +
                " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
                " xsi:schemaLocation=\"" +
                XDIMECPConstants.XDIMECP_MCS_NAMESPACE + " " +
                SCHEMA_FILE_LOCATION +
                "\">" +
                "<object class=\"object-class\" src=\"http://me/image.mimg\">mimg image" +
                "<param name=\"mcs-transcode\" value=\"true\"/>" +
                "</object>" +
                "<object href=\"obj.html\" src=\"http://me/image.mrsc\">mrsc image" +
                "<param name=\"mcs-transcode\" value=\"true\"/>" +
                "</object>" +
                "<object href=\"mylink.html\" src=\"http://me/image.my.mrsc\">linked mrsc image" +
                "<param name=\"mcs-transcode\" value=\"true\"/>" +
                "</object>" +
                "<caption>A lovely picture of Wilma Flintstone</caption>" +
                "</mcs:unit>"),
                getInputSourceForString(
                        "<unit>" + "" +
                "<img pane=\"object-pane\" class=\"object-class\" src=\"http://me/image.mimg\" alt=\"mimg image\" />" +
                "<a href=\"obj.html\">" +
                "<img pane=\"object-pane\" src=\"http://me/image-mrsc.mimg\" alt=\"mrsc image\" />" +
                "</a>" +
                "<a href=\"mylink.html\">" +
                "<img pane=\"object-pane\" src=\"http://me/image.my-mrsc.mimg\" alt=\"linked mrsc image\" />" +
                "</a>" +
                "<div>A lovely picture of Wilma Flintstone</div>" +
                "</unit>"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Jul-05	8813/5	pcameron	VBM:2005061608 Added aspect ratio parameter processing to XDIME-CP

 16-Jun-05	8796/9	pcameron	VBM:2005061504 Added mcs-transcode parameter to XDIME CP

 08-Sep-04	5447/3	pcameron	VBM:2004090711 object src attribute supports servlets

 02-Sep-04	5381/5	pcameron	VBM:2004082607 URL parameters are ignored when finding image extensions

 21-Jun-04	4645/23	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/7	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/5	pcameron	VBM:2004060306 Fixed test cases after integration

 11-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 10-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
*/
