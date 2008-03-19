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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLTransitionalTestCase.java,v 1.6 2003/02/06 11:38:48 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Sep-02    Steve           VBM:2002040809 - Unit test for XHTMLTransitional.
 *                              Added testOpenPane() to open a pane complete
 *                              with table output into a dom then test the
 *                              generated elements and attributes.
 * 14-Oct-02    Geoff           VBM:2002100905 - Modified to use new DOMPool
 *                              constructor.
 * 17-Oct-02    Sumit           VBM:2002070803 - Added MyMarinerRequestContext
 *                              class and overrode getRequestContext() and
 *                              allocateRSB()
 * 11-Nov-02    Phil W-S        VBM:2002102306 - Updated to work with the new
 *                              mariner element style optimizations.
 * 16-Dec-02    Adrian          VBM:2002100203 - Updated references to
 *                              supportsMultipleStyleClasses to
 *                              supportsMultipleSelectorClasses
 * 06-Feb-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.SegmentGridAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.styling.StylesBuilder;

import java.io.IOException;

public class XHTMLTransitionalTestCase extends XHTMLFullTestCase {

    private Element element;
    private MCSAttributes attribute;

    private static String elementName = "myElementName";

    private XHTMLTransitional protocol;
    private XHTMLBasicTestable testable;

