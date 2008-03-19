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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/WMLRootTestCase.java,v 1.29 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Oct-02    Allan           VBM:2002100202 - TestCase for WMLRoot protocol
 * 07-Oct-02    Allan           VBM:2002100708 - Added writeOptGroups tests
 *                              for prompt. Renamed
 *                              testWriteOptGroupsCaptionTexComponent to
 *                              writeOptGroupsCaptionTextComponent.
 * 14-Oct-02    Geoff           VBM:2002100905 - Modified to use new DOMPool
 *                              constructor.
 * 16-Dec-02    Adrian          VBM:2002100203 - Updated references to
 *                              supportsMultipleStyleClasses to
 *                              supportsMultipleSelectorClasses.
 * 13-Jan-03    Allan           VBM:2002120209 - testDoSelectInputAttributes()
 *                              added. Added setSupportsAcessKeyAttribute() to
 *                              My...Protocol inner class. Added
 *                              getContentBuffer() to same inner class. Added
 *                              ShortCut inner class and override of
 *                              protocol.getTextFromReference() to handle shotcut.
 *                              Reformmated conde to 4 character indents.
 * 21-Jan-03    Byron           VBM:2003011617 - Updated test cases to test
 *                              anchor tag generation for various permutations.
 * 20-Jan-03    Doug            VBM:2002120213: Commented out all the fixtures
 *                              that call writeOptGroups in WML protocol. This
 *                              is an interim measure.
 * 20-Jan-03    Doug            VBM:2003012408 - Added test cases for the
 *                              doSelectInput() method. Refactored all the
 *                              fixtures that I had commented out in the
 *                              previous edit.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes.
 * 17-Feb-03    Sumit           VBM:2003021301 - Added testOpenGridRow and
 *                              supporting layout creation function.
 * 19-Feb-03    Adrian          VBM:2003010605 - Updated usages of
 *                              ResourceAction to override actionPerformedImpl
 *                              instead of actionPerformed.
 * 21-Feb-03    Sumit           VBM:2003022101 - OpenGridRow needs to call
 *                              openGrid first
 * 25-Feb-03    Byron           VBM:2003022105 - Modified test methods to throw
 *                              Exception. Added tests for addEnterEvents.
 * 03-Mar-03    Byron           VBM:2003022813 - Added testAddActionResetBasic,
 *                              testAddActionResetFormAttributes,
 *                              testAddActionResetFormAttributesInitialValue,
 *                              doTestAddActionReset,
 *                              testAddActionResetFormAttributesInitialValueValid.
 * 04-Mar-03    Byron           VBM:2003022813 - Added transformMarkup that returns
 *                              a string representation of the DOM. Modified
 *                              doTestAddActionReset (and others) to use the
 *                              new method.
 * 06-Mar-03    Sumit           VBM:2003022605 - Moved static constants up to
 *                              DOMProtocolTestCase
 * 07-Mar-03    Sumit           VBM:2003030711 - Made document protected
 * 13-Mar-03    Mat             VBM:2003031203 - Added testDoMeta()
 * 14-Mar-03    Doug            VBM:2003030409 - Added the
 *                              testOpenDissectingPane() fixture
 * 13-Mar-03    Phil W-S        VBM:2003031110 - Make protocol and testable
 *                              protected so specializations can access them.
 * 21-Mar-03    Sumit           VBM:2003022828 - Added getTextInputFormat test
 * 25-Mar-03    Sumit           VBM:2003032006 - testOpenGridRow opens a pane
 *                              to add pane alignment to test buffer
 * 28-Mar-03    Geoff           VBM:2003031711 - Change name of parent class
 *                              to reflect refactoring, and fix ^M problems.
 * 02-Apr-03    Geoff           VBM:2003032609 - Add more tests for single
 *                              column grid rendering of horizontal alignment.
 * 17-Apr-03    Geoff           VBM:2003041505 - Expanded static objects into
 *                              instance variables.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add ProtocolException
 *                              declarations where necessary.
 * 17-Apr-03    Chris W         VBM:2003031909 - Added testAddPostFieldXXX and
 *                              testGetXFImplicitAttributesValueXXX methods.
 * 17-Apr-03    Geoff           VBM:2003040305 - Modified
 *                              testAddEnterEvents to avoid using the HTML
 *                              element, and modified
 *                              testAddOnEventElementTask to use the new
 *                              Script class, and add to do comment.
 * 24-Apr-03    Chris W         VBM:2003030404 - Added testSupportsNativeMarkup.
 *                              testWriteOpenNativeMarkup, testCloseCard,
 *                              testWriteCloseNativeMarkup, testOpenCard plus
 *                              supporting infrastructure.
 * 25-Mar-03    Chris W         VBM:2003031905 - Amended testOpenCard,
 *                              testCloseCard and added testOpenLayout and
 *                              getExpectedTestOpenLayoutResult to reflect
 *                              changes the way native markup handled in WMLRoot
 * 21-May-03    Sumit           VBM:2003032713 - Added renderMenuItemSeparator
 *                              test
 * 27-May-03    Byron           VBM:2003051904 - Added
 *                              testDoMenuHorizontal/Template(). Updated
 *                              TestWMLRoot inner class to be able to get/set
 *                              the style.
 * 30-May-03    Chris W         VBM:2003052702 - CHnaged testOpenDissectingPane
 *                              and added testCloseDissecingPane()
 * 31-May-03    Chris W         VBM:2003042906 - Fixed testClosDissectingPane()
 *                              to store ShardLinkActions in ShardLinkAttributes
 * 02-Jun-03    Geoff           VBM:2003042906 - Fix and reenable the test
 *                              cases I commented out earlier.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.dissection.DissectableAreaAttributes;
import com.volantis.mcs.dissection.DissectableAreaIdentity;
import com.volantis.mcs.dom.DOMAssertionUtilities;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.CaptionAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocolTestAbstract;
import com.volantis.mcs.protocols.DOMScript;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.DoSelectInputTestHelper;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.FragmentLinkRendererContext;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MetaAttributes;
import com.volantis.mcs.protocols.NativeMarkupAttributes;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.Script;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBufferFactory;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFImplicitAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstance;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.CaptionSideKeywords;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.StylingFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class tests the WMLRoot protocol.
 * <p>
 * Note that this test class is hierarchical.
 */
public class WMLRootTestCase extends DOMProtocolTestAbstract {

    protected WMLRoot protocol;
    protected WMLRootTestable testable;

    private Element element;
    private TestDOMOutputBuffer buffer;
    protected PageHead pageHead;
    protected String elementName = "myElementName";
    protected Document document;
    protected TestMarinerPageContext pageContext;

