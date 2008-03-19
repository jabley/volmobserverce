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
package com.volantis.mcs.themes;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Iterator;

/**
 * This class tests {@link MutableStyleProperties}.  It extends the a abstract
 * test case to mirror the hierarchy of classes.
 */
public abstract class MutableStylePropertiesTestAbstract
        extends TestCaseAbstract {

    private StylePropertyDefinitions definitions =
            StylePropertyDetails.getDefinitions();
    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();
    private StyleValue styleValue1 =
        STYLE_VALUE_FACTORY.getColorByRGB(null, 0xffeedd);
    private StyleValue styleValue2 =
        STYLE_VALUE_FACTORY.getString(null, "Hello");
    private StyleValue styleValue3 =
        STYLE_VALUE_FACTORY.getIdentifier(null, "Identifier");
    private StyleValue styleValue4 = STYLE_VALUE_FACTORY.getInteger(null, 5);
    private StyleProperty property1 = StylePropertyDetails.COLOR;
    private StyleProperty property2 = StylePropertyDetails.PADDING_BOTTOM;
    private StyleProperty property3 = StylePropertyDetails.BACKGROUND_ATTACHMENT;
    private StyleProperty property4 = StylePropertyDetails.MCS_SYSTEM_FONT;

    /**
     * This tests creating {@link MutableStyleProperties} objtects with both
     * valid and invalid parameters.
     */
    public void testCreation() {
        MutableStyleProperties properties = null;
        try {
            properties = createMutableStyleProperties(null);
            fail(
                    "Should get exception - creating mutable properties with null");
        } catch (IllegalArgumentException iae) {
            // Test succeeded if reached here :-)
        }

        properties = (MutableStyleProperties) createMutableStyleProperties();
        assertNotNull("Properties should exist", properties);
    }

    /**
     * This tests the get and set style methods.
     */
    public void testGetAndSetStyle() {
        MutableStyleProperties properties =
                (MutableStyleProperties) createMutableStyleProperties();

        properties.setStyleValue(property1, styleValue1);
        StyleValue returned = properties.getStyleValue(property1);
        assertNotNull("Should have returned a value", returned);
        assertSame("Should retrieve same style as", styleValue1, returned);
        assertNotSame("Should not match", returned, styleValue2);

        returned = properties.getStyleValue(property2);
        assertNull("Should be no value", returned);

        properties.setStyleValue(property2, styleValue2);
        returned = properties.getStyleValue(property2);
        assertNotNull("Should have returned a value", returned);
        assertSame("Should retrieve same style as", styleValue2, returned);
        assertNotSame("Should not match", returned, styleValue3);

        properties.setStyleValue(property3, styleValue3);
        returned = properties.getStyleValue(property3);
        assertNotNull("Should have returned a value", returned);
        assertSame("Should retrieve same style as", styleValue3, returned);
        assertNotSame("Should not match", returned, styleValue1);

    }

    /**
     * This tests the mutable and immutable states of a given instance
     */
    public void testImmutability() {
        MutableStyleProperties properties =
                (MutableStyleProperties) createMutableStyleProperties();

        // Set up some basic values, and also retrieve them
        properties.setStyleValue(property1, styleValue1);
        StyleValue returned = properties.getStyleValue(property1);
        assertNotNull("Should have returned a value", returned);
        assertSame("Should retrieve same style as", styleValue1, returned);
        assertNotSame("Should not match", returned, styleValue2);

        returned = properties.getStyleValue(property2);
        assertNull("Should be no value", returned);

        properties.setStyleValue(property2, styleValue2);
        returned = properties.getStyleValue(property2);
        assertNotNull("Should have returned a value", returned);
        assertSame("Should retrieve same style as", styleValue2, returned);
        assertNotSame("Should not match", returned, styleValue1);
    }

    protected abstract MutableStyleProperties createMutableStyleProperties();

    protected abstract MutableStyleProperties createMutableStyleProperties(
            MutableStyleProperties existingStyles);

    /**
     * Test that an empty properties works as expected.
     */
    public void testEmpty() {
        MutableStyleProperties properties = createMutableStyleProperties();
        assertTrue("It is not empty", properties.isEmpty());
    }

    /**
     * Test that setting a value and then getting it works properly.
     */
    public void testSetAndGet() {
        MutableStyleProperties properties = createMutableStyleProperties();

        properties.setStyleValue(property1, styleValue1);
        assertTrue("It is empty", !properties.isEmpty());
        assertSame("Get", styleValue1, properties.getStyleValue(property1));
    }

    /**
     * Test that getting a value from an empty set returns null.
     */
    public void testGetEmpty() {
        MutableStyleProperties properties = createMutableStyleProperties();

        assertNull("No property value", properties.getStyleValue(property1));
    }

    /**
     * Test that clearing a value works.
     */
    public void testClear() {
        MutableStyleProperties properties = createMutableStyleProperties();

        properties.setStyleValue(property1, styleValue1);
        assertTrue("It is empty", !properties.isEmpty());
        assertSame("Get", styleValue1, properties.getStyleValue(property1));

        properties.clearPropertyValue(property1);
        assertTrue("It is not empty", properties.isEmpty());
        assertNull("Get", properties.getStyleValue(property1));
    }

    /**
     * Test that setting a number of values and properties works properly.
     */
    public void testSetAndGetMultiple() {
        MutableStyleProperties properties = createMutableStyleProperties();

        properties.setStyleValue(property1, styleValue1);
        properties.setStyleValue(property2, styleValue2);
        properties.setStyleValue(property3, styleValue3);
        properties.setStyleValue(property4, styleValue4);

        assertSame("Property 1 not expected", styleValue1,
                properties.getStyleValue(property1));
        assertSame("Property 2 not expected", styleValue2,
                properties.getStyleValue(property2));
        assertSame("Property 3 not expected", styleValue3,
                properties.getStyleValue(property3));
        assertSame("Property 4 not expected", styleValue4,
                properties.getStyleValue(property4));
    }

    /**
     * Test that setting a number of values and properties causes the
     * property array to expand properly when adding properties to the end
     * of the array.
     */
    public void testExpandWhenAddingToEnd() {
        MutableStyleProperties properties = createMutableStyleProperties();

        addPropertiesToEnd(properties, 20);

        checkSequentialProperties(properties, 20);
    }

    private void addPropertiesToEnd(
            MutableStyleProperties properties, int number) {

        for (int i = 0; i < number; i += 1) {
            StyleProperty property = definitions.getStyleProperty(i);
            StyleInteger integer = STYLE_VALUE_FACTORY.getInteger(null, i);

            properties.setStyleValue(property, integer);
        }
    }

    private static void checkSequentialProperty(
            MutableStyleProperties properties,
            StyleProperty property) {

        StyleInteger integer = (StyleInteger)
                properties.getStyleValue(property);

        int index = property.getIndex();
        assertNotNull("Property " + index + " should be set", integer);
        assertEquals("Property " + index, index, integer.getInteger());
    }

    private static void checkSequentialPropertyValue(
            PropertyValue propertyValue) {

        StyleProperty property = propertyValue.getProperty();
        StyleInteger integer = (StyleInteger) propertyValue.getValue();

        int index = property.getIndex();
        assertNotNull("Property " + index + " should be set", integer);
        assertEquals("Property " + index, index, integer.getInteger());
    }

    private void checkSequentialProperties(
            MutableStyleProperties properties, int number) {

        // Check that all the properties were set correctly.
        for (int i = 0; i < number; i += 1) {
            StyleProperty property = definitions.getStyleProperty(i);

            checkSequentialProperty(properties, property);
        }
    }

    /**
     * Test that setting a number of values and properties causes the
     * property array to expand properly when adding properties to the beginning
     * of the array.
     */
    public void testExpandWhenAddingToBeginning() {
        MutableStyleProperties properties = createMutableStyleProperties();

        addPropertiesToBeginning(properties, 20);

        checkSequentialProperties(properties, 20);
    }

    private void addPropertiesToBeginning(
            MutableStyleProperties properties, int number) {

        for (int i = number - 1; i >= 0; i -= 1) {
            StyleProperty property = definitions.getStyleProperty(i);
            StyleInteger integer = STYLE_VALUE_FACTORY.getInteger(null, i);

            properties.setStyleValue(property, integer);
        }
    }

    /**
     * Test that setting and clearing a large number of properties.
     */
    public void testSettingAndSettingToNull() {
        MutableStyleProperties properties = createMutableStyleProperties();

        addPropertiesToBeginning(properties, 20);

        // Clear them all
        for (int i = 0; i < 20; i += 1) {
            properties.setStyleValue(definitions.getStyleProperty(i), null);
        }

        assertTrue("It is not empty", properties.isEmpty());
    }

    /**
     * Test that setting and clearing a large number of properties.
     */
    public void testSettingAndClearing() {
        MutableStyleProperties properties = createMutableStyleProperties();

        addPropertiesToBeginning(properties, 20);

        // Clear them all
        for (int i = 0; i < 20; i += 1) {
            properties.clearPropertyValue(definitions.getStyleProperty(i));
        }

        assertTrue("It is not empty", properties.isEmpty());
    }

    /**
     * Test that setting a value that is already set works properly.
     */
    public void testSetExisting() {
        MutableStyleProperties properties = createMutableStyleProperties();

        properties.setStyleValue(property1, styleValue1);
        properties.setStyleValue(property1, styleValue2);

        assertSame("Property get", styleValue2,
                properties.getStyleValue(property1));

        // Clear the property.
        properties.clearPropertyValue(property1);

        // Should now be empty.
        assertTrue("It is not empty", properties.isEmpty());
    }

//    /**
//     * Test that iteration over the properties works.
//     */
//    public void testStylePropertyIterateContinue() {
//        MutableStyleProperties properties = createMutableStyleProperties();
//
//        addPropertiesToEnd(properties, 30);
//
//        StylePropertyCountingIteratee iteratee =
//                new StylePropertyCountingIteratee(properties);
//        IterationAction iterationAction = properties.iterateStyleProperties(iteratee);
//        assertEquals("Iteration action unexpected", IterationAction.CONTINUE,
//                iterationAction);
//        assertEquals("Iteration count did not match", 30, iteratee.getCount());
//    }

//    /**
//     * Test that iteration over the properties stops if the iteratee returns
//     * {@link IterationAction#BREAK}.
//     */
//    public void testStylePropertyIterateBreak() {
//        MutableStyleProperties properties = createMutableStyleProperties();
//
//        addPropertiesToEnd(properties, 30);
//
//        StylePropertyCountingIteratee iteratee =
//                new StylePropertyCountingIteratee(properties) {
//                    public IterationAction next(StyleProperty property) {
//                        if (count == 10) {
//                            return IterationAction.BREAK;
//                        } else {
//                            return super.next(property);
//                        }
//                    }
//                };
//        IterationAction iterationAction = properties.iterateStyleProperties(iteratee);
//        assertEquals("Iteration action unexpected", IterationAction.BREAK,
//                iterationAction);
//        assertEquals("Iteration count did not match", 10, iteratee.getCount());
//    }

//    /**
//     * Test that the iterator provides access to all the properties.
//     */
//    public void testStylePropertyIterator() {
//
//        MutableStyleProperties properties = createMutableStyleProperties();
//
//        addPropertiesToEnd(properties, 30);
//
//        Iterator iterator = properties.stylePropertyIterator();
//        int count = 0;
//        while (iterator.hasNext()) {
//            StyleProperty property = (StyleProperty) iterator.next();
//            checkSequentialProperty(properties, property);
//            count += 1;
//        }
//        assertEquals("Iteration count did not match", 30, count);
//    }


    /**
     * Test that iteration over the {@link PropertyValue} works.
     */
    public void testPropertyValueIterateContinue() {
        MutableStyleProperties properties = createMutableStyleProperties();

        addPropertiesToEnd(properties, 30);

        PropertyValueCountingIteratee iteratee =
                new PropertyValueCountingIteratee();
        IterationAction iterationAction = properties.iteratePropertyValues(iteratee);
        assertEquals("Iteration action unexpected", IterationAction.CONTINUE,
                iterationAction);
        assertEquals("Iteration count did not match", 30, iteratee.getCount());
    }

    /**
     * Test that iteration over the {@link PropertyValue} stops if the iteratee
     * returns {@link IterationAction#BREAK}.
     */
    public void testPropertyValueIterateBreak() {
        MutableStyleProperties properties = createMutableStyleProperties();

        addPropertiesToEnd(properties, 30);

        PropertyValueCountingIteratee iteratee =
                new PropertyValueCountingIteratee() {
                    public IterationAction next(PropertyValue propertyValue) {
                        if (count == 10) {
                            return IterationAction.BREAK;
                        } else {
                            return super.next(propertyValue);
                        }
                    }
                };
        IterationAction iterationAction = properties.iteratePropertyValues(iteratee);
        assertEquals("Iteration action unexpected", IterationAction.BREAK,
                iterationAction);
        assertEquals("Iteration count did not match", 10,
                iteratee.getCount());
    }

    /**
     * Test that the iterator provides access to all the properties.
     */
    public void testPropertyValueIterator() {

        MutableStyleProperties properties = createMutableStyleProperties();

        addPropertiesToEnd(properties, 30);

        Iterator iterator = properties.propertyValueIterator();
        int count = 0;
        while (iterator.hasNext()) {
            PropertyValue property = (PropertyValue) iterator.next();
            checkSequentialPropertyValue(property);
            count += 1;
        }
        assertEquals("Iteration count did not match", 30, count);
    }

    public static class StylePropertyCountingIteratee
            implements StylePropertyIteratee {

        private final MutableStyleProperties properties;

        protected int count;

        public StylePropertyCountingIteratee(
                MutableStyleProperties properties) {
            this.properties = properties;
        }

        public IterationAction next(StyleProperty property) {
            checkSequentialProperty(properties, property);

            count += 1;

            return IterationAction.CONTINUE;
        }

        public int getCount() {
            return count;
        }
    }

    public static class PropertyValueCountingIteratee
            implements PropertyValueIteratee {

        protected int count;

        public IterationAction next(PropertyValue propertyValue) {
            checkSequentialPropertyValue(propertyValue);

            count += 1;

            return IterationAction.CONTINUE;
        }

        public int getCount() {
            return count;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/6	pduffin	VBM:2005111405 Massive changes for performance

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Apr-04	4124/1	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 06-Apr-04	3641/3	claire	VBM:2004032602 Enhancements and updating testcase coverage

 30-Mar-04	3641/1	claire	VBM:2004032602 Using menu types and styles in PAPI

 ===========================================================================
*/
