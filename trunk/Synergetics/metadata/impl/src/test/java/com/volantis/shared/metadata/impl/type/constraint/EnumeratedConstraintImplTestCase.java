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

package com.volantis.shared.metadata.impl.type.constraint;

import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.shared.metadata.MetaDataObject;
import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataTestCaseHelper;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.type.constraint.immutable.ImmutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import junitx.util.PrivateAccessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Abstract test case for {@link EnumeratedConstraintImpl}.
 */
public class EnumeratedConstraintImplTestCase
        extends ConstraintImplTestCaseAbstract {

    /**
     * Constructor
     * @param name The name of this test case.
     */
    public EnumeratedConstraintImplTestCase(String name) throws Exception {
        super(name);
    }

    // Javadoc inherited.
    protected MutableInhibitor getMutableInhibitor() {
        return new MutableEnumeratedConstraintImpl();
    }

    // Javadoc inherited.
    protected ImmutableInhibitor getImmutableInhibitor() {
        return new ImmutableEnumeratedConstraintImpl();
    }

    // Javadoc inherited.
    public void testEqualsAndHashcodeImplementedCorrectly() {
        MutableEnumeratedConstraint enumeratedConstraint1 =
                createEnumeratedConstraintForTestEquals();
        MutableEnumeratedConstraint enumeratedConstraint2 =
                createEnumeratedConstraintForTestEquals();

        // test that both the values are equal and they have the same hash codes
        assertEquals("Object 1 should be equal to object 2", enumeratedConstraint1,
                enumeratedConstraint2);

        int booleanType1Hashcode = enumeratedConstraint1.hashCode();
        int booleanType2Hashcode = enumeratedConstraint2.hashCode();
        assertTrue("Objects which are equal should have the same hash codes. Were : "
                + booleanType1Hashcode + " and " + booleanType2Hashcode,
                booleanType1Hashcode == booleanType2Hashcode);

        // change a property in one of the objects and ensure they are not equal
        MutableBooleanValue booleanValue = MetaDataFactory.getDefaultInstance().
                getValueFactory().createBooleanValue();

        enumeratedConstraint2.getMutableEnumeratedValues().add(booleanValue);

        assertNotEquals(enumeratedConstraint1, enumeratedConstraint2);
        assertNotEquals(enumeratedConstraint2, enumeratedConstraint1);

        // ensure that the hashcodes are different for these two objects
        booleanType1Hashcode = enumeratedConstraint1.hashCode();
        booleanType2Hashcode = enumeratedConstraint2.hashCode();
        assertFalse("Objects which are not equal should ideally have different hash codes",
                booleanType1Hashcode == booleanType2Hashcode);
    }

    /**
     * Helper method which creates a <code>MutableEnumeratedConstraint</code> which has
     * a single <code>SimpleValue</code> and is used for testing.
     * @return a mutable enumerated constraint.
     */
    private MutableEnumeratedConstraint createEnumeratedConstraintForTestEquals() {
        MutableEnumeratedConstraintImpl mutableEnumeratedConstraint =
                new MutableEnumeratedConstraintImpl();

        MutableBooleanValue booleanValue = MetaDataFactory.getDefaultInstance().
                getValueFactory().createBooleanValue();
        booleanValue.setValue(Boolean.TRUE);

        mutableEnumeratedConstraint.getMutableEnumeratedValues().add(booleanValue);
        return mutableEnumeratedConstraint;
    }

   /**
     * Test which ensures that when {@link EnumeratedConstraintImpl#getEnumeratedValues} is
     * called on a {@link EnumeratedConstraintImpl} then the returned
    * {@link java.util.Collection} is immutable.
     */
    public void testGetEnumeratedValuesReturnsImmutableListOnMutable(){
        MutableEnumeratedConstraint mutableConstraint = (MutableEnumeratedConstraint)
                getMutableInhibitor();
        List list = mutableConstraint.getEnumeratedValues();
        MetaDataTestCaseHelper.ensureListIsImmutable(list);
    }

    /**
     * Test which ensures that when {@link com.volantis.shared.metadata.value.CollectionValue#getContentsAsCollection} is
     * called on an {@link com.volantis.shared.metadata.value.immutable.ImmutableCollectionValue} then the returned {@link java.util.Collection}
     * is immutable.
     */
    public void testGetEnumeratedValuesReturnsImmutableCollectionOnImmutable(){
        ImmutableEnumeratedConstraint mutableConstraint = (ImmutableEnumeratedConstraint)
                getImmutableInhibitor();
        List list = mutableConstraint.getEnumeratedValues();
       MetaDataTestCaseHelper.ensureListIsImmutable(list);
    }

    /**
     * Tests that when a <code>MutableEnumeratedConstraintImpl</code> is created by a call
     * to {@link MutableEnumeratedConstraintImpl#createMutable}, the two
     * objects do not share a common <code>Collection</code>.
     */
    public void testCreateMutableFromMutableDoesNotShareCollection() throws Throwable {
        MutableEnumeratedConstraintImpl enumeratedConstraintImpl1 = (MutableEnumeratedConstraintImpl)
                getMutableInhibitor();

        MutableEnumeratedConstraintImpl enumeratedConstraintImpl2 =
                (MutableEnumeratedConstraintImpl) enumeratedConstraintImpl1.createMutable();

        ArrayList internalList1 = (ArrayList) PrivateAccessor.getField(enumeratedConstraintImpl1,
                "enumeratedValues");

        ArrayList internalList2 = (ArrayList) PrivateAccessor.getField(enumeratedConstraintImpl2,
                "enumeratedValues");

        assertNotSame("Separate instances of the same object should not share collections",
                internalList1, internalList2);

        // check that the lists are not linked somehow by ensuring that the
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

 14-Jan-05	6560/3	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
