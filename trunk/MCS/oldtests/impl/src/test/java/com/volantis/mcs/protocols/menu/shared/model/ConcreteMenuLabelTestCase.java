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
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuText;

/**
 * This is a unit test for the ConcreteMenuLabel class which forms part of the
 * menu model.
 */
public class ConcreteMenuLabelTestCase extends AbstractModelElementTestAbstract {

    /**
     * Descriptive text used on the test labels
     */
    private final OutputBuffer testText = new DOMOutputBuffer();

    /**
     * Icon asset reference used with the test labels
     */
    private final ImageAssetReference testIcon =
            new TestNormalImageAssetReference("icon reference");

    /**
     * Tests the getText method.  Because an instance of this class should
     * never be created without the text being set this method also tests
     * invalid configurations of the class as well as the get/set methods.
     */
    public void testGetText() {
        ElementDetails elementDetails = MenuModelHelper.createElementDetails();
        ConcreteMenuText text = new ConcreteMenuText(elementDetails);
        ConcreteMenuIcon icon = new ConcreteMenuIcon(elementDetails);

        MenuLabel label;
        text.setText(testText);
        icon.setNormalURL(testIcon);

        // Test illegal configurations
        try {
            label = createTestInstance(MenuModelHelper.createElementDetails(),
                                       null,
                                       null);
            fail("The creation of a class should have caused an exception");
        } catch (IllegalArgumentException iae) {
            // Test passes - exception gets thrown :-)
        }

        try {
            label = createTestInstance(MenuModelHelper.createElementDetails(),
                                       null,
                                       icon);
            fail("The creation of a class should have caused an exception");
        } catch (IllegalArgumentException iae) {
            // Test passes - exception gets thrown :-)
        }

        // Test legal configuration
        text.setText(testText);
        label = createTestInstance(MenuModelHelper.createElementDetails(),
                                   text,
                                   icon);
        MenuText testMenuText = label.getText();
        assertNotNull("Menu text should not be null", testMenuText);
        assertEquals("The menu texts should be the same", text, testMenuText);

        // Test method
        OutputBuffer secondTestText = new DOMOutputBuffer();
        ConcreteMenuText secondText = new ConcreteMenuText(elementDetails);
        secondText.setText(secondTestText);

        ((ConcreteMenuLabel)label).setText(secondText);
        testMenuText = label.getText();
        assertNotNull("Menu text should not be null", testMenuText);
        assertEquals("The menu texts should be the same",
                secondText, testMenuText);
    }

