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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/MMLBasicTestCase.java,v 1.2 2003/01/15 18:34:24 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Jan-03    Byron           VBM:2002111812 - Created. See class comment.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.DOMAssertionUtilities;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.DoSelectInputTestHelper;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SelectOptionGroup;
import com.volantis.mcs.protocols.ShardLinkAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.VolantisProtocolTestable;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSMenuOrientationKeywords;
import com.volantis.mcs.themes.properties.MCSSelectionListOptionLayoutKeywords;
import com.volantis.mcs.themes.properties.MCSSelectionListStyleKeywords;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.values.MutablePropertyValues;
import junit.framework.Assert;

import java.io.IOException;

/**
 * This class unit test the MMLBasic class.
 */
public class MMLBasicTestCase
        extends HTML_iModeTestCase {

    static final private String elementName = "myElementName";
    static final protected String PANE_NAME = "myPaneName";
    static final protected String CLASS_NAME = "VF-0";
    static final private String CLASS_NAME_2 = "VF-9";
    static final private String CLASS_NAME_3 = "VF-3";

    protected Pane pane;

    private TestMMLBasic protocol;
    private XHTMLBasicTestable testable;

    protected TestMarinerPageContext context;

    private Element element;
    private MCSAttributes attribute;


    public MMLBasicTestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestMMLBasicFactory(),
                internalDevice);
        return protocol;
    }

    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (TestMMLBasic) protocol;
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
    private void privateSetUp() throws RepositoryException {
        canvasLayout = new CanvasLayout();
        pane = new Pane(canvasLayout);
        pane.setName(PANE_NAME);
        pane.setInstance(0);

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout1 =
                RuntimeDeviceLayoutTestHelper.activate(canvasLayout);
        RuntimeDeviceLayout runtimeDeviceLayout =
                runtimeDeviceLayout1;

        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        context.setDeviceLayout(runtimeDeviceLayout);

        DeviceLayoutContext deviceContext = new TestDeviceLayoutContext();

        resetElement();

        attribute = new StyleAttributes();
        attribute.setStyles(StylesBuilder.getDeprecatedStyles());
        attribute.setTitle("My title");

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
        context.setDeviceName(NETSCAPE4_DEVICE_NAME);
    }

    /**
     * Reset the element
     */
    private void resetElement() {
        element = domFactory.createElement();
        element.setName(elementName);
    }


    /**
     * Test usesDivForMenus property.
     */
    public void testUsesDivForMenus() {
        VolantisProtocol protocol = createTestableProtocol(internalDevice);
        assertFalse("MMLBasic does not use div for menus. ",
                    protocol.usesDivForMenus());
    }

    /**
     * test open pane
     */
    public void testOpenPane() throws RepositoryException {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        Element el = null;

        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);
        pane.setBackgroundColour("#ff0000");
        pane.setBorderWidth("15");
        pane.setCellPadding("16");
        pane.setCellSpacing("17");

        final String cssValues = "background-color: #ff00ff; " +
                        "border-width: 5px; " +
                        "border-spacing: 7px";

        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles(cssValues));
        attributes.setPane(pane);

        protocol.openPane(buffer, attributes);
        try {
            el = buffer.closeElement("td");
        } catch (IllegalStateException ise) {
            fail("td element not found.");
        }

        //@todo add actual tests.
    }

    public void testOpenPaneWithContainerCell() throws Exception {
        privateSetUp();
        DOMOutputBuffer dom = new DOMOutputBuffer();

        dom.initialise();

        dom.openElement("td");

        Pane pane1 = new Pane(null);
        pane1.setName(PANE_NAME);
        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "background-color: #ff0000; border-width: 5px"));
        attributes.setPane(pane1);
        // Setting the border width to 5 will render the pane as a table
        // because the pane has no style class.

        protocol.setMarinerPageContext(context);
        protocol.openPane(dom, attributes);

        try {
            dom.closeElement("td");
            dom.closeElement("tr");
            dom.closeElement("table");
        } catch (IllegalStateException e) {
            fail("Expected table/tr/td elements not found");
        }

    }

    /**
     * test for the openDissectingPane method
     */
    public void testOpenDissectingPane() throws RepositoryException {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        String inclusionPath = "TestInclusionPath";
        String paneName = "TestPaneName";
        String nextShortcut = "TestNextShardShortcut";
        String nextLink = "TestNextShardLink";
        String nextClass = "TestNextShardLinkClass";
        String prevShortcut = "TestPreviousShardShortcut";
        String prevLink = "TestPreviousShardLink";
        String prevClass = "TestPreviousShardLinkClass";
        String maxContentSize = "100";
        boolean nextLinkFirst = true;

        DissectingPane pane = new DissectingPane(canvasLayout);
        pane.setName(paneName);
        pane.setAttribute(FormatConstants.NEXT_SHARD_SHORTCUT_ATTRIBUTE,
                          nextShortcut);
        pane.setAttribute(FormatConstants.NEXT_SHARD_LINK_TEXT_ATTRIBUTE,
                          nextLink);
        pane.setAttribute(FormatConstants.NEXT_SHARD_LINK_CLASS_ATTRIBUTE,
                          nextClass);
        pane.setAttribute(FormatConstants.PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE,
                          prevShortcut);
        pane.setAttribute(FormatConstants.PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE,
                          prevLink);
        pane.setAttribute(FormatConstants.PREVIOUS_SHARD_LINK_CLASS_ATTRIBUTE,
                          prevClass);
        pane.setAttribute(FormatConstants.MAXIMUM_CONTENT_SIZE_ATTRIBUTE, maxContentSize);

        DissectingPaneAttributes atts = new DissectingPaneAttributes();
        atts.setDissectingPane(pane);
        atts.setInclusionPath(inclusionPath);
        atts.setIsNextLinkFirst(nextLinkFirst);

        protocol.setDissecting(true);
        protocol.openDissectingPane(buffer, atts);

        if (protocol.isDissectionSupported()) {
            Element el = null;
            try {
                el = buffer.closeElement
                    (DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
            } catch (IllegalStateException ise) {
                fail(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT +
                " element not found.");
            }

            // check that all the attributes have been written out
            assertEquals("Invalid inclusion path attribute",
                    el.getAttributeValue
                    (DissectionConstants.INCLUSION_PATH_ATTRIBUTE),
                    atts.getInclusionPath());

            assertEquals("Invalid pane name attribute",
                    el.getAttributeValue
                    (DissectionConstants.DISSECTING_PANE_NAME_ATTRIBUTE),
                    atts.getDissectingPane().getName());

            assertEquals("Invalid next shard shortcut attribute",
                    el.getAttributeValue
                    (DissectionConstants.NEXT_SHARD_SHORTCUT_ATTRIBUTE),
                    nextShortcut);

            assertEquals("Invalid next shard link attribute",
                    el.getAttributeValue
                    (DissectionConstants.NEXT_SHARD_LINK_TEXT_ATTRIBUTE),
                    nextLink);

            assertEquals("Invalid previous shortcut attribute",
                    el.getAttributeValue
                    (DissectionConstants.PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE),
                    prevShortcut);

            assertEquals("Invalid previous link attribute",
                    el.getAttributeValue
                    (DissectionConstants.PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE),
                    prevLink);

            assertEquals("Invalid next link first attribute",
                    el.getAttributeValue
                    (DissectionConstants.GENERATE_NEXT_LINK_FIRST_ATTRIBUTE),
                    String.valueOf(nextLinkFirst));

            assertEquals("Invalid max contents attribute",
                    el.getAttributeValue
                    (DissectionConstants.MAXIMUM_CONTENT_SIZE_ATTRIBUTE),
                    maxContentSize);
        } else {
            assertTrue("Protocol does not support dissection so no markup " +
                    "should have been generated", buffer.isEmpty());
        }
    }

    public void testShardLinkStyleClass() throws Exception {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        ShardLinkAttributes attributes = new ShardLinkAttributes();
        Element anchor;
        Node node;
        String styleClass;

        buffer.initialise();
        attributes.setHref("http://www.volantis.com/page");
        attributes.setLinkText("Next");
        attributes.setShortcut("N");
        // todo XDIME-CP style dissection correctly.
//        attributes.setStyleClass("shardlink");

        protocol.doShardLink(buffer, attributes);

        node = buffer.getCurrentElement().getHead();

        assertNotNull("Should have found a node", node);

        assertTrue("Node should be an element",
                node instanceof Element);

        anchor = (Element) node;

        styleClass = anchor.getAttributeValue("class");

        assertEquals("Unexpected type of element found",
                "a",
                anchor.getName());

        assertEquals("Unexpected style class found",
                null, styleClass);
    }

    /**
     * test the getFormatStyleClass method.
     */
    public void notestGetFormatStyleClass() throws Exception {
        privateSetUp();

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Demonstrate that this device layout isn't used
        context.setDeviceLayout(null);

//        context.setStyleClassName(CLASS_NAME);

        Pane pane = new Pane(null);
        pane.setName(PANE_NAME);
        pane.setInstance(0);

        String classname = null;//protocol.getFormatStyleClass(pane);

        assertEquals("Pane classname does not match",
                CLASS_NAME,
                classname);

//        context.setStyleClassName(CLASS_NAME_2);

        Grid grid = new Grid(null);
        grid.setInstance(1);
        grid.setBorderWidth("10");

//        classname = protocol.getFormatStyleClass(grid);

        assertEquals("Grid classname does not match",
                CLASS_NAME_2,
                classname);

        // This should be ignored because the page head should have cached
        // the values for instance 0 so should not attempt to call the
        // context method again.
//        context.setStyleClassName(CLASS_NAME_3);

//        classname = protocol.getFormatStyleClass(pane);

        assertEquals("Pane re-test classname does not match",
                CLASS_NAME,
                classname);
    }


    /**
     * Test the doActionInput method
     */
    public void testActionInput() throws Exception {
        privateSetUp();

        checkActionInputType();
        doTest2002120611();
        checkActionInputShortcut();
    }

    /**
     * Test that we are rendering the type of action inputs properly.
     */
    protected void checkActionInputType() throws ProtocolException {

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        // Setup test for normal behaviour for input tag generation (submit)
        String name = "My Name";
        String actionType = "submit";
        String caption = "This is my caption";
        String value = "This is my value";

        attributes.setName(name);
        attributes.setType(actionType);
        attributes.setCaption(new LiteralTextAssetReference(caption));
        attributes.setValue(value);

        protocol.doActionInput(buffer, attributes);
        String actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" type=\"submit\" "+
                "value=\"" + caption + "\"/>", actualResult);

        // Setup test for normal behaviour for input tag generation (perform)
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        actionType = "perform";
        attributes.setType(actionType);        
        protocol.doActionInput(buffer, attributes);
        actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" " +
                "type=\"submit\" value=\"" + caption  + "\"/>",
                actualResult);

        // Setup test for normal behaviour for input tag generation (perform)
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        actionType = "reset";
        attributes.setType(actionType);
        protocol.doActionInput(buffer, attributes);
        actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" " +
                "type=\"reset\" value=\"" + caption  + "\"/>",
                actualResult);

        // Setup test for abnormal behaviour - garbage actionType
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        actionType = "GARBAGE";
        attributes.setType(actionType);
        try {
            protocol.doActionInput(buffer, attributes);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        // Setup test for abnormal behaviour - garbage actionType
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        actionType = "submit";
        attributes.setType(actionType);
        attributes.setTabindex("1");
        protocol.doActionInput(buffer, attributes);
        assertTrue(protocol.supportsTabindex);
        actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" " +
                "tabindex=\"1\" type=\"submit\" value=\"" + caption  + "\"/>",
                actualResult);
    }

    /**
     * Test the doActionInput method
     */
    public void doTest2002120611() throws Exception {


        String name = "My Name";
        String actionType = "submit";
        String caption = "The caption";
        String value = "This is my value";

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        attributes.setName(name);
        attributes.setType(actionType);
        attributes.setCaption(new LiteralTextAssetReference(caption));
        attributes.setValue(value);
        // todo XDIME-CP style forms correctly.
//        attributes.setStyleClass(style);

        // Setup test to ensure that link style form submision fallsback to the
        // default form submision
        protocol.doActionInput(buffer, attributes);
        String actualResult = bufferToString(buffer);

        // check that the correct output has been generated.
        assertEquals("DOM buffer should match",
                "<input name=\"" + name + "\" " +
                "type=\"submit\" value=\"" + caption  + "\"/>",
                actualResult);
    }


    /**
     * This is a template method that is called from within the testDoMenu and
     * provides a mechanism for subclasses to modify the expected output for
     * each menu item. For example, some protocols always have an ending <br/>,
     * others don't. Also some make use of the accesskey - if one exists.
     *
     * @param buf      the <code>StringBuffer</code> used to generated the
     *                 expected dom.
     * @param menuItem the menuitem under scrutiny
     * @param hasNext  true if there is another menuitem in the list, false
     *                 otherwise.
     */
    protected void templateTestDoMenu(StringBuffer buf,
            MenuItem menuItem,
            boolean hasNext, boolean isHorizontal) {
        buf.append("<a href=\"").append(menuItem.getHref()).append("\">");
        buf.append(menuItem.getText()).append("</a>");
        if (hasNext) {
            if(isHorizontal) {
                buf.append("&nbsp;");
            } else {
                buf.append("<br/>");
            }
        }
    }

    protected void checkActionInputShortcut() throws ProtocolException {

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        // Setup test for normal behaviour for input tag generation (submit)
        String shortcut = "1";
        String name = "My Name";
        String actionType = "submit";
        String caption = "This is my caption";
//        String value = "This is my value";

        attributes.setShortcut(new LiteralTextAssetReference(shortcut));
        attributes.setName(name);
        attributes.setType(actionType);
        attributes.setCaption(new LiteralTextAssetReference(caption));
//        attributes.setValue(value);

        protocol.doActionInput(buffer, attributes);
        String actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input accesskey=\"1\" name=\"" + name + "\" " +
                "type=\"submit\" value=\"" + caption + "\"/>", actualResult);
    }

    /**
     * Test for doProtocolString
     */
    public void testDoProtocolString()
            throws IOException, RepositoryException {

        privateSetUp();

        checkDoProtocolString(protocol, "");
    }


    /**
     * Test the output for a single default select with no options
     * selected
     * @exception Exception if an error occurs
     */
    public void testDefaultSingleSelect() throws Exception {
        privateSetUp();

        boolean[] selected = {false, false, false};

        doTestDefaultSelectInput(selected, false);


    }


    /**
     * Test the output for a single select with a single default
     * option selected
     * @exception Exception if an error occurs
     */
    public void testDefaultSingleSelectOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selected = {false, true, false};

        doTestDefaultSelectInput(selected, false);

    }

    /**
     * Test the output for a multi default select with no option selected
     * @exception Exception if an error occurs
     */
    public void testDefaultMultiSelect() throws Exception {
        privateSetUp();

        boolean[] selected = {false, false, false};

        doTestDefaultSelectInput(selected, true);
    }

    /**
     * Test the output for a multi default select with 2 options selected
     * @exception Exception if an error occurs
     */
    public void testDefaultMultiSelectOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selected = {false, true, true};

        doTestDefaultSelectInput(selected, true);
    }


    /**
     * Test the output for a single control select
     * @exception Exception if an error occurs
     */
    public void testControlSingleSelect() throws Exception {
        privateSetUp();

        boolean[] selected = {false, false, false};

        // no options selected
        // single select - ie radio
        // vertical options
        // align caption to the left
        // do not create any optgroups
        boolean multiple = false;
        boolean vertical = true;
        boolean alignright = false;
        int optGroupCount = 0;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);

    }

    /**
     * Test the output for a single control select with an option selected
     * @exception Exception if an error occurs
     */
    public void testControlSingleSelectOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selected = {true, false, false};

        boolean multiple = false;
        boolean vertical = false;
        boolean alignright = true;
        int optGroupCount = 0;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);
    }


    /**
     * Test the output for a multi control select
     * @exception Exception if an error occurs
     */
    public void testControlMultiSelect() throws Exception {
        privateSetUp();

        boolean[] selected = {false, false, false};

        // no options selected
        // single select - ie radio
        // horizontal options
        // align caption to the left
        boolean multiple = true;
        boolean vertical = false;
        boolean alignright = false;
        int optGroupCount = 0;

        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);
    }


    /**
     * Test the output for a multi control select with options selected
     * @exception Exception if an error occurs
     */
    public void testControlMultiSelectOptionsSelected() throws Exception {
        privateSetUp();

        boolean[] selected = {true, true, false};

        // no options selected
        // multi select - ie chackbox
        // vertical options
        // align caption to the right
        boolean multiple = true;
        boolean vertical = true;
        boolean alignright = true;
        int optGroupCount = 0;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);
    }


    /**
     * Ensure that no optgroups are being generated when rendering a
     * control select
     * @exception Exception if an error occurs
     */
    public void testNoOptGroupsWithControlSelect() throws Exception {
        privateSetUp();

        boolean[] selected = {false, false, false};

        // no options selected
        // single select - ie radio
        // vertical options
        // align caption to the left
        // do not create any optgroups
        boolean multiple = false;
        boolean vertical = true;
        boolean alignright = false;
        int optGroupCount = 1;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);

        optGroupCount = 4;
        doTestControlSelect(selected, multiple, vertical,
                alignright, optGroupCount);
    }


    /**
     * Helper method for testing the doSelectInput method when
     * generating default select menus (ie not checkboxes or radio buttons)
     * @param selectedOptions array specifing the options to set as selected
     * @param multiSelect true if and only if a multiple select is to be
     *                    generated
     * @exception Exception if an error occurs
     */
    public void doTestDefaultSelectInput(boolean selectedOptions[],
            boolean multiSelect)
    throws Exception {

        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();

        final String rowCount = "3";
        final String tabIndex ="1";

        XFSelectAttributes atts = helper.buildSelectAttributes();
        atts.setStyles(StylesBuilder.getStyles("mcs-rows: " + rowCount));
        atts.setMultiple(multiSelect);

        if(!multiSelect) {
            for(int i=0; i<selectedOptions.length; i++) {
                if(selectedOptions[i]) {
                    atts.setInitial("Value" + (i+1));
                    break;
                }
            }
        }
        atts.setTabindex(tabIndex);
        // add some options

        for(int i=0; i<selectedOptions.length; i++) {
            helper.addOption(atts,
                    "Caption" + (i+1),
                    "Prompt" + (i+1),
                    "Value" + (i+1),
                    selectedOptions[i]);
        }


        //protocol.addStyleMapping(atts, style);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        TestMarinerPageContext testPageContext =
            (TestMarinerPageContext)protocol.getMarinerPageContext();
        testPageContext.setFormFragmentResetState(true);

        testable.setCurrentBuffer(atts.getEntryContainerInstance(), buffer);

        StringBuffer sb = new StringBuffer();

        sb.append("<select");
        if(multiSelect) {
            sb.append(" multiple=\"multiple\"");
        }
        sb.append(" name=\"").append(atts.getName()).append("\"");
        if(multiSelect) {
            sb.append(" size=\"").append(rowCount).append("\"");
        }
        sb.append(" tabindex=\"1\"");
        sb.append(">");

        for(int i=0; i<selectedOptions.length; i++) {
            sb.append("<option");
            if(selectedOptions[i]) {
                sb.append(" selected=\"selected\"");
            }
            sb.append(" value=\"Value").append(i+1).append("\">")
            .append("Caption").append(i+1).append("</option>");
        }

        sb.append("</select>");


        helper.runTest(protocol, buffer, atts, sb.toString());

    }


    /**
     * Test that the optgroup nesting is OK when generating a defaulf
     * select
     * @exception Exception if an error occurs
     */
    public void testOptGroupsNestingWithDefaultSelect() throws Exception {
        privateSetUp();

        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();


        XFSelectAttributes atts = helper.buildSelectAttributes();

        // build the following nested option optgroup structure
        // <group 1>
        //   <group 2>
        //      <group 3>
        //         <option 1>
        //      </group>
        //   <group>
        // </group>
        // <group 4>
        //   <option 2>
        // </group>
        SelectOptionGroup group = helper.addOptionGroup(atts, "Group1",
        "Prompt1");
        group = helper.addOptionGroup(group, "Group2", "Prompt2");
        group = helper.addOptionGroup(group, "Group3", "Prompt3");
        helper.addOption(group, "Caption1", "Prompt1",  "Value1", false);

        group = helper.addOptionGroup(atts, "Group4",  "Prompt4");
        helper.addOption(group, "Caption2", "Prompt2",  "Value2", false);


        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        testable.setCurrentBuffer(atts.getEntryContainerInstance(), buffer);

        StringBuffer sb = new StringBuffer();

        sb.append("<select name=\"").append(atts.getName()).append("\">");

        for(int i=0; (i<testable.getMaxOptGroupNestingDepth() && i<3); i++) {
            sb.append("<optgroup label=\"Group").append(i+1);
            sb.append("\" title=\"Prompt").append(i+1).append("\">");
        }

        sb.append("<option value=\"Value1\">Caption1</option>");

        for(int i=0; (i<testable.getMaxOptGroupNestingDepth() && i<3); i++) {
            sb.append("</optgroup>");
        }


        if(testable.getMaxOptGroupNestingDepth() > 0) {
            sb.append("<optgroup label=\"Group4\" title=\"Prompt4\">");
        }

        sb.append("<option value=\"Value2\">Caption2</option>");

        if(testable.getMaxOptGroupNestingDepth() > 0) {
            sb.append("</optgroup>");
        }

        sb.append("</select>");
        helper.runTest(protocol, buffer, atts,  sb.toString());
    }


    /**
     * Helper method for testing the doSelectInput() method when generating
     * control options (checkboxes and radio buttons)
     * @param selectedOtions array of options that should be selected
     * @param multiSelect true if and only if a multiple select
     * @param vertical should the options be listed vertically
     * @param rightAlignCaption should the caption go to the right of the
     *                          control
     * @param optGroupCount how many optgroups should enclose that last
     *                      option
     * @exception Exception if an error occurs
     */
    public void doTestControlSelect(boolean selectedOtions[],
            boolean multiSelect,
            final boolean vertical,
            final boolean rightAlignCaption,
            int optGroupCount)
    throws Exception {


        DoSelectInputTestHelper helper = new DoSelectInputTestHelper();

        XFSelectAttributes atts = helper.buildSelectAttributes();
        Styles styles = atts.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();

        if(!multiSelect) {
            for(int i=0; i<selectedOtions.length; i++) {
                if(selectedOtions[i]) {
                    atts.setInitial("Value" + (i+1));
                    break;
                }
            }
        }

        atts.setMultiple(multiSelect);
        //atts.setTabindex(tabIndex);
        // add some options
        helper.addOption(atts, "Caption1", "Prompt1", "Value1",
                selectedOtions[0]);
        helper.addOption(atts, "Caption2", "Prompt2", "Value2",
                selectedOtions[1]);


        SelectOptionGroup group = null;
        String caption, prompt;
        int count = 0;
        do {
            caption = "Group" + (count + 1);
            prompt = "Prompt" + (count + 1);
            if(0 == count) {
                group = helper.addOptionGroup(atts, caption, prompt);;
            } else {
                group = helper.addOptionGroup(group, caption, prompt);
            }
        } while(++count < optGroupCount);


        helper.addOption(group, "Caption3", "Prompt3", "Value3",
                selectedOtions[2]);

        //protocol.addStyleMapping(atts, style);
        TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();

        propertyValues.setComputedValue(
                StylePropertyDetails.MCS_SELECTION_LIST_STYLE,
                MCSSelectionListStyleKeywords.CONTROLS);
        propertyValues.setComputedValue(
                StylePropertyDetails.MCS_SELECTION_LIST_OPTION_LAYOUT,
                rightAlignCaption ? MCSSelectionListOptionLayoutKeywords.CONTROL_FIRST
                : MCSSelectionListOptionLayoutKeywords.CAPTION_FIRST);
        propertyValues.setComputedValue(
                StylePropertyDetails.MCS_MENU_ORIENTATION,
                vertical ? MCSMenuOrientationKeywords.VERTICAL
                : MCSMenuOrientationKeywords.HORIZONTAL);

        TestMarinerPageContext testPageContext =
            (TestMarinerPageContext)protocol.getMarinerPageContext();
        testPageContext.setFormFragmentResetState(true);

        testable.setCurrentBuffer(atts.getEntryContainerInstance(), buffer);

        String type = (multiSelect) ? "checkbox" : "radio";
        String seperator = (vertical) ? "<br/>" : "\u00a0";
        StringBuffer sb = new StringBuffer();

        sb.append("<div>");

        for (int i=0; i<3; i++) {
            if(i > 0) {
                sb.append(seperator);
            }
            if (!rightAlignCaption) {
                sb.append("Caption").append(i+1);
            }
            sb.append("<input ");
            if(selectedOtions[i]) {
                sb.append("checked=\"checked\" ");
            }
            sb.append("name=\"").append(atts.getName()).append("\" ");
            sb.append("type=\"").append(type).append("\" ");
            sb.append("value=\"").append("Value").append(i+1).append("\"/>");
            if(rightAlignCaption) {
                sb.append("Caption").append(i+1);
            }
        }
        sb.append("</div>");


        helper.runTest(protocol, buffer, atts, sb.toString());
    }

    protected void checkRenderAltText(String altText, DOMOutputBuffer buffer) {
    }

    /**
     * Any attributes in the given element are used to generate a failure of
     * the test invoking this method.
     *
     * @param element the element to be checked for attributes
     * @throws Exception if an error occurs
     */
    protected void assertNoAttributes(Element element) throws Exception {
        Attribute attributes = element.getAttributes();

        if (attributes != null) {
            String unexpectedAttributes = attributes.getName();

            while ((attributes = attributes.getNext()) != null) {
                unexpectedAttributes += ", " + attributes.getName();
            }

            Assert.fail("the following attributes were not expected: " +
                    unexpectedAttributes);
        }
    }

    /**
     * Helper test method for testBufferSelector
     */
    private void doTestBufferSelector(MMLBasic.BufferSelector selector,
            Node expectedCaptionNode,
            Node expectedEntryNode,
            boolean expectedCaptionBuffer,
            boolean expectedEntryBuffer,
            boolean buffersAreSame)
    throws Exception {

        assertEquals("Caption node should match",
                expectedCaptionNode,
                selector.getInsertAfterCaptionNode());

        assertEquals("Entry node should match",
                expectedEntryNode,
                selector.getInsertAfterEntryNode());

        if (expectedCaptionBuffer) {
            assertNotNull(selector.getCaptionPaneOutputBuffer());
        } else {
            assertNull(selector.getCaptionPaneOutputBuffer());
        }
        if (expectedEntryBuffer) {
            assertNotNull(selector.getEntryPaneOutputBuffer());
        } else {
            assertNull(selector.getEntryPaneOutputBuffer());
        }

        if (buffersAreSame) {
            assertEquals("Caption buffer should match",
                    selector.getEntryPaneOutputBuffer(),
                    selector.getCaptionPaneOutputBuffer());
        } else {
            assertTrue("Caption buffer should match",
                    !selector.getEntryPaneOutputBuffer().equals(
                            selector.getCaptionPaneOutputBuffer()));
        }
    }


    /**
     * Test the buffer selection.
     */
    public void testBufferSelector() throws Exception {

        // Test with both entry and caption nodes null.
        // Expected: entryPaneNode == captionPaneNode == null AND
        //           entryBuffer == captionBuffer != null
        MMLBasic.BufferSelector selector;
        selector = protocol.createBufferSelector(
                null, null, null, null, null);
        doTestBufferSelector(selector, null, null, false, false, true);

        // Test with valid entry pane and null caption pane.
        // Expected: entryPaneNode != null AND captionPaneNode == null AND
        //           entryBuffer == captionBuffer != null
        Node entry = domFactory.createElement();
        selector = protocol.createBufferSelector(
                entry, null, null, null, null);
        doTestBufferSelector(selector, null, entry, true, true, true);

        // Test with null entry pane and valid caption pane.
        // Expected: entryPaneNode == captionPaneNode != null AND
        //           entryBuffer == captionBuffer != null
        Node caption = domFactory.createElement();
        selector = protocol.createBufferSelector(
                null, caption, null, null, null);
        doTestBufferSelector(selector, caption, null, true, true, true);

        // Test with null entry pane and valid caption pane.
        // Expected: entryPaneNode == captionPaneNode != null AND
        //           entryBuffer == captionBuffer != null
        selector = protocol.createBufferSelector(
                entry, caption, null, null, null);
        doTestBufferSelector(selector, caption, entry, true, true, false);

        DOMOutputBuffer dom = new DOMOutputBuffer();
        selector = protocol.createBufferSelector(
                null, null, null, null, dom);
        doTestBufferSelector(selector, null, null, true, true, true);

        DOMOutputBuffer entryBuffer = new DOMOutputBuffer();
        DOMOutputBuffer captionBuffer = new DOMOutputBuffer();
        selector = protocol.createBufferSelector(
                null, null, null, captionBuffer, null);
        doTestBufferSelector(selector, null, null, true, true, true);

        selector = protocol.createBufferSelector(
                null, null, entryBuffer, null, null);
        doTestBufferSelector(selector, null, null, true, true, true);

        selector = protocol.createBufferSelector(
                null, null, entryBuffer, captionBuffer, null);
        doTestBufferSelector(selector, null, null, true, true, false);

        selector = protocol.createBufferSelector(
                entry, caption, entryBuffer, captionBuffer, null);
        doTestBufferSelector(selector, caption, entry, true, true, false);

        selector = protocol.createBufferSelector(
                entry, caption, entryBuffer, captionBuffer, dom);
        doTestBufferSelector(selector, caption, entry, true, true, false);
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
        attrs.setSrc("http://www.volantis.com/my_image.jpg");
        attrs.setLocalSrc(true);
        attrs.setAltText("my_alt_text");
        attrs.setWidth("10");
        attrs.setHeight("20");

        protocol.doImage(buffer, attrs);

        String expected = "<img alt=\"my_alt_text\" height=\"20\" " +
        "src=\"http://www.volantis.com/my_image.jpg\" width=\"10\"/>";

        assertEquals("Unexpected img markup generated.",
                expected,
                bufferToString(buffer));
    }

    // javadoc inherited
    protected String expectedDefaultMimeType() {
        return "text/html";
    }

    /**
     * Ensure that every protocol that claims to support dissection has a
     * dissector and vice versa. Probably these two could be combined into
     * a single variable to avoid this test.
     *
     * Note this is for the old (non-accurate) dissector, which is still used
     * for non WML protocols.
     */
    public void testOldDissectorSetup() {
        assertEquals("dissection flag and dissector instance do not match",
                     protocol.isDissectionSupported(),
                     protocol.getProtocolConfiguration().getDissector() != null);
    }

    /**
     * Ensure that the protocol implements
     * {@link MMLBasic#enableNameIdentification} correctly for anchors.
     *
     * @throws ProtocolException
     */
    public void testAnchorNameIdentification() throws ProtocolException, RepositoryException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Initialise our protocol attributes with an id attribute.
        String idValue = "the identifier value";
        AnchorAttributes attributes = new AnchorAttributes();
        attributes.setId(idValue);

        // Create the expected value for the name attribute output.
        String expectedNameValue = null;
        if (protocol.enableNameIdentification) {
            expectedNameValue = idValue;
        }

        // Open and close the anchor to generate the markup.
        protocol.openAnchor(buffer, attributes);
        protocol.closeAnchor(buffer, attributes);

        // Check that the element generated has appropriate id and name
        // attributes.
        Element root = buffer.getCurrentElement();
        Element element = (Element) root.getHead();
