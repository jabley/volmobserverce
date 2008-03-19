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

import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import com.volantis.shared.metadata.value.BooleanValue;
import com.volantis.shared.metadata.value.NumberValue;
import com.volantis.shared.metadata.MetaDataFactory;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Test case for {@link TypedList} which ensures that this list will only store allowable
 * types for both it and its iterator.
 */
public class TypedListTestCase extends TypedCollectionTestCaseAbstract {

    /**
     * Tests that the typed list only accepts allowable types.
     */
    public void testListOnlyStoresAllowableTypes() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        MutableBooleanValue allowableObject = (MutableBooleanValue) f.getValueFactory()
            .createBooleanValue().createMutable();
        allowableObject.setValue(Boolean.TRUE);

        NumberValue unallowableObject = (NumberValue) f.getValueFactory()
            .createNumberValue();

        TypedList typedList = new TypedList(new ArrayList(), BooleanValue.class);

        try {
            typedList.add(allowableObject);
        } catch (IllegalArgumentException e) {
            fail("Adding a " + allowableObject.getClass() + " should be permitted.");
        }

        // check that the mutable list will not allow a forbiden type
        try {
            typedList.add(unallowableObject);
            fail("Adding any thing other than a " + allowableObject.getClass() +
                    " should throw an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * Tests that we are not allowed to add objects of the wrong type, and are
     * allowed to add objects of the correct type on the <code>ListIterator</code>
     * provided by {@link TypedList#iterator}.
     */
    public void testIteratorOnlyAddsAllowableTypes() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        MutableBooleanValue allowableObject = (MutableBooleanValue) f.getValueFactory()
            .createBooleanValue().createMutable();
        allowableObject.setValue(Boolean.TRUE);

        NumberValue unallowableObject = (NumberValue) f.getValueFactory()
            .createNumberValue();
        TypedList typedList = new TypedList(new ArrayList(), BooleanValue.class);
        ListIterator iterator = typedList.listIterator();

        // check that the list iterator will allow an permitted type to be added
        try {
            iterator.add(allowableObject);
        } catch (IllegalArgumentException e) {
            fail("Adding a " + allowableObject.getClass() + " should be permitted.");
        }

        // check that the list iterator will not allow a forbiden type to be added
        try {
            iterator.add(unallowableObject);
            fail("Adding any thing other than a " + allowableObject.getClass() +
                    " should throw an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
