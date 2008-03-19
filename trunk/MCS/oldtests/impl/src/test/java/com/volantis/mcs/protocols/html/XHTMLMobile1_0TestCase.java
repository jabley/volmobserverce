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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.css.version.CSSPropertyMock;
import com.volantis.mcs.css.version.CSSVersionMock;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.DOMFactoryMock;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.protocols.BigAttributes;
import com.volantis.mcs.protocols.BoldAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.ItalicAttributes;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.ProtocolSupportFactoryMock;
import com.volantis.mcs.protocols.SmallAttributes;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.ValidationHelperMock;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.StylesBuilder;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class tests the XHTMLMobile1_0TestCase protocol
 */
public class XHTMLMobile1_0TestCase extends XHTMLBasicTestCase {

    private XHTMLMobile1_0 protocol;
    private XHTMLBasicTestable testable;

    /**
     * Create a new instance of XHTMLMobile1_0
     * @param name
     */
    public XHTMLMobile1_0TestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLMobile1_0Factory(),
                internalDevice);
        return protocol;
    }

    // javadoc inherited
    protected void setTestableProtocol(VolantisProtocol protocol,
                                       VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (XHTMLMobile1_0) protocol;
        this.testable = (XHTMLBasicTestable) testable;
    }

    /**
     * Setup the unit test framework by creating some classes ensuring that each
     * test case starts off with a clean slate to work from
     *
     * @todo the fact that some of the tests don't need to call this method
     * at all indicates that we ought to refactor this so each test just
     * sets up what it needs. Otherwise, we can have unintended side effects...
     */
    private void privateSetUp() {
        canvasLayout = new CanvasLayout();
        pane = new Pane(canvasLayout);
        pane.setName(PANE_NAME);
        pane.setInstance(0);

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        context.setDeviceLayout(runtimeDeviceLayout);

        DeviceLayoutContext deviceContext = new DeviceLayoutContext();
        deviceContext.setDeviceLayout(runtimeDeviceLayout);
        context.pushDeviceLayoutContext(deviceContext);

        ContextInternals.setMarinerPageContext(requestContext, context);
        protocol.setMarinerPageContext(context);

        int idx[] = {1, 0};
        NDimensionalIndex index = new NDimensionalIndex(idx);
        PaneInstance paneInstance = new PaneInstance(index);

        paneInstance.setStyleClass("fred");
        context.setFormatInstance(paneInstance);

        PageHead head = new PageHead();
        testable.setPageHead(head);
        testable.setStyleSheetRenderer(CSSStyleSheetRenderer.getSingleton());
        protocol.setMarinerPageContext(context);
        context.setDeviceName(DEVICE_NAME);
    }

    /**
     * Test for doProtocolString
     */
    public void testDoProtocolString() throws IOException {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<!DOCTYPE html PUBLIC " +
                "\"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\" " +
                "\"http://www.wapforum.org/DTD/xhtml-mobile10.dtd\">";

        checkDoProtocolString(protocol, expected);
    }

    // Javadoc inherited.
    protected String getExpectedHorizontalRuleMarkup() {
        return "<hr/>";
    }

    /**
     * Test the italic markup
     * @throws Exception
     */
    public void testItalic() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        protocol.openItalic(buffer, new ItalicAttributes());

        buffer.appendEncoded("Test");
        protocol.closeItalic(buffer, new ItalicAttributes());
        String expected = "<i>Test</i>";
        assertEquals("Protocol string should match", expected,
                        bufferToString(buffer));

    }

    /**
     * Test the bold markup
     * @throws Exception
     */
    public void testBold() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        protocol.openBold(buffer, new BoldAttributes());

        buffer.appendEncoded("Test");
        protocol.closeBold(buffer, new BoldAttributes());
        String expected = "<b>Test</b>";
        assertEquals("Protocol string should match", expected,
                        bufferToString(buffer));

    }

    /**
     * Test the big markup
     * @throws Exception
     */
    public void testBig() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        protocol.openBig(buffer, new BigAttributes());

        buffer.appendEncoded("Test");
        protocol.closeBig(buffer, new BigAttributes());
        String expected = "<big>Test</big>";
        assertEquals("Protocol string should match", expected,
                        bufferToString(buffer));

    }

    /**
     * Test the small markup
     * @throws Exception
     */
    public void testSmall() throws Exception {
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        protocol.openSmall(buffer, new SmallAttributes());

        buffer.appendEncoded("Test");
        protocol.closeSmall(buffer, new SmallAttributes());
        String expected = "<small>Test</small>";
        assertEquals("Protocol string should match", expected,
                        bufferToString(buffer));

    }
    
    private ProtocolSupportFactoryMock createProtocolSupportFactoryMock(){
        ProtocolSupportFactoryMock protocolSupportFactoryMock =
            new ProtocolSupportFactoryMock("psfMock", expectations);
        
        DOMFactoryMock domFactoryMock =
            new DOMFactoryMock("domFactoryMock", expectations);
        
        protocolSupportFactoryMock.expects.getDOMFactory().
            returns(domFactoryMock);
        
        return protocolSupportFactoryMock;
    }
    
    private ProtocolConfigurationMock createProtocolConfigurationMock(){
        final CSSVersionMock cssVersionMock = new CSSVersionMock(expectations);
        
        cssVersionMock.expects.
            getProperty(StylePropertyDetails.MCS_INPUT_FORMAT).
            returns(new CSSPropertyMock(expectations));
        
        ProtocolConfigurationMock protocolConfigMock =
            new ProtocolConfigurationMock("protocolConfigMock",
                    expectations);
        
        protocolConfigMock.expects.getValidationHelper().
            returns(new ValidationHelperMock(expectations));
        
        protocolConfigMock.expects.getCssVersion().returns(cssVersionMock);
        
        return protocolConfigMock;
    }

    // javadoc inherited
    protected String expectedDefaultMimeType() {
        return "application/vnd.wap.xhtml+xml";
    }

    /**
     * Tests cellspacing with a pane's table.
     */
    public void testOpenPaneCellspacing() throws Exception {
        privateSetUp();
        context.setDevice(INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice(DEVICE_NAME, new HashMap(), null)));
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        PaneAttributes paneAttrs = new PaneAttributes();
        paneAttrs.setStyles(StylesBuilder.getDeprecatedStyles());
