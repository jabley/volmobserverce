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
package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ShortcutProperties;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuItemGroup;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitor;
import com.volantis.mcs.protocols.menu.model.MenuModelVisitorException;
import com.volantis.mcs.protocols.menu.model.ModelElement;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a unit test for the AbstractMenuModelVisitor class which forms part
 * of the menu model.  It is final and not abstract because whilst it tests
 * an abstract class there is no concrete implementation provided.  This means
 * that there was a need to test the framework using this class.  Anything
 * that extends {@link AbstractMenuModelHandler} should NOT unit test by
 * extending this test class.
 *
 */
public final class AbstractMenuModelVisitorTestCase extends TestCaseAbstract {

    /**
     * Available to allow equality checks to succeed.
     */
    OutputBuffer label = new DOMOutputBuffer();

    /**
     * Create a new instance of this test case.
     */
    public AbstractMenuModelVisitorTestCase() {
        super();
    }

    /**
     * Create a new named instance of this test case.
     *
     * @param s The name of the test case
     */
    public AbstractMenuModelVisitorTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the visit methods of the class using a suitable instance of the
     * abstract class.
     */
    public void testVisiting() throws Exception {

        final int LIMIT = 3;

        SimpleAbstractMenuModelVisitor testClass =
                new SimpleAbstractMenuModelVisitor(LIMIT, LIMIT, LIMIT);

        // Test a menu item
        SimpleMenuItem item = new SimpleMenuItem();
        testClass.visit(item);
        assertEquals("Number of visits not as", 1, testClass.getItemCount());

        // Test a menu item group
        SimpleMenuItemGroup itemGroup = new SimpleMenuItemGroup();
        SimpleMenuItem[] items = {item,
                                  new SimpleMenuItem(),
                                  new SimpleMenuItem(),
                                  new SimpleMenuItem(),
                                  new SimpleMenuItem()};
        for (int i = 0; i < items.length; i++) {
            itemGroup.add(items[i]);
        }
        testClass = new SimpleAbstractMenuModelVisitor(LIMIT, LIMIT, LIMIT);
        testClass.visit(itemGroup);
        assertEquals("Number of visits not as",
                5, testClass.getItemCount());
        assertEquals("Number of visits not as",
                1, testClass.getGroupCount());

        // Test a menu
        SimpleMenu menu = new SimpleMenu();
        SimpleMenuItemGroup[] groups = {itemGroup,
                                        new SimpleMenuItemGroup(),
                                        new SimpleMenuItemGroup(),
                                        new SimpleMenuItemGroup(),
                                        new SimpleMenuItemGroup()};
        for (int i = 0; i < groups.length; i++) {
            menu.add(groups[i]);
        }
        testClass = new SimpleAbstractMenuModelVisitor(LIMIT, LIMIT, LIMIT);
        testClass.visit(menu);
        assertEquals("Number of visits not as",
                5, testClass.getItemCount());
        assertEquals("Number of visits not as",
                5, testClass.getGroupCount());
        assertEquals("Number of visits not as",
                1, testClass.getMenuCount());
    }

    public void testVisitItemException() throws Exception {
        SimpleAbstractMenuModelVisitor visitor =
                new SimpleAbstractMenuModelVisitor(1, 0, 0, 1, 0, 0);
        SimpleMenuItem entry = new SimpleMenuItem();

        try {
            entry.accept(visitor);

            fail("should have had an exception thrown");
        } catch (MenuModelVisitorException e) {
            // Expected condition
        }
    }

    public void testVisitGroupException() throws Exception {
        SimpleAbstractMenuModelVisitor visitor =
                new SimpleAbstractMenuModelVisitor(0, 1, 0, 0, 1, 0);
        SimpleMenuItemGroup entry = new SimpleMenuItemGroup();

        try {
            entry.accept(visitor);

            fail("should have had an exception thrown");
        } catch (MenuModelVisitorException e) {
            // Expected condition
        }
    }

    public void testVisitMenuException() throws Exception {
        SimpleAbstractMenuModelVisitor visitor =
                new SimpleAbstractMenuModelVisitor(0, 0, 1, 0, 0, 1);
        SimpleMenu entry = new SimpleMenu();

        try {
            entry.accept(visitor);

            fail("should have had an exception thrown");
        } catch (MenuModelVisitorException e) {
            // Expected condition
        }
    }

    /**
     * A simple instantiation of the abstract AbstractMenuModelVisitor class.
     * This class implements the necessary methods and ensures basic
     * functionality works in the handle methods.
     */
    class SimpleAbstractMenuModelVisitor extends AbstractMenuModelHandler {

        private int itemCount = 0;
        private int groupCount = 0;
        private int menuCount = 0;

        private final int itemLimit;
        private final int groupLimit;
        private final int menuLimit;

        /**
         * Set to &lt;= 0 if you don't want to cause an exception to be thrown
         * or to a posititve integer if you want an exception to be thrown when
         * the <i>throwOnItemCount</i>th menu item is visited.
         */
        private final int throwOnItemCount;

