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

import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 */
public class ConcreteElementDetailsTestCase extends TestCaseAbstract {

    /**
     * An element name used in the various testing methods
     */
    private final String testElementName = "Element name";

    /**
      * Create a new instance of this test case.
      */
     public ConcreteElementDetailsTestCase() {
         super();
     }

     /**
      * Create a new named instance of this test case.
      *
      * @param s The name of the test case
      */
     public ConcreteElementDetailsTestCase(String s) {
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
     * Tests the get and set style properties methods
     */
    public void testStylesMutators() {
        ConcreteElementDetails testElementDetails =
                new ConcreteElementDetails();
        testElementDetails.setElementName(testElementName);

        // Test null
        try {
            testElementDetails.getStyles();
            fail("The element details was created without styles, so " +
                    "#getStyles should have thrown an exception");
        } catch (IllegalStateException iae) {
            // Test succeeded - the call caused this exception
        }

        // Test illegal setting
        try {
            testElementDetails.setStyles(null);
            fail("ElementDetails#getStyles should throw an exception if the " +
                    "new value for styles is null");
        } catch (IllegalArgumentException iae) {
            // Test succeeded - the call caused this exception
        }

        // Test setting
        Styles testStyles = StylesBuilder.getEmptyStyles();
        testElementDetails.setStyles(testStyles);

        // Test getting
        Styles gotStyles = testElementDetails.getStyles();
        assertNotNull("Should not be null", gotStyles);
        assertEquals("Should be the same", testStyles, gotStyles);
        assertNotNull("Should not be null", gotStyles.getPropertyValues());
    }

    /**
     * Tests the get and set id methods
     */
    public void testId() {
        ConcreteElementDetails testClass = new ConcreteElementDetails();

        // Test null getting
        String id = testClass.getId();
        assertNull("Should be null", id);

        // Test setting
        String setString = "Sample value";
        testClass.setId(setString);

        // Test non-null getting
        id = testClass.getId();
        assertNotNull("Should not be null", id);
        assertEquals("Should be the same", setString, id);
    }

    /**
     * Tests the get and set element methods
     */
    public void testElement() {
        ConcreteElementDetails testClass = new ConcreteElementDetails();

        // Test no element name set
        try {
            testClass.getElementName();
        } catch (IllegalStateException iae) {
            // Test succeeded - the call caused this exception
        }

        // Test illegal setting
        try {
            testClass.setElementName(null);
        } catch (IllegalArgumentException iae) {
            // Test succeeded - the call caused this exception
        }

        // Test legal setting
        testClass.setElementName(testElementName);

        // Test valid getting
        String elementName = testClass.getElementName();
        assertNotNull("Element name should exist", elementName);
        assertEquals("Element names should match",
                elementName, testElementName);
    }

// Commented out until we resolve VBM:2004040703.
//    /**
//     * Testing the equality method.
//     */
//    public void testEquals() {
//        ConcreteElementDetails testClassOne =
//                MenuModelHelper.createElementDetails(testElementName);
//        ConcreteElementDetails testClassTwo =
//                MenuModelHelper.createElementDetails(testElementName);
//        ConcreteElementDetails testClassThree =
//                MenuModelHelper.createElementDetails(testElementName);
//        ConcreteElementDetails testClassFour =
//                MenuModelHelper.createElementDetails("Another " +testElementName);
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
//        ConcreteElementDetails testClassOne =
//                MenuModelHelper.createElementDetails(testElementName);
//        ConcreteElementDetails testClassTwo =
//                MenuModelHelper.createElementDetails(testElementName);
//        ConcreteElementDetails testClassThree =
//                MenuModelHelper.createElementDetails(testElementName);
//        ConcreteElementDetails testClassFour =
//                MenuModelHelper.createElementDetails("Another " +testElementName);
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
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 27-Jun-05	8888/3	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 23-Jun-05	8483/5	emma	VBM:2005052410 Modifications after review

 22-Jun-05	8483/3	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 10-Mar-04	3306/1	claire	VBM:2004022706 Implementation of the menu model

 ===========================================================================
*/
