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
/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.MenuAttributes;
import com.volantis.mcs.papi.MenuItemAttributes;
import com.volantis.mcs.papi.MenuItemGroupAttributes;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.papi.menu.MenuRendererSelectorLocator;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.RolloverImagePolicy;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.TestPaneInstance;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitor;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitorException;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.model.AbstractMenuModelHandler;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayoutTestHelper;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverMock;
import com.volantis.mcs.runtime.policies.ReturnLiteralLinkReference;
import com.volantis.mcs.runtime.policies.ReturnLiteralTextReference;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import junit.framework.Assert;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Tests the functioning of the MenuBuilder within the PAPI environment rather
 * than individual functionality of the element start/end tags.  For more
 * information refer to these other tests:
 * {@see MenuElementImplTestCase}
 * {@see MenuItemElementImplTestCase}
 * {@see MenuItemGroupTestCase}
 * {@see MenuTestCase}
 */
public class EnhancedMenusMenuBuilderTestCase extends TestCaseAbstract {

    /**
     * This tests the construction of a complete menu through PAPI.  It is not
     * designed to test the indivdiual functioning of the elements themselves.
     * There should be individual test case classes for that.  In some ways
     * this is more of an integration test for the enhanced menu classes.
     *
     * <p>
     * Structure to test against:
     * <pre>
     * Top Menu
     * |
     * +----Item alpha
     * |
     * +----Item beta
     * |
     * +----Sub Menu 1
     * |    |
     * |    +----Item gamma.alpha
     * |    |
     * |    +----Item gamma.beta
     * |    |
     * |    +----Sub Menu 2
     * |         |
     * |         +----Item delta.alpha
     * |         |
     * |         +----Item delta.beta
     * |
     * +----Item epsilon    }
     * |                    } Grouped as a menu item group
     * +----Item zeta       }
     *
     * </pre>
     *
     * @throws Exception If any of the code in the method throws an unexpected
     *                   exception or any test fails.
     */
    public void testMenuBuilding() throws Exception {

        // Set up the test "environemnt" to simulate the request
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        MarinerRequestContext requestContext = new TestMarinerRequestContext();

        // Set up the test-specific renderer locator
        MenuRendererSelectorLocator.setDefaultInstance(new RendererLocator());

        // Set up the protocol (needed for OutputBuffer management)
        // The test implementation avoids the need for an initialized volantis
        // bean, which is jolly handy in this case
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestDOMProtocolFactory(),
                InternalDeviceTestHelper.createTestDevice());
        pageContext.setProtocol(protocol);

        // Set the Layout (needed for FormatReference and panes)
        CanvasLayout layout = new CanvasLayout();

        // Don't actual resolve anything as it requires repository connections,
        // branding information, etc.
        PolicyFactory factory = PolicyFactory.getDefaultInstance();
        RolloverImagePolicyBuilder rolloverBuilder =
                factory.createRolloverImagePolicyBuilder();
        rolloverBuilder.setName("Test rollover response");
        rolloverBuilder.setNormalPolicy(factory.createPolicyReference("Normal",
                PolicyType.IMAGE));
        rolloverBuilder.setOverPolicy(factory.createPolicyReference("Over",
                PolicyType.IMAGE));
        //pageContext.setRollover(rollover);

