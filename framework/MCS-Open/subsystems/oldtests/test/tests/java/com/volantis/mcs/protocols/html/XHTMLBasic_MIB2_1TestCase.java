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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasic_MIB2_1TestCase.java,v 1.8 2003/04/16 10:23:22 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Oct-02    Phil W-S        VBM:2002081322 - Created unit test and
 *                              implemented tests for openPane.
 * 27-Nov-02    Phil W-S        VBM:2002112003 - Updated testing for openPane
 *                              and added testing for closePane.
 * 13-Dec-02    Phil W-S        VBM:2002110516 - Changes for refactored
 *                              PageHead.
 * 19-Dec-02    Phil W-S        VBM:2002121601 - Fix tests after change to way
 *                              in which device layouts are used.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and so it 
 *                              uses the new TestMariner...Context classes 
 *                              rather than a "cut & paste" inner classes 
 *                              which extend Mariner...Context.
 * 06-Mar-03    Sumit           VBM:2003022605 - Moved static constants up to
 *                              DOMProtocolTestCase
 * 17-Apr-03    Geoff           VBM:2003041505 - Expanded static objects into
 *                              instance variables, made abstract as per name.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneRendering;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.StylesBuilder;

/**
 * This class unit test the XHTMLBasic_MIB2_1 protocol.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class XHTMLBasic_MIB2_1TestCase
    extends XHTMLBasicTestCase {

    protected final static String PANE_CLASS = "VF-0";

    private XHTMLBasic_MIB2_1 protocol;
    private XHTMLBasicTestable testable;

    private TestMarinerPageContext context;
    
    public XHTMLBasic_MIB2_1TestCase(String name) {
        super(name);
    }

    // javadoc inherited.
    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLBasic_MIB2_1Factory(),
                internalDevice);
        return protocol;
    }

    // javadoc inherited.
    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);
        
        this.protocol = (XHTMLBasic_MIB2_1) protocol;
        this.testable = (XHTMLBasicTestable) testable;
    }

    /**
     * Private setup.
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
        DeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        deviceContext.setDeviceLayout(runtimeDeviceLayout);
        context.pushDeviceLayoutContext(deviceContext);
        ContextInternals.setMarinerPageContext(requestContext, context);
        protocol.setMarinerPageContext(context);

        PaneInstance paneInstance = new TestPaneInstance();
        paneInstance.setStyleClass("fred");
        context.setFormatInstance(paneInstance);
        
        PageHead head = new PageHead();
        testable.setPageHead(head);
        testable.setStyleSheetRenderer(CSSStyleSheetRenderer.getSingleton());
        protocol.setMarinerPageContext(context);
        context.setDeviceName(DEVICE_NAME);
    }

    // javadoc inherited
    protected String getExpectedProtocolString() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML Basic 1.0//EN\" " +
                "\"http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd\">";
    }

    // Javadoc inherited.
    protected String getExpectedHorizontalRuleMarkup() {
        return "<hr/>";
    }

    public void testOpenPane() {
        // stub out XHTMLBasic version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    public void testOpenPaneWithContainerCell() throws Exception {
        // stub out XHTMLBasic version of this test
        // @todo refactor parent test so we can call it from here
        // or do a complete re-temporary if that makes sense
    }

    /**
     * To use a div, there must be style attributes on the pane and no td
     * around the pane's markup.
     *
     * todo XDIME-CP
     */
    public void notestOpenPaneUseDiv() throws Exception {
        privateSetUp();
        DOMOutputBuffer dom = new DOMOutputBuffer();
        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);

        dom.initialise();

        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "background-color: #ff0000"));
        attributes.setPane(pane);

        context.setFormatInstance(new TestPaneInstance());
        protocol.setMarinerPageContext(context);
        protocol.openPane(dom, attributes);

        Element element = dom.getCurrentElement();
        assertEquals("Element name", "div", element.getName());
    }

    /**
     * To use the surrounding td cell, there may be stylistic values on the
     * pane, but these must not clash with the cell's own attributes.
     */
    public void testOpenPaneUseCell() throws Exception {
        privateSetUp();
        DOMOutputBuffer dom = new DOMOutputBuffer();
        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);

        dom.initialise();

        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "background-color: #ff0000"));
        attributes.setPane(pane);

        context.setFormatInstance(new TestPaneInstance());
        protocol.setMarinerPageContext(context);
        Element parent = dom.openStyledElement("td", attributes);
        protocol.openPane(dom, attributes);

        Element element = dom.getCurrentElement();
        assertEquals("Element added", parent, element);
    }

    /**
     * To be unable to use the td cell, the surrounding td must have attributes
     * that clash with those of the pane. In this case a div will be output
     * to surround the pane's content.
     */
    public void testOpenPaneCantUseCell() throws Exception {
        privateSetUp();
        DOMOutputBuffer dom = new DOMOutputBuffer();
        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);
        Element parent = null;

        dom.initialise();

        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "background-color: #ff0000"));
        attributes.setPane(pane);

        context.setFormatInstance(new TestPaneInstance());
        protocol.setMarinerPageContext(context);
        parent = dom.openStyledElement("td", attributes);
        parent.setAttribute("id", "parent");
        attributes.setId("child");
        protocol.openPane(dom, attributes);

        Element element = dom.getCurrentElement();
        assertEquals("Element name", "div", element.getName());
    }

    /**
     * The pane's content doesn't need to be surrounded by anything if there
     * are no stylistic attributes on the pane.
     */
    public void notestOpenPaneDoNothing() throws Exception {
        privateSetUp();
        DOMOutputBuffer dom = new DOMOutputBuffer();
        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);
        PaneAttributes attributes = new PaneAttributes();
        Element parent = null;
        Element element = null;

        dom.initialise();

        attributes.setPane(pane);
        pane.setBackgroundColour(null);
        pane.setBorderWidth(null);
        pane.setCellPadding(null);
        pane.setCellSpacing(null);
        pane.setHeight(null);
        pane.setWidth(null);
        pane.setHorizontalAlignment(null);
        pane.setVerticalAlignment(null);

        context.setFormatInstance(new TestPaneInstance());
        protocol.setMarinerPageContext(context);

        // Make the test framework behave as if no style class is required!