//        // Handle the case where some protocols wrap the tag in another tag.
//        if (!element.getName().equals("a")) {
//            element = (Element) element.getHead();
//        }
        DOMAssertionUtilities.assertElement("a", element);
        DOMAssertionUtilities.assertAttributeEquals("name", expectedNameValue, element);
    }

    /**
     * Check encoding from the quote table
     */
    public void testElementEncoding() throws ProtocolException, RepositoryException {
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        buffer.openElement("p");
        buffer.appendEncoded("start ");
        buffer.appendEncoded("\u009f");
        buffer.appendEncoded(VolantisProtocol.NBSP);
        buffer.appendEncoded("\u00a1");
        buffer.appendEncoded(" finish");
        buffer.closeElement("p");

        assertEquals("Incorrect Encoding",
              "<p>start \u009f&nbsp;\u00a1 finish</p>", bufferToString(buffer));
    }

    /**
     * Ensure that the protocol implements
     * {@link MMLBasic#enableNameIdentification} correctly for anchors.
     *
     * @throws ProtocolException
     */
    public void testImageNameIdentification() throws ProtocolException, RepositoryException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Initialise our protocol attributes with an id attribute.
        String idValue = "the identifier value";
        ImageAttributes attributes = new ImageAttributes();
        attributes.setId(idValue);
        attributes.setSrc("required");

        // Create the expected value for the name attribute output.
        String expectedNameValue = null;
        if (protocol.enableNameIdentification) {
            expectedNameValue = idValue;
        }

        // Oo the image to generate the markup.
        protocol.doImage(buffer, attributes);

        // Check that the element generated has appropriate id and name
        // attributes.
        Element root = buffer.getCurrentElement();
        Element element = (Element) root.getHead();
