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
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralLinkAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.policies.variants.text.TextEncoding;

/**
 * This class tests a concrete implementation of the
 * {@link com.volantis.mcs.protocols.menu.model.MenuItem} interface.  Since
 * it is part of a class hierarchy, the test case is also part of a (test)
 * hierarchy.
 */
public class ConcreteMenuItemTestCase
        extends AbstractMenuEntryTestAbstract {

    /**
     * Used for naming menu items in the test cases
     */
    private final OutputBuffer itemName = new DOMOutputBuffer();

    /**
     * This method tests the get, set and remove event handler methods
     */
    public void testEventHandler() {
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        EventType blur = EventType.ON_BLUR;
        EventType click = EventType.ON_CLICK;
        EventType mouse = EventType.ON_MOUSE_OVER;

        ScriptAssetReference handlerOne =
                new LiteralScriptAssetReference("Handle blurrred things");
        ScriptAssetReference handlerTwo =
                new LiteralScriptAssetReference("Handle clicked things");
        ScriptAssetReference handlerThree =
                new LiteralScriptAssetReference("Handle mouse over things");

        Object handler;

        // Test retrieval with nothing set
        handler = item.getEventHandler(blur);
        assertNull("Should be no handler", handler);

        // Test null setting
        try {
            item.getEventHandler(null);
            fail("Null event handler types should cause an exception");
        } catch (IllegalArgumentException iae) {
            // Test succeeded - the event handling threw an exception
        }

        // Test setting and getting
        item.setEventHandler(blur, handlerOne);
        item.setEventHandler(click, handlerTwo);
        handler = item.getEventHandler(blur);
        assertNotNull("Should have a handler", handler);
        assertEquals("Handlers should be the same", handler, handlerOne);

        handler = item.getEventHandler(mouse);
        assertNull("Should be no handler", handler);
        item.setEventHandler(mouse, handlerThree);
        handler = item.getEventHandler(mouse);
        assertNotNull("Should have a handler", handler);
        assertEquals("Handlers should be the same", handler, handlerThree);

        // Test removal
        item.removeEventHandler(click);
    }

    /**
     * Test the get and set title methods.
     */
    public void testTitle() {
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        // Test getting a null segment
        String testTitle = item.getTitle();
        assertNull("Should be no title", testTitle);

        // Test setting a segment
        String title = "title";
        item.setTitle(title);

        // Test getting a non-null segment
        testTitle = item.getTitle();
        assertNotNull("Should be a title", testTitle);
        assertEquals("Titles should be the same", title, testTitle);
    }

    /**
     * Test the get and set target methods
     */
    public void testTarget() {
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        // Test getting a null segment
        String testTarget = item.getSegment();
        assertNull("Should be no target", testTarget);

        // Test setting a segment
        String target = "target";
        item.setSegment(target);

        // Test getting a non-null segment
        testTarget = item.getSegment();
        assertNotNull("Should be a target", testTarget);
        assertEquals("Targets should be the same", target, testTarget);
    }

    /**
     * Test the get and set segment methods
     */
    public void testSegment() {
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        // Test getting a null segment
        String testSegment = item.getSegment();
        assertNull("Should be no segment", testSegment);

        // Test setting a segment
        String segment = "segment";
        item.setSegment(segment);

        // Test getting a non-null segment
        testSegment = item.getSegment();
        assertNotNull("Should be a segment", testSegment);
        assertEquals("Segments should be the same", segment, testSegment);
    }

    /**
     * Test the get and set shortcut methods
     */
    public void testShortcut() {
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        // Test getting a null prompt
        TextAssetReference testShortcut = item.getShortcut();
        assertNull("Should be no prompt", testShortcut);

        // Test setting a prompt
        String shortcut = "Shortcut Object";
        item.setShortcut(new LiteralTextAssetReference(shortcut));

        // Test getting a non-null prompt
        testShortcut = item.getShortcut();
        assertNotNull("Should be a prompt", testShortcut);
        assertEquals("Prompts should be the same", shortcut,
                testShortcut.getText(TextEncoding.PLAIN));
    }

    /**
     * Test the getMenu method
     */
    public void testGetMenu() {
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        ConcreteMenu menu = MenuModelHelper.createMenu(false, null);

        Menu testMenu;

        // Testing no menu;
        testMenu = item.getMenu();
        assertNull("There should be no menu", testMenu);

        // Testing direct containership
        item.setContainer(menu);
        testMenu = item.getMenu();
        assertNotNull("There should be a menu", testMenu);
        assertEquals("The menus should be the same", menu, testMenu);

        // Testing no valid container
        ConcreteMenuItemGroup group =
                MenuModelHelper.createMenuItemGroup(false, null);
        try {
            item.setContainer(group);
            fail("Setting the container with no valid menu should fail");
        } catch (IllegalStateException iae) {
            // Success - the exception got thrown as it should have :-)
        }

        // Ensuring goes through hierarchy to get the menu
        group.setContainer(testMenu);
        item.setContainer(group);
        testMenu = item.getMenu();
        assertNotNull("There should be a menu", testMenu);
        assertEquals("The menus should be the same", menu, testMenu);
    }

    /**
     * Test the get and set lable methods.
     */
    public void testLabel() {
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        // Test getting a null label
        MenuLabel testLabel = item.getLabel();
        assertNotNull("Should be a default label", testLabel);

        // Test setting label
        MenuLabel label = MenuModelHelper.createMenuLabel(null);
        item.setLabel(label);

        // Test getting a non-null label
        testLabel = item.getLabel();
        assertNotNull("Should be a label", testLabel);
        assertEquals("Labels should be the same", label, testLabel);
    }

    /**
     * Test the get and set href methods
     */
    public void testHref() {
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        // Test getting a null href
        LinkAssetReference testHref = item.getHref();
        assertNull("Should be no prompt", testHref);

        // Test setting a href
        LinkAssetReference href = new LiteralLinkAssetReference(
                "relative/url/to/something");
        item.setHref(href);

        // Test getting a non-null href
        testHref = item.getHref();
        assertNotNull("Should be a prompt", testHref);
        assertEquals("Hrefs should be the same", href, testHref);
    }

    /**
     * Tests the get and set prompt methods
     */
    public void testPrompt() {
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        // Test getting a null prompt
        TextAssetReference testPrompt = item.getPrompt();
        assertNull("Should be no prompt", testPrompt);

        // Test setting a prompt
        String prompt = "Prompt Object";
        item.setPrompt(new LiteralTextAssetReference(prompt));

        // Test getting a non-null prompt
        testPrompt = item.getPrompt();
        assertNotNull("Should be a prompt", testPrompt);
        assertEquals("Prompts should be the same", prompt,
                testPrompt.getText(TextEncoding.PLAIN));
    }

    /**
     * Tests the is sub menu item (or not) functionality
     */
    public void testIsSubMenuItem(){
        ConcreteMenuItem item = MenuModelHelper.createMenuItem(itemName);

        // Test getting true
        boolean isSubMenu = item.isSubMenuItem();
        assertTrue("Should be a menu representation", isSubMenu);

        // Set a href value as this is part of the submenu check
        item.setHref(new LiteralLinkAssetReference("/href/to/something"));

        // Test getting false
        isSubMenu = item.isSubMenuItem();
        assertFalse("Should be a normal menu item", isSubMenu);
    }

    // JavaDoc inherited
    protected AbstractModelElement createTestInstance(
            ElementDetails elementDetails) {
        
        return new ConcreteMenuItem(elementDetails,
                                    MenuModelHelper.createMenuLabel(itemName));
    }

    // JavaDoc inherited
    public void testGetContainer() {
        AbstractMenuEntry testClass = (AbstractMenuEntry) createTestInstance();

        // Test null values
        MenuEntry parentObject = null;
        try {
            testClass.setContainer(parentObject);
            fail("The set should throw an exception as no menu parent");
        } catch (IllegalStateException ise) {
            // Test passed as the exception was thrown as expected
        }

        // Test non-null values;
        parentObject = MenuModelHelper.createMenu(false, null);
        testClass.setContainer(parentObject);
        MenuEntry testObject = testClass.getContainer();

        assertNotNull("The test object should not be null", testObject);
        assertEquals("The parent and test objects should be the same",
                parentObject, testObject);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 20-Apr-04	3945/1	claire	VBM:2004042006 Providing menu as menu item functionality

 02-Apr-04	3429/1	philws	VBM:2004031502 MenuLabelElement implementation

 15-Mar-04	3342/1	philws	VBM:2004022707 Review comment updates and changing MenuItem href to Object

 11-Mar-04	3306/7	claire	VBM:2004022706 Removed uncessary specialisation equals and hashcode methods

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