//        context.setStyleClassName(null);
        testable.setPageHead(new PageHead());

        parent = dom.openStyledElement("body", attributes);
        protocol.openPane(dom, attributes);

        try {
            element = dom.closeElement("body");
        } catch (IllegalStateException e) {
            fail("Expected body element not found");
        }

        assertSame("The parent was not as expected",
                   parent,
                   element);

        assertEquals("Pane class attribute not as expected",
                     null,
                     element.getAttributeValue("class"));
    }

    /**
     * Helper method.
     */
    private DOMOutputBuffer setupForPaneTests(PaneRendering rendering,
                                              PaneAttributes attributes) {
        context = new TestMarinerPageContext();

        DOMOutputBuffer dom = new DOMOutputBuffer();
        TestDeviceLayoutContext deviceLayoutContext = new TestDeviceLayoutContext();

        // Intialize the dom
        dom.initialise();

        // Intialize the context.
        context.pushDeviceLayoutContext(deviceLayoutContext);

        // Intialize the protocol
        protocol.setMarinerPageContext(context);

        // Initialize the attributes.
        attributes.setPane(new Pane(null));

        // Intialize the rendering.
        PaneInstance paneInstance = (PaneInstance) context.getDeviceLayoutContext().
                getCurrentFormatInstance(attributes.getPane());
        paneInstance.setRendering(rendering);

        return dom;
    }

    // javadoc inherited.
    public void testClosePane() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        DOMOutputBuffer dom = setupForPaneTests(PaneRendering.USE_TABLE,
                                                attributes);

        final Element expected;
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        dom.openStyledElement("body", attributes);
        expected = dom.openElement("div");

        protocol.closePane(dom, attributes);

        assertSame("The DOM's current element isn't the body element",
                   dom.getCurrentElement(),
                   expected);
    }

    /**
     * Test closing a pane where a div has been used to surround the pane's
     * content.
     */
    public void testClosePaneUseDiv() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        DOMOutputBuffer dom = setupForPaneTests(
                PaneRendering.CREATE_ENCLOSING_ELEMENT, attributes);


        // This does not effect the outcome of the test.
//        context.setStyleClassName(PANE_CLASS);

        final Element expected;
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        expected = dom.openStyledElement("body", attributes);
        dom.openElement("div");
        dom.appendEncoded("Example");

        protocol.closePane(dom, attributes);

        assertSame("The DOM's current element isn't the body element",
                   dom.getCurrentElement(),
                   expected);
    }

    /**
     * Test closing a pane where the surrounding td has been used as the
     * pane's content container.
     */
    public void testClosePaneUseCell() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        DOMOutputBuffer dom = setupForPaneTests(
                PaneRendering.USE_ENCLOSING_TABLE_CELL, attributes);

        final Element expected;
        expected = dom.openStyledElement("td", attributes);

        dom.appendEncoded("Example");

        protocol.closePane(dom, attributes);

        assertSame("The DOM's current element isn't the td element",
                   dom.getCurrentElement(),
                   expected);
    }

    /**
     * Test closing a pane where nothing needed to be done to surround the
     * pane's content.
     *
     * @throws Exception
     */
    public void testClosePaneDoNothing() throws Exception {
        PaneAttributes attributes = new PaneAttributes();
        DOMOutputBuffer dom = setupForPaneTests(
                PaneRendering.DO_NOTHING, attributes);

        final Element expected;
        expected = dom.openStyledElement("body", attributes);
        dom.appendEncoded("Example");

        protocol.closePane(dom, attributes);

        assertSame("The DOM's current element isn't the body element",
                   dom.getCurrentElement(),
                   expected);
    }

    // javadoc inherited
    protected String expectedDefaultMimeType() {
        return "text/html";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/7	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/3	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 04-Nov-04	5871/4	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - moved PaneRendering

 03-Nov-04	5871/2	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 22-Jul-04	4713/7	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 29-Jun-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/1	steve	VBM:2004042901 Style class rendering fix

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 01-Nov-03	1749/1	philws	VBM:2003081102 Port of Spatial and Temporal Iterator layout stylesheet handling from PROTEUS

 01-Nov-03	1745/1	philws	VBM:2003081102 Provide layout style for Spatial and Temporal Iterators

 26-Aug-03	1015/4	geoff	VBM:2003072208 Style Class on spatial iterated pane not set on table cell in generated markup (supermerge fixes)

 08-Aug-03	1015/1	geoff	VBM:2003072208 merge from Mimas

 08-Aug-03	1011/1	geoff	VBM:2003072208 port from metis

 08-Aug-03	1004/1	geoff	VBM:2003072208 fix pane rendering in xhtml from netfront3

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 ===========================================================================
*/
