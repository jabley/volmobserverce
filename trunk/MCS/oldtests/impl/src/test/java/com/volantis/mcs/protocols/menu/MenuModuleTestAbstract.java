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

package com.volantis.mcs.protocols.menu;

import com.volantis.mcs.protocols.menu.renderer.MenuRenderer;
import com.volantis.mcs.protocols.menu.renderer.MenuRendererSelector;
import com.volantis.mcs.protocols.menu.shared.model.MenuModelHelper;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.TestDeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.DefaultMenuModuleRendererFactory;
import com.volantis.mcs.protocols.renderer.TestRendererContext;
import com.volantis.mcs.protocols.renderer.RendererContext;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base class for menu module tests.
 */
public abstract class MenuModuleTestAbstract
        extends TestCaseAbstract {

    /**
     * The module to test.
     */
    private MenuModule module;
    private AssetResolverMock assetResolverMock;

    protected void setUp() throws Exception {
        super.setUp();

        assetResolverMock = new AssetResolverMock("assetResolverMock",
                expectations);

        RendererContext rendererContext =
                new TestRendererContext(assetResolverMock);
        DeprecatedOutputLocator outputLocator
                = new TestDeprecatedOutputLocator();
        TestMenuModuleCustomisation customisation
                = new TestMenuModuleCustomisation();

        MenuModuleRendererFactory rendererFactory = 
                new DefaultMenuModuleRendererFactory(rendererContext, 
                        outputLocator, customisation);        
        module = createMenuModule(rendererContext, rendererFactory);
    }

    /**
     * Create the menu module to test.
     *
     * @return The menu module to test.
     */
    protected abstract MenuModule createMenuModule(
            RendererContext rendererContext,
            MenuModuleRendererFactory rendererFactory);

    /**
     * This tests the creation of a menu renderer selector
     */
    public void testGetMenuRendererSelector() throws Exception {

        // Get the selector from the protocol
        MenuRendererSelector selector = module.getMenuRendererSelector();

/*
todo add an additional test in for this.
        // Check for null since some protocols don't support this and null
        // is a legal return value
        if (selector != null) {

            // Explicit package.class naming because of type conflict
            MenuRenderer renderer = selector.selectMenuRenderer(
                            MenuModelHelper.createMenu(true, "Test Menu"));

            // Check the renderer exists.  JavaDoc on MenuRendererSelector
            // says it may not be null.
            assertNotNull("Should be a renderer here", renderer);
        }
*/
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
