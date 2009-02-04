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
package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralImageAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuText;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteElementDetails;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuIcon;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuItem;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuLabel;
import com.volantis.mcs.protocols.menu.shared.model.ConcreteMenuText;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBufferLocator;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;


/**
 * This is a utility class for creating various menu elements for use when
 * testing renderers.  It contains various overloaded methods so that the test
 * cases can pick and choose which values they choose to take control over and
 * pass in.
 * <p>
 * There are already some test methods that exist in
 * {@link com.volantis.mcs.protocols.menu.shared.model.MenuModelHelper} but
 * these hide more of the initialisation away.  This class is intended to
 * provide a more flexible approach to creating the necessary menu elements.
 * </p>
 */
public final class MenuEntityCreation {

    /**
     * Test string used when creating style elements
     */
    public final String ELEMENT = "Element";

    /**
     * Test string used when creating image references
     */
    public final String IMAGE = "Image";

    /**
     * An instance of a menu buffer locator.  Used in the test creation, but
     * unless forced, this is not overwritten each time and the previously
     * created instance is returned.
     */
    private MenuBufferLocator bufferLocator;


    /**
     */
    public MenuEntityCreation() {
    }

    // ========================================================================
    //   Buffer methods
    // ========================================================================

    /**
     * Creates a buffer locator if necessary and returns it.
     *
     * @return A buffer locator
     */
    public MenuBufferLocator createTestMenuBuffer() {
        if (bufferLocator == null) {
            bufferLocator = new TestLocator(new TestBuffer());
        }
        return bufferLocator;
    }

    /**
     * Create a buffer locator and return it.  If an existing locator exists,
     * and forceNew is false then the existing one will be returned.  Setting
     * forceNew to true will always create a new locator.  If there is not an
     * existing locator a new one will be created regardles of the value of
     * forceNew
     *
     * @param forceNew True if a new locator should be created, false otherwise.
     *
     * @return A buffer locator
     */
    public MenuBufferLocator createTestMenuBuffer(boolean forceNew) {
        if (forceNew) {
            bufferLocator = null;
        }
        return createTestMenuBuffer();
    }

    // ========================================================================
    //   Menu Item methods
    // ========================================================================

    /**
     * Create a test menu item object using default values.

     * @return A menu item
     */
    public MenuItem createTestMenuItem() {
        return createTestMenuItem(createTestElementDetails());
    }

    /**
     * Create a test menu item object using the style iformation provided and
     * default values otherwise.
     *
     * @param elementDetails The information about the element corresponding
     * to the menu item
     * @return A menu item
     */
    public MenuItem createTestMenuItem(ElementDetails elementDetails) {
        return createTestMenuItem(elementDetails, createTestMenuLabel());
    }

    /**
     * Create a test menu item object using the menu label provided and default
     * values otherwise.
     *
     * @param label The label to use to represent the menu item
     * @return A menu item
     */
    public MenuItem createTestMenuItem(MenuLabel label) {
        return createTestMenuItem(createTestElementDetails(), label);
    }

    /**
     * Create a test menu item object using the style and menu label provided.
     *
     * @param elementDetails The information about the element corresponding
     * to the menu item
     * @param label The label to use to represent the menu item
     * @return A menu item
     */
    public MenuItem createTestMenuItem(ElementDetails elementDetails, MenuLabel label) {
        return new ConcreteMenuItem(elementDetails, label);
    }

    // ========================================================================
    //   Menu Label methods
    // ========================================================================

    /**
     * Create a test menu label object using default values.
     *
     * @return A menu label
     */
    public MenuLabel createTestMenuLabel() {
        return createTestMenuLabel(createTestElementDetails());
    }

    /**
     * Create a test menu label object using the provided element details and
     * default values for all others that are required.
     *
     * @param elementDetails The information about the element corresponding
     * to the menu label
     * @return A menu label
     */
    public MenuLabel createTestMenuLabel(ElementDetails elementDetails) {
        return createTestMenuLabel(elementDetails, createTestMenuText());
    }