        // Intitialise the context
        pageContext.pushRequestContext(requestContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        ContextInternals.setEnvironmentContext(requestContext,
                new TestEnvironmentContext());
        Pane testPane = new Pane(layout);
        testPane.setName("pane");
        pageContext.addPaneMapping(testPane);

        // Activate the device layout.
        RuntimeDeviceLayout runtimeDeviceLayout =
                RuntimeDeviceLayoutTestHelper.activate(layout);

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
        pageContext.setPolicyReferenceResolver(referenceResolverMock);

        referenceResolverMock.expects.resolveQuotedTextExpression(null)
                .returns(null).any();

        referenceResolverMock.fuzzy
                .resolveQuotedTextExpression(
                        mockFactory.expectsAny())
                .does(new ReturnLiteralTextReference()).any();

        referenceResolverMock.fuzzy
                .resolveQuotedLinkExpression(
                        mockFactory.expectsAny(),
                        mockFactory.expectsAny())
                .does(new ReturnLiteralLinkReference()).any();

        // Set up the elements
        MenuElementImpl top = new MenuElementImpl();
        MenuItemElementImpl alpha = new MenuItemElementImpl();
        MenuItemElementImpl beta = new MenuItemElementImpl();
        MenuElementImpl subMenuOne = new MenuElementImpl();
        MenuItemElementImpl gammaAlpha = new MenuItemElementImpl();
        MenuItemElementImpl gammaBeta = new MenuItemElementImpl();
        MenuElementImpl subMenuTwo = new MenuElementImpl();
        MenuItemElementImpl deltaAlpha = new MenuItemElementImpl();
        MenuItemElementImpl deltaBeta = new MenuItemElementImpl();
        MenuItemGroupElementImpl group = new MenuItemGroupElementImpl();
        MenuItemElementImpl epsilon = new MenuItemElementImpl();
        MenuItemElementImpl zeta = new MenuItemElementImpl();

        // Set up some attributes to pass in to the builder
        MenuAttributes menuAttributes = new MenuAttributes();
        MenuItemGroupAttributes menuItemGroupAttributes =
                                            new MenuItemGroupAttributes();


        // Test the elements - these must be called in the order expected in
        // the input files (i.e. valid markup) otherwise the builder will
        // throw exceptions and that is not the point of this test!
        // Assertions used to check return values and possible exceptions

        try {
            int result;

            menuAttributes = createBasicAttributes(menuAttributes);
            result = top.elementStart(requestContext, menuAttributes);
            checkTrue("MenuElement", result,
                    PAPIElement.PROCESS_ELEMENT_BODY);

            buildAndTestItem(alpha, "alpha", requestContext);
            buildAndTestItem(beta, "beta", requestContext);

            menuAttributes = createBasicAttributes(menuAttributes);
            result = subMenuOne.elementStart(requestContext,
                    menuAttributes);
            checkTrue("MenuElement", result,
                    PAPIElement.PROCESS_ELEMENT_BODY);

            buildAndTestItem(gammaAlpha, "gamma alpha", requestContext);
            buildAndTestItem(gammaBeta, "gamma beta", requestContext);

            menuAttributes = createBasicAttributes(menuAttributes);
            result = subMenuTwo.elementStart(requestContext,
                    menuAttributes);
            checkTrue("MenuElement", result,
                    PAPIElement.PROCESS_ELEMENT_BODY);

            buildAndTestItem(deltaAlpha, "delta alpha", requestContext);
            buildAndTestItem(deltaBeta, "delta beta", requestContext);

            result = subMenuTwo.elementEnd(requestContext,
                    menuAttributes);
            checkTrue("MenuElement", result,
                    PAPIElement.CONTINUE_PROCESSING);

            result = subMenuOne.elementEnd(requestContext,
                    menuAttributes);
            checkTrue("MenuElement", result,
                    PAPIElement.CONTINUE_PROCESSING);

            menuItemGroupAttributes =
                    createBasicAttributes(menuItemGroupAttributes);
            result = group.elementStart(requestContext,
                    menuItemGroupAttributes);
            checkTrue("MenuItemGroupElement", result,
                    PAPIElement.PROCESS_ELEMENT_BODY);

            buildAndTestItem(epsilon, "epsilon", requestContext);
            buildAndTestItem(zeta, "seta", requestContext);

            result = group.elementEnd(requestContext,
                    menuItemGroupAttributes);
            checkTrue("MenuItemGroupElement", result,
                    PAPIElement.CONTINUE_PROCESSING);

            // This will return a completed menu and handle it
            result = top.elementEnd(requestContext, menuAttributes);
            checkTrue("MenuElement", result, PAPIElement.CONTINUE_PROCESSING);

        } catch (PAPIException pe) {
            StringWriter stackTrace = new StringWriter();
            PrintWriter writer = new PrintWriter(stackTrace, true);
            pe.printStackTrace(writer);
            writer.flush();
            writer.close();
            fail(stackTrace.toString());
        }

        // Through testing the start and end implementations of menu the
        // complete menu building process should have been tested and
        // processed within the PAPI elements.  This can also be verified using
        // the markup and renderer classes below to ensure they were called
        // with a valid menu

        final int totalRender = 1;
        final int totalMenu = 3;
        final int totalGroup = 1;
        final int totalItem = 8;

        assertEquals("Renderer calls should have been: " + totalRender,
                Renderer.getRenderCount(), totalRender);

        assertEquals("Menu calls should have been: " + totalMenu,
                SimpleAbstractMenuModelVisitor.getMenuCount(), totalMenu);
        assertEquals("Menu group calls should have been: " + totalGroup,
                SimpleAbstractMenuModelVisitor.getGroupCount(), totalGroup);
        assertEquals("Menu item calls should have been: " + totalItem,
                SimpleAbstractMenuModelVisitor.getItemCount(), totalItem);

        Renderer.reset();

    }

