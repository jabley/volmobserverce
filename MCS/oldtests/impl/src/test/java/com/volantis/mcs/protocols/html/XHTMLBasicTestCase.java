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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasicTestCase.java,v 1.29 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Aug-02    Byron           VBM:2002081907 - A JUnit test case to test this
 *                              protocol
 * 04-Sep-02    Byron           VBM:2002081907 - Updated unit test cases and
 *                              moved this file to appropriate directory
 * 09-Sep-02    Byron           VBM:2002081907 - Updated unit test case(s)
 * 13-Sep-02    Steve           VBM:2002040809 - Added testOpenPane to test
 *                              div element and class attribute generation
 *                              in the DOM under all conditions.
 * 14-Oct-02    Geoff           VBM:2002100905 - Modified to use new DOMPool
 *                              constructor.
 * 17-Oct-02    Adrian          VBM:2002100404 - Fixed testOpenPane to account
 *                              for protocol change where styleclass name is
 *                              now retrieved from pageHead.
 * 17-Oct-02    Adrian          VBM:2002100404 - Test getPaneStyleClass
 * 11-Nov-02    Phil W-S        VBM:2002102306 - Updated to work with the new
 *                              mariner element style optimizations.
 * 13-Nov-02    Phil W-S        VBM:2002111208 - Added test for shard link
 *                              style class.
 * 19-Nov-02    Byron           VBM:2002110517 - Added all methods generated
 *                              by unittestgenerator. Added testActionInput()
 *                              and testAddClassAttribute() to be in line
 *                              with unit test case standard. Moved inner class
 *                              definitions to top of class. Reformatted
 *                              code to be consistently 4 space indents
 * 04-Dec-02    Phil W-S        VBM:2002111208 - Updated test for shard link
 *                              style class.
 * 13-Dec-02    Phil W-S        VBM:2002110516 - Changes for refactored
 *                              PageHead and getPaneStyleClass.
 * 16-Dec-02    Doug            VBM:2002120611 - Added the method
 *                              doTest2002120611(). Modified
 *                              testDoActionInput() to call doTest2002120611().
 * 16-Dec-02    Adrian          VBM:2002100203 - Updated references to
 *                              supportsMultipleStyleClasses to
 *                              supportsMultipleSelectorClasses.
 * 19-Dec-02    Phil W-S        VBM:2002121601 - Updated test for
 *                              getFormatStyleClass.
 * 30-Dec-02    Byron           VBM:2002071015 - Updated LocalRSBPool import to
 *                              reflect new package name.
 * 15-Jan-03    Adrian          VBM:2002120908 - Split
 *                              testSelectorTransformation out into an
 *                              implementation method that is given the element
 *                              name as a parameter. testSelectorTransformation
 *                              now passes in pane and xfoption into
 *                              doTestSelectorTransformation
 * 23-Jan-03    Byron           VBM:2003012211 - Added testDoProtocolString.
 * 27-Jan-03    Doug            VBM:2003012408 - Added test cases for the
 *                              doSelectInput() method.
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses
 *                              inheritance to mirror the structure of the
 *                              Protocol heirarchy it is testing, and uses the
 *                              new shared Test... versions of classes rather
 *                              their own "cut & paste" inner classes. Then,
 *                              add the test for xfaction generating accesskey.
 * 06-Mar-03    Sumit           VBM:2003022605 - Moved static constants up to
 *                              DOMProtocolTestCase
 * 14-Mar-03    Doug            VBM:2003030409 - Added the
 *                              testOpenDissectingPane() fixture
 * 21-Mar-03    Sumit           VBM:2003022828 - context member var has been
 *                              made protected
 * 28-Mar-03    Geoff           VBM:2003031711 - Add test for renderAltText,
 *                              change name of parent class to reflect
 *                              refactoring.
 * 09-Apr-03    Sumit           VBM:2003032713 - Tests for render support for
 *                              menu item groups
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Add
 *                              testAddPhoneNumberAttributes and various
 *                              associated helper methods.
 * 17-Apr-03    Geoff           VBM:2003041505 - Expanded static objects into
 *                              instance variables.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add ProtocolException
 *                              declarations where necessary.
 * 27-May-03    Allan           VBM:2003052004 - Modified
 *                              renderMenuItemSeparatorImplTest to provide
 *                              feedback by using assertEquals instead of
 *                              assertTrue.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.DOMAssertionUtilities;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocolTestAbstract;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.DoSelectInputTestHelper;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.PageHead;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.SelectOption;
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
import com.volantis.mcs.themes.properties.MCSImageKeywords;
import com.volantis.mcs.themes.properties.MCSMenuOrientationKeywords;
import com.volantis.mcs.themes.properties.MCSSelectionListOptionLayoutKeywords;
import com.volantis.mcs.themes.properties.MCSSelectionListStyleKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.impl.StylesImpl;
import com.volantis.styling.values.MutablePropertyValues;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.xml.sax.SAXException;

/**
 * This class tests underlying functionality of the XHTMLBasic protocol
 * @author byron
 *
 * @todo extract TestXHTMLBasic and make the Device*CssCandidate test cases use it too.
 * (In IDEA, Control-H on VolantisProtocol will show all subclasses...)
 */
public class XHTMLBasicTestCase extends DOMProtocolTestAbstract {

    private static final String STYLE = "myStyleClass";
    private static final String ELEMENT_NAME = "myElementName";
    protected static final String PANE_NAME = "myPaneName";
    protected static final String CLASS_NAME = "VF-0";
    private static final String CLASS_NAME_2 = "VF-9";
    private static final String CLASS_NAME_3 = "VF-3";