    /** Creates new TestDOMElement */
    public XHTMLTransitionalTestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLTransitionalFactory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (XHTMLTransitional) protocol;
        this.testable = (XHTMLBasicTestable) testable;
    }

    /**
     * Setup the unit test framework by creating some classes ensuring that each
     * test case starts off with a clean slate to work from
     */
    private void privateSetUp() {
        TestMarinerPageContext context = new TestMarinerPageContext();
        TestMarinerRequestContext requestContext =
                new TestMarinerRequestContext();
        ContextInternals.setMarinerPageContext(requestContext, context);
        context.pushRequestContext(requestContext);

        DeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        canvasLayout = new CanvasLayout();

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout1 =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        RuntimeDeviceLayout runtimeDeviceLayout =
                runtimeDeviceLayout1;
        deviceContext.setDeviceLayout(runtimeDeviceLayout);

        resetElement();

        attribute = new StyleAttributes();
        attribute.setStyles(StylesBuilder.getDeprecatedStyles());
        attribute.setTitle("My title");

        context.pushDeviceLayoutContext(deviceContext);
        protocol.setMarinerPageContext(context);

        int idx[] = {1, 0};
        NDimensionalIndex index = new NDimensionalIndex(idx);
        PaneInstance paneInstance = new PaneInstance(index);

        paneInstance.setStyleClass("fred");
        context.setFormatInstance(paneInstance);
        context.setDeviceName(NETSCAPE4_DEVICE_NAME);
        PageHead head = new PageHead();
        testable.setPageHead(head);
    }

    private void resetElement() {
        element = domFactory.createElement();
        element.setName(elementName);
    }

    /**
     * test open pane
     *
     * todo XDIME-CP: Fix when done layout transformation.
     */
    public void notestOpenPane() throws RepositoryException {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        Element el = null;

        Pane pane = null;
        PaneAttributes attributes = null;

        //=============================================================
        // Styles defined = No
        //=============================================================
        pane = new Pane(null);
        attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        attributes.setPane(pane);

        protocol.openPane(buffer, attributes);

        assertSame("The buffer current element should be the root element " +
                   "but was" + buffer.getCurrentElement(),
                   buffer.getRoot(),
                   buffer.getCurrentElement());

        //=============================================================
        // Styles defined = Yes
        //@todo XDIME-CP pass in Styles and check they're correctly propagated through
        //=============================================================
        pane = new Pane(null);

        attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
//        attributes.setStyleClass("fred");
//        attributes.setBackgroundColour("#ff0000");
//        attributes.setBorderWidth("1");
//        attributes.setCellPadding("2");
//        attributes.setCellSpacing("3");
        attributes.setPane(pane);

        protocol.openPane(buffer, attributes);
        try {
            // A div will have been opened to render styles because:
            // 1. This protocol supports stylesheets.
            // 2. The pane has a style class "fred".
            // 3. Any of border width/cell padding/cell spacing have non-zero
            //    values.
            // The div will have an appropriate class attribute.
            el = buffer.closeElement("div");
//            assertEquals("Invalid class attribute on div",
//                    "VE-pane-fred", el.getAttributeValue("class"));

            el = buffer.closeElement("td");

            assertNull("bgcolor on td", el.getAttributeValue("bgcolor"));
            assertNull("border on td", el.getAttributeValue("border"));
            assertNull("cellspacing on td", el.getAttributeValue("cellspacing"));
            assertNull("cellpadding on td", el.getAttributeValue("cellpadding"));
            el = buffer.closeElement("tr");
            assertNull("Class attribute on tr", el.getAttributeValue("class"));
            assertNull("bgcolor on tr", el.getAttributeValue("bgcolor"));
            assertNull("border on tr", el.getAttributeValue("border"));
            assertNull("cellspacing on tr", el.getAttributeValue("cellspacing"));
            assertNull("cellpadding on tr", el.getAttributeValue("cellpadding"));
            el = buffer.closeElement("table");
//            assertNull("Invalid class attribute on table",
//                    el.getAttributeValue("class"));
            assertEquals("Invalid bgcolor attribute",
                    "#ff0000", el.getAttributeValue("bgcolor"));
            assertEquals("Invalid border attribute",
                    "1", el.getAttributeValue("border"));
            assertEquals("Invalid cellspacing attribute",
                    "3", el.getAttributeValue("cellspacing"));
            assertEquals("Invalid cellpadding attribute",
                    "2", el.getAttributeValue("cellpadding"));
        } catch (IllegalStateException ise) {
            fail("Error checking element <" + el.getName() + ">");
        }
    }

    /**
     * Test for doProtocolString
     */
    public void testDoProtocolString() throws IOException, RepositoryException {
        // @todo implement this test.
    }

    /**
     * Test the method doImage
     */
    public void testDoImage() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Needed to allow the call to getTextFromReference within doImage to work
        context = new TestMarinerPageContext();

        protocol.setMarinerPageContext(context);

        ImageAttributes attrs = new ImageAttributes();
        attrs.setStyles(StylesBuilder.getInitialValueStyles());
        attrs.setSrc("http://www.volantis.com/my_image.jpg");
        attrs.setLocalSrc(true);
        attrs.setAltText("my_alt_text");
        attrs.setWidth("10");
        attrs.setHeight("20");
        attrs.setBorder("5");

        protocol.doImage(buffer, attrs);

        String expected = "<img alt=\"my_alt_text\" border=\"5\" " +
                "height=\"20\" src=\"http://www.volantis.com/my_image.jpg\" " +
                "width=\"10\"/>";

        assertEquals("Unexpected img markup generated.",
                     expected,
                     bufferToString(buffer));
    }

    // Javadoc inherited.
    protected String getExpectedEnclosingTableCellMarkup() {
        return "<td bgcolor=\"red\"/>";
    }

    // Javadoc inherited.
    protected String getExpectedHorizontalRuleMarkup() {
        return "<hr align=\"left\" noshade=\"true\" size=\"5\" width=\"100\"/>";
    }

    /**
     * Test the behaviour of OpenSegmentGrid. In particular test the way it
     * handles null values for "rows" and "cols" attributes and negative values.
     *
     * @throws Exception
     */
    public void testOpenSegmentGrid() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        int[] rows = null;
        int[] cols = null;


        SegmentGridAttributes ga = new SegmentGridAttributes();
        // set to null
        ga.setColumnWidths(cols);
        ga.setRowHeights(rows);

        protocol.openSegmentGrid(buffer, ga);

        // test to ensure that null columnWidth and rowHeights produce NO
        // args in the resulting HTML
        String expected = "<frameset border=\"0\" frameborder=\"no\" " +
                "framespacing=\"0\"/>";
        assertEquals("Ensure that null column and row widths do not produce " +
                "attributes", expected, bufferToString(buffer));

        // test to ensure that -1 for row or column attribute will be replaced
        // by "*" (indicating default value for the browser to decide how to
        // render
        expected = "<frameset border=\"0\" frameborder=\"no\" " +
                "framespacing=\"0\" rows=\"*\"/>";
        rows = new int[1];
        rows[0] = -1;
        ga.setRowHeights(rows);
        buffer.clear();
        protocol.openSegmentGrid(buffer, ga);
        assertEquals("Ensure that -1 is replaced by * (rows)", expected,
                bufferToString(buffer));

        // same test as above but for columns
        expected = "<frameset border=\"0\" cols=\"*\" frameborder=\"no\" " +
                "framespacing=\"0\"/>";
        ga.setRowHeights(null);
        ga.setColumnWidths(rows);
        buffer.clear();
        protocol.openSegmentGrid(buffer, ga);
        assertEquals("Ensure -1 is replaced by * (cols)", expected,
                bufferToString(buffer));

        // general test to check substitution of negative values for "*"
        expected = "<frameset border=\"0\" cols=\"*,4,*\" frameborder=\"no\" " +
                "framespacing=\"0\" rows=\"3,*,3\"/>";
        rows = new int[]{3, -100, 3};
        cols = new int[]{-1, 4, -1};
        ga.setRowHeights(rows);
        ga.setColumnWidths(cols);
        buffer.clear();
        protocol.openSegmentGrid(buffer, ga);
        assertEquals("Ensure that general substitution occurs correctly",
                expected, bufferToString(buffer));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/5	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 22-Apr-05	7791/1	philws	VBM:2005040113 Port openPane changes from 3.3

 22-Apr-05	7746/1	philws	VBM:2005040113 Correct pane rendering where width and/or alignment are specified

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 27-Sep-04	5661/1	tom	VBM:2004091403 Added stylesheet support to iMode and fixed bgcol in cells

 27-Sep-04	5581/3	tom	VBM:2004091403 Introduced stylesheet emulation for font and repaired background

 09-Sep-04	4839/15	pcameron	VBM:2004062801 div tag is now used again in a pane's table if stylesheets are in use

 29-Jun-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 05-May-04	4157/7	matthew	VBM:2003030319 added test implementation to HDML_Version3TestCase.testOpenSegmentGrid, changed signature of XHTMLTransitional.addRowColAttributes

 05-May-04	4157/5	matthew	VBM:2003030319 change the way default values of rowHeight and columnHeight attributes are handled (insert a bin bogus-hypersonic-db.properties bogus-hypersonic-db.script build build-ab.xml build-admin.xml build-charset.xml build-clean_war.xml build-cli.xml build-code-generators.xml build-common.xml build-controls.xml build-core.xml build-deploy.xml build-docs.xml build-dynamo.xml build-eclipse-common.xml build-eclipse-updateclient.xml build-eclipse.xml build-examples.xml build-external-plugins.xml build-i18n.xml build-librarian-generator.xml build-librarian.xml build-migrate30.xml build-properties.xml build-release.xml build-samples.xml build-servlet.xml build-targets.xml build-testsuite.xml build-tests.xml build-testtools.xml build-tomcat.xml build-tt_gui.xml build-tt_war.xml build-uaprof.xml build-ucp.xml build-update-client-cli.xml build-update-deploy.xml build-update.xml build-validation.xml build-version.properties build-vignette.xml build-weblogic.xml build-websphere.xml build.xml client.cer client.keystore com db doc hypersonic-db.data hypersonic-db.properties hypersonic-db.script jar javadoc key librarian-lookup-table.xml librarian.xml mcs.ipr mcs.iws product.key product.lkd redist report.txt Test testdata tests TESTS-TestSuites.xml testsuite volantis webapp rather then -1)

 05-May-04	4157/3	matthew	VBM:2003030319 change the way default values of rowHeight and columnHeight attributes are handled (insert a bin build-ab.xml build-admin.xml build-charset.xml build-clean_war.xml build-cli.xml build-code-generators.xml build-common.xml build-controls.xml build-core.xml build-deploy.xml build-docs.xml build-dynamo.xml build-eclipse-common.xml build-eclipse-updateclient.xml build-eclipse.xml build-examples.xml build-external-plugins.xml build-i18n.xml build-librarian-generator.xml build-librarian.xml build-migrate30.xml build-properties.xml build-release.xml build-samples.xml build-servlet.xml build-targets.xml build-testsuite.xml build-tests.xml build-testtools.xml build-tomcat.xml build-tt_gui.xml build-tt_war.xml build-uaprof.xml build-ucp.xml build-update-client-cli.xml build-update-deploy.xml build-update.xml build-validation.xml build-version.properties build-vignette.xml build-weblogic.xml build-websphere.xml build.xml client.cer client.keystore com db doc jar javadoc key librarian-lookup-table.xml librarian.xml mcs.ipr mcs.iws product.key product.lkd redist report.txt Test testdata tests testsuite volantis webapp rather then -1)

 05-May-04	4157/1	matthew	VBM:2003030319 change the way default values of rowHeight and columnHeight attributes are handled (insert a bin build build-ab.xml build-admin.xml build-charset.xml build-clean_war.xml build-cli.xml build-code-generators.xml build-common.xml build-controls.xml build-core.xml build-deploy.xml build-docs.xml build-dynamo.xml build-eclipse-common.xml build-eclipse-updateclient.xml build-eclipse.xml build-examples.xml build-external-plugins.xml build-i18n.xml build-librarian-generator.xml build-librarian.xml build-migrate30.xml build-properties.xml build-release.xml build-samples.xml build-servlet.xml build-targets.xml build-testsuite.xml build-tests.xml build-testtools.xml build-tomcat.xml build-tt_gui.xml build-tt_war.xml build-uaprof.xml build-ucp.xml build-update-client-cli.xml build-update-deploy.xml build-update.xml build-validation.xml build-version.properties build-vignette.xml build-weblogic.xml build-websphere.xml build.xml client.cer client.keystore com db doc jar javadoc key librarian-lookup-table.xml librarian.xml mcs.ipr mcs.iws product.key product.lkd redist report.txt Test testdata tests testsuite volantis webapp rather then -1)

 03-Nov-03	1760/1	philws	VBM:2003031710 Port image alt text component reference handling from PROTEUS

 02-Sep-03	1305/1	adrian	VBM:2003082108 added new openwave6 xhtml protocol

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 14-Aug-03	1063/1	geoff	VBM:2003081305 Port 2003060909 forward

 24-Jul-03	728/6	adrian	VBM:2003052001 fixedup testcases - removed suite and main methods

 07-Jul-03	728/4	adrian	VBM:2003052001 fixed pane attribute generation

 07-Jul-03	728/2	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	706/1	allan	VBM:2003070302 Added TestSuiteGenerator ant task. Run testsuite in a single jvm

 ===========================================================================
*/
