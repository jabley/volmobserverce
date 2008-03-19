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

import com.volantis.shared.metadata.value.CollectionValue;
import com.volantis.shared.metadata.value.MetaDataValueTestCaseAbstract;
import com.volantis.shared.metadata.value.immutable.ImmutableCollectionValue;
import com.volantis.shared.metadata.value.mutable.MutableCollectionValue;

import java.util.Collection;
import java.util.Iterator;

/**
 * Abstract test case for implementations of <code>CollectionValue</code>.
 */
public abstract class CollectionValueImplTestCaseAbstract
        extends MetaDataValueTestCaseAbstract {

    // Javadoc inherited.
    public CollectionValueImplTestCaseAbstract(String name) throws Exception {
        super(name);
    }

    /**
     * Test which ensures that when {@link CollectionValue#getContentsAsCollection} is
     * called on a {@link MutableCollectionValue} then the returned {@link Collection} is
     * immutable.
     */
    public void testGetContentsAsCollectionReturnsImmutableCollectionOnMutable(){
        MutableCollectionValue mutableCollection = (MutableCollectionValue)
                getMutableInhibitor();
        Collection collection = mutableCollection.getContentsAsCollection();
        doImmutableCollectionTest(collection);
    }

    /**
     * Test which ensures that when {@link CollectionValue#getContentsAsCollection} is
     * called on an {@link ImmutableCollectionValue} then the returned {@link Collection}
     * is immutable.
     */
    public void testGetContentsAsCollectionReturnsImmutableCollectionOnImmutable(){
        ImmutableCollectionValue immutableCollection = (ImmutableCollectionValue)
                getImmutableInhibitor();
        Collection collection = immutableCollection.getContentsAsCollection();
        doImmutableCollectionTest(collection);
    }

    /**
     * Helper method which tests that the provided <code>Collection</code> is immutable.
     * All methods which can modify the list, including the Iterator which it provides,
     * should throw an <code>UnsupportedOperationException</code>.
     * @param collection The <code>Collection</code> which should be immutable.
     */
    protected void doImmutableCollectionTest(Collection collection) {

        // test add(Object o)
        try {
            collection.add(null);
            fail("A collection obtained by getContentAsCollection() should be totally " +
                    "unmodifiable - add(Object o) should not be supported");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test addAll(Collection c)
        try {
            collection.addAll(null);
            fail("A collection obtained by getContentAsCollection() should be totally " +
                    "unmodifiable - addAll(Collection c) should not be supported");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test remove(Object o)
        try {
            collection.remove(null);
            fail("A collection obtained by getContentAsCollection() should be totally " +
                    "unmodifiable - remove(Object o) should not be supported");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test removeAll(Collection c)
        try {
            collection.removeAll(null);
            fail("A collection obtained by getContentAsCollection() should be totally " +
                    "unmodifiable - removeAll(Collection c) should not be supported");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test retainAll(Collection c)
        try {
            collection.retainAll(null);
            fail("A collection obtained by getContentAsCollection() should be totally " +
                    "unmodifiable - retainAll(Collection c) should not be supported");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        // test that the Iterator does not support remove
        Iterator iterator = collection.iterator();
        try{
            iterator.remove();
            fail("The remove method should throw an unsupported operation exception as it" +
                    "should not be possible to modify an immutable collection");
        } catch(UnsupportedOperationException e) {
            // expected.
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