    private static final String MCS_IMAGE_URL = "/test/mcsImage.mimg";
    private static final String FORM_IMAGE_URL = "/test/formImage.mimg";

    protected Pane pane;

    private XHTMLBasic protocol;
    private XHTMLBasicTestable testable;

    protected TestMarinerPageContext context;

    private Element element;
    private MCSAttributes attribute;

    /**
     * Creates new XHTMLBasicTestCase instance.
     */
    public XHTMLBasicTestCase(String name) {
        super(name);
    }

    // javadoc inherited.
    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestXHTMLBasicFactory(),
                internalDevice);
        return protocol;
    }

    // javadoc inherited.
    protected void setTestableProtocol(VolantisProtocol protocol,
            VolantisProtocolTestable testable) {
        super.setTestableProtocol(protocol, testable);

        this.protocol = (XHTMLBasic) protocol;
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
        context.setDeviceName(DEVICE_NAME);
    }

    /**
     * Reset the element
     */
    private void resetElement() {
        element = domFactory.createElement();
        element.setName(ELEMENT_NAME);
    }

    /**
     * Test case permutations where the following items are always true
     * - No device theme
     * - HAS alt class name
     * This test case should test the area outside the actual transformation.
     * ie. the following items should not be transformed (use 'VE-')
     */
    public void notestNoDeviceThemeWithAltClassName() throws RepositoryException {
        privateSetUp();

        assertTrue("Protocol shouldn't be null", protocol != null);
        assertTrue("Element shouldn't be null", element != null);

        String className = "myClassName";
        element.setAttribute("class", className);

        String altClassName = "alt" + className;

        String UNCHANGED = "Element should be unchanged";
        String CHANGED = "Element should change";

        // We have a protcol, an element and an attribute
        // 1) no element, no style
        String originalAttribute = element.getAttributeValue("class");
//        protocol.addCoreAttributes(element, attribute, null);
        String result = element.getAttributeValue("class");
        assertEquals(UNCHANGED, originalAttribute, result);
        resetElement();


        // 2) no element, has style
//        attribute.setStyleClass(STYLE);
//        protocol.addCoreAttributes(element, attribute, altClassName);
        result = element.getAttributeValue("class");
        assertEquals(CHANGED, STYLE, result);
        resetElement();


        // 3) has element, no style
//        attribute.setStyleClass(null);
        attribute.setTagName("p");
        element.setAttribute("class", className);
//        protocol.addCoreAttributes(element, attribute, altClassName);
        result = element.getAttributeValue("class");
        assertEquals(CHANGED + ": " + result, altClassName, result);
        resetElement();

        // 4) has element, has style
//        attribute.setStyleClass(STYLE);
        attribute.setTagName("br");
        element.setAttribute("class", className);
//        protocol.addCoreAttributes(element, attribute, altClassName);
        result = element.getAttributeValue("class");
        assertEquals(CHANGED + ": " + result, altClassName, result);
        resetElement();
    }

    /**
     * Test case permutations where the following items are always true
     * - No device theme
     * - no alt class name
     * This test case should test the area outside the actual transformation.
     * ie. the following items should not be transformed (use 'VE-')
     *
     * @todo XDIME-CP
     */
    public void notestNoDeviceThemeAndNoAltClassName() throws RepositoryException {
        privateSetUp();

        assertTrue("Protocol shouldn't be null", protocol != null);
        assertTrue("Element shouldn't be null", element != null);

        String className = "myClassName";
        element.setAttribute("class", className);

        String UNCHANGED = "Element should be unchanged";
        String CHANGED = "Element should change";

        // We have a protcol, an element and an attribute
        // 1) no element, no style
        String elementAttribute = element.getAttributeValue("class");
        protocol.addCoreAttributes(element, attribute);
        String result = element.getAttributeValue("class");
        assertEquals(UNCHANGED, elementAttribute, result);
        resetElement();


        // 2) no element, has style
//        attribute.setStyleClass(STYLE);
        protocol.addCoreAttributes(element, attribute);
        result = element.getAttributeValue("class");
        assertEquals(CHANGED, STYLE, result);
        resetElement();

        // 3) has element, no style
//        attribute.setStyleClass(null);
        attribute.setTagName("p");
        element.setAttribute("class", className);
        protocol.addCoreAttributes(element, attribute);
        result = element.getAttributeValue("class");
        assertEquals(UNCHANGED + ": " + result, className, result);
        resetElement();

        // 4) has element, has style
//        attribute.setStyleClass(STYLE);
        attribute.setTagName("br");
        element.setAttribute("class", className);
        protocol.addCoreAttributes(element, attribute);
        result = element.getAttributeValue("class");
        assertEquals(CHANGED + ": " + result, STYLE, result);
        resetElement();
    }

    /**
     * Test usesDivForMenus property.
     */
    public void testUsesDivForMenus() {
        VolantisProtocol protocol = createTestableProtocol(internalDevice);
        assertTrue("XHTMLBasic uses div for menus. usesDivForMenus should be true",
                   protocol.usesDivForMenus());
    }

    /**
     * This test case should test the permutations when there is a device theme
     * associated with the protocol and the multiple style class is set to 'selector'
     * - Multiple style class set to 'none'
     * - has device theme
     * - always has mariner element
     *
     * todo XDIME-CP: Fix when done layout transformation.
     */
    public void notestSelectorTransformation() throws RepositoryException {
        privateSetUp();

        doTestSelectorTransformation("pane");
        doTestSelectorTransformation("xfoption");
    }

    /**
     * implementation for testSelectorTransformation allowing the test to be
     * performed with any element.
     * @param testElement
     */
    public void doTestSelectorTransformation(String testElement) {
        // Set multiple style class support to 'selector'
//        testable.setSupportsMultipleAttributeClasses(true);
//        testable.setSupportsMultipleStyleClasses(true);

        // Set mariner element
        String marinerElement = testElement;
        attribute.setTagName(marinerElement);

        // Variable parameters that may be set are:
        // - altClassName (null or valid value)
        // - styleClass( null or valid value)
        String altClassName = "VF-2";
        String styleClass = "bg-purple";


        // 1) altclassname is null, styleClass is null
//        attribute.setStyleClass(null);
//        protocol.addCoreAttributes(element, attribute, null);
        String result = element.getAttributeValue("class");
        String expected = "VE-" + marinerElement;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();

        // 2) altclassname has value, styleClass is null
//        attribute.setStyleClass(null);
//        protocol.addCoreAttributes(element, attribute, altClassName);
        result = element.getAttributeValue("class");
        expected = "VE-" + marinerElement + " " + altClassName;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();

        // 3) altclassname is null, styleClass has value
//        attribute.setStyleClass(styleClass);
//        protocol.addCoreAttributes(element, attribute, null);
        result = element.getAttributeValue("class");
        expected = "VE-" + marinerElement + " " + styleClass;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();

        // 4) altclassname has value, styleClass has value
//        attribute.setStyleClass(styleClass);
//        protocol.addCoreAttributes(element, attribute, altClassName);
        result = element.getAttributeValue("class");
        expected = "VE-" + marinerElement + " " + styleClass + " " + altClassName;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();
    }


    /**
     * This test case should test the permutations when there is a device theme
     * associated with the protocol and the multiple style class is set to 'attribute'
     * <ul>
     *   <li>Multiple style class set to 'attribute' only</li>
     *   <li>has device theme</li>
     *   <li>always has mariner element</li>
     * </ul>
     *
     * todo XDIME-CP: Fix when done layout transformation.
     */
    public void notestAttributeTransformation() throws RepositoryException {
        privateSetUp();

        resetElement();
        // Set multiple style class support to 'attribute'
//        testable.setSupportsMultipleAttributeClasses(true);
//        testable.setSupportsMultipleStyleClasses(false);

        // Set mariner element
        String marinerElement = "pane";
        attribute.setTagName(marinerElement);

        // Variable parameters that may be set are:
        // - altClassName (null or valid value)
        // - styleClass( null or valid value)
        String altClassName = "VF-1";
        String styleClass = "fg-red";


        // 1) altclassname is null, styleClass is null
//        attribute.setStyleClass(null);
//        protocol.addCoreAttributes(element, attribute, null);
        String result = element.getAttributeValue("class");
        String expected = "VE-" + marinerElement;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();

        // 2) altclassname has value, styleClass is null
//        attribute.setStyleClass(null);
//        protocol.addCoreAttributes(element, attribute, altClassName);
        result = element.getAttributeValue("class");
        expected = "VE-" + marinerElement + " " + altClassName;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();

        // 3) altclassname is null, styleClass has value
//        attribute.setStyleClass(styleClass);
//        protocol.addCoreAttributes(element, attribute, null);
        result = element.getAttributeValue("class");
        expected = "VE-" + marinerElement + "-" + styleClass + " VE-" +
                marinerElement + " " + styleClass;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();

        // 4) altclassname has value, styleClass has value
//        attribute.setStyleClass(styleClass);
//        protocol.addCoreAttributes(element, attribute, altClassName);
        result = element.getAttributeValue("class");
        expected = "VE-" + marinerElement + "-" + styleClass + " VE-" +
                marinerElement + " " + styleClass + " " + altClassName;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();
    }


    /**
     * This test case should test the permutations when there is a device theme
     * associated with the protocol and the multiple style class is set to 'none'
     * <ul>
     *   <li>Multiple style class set to 'none'</li>
     *   <li>has device theme</li>
     *   <li>always has mariner element</li>
     * </ul>
     *
     * todo XDIME-CP: Fix when done layout transformation.
     */
    public void notestNoneTransformation() throws RepositoryException {
        privateSetUp();

        // Set multiple style class support to 'none'
//        testable.setSupportsMultipleAttributeClasses(false);
//        testable.setSupportsMultipleStyleClasses(false);

        // Set mariner element
        String marinerElement = "pane";
        attribute.setTagName(marinerElement);

        // Variable parameters that may be set are:
        // - altClassName (null or valid value)
        // - styleClass( null or valid value)

        String altClassName = CLASS_NAME;
        String styleClass = "bg-blue";

        // 1) altclassname is null, styleClass is null
//        attribute.setStyleClass(null);
//        protocol.addCoreAttributes(element, attribute, null);
        String result = element.getAttributeValue("class");
        String expected = "VE-" + marinerElement;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();

        // 2) altclassname has value, styleClass is null
//        attribute.setStyleClass(null);
//        protocol.addCoreAttributes(element, attribute, altClassName);
        result = element.getAttributeValue("class");
        expected = altClassName;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();

        // 3) altclassname is null, styleClass has value
//        attribute.setStyleClass(styleClass);
//        protocol.addCoreAttributes(element, attribute, null);
        result = element.getAttributeValue("class");
        expected = "VE-" + marinerElement + "-" + styleClass;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();

        // 4) altclassname has value, styleClass has value
//        attribute.setStyleClass(styleClass);
//        protocol.addCoreAttributes(element, attribute, altClassName);
        result = element.getAttributeValue("class");
        expected = "VE-" + marinerElement + "-" + styleClass;
        assertEquals(getMessage(expected, result), expected, result);
        resetElement();
    }
    /**
     *
     */
    private String getMessage(String expected, String got) {
        return "Expected: '" + expected + "', got '" + got + "'";
    }
   /**
    * Transformation table
    * <pre>
    *       +==========+==========+=======+===================================+
    *       | element  |  Alt-    | Style | RESULT                            |
    *       |          | Classname| Style | RESULT                            |
    *+======+==========+==========+=======+===================================+
    *|      |  pane0   |  -       |  -    | VE-pane0                          |
    *|      +----------+----------+-------+-----------------------------------+
    *|      |  pane0   |  VF-0    |  -    | VF-0                              |
    *| None +----------+----------+-------+-----------------------------------+
    *|      |  pane0   |  -       | bgblue| VE-pane0-bgblue                   |
    *|      +----------+----------+-------+-----------------------------------+
    *|      |	pane0   |  VF-0   | bgblue| VE-pane0-bgblue                   |
    *+======+==========+==========+=======+===================================+
    *|      |  pane1   |  -       |  -    | VE-pane1                          |
    *|      +----------+----------+-------+-----------------------------------+
    *|      |  pane1   |  VF-1    |  -    | VE-pane1 VF-1                     |
    *| Attr +----------+----------+-------+-----------------------------------+
    *|      |  pane1   |  -       | fgred | VE-pane1-fgred VE-pane1 fgred     |
    *|      +----------+----------+-------+-----------------------------------+
    *|      |  pane1   |  VF-1    | fgred | VE-pane1-fgred VE-pane1 fgred VF-1|
    *+======+==========+==========+=======+===================================+
    *|      |  pane2   |  -       |  -    | VE-pane2                          |
    *|      +----------+----------+-------+-----------------------------------+
    *|      |  pane2   |  VF-2    |  -    | VE-pane2 VF-2                     |
    *|Select+----------+----------+-------+-----------------------------------+
    *|      |  pane2   |  -       | bgblue| VE-pane2 bgblue                   |
    *|      +----------+----------+-------+-----------------------------------+
    *|      |  pane2   |  VF-2    | bgblue| VE-pane2 bgblue VF-2              |
    *+======+==========+==========+=======+===================================+
    * </pre>
    *
    * todo XDIME-CP: Fix when done layout transformation.
    */
   public void notestAddClassAttribute() throws RepositoryException {
       privateSetUp();

       // Note that this test involves the following methods which rely on
       // setUp and tearDown to called after each test. Hence they cannot
       // be run from within this test.
//       doTestNoneTransformation();
//       doTestAttributeTransformation();
//       doTestSelectorTransformation();
//       doTestNoDeviceThemeAndNoAltClassName();
//       doTestNoDeviceThemeWithAltClassName();
   }

    /**
     * test open pane
     *
     * todo XDIME-CP: Fix when done layout transformation.
     */
    public void notestOpenPane() throws RepositoryException, IOException,
            ParserConfigurationException, SAXException {
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
//        attributes.setBackgroundColour("#ff00ff");
//        attributes.setBorderWidth("5");
//        attributes.setCellPadding("6");
//        attributes.setCellSpacing("7");
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
        context.setDevice(INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice(DEVICE_NAME, new HashMap(), null)));
        
        DOMOutputBuffer dom = new DOMOutputBuffer();
        Element containerCell;

        dom.initialise();

        containerCell = dom.openElement("td");
        containerCell.setAttribute("valign", "top");

        Pane pane1 = new Pane(null);
        pane1.setName(PANE_NAME);

        PaneAttributes attributes = new PaneAttributes();
        attributes.setStyles(StylesBuilder.getStyles(
                "background-color: #ff0000"));
        attributes.setPane(pane1);