    /**
     * Tests the getIcon method using various null configurations and als the
     * get/set method combinations.
     */
    public void testGetIcon() {
        MenuLabel label;

        // Test non-null configuration
        label = createTestInstance(null, true);
        MenuIcon testIcon = label.getIcon();
        assertNotNull("Icon should not be null", testIcon);

        // Test null configuration
        label = createTestInstance(null, false);
        testIcon = label.getIcon();
        assertNull("Icon should be null", testIcon);

        // Test method
        ElementDetails elementDetails = MenuModelHelper.createElementDetails();
        MenuIcon icon = new ConcreteMenuIcon(elementDetails);

        ((ConcreteMenuLabel)label).setIcon(icon);
        testIcon = label.getIcon();
        assertNotNull("Icon should not be null", testIcon);
        assertEquals("Icons should be the same", icon, testIcon);

        ImageAssetReference secondIconReference =
                new TestNormalImageAssetReference("another icon reference");
        ConcreteMenuIcon secondIcon = new ConcreteMenuIcon(elementDetails);
        secondIcon.setNormalURL(secondIconReference);

        ((ConcreteMenuLabel)label).setIcon(secondIcon);
        testIcon = label.getIcon();
        assertNotNull("Icon should not be null", testIcon);
        assertEquals("Icons should be the same", secondIcon, testIcon);
    }

// Commented out until we resolve VBM:2004040703.
//    /**
//     * Testing the equality method.
//     */
//    public void testEquals() {
//        ConcreteMenuLabel testClassOne = createMutableStyleProperties(null, true);
//        ConcreteMenuLabel testClassTwo = createMutableStyleProperties(null, true);
//        ConcreteMenuLabel testClassThree = createMutableStyleProperties(null, true);
//        ConcreteMenuLabel testClassFour = createMutableStyleProperties(
//            new DOMOutputBuffer(), true);
//
//        // Reflexive
//        assertEquals("Classes should be the same",
//                testClassOne, testClassOne);
//
//        // Symmetric
//        assertEquals("Classes should be the same",
//                testClassOne, testClassTwo);
//        assertEquals("Classes should be the same",
//                testClassTwo, testClassOne);
//
//        // Transitive
//        assertEquals("Classes should be the same",
//                testClassTwo, testClassThree);
//        assertEquals("Classes should be the same",
//                testClassOne, testClassThree);
//
//        // Null
//        assertTrue("Null is not equal", !testClassOne.equals(null));
//
//        // Inequality
//        assertTrue("Classes should not be the same",
//                !testClassOne.equals(testClassFour));
//    }
//
//    /**
//     * Testing the hash code generation.
//     */
//    public void testHashCode() {
//        ConcreteMenuLabel testClassOne = createMutableStyleProperties(null, true);
//        ConcreteMenuLabel testClassTwo = createMutableStyleProperties(null, true);
//        ConcreteMenuLabel testClassThree = createMutableStyleProperties(null, true);
//        ConcreteMenuLabel testClassFour = createMutableStyleProperties(
//            new DOMOutputBuffer(), true);
//
//        // Reflexive
//        assertEquals("Hash codes should be the same",
//                testClassOne.hashCode(), testClassOne.hashCode());
//
//        // Symmetric
//        assertEquals("Hash codes should be the same",
//                testClassOne.hashCode(), testClassTwo.hashCode());
//        assertEquals("Hash codes should be the same",
//                testClassTwo.hashCode(), testClassOne.hashCode());
//
//        // Transitive
//        assertEquals("Hash codes should be the same",
//                testClassTwo.hashCode(), testClassThree.hashCode());
//        assertEquals("Hash codes should be the same",
//                testClassOne.hashCode(), testClassThree.hashCode());
//
//        // Inequality
//        assertTrue("Hash codes should not be the same",
//                testClassOne.hashCode() != testClassFour.hashCode());
//    }

    /**
     * Creates an instance of a ConcreteMenuLabel for testing purposes.  Both
     * icon and text attributes will be initialised. If the default text is not
     * required (null parameter) then the text to use can be specified.
     *
     * @param textToUse The text to use for the label text, if null the default
     *                  will be used
     * @param withIcon  Indicates whether an icon should or should not be set
     * @return An initialised instance of ConcreteMenuLabel
     */
    protected ConcreteMenuLabel createTestInstance(OutputBuffer textToUse,
                                                   boolean withIcon) {
        ElementDetails elementDetails = MenuModelHelper.createElementDetails();
        ConcreteMenuText text = new ConcreteMenuText(elementDetails);
        ConcreteMenuIcon icon = (withIcon ?
            new ConcreteMenuIcon(elementDetails) :
            null);

        if (textToUse == null) {
            text.setText(testText);
        } else {
            text.setText(textToUse);
        }

        if (withIcon) {
            icon.setNormalURL(testIcon);
        }

        return createTestInstance(elementDetails, text, icon);
    }

    /**
     * Creates an instance of a ConcreteMenuLabel for testing purposes. The
     * given style info, text and icon are used in the constructor call as
     * needed.
     *
     * @param elementDetails    The details of the PAPI element which
     *                          corresponds to this label.
     * @param text              The text for the label.
     * @param icon              The icon for the label.
     * @return a new instance initialized as required
     */
    protected ConcreteMenuLabel createTestInstance(ElementDetails elementDetails,
                                                   ConcreteMenuText text,
                                                   ConcreteMenuIcon icon) {
        if (icon == null) {
            return new ConcreteMenuLabel(elementDetails, text);
        } else {
            return new ConcreteMenuLabel(elementDetails, text, icon);
        }
    }

    // javadoc inherited
    protected AbstractModelElement createTestInstance(
            ElementDetails elementDetails) {

        return new ConcreteMenuLabel(elementDetails,
                new ConcreteMenuText(new ConcreteElementDetails()));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 02-Apr-04	3429/3	philws	VBM:2004031502 MenuLabelElement implementation

 26-Mar-04	3500/2	claire	VBM:2004031806 Initial implementation of abstract component image references

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 11-Mar-04	3306/3	claire	VBM:2004022706 Removed uncessary specialisation equals and hashcode methods

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 ===========================================================================
*/
