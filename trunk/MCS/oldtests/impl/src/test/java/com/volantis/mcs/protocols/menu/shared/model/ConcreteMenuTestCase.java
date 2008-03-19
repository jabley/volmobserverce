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
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuLabel;

/**
 * This class tests a concrete implementation of the
 * {@link com.volantis.mcs.protocols.menu.model.Menu} interface.  Since
 * it is part of a class hierarchy, the test case is also part of a (test)
 * hierarchy.
 */
public class ConcreteMenuTestCase extends AbstractMenuEntryTestAbstract {

    /**
     * A string used throughout the tests to name a menu
     */
    private final String testTitle = "Menu Title";

    /**
      * This method tests the getSize() method in the MenuItemGroup interface
      * as well as the add() and method in ConcreteMenuItemGroup.
      */
     public void testGetSize() {
         ConcreteMenu menu = MenuModelHelper.createMenu(false, testTitle);

         AbstractMenuEntry[] entries = MenuModelHelper.createEntryArray();

         // Test no items
         assertEquals("Should be no items in the list", 0, menu.getSize());

         // Test one item

         menu.add(entries[0]);
         assertEquals("Should be one item in the list", 1, menu.getSize());

         // Test multipleItems
         menu.add(entries[1]);
         assertEquals("Should be many items in the list",
                 2, menu.getSize());
         menu.add(entries[2]);
         assertEquals("Should be many items in the list",
                 3, menu.getSize());
     }

     /**
      * This method tests the get() method
      */
     public void testGet() {
         ConcreteMenu itemGroup = MenuModelHelper.createMenu(true, testTitle);

         int maxItems = itemGroup.getSize();

         // Test get within bounds
         MenuEntry item = itemGroup.get(1);
         assertNotNull("Menu item should not be null", item);

         // Test get out of bounds
         try {
             item = itemGroup.get(maxItems + 1);
             fail("get() call should have thrown an exception");
         } catch (IndexOutOfBoundsException iob) {
             // Test success - this exception should have been thrown
         }
     }

    /**
     * This method tests the get and set help methods
     */
    public void testHelp() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, testTitle);

        // Test getting a null help
        TextAssetReference testHelp = item.getHelp();
        assertNull("Should be no help", testHelp);

        // Test setting help
        TextAssetReference help = new LiteralTextAssetReference("Help Object");
        item.setHelp(help);

        // Test getting a non-null help
        testHelp = item.getHelp();
        assertNotNull("Should be a help", testHelp);
        assertEquals("Helps should be the same", help, testHelp);
    }

    /**
     * This method tests the get and set error methods
     */
    public void testErrorMessage() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, testTitle);

        // Test getting a null error
        TextAssetReference testError = item.getErrorMessage();
        assertNull("Should be no error message", testError);

        // Test setting error
        TextAssetReference error = new LiteralTextAssetReference(
                "Error Message Object");
        item.setErrorMessage(error);

        // Test getting a non-null error
        testError = item.getErrorMessage();
        assertNotNull("Should be an error message", testError);
        assertEquals("Error messages should be the same", error, testError);
    }

    /**
     * This method tests the get and set label methods
     */
    public void testLabel() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, testTitle);

        // Test getting a null label
        MenuLabel testLabel = item.getLabel();
        assertNull("Should be no label", testLabel);

        // Test setting label
        MenuLabel label = MenuModelHelper.createMenuLabel(null);
        item.setLabel(label);

        // Test getting a non-null label
        testLabel = item.getLabel();
        assertNotNull("Should be a label", testLabel);
        assertEquals("Labels should be the same", label, testLabel);
    }

    /**
     * This method tests the get, set and remove event handler methods
     */
    public void testEventHandler() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, testTitle);

        EventType blur = EventType.ON_BLUR;
        EventType click = EventType.ON_CLICK;
        EventType mouse = EventType.ON_MOUSE_OVER;
        EventType focus = EventType.ON_FOCUS;

        ScriptAssetReference handlerOne =
                new LiteralScriptAssetReference("Handle blurrred things");
        ScriptAssetReference handlerTwo =
                new LiteralScriptAssetReference("Handle clicked things");
        ScriptAssetReference handlerThree =
                new LiteralScriptAssetReference("Handle mouse over things");
        ScriptAssetReference handlerFour =
                new LiteralScriptAssetReference("Handle focus things");

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
        try {
            item.setEventHandler(blur, handlerOne);
            fail("Not allowed to use blur events on menus");
        } catch (IllegalArgumentException iae) {
            // Test succeeded - unable to use blurs on menus
        }
        try {
            item.setEventHandler(focus, handlerFour);
            fail("Not allowed to use focus events on menus");
        } catch (IllegalArgumentException iae) {
            // Test succeeded - unable to use focus on menus
        }
        item.setEventHandler(click, handlerTwo);
        handler = item.getEventHandler(click);
        assertNotNull("Should have a handler", handler);
        assertEquals("Handlers should be the same", handler, handlerTwo);

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
        ConcreteMenu item = MenuModelHelper.createMenu(false, null);

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
     * Tests the get and set prompt methods
     */
    public void testPrompt() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, null);

        // Test getting a null prompt
        TextAssetReference testPrompt = item.getPrompt();
        assertNull("Should be no prompt", testPrompt);

        // Test setting a prompt
        TextAssetReference prompt = new LiteralTextAssetReference(
                "Prompt Object");
        item.setPrompt(prompt);

        // Test getting a non-null prompt
        testPrompt = item.getPrompt();
        assertNotNull("Should be a prompt", testPrompt);
        assertEquals("Prompts should be the same", prompt, testPrompt);
    }

    /**
     * Tests the retrieval of a menu as a menu item for displaying
     */
    public void testGetAsMenuItem() {
        // Setup a label
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.appendEncoded("test");
        ConcreteMenuLabel menuLabel = MenuModelHelper.createMenuLabel(buffer);
        // Setup the menu title
        String title = "test menu";

        // Create a test menu
        ConcreteMenu menu = MenuModelHelper.createMenu(false, title);
        menu.setLabel(menuLabel);

        // Test getting a null menu item
        MenuItem response = menu.getAsMenuItem();
        assertNull("The menu should be top level", response);

        // Set a parent container to force a submenu
        menu.setContainer((MenuEntry)createTestInstance(
                MenuModelHelper.createElementDetails()));

        // Test getting a valid menu item
        response = menu.getAsMenuItem();
        assertNotNull("The menu should be a sub menu", response);

        // Check the labels are the same
        assertSame("Labels should be the same", menuLabel, response.getLabel());

        // Check the title
        assertEquals("Titles should match", title, response.getTitle());
    }

    // JavaDoc inherited
    protected AbstractModelElement createTestInstance(
            ElementDetails elementDetails) {
        return new ConcreteMenu(elementDetails);
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

 20-Apr-04	3945/1	claire	VBM:2004042006 Providing menu as menu item functionality

 11-Mar-04	3306/7	claire	VBM:2004022706 Removed uncessary specialisation equals and hashcode methods

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
