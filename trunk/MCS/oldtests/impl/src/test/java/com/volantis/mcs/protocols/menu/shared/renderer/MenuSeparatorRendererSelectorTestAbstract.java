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

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.LiteralImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.shared.builder.ConcreteMenuModelBuilder;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.MCSMenuHorizontalSeparatorKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base test for {@link MenuSeparatorRendererSelector} implementations.
 */
public abstract class MenuSeparatorRendererSelectorTestAbstract
        extends TestCaseAbstract {
    /**
     * The horizontal separator type enumeration values in an iterable form.
     */
    protected final static StyleKeyword[] horizontalSeparatorTypes =
            {MCSMenuHorizontalSeparatorKeywords.NONE,
             MCSMenuHorizontalSeparatorKeywords.SPACE,
             MCSMenuHorizontalSeparatorKeywords.NON_BREAKING_SPACE};

    /**
     * Checks that the correct class of separator renderer is returned for each
     * type of horizontal menu separator.
     */
    public void testSelectMenuSeparatorHorizontal() throws Exception {
        for (int index = 0; index < horizontalSeparatorTypes.length; index++) {
            MenuSeparatorRendererSelector selector = createSelector();
            Menu menu;

            StyleKeyword type = horizontalSeparatorTypes[index];

            menu = createMenu(StylesBuilder.getCompleteStyles(
                    "mcs-menu-orientation: horizontal; " +
                    "mcs-menu-horizontal-separator: " + type.getStandardCSS()),
                    null);

            checkSeparator("selectMenuSeparator(horizontal type=" + type + ")",
                           getMenuSeparatorHorizontalClass(type),
                           selector.selectMenuSeparator(menu));
        }
    }

    /**
     * Checks that the correct class of separator renderer is returned for the
     * vertical menu separator.
     */
    public void testSelectMenuSeparatorVertical() throws Exception {
        MenuSeparatorRendererSelector selector = createSelector();
        Menu menu;

        menu = createMenu(StylesBuilder.getCompleteStyles(
                "mcs-menu-orientation: vertical"), null);

        checkSeparator("selectMenuSeparator(vertical)",
                       getMenuSeparatorVerticalClass(),
                       selector.selectMenuSeparator(menu));
    }

    /**
     * Checks that the correct class of separator renderer is returned for the
     * "none" type of menu item group separator.
     */
    public void testSelectMenuItemGroupSeparatorNone() throws Exception {
        MenuSeparatorRendererSelector selector = createSelector();
        Menu menu;

        menu = createMenu(null, StylesBuilder.getCompleteStyles(
                "mcs-menu-separator-type: none"));

        checkSeparator("selectMenuItemGroupSeparator(none)",
                       null,
                       selector.selectMenuItemGroupSeparator(
                               (MenuItemGroup)menu.get(0),
                               true));
    }

    /**
     * Checks that the correct class of separator renderer is returned for the
     * "characters" type of menu item group separator.
     */
    public void testSelectMenuItemGroupSeparatorCharacters() throws Exception {
        MenuSeparatorRendererSelector selector = createSelector();
        Menu menu;

        menu = createMenu(null, StylesBuilder.getCompleteStyles(
                "mcs-menu-separator-characters: \"-\"; " +
                "mcs-menu-separator-position: before; " +
                "mcs-menu-separator-repeat: 2; " +
                "mcs-menu-separator-type: characters"));

        checkSeparator("selectMenuItemGroupSeparator(characters before match)",
                       getMenuItemGroupSeparatorCharactersClass(),
                       selector.selectMenuItemGroupSeparator(
                               (MenuItemGroup)menu.get(0),
                               true));

        checkSeparator("selectMenuItemGroupSeparator(characters before mismatch)",
                       null,
                       selector.selectMenuItemGroupSeparator(
                               (MenuItemGroup)menu.get(0),
                               false));
    }

    /**
     * Checks that the correct class of separator renderer is returned for the
     * "image" type of menu item group separator.
     */
    public void testSelectMenuItemGroupSeparatorImage() throws Exception {
        MenuSeparatorRendererSelector selector = createSelector();
        Menu menu;

        menu = createMenu(null, StylesBuilder.getCompleteStyles(
                "mcs-menu-separator-image: url(uri); " +
                "mcs-menu-separator-position: after; " +
                "mcs-menu-separator-type: image"));

        checkSeparator("selectMenuItemGroupSeparator(image after match)",
                       getMenuItemGroupSeparatorImageClass(),
                       selector.selectMenuItemGroupSeparator(
                               (MenuItemGroup)menu.get(0),
                               false));

        checkSeparator("selectMenuItemGroupSeparator(image after mismatch)",
                       null,
                       selector.selectMenuItemGroupSeparator(
                               (MenuItemGroup)menu.get(0),
                               true));
    }

    /**
     * Checks that a menu item group explicitly targeted at a pane will not do
     * any separator rendering.
     */
    public void testSelectMenuItemGroupSeparatorExplicitTargeting()
            throws Exception {
        MenuSeparatorRendererSelector selector = createSelector();
        FormatReference pane = new FormatReference("pane",
                                                   NDimensionalIndex.
                                                   ZERO_DIMENSIONS);
        Menu menu;

        menu = createMenu(null, StylesBuilder.getCompleteStyles(
                "mcs-menu-separator-type: image; " +
                "mcs-menu-separator-image: url(uri); " +
                "mcs-menu-separator-position: after"), pane);

        checkSeparator("selectMenuItemGroupSeparator(image after match)",
                       null,
                       selector.selectMenuItemGroupSeparator(
                               (MenuItemGroup)menu.get(0),
                               false));
    }

    /**
     * Checks that the correct class of separator renderer is returned for each
     * type of horizontal menu item separator.
     */
    public void testSelectMenuItemSeparatorHorizontal() throws Exception {
        for (int index = 0; index < horizontalSeparatorTypes.length; index++) {
            MenuSeparatorRendererSelector selector = createSelector();
            Menu menu;

            StyleKeyword type = horizontalSeparatorTypes[index];

            menu = createMenu(StylesBuilder.getCompleteStyles(
                    "mcs-menu-item-orientation: horizontal; " +
                    "mcs-menu-horizontal-separator: " + type.getStandardCSS()),
                    null);

            checkSeparator("selectMenuItemSeparator(horizontal type=" + type + ")",
                           getMenuItemSeparatorHorizontalClass(type),
                           selector.selectMenuItemSeparator(menu));
        }
    }

    /**
     * Checks that the correct class of separator renderer is returned for the
     * vertical menu item separator.
     */
    public void testSelectMenuItemSeparatorVertical() throws Exception {
        MenuSeparatorRendererSelector selector = createSelector();
        Menu menu;

        menu = createMenu(StylesBuilder.getCompleteStyles(
                "mcs-menu-item-orientation: vertical"), null);

        checkSeparator("selectMenuItemSeparator(vertical)",
                       getMenuItemSeparatorVerticalClass(),
                       selector.selectMenuItemSeparator(menu));
    }

    /**
     * Creates a simple menu containing a menu group containing a single menu
     * item. The group can be optionally targeted at a specific pane.
     *
     * @param menuStyles
     *                the style property values for the menu, may be null
     * @param groupStyles
     *                the style property values for the menu group, may be null
     * @param groupPane the format reference for the pane to which the the
     *                group should be targeted, may be null
     * @return a populated menu with a group and an item within the group
     */
    protected Menu createMenu(Styles menuStyles,
                              Styles groupStyles,
                              FormatReference groupPane) throws Exception {

        if (menuStyles == null) {
            menuStyles = StylesBuilder.getInitialValueStyles();
        }

        if (groupStyles == null) {
            groupStyles = StylesBuilder.getInitialValueStyles();
        }

        MenuModelBuilder builder = new ConcreteMenuModelBuilder();
        builder.startMenu();
        builder.setElementDetails("menu", "menu1", menuStyles);
        builder.startMenuGroup();
        builder.setElementDetails("menugroup", "menugroup1", groupStyles);

        if (groupPane != null) {
            builder.setPane(groupPane);
        }

        builder.startMenuItem();
        builder.setElementDetails("menuitem", "menuitem1",
                StylesBuilder.getInitialValueStyles());
        builder.startLabel();
        builder.startIcon();
        builder.setNormalImageURL(new LiteralImageAssetReference("normal.gif"));
        builder.setOverImageURL(new LiteralImageAssetReference("over.gif"));
        builder.endIcon();
        builder.startText();

        OutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.writeText("text");

        builder.setText(buffer);
        builder.endText();
        builder.endLabel();
        builder.setHref(new LiteralLinkAssetReference("href.xml"));
        builder.endMenuItem();
        builder.endMenuGroup();
        builder.endMenu();

        return builder.getCompletedMenuModel();
    }

    /**
     * Creates a simple menu containing a menu group containing a single menu
     * item.
     *
     * @param menuStyles
     *         the style property values for the menu, may be null
     * @param groupStyles
     *         the style property values for the menu group, may be null
     * @return a populated menu with a group and an item within the group
     */
    protected Menu createMenu(Styles menuStyles, Styles groupStyles)
            throws Exception {
        return createMenu(menuStyles, groupStyles, null);
    }

    /**
     * Asserts that the named method returned the right type of separator
     * renderer.
     *
     * @param method the method under test
     * @param expected the expected class of separator renderer
     * @param actual the actual separator renderer returned
     */
    protected void checkSeparator(
            String method,
            Class expected,
            SeparatorRenderer actual) throws Exception {
        if (expected == null) {
            assertNull(method + " should have returned null",
                       actual);
        } else {
            assertSame(method + " should have returned " + expected.getName() +
                       " but returned " + actual.getClass().getName(),
                       expected,
                       actual.getClass());
        }
    }

    /**
     * Returns an instance of the renderer selector under test.
     *
     * @return the instance to be tested
     */
    protected abstract MenuSeparatorRendererSelector createSelector();

    /**
     * Returns the expected class of separator renderer for rendering
     * horizontally oriented menus with a specified horizontal separator type.
     *
     * @param type the required horizontal separator type
     * @return the class of separator renderer expected
     */
    protected abstract Class getMenuSeparatorHorizontalClass(StyleKeyword type);

    /**
     * Returns the expected class of separator renderer for rendering
     * vertically oriented menus.
     *
     * @return the class of separator renderer expected
     */
    protected abstract Class getMenuSeparatorVerticalClass();

    /**
     * Returns the expected class of separator renderer for rendering
     * character menu groups, either at the start or end of the group.
     *
     * @return the class of separator renderer expected
     */
    protected abstract Class getMenuItemGroupSeparatorCharactersClass();

    /**
     * Returns the expected class of separator renderer for rendering
     * image menu groups, either at the start or end of the group.
     *
     * @return the class of separator renderer expected
     */
    protected abstract Class getMenuItemGroupSeparatorImageClass();

    /**
     * Returns the expected class of separator renderer for rendering
     * between menu item components in a horizontally oriented menus with a
     * specified horizontal separator type.
     *
     * @param type the required horizontal separator type
     * @return the class of separator renderer expected
     */
    protected abstract Class getMenuItemSeparatorHorizontalClass(StyleKeyword type);

    /**
     * Returns the expected class of separator renderer for rendering
     * between menu item components in a vertically oriented menus.
     *
     * @return the class of separator renderer expected
     */
    protected abstract Class getMenuItemSeparatorVerticalClass();
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4204/1	philws	VBM:2004042810 Suppress Menu Item Group separators when needed

 22-Apr-04	3681/3	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 ===========================================================================
*/
