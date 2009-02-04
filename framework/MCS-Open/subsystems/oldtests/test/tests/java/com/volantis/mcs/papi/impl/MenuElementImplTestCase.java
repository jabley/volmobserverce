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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/MenuElementImplTestCase.java,v 1.2 2003/04/24 16:42:23 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Apr-03    Adrian          VBM:2003040903 - Created this testcase class to
 *                              test MenuElement 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.papi.MenuAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.menu.MenuRendererSelectorLocatorMock;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.ShortcutPropertiesMock;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuMock;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilderMock;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelectorMock;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererMock;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDeviceTestHelper;

import junitx.util.PrivateAccessor;

/**
 * This class tests the PAPIElement MenuElementImpl.
 */
public class MenuElementImplTestCase extends BlockElementTestAbstract {

    MenuElementImpl menuElement;
    MarinerRequestContextMock requestContext;
    MarinerPageContextMock pageContext;
    ShortcutPropertiesMock properties;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        menuElement = (MenuElementImpl) createTestablePAPIElement();
        requestContext = new MarinerRequestContextMock("requestContext",
                expectations);
        pageContext = new MarinerPageContextMock("pageContext", expectations);
        properties = new ShortcutPropertiesMock("properties", expectations);
    }

    // javadoc inherited from superclass
    protected PAPIElement createTestablePAPIElement() {
        return new MenuElementImpl();
    }

    /**
     * Test the method elementStart.
     */
    public void testElementStartAddsStyles() throws Exception {
        MenuElementImpl element =
                (MenuElementImpl) createTestablePAPIElement();

        // configure MCS
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                InternalDeviceTestHelper.createTestDevice());
        TestMarinerPageContext pageContext = new TestMarinerPageContext();

        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);

        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);

        // pane setup required by BlockElementImpl#exprElementStart
        Pane testPane =
                new Pane(new CanvasLayout());
        final String pane = "testPane";
        testPane.setName(pane);
        pageContext.addPaneMapping(testPane);
        pageContext.setCurrentPane(testPane);

        TestDeviceLayoutContext deviceLayoutContext =
                new TestDeviceLayoutContext();

        PaneInstance paneInstance = new TestPaneInstance();
        paneInstance.setFormat(testPane);
        paneInstance.setDeviceLayoutContext(deviceLayoutContext);

        deviceLayoutContext.setFormatInstance(testPane,
                NDimensionalIndex.ZERO_DIMENSIONS, paneInstance);
        pageContext.pushDeviceLayoutContext(deviceLayoutContext);
        pageContext.setFormatInstance(paneInstance);

        final PolicyReferenceResolverMock referenceResolverMock =
                new PolicyReferenceResolverMock("referenceResolverMock",
                        expectations);

        referenceResolverMock.expects.resolveQuotedTextExpression(null)
                .returns(null).any();

        pageContext.setPolicyReferenceResolver(referenceResolverMock);

        // set up menu atttributes
        MenuAttributes menuAttrs = new MenuAttributes();
        final String styleClass = "styleClass";
        menuAttrs.setStyleClass(styleClass);
        final String id = "id";
        menuAttrs.setId(id);
        menuAttrs.setPane(pane);

        int result = element.elementStart(requestContext, menuAttrs);

        assertTrue("Unexpected value returned from MenuElement.  Should have" +
                "been PROCESS_ELEMENT_BODY.",
                result == PAPIElement.PROCESS_ELEMENT_BODY);

        // retrieve the menu that was just added to the menuBuilder
        final Menu menu = (Menu)PrivateAccessor.getField(
                pageContext.getMenuBuilder(), "currentEntity");

        assertNotNull("Menu should not be null", menu);
        assertEquals("PAPI and Protocol pane names should match",
                testPane.getName(), menu.getPane().getStem());

        ElementDetails menuElementDetails = menu.getElementDetails();
        assertNotNull("Menu ElementDetails should not be null",
                menuElementDetails);
        assertNotNull("Menu styles should not be null",
                menuElementDetails.getStyles());
    }

    /**
     * Test the elementStartImpl method     
     */ 
    public void testElementStartImpl() throws Exception {
//todo: uncomment this test, or repair it, or defunct it after enhanced menus has been fully implemented
/*
        MenuElement element = (MenuElement) createTestablePAPIElement();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        MarinerRequestContext requestContext = new TestMarinerRequestContext();
        VolantisProtocol protocol = new VolantisProtocolStub();
        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);


        com.volantis.mcs.papi.MenuAttributes menuAttributes =
          (com.volantis.mcs.papi.MenuAttributes)createTestableBlockAttributes();

        final String plainText = "plaintext";
        menuAttributes.setType(plainText);

        final String styleClass = "styleClass";
        menuAttributes.setStyleClass(styleClass);

        final String id = "id";
        menuAttributes.setId(id);

        final String prompt = "<prompt>content</prompt>";
        menuAttributes.setPrompt(prompt);

        final String errmsg = "<nomatch>an error occured</nomatch>";
        menuAttributes.setErrmsg(errmsg);

        final String help = "<help>select an option from the list</help>";
        menuAttributes.setHelp(help);

        int result = element.elementStartImpl(requestContext, menuAttributes);

        assertTrue("Unexpected value returned from MenuElement.  Should have" +
                "been PROCESS_ELEMENT_BODY.",
                result == PAPIElement.PROCESS_ELEMENT_BODY);

        com.volantis.mcs.protocols.MenuAttributes pAttrs =
                element.pattributes;

        assertEquals("PAPI attribute and Protocol attribute values should" +
                "match.", plainText, pAttrs.getType());

        assertEquals("PAPI attribute and Protocol attribute values should" +
                "match.", styleClass, pAttrs.getStyleClass());

        assertEquals("PAPI attribute and Protocol attribute values should" +
                "match.", id, pAttrs.getId());

        assertEquals("PAPI attribute and Protocol attribute values should" +
                "match.", prompt, pAttrs.getPrompt());

        assertEquals("PAPI attribute and Protocol attribute values should" +
                "match.", errmsg, pAttrs.getErrmsg());

        assertEquals("PAPI attribute and Protocol attribute values should" +
                "match.", help, pAttrs.getHelp());
*/
    }

    /**
     * Test the elementEndImpl method.  Ensure that it calls protocol.doMenu()
     */
    public void testElementEndImpl() throws Exception {
//todo: uncomment this test, or repair it, or defunct it after enhanced menus has been fully implemented
/*
        MenuElement element = (MenuElement) createTestablePAPIElement();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        MarinerRequestContext requestContext = new TestMarinerRequestContext();

        final ResultObject resultObject = new ResultObject();
        final MenuAttributes attrs = element.pattributes;

        VolantisProtocol protocol = new VolantisProtocolStub() {
            // javadoc inherited from superclass
            public void doMenu(MenuAttributes attributes) {
                resultObject.setResult(attrs == attributes);
            }
        };

        pageContext.pushRequestContext(requestContext);
        pageContext.setProtocol(protocol);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        protocol.setMarinerPageContext(pageContext);

        int result = element.elementEndImpl(requestContext, null);

        assertTrue("Unexpected value returned from MenuElement.  Should have" +
                "been CONTINUE_PROCESSING.",
                result == PAPIElement.CONTINUE_PROCESSING);

        assertTrue("Either doMenu was not called or the wrong parameters" +
                "were passed into the method.", resultObject.getResult());
*/                
    }

    /**
     * Verify that the shortcut properties are not modified when the device
     * supports mixed content and doesn't insert a line break.
     *
     * @throws PAPIException if there was a problem running the test.
     */
    public void testElementEndImplWhenDeviceSupportsMixedContentAndDoesNotInsertLineBreak()
            throws PAPIException {

        // Set expectations.
        createElementEndImplExpectations();
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_SUPPORTS_MIXED_CONTENT).
                returns(DevicePolicyConstants.FULL_SUPPORT_POLICY_VALUE);
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_BREAKS_LINE).
                returns(DevicePolicyConstants.NO_SUPPORT_POLICY_VALUE);
        BlockAttributes blockAttributes = createTestableBlockAttributes();

        // Run test.
        menuElement.elementEndImpl(requestContext, blockAttributes);
    }

    /**
     * Verify that the shortcut properties are modified so that they don't
     * support span elements when the device does not support mixed content
     * and doesn't insert a line break.
     *
     * @throws PAPIException if there was a problem running the test.
     */
    public void testElementEndImplWhenDeviceDoesNotSupportMixedContentOrInsertLineBreak()
            throws PAPIException {

        // Set expectations.
        createElementEndImplExpectations();
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_SUPPORTS_MIXED_CONTENT).
                returns(DevicePolicyConstants.NO_SUPPORT_POLICY_VALUE);
        properties.expects.setSupportsSpan(false);
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_BREAKS_LINE).
                returns(DevicePolicyConstants.NO_SUPPORT_POLICY_VALUE);
        
        // Run test.
        final BlockAttributes blockAttributes = createTestableBlockAttributes();
        menuElement.elementEndImpl(requestContext, blockAttributes);
    }

    /**
     * Verify that the shortcut properties are modified so that shortcuts are
     * always active when the device supports mixed content and inserts a line
     * break before the link.
     *
     * @throws PAPIException if there was a problem running the test.
     */
    public void testElementEndImplWhenDeviceSupportsMixedContentAndInsertsLineBreakBefore()
            throws PAPIException {

        // Set expectations.
        createElementEndImplExpectations();
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_SUPPORTS_MIXED_CONTENT).
                returns(DevicePolicyConstants.FULL_SUPPORT_POLICY_VALUE);
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_BREAKS_LINE).returns("before");
        properties.expects.setActive(true);

        // Run test.
        final BlockAttributes blockAttributes = createTestableBlockAttributes();
        menuElement.elementEndImpl(requestContext, blockAttributes);
    }

    /**
     * Verify that the shortcut properties are modified so that shortcuts are
     * always active and spans aren't supported when the device doesn't support
     * mixed content and inserts a line break before the link.
     *
     * @throws PAPIException if there was a problem running the test.
     */
    public void testElementEndImplWhenDeviceDoesNotSupportsMixedContentAndInsertsLineBreaksBefore()
            throws PAPIException {

        // Set expectations.
        createElementEndImplExpectations();
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_SUPPORTS_MIXED_CONTENT).
                returns(DevicePolicyConstants.NO_SUPPORT_POLICY_VALUE);
        properties.expects.setSupportsSpan(false);
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_BREAKS_LINE).returns("before");
        properties.expects.setActive(true);

        // Run test.
        final BlockAttributes blockAttributes = createTestableBlockAttributes();
        menuElement.elementEndImpl(requestContext, blockAttributes);
    }

   /**
     * Verify that the shortcut properties are not modified when the device
     * supports mixed content and inserts a line break after the link.
     *
     * @throws PAPIException if there was a problem running the test.
     */
    public void testElementEndImplWhenDeviceSupportsMixedContentAndInsertLineBreakAfter()
            throws PAPIException {

        // Set expectations.
        createElementEndImplExpectations();
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_SUPPORTS_MIXED_CONTENT).
                returns(DevicePolicyConstants.FULL_SUPPORT_POLICY_VALUE);
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_BREAKS_LINE).returns("after");

        // Run test.
        final BlockAttributes blockAttributes = createTestableBlockAttributes();
        menuElement.elementEndImpl(requestContext, blockAttributes);
    }

    /**
     * Verify that the shortcut properties are modified so that spans are not
     * supported when the device doesn't support mixed content and inserts a
     * line break after the link.
     *
     * @throws PAPIException if there was a problem running the test.
     */
    public void testElementEndImplWhenDeviceDoesNotSupportsMixedContentAndBreakLineAfter()
            throws PAPIException {

        // Set expectations.
        createElementEndImplExpectations();
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_SUPPORTS_MIXED_CONTENT).
                returns(DevicePolicyConstants.NO_SUPPORT_POLICY_VALUE);
        properties.expects.setSupportsSpan(false);
        pageContext.expects.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_A_BREAKS_LINE).returns("after");

        // Run test.
        final BlockAttributes blockAttributes = createTestableBlockAttributes();
        menuElement.elementEndImpl(requestContext, blockAttributes);
    }

    /**
     * Set up the expectations that are common to every test which runs
     * {@link MenuElementImpl#elementEndImpl}
     */
    private void createElementEndImplExpectations() {

        // Create test objects.        
        MenuModelBuilderMock builder =
                new MenuModelBuilderMock("builder", expectations);
        MenuMock menu = new MenuMock("menu", expectations);
        MenuRendererSelectorLocatorMock rendererLocator =
                new MenuRendererSelectorLocatorMock(
                        "rendererLocator", expectations);
        MenuRendererSelectorMock rendererSelector =
                new MenuRendererSelectorMock("rendererLocator", expectations);
        MenuRendererMock renderer =
                new MenuRendererMock("renderer", expectations);        

        // Set expectations.
        requestContext.expects.getMarinerPageContext().returns(pageContext);
        pageContext.expects.popElement(menuElement);
        pageContext.expects.getMenuBuilder().returns(builder);
        builder.expects.endMenu().returns(menu);
        pageContext.expects.getRendererLocator().returns(rendererLocator);
        rendererLocator.expects.getMenuRendererSelector(pageContext).
                returns(rendererSelector);
        rendererSelector.expects.selectMenuRenderer(menu).returns(renderer);
        menu.expects.getShortcutProperties().returns(properties);       
        renderer.expects.render(menu);

    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.papi.impl.BlockElementTestAbstract#createTestableBlockAttributes()
     */
    protected BlockAttributes createTestableBlockAttributes() {
        return new com.volantis.mcs.papi.MenuAttributes();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 30-Jun-05	8888/4	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 02-Apr-04	3429/1	philws	VBM:2004031502 MenuLabelElement implementation

 18-Mar-04	3412/1	claire	VBM:2004031201 Early implementation of new menus in PAPI

 13-Aug-03	958/3	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 31-Jul-03	868/1	mat	VBM:2003070704 Initial work on this task

 04-Jul-03	337/1	chrisw	VBM:2003020609 implemented rework, added testcases

 ===========================================================================
*/