//        paneAttrs.setBorderWidth("5");
        paneAttrs.setPane(pane);

        protocol.openPane(buffer, paneAttrs);

        buffer.closeElement("td");
        buffer.closeElement("tr");
        Element table = buffer.closeElement("table");
        checkTableAttributes(table);
    }

    /**
     * Tests cellspacing with a spatial format iterator's table.
     */
    public void testOpenSpatialFormatIteratorCellspacing() throws Exception {
        privateSetUp();
        context.setDevice(INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice(DEVICE_NAME, new HashMap(), null)));
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        SpatialFormatIterator sfi = new SpatialFormatIterator(canvasLayout);

        SpatialFormatIteratorAttributes sfiAttrs =
                new SpatialFormatIteratorAttributes();
        sfiAttrs.setStyles(StylesBuilder.getDeprecatedStyles());

        sfiAttrs.setFormat(sfi);

        protocol.openSpatialFormatIterator(buffer, sfiAttrs);

        Element table = buffer.closeElement("table");
        checkTableAttributes(table);
    }

    /**
     * Tests cellspacing with a column iterator's table.
     */
    public void testOpenColumnIteratorPaneCellspacing() throws Exception {
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        ColumnIteratorPaneAttributes cipAttrs =
                new ColumnIteratorPaneAttributes();
        cipAttrs.setStyles(StylesBuilder.getDeprecatedStyles());
        cipAttrs.setPane(pane);
        cipAttrs.setFormat(pane);

        protocol.openColumnIteratorPane(buffer, cipAttrs);

        buffer.closeElement("tr");
        Element table = buffer.closeElement("table");
        checkTableAttributes(table);
    }

    /**
     * Tests cellspacing with a grid's table.
     */
    public void testOpenGridCellspacing() throws Exception {
        privateSetUp();
        context.setDevice(INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice(DEVICE_NAME, new HashMap(), null)));
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        GridAttributes gridAttrs = new GridAttributes();
        gridAttrs.setStyles(StylesBuilder.getDeprecatedStyles());
        gridAttrs.setFormat(pane);

        protocol.openGrid(buffer, gridAttrs);

        Element table = buffer.closeElement("table");
        checkTableAttributes(table);
    }

    /**
     * Tests cellspacing with a table.
     */
    public void testOpenTableCellspacing() throws Exception {
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        TableAttributes tableAttrs = new TableAttributes();
        tableAttrs.setStyles(StylesBuilder.getInitialValueStyles());

        protocol.openTable(buffer, tableAttrs);

        Element table = buffer.closeElement("table");
        checkTableAttributes(table);
    }

    /**
     * Helper method which checks the attributes of the given table element.
     * @param table the table element whose attributes are to be checked
     */
    protected void checkTableAttributes(Element table) {
    }

//    protected void renderMenuItemSeparatorImplTest(String result) {
//        assertEquals("###<br/><a href=\"http://1\">1" +
//                "</a><br/><a href=\"http://2\">2</a><br/>###<br/>" +
//                "<a href=\"http://3\">3</a>", result.trim());
//    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10803/5	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 14-Dec-05	10803/2	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 14-Dec-05	10799/1	geoff	VBM:2005081506 Port 2005071314 forward to MCS

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/8	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Aug-05	9314/1	rgreenall	VBM:2005081516 Merge from 330 - forward port of VBM:2004041304

 17-Aug-05	9310/1	rgreenall	VBM:2005081516 Forward port of VBM:2004041304

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 19-Jul-05	9039/4	emma	VBM:2005071401 supermerge required

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 22-Jun-05	8483/4	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/2	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Nov-04	6298/2	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 26-Nov-04	6076/4	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 06-Sep-04	5361/5	pcameron	VBM:2004082517 New subprotocol for SE P800/P900 devices which adds cellspacing=0 to all tables

 06-Sep-04	5361/3	pcameron	VBM:2004082517 New subprotocol for SE P800/P900 devices which adds cellspacing=0 to all tables

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 22-Jul-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/1	steve	VBM:2004042901 Style class rendering fix

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 23-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 12-Nov-03	1861/1	mat	VBM:2003110602 Added presentation markup to XHTMLMobile1_0 and corrected mime types

 15-Sep-03	1321/5	adrian	VBM:2003082111 Fixed bug in wap-input-required styleproperty generation

 10-Sep-03	1321/3	adrian	VBM:2003082111 output validation css in same rule as theme properties

 05-Sep-03	1321/1	adrian	VBM:2003082111 added wcss input validation for xhtmlmobile

 02-Sep-03	1305/1	adrian	VBM:2003082108 added new openwave6 xhtml protocol

 ===========================================================================
*/