    /**
     * Create a test menu label object using the provided menu text.  The other
     * values required are default values.
     *
     * @param text  The text component of the label
     * @return A menu label
     */
    public MenuLabel createTestMenuLabel(MenuText text) {
        return createTestMenuLabel(createTestElementDetails(), text,
                                   createTestMenuIcon());
    }

    /**
     * Create a test menu label object using the element details and menu text
     * provided. The other necessary values are provided from default values.
     *
     * @param elementDetails The information about the element corresponding
     * to the menu label
     * @param text  The text component of the label
     * @return A menu label
     */
    public MenuLabel createTestMenuLabel(ElementDetails elementDetails,
                                         MenuText text) {
        return createTestMenuLabel(elementDetails, text, createTestMenuIcon());
    }

    /**
     * Create a test menu label object using the provided icon.  The other
     * required values are provided from default values.
     *
     * @param icon  The icon component of the label, if null, is not added.
     * @return A menu label
     */
    public MenuLabel createTestMenuLabel(MenuIcon icon) {
        return createTestMenuLabel(createTestElementDetails(),
                                   createTestMenuText(), icon);
    }

    /**
     * Create a test menu label object using the element details and menu icon
     * provided. The other necessary values are provided from default values.
     *
     * @param elementDetails The element details to use for the label
     * @param icon  The icon component of the label, if null, is not added.
     * @return A menu label
     */
    public MenuLabel createTestMenuLabel(ElementDetails elementDetails,
                                         MenuIcon icon) {
        return createTestMenuLabel(elementDetails, createTestMenuText(), icon);
    }

    /**
     * Create a test menu label object using the menu icon and menu text
     * provided.  The other necessary values are provided from default values.
     *
     * @param text  The text component of the label
     * @param icon  The icon component of the label, if null, is not added.
     * @return A menu label
     */
    public MenuLabel createTestMenuLabel(MenuText text, MenuIcon icon) {
        return createTestMenuLabel(createTestElementDetails(), text, icon);
    }

    /**
     * Create a test menu label object.  This is created and initialised using
     * the various values provided as parameters.
     *
     * @param elementDetails The element details to use for the label
     * @param text  The text component of the label
     * @param icon  The icon component of the label, if null, is not added.
     * @return A menu label
     */
    public MenuLabel createTestMenuLabel(ElementDetails elementDetails,
                                         MenuText text, MenuIcon icon) {
        ConcreteMenuLabel label = new ConcreteMenuLabel(elementDetails, text);
        if (icon != null) {
            label.setIcon(icon);
        }
        return label;
    }

    // ========================================================================
    //   Menu Icon methods
    // ========================================================================

    /**
     * Create a test menu icon object using default values.
     *
     * @return A menu icon
     */
    public MenuIcon createTestMenuIcon() {
        return createTestMenuIcon(createTestElementDetails());
    }

    /**
     * Create a test menu icon object using the provided elementDetails, and
     * defaults for the other necessary values.
     *
     * @param elementDetails The elementDetails to use for the icon
     * @return A menu icon
     */
    public MenuIcon createTestMenuIcon(ElementDetails elementDetails) {
        return createTestMenuIcon(elementDetails,
                createImageAssetReference(), createImageAssetReference());
    }

    /**
     * Create a test menu icon object using the provided image asset reference
     * for either the normal or the over url.  The other value, and the element
     * details are provided by default values.
     *
     * @param url      The asset reference that points to the image to be set
     * @param isNormal True if the url points to a normal image, false for an
     *                 over image
     * @return A menu icon
     */
    public MenuIcon createTestMenuIcon(ImageAssetReference url,
                                       boolean isNormal) {
        MenuIcon icon;
        if (isNormal) {
            icon = createTestMenuIcon(createTestElementDetails(), url,
                                      createImageAssetReference());
        } else {
            icon = createTestMenuIcon(createTestElementDetails(),
                                      createImageAssetReference(), url);
        }

        return icon;
    }