    /** Creates new TestDOMElement */
    public WMLRootTestCase(String name) {
        super(name);
    }


    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestWMLRootFactory(), internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (WMLRoot) protocol;
        this.testable = (WMLRootTestable) testable;
    }

    /**
     * Setup the unit test framework by creating some classes ensuring that each
     * test case starts off with a clean slate to work from
     */
    private void privateSetUp() {
        canvasLayout = new CanvasLayout();
        //testable.setSupportsAccessKeyAttribute(false);
        element = domFactory.createElement();
        element.setName(elementName);
        buffer = new TestDOMOutputBuffer();
        document = domFactory.createDocument();
        testable.setCurrentBuffer(null, buffer);
        pageContext = new TestMarinerPageContext();
        pageContext.setFormFragmentResetState(true);
        pageContext.setProtocol(protocol);
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        pageContext.pushRequestContext( requestContext );
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);

        // Add in a default protocols configuration so that post field
        // elements do not fail.

        Volantis bean = getVolantis();
        ProtocolsConfiguration config = bean.getProtocolsConfiguration();
        config.setWmlPreferredOutputFormat("wmlc");
        pageContext.setVolantis(bean);


        // set a page head attribute to prevent multiple fixtures failing when
        // these tests are run with the WapTV5_WMLVersion1_3 protocol subclass.
        // @todo this code ought to be shared between the subclasses!
        pageHead = new PageHead();
        pageHead.setAttribute("wtv_connect_now", "non-null");
        testable.setPageHead(pageHead);

    }
    
    public void testWritePageHead() throws Exception {
        // DOMProtocolTestCase.testWritePageHead hardcodes support of
        // Javascript to true, which breaks in this context, so I've had
        // to stub this out for now.
        // @todo modify parent test case so it works in this context
    }

    /**
     * Test writeOptGroups with an empty SelectOptionGroup (i.e. no options)
     * element in the List of options. This method validates the use of a
     * literal string for the title attribute that originates from the caption
     * tag attribute.
     */
    public void testWriteOptGroupsCaptionReference() throws ProtocolException {
        privateSetUp();

        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();
        XFSelectAttributes atts = helper.buildSelectAttributes();
        helper.addOptionGroup(atts, "literal", null);
        doOptionGroupTitleTest(atts,"literal");
    }


    /**
     * Test writeOptGroups with an empty SelectOptionGroup (i.e. no options)
     * element in the List of options. This method validates the use of a
     * literal string for the title attribute that originates from the prompt
     * tag attribute.
     */
    public void testWriteOptGroupsPromptReference() throws ProtocolException {
        privateSetUp();

        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();
        XFSelectAttributes atts = helper.buildSelectAttributes();

        helper.addOptionGroup(atts, null, "literal");
        doOptionGroupTitleTest(atts, "literal");
    }

    /**
     * Helper method to calls doSelectInput() and then check the
     * title attribute of the optgroup elements
     * @param atts the XFSelectAttributes
     * @param expectedTitle the expected title
     */
    protected void doOptionGroupTitleTest(XFSelectAttributes atts,
            final String expectedTitle) throws ProtocolException {

        protocol.doSelectInput(atts);

        element = (Element) buffer.popElement().getHead();

        OptionGroupTitleChecker checker = new OptionGroupTitleChecker();

        Element child = getSelectElement(element);

        checker.checkElement(child, expectedTitle);
    }

    private static class OptionGroupTitleChecker
            extends RecursingDOMVisitor {

        private String expectedTitle;

        public void checkElement(Element element, String expectedTitle) {
            this.expectedTitle = expectedTitle;
            element.forEachChild(this);
        }

        public void visit(Element element) {
            assertEquals("Should be an optgroup element", "optgroup",
                         element.getName());
            String title = element.getAttributeValue("title");
            assertEquals("title should be ", expectedTitle,  title);

            skipRemainder();
        }
    }

    protected Element getSelectElement(Element element) {
        return element;
    }

    /**
     * Test openHeading1() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenHeading1EmptyAttributes() throws Exception {
        privateSetUp();

        final HeadingAttributes attributes = new HeadingAttributes();
        addStyles(attributes, "h1");
        protocol.openHeading1(buffer, attributes);
        checkElements(expectedDefaultHeading1Elements());
        document.addNode(element);

        String br = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("<br/>", br.substring(0, 5));
    }
*/

    protected String[] expectedDefaultHeading1Elements() {
        return new String[]{"big", "ANTI-U", "ANTI-I", "b"};
    }

    /**
     * Test openHeading2() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenHeading2EmptyAttributes() throws Exception {
        privateSetUp();

        final HeadingAttributes attributes = new HeadingAttributes();
        addStyles(attributes, "h2");
        protocol.openHeading2(buffer, attributes);
        checkElements(expectedDefaultHeading2Elements());
        document.addNode(element);
        String br = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("<br/>", br.substring(0, 5));
    }
*/

    protected String[] expectedDefaultHeading2Elements() {
        return new String[]{"ANTI-SIZE", "ANTI-U", "i", "b"};
    }

    /**
     * Test openHeading3() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenHeading3EmptyAttributes() throws Exception {
        privateSetUp();

        final HeadingAttributes attributes = new HeadingAttributes();
        addStyles(attributes, "h3");
        protocol.openHeading3(buffer, attributes);
        checkElements(expectedDefaultHeading3Elements());
        document.addNode(element);
        String br = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("<br/>", br.substring(0, 5));
    }
*/

    protected String[] expectedDefaultHeading3Elements() {
        return new String[]{"ANTI-SIZE", "ANTI-U", "i"};
    }

    /**
     * Test openHeading4() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenHeading4EmptyAttributes() throws Exception {
        privateSetUp();

        final HeadingAttributes attributes = new HeadingAttributes();
        addStyles(attributes, "h4");
        protocol.openHeading4(buffer, attributes);
        checkElements(expectedDefaultHeading4Elements());
        document.addNode(element);
        String br = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("<br/>", br.substring(0, 5));
    }
*/

    protected String[] expectedDefaultHeading4Elements() {
        return new String[]{"ANTI-SIZE", "ANTI-U", "ANTI-I"};
    }

    /**
     * Test openHeading5() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenHeading5EmptyAttributes() throws Exception {
        privateSetUp();

        final HeadingAttributes attributes = new HeadingAttributes();
        addStyles(attributes, "h5");
        protocol.openHeading5(buffer, attributes);
        checkElements(expectedDefaultHeading5Elements());
        document.addNode(element);
        String br = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("<br/>", br.substring(0, 5));
    }
*/

    protected String[] expectedDefaultHeading5Elements() {
        return new String[]{"small", "ANTI-U", "i"};
    }

    /**
     * Test openHeading6() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenHeading6EmptyAttributes() throws Exception {
        privateSetUp();

        final HeadingAttributes attributes = new HeadingAttributes();
        addStyles(attributes, "h6");
        protocol.openHeading6(buffer, attributes);
        checkElements(expectedDefaultHeading6Elements());
        document.addNode(element);
        String br = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("<br/>", br.substring(0, 5));
    }
*/

    protected String[] expectedDefaultHeading6Elements() {
        return new String[]{"small", "ANTI-U", "ANTI-I"};
    }

    /**
     * Test openUnderline() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenUnderlineEmptyAttributes() throws Exception {
        privateSetUp();

        final UnderlineAttributes attributes = new UnderlineAttributes();
        addStyles(attributes, "u");
        protocol.openUnderline(buffer, attributes);
        checkElements(new String[]{"ANTI-SIZE", "u", "ANTI-I"});
    }
*/

    /**
     * Test openBold() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenBoldEmptyAttributes() throws Exception {
        privateSetUp();

        final BoldAttributes attributes = new BoldAttributes();
        addStyles(attributes, "b");
        protocol.openBold(buffer, attributes);
        checkElements(new String[]{"ANTI-SIZE", "ANTI-U", "ANTI-I", "b"});
    }
*/

    /**
     * Test openItalic() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenItalicEmptyAttributes() throws Exception {
        privateSetUp();

        final ItalicAttributes attributes = new ItalicAttributes();
        addStyles(attributes, "i");
        protocol.openItalic(buffer, attributes);
        checkElements(new String[]{"ANTI-SIZE", "ANTI-U", "i"});
    }
*/

    /**
     * Test openSmall() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenSmallEmptyAttributes() throws Exception {
        privateSetUp();

        final SmallAttributes attributes = new SmallAttributes();
        addStyles(attributes, "small");
        protocol.openSmall(buffer, attributes);
        checkElements(new String[]{"small", "ANTI-U", "ANTI-I"});
    }
*/

    /**
     * Test openBig() with empty attributes.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenBigEmptyAttributes() throws Exception {
        privateSetUp();

        final BigAttributes attributes = new BigAttributes();
        addStyles(attributes, "big");
        protocol.openBig(buffer, attributes);
        checkElements(new String[]{"big", "ANTI-U", "ANTI-I"});
    }
*/

