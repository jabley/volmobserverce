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

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.metadata.immutable.ImmutableMetaDataObject;
import com.volantis.shared.metadata.value.BooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Test case for {@link ImmutableGeneratingTypedList}.
 */
public class ImmutableGeneratingTypedListTestCase extends TypedListTestCase{

    /**
     * Tests that when a {@link com.volantis.shared.metadata.mutable.MutableMetaDataObject}
     * is put into the list, it is converted into an {@link ImmutableMetaDataObject}.
     */
    public void testAddOnlyStoresImmutable() {
        ImmutableGeneratingTypedList typedList =
                new ImmutableGeneratingTypedList(new ArrayList(), BooleanValue.class);

        MutableBooleanValue booleanValue = MetaDataFactory.getDefaultInstance().
                getValueFactory().createBooleanValue();
        typedList.add(booleanValue);

        BooleanValue obtainedBooleanValue = (BooleanValue) typedList.get(0);

        if (obtainedBooleanValue instanceof ImmutableInhibitor == false) {
            fail("the returned value should be an implementation of an " +
                    "ImmutableMetaDataOobject, was " + obtainedBooleanValue.getClass());
        }
    }

    /**
     * Tests that we are not allowed to set objects of the wrong type, and are
     * allowed to set objects of the correct type on the <code>ListIterator</code>
     * provided by {@link TypedList#iterator}.
     */
    public void testIteratorOnlySetsAllowableTypes() {
       ImmutableGeneratingTypedList typedList =
                new ImmutableGeneratingTypedList(new ArrayList(), BooleanValue.class);

        MutableBooleanValue booleanValue1 = MetaDataFactory.getDefaultInstance().
                getValueFactory().createBooleanValue();
        // create a second value which is not equal to the first value
        MutableBooleanValue booleanValue2 = MetaDataFactory.getDefaultInstance().
                getValueFactory().createBooleanValue();
        booleanValue2.setValue(Boolean.TRUE);

        typedList.add(booleanValue1);

        ListIterator iterator = typedList.listIterator();

        // check that the list iterator will store this new value as an immutable object
        iterator.next();
        iterator.set(booleanValue2);

        // Get the value stored in index 1
        BooleanValue obtainedBooleanValue = (BooleanValue) typedList.get(0);

        // check that the new value set at index 1 is value 2
        assertEquals("The new value at index 1 should be " + booleanValue2,
                booleanValue2, obtainedBooleanValue);

        // check that it is immutable
        if (obtainedBooleanValue instanceof ImmutableInhibitor == false) {
            fail("the returned value should be an implementation of an " +
                    "ImmutableMetaDataOobject, was " + obtainedBooleanValue.getClass());
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
