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

package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataTestCaseHelper;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.immutable.ImmutableListValue;
import com.volantis.shared.metadata.value.mutable.MutableListValue;
import junitx.util.PrivateAccessor;

import java.util.List;

/**
 * Test case for {@link ListValueImpl}.
 */
public class ListValueImplTestCase extends CollectionValueImplTestCaseAbstract {

    // Javadoc inherited.
    public ListValueImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableListValueImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableListValueImpl();
    }

    /**
     * Test which ensures that when {@link ListValueImpl#getContentsAsList} is
     * called on a {@link MutableListValue} then the returned {@link java.util.List} is
     * immutable.
     */
    public void testGetContentsAsListReturnsImmutableListOnMutable() {
        MutableListValue mutableList = (MutableListValue)
                getMutableInhibitor();
        List list = mutableList.getContentsAsList();
        MetaDataTestCaseHelper.ensureListIsImmutable(list);
    }

    /**
     * Test which ensures that when {@link ListValueImpl#getContentsAsList} is
     * called on an {@link ImmutableListValue} then the returned {@link java.util.List} is
     * immutable.
     */
    public void testGetContentsAsListReturnsImmutableListOnImmutable() {
        ImmutableListValue immutableList = (ImmutableListValue)
                getImmutableInhibitor();
        List list = immutableList.getContentsAsList();
        MetaDataTestCaseHelper.ensureListIsImmutable(list);
    }

    /**
     * This method ensures that when ListValueImpl#getContentsAsMutableList() is called
     * then the list returned is a
     * {@link com.volantis.shared.metadata.impl.ImmutableGeneratingTypedList} and has been
     * set to accept only {@link com.volantis.shared.metadata.value.MetaDataValue} objects.
     *
     * <p>There is a separate test case for
     * <code>ImmutableGeneratingTypedList</code> which ensures that the list generates
     * immutable objects on the fly as items are added/ inserted to it and will only accept
     * items of the specified class. See
     * {@link com.volantis.shared.metadata.impl.ImmutableGeneratingTypedListTestCase}.</p>
     *
     */
    public void testGetContentsAsMutableListReturnsCorrectList() throws Throwable {
        MutableListValue mutableList = (MutableListValue)
                getMutableInhibitor();
        List list = mutableList.getContentsAsMutableList();

        MetaDataTestCaseHelper.ensureMutableListIsCorrect(list, MetaDataValue.class);
    }


    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableListValueImpl listValue1 = createListValueForTests();
        MutableListValueImpl listValue2 = createListValueForTests();

        // ensure that the two objects are equal
        assertEquals("Object 1 should  be equal to object 2", listValue1, listValue2);

        // ensure that they have the same hash code
        int listValue1Hashcode = listValue1.hashCode();
        int listValue2Hashcode = listValue2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);

        // now change a single item in each list and ensure that they are different
        listValue2.getContentsAsMutableList().remove(0);
        assertNotEquals(listValue1, listValue2);

        // see if the hashcodes are different
        listValue1Hashcode = listValue1.hashCode();
        listValue2Hashcode = listValue2.hashCode();
        assertFalse("Objects which are not equal should ideally not have the same hash " +
                "codes. Were : " + listValue1Hashcode + " and " + listValue2Hashcode,
                listValue1Hashcode == listValue2Hashcode);
    }

    /**
     * Helper method for which creates a MutableListValueImpl with data in it.
     * @return a <code>MutableListValueImpl</code> with a single item in its list.
     */
    private MutableListValueImpl createListValueForTests() {
        MutableListValueImpl mutableListValue = new MutableListValueImpl();
        List list = mutableListValue.getContentsAsMutableList();
        list.add(new MutableBooleanValueImpl());
        return mutableListValue;
    }

    /**
     * Tests that when a <code>MutableListValue</code> is created by a call to
     * {@link com.volantis.shared.metadata.value.ListValue#createMutable},
     * the two objects do not
     * share a common <code>List</code>.
     */
    public void testCreateMutableFromMutableDoesNotShareList() throws Throwable {
        MutableListValueImpl listValueImpl1 = (MutableListValueImpl)
                getMutableInhibitor();

        MutableListValueImpl listValueImpl2 =
                (MutableListValueImpl) listValueImpl1.createMutable();

        List internalList1 = (List) PrivateAccessor.getField(listValueImpl1,
                "contents");

        List internalList2 = (List) PrivateAccessor.getField(listValueImpl2,
                "contents");

        assertNotSame("Separate instances of the same object should not share collections",
                internalList1, internalList2);

        // check that the collections are not linked somehow by ensuring that the
        // sizes of the lists are different after adding an object to one of them
        internalList1.add(new Object());

        assertFalse("The two lists should have different sizes after adding an object " +
                "to one list", internalList1.size() == internalList2.size());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/7	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/5	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