//        context.setStyleClassName(CLASS_NAME);
        protocol.setMarinerPageContext(context);
        protocol.openPane(dom, attributes);

        Element cell = null;
        try {
            cell = dom.closeElement("td");
            dom.closeElement("tr");
            dom.closeElement("table");
        } catch (IllegalStateException e) {
            fail("Expected table/tr/td elements not found");
        }

        if (containerCell != null) {
            if ("td".equals(containerCell.getName())) {
                assertEquals("Vertical alignment should have been preserved",
                             containerCell.getAttributeValue("valign"),
                             cell.getAttributeValue("valign"));
            }
        }
    }

    /**
     * Test the method: closePane
     */
    public void testClosePane() throws Exception {
        context = new TestMarinerPageContext();
        protocol.setMarinerPageContext(context);
        TestDeviceLayoutContext deviceContext = new TestDeviceLayoutContext();
        context.pushDeviceLayoutContext(deviceContext);

        DOMOutputBuffer dom = new DOMOutputBuffer();
        dom.initialise();
        PaneAttributes attributes = new PaneAttributes();
        Pane pane = new Pane(null);
        attributes.setPane(pane);

        Element body;
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        body = dom.openStyledElement("body", attributes);
        dom.openElement("table");
        dom.openElement("tr");
        dom.openElement("td");

        dom.appendEncoded("Example");

        protocol.closePane(dom, attributes);

        assertSame("The DOM's current element isn't the body element",
                   dom.getCurrentElement(),
                   body);
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
        pane.setAttribute(FormatConstants.MAXIMUM_CONTENT_SIZE_ATTRIBUTE,
                          maxContentSize);

        DissectingPaneAttributes atts = new DissectingPaneAttributes();
        atts.setDissectingPane(pane);
        atts.setInclusionPath(inclusionPath);
        atts.setIsNextLinkFirst(nextLinkFirst);

        protocol.setDissecting(true);
        protocol.openDissectingPane(buffer, atts);

        if (protocol.isDissectionSupported()) {
            Element el = null;
            try {
                el = buffer.closeElement("div");
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
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        Element anchor;
        Node node;

        buffer.initialise();
        attributes.setHref("http://www.volantis.com/page");
        attributes.setLinkText("Next");
        attributes.setShortcut("N");

        protocol.doShardLink(buffer, attributes);

        node = buffer.getCurrentElement().getHead();

        assertNotNull("Should have found a node",
                      node);

        assertTrue("Node should be an element",
                   node instanceof Element);

        anchor = (Element) node;

        assertEquals("Unexpected type of element found",
                     "a",
                     anchor.getName());
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

        String classname = null; //protocol.getFormatStyleClass(pane);

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

    public void testActionInput() throws Exception {
        checkActionInput(true, true);
    }

    /**
     * Test the doActionInput method
     */
    public void checkActionInput(boolean generateAccessKey,
            boolean generateStyles) throws Exception {
        privateSetUp();

        if (generateStyles) {
            checkActionInputType();
            doTest2002120611();
        }
        // @todo refactor so that that these test methods work without styles

        checkActionInputShortcut(generateAccessKey, generateStyles);
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
                     "<input name=\"" + name + "\" " +
                     "type=\"" + actionType + "\" value=\"" + caption  + "\"/>",
                     actualResult);

        // Setup test for normal behaviour for input tag generation (perform)
        buffer = new DOMOutputBuffer();
        buffer.initialise();
        actionType = "perform";
        attributes.setType(actionType);

        protocol.doActionInput(buffer, attributes);
        actualResult = bufferToString(buffer);

        assertEquals("DOM buffer should match",
                     "<input name=\"" + name + "\" " +
                     "type=\"" + "submit" + "\" value=\"" + caption  + "\"/>",
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
                     "type=\"" + actionType + "\" value=\"" + caption  + "\"/>",
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
                      "tabindex=\"1\" " +
                     "type=\"" + actionType + "\" value=\"" + caption  + "\"/>",
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
                     "type=\"" + "submit" + "\" value=\"" + caption  + "\"/>",
                     actualResult);
    }

    /**
     * Get the name of the menu element for the XHTMLBasic protocol. Sub-classes
     * should override this method if their menu element is different from
     * their parent class.
     * @return "div".
     */
    protected String getMenuElementName() {
        return "div";
    }

    protected void checkActionInputShortcut(boolean generated,
            boolean styled) throws ProtocolException {

        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();
        XFActionAttributes attributes = new XFActionAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());

        // Setup test for normal behaviour for input tag generation (submit)
        String shortcut = "1";
        String name = "My Name";
        String actionType = "submit";
        String caption = "This is my caption";
        //String value = "This is my value";

        attributes.setShortcut(new LiteralTextAssetReference(shortcut));
        attributes.setName(name);
        attributes.setType(actionType);
        attributes.setCaption(new LiteralTextAssetReference(caption));
        //attributes.setValue(value);

        protocol.doActionInput(buffer, attributes);
        String actualResult = bufferToString(buffer);
        assertEquals("DOM buffer should match",
                "<input " +
                    (generated ? "accesskey=\"" + shortcut + "\" " : "") +
                    "name=\"" + name + "\" " +
                    "type=\"" + actionType + "\" " +
                    "value=\"" + caption + "\"/>",
                actualResult);
    }

    /**
     * Test for doProtocolString
     */
    public void testDoProtocolString() throws IOException, RepositoryException {
        context = new TestMarinerPageContext();
        protocol.setMarinerPageContext(context);

        checkDoProtocolString(protocol, getExpectedProtocolString());
    }

    /**
     * Other protocols may have a different expected protocol string.
     *
     * @return the expected protocol string.
     */
    protected String getExpectedProtocolString() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE html " +
        "PUBLIC \"-//W3C//DTD XHTML Basic 1.0//EN\"" +
        " \"http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd\">";
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

        sb.append("<select ");
        sb.append("id=\"").append(atts.getId()).append("\" ");
        if(multiSelect) {
            sb.append("multiple=\"multiple\" ");
        }
        sb.append("name=\"").append(atts.getName()).append("\" ");
        if(multiSelect) {
            sb.append("size=\"").append(rowCount).append("\" ");
        }
        sb.append("tabindex=\"").append(tabIndex).append("\" ");
        sb.append("title=\"")
                .append(atts.getPrompt().getText(TextEncoding.PLAIN))
                .append("\">");

        for(int i=0; i<selectedOptions.length; i++) {
            sb.append("<option id=\"OptionId\" ");
            if(selectedOptions[i]) {
                sb.append("selected=\"selected\" ");
            }
            sb.append("title=\"Prompt").append(i+1)
              .append("\" value=\"Value").append(i+1)
              .append("\">Caption").append(i+1).append("</option>");
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

        sb.append("<select ");
        sb.append("id=\"").append(atts.getId()).append("\" ");
        sb.append("name=\"").append(atts.getName()).append("\" ");
        sb.append("title=\"").append(atts.getPrompt()
                .getText(TextEncoding.PLAIN))
                .append("\">");

        for(int i=0; (i<testable.getMaxOptGroupNestingDepth() && i<3); i++) {
            sb.append("<optgroup id=\"GroupId\" ");
            sb.append("label=\"Group").append(i+1);
            sb.append("\" title=\"Prompt").append(i+1).append("\">");
        }

        sb.append("<option id=\"OptionId\" ");
        sb.append("title=\"Prompt1\" value=\"Value1\">Caption1</option>");

        for(int i=0; (i<testable.getMaxOptGroupNestingDepth() && i<3); i++) {
            sb.append("</optgroup>");
        }


        if(testable.getMaxOptGroupNestingDepth() > 0) {
            sb.append("<optgroup id=\"GroupId\" ");
            sb.append("label=\"Group4\" title=\"Prompt4\">");
        }

        sb.append("<option id=\"OptionId\" ");
        sb.append("title=\"Prompt2\" value=\"Value2\">Caption2</option>");

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

       sb.append("<div ");
       sb.append("id=\"" + atts.getId() + "\">");

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
            sb.append("id=\"OptionId\" ");
            sb.append("name=\"").append(atts.getName()).append("\" ");
            sb.append("title=\"").append("Prompt").append(i+1).append("\" ");
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
        // We changed the default for (X)HTML to render a logical SPAN to
        // avoid adding whitespace around the text. I believe SPAN might be
        // better as the default, but this would be a larger change.
        Element root = buffer.getCurrentElement();
        Element span = checkElementEquals("span", root.getHead());
        checkTextEquals(altText, span.getHead());
    }

    public void testAddPhoneNumberAttributes() throws Exception {
        Element element = domFactory.createElement();
        PhoneNumberAttributes attributes =
            createPhoneNumberAttributes(phoneNumberReference);
        element.setName("test");

        protocol.addPhoneNumberAttributes(element, attributes);

        checkAddPhoneNumberAttributes(element);
    }

    /**
     * Helper method that should check that the element contains the
     * appropriate attributes relating to the attributes set up in
     * {@link #createPhoneNumberAttributes}. This must be augmented by
     * specializations to ensure that attributes that should be generated
     * are checked and then removed, each specialization finally calling
     * the superclass version up the chain to this version where the element
     * is checked to be empty of attributes. Any attributes left at this
     * point are used to generate a failure of the test invoking this
     * method.
     *
     * @param element the element to be interrogated
     * @throws Exception if an error occurs
     */
    protected void checkAddPhoneNumberAttributes(Element element)
        throws Exception {
        assertNoAttributes(element);
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
    private void doTestBufferSelector(XHTMLBasic.BufferSelector selector,
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
        XHTMLBasic.BufferSelector selector;
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
        return "application/xhtml+xml";
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
     * Test the image element. If there is an AssetURLSuffix set then it should
     * be appended to the image URL.
     */
    public void testDoImageSuffix() throws ProtocolException, RepositoryException {

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

    /**
     * Ensure that the protocol implements
     * {@link XHTMLBasic#enableNameIdentification} correctly for anchors.
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
        attributes.setStyles(StylesBuilder.getInitialValueStyles());
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
        // html 3.2, etc, do no generate id's.
        // DOMUtilities.assertAttributeEquals("id", idValue, element);
        DOMAssertionUtilities.assertAttributeEquals("name", expectedNameValue, element);
    }

    /**
     * Ensure that the protocol implements
     * {@link XHTMLBasic#enableNameIdentification} correctly for anchors.
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
        attributes.setStyles(StylesBuilder.getInitialValueStyles());
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
     * Ensure that valid image tags are generated
     * @throws ProtocolException
     */
    public void testImageValid() throws ProtocolException, RepositoryException {
        // Set up the protocol and dependent objects.
        privateSetUp();
        DOMOutputBuffer buffer = new DOMOutputBuffer();
        buffer.initialise();

        // Initialise our protocol attributes with an id attribute.
        ImageAttributes attributes = new ImageAttributes();
        attributes.setStyles(StylesBuilder.getInitialValueStyles());
        attributes.setLocalSrc(false);
        attributes.setSrc("myImage.jpg");
        attributes.setAltText("Alternate Text");

        protocol.doImage(buffer, attributes);

        // valid src and alt generates <img src="url" alt="text" />
        Element root = buffer.getCurrentElement();
        Element element = (Element) root.getHead();

        DOMAssertionUtilities.assertElement("img", element);
        DOMAssertionUtilities.assertAttributeEquals("src","myImage.jpg",element);
        DOMAssertionUtilities.assertAttributeEquals("alt","Alternate Text",element);
    }

    /**
     * Ensure that valid image tags are generated when there is no source
     * @throws ProtocolException
     */
    public void testImageNoSrc() throws ProtocolException, RepositoryException {
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
        Element span = (Element) root.getHead();

        DOMAssertionUtilities.assertElement("span", span);

        Text altText = (Text)span.getHead();
        String txtAltText = new String(altText.getContents(),
                0, expected.length());
        assertEquals("Incorrect Text", expected, txtAltText);
    }

    /**
     * Ensure that the protocol implements
     * {@link XHTMLBasic#enableNameIdentification} correctly for anchors.
     *
     * @throws ProtocolException
     */
    public void testImageNoSrcNoAlt() throws ProtocolException, RepositoryException {
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
     * Test the adding of grid attributes
     */
    public void testAddGridAttributes() throws Exception {
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        resetElement();
        context = new TestMarinerPageContext();
        context.pushRequestContext(requestContext);
        context.setDeviceName("PC");
        context.setDevice(INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice(DEVICE_NAME, new HashMap(), null)));
        protocol.setMarinerPageContext(context);

        // INPUT: no style class, a device name that doesn't end with 'Netscape4'.
        // EXPECTED: Should result no borderwidth and style class.
        GridAttributes attributes = new GridAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        protocol.addGridAttributes(element, attributes);

        // INPUT: a device name that ends with 'Netscape4'.
        // EXPECTED: Should result in a '0' borderwidth and no style class.
        attributes = new GridAttributes();
        attributes.setStyles(StylesBuilder.getDeprecatedStyles());
        resetElement();
        context.setDeviceName("xxxxxxNetscape4");
        protocol.addGridAttributes(element, attributes);
    }

    public void testXFOptionStyles() throws Exception {
        privateSetUp();
        final DoSelectInputTestHelper helper = new DoSelectInputTestHelper();

        final XFSelectAttributes attributes = helper.buildSelectAttributes();
        Styles styles = attributes.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();
        attributes.setMultiple(false);

        // add some optionsDOMP
        helper.addOption(attributes, "Caption1", "Prompt1", "Value1", true);
        helper.addOption(attributes, "Caption2", "Prompt2", "Value2", false);
        final Styles captionStyles =
                StylesBuilder.getCompleteStyles("font-weight: bold");
        ((SelectOption) attributes.getOptions().get(0)).setCaptionStyles(captionStyles);

        // create style
        propertyValues.setComputedValue(
                StylePropertyDetails.MCS_SELECTION_LIST_STYLE,
                MCSSelectionListStyleKeywords.CONTROLS);
        propertyValues.setComputedValue(
                StylePropertyDetails.MCS_SELECTION_LIST_OPTION_LAYOUT,
                MCSSelectionListOptionLayoutKeywords.CONTROL_FIRST);
        propertyValues.setComputedValue(
                StylePropertyDetails.MCS_MENU_ORIENTATION,
                MCSMenuOrientationKeywords.VERTICAL);

        // initialise the buffer
        final TestDOMOutputBuffer buffer = new TestDOMOutputBuffer();
        testable.setCurrentBuffer(attributes.getEntryContainerInstance(), buffer);
        buffer.initialise();

        // select
        protocol.doSelectInput(attributes);

        // extract the result
        final Document document = domFactory.createDocument();
        Element root = buffer.getRoot();
        if (root.getName() == null) {
            root.setName("testRoot");
        }
        document.addNode(root);
        StyledDOMTester tester = new StyledDOMTester(true);
        final String result = tester.render(document);

        assertEquals(tester.normalize(getExpectedXFSelectString()), result);
    }

    /**
     * Expected result for the <code>testXFOptionStyles</code> test case.
     * @return the expected String, depending on the protocol used
     */
    protected String getExpectedXFSelectString() {
        return "<testRoot><div id=\"TestID\"><input id=\"OptionId\" " +
            "name=\"TestSelect\" title=\"Prompt1\" type=\"radio\" " +
            "value=\"Value1\"/><span style='font-weight: bold'>Caption1" +
            "</span><br/><input id=\"OptionId\" name=\"TestSelect\" " +
            "title=\"Prompt2\" type=\"radio\" value=\"Value2\"/>Caption2" +
            "</div></testRoot>";
    }

    /**
     * Verify that if the mcs-image property has a valid URL and
     * mcs-form-action-image is null, the mcs-image url is returned from
     * XHTMLBasic#getActionURL.
     */
    public void testGetActionURLWithMCSImageAndNullFormImage() {

        final StylesImpl styles = new StylesImpl(null, null);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_IMAGE,
                styleValueFactory.getURI(null, MCS_IMAGE_URL));

        String url = protocol.getActionImageURL(styles);
        if (protocol.supportsImageButtons) {
            assertEquals(MCS_IMAGE_URL, url);
        } else {
            assertNull(url);
        }
    }

    /**
     * Verify that if the mcs-image property has a valid URL and
     * mcs-form-action-image is MarinerImageEnumeration#NONE, the mcs-image url
     * is returned from XHTMLBasic#getActionURL.
     */
    public void testGetActionURLWithMCSImageAndFormImageOfNone() {

        final StylesImpl styles = new StylesImpl(null, null);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_IMAGE,
                styleValueFactory.getURI(null, MCS_IMAGE_URL));
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_FORM_ACTION_IMAGE,
                MCSImageKeywords.NONE);

        String url = protocol.getActionImageURL(styles);
        if (protocol.supportsImageButtons) {
            assertEquals(MCS_IMAGE_URL, url);
        } else {
            assertNull(url);
        }
    }

    /**
     * Verify that if both the mcs-image property and mcs-form-action-image
     * have valid URLs, the mcs-image url is returned from
     * XHTMLBasic#getActionURL
     */
    public void testGetActionURLWithMCSImageAndFormImage() {
        final StylesImpl styles = new StylesImpl(null, null);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_IMAGE,
                styleValueFactory.getURI(null, MCS_IMAGE_URL));
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_FORM_ACTION_IMAGE,
                styleValueFactory.getURI(null, FORM_IMAGE_URL));

        String url = protocol.getActionImageURL(styles);
        if (protocol.supportsImageButtons) {
            assertEquals(MCS_IMAGE_URL, url);
        } else {
            assertNull(url);
        }
    }

    /**
     * Verify that if the mcs-form-action-image property has a valid URL and
     * mcs-image is null, the mcs-form-action-image url is returned from
     * XHTMLBasic#getActionURL.
     */
    public void testGetActionURLWithNullMCSImageAndFormImage() {
        final StylesImpl styles = new StylesImpl(null, null);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_FORM_ACTION_IMAGE,
                styleValueFactory.getURI(null, FORM_IMAGE_URL));

        String url = protocol.getActionImageURL(styles);
        if (protocol.supportsImageButtons) {
            assertEquals(FORM_IMAGE_URL, url);
        } else {
            assertNull(url);
        }
    }

    /**
     * Verify that if the mcs-form-action-image property has a valid URL and
     * mcs-image is set to MarinerImageEnumeration#NONE, the
     * mcs-form-action-image url is returned from XHTMLBasic#getActionURL.
     */
    public void testGetActionURLWithMCSImageOfNoneAndMCSFormImage() {
        final StylesImpl styles = new StylesImpl(null, null);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_IMAGE,
                MCSImageKeywords.NONE);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_FORM_ACTION_IMAGE,
                styleValueFactory.getURI(null, FORM_IMAGE_URL));

        String url = protocol.getActionImageURL(styles);
        if (protocol.supportsImageButtons) {
            assertEquals(FORM_IMAGE_URL, url);
        } else {
            assertNull(url);
        }
    }

    /**
     * Verify that if both the mcs-image and mcs-form-action-image properties
     * have null values, XHTMLBasic#getActionURL returns null.
     */
    public void testGetActionURLWithNoImages() {
        final StylesImpl styles = new StylesImpl(null, null);
        String url = protocol.getActionImageURL(styles);
        assertNull(url);
    }

    /**
     * Verify that if both the mcs-image and mcs-form-action-image properties
     * are set to MarinerImageEnumeration#NONE, XHTMLBasic#getActionURL returns
     * null.
     */
    public void testGetActionURLWithImagesSetToNone() {
        final StylesImpl styles = new StylesImpl(null, null);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_IMAGE,
                MCSImageKeywords.NONE);
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.MCS_FORM_ACTION_IMAGE,
                MCSImageKeywords.NONE);
        String url = protocol.getActionImageURL(styles);
        assertNull(url);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10799/1	geoff	VBM:2005081506 Port 2005071314 forward to MCS

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 09-Nov-05	10201/3	emma	VBM:2005102606 Fixing various styling bugs

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 07-Nov-05	10096/1	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 02-Nov-05	10048/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 01-Nov-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 09-Nov-05	10221/1	emma	VBM:2005102606 Forward port: fixing various styling bugs

 09-Nov-05	10201/3	emma	VBM:2005102606 Fixing various styling bugs

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 07-Nov-05	10126/1	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 07-Nov-05	10096/1	ianw	VBM:2005101918 Change rendering of mcs-rows to use correct styling, also fix ClassCastException in AssetGroups

 01-Nov-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 03-Oct-05	9600/7	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 30-Sep-05	9600/4	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 02-Oct-05	9590/6	schaloner	VBM:2005092204 Migrated XMLLayoutAccessor and XMLDeviceLayoutAccessor to JiBX

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 02-Oct-05	9652/2	gkoch	VBM:2005092204 Tests for layoutFormat marshaller/unmarshaller

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 05-Sep-05	9407/4	pduffin	VBM:2005083007 Removed old themes model

 02-Sep-05	9407/2	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9375/4	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 25-Aug-05	9370/2	gkoch	VBM:2005070507 xform select option to store caption styles instead of caption (style) class

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/5	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/5	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 23-Jun-05	8833/4	pduffin	VBM:2005042901 Addressing review comments

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/9	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 09-Jun-05	8665/6	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

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

 20-Oct-04	5816/4	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 19-Oct-04	5816/2	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 12-Oct-04	5778/2	adrianj	VBM:2004083106 Provide styling engine API

 11-Oct-04	5744/2	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 22-Jul-04	4713/11	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 02-Jul-04	4713/8	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 29-Jun-04	4720/4	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 28-Jun-04	4685/20	steve	VBM:2004050406 Remove empty span around alt text

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 05-May-04	4167/3	steve	VBM:2004042901 Fix up javadoc

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/1	steve	VBM:2004042901 Style class rendering fix

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 23-Mar-04	3512/3	allan	VBM:2004032205 MCS performance enhancements.

 22-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 15-Mar-04	3422/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 19-Feb-04	3148/1	geoff	VBM:2003121501 XHTML basic throws exception when using dissecting panes

 19-Feb-04	3138/1	geoff	VBM:2003121501 XHTML basic throws exception when using dissecting panes

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 03-Nov-03	1760/1	philws	VBM:2003031710 Port image alt text component reference handling from PROTEUS

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 04-Sep-03	1015/7	geoff	VBM:2003072208 Style Class on spatial iterated pane not set on table cell in generated markup (supermerge)

 26-Aug-03	1015/4	geoff	VBM:2003072208 Style Class on spatial iterated pane not set on table cell in generated markup (supermerge fixes)

 08-Aug-03	1015/1	geoff	VBM:2003072208 merge from Mimas

 08-Aug-03	1011/1	geoff	VBM:2003072208 port from metis
 02-Sep-03	1305/1	adrian	VBM:2003082108 added new openwave6 xhtml protocol

 18-Aug-03	424/7	byron	VBM:2003022825 Task 580: Enhance behaviour of pane element within xfforms update

 18-Aug-03	424/5	byron	VBM:2003022825 Task 580: Enhance behaviour of pane element within xfforms update

 08-Aug-03	1004/1	geoff	VBM:2003072208 fix pane rendering in xhtml from netfront3

 01-Aug-03	728/5	adrian	VBM:2003052001 fixed merged conflicts again

 07-Jul-03	728/2	adrian	VBM:2003052001 fixed pane attribute generation
 18-Aug-03	1052/7	allan	VBM:2003073101 Update add display inline method.

 17-Aug-03	1052/4	allan	VBM:2003073101 Support styles on menu and menuitems

 14-Aug-03	1083/3	geoff	VBM:2003081305 fix merge problem

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 14-Aug-03	1063/1	geoff	VBM:2003081305 Port 2003060909 forward

 01-Aug-03	728/5	adrian	VBM:2003052001 fixed merged conflicts again

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/4	geoff	VBM:2003071405 now with fixed architecture

 23-Jul-03	807/2	geoff	VBM:2003071405 works and tested but no design review yet

 04-Jul-03	706/1	allan	VBM:2003070302 Added TestSuiteGenerator ant task. Run testsuite in a single jvm

 20-Jun-03	424/2	byron	VBM:2003022825 Enhance behaviour of pane element within xfform

 ===========================================================================
*/
