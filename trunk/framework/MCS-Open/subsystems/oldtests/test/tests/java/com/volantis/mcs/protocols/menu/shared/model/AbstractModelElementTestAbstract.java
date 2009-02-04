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

import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This is a unit test for the AbstractModelElement class which forms part of
 * the menu model.  It is abstract because it tests an abstract class.
 * Anything that extends {@link AbstractModelElement} should unit test by
 * extending this test class.
 */
public abstract class AbstractModelElementTestAbstract
        extends TestCaseAbstract {

    /**
     * Used for various element name set ups.
     */
    protected final String ELEMENT = "Element";

    /**
     * Used to create new ElementDetails instances for use in tests.
     */
    protected final Styles TEST_STYLES = StylesBuilder.getEmptyStyles();

    /**
     * Create a new instance of this test case.
     */
    public AbstractModelElementTestAbstract() {
    }

    /**
     * This method tests the getElementDetails() method of the
     * AbstractModelElement class.  It makes use of a simple test class
     * provided by {@link #createTestInstance createMutableStyleProperties()} to create
     * a concrete instance for testing purposes.
     */
    public void testGetElementDetails() throws Exception {
        ElementDetails elementDetails =
                createElementDetails(ELEMENT, TEST_STYLES);
        AbstractModelElement testClass = createTestInstance(elementDetails);

        ElementDetails testElementDetails = testClass.getElementDetails();
        assertNotNull("element details should not be null",
                testElementDetails);
        assertEquals("element details should be the same",
                elementDetails, testElementDetails);
    }

// Commented out until we resolve VBM:2004040703.
//    /**
//     * Testing the equality method.
//     */
//    public void testEquals() {
//        AbstractModelElement testClassOne = createMutableStyleProperties(ELEMENT);
//        AbstractModelElement testClassTwo = createMutableStyleProperties(ELEMENT);
//        AbstractModelElement testClassThree = createMutableStyleProperties(ELEMENT);
//        AbstractModelElement testClassFour =
//                createMutableStyleProperties("Different " + ELEMENT);
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
//        AbstractModelElement testClassOne = createMutableStyleProperties(ELEMENT);
//        AbstractModelElement testClassTwo = createMutableStyleProperties(ELEMENT);
//        AbstractModelElement testClassThree = createMutableStyleProperties(ELEMENT);
//        AbstractModelElement testClassFour =
//                createMutableStyleProperties("Different " + ELEMENT);
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
     * A utility method to create an instance of AbstractModelElement with the
     * default element name and Styles.
     *
     * @return            An initialised instance of a subclass of
     *                    AbstractModelElement
     */
    protected AbstractModelElement createTestInstance() {
        return createTestInstance(ELEMENT, TEST_STYLES);
    }


    /**
     * A utility method to create an instance of AbstractModelElement with the
     * specified element name and styles.
     *
     * @param elementName   The element name to use
     * @param styles        The Styles which are applicable to the PAPI element
     *                      that this ModelElement represents.
     * @return              An initialised instance of a subclass of
     *                      AbstractModelElement
     */
    protected AbstractModelElement createTestInstance(String elementName,
                                                      Styles styles) {
        return createTestInstance(
                createElementDetails(elementName, TEST_STYLES));
    }

    /**
     * Provides a valid concrete implementation of {@link AbstractModelElement}
     * for testing purposes.  It is initialised with the provided element
     * details.
     *
     * @param elementDetails    The elementDetails to initialise the new
     *                          ModelElement instance with.
     * @return      An initialised instance of some class extending the
     *              abstract class
     */
    protected abstract AbstractModelElement createTestInstance(
            ElementDetails elementDetails);

    /**
     * A utility method to create an instance of ElementDetails with the
     * specified element name and Styles. An ElementDetails must have non null
     * Styles to be valid.
     *
     * @param element   The element name to use
     * @param styles    The Styles which are applicable to the PAPI element
     *                  that this ElementDetails represents.
     * @return          An initialised implementation instance of ElementDetails
     */
    protected ElementDetails createElementDetails(String element,
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

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 27-Jun-05	8888/2	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 11-Mar-04	3306/6	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/4	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
