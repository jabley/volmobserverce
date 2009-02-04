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

package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.TestMenuModuleCustomisation;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.DefaultMenuModule;
import com.volantis.mcs.protocols.menu.shared.builder.ConcreteMenuModelBuilder;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenu;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DefaultMenuRendererSelectorTestCase;
import com.volantis.mcs.protocols.menu.shared.renderer.TestDeprecatedOutputLocator;
import com.volantis.mcs.protocols.renderer.TestRendererContext;
import com.volantis.mcs.runtime.OutputBufferResolverMock;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.value.CompositeExpectedValue;

/**
 * This test class tests <code>OpenwaveMenuRendererSelector</code>.  It extends
 * the <code>DefaultMenuRendererSelectorTestCase</code> test case because the
 * class it is testing is part of a hierarchy.
 */
public class OpenwaveMenuRendererSelectorTestCase
        extends DefaultMenuRendererSelectorTestCase {

    private StyledDOMTester styledDOMTester;

    /**
     * Initialise a new instance of this test case.
     */
    public OpenwaveMenuRendererSelectorTestCase() {
        // This is required for Pipeline namespace support in IDEA. Sigh!
        // todo: fix this.
        new Volantis();

        MutableStylePropertySet interestingProperties =
                new MutableStylePropertySetImpl();
        interestingProperties.add(StylePropertyDetails.WHITE_SPACE);
        styledDOMTester = new StyledDOMTester(interestingProperties);
    }

    // JavaDoc inherited
    public void testSelectMenuRenderer() throws Exception {

        // Overriding this method to provide more specific tests of the
        // functionality provided by the OpenwaveMenuRendererSelector

        // Get the selector
        MenuRendererSelector selector = getTestMenuRendererSelector();

        // Not checking for null as this protocol explicitly supports menu
        // rendering - so a null here would be a test failure anyway.

        // Set styles to force Openwave
        ConcreteElementDetails elementDetails = new ConcreteElementDetails();

        elementDetails.setStyles(StylesBuilder.getCompleteStyles(
                "mcs-menu-link-style: numeric-shortcut"));

        // Create a test menu
        ConcreteMenu testMenu = new ConcreteMenu(elementDetails);

        // Test renderer returned
        MenuRenderer renderer = selector.selectMenuRenderer(testMenu);

        // Check the renderer exists.  JavaDoc on MenuRendererSelector
        // says it may not be null.
        assertNotNull("Should be a renderer here (1)", renderer);

        // Check appropriate renderer returned
        assertTrue("Wrong renderer returned (1)",
                   renderer instanceof OpenwaveMenuRenderer);

        // Set styles to force WML
        elementDetails = new ConcreteElementDetails();
        elementDetails.setStyles(StylesBuilder.getCompleteStyles(
                "mcs-menu-link-style: default"));

        // Create a test menu
        testMenu = new ConcreteMenu(elementDetails);

        // Test renderer returned
        renderer = selector.selectMenuRenderer(testMenu);

        // Check the renderer exists.  JavaDoc on MenuRendererSelector
        // says it may not be null.
        assertNotNull("Should be a renderer here (2)", renderer);

        // Check appropriate renderer returned
        assertTrue("Wrong renderer returned (2)",
                   renderer instanceof DefaultMenuRenderer);
    }

    // JavaDoc inherited
    protected MenuRendererSelector getTestMenuRendererSelector() {
        return new OpenwaveMenuRendererSelector(
                super.getTestMenuRendererSelector(), itemRendererSelector,
                bufferLocatorFactory);
    }

    public void testRenderer()
            throws Exception {

        FormatReference MENU_PANE
                = new FormatReference("menu",
                                      NDimensionalIndex.ZERO_DIMENSIONS);

        FormatReference ITEM1_PANE
                = new FormatReference("item1",
                                      NDimensionalIndex.ZERO_DIMENSIONS);

        FormatReference ITEM2_PANE
                = new FormatReference("item2",
                                      NDimensionalIndex.ZERO_DIMENSIONS);

        final AssetResolverMock assetResolverMock =
                new AssetResolverMock("assetResolverMock", expectations);

        final TestRendererContext rendererContext = new TestRendererContext(
                assetResolverMock);
        TestDeprecatedOutputLocator outputLocator
                = new TestDeprecatedOutputLocator();
        TestMenuModuleCustomisation customisation
                = new TestMenuModuleCustomisation();

        // Create a mock OutputBufferResolver that expects to be called lots of
        // times with the same value.
        ExpectationBuilder expectations = mockFactory.createOrderedBuilder();
        final OutputBufferResolverMock resolverMock =
                new OutputBufferResolverMock("resolverMock", expectations);
        CompositeExpectedValue expectedArgument =
            MockFactory.getDefaultInstance().expectsAnyDefined();
        expectedArgument.addExpectedValue(mockFactory.expectsNull());
        expectedArgument.addExpectedValue(mockFactory.expectsSame(MENU_PANE));
        resolverMock.fuzzy.resolvePaneOutputBuffer(expectedArgument)
                .returns(rendererContext.getBuffer()).any();
        rendererContext.setOutputBufferResolver(resolverMock);

        // todo Customise the menu model to use a MockMenuBufferLocatorFactory
        // todo that will create a MockMenuBufferLocatory that expects to be
        // todo called only for menus and returns a fixed buffer.

        // Create the module.
        MenuModuleRendererFactory rendererFactory
                = new OpenwaveMenuModuleRendererFactory(
                        rendererContext, outputLocator, customisation);

        MenuModule module = new OpenwaveMenuModule(
                rendererContext, rendererFactory,
                new DefaultMenuModule(rendererContext, rendererFactory));
        MenuRendererSelector selector = module.getMenuRendererSelector();

        // Create the menu.
        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        builder.startMenu();

        // Set up style properties to use OpenWave menu renderer.
        builder.setElementDetails("menu", null,
                StylesBuilder.getCompleteStyles(
                        "mcs-menu-link-style: numeric-shortcut"));

        // Target it at a specific pane.
        DOMOutputBuffer buffer;

        builder.setPane(MENU_PANE);

        // Create the first menu item explicitly targeted at a different pane.
        builder.startMenuItem();

        // Set the style.
        builder.setElementDetails("menuitem", null,
                StylesBuilder.getInitialValueStyles());

        // Set the href.
        builder.setHref(new LiteralLinkAssetReference("href1.xml"));

        // Set the pane.
        builder.setPane(ITEM1_PANE);

        // Create a plain text label.
        builder.startLabel();
        builder.startText();

        // Set the text.
        buffer = new TestDOMOutputBuffer();
        buffer.writeText("item 1");
        builder.setText(buffer);

        builder.endText();
        builder.endLabel();

        builder.endMenuItem();

        // Create the second menu item explicitly targeted at a different pane.
        builder.startMenuItem();

        // Set the style.
        builder.setElementDetails("menuitem", null,
                StylesBuilder.getInitialValueStyles());

        // Set the href.
        builder.setHref(new LiteralLinkAssetReference("href2.xml"));

        // Set the pane.
        builder.setPane(ITEM2_PANE);

        // Create a plain text label.
        builder.startLabel();
        builder.startText();

        // Set the text.
        buffer = new TestDOMOutputBuffer();
        buffer.writeText("item 2");
        builder.setText(buffer);

        builder.endText();
        builder.endLabel();

        builder.endMenuItem();

        builder.endMenu();

        Menu menu = builder.getCompletedMenuModel();

        // Select the renderer to use for this menu.
        MenuRenderer renderer = selector.selectMenuRenderer(menu);

        // Make sure that it is of the appropriate type.
        assertTrue("Incorrect renderer returned: " + renderer,
                   renderer instanceof OpenwaveMenuRenderer);

        // Render the menu.
        renderer.render(menu);

        // Check the markup output.
        String expected
                = "<BLOCK style='mcs-menu-link-style: numeric-shortcut; white-space: nowrap'>"
                + "<select>"
                + "<option onpick=\"href1.xml\">item 1</option>"
                + "<option onpick=\"href2.xml\">item 2</option>"
                + "</select>"
                + "</BLOCK>";

        DOMOutputBuffer outputBuffer = rendererContext.getBuffer();

        String actual = styledDOMTester.render(outputBuffer.getRoot());
        expected = styledDOMTester.normalize(expected);

        assertEquals("Output not correct", expected, actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Sep-05	9600/2	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/3	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 10-May-04	4233/4	claire	VBM:2004050710 Fixed MenuRendererSelection null pointer and tidied class naming

 10-May-04	4233/1	claire	VBM:2004050710 Fixed MenuRendererSelection null pointer and tidied class naming

 10-May-04	4227/1	philws	VBM:2004050706 Unique Menu Buffer Locator per Menu

 06-May-04	4174/3	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 30-Apr-04	4124/3	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 ===========================================================================
*/
