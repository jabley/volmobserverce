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

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBufferFactory;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBufferFactory;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.css.DefaultStylePropertyResolver;
import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.TestDOMMenuModule;
import com.volantis.mcs.protocols.menu.shared.model.MenuModelHelper;
import com.volantis.mcs.runtime.OutputBufferResolver;
import com.volantis.mcs.runtime.TestOutputBufferResolver;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This test class tests <code>DefaultMenuRendererSelector</code>.
 */
public class DefaultMenuRendererSelectorTestCase
        extends TestCaseAbstract {

    protected MenuItemRendererSelector itemRendererSelector;
    protected MenuBracketingRenderer bracketingRenderer;
    protected MenuBufferLocatorFactory bufferLocatorFactory;
    protected MenuSeparatorRendererSelector separatorRendererSelector;

    protected void setUp() throws Exception {
        super.setUp();

        OutputBufferFactory outputBufferFactory
                = new TestDOMOutputBufferFactory();
        MenuItemRendererFactory itemRendererFactory
                = new TestMenuItemRendererFactory(outputBufferFactory);
        AssetResolver assetResolver = new AssetResolverMock("assetResolverMock",
                expectations);
        MenuSeparatorRendererFactory separatorRendererFactory
                = new TestMenuSeparatorRendererFactory();

        separatorRendererSelector
                = new DefaultMenuSeparatorRendererSelector(
                        separatorRendererFactory, assetResolver,
                        new DefaultStylePropertyResolver(null, null));
        itemRendererSelector
                = new DefaultMenuItemRendererSelector(
                        itemRendererFactory, separatorRendererSelector);
        DeprecatedDivOutput divOutput = new TestDeprecatedDivOutput();
        bracketingRenderer
                = new DefaultMenuBracketingRenderer(divOutput);
        DOMOutputBuffer outputBuffer = new TestDOMOutputBuffer();
        OutputBufferResolver bufferResolver
                = new TestOutputBufferResolver(outputBuffer);
        bufferLocatorFactory
                = new DefaultMenuBufferLocatorFactory(bufferResolver);
    }

    protected TestDOMMenuModule createMenuModule() {
        return new TestDOMMenuModule();
    }

    public void testConstruction() throws Exception {
        MenuRendererSelector selector = null;
        try {
            selector = new DefaultMenuRendererSelector(null, null, null, null);
            fail("Construction should have caused an exception (1)");
        } catch (IllegalArgumentException iae) {
            // Test succeeded if we get here as the exception was thrown :-)
        }
        try {
            selector = new DefaultMenuRendererSelector(itemRendererSelector,
                    null, null, null);
            fail("Construction should have caused an exception (2)");
        } catch (IllegalArgumentException iae) {
            // Test succeeded if we get here as the exception was thrown :-)
        }
        try {
            selector = new DefaultMenuRendererSelector(
                    itemRendererSelector, separatorRendererSelector,
                    null, null);
            fail("Construction should have caused an exception (3)");
        } catch (IllegalArgumentException iae) {
            // Test succeeded if we get here as the exception was thrown :-)
        }
        try {
            selector = new DefaultMenuRendererSelector(
                    itemRendererSelector, separatorRendererSelector,
                    bracketingRenderer, null);
            fail("Construction should have caused an exception (4)");
        } catch (IllegalArgumentException iae) {
            // Test succeeded if we get here as the exception was thrown :-)
        }

        // Now test valid construction
        selector = new DefaultMenuRendererSelector(
                itemRendererSelector, separatorRendererSelector,
                bracketingRenderer, bufferLocatorFactory);
        assertNotNull("Construction should have succeeded", selector);
    }

    public void testSelectMenuRenderer() throws Exception {

        final MenuRendererSelector selector = getTestMenuRendererSelector();
        MenuRenderer renderer =
                selector.selectMenuRenderer(
                        MenuModelHelper.createMenu(true, "Test Menu"));

        assertNotNull("Selected renderer must not be null", renderer);

        // Check the type is as it should be
        assertEquals("The class types should match",
                     renderer.getClass(), getSelectMenuRendererClass());
    }

    /**
     * Provides an menu renderer selector instance for use in the test case(s).
     *
     * @return A suitably initialised menu renderer selector.
     */
    protected MenuRendererSelector getTestMenuRendererSelector() {
        return new DefaultMenuRendererSelector(
                itemRendererSelector, separatorRendererSelector,
                bracketingRenderer, bufferLocatorFactory);
    }

    // JavaDoc inherited
    protected Class getSelectMenuRendererClass() {
        return DefaultMenuRenderer.class;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/3	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 10-May-04	4233/6	claire	VBM:2004050710 Fixed MenuRendererSelection null pointer and tidied class naming

 10-May-04	4233/4	claire	VBM:2004050710 Fixed MenuRendererSelection null pointer and tidied class naming

 10-May-04	4227/1	philws	VBM:2004050706 Unique Menu Buffer Locator per Menu

 06-May-04	4174/3	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 30-Apr-04	4124/3	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 ===========================================================================
*/
