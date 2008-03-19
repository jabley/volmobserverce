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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.impl;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junitx.util.PrivateAccessor;

import java.util.List;

/**
 * Helper method for {@link com.volantis.shared.metadata.MetaDataObject} test cases.
 */
public abstract class MetaDataTestCaseHelper extends TestCaseAbstract {

    /**
     * This method ensures that when ListValueImpl#getContentsAsMutableList() is called
     * then the list returned is a
     * {@link com.volantis.shared.metadata.impl.ImmutableGeneratingTypedList} and has been set
     * to accept only {@link com.volantis.shared.metadata.value.MetaDataValue} objects.
     *
     * <p>There is a separate test case for
     * {@link com.volantis.shared.metadata.impl.ImmutableGeneratingTypedList} which
     * ensures that the list is generates immutable objects on the fly as items are added
     * / inserted to it and will only accept items of the specified class.</p>
     *
     */
    public static void ensureMutableListIsCorrect(List list,
                                                  Class expectedAllowableClass)
            throws Throwable {
        // Test that the implementation uses the correct type of list.
        Class expectedListClass = ImmutableGeneratingTypedList.class;
        Class returnedClass = list.getClass();
        assertEquals("getContentsAsMutableList should return a " + expectedListClass
                + " but was " + returnedClass,
                expectedListClass, returnedClass);

        // Check that the list has been initalized with the correct type of allowable
        // class.
        Class allowableClassField = (Class) PrivateAccessor.getField(list,
                "allowableClass");
        assertEquals("Unexpected allowableClass for the Typed List: ",
                expectedAllowableClass, allowableClassField);
    }

    /**
     * Helper method which ensures that the specified list is an UnmodifiableList
     * This method will have to be changed if Sun change the name / location of
     * java.util.Collections$UnmodifiableRandomAccessList
     * @param list The list we want to examine for immutability.
     */
    public static void ensureListIsImmutable(List list) {
        String listClass = list.getClass().getName();
        String expectedClass = "java.util.Collections$UnmodifiableRandomAccessList";
        assertEquals("getContentsAsList should return an unmodifiable list : was "
                + listClass, expectedClass, listClass);
    }

    /**
     * Helper method which ensures that the two objects provided have the same
     * hashcodes.
     * @param o1 The first object to test.
     * @param o2 The second object to test.
     */
    public static void ensureHashcodesEqual(Object o1, Object o2) {
        int object1Hashcode = o1.hashCode();
        int object2Hashcode = o2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + object1Hashcode + " and " + object2Hashcode,
                object1Hashcode == object2Hashcode);
    }

    /**
     * Helper method which ensures that the two objects provided have different hashcodes.
     * This is used by methods which test that equals and hashcode have been implemented
     * correctly.
     *
     * <p>This method is used to establish if unequal objects have different
     * hashcodes - hence the message prompts the implementor to try and ensure that the
     * objects which they are creating have different hashcodes if they are not equals. In
     * practice it is very hard to guarantee this for all situations but we should ensure
     * that it is achievable in at least the most basic of situations</p>.
     * @param o1 The first object to test.
     * @param o2 The second object to test.
     */
    public static void ensureHashcodesNotEqual(Object o1, Object o2) {
        int object1Hashcode = o1.hashCode();
        int object2Hashcode = o2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the same " +
                "hash codes. Were : "
                + object1Hashcode + " and " + object2Hashcode,
                object1Hashcode == object2Hashcode);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