//    public void testOpenStrongWithEmptyAttributes() throws Exception {
//        privateSetUp();
//        pageContext.setDevicePolicyValue(
//                DevicePolicyConstants.FIX_FOR_OPEN_INLINE_STYLING_ELEMENTS,
//                DevicePolicyConstants.NO_WHITESPACE_FIXING);
//        StrongAttributes attributes = new StrongAttributes();
//        protocol.openStrong(buffer, attributes);
//        element = buffer.getRoot();
//        document.addNode(element);
//        String strong = DOMUtilities.toString(document, protocol);
//        assertEquals("<strong/>", strong);
//    }

    /**
     * Test that doSelectInput does not set an accessKey attribute.
     */
    public void testDoSelectInputAccessKey() throws ProtocolException {
        privateSetUp();

        XFSelectAttributes attributes = new XFSelectAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        attributes.setEntryContainerInstance(new PaneInstance(null));
        attributes.setShortcut(new LiteralTextAssetReference("shortcut"));
        attributes.setName("name");
        FormInstance formInstance = new FormInstance(NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(new Form(null));
        attributes.setFormData(formInstance);

        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();
        helper.addOption(attributes,"Caption","Prompt","Value",false);
        
        testable.setSupportsAccessKeyAttribute(true);
        protocol.doSelectInput(attributes);

        element = buffer.popElement();
        element = (Element) element.getHead();

        Element selectElement = getSelectElement(element);
        assertNull("The select tag has an accesskey attribute when it" +
                   "should not.",
                   selectElement.getAttributeValue("accesskey"));
        assertEquals("name", selectElement.getAttributeValue("name"));
        assertEquals("select", selectElement.getName());
        assertNull("ivalue attribute should be null",
                   selectElement.getAttributeValue("ivalue"));
        assertEquals("false", selectElement.getAttributeValue("multiple"));
    }

    /**
     * Test that doSelectInput does not set an accessKey attribute.
     */
    public void doTestSimpleOptGroup() {
        privateSetUp();

        XFSelectAttributes attributes = new XFSelectAttributes();

        attributes.setEntryContainerInstance(new PaneInstance(null));
        attributes.setName("name");
        attributes.setInitial("E");

        FormInstance formInstance = new FormInstance(
                NDimensionalIndex.ZERO_DIMENSIONS);
        formInstance.setFormat(new Form(null));
        attributes.setFormData(formInstance);
    }

    /**
     * Test that the doSelectInput() method generates the correct markup
     * for a single select with no options selected
     * @exception Exception if an error occurs
     */
    public void testSingleSelectNoOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selectedOpts = {false, false, false};
        doSelectInputTestHelper(selectedOpts, false, null);
    }

    /**
     * Test that the doSelectInput() method generates the correct markup
     * for a single select with options selected
     * @exception Exception if an error occurs
     */
    public void testSingleSelectOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selectedOpts = {false, true, false};
        doSelectInputTestHelper(selectedOpts, false, "2");
    }

     /**
     * Test that the doSelectInput() method generates the correct markup
     * for a multi select with no options selected
     * @exception Exception if an error occurs
     */
    public void testMultipleSelectNoOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selectedOpts = {false, false, false};
        doSelectInputTestHelper(selectedOpts, true, null);
    }

    /**
     * Test that the doSelectInput() method generates the correct markup
     * for a multi select with options selected
     * @exception Exception if an error occurs
     */
    public void testMultipleSelectOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selectedOpts = {false, true, true};
        doSelectInputTestHelper(selectedOpts, true, "2;3");
    }


    /**
     * Helper method for testing the doSelectInput() markup.
     * @param selectedOptions array of options that should be selected
     * @param multiple true if and only if the select is a multilple select
     * @param expectedInitValue the expected value for the ivalue attribute
     * @exception Exception if an error occurs
     */
    public void doSelectInputTestHelper(boolean[] selectedOptions,
                                        boolean multiple,
                                        String expectedInitValue)
            throws Exception {

        assertEquals("selcted option array should have 3 entries",
                     selectedOptions.length, 3);

        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();
        XFSelectAttributes atts = helper.buildSelectAttributes();

        atts.setMultiple(multiple);

        if(!multiple) {
            for(int i=0; i<selectedOptions.length; i++) {
                if(selectedOptions[i]) {
                    atts.setInitial("Value" + (i+1));
                    break;
                }
            }
        }

        SelectOptionGroup group1 = helper.addOptionGroup(atts,
                                                          "CaptionA",
                                                          "GroupA");

        helper.addOption(group1, "Caption1", "Prompt1", "Value1",
                          selectedOptions[0]);

        SelectOptionGroup group2 = helper.addOptionGroup(group1,
                                                          "CaptionB",
                                                          "GroupB");
        helper.addOption(group2, "Caption2", "Prompt2", "Value2",
                          selectedOptions[1]);
        helper.addOption(group2, "Caption3", "Prompt3", "Value3",
                          selectedOptions[2]);

        String expected = getSelectPrefix() +
                          "<select "+

                          ((null != expectedInitValue) ?
                            "ivalue=\"" + expectedInitValue + "\" " : "") +

                          "multiple=\"" + String.valueOf(multiple) + "\" " +
                          "name=\"" + atts.getName() + "\" " +
                          getSelectStyle() +
                          "title=\"" + atts.getPrompt().getText(TextEncoding.PLAIN) + "\">" +
                          "<optgroup title=\"CaptionA\">" +
                          "<option title=\"Prompt1\" " +
                          "value=\"Value1\">Caption1</option>" +
                          "<optgroup title=\"CaptionB\">" +
                          "<option title=\"Prompt2\" " +
                          "value=\"Value2\">Caption2</option>" +
                          "<option title=\"Prompt3\" " +
                          "value=\"Value3\">Caption3</option>" +
                          "</optgroup></optgroup></select>" +
                          getSelectSuffix();

        helper.runTest(protocol, buffer, atts, expected);

    }

    protected String getSelectStyle() {
        return "";
    }

    protected String getSelectPrefix() {
        return "";
    }

    protected String getSelectSuffix() {
        return "";
    }

    /**
     * Ensure that, when generating the markup for a single column grid
     * containing a pane, that Pane format alignment overrides Grid format
     * alignment and is then translated into the align attribute on the P
     * generated for grid rows.
     *
     * @throws Exception
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testSingleColumnGridPaneAlign() throws Exception {
        checkSingleColumnGrid(true,
                null,
                "center",
                "center");
        checkSingleColumnGrid(true,
                "right",
                "center",
                "center");
        checkSingleColumnGrid(true,
                "right",
                null,
                null);
    }
*/

    /**
     * test for the openDissectingPane method
     */
    public void testOpenDissectingPane() {
        privateSetUp();

        // Set the protocol to allow tranformations
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        DissectingPaneAttributes atts = setupDissectingPaneTests();

        protocol.setDissecting(true);
        protocol.openDissectingPane(buffer, atts);

        Element el = null;
        try {
            el = buffer.closeElement
                    (DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        } catch (IllegalStateException ise) {
            fail(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT +
                    " element not found.");
        }

        DissectableAreaIdentity dissectableAreaIdentity
                = ((DissectableAreaAttributes) el.getAnnotation()).
                getIdentity();
        // check that all the attributes have been written out
        assertEquals("Invalid inclusion path attribute",
                atts.getInclusionPath(),
                dissectableAreaIdentity.getInclusionPath());

        assertEquals("Invalid pane name attribute",
                atts.getDissectingPane().getName(),
                dissectableAreaIdentity.getName());
    }

    /**
     * Private helper to create the DissectingPaneAttributes for
     * testOpenDissectingPane() and testCloseDissectingPane()
     * @return DissectingPaneAttributes
     */
    private DissectingPaneAttributes setupDissectingPaneTests() {
        String inclusionPath = "TestInclusionPath";
        String paneName = "TestPaneName";
        String nextShortcut = "TestNextShardShortcut";
        String nextLink = "TestNextShardLink";
        String nextClass = "TestNextShardLinkClass";
        String prevShortcut = "TestPreviousShardShortcut";
        String prevLink = "TestPreviousShardLink";
        String prevClass = "TestPreviousShardLinkClass";
        boolean nextLinkFirst = true;

        DissectingPane pane = new DissectingPane(canvasLayout);
        pane.setName(paneName);
        pane.setAttribute(FormatConstants.NEXT_SHARD_SHORTCUT_ATTRIBUTE,
                          nextShortcut);
        pane.setAttribute(FormatConstants.NEXT_SHARD_LINK_CLASS_ATTRIBUTE,
                          nextClass);
        pane.setAttribute(FormatConstants.PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE,
                          prevShortcut);
        pane.setAttribute(FormatConstants.PREVIOUS_SHARD_LINK_CLASS_ATTRIBUTE,
                          prevClass);

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        pageContext.setDeviceLayout(runtimeDeviceLayout);
        pageContext.setCurrentPane(pane);
        DissectingPaneInstance paneInstance = new DissectingPaneInstance(
                NDimensionalIndex.ZERO_DIMENSIONS) {
            protected boolean isEmptyImpl(NDimensionalIndex index)
            {
                return false;
            }

            protected boolean ignoreImpl()
            {
                return false;
            }
        };
        TestDeviceLayoutContext dlc = new TestDeviceLayoutContext();
        dlc.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneInstance);
        dlc.setMarinerPageContext(pageContext);
        dlc.setDeviceLayout(runtimeDeviceLayout);
        dlc.initialise();
        pageContext.pushDeviceLayoutContext(dlc);

        DissectingPaneAttributes atts = new DissectingPaneAttributes();
        atts.setDissectingPane(pane);
        atts.setInclusionPath(inclusionPath);
        atts.setIsNextLinkFirst(nextLinkFirst);
        // we have to set the text for the links in the attributes since
        // ShardLinkMenuModelBuilder.buildShardLinkMenuModel() depends on these values
        // being set
        atts.setLinkText(nextLink);
        atts.setBackLinkText(prevLink);
        atts.setStyles(StylesBuilder.getEmptyStyles());
        return atts;
    }

    /**
     * tests the closeDissectingPane method. We cannot easily test that the
     * orientation of the shard links (horizontal or vertical) because
     * TestWMLRoot.setStyle and TestWMLRoot.getStyle store styles using a
     * HashMap. This means that we need to pass in the same MenuAttributes
     * object in order to retrieve the Style class. We cannot do this as the
     * protocol creates the MenuAttributes object on the fly - why? well
     * because externally this is a dissecting pane but internally the shard
     * links are represented as a menu.
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testCloseDissectingPane() throws Exception {
        privateSetUp();

        // Set the protocol to allow tranformations
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Create a device theme since menus rely on styles.
        // We can't do this in privateSetUp since in breaks WapTV test case.
        // Yuck.
        DeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        deviceContext.setDeviceLayout(runtimeDeviceLayout);
        pageContext.pushDeviceLayoutContext(deviceContext);

        DissectingPaneAttributes atts = setupDissectingPaneTests();
        DissectingPane pane = atts.getDissectingPane();

        protocol.setDissecting(true);
        protocol.openDissectingPane(buffer, atts);
        protocol.closeDissectingPane(buffer, atts);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        System.out.println(output);

        // Check that the correct markup is output
        String expected = getExpectedDissectingPaneMarkup(pane, atts);
        //System.out.println(expected);
        assertEquals("Wrong output in dissecting pane / shard link group",
                expected, output);

        // check the DISSECTABLE CONTENTS element attributes
        Element element = (Element) buffer.getRoot().getHead();
        DissectableAreaIdentity dissectableAreaIdentity
                = ((DissectableAreaAttributes) element.getAnnotation()).
                getIdentity();
        assertEquals("Invalid inclusion path attribute",
                atts.getInclusionPath(),
                dissectableAreaIdentity.getInclusionPath());
        assertEquals("Invalid pane name attribute",
                atts.getDissectingPane().getName(),
                dissectableAreaIdentity.getName());

        // check the SHARD LINK GROUP element attributes
        element = (Element) element.getNext();
        dissectableAreaIdentity
                = ((ShardLinkGroupAttributes) element.getAnnotation()).
                getDissectableArea();
        assertEquals("Invalid inclusion path attribute",
                atts.getInclusionPath(),
                dissectableAreaIdentity.getInclusionPath());
        assertEquals("Invalid pane name attribute",
                atts.getDissectingPane().getName(),
                dissectableAreaIdentity.getName());

        // Check the SHARD LINK element attributes
        element = (Element) ((Element) element.getHead()).getHead();
        assertEquals("Wrong atribute on shard link",
                ShardLinkAction.NEXT,
                ((ShardLinkAttributes) element.getAnnotation()).
                getAction());

        // Check the SHARD LINK CONDITIONAL element attributes
        element = (Element) element.getNext();
        ShardLinkConditionalAttributes slcAttributes
                = (ShardLinkConditionalAttributes) element.getAnnotation();
        assertEquals("Wrong rule on shard link conditional",
                StandardContentRules.getSeparatorRule(),
                slcAttributes.getContentRule());

        // Check the SHARD LINK element attributes
        element = (Element) element.getNext();
        assertEquals("Wrong attribute on shard link",
                ShardLinkAction.PREVIOUS,
                ((ShardLinkAttributes) element.getAnnotation()).
                getAction());
    }
*/

    /**
     * Returns the expected markup for testCloseDissectingPane(). This allows
     * subclasses to override this method as they may produce different markup
     * @param pane The DissectingPane
     * @param attr The DissectingPaneAttributes
     * @return String
     */
    protected String getExpectedDissectingPaneMarkup(DissectingPane pane,
                                                     DissectingPaneAttributes attr) {
        String expected =
          "<" + DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT + "/>" +
          "<" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">" +
            "<p mode=\"wrap\">" +
              "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
                "<a href=\"" + DissectionConstants.URL_MAGIC_CHAR + "\">" +
                attr.getLinkText() +
                "</a>" +
              "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
              "<" + DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT + ">" +
                '\u00a0' +
                "</" + DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT + ">" +
              "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
                "<a href=\"" + DissectionConstants.URL_MAGIC_CHAR +"\">" +
               attr.getBackLinkText() +
                "</a>" +
              "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
            "</p>" +
          "</" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">";
        return expected;
    }

    /**
     * Test the openAction method
     */
    public void testOpenAction() {
        privateSetUp();

        TestMarinerPageContext mpc = new TestMarinerPageContext();
        mpc.pushRequestContext( new TestMarinerRequestContext());

        ProtocolsConfiguration config = new ProtocolsConfiguration();
        config.setWmlPreferredOutputFormat("wmlc");
        Volantis bean = new Volantis();
        bean.setProtocolsConfiguration(config);
        mpc.setVolantis(bean);

        /**
         * Inner class that may be used to iterate through tests in the
         * specified order.
         */
        class Values {
            XFActionAttributes attributes;
            boolean inline = false;
            boolean supportsAccessKeys = false;
            String expected = null;

            Values(boolean inline,
                   boolean supportsAccessKeys,
                   String expected) {
                this(inline, supportsAccessKeys, null, expected);
            }
            Values(boolean inline,
                   boolean supportsAccessKeys,
                   String shortcut,
                   String expected) {
                this(inline, supportsAccessKeys, shortcut, null, null, expected);
            }
            Values(boolean inline,
                   boolean supportsAccessKeys,
                   int tabIndex,
                   String expected) {
                this(inline, supportsAccessKeys, null, expected);
                attributes.setTabindex(Integer.toString(tabIndex));
            }

            Values(boolean inline,
                   boolean supportsAccessKeys,
                   String shortcut,
                   String title,
                   String caption,
                   String expected) {

                this.inline = inline;
                this.supportsAccessKeys = supportsAccessKeys;
                this.expected = expected;
                attributes = new XFActionAttributes();
                attributes.setStyles(StylesBuilder.getInitialValueStyles());
                attributes.setShortcut(new LiteralTextAssetReference(shortcut));
                attributes.setCaption(new LiteralTextAssetReference(caption));
                attributes.setTitle(title);
            }
        }

        List tests = new ArrayList();

        // Test anchor generation (not inline)
        tests.add(new Values(false, false,
                             "<do name=\"" + mpc.generateWMLActionID() +
                             "\" type=\"accept\"/>"));

        // Test the tabindex addition
        tests.add(new Values(false, false, 8,
                                     "<do name=\"" + mpc.generateWMLActionID() +
                                     "\" type=\"accept\" tabindex=\"8\"/>"));
        // Test anchor generation (inline)
        tests.add(new Values(true, false,
                             "<anchor/>"));

        // Test shortcut accesskey attribute (accesskeys not allowed)
        tests.add(new Values(true, false, "shortcut",
                             "<anchor/>"));

        // Test shortcut accesskey attribute (accesskeys allowed)
        tests.add(new Values(true, true, "shortcut",
                             "<anchor accesskey=\"shortcut\"/>"));

        // Test caption generation (not inline)
        String caption = "Not Inline Caption";
        tests.add(new Values(false, false, null, null, caption,
                             "<do label=\"" + caption + "\" name=\"" +
                             mpc.generateWMLActionID() + "\" " +
                             "type=\"accept\"/>"));

        // Test caption generation (inline)
        caption = "Inline Caption";
        tests.add(new Values(true, false, null, null, caption,
                             "<anchor>" + caption + "</anchor>"));

        // Test caption generation (inline and acceskeys allowed)
        caption = "Inline Caption";
        tests.add(new Values(true, true, "shortcut", null, caption,
                             "<anchor accesskey=\"shortcut\">" + caption +
                             "</anchor>"));

        // Test title generation (not inline)
        String title = "Not Inline Title";
        tests.add(new Values(false, false, null, title, null,
                             "<do label=\"" + title + "\" name=\"" +
                             mpc.generateWMLActionID() + "\" " +
                             "type=\"accept\"/>"));

        // Test title generation (inline)
        title = "Inline Title";
        tests.add(new Values(true, false, null, title, null,
                             "<anchor title=\"" + title + "\">" + title +
                             "</anchor>"));

        title = "Title";
        caption = "Caption";
        tests.add(new Values(true, true, "shortcut", title, caption,
                             "<anchor accesskey=\"shortcut\" title=\"" +
                             title + "\">" + caption + "</anchor>"));
        // Iterate over the tests and an check the expected result equals
        // the actual result.
        for (int i = 0; i < tests.size(); i++) {
            Values values = (Values) tests.get(i);
            buffer = new TestDOMOutputBuffer();

            protocol.setMarinerPageContext(mpc);
            testable.setSupportsAccessKeyAttribute(values.supportsAccessKeys);
            //@todo need to reenable this check?
            //assertEquals(protocol.supportsAccessKeyAttribute(), values.supportsAccessKeys);

            Element result = protocol.openAction(buffer,
                                                 values.attributes,
                                                 values.inline);
            assertNotNull("Result should not be null", result);
            try {
                String actual = DOMUtilities.toString(result, protocol.getCharacterEncoder());
                //System.out.println("Result is " +  actual);
                assertEquals("Test failed for test " + (i + 1) + " of " + tests.size() + ":",
                             "\n" + transformMarkup(values.expected, protocol),
                             "\n" + actual);
            } catch (Exception e) {
                e.printStackTrace();
                fail("Unexpected exception.");
            }
        }
    }

    /**
     * Test the entering of event for WMLRoot.
     * @throws Exception
     */
    public void testAddEnterEvents() throws Exception {
        privateSetUp();
        com.volantis.mcs.protocols.CanvasAttributes attributes =
                new com.volantis.mcs.protocols.CanvasAttributes();
        protocol.addEnterEvents(buffer, attributes);
        assertEquals("Buffer should be unchanged",
                     "",
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));

        EventAttributes events = attributes.getEventAttributes (false);
        events.reset();
        events.setEvent(EventConstants.ON_ENTER_BACKWARD, "Test event backward");
        events.setEvent(EventConstants.ON_ENTER_FORWARD, "Test event forward");
        protocol.addEnterEvents(buffer, attributes);
        String expected = transformMarkup(
                "<root><onevent type=\"onenterforward\">Test event forward" +
                "</onevent><onevent type=\"onenterbackward\">" +
                "Test event backward</onevent></root>", protocol);
        assertEquals("Buffer should be as",
                     expected,
                     "<root>" + DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()) +
                     "</root>");
    }

    /**
     * Test the adding of event for an element.
     * @throws Exception
     */
    public void testAddOnEventElement() throws Exception {
        privateSetUp();
        com.volantis.mcs.protocols.CanvasAttributes attributes =
                new com.volantis.mcs.protocols.CanvasAttributes();
        protocol.addOnEventElement(buffer, attributes, "testEvent", 0);
        assertEquals("Buffer should be unchanged",
                     "",
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));

        EventAttributes events = attributes.getEventAttributes (false);
        events.reset();
        events.setEvent(EventConstants.ON_ENTER_BACKWARD, "Test event backward");
        protocol.addOnEventElement(buffer, attributes, "testEvent",
                                   EventConstants.ON_ENTER_BACKWARD);
        String expected = transformMarkup(
                "<onevent type=\"testEvent\">Test event backward</onevent>",
                protocol);
        assertEquals("Buffer should as",
                     expected,
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));
    }
    /**
     * Test the adding of an event with and without a valid task string.
     * @throws Exception
     */
    public void testAddOnEventElementTask() throws Exception {

        privateSetUp();
        com.volantis.mcs.protocols.CanvasAttributes attributes =
                new com.volantis.mcs.protocols.CanvasAttributes();
        protocol.addOnEventElement(buffer, attributes, "testEvent", null);
        assertEquals("Buffer should be unchanged",
                     "",
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));

        Script script = DOMScript.createScript(
                new LiteralScriptAssetReference("task"));
        protocol.addOnEventElement(buffer, attributes, "testEvent", script);
        String expected = transformMarkup(
                "<onevent type=\"testEvent\">task</onevent>",
                protocol);
        assertEquals("Buffer should as",
                     expected,
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));
    }

    /**
     * Test the addActionReset method.
     */
    public void testAddActionResetBasic() throws Exception {
        doTestAddActionReset("<refresh/>", null);
    }

    public void testAddActionResetFormAttributes() throws Exception {
        XFTextInputAttributes field = new XFTextInputAttributes();
        doTestAddActionReset("<refresh/>", field);
    }

    public void testAddActionResetFormAttributesInitialValue() throws Exception {
        XFTextInputAttributes field = new XFTextInputAttributes();
        field.setName("Name");
        field.setEntryContainerInstance(new PaneInstance(null));

        String expected = "<refresh><setvar name=\"Name\" value=\"\"/></refresh>";
        doTestAddActionReset(expected, field);
    }

    /**
     * Return the dom transformed string represention of the markup passed in
     * via the the expected parameter.
     *
     * @param  markup   the string that contains the markup to be read into a
     *                  dom and returned as a string again.
     * @param  protocol the protocol used for the DOM transformation.
     * @return          the dom transformed string represention of the markup
     *                  passed in via the the expected parameter.
     *
     * @todo explain that the purpose of this is to "normalise" markup.
     * @todo move this to DOMProtocolTestCaseAbstract.
     */
    protected String transformMarkup(String markup, VolantisProtocol protocol)
            throws Exception {

        return DOMUtilities.toString(
                DOMUtilities.read(DOMUtilities.getReader(), markup),
                protocol.getCharacterEncoder());
    }

    protected void doTestAddActionReset(String expected,
                                        XFTextInputAttributes field)
            throws Exception {
        privateSetUp();
        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        XFFormAttributes formAttributes = new XFFormAttributes();
        attributes.setFormAttributes(formAttributes);

        if (field != null) {
            formAttributes.addField(field);
        }

        protocol.addActionReset(buffer, attributes);
        assertEquals(transformMarkup(expected, protocol),
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));
    }

    public void testAddActionResetFormAttributesInitialValueValid() throws Exception {
        privateSetUp();

        XFActionAttributes attributes = new XFActionAttributes();;
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        XFFormAttributes formAttributes = new XFFormAttributes();
        attributes.setFormAttributes(formAttributes);

        XFTextInputAttributes field = new XFTextInputAttributes();
        field.setInitial("InitialValue");
        field.setName("Name");
        field.setEntryContainerInstance(new PaneInstance(null));
        formAttributes.addField(field);
        protocol.addActionReset(buffer, attributes);
        String expected = transformMarkup(
                "<refresh><setvar name=\"Name\" value=\"InitialValue\"/>" +
                "</refresh>",
                protocol);
        assertEquals(expected,
                     DOMUtilities.toString(
                             buffer.getRoot(), protocol.getCharacterEncoder()));
    }

    /**
     * Tests whether we support native markup in this protocol
     */
    public void testSupportsNativeMarkup()
    {
        assertTrue("Protocol should support nativemarkup",
                    protocol.supportsNativeMarkup());
    }

    /**
     * Tests writeOpenNativeMarkup method
     * @throws java.io.IOException
     */
    protected void doWriteOpenNativeMarkupTest(
        OutputBufferFactory factory,
        TestMarinerPageContext pageContext,
        PageHead pageHead,
        DeviceLayoutContext dlc) throws Exception {
        NativeMarkupAttributes attributes = new NativeMarkupAttributes();

        // Allow the superclass version to do initial tests
        super.doWriteOpenNativeMarkupTest(factory, pageContext, pageHead, dlc);

        // @todo why are these reset?
//        pageHead.setMarinerPageContext(pageContext);
        pageHead.setOutputBufferFactory(factory);

        // Perform the WML-specific additional tests
        attributes.setTargetLocation("wml.deck.head");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for head",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
                    pageHead.getHead());

        attributes.resetAttributes();
        attributes.setTargetLocation("wml.deck.template");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for head",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
                pageHead.getBuffer(WMLRoot.PAGE_TEMPLATE_BUFFER_NAME, false));

        attributes.resetAttributes();
        attributes.setTargetLocation("wml.card.timer");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for timer",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
           dlc.getOutputBuffer(NativeMarkupAttributes.WML_CARD_TIMER, false));

        attributes.resetAttributes();
        attributes.setTargetLocation("wml.card.onevent");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for " +
                     "onevent",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
           dlc.getOutputBuffer(
               NativeMarkupAttributes.WML_CARD_ONEVENT, false));

        attributes.resetAttributes();
        attributes.setTargetLocation("wml.card.beforebody");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for " +
                     "beforebody",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
           dlc.getOutputBuffer(
               NativeMarkupAttributes.WML_CARD_BEFOREBODY, false));
    }

    protected void doWriteCloseNativeMarkupTest(
        TestMarinerPageContext pageContext,
        PageHead pageHead,
        DeviceLayoutContext dlc,
        DOMOutputBuffer buffer) throws Exception {
        NativeMarkupAttributes attributes = new NativeMarkupAttributes();

        super.doWriteCloseNativeMarkupTest(pageContext,
                                           pageHead,
                                           dlc,
                                           buffer);

        attributes.setTargetLocation("wml.deck.head");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for head",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
                    pageHead.getHead());
        protocol.writeCloseNativeMarkup(attributes);
        // Check that output buffers have been removed from stack
        assertEquals("after closeNativeMarkup wrong output buffer on stack",
                    buffer, pageContext.getCurrentOutputBuffer());

        attributes.resetAttributes();
        attributes.setTargetLocation("wml.deck.template");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for head",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
                pageHead.getBuffer(WMLRoot.PAGE_TEMPLATE_BUFFER_NAME, false));
        protocol.writeCloseNativeMarkup(attributes);
        // Check that output buffers have been removed from stack
        assertEquals("after closeNativeMarkup wrong output buffer on stack",
                    buffer, pageContext.getCurrentOutputBuffer());

        attributes.resetAttributes();
        attributes.setTargetLocation("wml.card.timer");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for timer",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
           dlc.getOutputBuffer(NativeMarkupAttributes.WML_CARD_TIMER, false));
        protocol.writeCloseNativeMarkup(attributes);
        // Check that output buffers have been removed from stack
        assertEquals("after closeNativeMarkup wrong output buffer on stack",
                    buffer, pageContext.getCurrentOutputBuffer());

        attributes.resetAttributes();
        attributes.setTargetLocation("wml.card.onevent");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for onevent",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
           dlc.getOutputBuffer(NativeMarkupAttributes.WML_CARD_ONEVENT, false));
        protocol.writeCloseNativeMarkup(attributes);
        // Check that output buffers have been removed from stack
        assertEquals("after closeNativeMarkup wrong output buffer on stack",
                    buffer, pageContext.getCurrentOutputBuffer());

        attributes.resetAttributes();
        attributes.setTargetLocation("wml.card.beforebody");
        protocol.writeOpenNativeMarkup(attributes);
        assertEquals("native markup written to wrong output buffer for beforebody",
                    protocol.getMarinerPageContext().getCurrentOutputBuffer(),
           dlc.getOutputBuffer(NativeMarkupAttributes.WML_CARD_BEFOREBODY, false));
        protocol.writeCloseNativeMarkup(attributes);
        // Check that output buffers have been removed from stack
        assertEquals("after closeNativeMarkup wrong output buffer on stack",
                    buffer, pageContext.getCurrentOutputBuffer());
    }

    /**
     * Sets up the environment to test various methods that need a fully
     * initialized device layout.
     *
     * @return TestDeviceLayoutContext a test version of DeviceLayoutContext
     */
    private TestDeviceLayoutContext setUpDeviceLayoutContext()
    {
        OutputBufferFactory factory = new TestDOMOutputBufferFactory();
        //pageHead.setMarinerPageContext(pageContext);
        pageHead.setOutputBufferFactory(factory);

        CanvasLayout canvasLayout = new CanvasLayout();
        Pane pane = new Pane(canvasLayout);
        pane.setName("pane");

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);

        pageContext.setDeviceLayout(runtimeDeviceLayout);
        pageContext.setCurrentPane(pane);
        PaneInstance paneInstance = new TestPaneInstance();
        TestDeviceLayoutContext dlc = new TestDeviceLayoutContext();
        dlc.setFormatInstance(pane, NDimensionalIndex.ZERO_DIMENSIONS, paneInstance);
        dlc.setMarinerPageContext(pageContext);
        dlc.setDeviceLayout(runtimeDeviceLayout);
        dlc.initialise();
        pageContext.pushDeviceLayoutContext(dlc);
        return dlc;
    }

    /**
     * Test openCard method.
     */
    public void testOpenCard() throws Exception
    {
        privateSetUp();
        pageContext.pushOutputBuffer(buffer);
        setUpDeviceLayoutContext();

        CanvasAttributes attributes = new CanvasAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());
        protocol.openCard(buffer, attributes);

        // Output buffer on stack should now be buffer
        assertEquals("wrong output buffer at top of stack",
                            buffer, pageContext.getCurrentOutputBuffer());
        assertEquals("wrong element added to stack", "card",
                    buffer.getCurrentElement().getName());
    }

    /**
     * Test closeCard method.
     */
    public void testCloseCard() throws Exception
    {
        privateSetUp();
        pageContext.pushOutputBuffer(buffer);
        setUpDeviceLayoutContext();

        CanvasAttributes attributes = new CanvasAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        protocol.openCard(buffer, attributes);
        protocol.closeCard(buffer, attributes);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("Wrong output in card element", "<card" +
                getExpectedCardAttributes() +
                "/>", output);
    }

    protected String getExpectedCardAttributes() {
        return "";
    }

    /**
     * Test openLayout
     */