    /**
     * Add the basic attributes for a menu element.
     *
     * @param attr The menu attributes to add the attributes to
     * @return     The updated menu attributes
     */
    private MenuAttributes createBasicAttributes(MenuAttributes attr) {
        attr.setType("plaintext");
        attr.setStyleClass("styleClass");
        attr.setId("id");
        attr.setPrompt("<prompt>content</prompt>");
        attr.setErrmsg("<nomatch>an error occured</nomatch>");
        attr.setHelp("<help>select an option from the list</help>");
        attr.setPane("pane");
        attr.setTitle("Menu");

        return attr;
    }

    /**
     * Add the basic attributes for a menu item element.
     *
     * @param attr The menu item attributes to add the attributes to
     * @param text The title of the item
     * @return     The updated menu item attributes
     */
    private MenuItemAttributes createBasicAttributes(MenuItemAttributes attr,
                                                     String text) {
        attr.setShortcut("shortcut");
        attr.setHref("http://www.volantis.com");
        attr.setPrompt("<prompt>enter a value</prompt>");
        attr.setOffColor("red");
        attr.setOffImage("stars");
        attr.setOnColor("blue");
        attr.setOnImage("volantis");
        attr.setRolloverImage("rollover");
        attr.setSegment("segment");
        attr.setTarget("target");
        attr.setText(text);
// @todo: handle the new image attribute once that becomes available

        return attr;
    }

    /**
     * Add the basic attributes for a menu item group element.
     *
     * @param attr The menu item group attributes to add the attributes to
     * @return     The updated menu item group attributes
     */
    private MenuItemGroupAttributes
                    createBasicAttributes(MenuItemGroupAttributes attr) {
        attr.setId("testid");
        attr.setStyleClass("myClass");

        return attr;
    }

    /**
     * A convenience method which wraps an assert of two return values (or
     * any two integers) being equal with a suitable message output if not.
     *
     * @param item       The name of the item being tested output on error
     * @param one        The item to test for equality
     * @param two        The item to compare against
     * @throws Exception If the test assertion fails
     */
    private void checkTrue(String item, int one, int two) throws Exception {
        String message = "Unexpected value returned from " + item + ".  Was " +
                         one + " but should have been " + two + ".\n";
        assertTrue(message, one == two);
    }

    /**
     * A utility method to construct a menu item element and process the start
     * and end tags and check that the return values from these calls are
     * as expected.
     * <p>
     * This does not test detailed functionality of a menu item element,  This
     * is used when building a complete menu structure for test.
     *
     * @param element        The element to construtc
     * @param name           The name of the element and displayed on the item
     * @param requestContext The context in which the processing should occur
     * @throws Exception     If any test assertions fail
     */
    private void buildAndTestItem(MenuItemElementImpl element, String name,
                                  MarinerRequestContext requestContext)
                                                            throws Exception {
        int result;
        MenuItemAttributes menuItemAttributes = new MenuItemAttributes();

        // Create attributes
        menuItemAttributes = createBasicAttributes(menuItemAttributes, name);

        // Start element
        result = element.exprElementStart(requestContext, menuItemAttributes);
        checkTrue("MenuItemElement", result, PAPIElement.PROCESS_ELEMENT_BODY);

        // End element
        result = element.exprElementEnd(requestContext, menuItemAttributes);
        checkTrue("MenuItemElement", result, PAPIElement.CONTINUE_PROCESSING);
    }

