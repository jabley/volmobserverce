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
package com.volantis.mcs.protocols.menu.shared.builder;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.TestNormalImageAssetReference;
import com.volantis.mcs.protocols.menu.builder.BuilderException;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventTarget;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuText;
import com.volantis.mcs.protocols.menu.model.ModelElement;
import com.volantis.mcs.protocols.menu.model.PaneTargeted;
import com.volantis.mcs.protocols.menu.model.Titled;
import com.volantis.mcs.protocols.menu.shared.MenuEntityCreation;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenu;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

import java.util.Stack;

/**
 * Tests {@link ConcreteMenuModelBuilder}.
 *
 * @todo later this test could probably do with using some form of command pattern to reduce the cut-n-pasting
 * @todo later much of this could be pushed up into a test abstract for the {@link com.volantis.mcs.protocols.menu.builder.MenuModelBuilder} API.
 */
public class ConcreteMenuModelBuilderTestCase extends TestCaseAbstract {
    /**
     * The builder instance under test.
     */
    ConcreteMenuModelBuilder builder;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        builder = createBuilder();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        builder = null;

        super.tearDown();
    }

    /**
     * Helper method used to populate a menu group in the most minimal manner
     * such that it is valid.
     */
    protected void configureMinimalGroup() throws Exception {
        builder.startMenuItem();
        configureMinimalItem();
        builder.endMenuItem();
    }

    /**
     * Helper method used to populate a menu item in the most minimal manner
     * such that it is valid.
     */
    protected void configureMinimalItem() throws Exception {
        // The currently open menu item must have a specified href
        builder.setHref(new LiteralLinkAssetReference("dummy"));

        // It must also have a label
        builder.startLabel();
        configureMinimalLabel(true);
        builder.endLabel();
    }

    /**
     * Helper method used to populate a menu label in the most minimal manner
     * such that it is valid.
     */
    protected void configureMinimalLabel(boolean inMenuItem) throws Exception {
        // The currently open menu label must have a configured text
        builder.startText();
        configureMinimalText();
        builder.endText();

        if (inMenuItem) {
            // It should only have an icon in a menu item
            builder.startIcon();
            configureMinimalIcon();
            builder.endIcon();
        }
    }

    /**
     * Helper method used to populate a menu icon in the most minimal manner
     * such that it is valid.
     */
    protected void configureMinimalIcon() throws BuilderException {
        // The currently open icon must have a normal image URL
        ImageAssetReference reference = new TestNormalImageAssetReference(
                "dummy");
        builder.setNormalImageURL(reference);
    }

    /**
     * Helper method used to populate a menu text in the most minimal manner
     * such that it is valid.
     */
    protected void configureMinimalText() throws BuilderException {
        // The currently open text must have a text string value
        builder.setText(new DOMOutputBuffer());
    }

    /**
     * Helper method used to create and populate an incomplete menu label (i.e.
     * an invalid label definition).
     */
    protected void configureIncompleteLabel() throws Exception {
        builder.startLabel();

        // The currently open menu label must have a configured text
        builder.startText();
        configureMinimalText();
        builder.endText();

        builder.startIcon();
        // Intentionally create a "bad" icon - no normal URL specified
        builder.endIcon();

        builder.endLabel();
    }

    /**
     * Verifies that the menu model is returned as null until the model is
     * completed.
     */
    public void testGetCompletedMenuModel() throws Exception {
        assertNull("menu model should be null (1)",
                   builder.getCompletedMenuModel());

        builder.startMenu();

        assertNull("menu model should be null (2)",
                   builder.getCompletedMenuModel());

        builder.startMenu();

        assertNull("menu model should be null (3)",
                   builder.getCompletedMenuModel());

        builder.endMenu();

        assertNull("menu model should be null (4)",
                   builder.getCompletedMenuModel());

        builder.endMenu();

        assertNotNull("menu model should be non-null",
                      builder.getCompletedMenuModel());
    }

    /**
     * Menus may be top-level or nested within menus. Note that this explicitly
     * tests some lower-level (menu model) features to ensure that they operate
     * as required by the builder.
     */
    public void testStartMenuSuccess() throws Exception {
        assertNull("menu model should be null",
                   getMenuModel());

        builder.startMenu();

        Menu menu = getMenuModel();

        // Demonstrate that a new menu is created
        assertNotNull("menu model should not be null",
                      getMenuModel());

        assertSame("Menu should be the current entity",
                   menu,
                   getCurrentEntity());

        builder.startMenu();

        assertTrue("current entity should be a menu",
                   getCurrentEntity() instanceof Menu);

        Menu subMenu = (Menu)getCurrentEntity();

        assertNotSame("subMenu should be different from menu",
                      menu,
                      subMenu);

        assertEquals("menu child should have been added",
                     1,
                     menu.getSize());

        assertEquals("menu child's container should have been set",
                     menu,
                     subMenu.getContainer());
    }

    /**
     * Menus may not appear in menu groups, menu items, menu labels, menu icons
     * or menu text.
     */
    public void testStartMenuFailure() throws Exception {
        builder.startMenu();

        builder.startMenuGroup();

        try {
            // Menus are not permitted in menu item groups
            builder.startMenu();

            fail("should have had a builder exception (0)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItemGroup);
        }

        builder.startMenuItem();

        try {
            // Menus are not permitted in menu items
            builder.startMenu();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItem);
        }

        builder.startLabel();

        try {
            // Menus are not permitted in menu labels
            builder.startMenu();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuLabel);
        }

        builder.startIcon();

        try {
            // Menus are not permitted in menu icon
            builder.startMenu();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuIcon);
        }

        configureMinimalIcon();
        builder.endIcon();
        builder.startText();

        try {
            // Menus are not permitted in menu text
            builder.startMenu();

            fail("should have had a builder exception (4)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuText);
        }
    }

    /**
     * Verifies that successful ending of the menu returns null for sub-menus
     * and the menu model for a top-level one.
     */
    public void testEndMenuSuccess() throws Exception {
        builder.startMenu();

        Menu menu = getMenuModel();

        builder.startMenu();

        Object subMenu = getCurrentEntity();

        builder.startMenu();

        assertNull("should not have been given a menu (1)",
                   builder.endMenu());

        assertSame("sub-menu should be current again",
                   subMenu,
                   getCurrentEntity());

        assertNull("should not have been given a menu (2)",
                   builder.endMenu());

        assertSame("should have been given the menu model",
                   menu,
                   builder.endMenu());
    }

    /**
     * Ensures that end menu throws an exception if the builder state is not
     * appropriate.
     */
    public void testEndMenuFailure() throws Exception {
        try {
            // This doesn't balance a start menu call
            builder.endMenu();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
        }

        builder.startMenu();
        builder.startMenuItem();

        try {
            // This doesn't balance a start menu call
            builder.endMenu();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
        }
    }

    /**
     * Ensures that the end menu throws an exception if there is an icon
     * specified in a label directly in the menu.
     */
    public void testEndMenuIconFailure() throws Exception {
        builder.startMenu();
        builder.startLabel();
        // Even though not in a menu item, set to true to force the exception
        configureMinimalLabel(true);
        builder.endLabel();

        try {
            // Icons are not allowed in menus
            builder.endMenu();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
        }

    }

    /**
     * Menu groups may appear in (top-level or nested) menus.
     */
    public void testStartMenuGroupSuccess() throws Exception {
        builder.startMenu();

        Menu menu = (Menu)getCurrentEntity();

        builder.startMenuGroup();

        MenuItemGroup group1 = (MenuItemGroup)getCurrentEntity();

        assertSame("group's container should be the menu",
                   menu,
                   group1.getContainer());

        // Close off this group to allow a sub-menu
        configureMinimalGroup();

        builder.endMenuGroup();

        builder.startMenu();

        Object subMenu = getCurrentEntity();

        builder.startMenuGroup();

        assertSame("group's container should be the sub-menu",
                   subMenu,
                   ((MenuItemGroup)getCurrentEntity()).getContainer());
    }

    /**
     * Menu groups may not appear in menu groups, menu items, menu labels,
     * menu icons or menu text and may not be top-level.
     */
    public void testStartMenuGroupFailure() throws Exception {
        try {
            builder.startMenuGroup();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();
        builder.startMenuGroup();

        try {
            builder.startMenuGroup();
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItemGroup);
        }

        builder.startMenuItem();

        try {
            // Menu groups are not permitted in menu items
            builder.startMenuGroup();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItem);
        }

        builder.startLabel();

        try {
            // Menu groups are not permitted in menu labels
            builder.startMenuGroup();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuLabel);
        }

        builder.startIcon();

        try {
            // Menu groups are not permitted in menu icon
            builder.startMenuGroup();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuIcon);
        }

        configureMinimalIcon();
        builder.endIcon();
        builder.startText();

        try {
            // Menu groups are not permitted in menu text
            builder.startMenuGroup();

            fail("should have had a builder exception (4)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuText);
        }
    }

    /**
     * Menu groups may contain one or more items and may appear in menus.
     */
    public void testEndMenuGroupSuccess() throws Exception {
        builder.startMenu();

        builder.startMenuGroup();

        configureMinimalGroup();

        configureMinimalGroup();

        builder.endMenuGroup();

        assertSame("menu should be current again",
                   getMenuModel(),
                   getCurrentEntity());
    }

    /**
     * Ensures that end menu group throws an exception if the builder state is
     * not appropriate.
     */
    public void testEndMenuGroupFailure() throws Exception {
        try {
            // This doesn't balance a start menu group call
            builder.endMenuGroup();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();

        try {
            // This doesn't balance a start menu group call
            builder.endMenuGroup();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof Menu);
        }

        builder.startMenuGroup();

        try {
            // This leaves the menu group incompletely specified
            builder.endMenuGroup();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
        }
    }

    /**
     * Menu items may appear in (top-level or nested) menus and menu groups.
     */
    public void startMenuItemSuccess() throws Exception {
        builder.startMenu();

        Object container = getCurrentEntity();
        MenuItem item;
        Object menu;

        builder.startMenuItem();

        item = (MenuItem)getCurrentEntity();

        assertSame("container (1) not as",
                   container,
                   item.getContainer());

        assertSame("menu (1) not as",
                   container,
                   item.getMenu());

        configureMinimalItem();

        builder.endMenuItem();

        builder.startMenu();

        // This menu will be the menu for items in this menu or in menu groups
        // that are children of this menu
        menu = container = getCurrentEntity();

        builder.startMenuItem();

        item = (MenuItem)getCurrentEntity();

        assertSame("container (2) not as",
                   container,
                   item.getContainer());

        assertSame("menu (2) not as",
                   container,
                   item.getMenu());

        configureMinimalItem();

        builder.endMenuItem();

        builder.startMenuGroup();

        container = getCurrentEntity();

        builder.startMenuItem();

        item = (MenuItem)getCurrentEntity();

        assertSame("container (3) not as",
                   container,
                   item.getContainer());

        assertSame("menu (3) not as",
                   menu,
                   item.getMenu());
    }

    /**
     * Menu items may not appear in menu items, menu labels, menu icons or menu
     * text and may not be top-level.
     */
    public void testStartMenuItemFailure() throws Exception {
        try {
            builder.startMenuItem();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();
        builder.startMenuItem();

        try {
            // Menu items are not permitted in menu items
            builder.startMenuItem();
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItem);
        }

        builder.startLabel();

        try {
            // Menu items are not permitted in menu labels
            builder.startMenuItem();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuLabel);
        }

        builder.startIcon();

        try {
            // Menu items are not permitted in menu icon
            builder.startMenuItem();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuIcon);
        }

        configureMinimalIcon();
        builder.endIcon();
        builder.startText();

        try {
            // Menu items are not permitted in menu text
            builder.startMenuItem();

            fail("should have had a builder exception (4)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuText);
        }
    }

    /**
     * Menu items may appear in menus and menu groups.
     */
    public void testEndMenuItemSuccess() throws Exception {
        builder.startMenu();

        builder.startMenuGroup();

        Object current = getCurrentEntity();

        builder.startMenuItem();

        configureMinimalItem();

        builder.endMenuItem();

        assertSame(current,
                   getCurrentEntity());

        builder.endMenuGroup();

        builder.startMenuItem();

        configureMinimalItem();

        builder.endMenuItem();

        assertSame("menu should be current again",
                   getMenuModel(),
                   getCurrentEntity());
    }

    /**
     * Ensures that end menu item throws an exception if the builder state is
     * not appropriate.
     */
    public void testEndMenuItemFailure() throws Exception {
        try {
            // This doesn't balance a start menu item call
            builder.endMenuItem();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();

        try {
            // This doesn't balance a start menu item call
            builder.endMenuItem();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof Menu);
        }

        builder.startMenuItem();

        try {
            // This leaves the menu item incompletely specified
            builder.endMenuItem();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
        }

        // The currently open menu item must have a specified href
        builder.setHref(new LiteralLinkAssetReference("dummy"));

        configureIncompleteLabel();

        try {
            // This leaves the menu item incompletely specified
            builder.endMenuItem();

            fail("should have had a builder exception (4)");
        } catch (BuilderException e) {
            // expected condition
        }

    }

    /**
     * Menu labels may appear in (top-level or nested) menus and menu items.
     */
    public void testStartLabelAndEndLabelSuccess() throws Exception {
        builder.startMenu();

        builder.startLabel();

        assertTrue("MenuLabel should be open",
                   getCurrentEntity() instanceof MenuLabel);

        configureMinimalLabel(false);

        builder.endLabel();

        assertSame("containing menu should be current",
                   getMenuModel(),
                   getCurrentEntity());

        builder.startMenu();

        Object current = getCurrentEntity();

        builder.startLabel();

        assertTrue("MenuLabel should be open",
                   getCurrentEntity() instanceof MenuLabel);

        configureMinimalLabel(false);

        builder.endLabel();

        assertSame("nested menu should be current",
                   current,
                   getCurrentEntity());

        builder.startMenuItem();

        current = getCurrentEntity();

        builder.startLabel();

        assertTrue("MenuLabel should be open",
                   getCurrentEntity() instanceof MenuLabel);

        configureMinimalLabel(true);

        builder.endLabel();

        assertSame("menu item should be current",
                   current,
                   getCurrentEntity());
    }

    /**
     * Menu labels may not appear in menu groups, menu labels, menu icons or
     * menu text and may not be top-level.
     */
    public void testStartLabelFailure() throws Exception {
        try {
            builder.startLabel();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();
        builder.startMenuGroup();

        try {
            // Labels are not permitted in menu groups
            builder.startLabel();
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItemGroup);
        }

        builder.startMenuItem();
        builder.startLabel();

        try {
            // Labels are not permitted in menu labels
            builder.startLabel();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuLabel);
        }

        builder.startIcon();

        try {
            // Labels are not permitted in menu icon
            builder.startLabel();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuIcon);
        }

        configureMinimalIcon();
        builder.endIcon();
        builder.startText();

        try {
            // Labels are not permitted in menu text
            builder.startLabel();

            fail("should have had a builder exception (4)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuText);
        }
    }

    /**
     * Ensures that end label throws an exception if the builder state is
     * not appropriate (see {@link #testEndLabelFailureInMenuItem} for
     * additional tests).
     */
    public void testEndLabelFailure() throws Exception {
        try {
            // This doesn't balance a start label call
            builder.endLabel();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();

        try {
            // This doesn't balance a start label call
            builder.endLabel();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof Menu);
        }
    }

    /**
     * Checks that ending an incomplete label in a menu item throws a builder
     * exception.
     */
    public void testEndLabelFailureInMenuItem() throws Exception {
        builder.startMenu();
        builder.startMenuItem();
        builder.startLabel();
        builder.startText();
        configureMinimalText();
        builder.endText();
        builder.startIcon();
        // This leaves the menu label incomplete (no normal URL)
        builder.endIcon();
        builder.endLabel();

        try {
            builder.endMenuItem();

            fail("should have had a builder exception");
        } catch (BuilderException e) {
            // expected condition
        }
    }

    /**
     * Menu icons may appear in menu items' menu labels.
     */
    public void testStartIconAndEndIconSuccess() throws Exception {
        builder.startMenu();

        builder.startMenuItem();

        builder.startLabel();

        Object label = getCurrentEntity();

        builder.startIcon();

        assertTrue("MenuIcon should be open",
                   getCurrentEntity() instanceof MenuIcon);

        configureMinimalIcon();
        builder.endIcon();

        assertSame("label should now be current",
                   label,
                   getCurrentEntity());
    }

    /**
     * Menu icons may not appear in menus, menu groups, menu items, menu icons
     * or menu text, may not be top-level and may not appear in menus' menu
     * labels.
     */
    public void testStartIconFailure() throws Exception {
        try {
            builder.startIcon();

            fail("should have had a builder exception (0)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();

        try {
            builder.startIcon();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof Menu);
        }

        builder.startLabel();

        builder.startText();
        configureMinimalText();
        builder.endText();

        builder.endLabel();

        builder.startMenuGroup();

        try {
            // Icons are not permitted in menu groups
            builder.startIcon();
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItemGroup);
        }

        builder.startMenuItem();

        try {
            // Icons are not permitted in menu items
            builder.startIcon();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItem);
        }

        builder.startLabel();
        builder.startIcon();

        try {
            // Icons are not permitted in menu icons
            builder.startIcon();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuIcon);
        }

        configureMinimalIcon();
        builder.endIcon();
        builder.startText();

        try {
            // Icons are not permitted in menu texts
            builder.startIcon();

            fail("should have had a builder exception (4)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuText);
        }
    }

    /**
     * Ensures that end icon throws an exception if the builder state is
     * not appropriate.
     */
    public void testEndIconFailure() throws Exception {
        try {
            // This doesn't balance a start icon call
            builder.endIcon();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();

        try {
            // This doesn't balance a start icon call
            builder.endIcon();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof Menu);
        }
    }

    /**
     * Menu text may appear in menu and menu items' menu labels.
     */
    public void testStartTextAndEndTextSuccess() throws Exception {
        builder.startMenu();

        builder.startLabel();

        Object label = getCurrentEntity();

        builder.startText();

        assertTrue("MenuText should be open (1)",
                   getCurrentEntity() instanceof MenuText);

        configureMinimalText();

        builder.endText();

        assertSame("label should now be current (1)",
                   label,
                   getCurrentEntity());

        builder.endLabel();

        builder.startMenuItem();

        builder.startLabel();

        label = getCurrentEntity();

        builder.startText();

        assertTrue("MenuText should be open (2)",
                   getCurrentEntity() instanceof MenuText);

        configureMinimalText();

        builder.endText();

        assertSame("label should now be current (2)",
                   label,
                   getCurrentEntity());
    }

    /**
     * Menu text may not appear in menus, menu groups, menu items, menu icons
     * or menu text and may not be top-level.
     */
    public void testStartTextFailure() throws Exception {
        try {
            builder.startText();

            fail("should have had a builder exception (0)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();

        try {
            // Texts are not permitted in menus
            builder.startText();
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof Menu);
        }

        builder.startMenuGroup();

        try {
            // Texts are not permitted in menu groups
            builder.startText();
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItemGroup);
        }

        builder.startMenuItem();

        try {
            // Texts are not permitted in menu items
            builder.startText();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuItem);
        }

        builder.startLabel();
        builder.startIcon();

        try {
            // Texts are not permitted in menu icons
            builder.startText();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuIcon);
        }

        configureMinimalIcon();
        builder.endIcon();
        builder.startText();

        try {
            // Texts are not permitted in menu texts
            builder.startText();

            fail("should have had a builder exception (4)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof MenuText);
        }
    }

    /**
     * Ensures that end text throws an exception if the builder state is
     * not appropriate.
     */
    public void testEndTextFailure() throws Exception {
        try {
            // This doesn't balance a start text call
            builder.endText();

            fail("should have had a builder exception (1)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();

        try {
            // This doesn't balance a start text call
            builder.endText();

            fail("should have had a builder exception (2)");
        } catch (BuilderException e) {
            // expected condition
            assertTrue(getCurrentEntity() instanceof Menu);
        }

        builder.startMenuItem();
        builder.startLabel();
        builder.startText();

        try {
            // This leaves the menu text incompletely specified
            builder.endText();

            fail("should have had a builder exception (3)");
        } catch (BuilderException e) {
            // expected condition
        }
    }

    /**
     * An extended command pattern interface that allows simplification of
     * testing of the various "build data" methods on the builder API.
     */
    protected interface BuildDataMethodTester {
        /**
         * Invokes the required build data method.
         *
         * @throws Exception if the method invocation is not valid given the
         *                   build state
         */
        // javadoc inherited
        public void invoke() throws Exception;

        /**
         * When a build data method has been successfully invoked (no
         * exception thrown), this method is used to verify that the setting
         * was valid.
         *
         * @throws Exception if the verification fails
         */
        // javadoc inherited
        public void verify() throws Exception;

        /**
         * Returns true if the invocation should be successful on a specified
         * class. These classes will be implementations of the various menu
         * model interfaces.
         *
         * @return true if the given class should support the associated build
         *         data method call
         */
        // javadoc inherited
        public boolean supportedOn(Class clazz);
    }

    /**
     * Runs the specified build data method tester against all available menu
     * model entities.
     *
     * @param tester an extended command pattern object that can invoke the
     *               required build data method, can check that it worked
     *               and indicates which menu model entities should support
     *               the data method call
     */
    public void doBuildDataMethodTest(BuildDataMethodTester tester)
            throws Exception {
        try {
            // May never be top-level
            tester.invoke();

            fail("should have had a builder exception (0)");
        } catch (BuilderException e) {
            // expected condition
            assertNull(getCurrentEntity());
        }

        builder.startMenu();

        checkBuildDataMethodInvocation(tester);

        builder.startMenuGroup();

        checkBuildDataMethodInvocation(tester);

        builder.startMenuItem();

        checkBuildDataMethodInvocation(tester);

        builder.startLabel();

        checkBuildDataMethodInvocation(tester);

        builder.startIcon();

        checkBuildDataMethodInvocation(tester);

        // Make sure the icon can be closed successfully
        try {
            configureMinimalIcon();
        } catch (Exception e) {
            // Ignored
        }

        builder.endIcon();

        builder.startText();

        checkBuildDataMethodInvocation(tester);
    }

    /**
     * Supporting method that checks an invocation of the tester's build data
     * method behaves as expected.
     *
     * @param tester the tester used to invoke the build data method and
     *               verify results
     * @throws Exception if something doesn't work as expected
     */
    protected void checkBuildDataMethodInvocation(
            BuildDataMethodTester tester) throws Exception {
        try {
            tester.invoke();

            if (!tester.supportedOn(getCurrentEntity().getClass())) {
                fail("should have failed on " +
                     getCurrentEntity().getClass().getName());
            } else {
                tester.verify();
            }
        } catch (BuilderException e) {
            if (tester.supportedOn(getCurrentEntity().getClass())) {
                fail("should not have failed on " +
                     getCurrentEntity().getClass().getName());
            }
        }
    }

    /**
     * A pane may be specified on any menu entry but nothing else.
     */
    public void testSetPane() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            FormatReference pane = new FormatReference(
                    "pane",
                    NDimensionalIndex.ZERO_DIMENSIONS);

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setPane(pane);
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertSame(pane,
                           ((PaneTargeted)getCurrentEntity()).getPane());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return PaneTargeted.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     *
     */
    public void testCheckNestedPanes() throws Exception {

        // Test pane to use when calling checkNestedPanes
        FormatReference pane =
                new FormatReference("test-pane",
                                    NDimensionalIndex.ZERO_DIMENSIONS);

        // Build a one level menu
        MenuEntityCreation entities = new MenuEntityCreation();
        ConcreteMenu menu =
                new ConcreteMenu(entities.createTestElementDetails());
        menu.setTitle("Top Level Menu");
        menu.setPane(pane);
        boolean result = false;
        try {
            PrivateAccessor.setField(builder, "menu", menu);
            // Test success
            Boolean objectResult =
                    (Boolean) PrivateAccessor.invoke(builder,
                                                     "checkNestedPanes",
                                                     new Class[] {
                                                         FormatReference.class
                                                     },
                                                     new Object[] {pane});
            result = objectResult.booleanValue();
        } catch (Throwable t) {
            fail("Test infrastructure failed (1)");
        }
        assertTrue("Should have returned true for the check", result);

        // Build a two level menu
        ConcreteMenu menuTwo =
                new ConcreteMenu(entities.createTestElementDetails());
        menuTwo.setTitle("Sub Menu");
        menuTwo.setPane(pane);
        menu.add(menuTwo);
        try {
            PrivateAccessor.setField(builder, "menu", menuTwo);
            // Test failure
            Boolean objectResult =
                    (Boolean) PrivateAccessor.invoke(builder,
                                                     "checkNestedPanes",
                                                     new Class[] {
                                                         FormatReference.class
                                                     },
                                                     new Object[] {pane});
            result = objectResult.booleanValue();
        } catch (Throwable t) {
            fail("Test infrastructure failed (2)");
        }
        assertFalse("Should have returned false for the check", result);

    }

    /**
     * A prompt may only be set on a menu or menu item.
     */
    public void testSetPrompt() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            String prompt = "prompt";

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setPrompt(new LiteralTextAssetReference(prompt));
            }

            // javadoc inherited
            public void verify() throws Exception {
                if (getCurrentEntity() instanceof Menu) {
                    assertEquals(prompt,
                                 ((Menu)getCurrentEntity()).getPrompt()
                            .getText(TextEncoding.PLAIN));
                } else {
                    assertEquals(prompt,
                                 ((MenuItem)getCurrentEntity()).getPrompt()
                            .getText(TextEncoding.PLAIN));
                }
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return Menu.class.isAssignableFrom(clazz) ||
                        MenuItem.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * An error message may only be set on a menu.
     */
    public void testSetErrorMessage() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            String message = "message";

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setErrorMessage(new LiteralTextAssetReference(message));
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals(message,
                             ((Menu)getCurrentEntity()).getErrorMessage()
                        .getText(TextEncoding.PLAIN));
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return Menu.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Help may only be set on a menu.
     */
    public void testSetHelp() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            String help = "help";

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setHelp(new LiteralTextAssetReference(help));
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals(help,
                             ((Menu)getCurrentEntity()).getHelp()
                        .getText(TextEncoding.PLAIN));
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return Menu.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Event handlers may only be set on menus and menu items. Note that not
     * all event handler types are applicable to both. Choosing one that does
     * for this test!
     */
    public void testSetEventHandler() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            EventType type = EventType.ON_CLICK;

            /**
             * Sample data for use in testing
             */
            String handler = "dummy";

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setEventHandler(type,
                        new LiteralScriptAssetReference(handler));
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals(handler,
                             ((EventTarget)getCurrentEntity()).
                             getEventHandler(type).getScript());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return EventTarget.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Href may only be set on a menu item.
     */
    public void testSetHref() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            LinkAssetReference href = new LiteralLinkAssetReference("href");

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setHref(href);
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals(href,
                             ((MenuItem)getCurrentEntity()).getHref());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return MenuItem.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Segment may only be set on a menu item.
     */
    public void testSetSegment() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            String segment = "segment";

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setSegment(segment);
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals((Object)segment,
                             ((MenuItem)getCurrentEntity()).getSegment());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return MenuItem.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Target may only be set on a menu item.
     */
    public void testSetTarget() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            String target = "target";

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setTarget(target);
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals((Object)target,
                             ((MenuItem)getCurrentEntity()).getTarget());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return MenuItem.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Title may only be set on menus and menu items.
     */
    public void testSetTitle() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            String title = "dummy";

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setTitle(title);
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals((Object)title,
                             ((Titled)getCurrentEntity()).
                             getTitle());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return Titled.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Image URLs may only be set on a menu icon.
     */
    public void testSetNormalImageURL() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            ImageAssetReference reference = new TestNormalImageAssetReference(
                    "url");
            // javadoc inherited
            public void invoke() throws Exception {
                builder.setNormalImageURL(reference);
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals(reference,
                             ((MenuIcon)getCurrentEntity()).getNormalURL());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return MenuIcon.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Image URLs may only be set on a menu icon.
     */
    public void testSetOverImageURL() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            ImageAssetReference reference = new TestNormalImageAssetReference(
                    "url");

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setOverImageURL(reference);
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals(reference,
                             ((MenuIcon)getCurrentEntity()).getOverURL());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return MenuIcon.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Element details may only be set on entities that correspond to PAPI
     * elements (i.e. that implement ModelElement).
     */
    public void testSetElementDetails() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            String elementName = "element";

            /**
             * Sample data for use in testing
             */
            String id = "id";

            /**
             * Sample data for use in testing
             */
            Styles styles = StylesBuilder.getEmptyStyles();

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setElementDetails(elementName, id, styles);
            }

            // javadoc inherited
            public void verify() throws Exception {
                ModelElement modelElement = (ModelElement)getCurrentEntity();

                ElementDetails elementDetails =
                        modelElement.getElementDetails();

                assertEquals((Object)elementName,
                             elementDetails.getElementName());
                assertEquals((Object)id,
                             elementDetails.getId());
                assertEquals(styles,
                             elementDetails.getStyles());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return ModelElement.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Shortcut may only be set on a menu item.
     */
    public void testSetShortcut() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            String shortcut = "shortcut";

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setShortcut(new LiteralTextAssetReference(shortcut));
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertEquals(shortcut,
                             ((MenuItem)getCurrentEntity()).getShortcut()
                        .getText(TextEncoding.PLAIN));
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return MenuItem.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Text may only be set on a menu text.
     */
    public void testSetText() throws Exception {
        doBuildDataMethodTest(new BuildDataMethodTester() {
            /**
             * Sample data for use in testing
             */
            OutputBuffer text = new DOMOutputBuffer();

            // javadoc inherited
            public void invoke() throws Exception {
                builder.setText(text);
            }

            // javadoc inherited
            public void verify() throws Exception {
                assertSame(text,
                           ((MenuText)getCurrentEntity()).getText());
            }

            // javadoc inherited
            public boolean supportedOn(Class clazz) {
                return MenuText.class.isAssignableFrom(clazz);
            }
        });
    }

    /**
     * Helper method returning the current menu model for the {@link #builder}.
     *
     * @return the current menu model
     */
    protected Menu getMenuModel() {
        Menu menu = null;

        try {
            Stack entities =
                (Stack)PrivateAccessor.getField(builder, "entities");

            if ((entities != null) &&
                !entities.isEmpty()) {
                menu = (Menu)entities.get(0);
            }
        } catch (NoSuchFieldException e) {
            fail("test infrastructure failure");
        }

        return menu;
    }

    /**
     * Helper method returning the current entity from the {@link #builder}.
     *
     * @return the current entity
     */
    protected Object getCurrentEntity() {
        Object currentEntity = null;

        try {
            currentEntity = PrivateAccessor.getField(builder, "currentEntity");
        } catch (NoSuchFieldException e) {
            fail("test infrastructure failure");
        }

        return currentEntity;
    }

    /**
     * Factory method that can be overridden by specializations to handle
     * different builder specializations.
     */
    protected ConcreteMenuModelBuilder createBuilder() {
        return new ConcreteMenuModelBuilder();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 27-Sep-05	9609/3	ibush	VBM:2005082215 Move on/off color values for menu items

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4220/1	claire	VBM:2004050603 Enhance Menu Support: Builder: Validate nested pane names

 06-May-04	3999/1	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 06-Apr-04	3429/7	philws	VBM:2004031502 MenuLabelElement implementation

 06-Apr-04	3641/5	claire	VBM:2004032602 Enhancements and updating testcase coverage

 06-Apr-04	3641/3	claire	VBM:2004032602 Corrected icon and label validation

 30-Mar-04	3641/1	claire	VBM:2004032602 Using menu types and styles in PAPI

 29-Mar-04	3500/6	claire	VBM:2004031806 Fixed supermerge issues

 26-Mar-04	3500/3	claire	VBM:2004031806 Initial implementation of abstract component image references

 26-Mar-04	3491/3	philws	VBM:2004031912 Add handling of title to Menu Label

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 15-Mar-04	3342/3	philws	VBM:2004022707 Review comment updates and changing MenuItem href to Object

 15-Mar-04	3342/1	philws	VBM:2004022707 Implement the Menu Model Builder

 ===========================================================================
*/
