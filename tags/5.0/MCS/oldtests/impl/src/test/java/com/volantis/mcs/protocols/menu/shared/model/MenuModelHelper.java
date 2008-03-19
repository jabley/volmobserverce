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
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.TestNormalImageAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.styling.StylesBuilder;

/**
 * A utility class that contains methods that are used across various test
 * classes relating to the menu model
 * @deprecated This class has been deprecated in favour of
 * {@link com.volantis.mcs.protocols.menu.shared.MenuEntityCreation} although
 * it may be that some extra methods need to be added to completely support the
 * functionality provided in this class.
 */
public class MenuModelHelper {

    private static OutputBuffer ONE = new DOMOutputBuffer();
    private static OutputBuffer TWO = new DOMOutputBuffer();
    private static OutputBuffer THREE = new DOMOutputBuffer();
    private static OutputBuffer FOUR = new DOMOutputBuffer();

    /**
     * Prevent instantiations of this class
     */
    private MenuModelHelper() {

    }

    /**
     * A utility method to create an instance of
     * {@link com.volantis.mcs.protocols.menu.model.ElementDetails}
     * for use in other methods and tests.
     *
     * @return A suitable instance of the interface
     */
    public static ConcreteElementDetails createElementDetails(
            String elementName) {
        ConcreteElementDetails elementDetails = new ConcreteElementDetails();
        elementDetails.setElementName(elementName);
        elementDetails.setStyles(StylesBuilder.getInitialValueStyles());

        return elementDetails;
    }

    /**
     * A utility method to create an instance of
     * {@link com.volantis.mcs.protocols.menu.model.ElementDetails}
     * for use in other methods and tests.
     *
     * @return A suitable instance of the interface
     */
    public static ElementDetails createElementDetails() {
        return createElementDetails("Test Style Element");
    }

    /**
     * A utility method that creates an instance of a menu item for use within
     * the various methods and tests within this class.
     *
     * @param itemName The name to use for the menu item
     * @return         An instance of the a menu item
     */
    public static ConcreteMenuItem createMenuItem(OutputBuffer itemName) {
        ConcreteMenuText text =
                new ConcreteMenuText(createElementDetails());
        text.setText(itemName);
        MenuLabel label = new ConcreteMenuLabel(createElementDetails(), text);
        return new ConcreteMenuItem(createElementDetails(), label);
    }

    /**
     * A utility method that creates a basic instance of a
     * ConcreteMenuItemGroup for testing purposes.  If specified, the instance
     * will also be initialised with basic items.
     *
     * @param includeItems True if the menu group should include items
     * @param menu         The menu to use for this group's parent.  If this
     *                     is null subsequently adding items to this group
     *                     may cause exceptions to be thrown.
     * @return             The newly created menu item group
     */
    public static ConcreteMenuItemGroup
            createMenuItemGroup(boolean includeItems, Menu menu) {
        ConcreteMenuItemGroup group =
                new ConcreteMenuItemGroup(createElementDetails());
        group.setContainer(menu);
        if (includeItems) {
            ConcreteMenuItem[] items = createItemArray();
            for (int i = 0; i < items.length; i++) {
                group.add(items[i]);
            }
        }
        return group;
    }

    /**
     * A utility method that creates a basic instance of a ConcreteMenu
     * for testing purposes.  If specified, the instance will also be
     * initialised with basic items.
     *
     * @param includeItems True if the menu should have sample entries included
     * @param title        If not null, will be set as the menu's title
     * @return             The newly created menu item group
     */
    public static ConcreteMenu createMenu(boolean includeItems, String title) {
        ConcreteMenu menu =
                new ConcreteMenu(createElementDetails());
        if (includeItems) {
            AbstractMenuEntry[] items = createEntryArray();
            for (int i = 0; i < items.length; i++) {
                menu.add(items[i]);
            }
        }
        if (title != null) {
            menu.setTitle(title);
        }
        return menu;
    }

    /**
     * A utility method to create an array of entries that can be contained
     * in a menu.  This is necessary for setting up some of the tests.
     *
     * @return An array of entries to be stored in a menu group
     */
    public static AbstractMenuEntry[] createEntryArray() {
        AbstractMenuEntry[] items = {createMenuItem(ONE),
                                     createMenuItemGroup(true,
                                             createMenu(false, null)),
                                     createMenuItem(TWO),
                                     createMenuItem(THREE),
                                     createMenu(false, "Menu Title"),
                                     createMenuItem(FOUR)};
        return items;
    }

    /**
     * A utility method to create an array of items that can be contained
     * in a menu group.  This is necessary for setting up some of the tests.
     *
     * @return An array of items to be stored in a menu group
     */
    public static ConcreteMenuItem[] createItemArray() {
        ConcreteMenuItem[] items = {createMenuItem(ONE),
                                    createMenuItem(TWO),
                                    createMenuItem(THREE),
                                    createMenuItem(FOUR)};
        return items;
    }


    /**
     * A utility method to create an instance of a ConcreteMenuLabel for
     * testing purposes.  Both icon and text attributes will be initialised.
     * If the default text is not required (null parameter) then the text to
     * use can be specified.
     *
     * @param textToUse The text to use for the label text, if null the default
     *                  will be used
     * @return          An initialised instance of ConcreteMenuLabel
     */
    public static ConcreteMenuLabel createMenuLabel(OutputBuffer textToUse) {
        String testIcon = "relative/URL/to/icon";

        ElementDetails elementDetails = MenuModelHelper.createElementDetails();
        ConcreteMenuText text = new ConcreteMenuText(elementDetails);
        ConcreteMenuIcon icon = new ConcreteMenuIcon(elementDetails);

        if (textToUse == null) {
            text.setText(ONE);
        } else {
            text.setText(textToUse);
        }
        ImageAssetReference reference = new TestNormalImageAssetReference(
                testIcon);
        icon.setNormalURL(reference);

        return new ConcreteMenuLabel(createElementDetails(), text, icon);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 10-May-04	4227/1	philws	VBM:2004050706 Unique Menu Buffer Locator per Menu

 20-Apr-04	3715/1	claire	VBM:2004040201 Improving WML menu item renderers

 02-Apr-04	3429/3	philws	VBM:2004031502 MenuLabelElement implementation

 26-Mar-04	3500/3	claire	VBM:2004031806 Initial implementation of abstract component image references

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 ===========================================================================
*/
