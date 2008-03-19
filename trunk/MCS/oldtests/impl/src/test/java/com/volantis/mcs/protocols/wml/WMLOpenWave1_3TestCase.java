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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/WMLOpenWave1_3TestCase.java,v 1.7 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Oct-02    Allan           VBM:2002100801 - TestCase for WML OpenWave
 *                              1.3 protocol
 * 14-Oct-02    Geoff           VBM:2002100905 - Modified to use new DOMPool
 *                              constructor.
 * 16-Dec-02    Adrian          VBM:2002100203 - Updated references to
 *                              supportsMultipleStyleClasses to
 *                              supportsMultipleSelectorClasses.
 * 20-Jan-03    Doug            VBM:2002120213: Removed the writeOptGroups()
 *                              method from inner MyWMLOpenWave1_3 class.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes.
 * 14-Mar-03    Chris W         VBM:2003030404 - Added getOutputBufferfactory
 *                              to TestWMLOpenWave1_3 inner class.
 * 27-May-03    Byron           VBM:2003051904 - Added templateTestDoMenu and
 *                              templateTestDoMenuHorizontal methods. Updated
 *                              TestWMLOpenWave1_3 inner class to be able to
 *                              get/set the style.
 * 30-May-03    Chris W         VBM:2003052702 - getExpectedDissectingPaneMarkup
 *                              overridden so to make tests pass.
 * 02-Jun-03    Mat             VBM:2003042906 - Removed testDoProtocolString()
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.DoSelectInputTestHelper;
import com.volantis.mcs.protocols.FragmentLinkRenderer;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.StylesBuilder;

/**
 * Test the WMLOpenWave1_3 (Open Wave Version 5 browser) protocol.
 *
 * @todo this class in in wrong position in test hierarchy...
 */
public class WMLOpenWave1_3TestCase extends WMLRootTestCase {

    private WMLOpenWave1_3 protocol;
    private WMLRootTestable testable;

    private TestDOMOutputBuffer buffer;
    private Document document;

