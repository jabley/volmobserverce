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

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.protocols.css.DefaultStylePropertyResolver;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValue;

/**
 * Tests {@link DefaultMenuSeparatorRendererSelector}.
 */
public class DefaultMenuSeparatorRendererSelectorTestCase
        extends MenuSeparatorRendererSelectorTestAbstract {
    /**
     * A trivial separator renderer implementation to simplify creation of
     * the separator renderer classes that represent the various factory methods
     * that the selector can call.
     */
    protected static class TestSeparatorRenderer implements SeparatorRenderer {
        public void render(OutputBuffer buffer)
                throws RendererException {
        }
    }

    /**
     * Used when a Horizontal Menu Separator Renderer is required.
     */
    protected static class HMSR extends TestSeparatorRenderer {
    }

    /**
     * Used when a Vertical Menu Separator Renderer is required.
     */
    protected static class VMSR extends TestSeparatorRenderer {
    }

    /**
     * Used when a Horizontal Menu Item Separator Renderer is required.
     */
    protected static class HMISR extends TestSeparatorRenderer {
    }

    /**
     * Used when a Vertical Menu Item Separator Renderer is required.
     */
    protected static class VMISR extends TestSeparatorRenderer {
    }

    /**
     * Used when a Character Menu Item Group Separator Renderer is required.
     */
    protected static class CMIGSR extends TestSeparatorRenderer {
    }

    /**
     * Used when an Image Menu Item Group Separator Renderer is required.
     */
    protected static class IMIGSR extends TestSeparatorRenderer {
    }

    /**
     * A factory that utilizes the various {@link TestSeparatorRenderer
     * TestSeparatorRenderer} specializations to generate instances of specific
     * classes of separator renderer to enable the base test case to determine
     * that the test subject is operating correctly.
     *
     * <p>This factory is designed to match the various
     * <code>get...Class</code> methods in the test case.</p>
     */
    protected static class TestFactory
            implements MenuSeparatorRendererFactory {
        // javadoc inherited
        public SeparatorRenderer createHorizontalMenuSeparator(
                StyleValue separatorType) {
            return new HMSR();
        }

        // javadoc inherited
        public SeparatorRenderer createVerticalMenuSeparator() {
            return new VMSR();
        }

        // javadoc inherited
        public SeparatorRenderer createHorizontalMenuItemSeparator(
                StyleValue separatorType) {
            return new HMISR();
        }

        // javadoc inherited
        public SeparatorRenderer createVerticalMenuItemSeparator() {
            return new VMISR();
        }

        // javadoc inherited
        public SeparatorRenderer createCharacterMenuItemGroupSeparator(
                String chars,
                int repeat) {
            return new CMIGSR();
        }

        // javadoc inherited
        public SeparatorRenderer createImageMenuItemGroupSeparator(
                ImageAssetReference imageAssetReference) {
            return new IMIGSR();
        }
    }

    // javadoc inherited
    protected MenuSeparatorRendererSelector createSelector() {
        return new DefaultMenuSeparatorRendererSelector(
                createFactory(), createAssetResolver(),
                new DefaultStylePropertyResolver(null, null));
    }

    /**
     * Utility method to create an appropriate factory instance to use
     * in the test subject.
     *
     * @return a factory for use by the test subject
     */
    protected MenuSeparatorRendererFactory createFactory() {
        return new TestFactory();
    }

    /**
     * Utility method to create an appropriate asset resolver instance to use
     * in the test subject.
     *
     * @return an asset resolver for use by the test subject
     */
    protected AssetResolver createAssetResolver() {
        return new AssetResolverMock("assetResolverMock", expectations);
    }

    // javadoc inherited
    protected Class getMenuSeparatorHorizontalClass(StyleKeyword type) {
        return HMSR.class;
    }

    // javadoc inherited
    protected Class getMenuSeparatorVerticalClass() {
        return VMSR.class;
    }

    // javadoc inherited
    protected Class getMenuItemGroupSeparatorCharactersClass() {
        return CMIGSR.class;
    }

    // javadoc inherited
    protected Class getMenuItemGroupSeparatorImageClass() {
        return IMIGSR.class;
    }

    protected Class getMenuItemSeparatorHorizontalClass(StyleKeyword type) {
        return HMISR.class;
    }

    // javadoc inherited
    protected Class getMenuItemSeparatorVerticalClass() {
        return VMISR.class;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 22-Apr-04	3681/3	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