    /**
     * A test implementation of a locator for a renderer
     */
    static class RendererLocator extends MenuRendererSelectorLocator {
        // JavaDoc inherited
        public MenuRendererSelector
            getMenuRendererSelector(MarinerPageContext context) {
            return new RendererSelector();
        }
    }

    /**
     * A test implementation of a selector for a renderer
     */
    static class RendererSelector implements MenuRendererSelector {
        // JavaDoc inherited
        public MenuRenderer selectMenuRenderer(Menu menu)
            throws RendererException {
            return new Renderer();
        }
    }

    /**
     * A test implementation of a renderer that counts the number of times
     * the methods have been called and provides suitable access to those
     * counts.
     */
    static class Renderer implements MenuRenderer {
        /**
         * Count of the number of times that the render method has been called
         */
        private static int renderCount = 0;

        /**
         * Creates an instance of this class
         */
        public Renderer() {
            reset();
        }

        // JavaDoc inherited
        static public int getRenderCount() {
            return renderCount;
        }

        /**
         * Reset all the counts in this class.
         */
        static public void reset() {
            renderCount = 0;
        }

        // JavaDoc inherited
        public void render(Menu menu)
                throws RendererException {

            renderCount++;
            // Test menu access
            MenuModelVisitor visitor = new SimpleAbstractMenuModelVisitor();

            try {
                visitor.visit(menu);
            } catch (MenuModelVisitorException e) {
                throw new RendererException(e);
            }
        }
    }

    /**
     * A simple instantiation of the abstract AbstractMenuModelVisitor class.
     * This class implements the necessary methods and ensures basic
     * functionality works in the handle methods.
     */
    static class SimpleAbstractMenuModelVisitor extends AbstractMenuModelHandler {

        private static int itemCount = 0;

        private static int groupCount = 0;

        private static int menuCount = 0;

        /**
         * Create a new instance of this visitor implementation.
         */
        public SimpleAbstractMenuModelVisitor() {
            reset();
        }

        public static void reset() {
            itemCount = 0;
            groupCount = 0;
            menuCount = 0;
        }

        // JavaDoc inherited
        public boolean handle(MenuItem item) {
            Assert.assertNotNull("Cannot visit null menu items", item);
            itemCount++;
            return true;
        }

        // JavaDoc inherited
        public boolean handle(MenuItemGroup group) {
            Assert.assertNotNull("Cannot visit null menu item groups", group);
            groupCount++;
            return true;
        }

        // JavaDoc inherited
        public boolean handle(Menu menu) {
            Assert.assertNotNull("Cannot visit null menus", menu);
            menuCount++;
            return true;
        }

        /**
         * Obtains the item count from this class
         * @return the number of calls made to handle an item
         */
        public static int getItemCount() {
            return itemCount;
        }

        /**
         * Obtains the item group count from this class
         * @return the number of calls made to handle an item group
         */
        public static int getGroupCount() {
            return groupCount;
        }

        /**
         * Obtains the menu count from this class
         * @return the number of class made to handle a menu
         */
        public static int getMenuCount() {
            return menuCount;
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 28-Jun-05	8878/3	emma	VBM:2005062306 rework

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 24-May-05	8123/11	ianw	VBM:2005050906 Fix accurev merge issues

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 04-May-04	4164/1	pduffin	VBM:2004050404 Refactored DefaultMenuRenderer internal visitor class to allow pre and post processing.

 07-Apr-04	3735/1	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 06-Apr-04	3429/9	philws	VBM:2004031502 MenuLabelElement implementation

 06-Apr-04	3641/4	claire	VBM:2004032602 Corrected icon and label validation

 30-Mar-04	3641/1	claire	VBM:2004032602 Using menu types and styles in PAPI

 05-Apr-04	3733/1	philws	VBM:2004040504 Added MenuModelVisitorException

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 19-Mar-04	3478/1	claire	VBM:2004031805 Removed MenuMarkupGenerator and associated code

 19-Mar-04	3412/3	claire	VBM:2004031201 Improving PAPI and new menus

 18-Mar-04	3412/1	claire	VBM:2004031201 Early implementation of new menus in PAPI

 ===========================================================================
*/
