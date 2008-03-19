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

package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.renderer.TestRendererContext;
import com.volantis.mcs.protocols.menu.TestMenuModuleCustomisation;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This tests the default menu item renderer factory.  It tests each method to
 * ensure that appropriate class instances are being returned.  This will not
 * necessarily exhaustively test all the menu item renderers - it only tests
 * the functioning of the factory.
 */
public class DefaultMenuItemRendererFactoryTestCase extends TestCaseAbstract {

    protected TestMenuModuleCustomisation customisation;

    /**
     * The test instance of the factory that is used for the various
     * tests across this complete test case.
     */
    protected DefaultMenuItemRendererFactory factory;

    protected SeparatorRenderer separator;

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        // Initialise the factory
        customisation = new TestMenuModuleCustomisation();
        factory = new DefaultMenuItemRendererFactory(
                new TestRendererContext(null),
                new TestDeprecatedOutputLocator(),
                customisation);
        separator = new TestHorizontalSeparator();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        factory = null;
        separator = null;
        super.tearDown();
    }

    /**
     * This tests createHorizontalSeparator and createVerticalSeparator
     */
    public void testSeparators() {
        // @todo: implementation once separator implementations exist
    }

    /**
     * This tests createInnerLinkRenderer
     */
    public void testInnerLinkRenderers() throws Exception {

        // test the inner link renderer
        MenuItemBracketingRenderer factoryRenderer =
                factory.createInnerLinkRenderer(null);
        assertNotNull("Should be am inner link renderer", factoryRenderer);

        if (!(factoryRenderer instanceof DefaultAnchorMenuItemRenderer)) {
            fail("The factory did not return the expected inner link renderer");
        }
    }

    /**
     * This tests the two createOuterLinkRenderer methods
     */
    public void testOuterLinkRenderers() throws Exception{

        // test the outer link renderer
        MenuItemBracketingRenderer factoryRenderer =
                factory.createOuterLinkRenderer(null);
        assertNotNull("Should be am outer link renderer", factoryRenderer);

        if (!(factoryRenderer instanceof DefaultAnchorMenuItemRenderer)) {
            fail("Factory did not return the expected outer link renderer (1)");
        }

        // test the outer link renderer
        factoryRenderer = factory.createOuterLinkRenderer(null);
        assertNotNull("Should be am outer link renderer", factoryRenderer);

        if (!(factoryRenderer instanceof DefaultAnchorMenuItemRenderer)) {
            fail("Factory did not return the expected outer link renderer (1)");
        }
    }

    /**
     * This tests createOuterRenderer
     */
    public void testOuterRenderer() throws Exception {

        // Testing the outer renderer
        MenuItemBracketingRenderer factoryRenderer =
                factory.createOuterRenderer();
        assertNotNull("Should be a plain image renderer", factoryRenderer);

        if (!(factoryRenderer instanceof DefaultSpanMenuItemRenderer)) {
            fail("The factory did not return the expected outer renderer");
        }
    }

    /**
     * This test createPlainImageRenderer and createRolloverImageRenderer
     */
    public void testImageRendering() {
        // Testing the plain image
        MenuItemComponentRenderer factoryRenderer
                = factory.createPlainImageRenderer(true);
        assertNotNull("Should be a plain image renderer", factoryRenderer);

        if (!(factoryRenderer instanceof DefaultImageMenuItemRenderer)) {
            fail("The factory did not return the expected image renderer (1)");
        }

        // Testing the rollover image
        factoryRenderer = factory.createRolloverImageRenderer(true);
        assertNotNull("Should be a rollover image renderer", factoryRenderer);

        // Default does not do rollover images so expect a normal image renderer
        if (!(factoryRenderer instanceof DefaultImageMenuItemRenderer)) {
            fail("The factory did not return the expected image renderer (2)");
        }
    }

    /**
     * This tests createPlainTextRenderer
     */
    public void testTextRendering() {
        MenuItemComponentRenderer factoryRenderer
                = factory.createPlainTextRenderer();
        assertNotNull("Should be a text renderer", factoryRenderer);

        if (!(factoryRenderer instanceof DefaultStyledPlainTextMenuItemRenderer)) {
            fail("The factory did not return the expected text renderer");
        }
    }

    /**
     * This tests createNumericEmulationRenderer.
     */
    public void testNumericShortcutEmulationRendering() {
        
        NumericShortcutEmulationRenderer numericShortcutEmulator
                = factory.createNumericShortcutEmulationRenderer();
        assertNull("Should be null", numericShortcutEmulator);
        
        customisation.setSupportsAccessKeyAttribute(true);
        numericShortcutEmulator
                = factory.createNumericShortcutEmulationRenderer();
        assertNotNull("Should be not null", numericShortcutEmulator);
        if (!(numericShortcutEmulator instanceof DefaultNumericShortcutEmulationRenderer)) {
            fail("The factory did not return the expected text renderer");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/2	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 06-May-04	4153/6	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 30-Apr-04	4124/1	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 29-Apr-04	4013/2	pduffin	VBM:2004042210 Restructure menu item renderers

 27-Apr-04	4025/4	claire	VBM:2004042302 Refining numeric emulation interface

 27-Apr-04	4025/2	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 26-Apr-04	3920/2	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 20-Apr-04	3715/3	claire	VBM:2004040201 Improving WML menu item renderers

 16-Apr-04	3715/1	claire	VBM:2004040201 Enhanced Menu: WML Menu Item Renderers

 ===========================================================================
*/