/* XDIME-CP Needs fixing, now that emulation is done later
    public void testOpenLayout() throws Exception {
        privateSetUp();
        pageContext.pushOutputBuffer(buffer);
        pageContext.setTagEmphasisEmulation(true);
        TestDeviceLayoutContext dlc = setUpDeviceLayoutContext();

        // Shove some content in the card. We add an onevent attribute to
        // the canvas in order to recreate the jsp that demonstrates the bug.
        CanvasAttributes canvasAttributes = new CanvasAttributes();
        canvasAttributes.setTitle("title");
        EventAttributes eventAttributes =
                            canvasAttributes.getEventAttributes(true);
        eventAttributes.setEvent(EventConstants.ON_TIMER,
                                "<go href=\"http://www.my.com\"/>");
        protocol.openCard(buffer, canvasAttributes);

        // Save the insertion point before we close the card.
        buffer.saveInsertionPoint();
        protocol.closeCard(buffer, canvasAttributes);

        // Shove some content in the relevant buffers to be appending before
        // the closing card tag.
        DOMOutputBuffer oneventBuffer = (DOMOutputBuffer)dlc.getOutputBuffer(
                            NativeMarkupAttributes.WML_CARD_ONEVENT, true);
        DOMOutputBuffer timerBuffer = (DOMOutputBuffer)dlc.getOutputBuffer(
                            NativeMarkupAttributes.WML_CARD_TIMER, true);
        DOMOutputBuffer beforeBodyBuffer = (DOMOutputBuffer)dlc.getOutputBuffer(
                            NativeMarkupAttributes.WML_CARD_BEFOREBODY, true);
        oneventBuffer.addStyledElement("onevent", canvasAttributes);
        timerBuffer.addStyledElement("timer", canvasAttributes);
        beforeBodyBuffer.addStyledElement("beforebody", canvasAttributes);

        // Restore the insertion point
        buffer.restoreInsertionPoint();

        // Append the buffers.
        LayoutAttributes layoutAttributes = new LayoutAttributes();
        protocol.openLayout(buffer, layoutAttributes);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("Wrong output in card element",
           getExpectedTestOpenLayoutResult(), output);
    }
*/

    /**
     * Return the expected result when testOpenLayout() is run. We get
     * different results in WMLRoot and WapTV5_WMLVersion1_3
     * @return String
     */
    protected String getExpectedTestOpenLayoutResult() {
        return "<card title=\"title\">" +
                 "<onevent type=\"ontimer\">" +
                   "<go href=\"http://www.my.com\"/>" +
                 "</onevent>" +
                 "<onevent/>" +
                 "<timer/>" +
// TODO: re-enable this when we fix emulate emphasis
//                 "<p align=\"center\">" +
//                   "<devicePolicyValue>" +
//                     "devicePolicyValuetitledevicePolicyValue" +
//                   "</devicePolicyValue>" +
//                 "</p>" + 
                 "<beforebody/>" +
               "</card>";
    }

    public void testDoMeta() throws Exception {
       privateSetUp();

       MetaAttributes attributes = new MetaAttributes();
       attributes.setContent("Test & encoding");
       attributes.setHttpEquiv("refresh");
       attributes.setName("URL");

       protocol.doMeta(buffer, attributes);

       // Check the meta tag and attributes
       Element nullElement = buffer.popElement();
       Element element = (Element) nullElement.getHead();

       // We need to get the content string from the buffer as it
       // isn't encoded in the element.
       String result = bufferToString(buffer);
       int start = result.indexOf("content");
       int end = result.indexOf("ing\"", start);
       assertEquals("content=\"Test &amp; encoding\"", result.substring(start, end + 4));
       // For WML, there may be more than one meta generated, hence this loop.
       do {
           assertEquals("meta", element.getName());
           if(element.getAttributeValue("http-equiv") != null) {
               assertEquals("refresh", element.getAttributeValue("http-equiv"));
           }
           if(element.getAttributeValue("name") != null) {
               assertEquals("URL", element.getAttributeValue("name"));
           }
       } while((element = (Element) element.getNext()) != null);

    }

    /**
     * Test that get packaging type returns the expected value.
     */
    public void testGetPackagingType() {
        assertEquals("multipart/mixed", protocol.getPackagingType());
    }

    /**
     * Tests the addition of the emptyok attribute to an input type if the
     * the input field validation text beings with lower case
     */
    public void testAddTextInputValidation() {
        privateSetUp();

        // If no validation is specified then no validation attributes are
        // added.
        checkTextInputValidation(null, null, null);

        // If the validation allows empty field then expect emptyok attribute
        // to be set and the format attribute to be set.
        checkTextInputValidation("n:####", "true", "NNNN");

        // If the validation does not allow empty fields then expect emptyok
        // attribute to not be set and the format attribute to be set.
        checkTextInputValidation("N:####", null, "NNNN");
    }

    /**
     * Check that the text input validation works from styles correctly.
     *
     * @param format          The input format that is stored in a style.
     * @param expectedEmptyOk The expected value of emptyok attribute.
     * @param expectedFormat  The expected value of format attribute.
     */
    protected void checkTextInputValidation(
            final String format, final String expectedEmptyOk,
            final String expectedFormat) {

        StrictStyledDOMHelper helper = new StrictStyledDOMHelper();

        XFTextInputAttributes attributes = new XFTextInputAttributes();
        String css;
        if (format == null) {
            css = "";
        } else {
            css = "mcs-input-format:\"" + format + "\"";
        }

        attributes.setStyles(StylesBuilder.getCompleteStyles(
                css));

        Element element = domFactory.createElement("input");
        protocol.addTextInputValidation(element, attributes);

        String output = helper.render(element);
        String expectedMarkup = "<input";
        if (expectedEmptyOk != null) {
            expectedMarkup += " emptyok=\"" + expectedEmptyOk + "\"";
        }
        if (format != null) {
            expectedMarkup += " format=\"" + expectedFormat + "\"";
        }
        expectedMarkup += "/>";
        assertEquals(expectedMarkup, output);
    }

    /**
     * Test the image element. If there is an AssetURLSuffix set then it should
     * be appended to the image URL.
     */
    public void testDoImageNoSuffix() throws ProtocolException {

        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        
        ImageAttributes attrs = new ImageAttributes();
        attrs.setStyles(StylesBuilder.getInitialValueStyles());

        attrs.setSrc("http://www.images.com/test/image.jpg");
        protocol.doImage(buffer,attrs);

        Element root = buffer.getCurrentElement();
        Element el = (Element) root.getHead();
        assertNotNull("Image element should exist.", el );
        assertEquals("Image element should exist", "img", el.getName());
        assertEquals("Incorrect src attribute",
                "http://www.images.com/test/image.jpg", el.getAttributeValue("src"));
    }

    public void testDoImageSuffix() throws ProtocolException {

        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        
        ImageAttributes attrs = new ImageAttributes();
        attrs.setStyles(StylesBuilder.getInitialValueStyles());

        attrs.setSrc("http://www.images.com/test/image.jpg");
        attrs.setAssetURLSuffix("?name=fred");
        protocol.doImage(buffer,attrs);

        Element root = buffer.getCurrentElement();
        Element el = (Element) root.getHead();
        assertNotNull("Image element should exist.", el );
        assertEquals("Image element should exist", "img", el.getName());
        assertEquals("Incorrect src attribute",
                "http://www.images.com/test/image.jpg?name=fred", 
                el.getAttributeValue("src"));
    }

    // javadoc inherited
    protected void templateTestDoMenuHorizontal(StringBuffer buf,
                                                MenuItem menuItem,
                                                boolean hasNext) {
        buf.append("<a href=\"").append(menuItem.getHref()).append("\">");
        buf.append(menuItem.getText()).append("</a>");
        if (hasNext) {
            buf.append("&#160;");
        }
    }

    /**
     * Test getXFImplicitAttributesValue with no attributes
     */
    public void testGetXFImplicitAttributesValueNoAttributes() {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        assertNull("should be null when value and clientVariableName not supplied",
                protocol.getXFImplicitAttributesValue(attributes));
    }

    /**
     * Test getXFImplicitAttributesValue with just a value attribute
     */
    public void testGetXFImplicitAttributesValueValueAttribute() {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        // Test just value set
        attributes.setValue("value");
        assertEquals("wrong value returned", "value",
                protocol.getXFImplicitAttributesValue(attributes));
    }

    /**
     * Test getXFImplicitAttributesValue with just clientVariableName
     */
    public void testGetXFImplicitAttributesValueClientVariableName() {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        // Test just clientVariableName set
        attributes.setClientVariableName("clientVariableName");
        assertEquals("wrong clientVariableName returned",
                WMLVariable.WMLV_NOBRACKETS + "clientVariableName" +
                WMLVariable.WMLV_NOBRACKETS,
                protocol.getXFImplicitAttributesValue(attributes));
    }

    /**
     * Test getXFImplicitAttributesValue with both clientVariableName
     * and value attributes
     */
    public void testGetXFImplicitAttributesValueBothAttributes() {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        // Test that clientVariableName takes precedence over value
        attributes.setClientVariableName("clientVariableName");
        attributes.setValue("value");
        assertEquals("wrong clientVariableName returned",
                WMLVariable.WMLV_NOBRACKETS + "clientVariableName" +
                WMLVariable.WMLV_NOBRACKETS,
                protocol.getXFImplicitAttributesValue(attributes));
    }

    /**
     * Test addPostField puts the correct element in the output buffer when
     * we pass it no XFImplicit attributes.
     */
    public void testAddPostFieldNoAttributes() throws Exception {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        // Test with no attributes
        protocol.addPostField(buffer, attributes);
    }

    /**
     * Test addPostField puts the correct element in the output buffer when
     * we pass it just a value in the XFImplicit attributes.
     */
    public void testAddPostFieldValueAttributes() throws Exception {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        buffer = new TestDOMOutputBuffer();
        document = domFactory.createDocument();

        attributes.setName("name");
        attributes.setValue("value");
        protocol.addPostField(buffer, attributes);
        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("Wrong output in postfield element",
           "<postfield name=\"name\" value=\"value\"/>", output);
    }

    /**
     * Test addPostField puts the correct element in the output buffer when
     * we pass it just a clientVariableName XFImplicit attributes.
     */
    public void testAddPostFieldClientVariableName() throws Exception {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        buffer = new TestDOMOutputBuffer();
        document = domFactory.createDocument();

        attributes.setName("name");
        attributes.setClientVariableName("clientVariableName");
        protocol.addPostField(buffer, attributes);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        // Here I replaced $ with WMLVariable.WML_NOBRACKETS -because for variable encoding
        // $ character is not expected. Modification was possible because main role
        // of test is to examine if XFImplicitAttributes is build correctly 
        assertEquals("Wrong output in postfield element",
            "<postfield name=\"name\" value=\""+WMLVariable.WMLV_NOBRACKETS+"clientVariableName"
            +WMLVariable.WMLV_NOBRACKETS+"\"/>",output );
    }

    /**
     * Test addPostField puts the correct element in the output buffer when
     * we pass it clientVariableName and value XFImplicit attributes.
     */
    public void testAddPostFieldClientVariableNameAndValueAttributes()
             throws Exception {
        privateSetUp();
        XFImplicitAttributes attributes = new XFImplicitAttributes();

        // Test that clientVariableName takes precedence over value
        buffer = new TestDOMOutputBuffer();
        document = domFactory.createDocument();

        attributes.setName("name");
        attributes.setValue("value");
        attributes.setClientVariableName("clientVariableName");
        protocol.addPostField(buffer, attributes);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        // Here I replaced $ with WMLVariable.WML_NOBRACKETS -because for variable encoding
        // $ character is not expected. Modification was possible because main role
        // of test is to examine if XFImplicitAttributes is build correctly 
        // in this case if clientVariableName takes precedence over value 
        // as it was written above
        assertEquals("Wrong output in postfield element",
           "<postfield name=\"name\" value=\""+WMLVariable.WMLV_NOBRACKETS+"clientVariableName"+
            WMLVariable.WMLV_NOBRACKETS+"\"/>",output );
    }

    /**
     * Get the name of the menu element for the protocol under test.
     * @return The name of the menu element for the protocol under test.
     */
    protected String getMenuElementName() {
        return "";
    }

    /**
     * Test the horizontal rule
     */
    public void testDoHorizontalRuleEmulationOff() throws Exception {
        privateSetUp();
        assertFalse("Emulation should be off", protocol.emulateHorizontalTag);

        protocol.doHorizontalRule(buffer, null);

        document.addNode(buffer.getRoot());
        String output = DOMUtilities.toString(
                document, protocol.getCharacterEncoder());
        assertEquals("Wrong output in doHorizontalRule method", "", output);
    }

    /**
     * Test the horizontal rule
     */
    public void testDoHorizontalRuleEmulationOn() throws Exception {
        privateSetUp();
        protocol.emulateHorizontalTag = true;
        assertTrue("Emulation should be on", protocol.emulateHorizontalTag);

        pageContext.setDevicePolicyValue("charactersx", "10");
        HorizontalRuleAttributes attrs = new HorizontalRuleAttributes();
        final StylingFactory factory = StylingFactory.getDefaultInstance();
        attrs.setStyles(factory.createStyles(factory.createPropertyValues(
                StylePropertyDetails.getDefinitions())));
        protocol.doHorizontalRule(buffer, attrs);
        document.addNode(buffer.getRoot());
        StyledDOMTester tester = new StyledDOMTester();
        String output = tester.render(document);
        String expected =
                "<BLOCK>" +
                    "--------" +
                "</BLOCK>";
        assertEquals("Wrong output in doHorizontalRule method",
                tester.normalize(expected), output);
    }

    protected void checkFragmentLinkRendererContext(
            FragmentLinkRendererContext context) {
        assertTrue(context instanceof WMLFragmentLinkRendererContext);
    }

    // javadoc inherited
    protected String expectedDefaultMimeType() {
        return "text/vnd.wap.wml";
    }

    protected void checkRenderAltText(String altText, DOMOutputBuffer buffer) {
        // WML just adds the text in without a containing tag.
        Element root = buffer.getCurrentElement();
        checkTextEquals(altText, root.getHead());
    }

    /**
     * Ensure that valid image tags are generated 
     * @throws ProtocolException
     */ 
    public void testImageValid() throws ProtocolException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        
        // Initialise our protocol attributes with an id attribute.
        ImageAttributes attributes = new ImageAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        attributes.setLocalSrc(true);
        attributes.setSrc("myImage.jpg");
        attributes.setAltText("Alternate Text");

        protocol.doImage(buffer, attributes);
        
        // valid src and alt generates <img src="url" alt="text" />
        Element root = buffer.getCurrentElement();
        Element element = (Element) root.getHead();

        DOMAssertionUtilities.assertElement("img", element);
        DOMAssertionUtilities.assertAttributeEquals("localsrc","myImage.jpg",element);
        DOMAssertionUtilities.assertAttributeEquals("alt","Alternate Text",element);
    }

    /**
     * Ensure that valid image tags are generated when there is no source
     * @throws ProtocolException
     */ 
    public void testImageNoSrc() throws ProtocolException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        
        String expected = "Alternate Text";
        
        // Initialise our protocol attributes with an id attribute.
        ImageAttributes attributes = new ImageAttributes();
        attributes.setLocalSrc(true);
        attributes.setSrc(null);
        attributes.setAltText(expected);

        protocol.doImage(buffer, attributes);
        
        // No src with alt text generates <span>text</span>
        Element root = buffer.getCurrentElement();
        Text altText = (Text)root.getHead();
        String txtAltText = new String(altText.getContents(), 
                0, expected.length());
        assertEquals("Incorrect Text", expected, txtAltText);
    }

    /**
     * Ensure that no output is generated when there is no source or alt text
     * @throws ProtocolException
     */ 
    public void testImageNoSrcNoAlt() throws ProtocolException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        
        // Initialise our protocol attributes with an id attribute.
        ImageAttributes attributes = new ImageAttributes();
        attributes.setLocalSrc(true);
        attributes.setSrc(null);
        attributes.setAltText("    ");

        protocol.doImage(buffer, attributes);
        
        // No src and whitespace text generates no output
        Element root = buffer.getCurrentElement();
        Element empty = (Element) root.getHead();
        assertNull("No output should be generated", empty);
    }
    
    
    /**
     * Force the underlying protocols to test for a valid DTD/name/code.
     */
    public void testProtocolHasCorrectDTD() throws Exception {
        //fail("This test should be overridden.");
    }

    /**
     * Tests that a row of a spatial format iterator is contained within
     * &lt;p&gt; tags.
     * @todo This test should be moved to DOMProtocolTestAbstract so that
     * protocol test subclasses can override it where necessary. Moving it to
     * the superclass results in 18 test case failures because some protocols
     * expect there to be no containing tag or expect a &lt;tr&gt; container.
     */
    public void testSpatialFormatIteratorRowContainingTag() throws Exception {
        privateSetUp();

        MarinerRequestContext requestContext
                = new TestMarinerRequestContext();
        TestMarinerPageContext context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        protocol.setMarinerPageContext(context);

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        context.setCurrentOutputBuffer(buffer);

        SpatialFormatIteratorAttributes attributes =
                new SpatialFormatIteratorAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        Pane pane = new Pane(canvasLayout);
        attributes.setFormat(pane);

        // Open and then close the row.
        protocol.writeOpenSpatialFormatIteratorRow(attributes);
        protocol.writeCloseSpatialFormatIteratorRow(attributes);

        // Retrieve and test the row's containing tag.
        String containingTag = DOMUtilities.toString(buffer.getRoot());
        assertEquals("Spatial iterator row should be contained within <p> " +
                "tags", containingTag, "<BLOCK/>");
    }

    /**
     * Prove that the column attribute is not added to the element by the
     * openGrid method
     */
    public void testOpenGrid() {
        privateSetUp();
        GridAttributes attributes = new GridAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        attributes.setFormat(new Form(new CanvasLayout()));
        buffer.addStyledElement("table", attributes);
        protocol.openGrid(buffer, attributes);
        Element e = buffer.getCurrentElement();
        assertNull(e.getAttributeValue("columns"));
    }

    /**
     * Prove that the column attribute is not added to the element by the
     * openTable method
     */
    public void testOpenTable() {
        privateSetUp();
        TableAttributes attributes = new TableAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());

        buffer.addStyledElement("table", attributes);
        protocol.openTable(buffer, attributes);
        Element e = buffer.getCurrentElement();
        assertNull(e.getAttributeValue("columns"));
    }

    public void testRenderAltText() throws Exception {
// XDIME-CP Needs fixing, now that emulation is done later
    }

    /**
     * Tests the correct functioning of captions with caption-side: top
     *
     * @throws Exception if an error occurs
     */
    public void testCaptionTop() throws Exception {
        doTestCaption(CaptionSideKeywords.TOP, true);
    }

    /**
     * Tests the correct functioning of captions with caption-side: bottom
     *
     * @throws Exception if an error occurs
     */
    public void testCaptionBottom() throws Exception {
        doTestCaption(CaptionSideKeywords.BOTTOM, false);
    }

    /**
     * Tests that for a specified caption side value, the caption appears in
     * the expected location relative to the table.
     *
     * @param captionSide The caption side style value
     * @param captionExpectedBeforeTable True if the caption should appear
     *                                   before the table, false otherwise
     * @throws Exception if an error occurs
     */
    private void doTestCaption(StyleKeyword captionSide,
                              boolean captionExpectedBeforeTable)
            throws Exception {
        privateSetUp();

        TableAttributes tableAttributes = new TableAttributes();
        tableAttributes.setStyles(StylesBuilder.getInitialValueStyles());
        CaptionAttributes captionAttributes = new CaptionAttributes();
        Styles captionStyles = StylesBuilder.getInitialValueStyles();
        captionStyles.getPropertyValues().setComputedValue(
                StylePropertyDetails.CAPTION_SIDE, captionSide);
        captionAttributes.setStyles(captionStyles);

        // Write an empty table with a caption
        protocol.openTable(buffer, tableAttributes);
        protocol.openTableCaption(buffer, captionAttributes);
        buffer.writeText("caption");
        protocol.closeTableCaption(buffer, captionAttributes);
        protocol.closeTable(buffer, tableAttributes);

        assertNotNull("Two elements (table and caption) should be created",
                buffer.getRoot());
        Element firstElement = (Element) buffer.getRoot().getHead();
        assertNotNull("Two elements (table and caption) should be created",
                firstElement.getNext());
        Element secondElement = (Element) firstElement.getNext();
        assertNull("Two elements (table and caption) should be created",
                secondElement.getNext());

        String firstElementExpected = captionExpectedBeforeTable ?
                WMLConstants.BLOCH_ELEMENT : "table";
        String secondElementExpected = captionExpectedBeforeTable ?
                "table" : WMLConstants.BLOCH_ELEMENT;

        assertEquals("First element should be '" + firstElementExpected + "'",
                firstElementExpected, firstElement.getName());
        assertEquals("Second element should be '" + secondElementExpected + "'",
                secondElementExpected, secondElement.getName());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10701/1	pduffin	VBM:2005110905 Porting forward changes from 3.5

 08-Dec-05	10675/1	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10328/17	pabbott	VBM:2005110907 Align not honoured on p tags in WML

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 16-Nov-05	10333/4	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 16-Nov-05	10333/4	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 17-Nov-05	10330/4	pabbott	VBM:2005110907 Honour align with mode=nospace

 16-Nov-05	10333/4	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 17-Nov-05	10356/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 16-Nov-05	10333/4	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10278/1	ianw	VBM:2005110425 Fixed up formating/comments

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 03-Oct-05	9600/9	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 30-Sep-05	9600/6	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 29-Sep-05	9600/3	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 02-Oct-05	9590/6	schaloner	VBM:2005092204 Migrated XMLLayoutAccessor and XMLDeviceLayoutAccessor to JiBX

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 02-Oct-05	9652/2	gkoch	VBM:2005092204 Tests for layoutFormat marshaller/unmarshaller

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 23-Aug-05	9363/8	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/3	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/11	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 18-Aug-05	9007/4	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Aug-05	9187/1	tom	VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file] - second attempt

 09-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8833/5	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/2	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 17-Jun-05	8830/2	pduffin	VBM:2005042901 Merging from 3.2.3 - Added support for specifying symbols, punctuation marks, or numbers in a text format

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 09-Jun-05	8665/7	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 08-Jun-05	7997/2	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 07-Jun-05	8637/1	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 16-Mar-05	7372/5	emma	VBM:2005031008 Modifications after review

 16-Mar-05	7372/3	emma	VBM:2005031008 Make cols attribute optional on the XDIME table element

 11-Mar-05	7357/2	pcameron	VBM:2005030906 Fixed node annotation for dissection

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 21-Jan-05	6744/1	adrianj	VBM:2005011005 Fix for double-encoding of character references in WML native markup

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 20-Sep-04	5575/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 20-Sep-04	5527/5	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 06-Aug-04	5132/1	pcameron	VBM:2004080313 Spatial format iterator row renders correctly for WML protocols

 06-Aug-04	5100/3	pcameron	VBM:2004080313 Spatial format iterator row renders correctly for WML protocols

 22-Jul-04	4713/10	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 20-Jul-04	4713/7	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 29-Jun-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 12-Jul-04	4783/3	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 28-Jun-04	4685/38	steve	VBM:2004050406 Remove empty span around alt text

 11-Jun-04	4676/1	steve	VBM:2004050406 Remove span from whitespace alt text

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 14-May-04	4315/4	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 04-May-04	4117/2	steve	VBM:2004042901 Style class rendering fix

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 25-Mar-04	3386/1	steve	VBM:2004030901 Supermerged and merged back with Proteus

 30-Apr-04	4096/1	mat	VBM:2004042809 Make DOMProtocol object pools configurable

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable
 15-Jun-04	4704/5	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-Jun-04	4461/4	ianw	VBM:2004051714 super merged

 18-May-04	4461/1	ianw	VBM:2004051714 Only render select element if it contains options for wml

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 28-Jun-04	4676/4	steve	VBM:2004050406 supermerged

 11-Jun-04	4676/1	steve	VBM:2004050406 Remove span from whitespace alt text

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 04-May-04	4117/2	steve	VBM:2004042901 Style class rendering fix

 30-Apr-04	4096/1	mat	VBM:2004042809 Make DOMProtocol object pools configurable

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 12-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 15-Mar-04	2736/7	steve	VBM:2003121104 Supermerged

 02-Mar-04	2736/4	steve	VBM:2003121104 Patched from Proteus2 and merged with MCS

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 23-Jan-04  2736/1  steve   VBM:2003121104 Configurable WMLC and dollar encoding

 23-Jan-04  2685/1  steve   VBM:2003121104 Configurable WMLC and dollar encoding
 09-Mar-04	3366/1	geoff	VBM:2004022712 Image component fallback to text is wrapped in a paragraph element on WML

 09-Mar-04	3364/1	geoff	VBM:2004022712 Image component fallback to text is wrapped in a paragraph element on WML

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 02-Nov-03	1763/1	allan	VBM:2003032007 Patched from Proteus2.
 23-Jan-04	2685/1	steve	VBM:2003121104 Configurable WMLC and dollar encoding

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 02-Nov-03	1754/1	allan	VBM:2003032007 Correct packaging type to multipart/mixed.

 25-Sep-03	1412/6	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 17-Sep-03	1412/4	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 16-Sep-03	1301/9	byron	VBM:2003082107 Support Openwave GUI Browser extensions - single select controls handled as type=radio

 16-Sep-03	1301/7	byron	VBM:2003082107 Support Openwave GUI Browser extensions - single select controls handled as type=radio

 11-Sep-03	1301/4	byron	VBM:2003082107 Support Openwave GUI Browser extensions - resolved merge conflicts

 10-Sep-03	1301/1	byron	VBM:2003082107 Support Openwave GUI Browser extensions

 12-Sep-03	1331/4	byron	VBM:2003090201 OpenWave protocols not using correct DTD - fixed merge conflicts
 04-Sep-03	1355/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 04-Sep-03	1331/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 10-Sep-03	1386/1	philws	VBM:2003090801 (X)HTML support for native markup

 10-Sep-03	1379/4	philws	VBM:2003090801 Rename a setup method to be more accurate about what it is for

 10-Sep-03	1379/4	philws	VBM:2003090801 Rename a setup method to be more accurate about what it is for

 10-Sep-03	1379/2	philws	VBM:2003090801 (X)HTML support for native markup

 10-Sep-03	1379/2	philws	VBM:2003090801 (X)HTML support for native markup

 12-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 09-Mar-04	3364/1	geoff	VBM:2004022712 Image component fallback to text is wrapped in a paragraph element on WML

 23-Feb-04	2685/4	steve	VBM:2003121104 supermerged

 23-Jan-04	2685/1	steve	VBM:2003121104 Configurable WMLC and dollar encoding

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 02-Nov-03	1754/1	allan	VBM:2003032007 Correct packaging type to multipart/mixed.

 04-Sep-03	1355/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 04-Sep-03	1331/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 10-Sep-03	1379/4	philws	VBM:2003090801 Rename a setup method to be more accurate about what it is for

 10-Sep-03	1379/2	philws	VBM:2003090801 (X)HTML support for native markup

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 18-Aug-03	1052/5	allan	VBM:2003073101 Update add display inline method.

 17-Aug-03	1052/2	allan	VBM:2003073101 Support styles on menu and menuitems

 12-Aug-03	1055/2	philws	VBM:2003052704 Fix merge issues

 11-Jul-03	772/1	sumit	VBM:2003052704 tabindex attribute added to do elements

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 17-Jun-03	427/1	mat	VBM:2003061607 Add Menu Horizontal Separator theme

 13-Jun-03	399/1	mat	VBM:2003060503 Handle new special dissection tags

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 06-Jun-03	277/2	chrisw	VBM:2003052702 Merged changes from Metis to Mimas

 05-Jun-03	285/1	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