//        // Handle the case where some protocols wrap the tag in another tag.
//        if (!element.getName().equals("img")) {
//            element = (Element) element.getHead();
//        }
        DOMAssertionUtilities.assertElement("img", element);
        // html 3.2, etc, do no generate id's.
        // DOMUtilities.assertAttributeEquals("id", idValue, element);
        DOMAssertionUtilities.assertAttributeEquals("name", expectedNameValue, element);
    }

    /**
     * Get the name of the menu element for the MMLBasic protocol. Sub-classes
     * should override this method if their menu element is different from
     * their parent class.
     * @return "span".
     */
    protected String getMenuElementName() {
          return "span";
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 07-Nov-05	10096/1	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 07-Nov-05	10126/1	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 07-Nov-05	10096/1	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 05-Sep-05	9407/4	pduffin	VBM:2005083007 Removed old themes model

 02-Sep-05	9407/2	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9375/4	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/5	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 23-Jun-05	8833/4	pduffin	VBM:2005042901 Addressing review comments

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/2	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 07-Jun-05	8637/1	pcameron	VBM:2005050402 Fixed quoting of string style values and font-family values

 22-Apr-05	7820/1	philws	VBM:2005040411 Port submit button fix from 3.3

 22-Apr-05	7812/1	philws	VBM:2005040411 Ensure submit action is retained if action style is image but no image can be found

 15-Mar-05	7404/1	philws	VBM:2005031006 Port portlet style class fix from 3.3

 15-Mar-05	7392/1	philws	VBM:2005031006 Fix portlet inclusion style generation in XHTML protocols

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-Nov-04	6076/4	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 22-Nov-04	5733/7	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	6183/5	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 18-Nov-04	6183/2	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/3	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 03-Nov-04	5871/2	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/2	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 28-Sep-04	5661/3	tom	VBM:2004091403 Added style emulation to i-mode and fixed bgcol in cells

 27-Sep-04	5581/7	tom	VBM:2004091403 Introduced stylesheet emulation for font and repaired background

 09-Sep-04	4839/8	pcameron	VBM:2004062801 div tag is now used again in a pane's table if stylesheets are in use

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 22-Jul-04	4713/7	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 29-Jun-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 22-Apr-04	3973/4	steve	VBM:2004042002 Encode #nbsp;

 21-Apr-04	3973/2	steve	VBM:2004042002 Encode #nbsp;

 14-Apr-04	3834/1	steve	VBM:2004041306 Converted MMLBasic to a DOMProtocol derived protocol

 12-Feb-04	2958/2	philws	VBM:2004012715 Add protocol.content.type device policy

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 25-Jun-03	546/1	chrisw	VBM:2003032802 Fix ClassCastException when rendering MMLBasic

 ===========================================================================
*/