    public WMLOpenWave1_3TestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestWMLOpenWave1_3Factory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (WMLOpenWave1_3) protocol;
        this.testable = (WMLRootTestable) testable;
    }

    /**
     * Setup the unit test framework by creating some classes ensuring that each
     * test case starts off with a clean slate to work from
     */
    private void privateSetUp() {
        protocol.setMarinerPageContext(new TestMarinerPageContext());
        buffer = new TestDOMOutputBuffer();
        buffer.initialise();
        document = domFactory.createDocument();
        testable.setCurrentBuffer(null, buffer);
    }

    // javadoc inherited.
    public void testProtocolHasCorrectDTD() throws Exception {
        final WMLOpenWave1_3Configuration cfg =
            (WMLOpenWave1_3Configuration)protocol.getProtocolConfiguration();

        assertEquals("DTD should match",
                     "http://www.openwave.com/dtd/wml13.dtd",
                     cfg.getPublicIdCode().getDtd());

        assertEquals("Name should match",
                     "-//OPENWAVE.COM//DTD WML 1.3//EN",
                     cfg.getPublicIdCode().getName());

        assertEquals("Code should match",
                     0x110D,
                     cfg.getPublicIdCode().getInteger());
    }

    // javadoc inherited
    protected void templateTestDoMenuHorizontal(StringBuffer buf,
                                                MenuItem menuItem,
                                                boolean hasNext) {
        buf.append("<a");
        if (menuItem.getShortcut() != null) {
            buf.append(" accesskey=\"" + menuItem.getShortcut() + "\"");
        }
        buf.append(" href=\"").append(menuItem.getHref()).append("\">");
        buf.append(menuItem.getText()).append("</a>");
        if (hasNext) {
            buf.append("&#160;");
        }
    }

    // javadoc inherited
    protected String getExpectedDissectingPaneMarkup(DissectingPane pane, DissectingPaneAttributes attr) {
        String expected =
          "<" + DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT + "/>" +
          "<" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">" +
            "<p mode=\"wrap\">" +
              "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
                "<a accesskey=\"" + pane.getNextShardShortcut() + "\" " +
                    "href=\"" + DissectionConstants.URL_MAGIC_CHAR + "\">" +
                attr.getLinkText() +
                "</a>" +
              "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
              "<" + DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT + ">" +
            '\u00a0' +
              "</" + DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT + ">" +
              "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
                "<a accesskey=\"" + pane.getPreviousShardShortcut() + "\" " +
                    "href=\"" + DissectionConstants.URL_MAGIC_CHAR +"\">" +
                attr.getBackLinkText() +
                "</a>" +
              "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
            "</p>" +
          "</" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">";
        return expected;
    }

    // javadoc inherited
    protected String getExpectedNumericShortcutMenuOutput(
            boolean accesskeynumdisplay) {
        // delegate to the OpenwaveMenuTestCaseHelper helper class
        return OpenwaveMenuTestCaseHelper.
                getExpextedNumericShortcutRendererOutput();
    }

    // javadoc inherited
    public void testDoHorizontalRuleEmulationOff() throws Exception {
        privateSetUp();
        assertFalse("Emulation should be off", protocol.emulateHorizontalTag);

        protocol.doHorizontalRule(buffer, null);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("Wrong output in doHorizontalRule method", "<hr/>", output);
    }

    // javadoc inherited
    public void testDoHorizontalRuleEmulationOn() throws Exception {
        privateSetUp();
        protocol.emulateHorizontalTag = true;
        assertTrue("Emulation should be on", protocol.emulateHorizontalTag);

        pageContext = new TestMarinerPageContext();
        pageContext.setProtocol(protocol);
        pageContext.setDevicePolicyValue("charactersx", "10");
        protocol.doHorizontalRule(buffer, null);
        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
            document, protocol.getCharacterEncoder());
        assertEquals("Wrong output in doHorizontalRule method",
                     "<hr/>",
                     output);
    }

    /**
     * Test the rendering of the
     * @throws Exception
     */
    public void testRenderSelectionSimple() throws Exception {
        privateSetUp();
        XFSelectAttributes attributes = new XFSelectAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());
        
        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();
        helper.addOption(attributes,"Caption","Prompt","Value",false);
                
        attributes.setName("testName");
        WMLDefaultSelectionRenderer renderer =
            (WMLDefaultSelectionRenderer)protocol.getSelectionRenderer(attributes);
        renderer.renderSelection(attributes, buffer);
        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("Wrong output in getSelectionRenderer method",
            "<select multiple=\"false\" name=\"testName\">" +
            "<option title=\"Prompt\" value=\"Value\">Caption</option>" +
            "</select>",
            output);
    }

    /**
     * Test the complex rendering for a circular-list (spin).
     */
    public void testRenderSelectionComplexMenu() throws Exception {
        doTestRenderSelection("circular-list",
            "<select multiple=\"false\" name=" +
            "\"testName\" type=\"spin\">" +
            "<option title=\"Prompt\" value=\"Value\">Caption</option>" +
            "</select>",
            false);
    }

    /**
     * Test the complex rendering for a menu (popup).
     */
    public void testRenderSelectionComplexSpin() throws Exception {
        doTestRenderSelection("menu",
            "<select multiple=\"false\" name=" +
            "\"testName\" type=\"popup\">" +
            "<option title=\"Prompt\" value=\"Value\">Caption</option>" +
            "</select>",
            false);
    }

    /**
     * Test the complex rendering for a default.
     */
    public void testRenderSelectionComplexDefault() throws Exception {
        doTestRenderSelection("default",
            "<select multiple=\"false\" name=" +
            "\"testName\">" +
            "<option title=\"Prompt\" value=\"Value\">Caption</option>" +
            "</select>",
            false);
    }


    /**
     * Test the complex rendering for a default.
     */
    public void testRenderSelectionComplexSpinMulti() throws Exception {
        doTestRenderSelection("circular-list",
            "<select multiple=\"true\" name=" +
            "\"testName\">" +
            "<option title=\"Prompt\" value=\"Value\">Caption</option>" +
            "</select>",
            true);                               
    }

    /**
     * Test the complex rendering for a default.
     */
    public void testRenderSelectionComplexMenuMulti() throws Exception {
        doTestRenderSelection("menu",
            "<select multiple=\"true\" name=" +
            "\"testName\">" +
            "<option title=\"Prompt\" value=\"Value\">Caption</option>" +
            "</select>",
            true);                               
    }

    /**
     * Test the complex rendering for 'controls' single select.
     */
    public void testRenderSelectionControlsSingleSelect() throws Exception {
        doTestRenderSelection("controls",
            "<select multiple=\"false\" name=" +
            "\"testName\" type=\"radio\">" +
            "<option title=\"Prompt\" value=\"Value\">Caption</option>" +
            "</select>",
        false);                               
    }

    /**
     * Test the complex rendering for 'controls' mutliple select.
     */
    public void testRenderSelectionControlsMultiSelect() throws Exception {
        doTestRenderSelection("controls",
                               "<select multiple=\"true\" name=" +
                               "\"testName\">" +
            "<option title=\"Prompt\" value=\"Value\">Caption</option>" +
            "</select>",
            true);
    }

    /**
     * Helper method to actually do the test for the selection renderer.
     */
    private void doTestRenderSelection(final String listType,
                                       final String expected,
                                       final boolean multipleSelect)
        throws Exception {

        privateSetUp();

        XFSelectAttributes attributes = new XFSelectAttributes();
        attributes.setStyles(StylesBuilder.getCompleteStyles(
                "mcs-selection-list-style: " + listType));

        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();
        helper.addOption(attributes,"Caption","Prompt","Value",false);

        attributes.setName("testName");
        attributes.setMultiple(multipleSelect);
        attributes.setTitle("title");

        MarinerRequestContext requestContext = new TestMarinerRequestContext();

        TestMarinerPageContext context =
            (TestMarinerPageContext)protocol.getMarinerPageContext();

        context.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, context);

        PaneInstance paneInstance = new TestPaneInstance() {
            public OutputBuffer getCurrentBuffer() {
                return buffer;
            }
        };
        context.setFormatInstance(paneInstance);

        protocol.setMarinerPageContext(context);

        WMLDefaultSelectionRenderer renderer =
            (WMLDefaultSelectionRenderer)protocol.getSelectionRenderer(null); // Arguments are only used in WapTV5

        renderer.renderSelection(attributes, buffer);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("Wrong output in getSelectionRenderer method",
                     expected,
                     output);
    }

    // Inherit Javadoc.
    protected void checkNumericShortcutFragmentLinkRenderer(
            FragmentLinkRenderer renderer) {

        assertTrue("Numeric-shortcut renderer should be OpenWave", renderer
                instanceof OpenWaveNumericShortcutFragmentLinkRenderer);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10328/7	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 17-Nov-05	10330/2	pabbott	VBM:2005110907 Honour align with mode=nospace

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/5	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 20-Sep-04	5575/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 20-Sep-04	5527/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 22-Jul-04	4713/7	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 20-Jul-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 12-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 15-Jun-04	4704/4	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 18-May-04	4461/1	ianw	VBM:2004051714 Only render select element if it contains options for wml

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 14-May-04	4315/2	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 07-May-04	4164/4	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 29-Apr-04	4091/1	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 06-May-04	3272/2	philws	VBM:2004021117 Fix merge issues

 29-Apr-04	4091/1	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 05-May-04	4167/3	steve	VBM:2004042901 Fix up javadoc

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/1	steve	VBM:2004042901 Style class rendering fix

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 25-Sep-03	1412/10	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 23-Sep-03	1412/6	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (rework to get link style from source fragment)

 17-Sep-03	1412/3	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 22-Sep-03	1394/10	doug	VBM:2003090902 centralised common openwave menu rendering code

 17-Sep-03	1394/8	doug	VBM:2003090902 added support for openwave numeric shortcut menus

 16-Sep-03	1301/7	byron	VBM:2003082107 Support Openwave GUI Browser extensions - single select controls handled as type=radio

 16-Sep-03	1301/5	byron	VBM:2003082107 Support Openwave GUI Browser extensions - single select controls handled as type=radio

 10-Sep-03	1301/1	byron	VBM:2003082107 Support Openwave GUI Browser extensions

 04-Sep-03	1331/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 17-Aug-03	1052/1	allan	VBM:2003073101 Support styles on menu and menuitems

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 17-Jun-03	427/1	mat	VBM:2003061607 Changes to testcases

 13-Jun-03	399/1	mat	VBM:2003060503 Handle new special dissection tags

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 06-Jun-03	277/2	chrisw	VBM:2003052702 Merged changes from Metis to Mimas

 05-Jun-03	285/2	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