    /**
     * Create a test menu icon object using the provided image references
     * and using default values for the element details.
     *
     * @param normalURL The asset reference that points to the normal image
     * @param overURL The asset reference that points to the over image
     * @return A menu icon
     */
    public MenuIcon createTestMenuIcon(ImageAssetReference normalURL,
                                       ImageAssetReference overURL) {
        return createTestMenuIcon(createTestElementDetails(), normalURL,
                overURL);
    }

    /**
     * Create a test menu icon object using the provided parameters to
     * initialise it.
     *
     * @param elementDetails The elementDetails to use for the icon
     * @param normalURL The asset reference that points to the normal image
     * @param overURL The asset reference that points to the over image
     * @return A menu icon
     */
    public MenuIcon createTestMenuIcon(ElementDetails elementDetails,
                                       ImageAssetReference normalURL,
                                       ImageAssetReference overURL) {
        ConcreteMenuIcon icon = new ConcreteMenuIcon(elementDetails);
        icon.setNormalURL(normalURL);
        icon.setOverURL(overURL);
        return icon;
    }

    // ========================================================================
    //   IamegAssetReference methods
    // ========================================================================

    /**
     * Create a test image asset reference using default values.
     *
     * @return An image asset reference
     */
    public ImageAssetReference createImageAssetReference() {
        return new LiteralImageAssetReference(IMAGE);
    }

    /**
     * Create a test image asset reference using the name provided as the
     * name of the component identity inside of the reference.  All other
     * values are defaults.
     *
     * @param name The name of the image to create the reference around
     * @return An image asset reference
     */
    public ImageAssetReference createImageAssetReference(String name) {
        return new LiteralImageAssetReference(name);
    }

    // ========================================================================
    //   Menu Text methods
    // ========================================================================

    /**
     * Create a test menu text object using default values.
     *
     * @return A menu text
     */
    public MenuText createTestMenuText() {
        return createTestMenuText(createTestElementDetails());
    }

    /**
     * Create a test menu text object using the provided element details and
     * defaults for all other necessary values.
     *
     * @param elementDetails The elementDetails to use for the text
     * @return A menu text
     */
    public MenuText createTestMenuText(ElementDetails elementDetails) {
        return createTestMenuText(elementDetails, new TestDOMOutputBuffer());
    }

    /**
     * Create a test menu object using the provided output buffer and defaults
     * for all other necessary values.
     *
     * @param buffer The buffer to use as the textual content
     * @return A menu text
     */
    public MenuText createTestMenuText(OutputBuffer buffer) {
        return createTestMenuText(createTestElementDetails(), buffer);
    }

    /**
     * Create a test menu text object using the element details and the output
     * buffer provided.  Both are used in the creation and initialisation
     * of the object returned.
     *
     * @param elementDetails  The elementDetails to use for the text
     * @param buffer The buffer to use as the textual content
     * @return A menu text
     */
    public MenuText createTestMenuText(ElementDetails elementDetails,
                                       OutputBuffer buffer) {
        ConcreteMenuText text = new ConcreteMenuText(elementDetails);
        text.setText(buffer);
        return text;
    }

    // ========================================================================
    //   ElementDetails methods
    // ========================================================================

    /**
     * Create a test info object using default values.
     *
     * @return An ElementDetails
     */
    public ElementDetails createTestElementDetails() {
        return createTestElementDetails(ELEMENT, StylesBuilder.getEmptyStyles());
    }

    /**
     * Create a test object using the element name provided.
     *
     * @param element The name of the element to set in the new element details.
     * @param styles The styles which are applicable to this new element details
     * @return An ElementDetails
     */
    public ElementDetails createTestElementDetails(String element,
                                                   Styles styles) {
        ConcreteElementDetails elementDetails = new ConcreteElementDetails();
        elementDetails.setElementName(element);
        elementDetails.setStyles(styles);

        return elementDetails;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 22-Apr-04	4004/1	claire	VBM:2004042204 Implemented remaining required WML renderers

 21-Apr-04	3681/1	philws	VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

 16-Apr-04	3715/1	claire	VBM:2004040201 Enhanced Menu: WML Menu Item Renderers

 ===========================================================================
*/