        /**
         * Set to &lt;= 0 if you don't want to cause an exception to be thrown
         * or to a posititve integer if you want an exception to be thrown when
         * the <i>throwOnGroupCount</i>th menu group is visited.
         */
        private final int throwOnGroupCount;

        /**
         * Set to &lt;= 0 if you don't want to cause an exception to be thrown
         * or to a posititve integer if you want an exception to be thrown when
         * the <i>throwOnMenuCount</i>th menu is visited.
         */
        private final int throwOnMenuCount;

        public SimpleAbstractMenuModelVisitor(int itemLimit,
                                              int groupLimit,
                                              int menuLimit) {
            this(itemLimit, groupLimit, menuLimit, 0, 0, 0);
        }

        public SimpleAbstractMenuModelVisitor(int itemLimit,
                                              int groupLimit,
                                              int menuLimit,
                                              int throwOnItemCount,
                                              int throwOnGroupCount,
                                              int throwOnMenuCount) {
            this.itemLimit = itemLimit;
            this.groupLimit = groupLimit;
            this.menuLimit = menuLimit;
            this.throwOnItemCount = throwOnItemCount;
            this.throwOnGroupCount = throwOnGroupCount;
            this.throwOnMenuCount = throwOnMenuCount;
        }

        // JavaDoc inherited
        public boolean handle(MenuItem item) throws MenuModelVisitorException {
            assertNotNull("Cannot visit null menu items", item);
            itemCount++;

            if (itemCount == throwOnItemCount) {
                throw new MenuModelVisitorException("thrown at item count " +
                                                    itemCount);
            }

            return (itemCount < itemLimit);
        }

        // JavaDoc inherited
        public boolean handle(MenuItemGroup group)
                throws MenuModelVisitorException {
            assertNotNull("Cannot visit null menu item groups", group);
            groupCount++;

            if (groupCount == throwOnGroupCount) {
                throw new MenuModelVisitorException("thrown at group count " +
                                                    groupCount);
            }

            return (groupCount < groupLimit);
        }

        // JavaDoc inherited
        public boolean handle(Menu menu) throws MenuModelVisitorException {
            assertNotNull("Cannot visit null menus", menu);
            menuCount++;

            if (menuCount == throwOnMenuCount) {
                throw new MenuModelVisitorException("thrown at menu count " +
                                                    menuCount);
            }

            return (menuCount < menuLimit);
        }

        /**
         * Obtains the item count from this class
         * @return the number of calls made to handle an item
         */
        public int getItemCount() {
            return itemCount;
        }

        /**
         * Obtains the item group count from this class
         * @return the number of calls made to handle an item group
         */
        public int getGroupCount() {
            return groupCount;
        }

        /**
         * Obtains the menu count from this class
         * @return the number of class made to handle a menu
         */
        public int getMenuCount() {
            return menuCount;
        }
    }

    /**
     * Implementation of Styled for visitor testing purposes
     */
    class SimpleModelElement implements ModelElement {
        public SimpleModelElement() {
        }

        // JavaDoc inherited
        public ElementDetails getElementDetails() {
            ConcreteElementDetails elementDetails = new ConcreteElementDetails();
            elementDetails.setElementName("Element");
            elementDetails.setStyles(StylesBuilder.getEmptyStyles());
            return elementDetails;
        }
    }

    /**
     * Implementation of MenuEntry for visitor testing purposes
     */
    abstract class SimpleMenuEntryAbstract extends SimpleModelElement
                                           implements MenuEntry {
        private MenuEntry container;
        private FormatReference pane;

        // JavaDoc inherited
        public FormatReference getPane() {
            return pane;
        }

        /**
         * Allow the pane to be set.
         *
         * @param pane the required pane
         */
        public void setPane(FormatReference pane) {
            this.pane = pane;
        }

        /**
         * Sets the container that this menu entry is held within.  It is valid
         * to call this with a null parameter if this menu entry is the root of
         * the menu model.
         *
         * @param container The parent of this menu entry
         */
        void setContainer(MenuEntry container) {
            this.container = container;
        }

        // JavaDoc inherited
        public MenuEntry getContainer() {
            return container;
        }

        // JavaDoc inherited
        public abstract void accept(MenuModelVisitor visitor)
                throws MenuModelVisitorException;
    }

    /**
     * Implementation of Menu for visitor testing purposes
     */
    class SimpleMenu extends SimpleMenuEntryAbstract implements Menu {
        List menuEntries;

        public SimpleMenu() {
            menuEntries = new ArrayList();
        }

        // JavaDoc inherited
        public MenuItem getAsMenuItem() {
            return null;
        }

        // JavaDoc inherited
        public int getSize() {
            return menuEntries.size();
        }

        // JavaDoc inherited
        public MenuEntry get(int index) throws IndexOutOfBoundsException {
            return (MenuEntry) menuEntries.get(index);
        }

        /**
         * Add another entry to the collection of objects held by this menu.
         * This will set the parent of the entry being added to the instance
         * of this class that will hold it.
         *
         * @param newEntry The entry to add
         */
        public void add(MenuEntry newEntry) {
            menuEntries.add(newEntry);
            ((SimpleMenuEntryAbstract)newEntry).setContainer(this);
        }

        /**
         * Remove the given entry from the collection maintained by this menu.
         * If the removal is successful then the container of the element
         * removed is set to null.
         *
         * @param oldEntry The entry to remove
         * @return true    if the item existed and was removed successfully,
         *                 false otherwise
         */
        public boolean remove(MenuEntry oldEntry) {
            boolean success = menuEntries.remove(oldEntry);
            if (success) {
                ((SimpleMenuEntryAbstract)oldEntry).setContainer(null);
            }
            return success;
        }

        // JavaDoc inherited
        public TextAssetReference getHelp() {
            return new LiteralTextAssetReference("Help");
        }

        // JavaDoc inherited
        public MenuLabel getLabel() {
            return MenuModelHelper.createMenuLabel(label);
        }

        // JavaDoc inherited
        public TextAssetReference getErrorMessage() {
            return new LiteralTextAssetReference("Error");
        }

        // JavaDoc inherited
        public TextAssetReference getPrompt() {
            return new LiteralTextAssetReference("Prompt");
        }

        // JavaDoc inherited
        public String getTitle() {
            return "Title";
        }

        // JavaDoc inherited
        public ScriptAssetReference getEventHandler(EventType type) {
            return null;
        }

        // JavaDoc inherited.
        public ShortcutProperties getShortcutProperties() {
            return null;
        }

        public void accept(MenuModelVisitor visitor)
                throws MenuModelVisitorException {
            visitor.visit(this);
        }
    }

    /**
     * Implementation of MenuItem for visitor testing purposes
     */
    class SimpleMenuItem extends SimpleMenuEntryAbstract implements MenuItem {
        public SimpleMenuItem() {
        }

        // JavaDoc inherited
        public LinkAssetReference getHref() {
            return new LiteralLinkAssetReference("some/relative/path");
        }

        // JavaDoc inherited
        public TextAssetReference getPrompt() {
            return new LiteralTextAssetReference("Prompt");
        }

        // JavaDoc inherited
        public MenuLabel getLabel() {
            return MenuModelHelper.createMenuLabel(label);
        }

        // JavaDoc inherited
        public String getTitle() {
            return "Title";
        }

        // JavaDoc inherited
        public void accept(MenuModelVisitor visitor)
                throws MenuModelVisitorException {
            visitor.visit(this);
        }

        // JavaDoc inherited
        public TextAssetReference getShortcut() {
            return new LiteralTextAssetReference("Shortcut");
        }

        // JavaDoc inherited
        public Menu getMenu() {
            return new SimpleMenu();
        }

        // JavaDoc inherited
        public String getSegment() {
            return "Segment";
        }

        // JavaDoc inherited
        public String getTarget() {
            return "Target";
        }

        // JavaDoc inherited
        public ScriptAssetReference getEventHandler(EventType type) {
            return null;
        }

        // JavaDoc inherited
        public boolean isSubMenuItem() {
            return false;
        }
    }

    /**
     * Implementation of MenuUItemGroup for visitor testing purposes
     */
    class SimpleMenuItemGroup extends SimpleMenuEntryAbstract
                              implements MenuItemGroup {
        List menuItems;

        public SimpleMenuItemGroup() {
            menuItems = new ArrayList();
        }

        // JavaDoc inherited
        public void accept(MenuModelVisitor visitor)
                throws MenuModelVisitorException {
            visitor.visit(this);
        }

        // JavaDoc inherited
        public  MenuItem get(int index) throws IndexOutOfBoundsException {
            return (MenuItem) menuItems.get(index);
        }

        /**
         * Add another entry to the collection of objects held by this menu.
         * This will set the parent of the entry being added to the instance
         * of this class that will hold it.
         *
         * @param newEntry The entry to add
         */
        public void add(MenuItem newEntry) {
            menuItems.add(newEntry);
            ((SimpleMenuEntryAbstract)newEntry).setContainer(this);
        }

        /**
         * Remove the given entry from the collection maintained by this menu.
         * If the removal is successful then the container of the element
         * removed is set to null.
         *
         * @param oldEntry The entry to remove
         * @return true    if the item existed and was removed successfully,
         *                 false otherwise
         */
        public boolean remove(MenuItem oldEntry) {
            boolean success = menuItems.remove(oldEntry);
            if (success) {
                ((SimpleMenuItem)oldEntry).setContainer(null);
            }
            return success;
        }


        // JavaDoc inherited
        public int getSize() {
            return menuItems.size();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 04-May-04	4164/1	pduffin	VBM:2004050404 Refactored DefaultMenuRenderer internal visitor class to allow pre and post processing.

 20-Apr-04	3945/1	claire	VBM:2004042006 Providing menu as menu item functionality

 05-Apr-04	3429/4	philws	VBM:2004031502 MenuLabelElement implementation

 05-Apr-04	3733/1	philws	VBM:2004040504 Added MenuModelVisitorException

 15-Mar-04	3342/1	philws	VBM:2004022707 Review comment updates and changing MenuItem href to Object

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
