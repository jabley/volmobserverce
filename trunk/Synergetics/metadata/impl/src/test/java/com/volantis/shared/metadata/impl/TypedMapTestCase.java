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
import com.volantis.shared.metadata.value.NumberValue;
import com.volantis.shared.metadata.value.BooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import com.volantis.shared.metadata.MetaDataFactory;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;

/**
 * Test case for {@link TypedMap}.
 */
public class TypedMapTestCase extends TypedCollectionTestCaseAbstract {

    protected TypedMap createTypedMap(Map delegate,
                                      Class allowableValueClass,
                                      boolean allowNullValue) {
        return new TypedMap(delegate, allowableValueClass, allowNullValue);
    }

    /**
     * Tests that the typed list only accepts allowable types.
     */
    public void testMapOnlyStoresAllowableTypes() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        String key = "key";

        BooleanValue allowableValue = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        Object forbiddenValue = new Integer(1);

        // create the map so that it only allows allowable values and keys
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           allowableValue.getClass(), false);

        // test that we can add an allowable value with an allowable key
        try {
            typedMap.put(key, allowableValue);
        } catch (IllegalArgumentException e) {
            fail("Adding a " + allowableValue.getClass() + " value with a "
                 + key.getClass() + " key should be permitted.");
        }

        // test that we can not add a forbidden value with an allowable key
        try {
            typedMap.put(key, forbiddenValue);
            fail("Adding a forbidden value with an allowed key should not be " +
                 "permitted. Allowed value: " + allowableValue.getClass()
                 + " forbidden value : " + forbiddenValue.getClass());
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * This tests that the containsValue method will only work if the value is of the
     * permitted type.
     */
    public void testContainsValueOnlyAcceptsAllowableValues() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        String allowableKey = "key";

        BooleanValue allowableValue = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        Object forbiddenValue = new Integer(1);

        // create the map so that it only allows allowable values and keys
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           allowableValue.getClass(), false);

        // test that we can add an allowable value with an allowable key
        try {
            typedMap.containsValue(allowableValue);
        } catch (IllegalArgumentException e) {
            fail("Calling containsValue with an allowableValue should be permitted.");
        }

        // test that we can not add a forbidden value with an allowable key
        try {
            typedMap.containsValue(forbiddenValue);
            fail("Calling containsValue with a forbidden value should not be permitted.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * This tests that the containsKey method will only work if the key is of the
     * permitted type.
     */
    public void testContainsKeyOnlyAcceptsAllowableKeys() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        String allowableKey = "key";

        BooleanValue allowableValue = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        Object forbiddenKey = new Integer(2);
        // create the map so that it only allows allowable values and keys
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           allowableValue.getClass(), false);

        // test that we can add an allowable value with an allowable key
        try {
            typedMap.containsKey(allowableKey);
        } catch (IllegalArgumentException e) {
            fail("Calling containsKey with an allowableKey should be permitted.");
        }

        // test that we can not add a forbidden value with an allowable key
        try {
            typedMap.containsKey(forbiddenKey);
            fail("Calling containsKey with a forbidden key should not be permitted.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * This tests that the remove method will only work if the key is of the
     * permitted type.
     */
    public void testRemoveAcceptsAllowableValues() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        String allowableKey = "key";

        BooleanValue allowableValue = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        Object forbiddenValue = new Integer(1);
        // create the map so that it only allows allowable values and keys
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           allowableValue.getClass(), false);

        typedMap.put(allowableKey, allowableValue);

        // test that we can add remove with an allowable value
        try {
            typedMap.remove(allowableKey);
        } catch (IllegalArgumentException e) {
            fail("Calling remove with an allowableValue should be permitted.");
        }

        // test that calling remove with a forbidden value throws an exception
        try {
            typedMap.remove(forbiddenValue);
            fail("Calling remove with an forbidden value should be permitted.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * This tests that the get method will only work if the key is of the
     * permitted type.
     */
    public void testGetOnlyAcceptsAllowableKeys() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        MutableBooleanValue allowableValue = (MutableBooleanValue) f.getValueFactory()
            .createBooleanValue().createMutable();
        String allowableKey = "key";

        NumberValue forbiddenKey = (NumberValue) f.getValueFactory()
            .createNumberValue();

        // create the map so that it only allows allowable values and keys
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           allowableValue.getClass(), false);

        // test that we can add an allowable value with an allowable key
        try {
            typedMap.get(allowableKey);
        } catch (IllegalArgumentException e) {
            fail("Calling get with an allowableKey should be permitted.");
        }

        // test that we can not add a forbidden value with an allowable key
        try {
            typedMap.get(forbiddenKey);
            fail("Calling get with a forbidden key should not be permitted.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * This test ensures that when entrySet() is called, then the provided Iterator will
     * not allow callers to use setValue on the <code>Map.Entry</code> object with
     * forbidden types, but will allow callers to setValue with an allowed type.
     */
    public void testEntrySetIteratorWillOnlyAllowCorrectType() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        MutableBooleanValue allowableValue= (MutableBooleanValue) f.getValueFactory()
            .createBooleanValue().createMutable();
        allowableValue.setValue(Boolean.TRUE);

        BooleanValue newAllowableValue = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        String allowableKey = "key";
        NumberValue forbiddenValue = (NumberValue) f.getValueFactory().createNumberValue();

        // create the map so that it only allows allowable values and keys
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           allowableValue.getClass(), false);

        typedMap.put(allowableKey, allowableValue);
        Set entrySet = typedMap.entrySet();
        Iterator iter = entrySet.iterator();
        Map.Entry entry = (Map.Entry) iter.next();

        // try with an allowed value
        try {
            entry.setValue(newAllowableValue);
        } catch (IllegalArgumentException e) {
            fail("Setting an allowable value on the EntrySet should be allowed");
        }

        try {
            entry.setValue(forbiddenValue);
            fail("Setting a forbidden value on the EntrySet should throw an exception.");
        } catch (IllegalArgumentException e) {
            // expected.
        }
    }

    /**
     * This test ensures that when entrySet() is called, the set returned is backed by the
     * <code>TypedMap</code>.
     */
    public void testEntrySetIteratorEffectsTheSet() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        MutableBooleanValue originalValue = (MutableBooleanValue) f.getValueFactory()
            .createBooleanValue().createMutable();
        originalValue.setValue(Boolean.TRUE);

        BooleanValue newValue = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        String key = "key";

        // create the map
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           originalValue.getClass(), false);
        typedMap.put(key, originalValue);

        // Get an iterator on the entry set
        Set entrySet = typedMap.entrySet();
        Iterator iter = entrySet.iterator();
        Map.Entry entry = (Map.Entry) iter.next();

        // change the original value to the new value
        entry.setValue(newValue);

        // ensure that the value has changed in the map
        Object retrievedObject = typedMap.get(key);
        assertSame("Making a change to an object in the entrySet should also change " +
                   "the backing Map", retrievedObject, newValue);
    }

    /**
     * This test ensures that when entrySet() is called, the remove() method of the
     * iterator works correctly.
     */
    public void testEntrySetIteratorRemoveMethodWorks() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        String key = "key";

        BooleanValue value = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        // create the map
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           value.getClass(), false);
        typedMap.put(key, value);

        // Get an iterator on the entry set
        Set entrySet = typedMap.entrySet();
        Iterator iter = entrySet.iterator();
        iter.next();
        iter.remove();

        // ensure that the map does not contain this value
        Object retrievedObject = typedMap.get(key);

        assertNull("The value put in the map should be removed after it has been removed" +
                   "via the iterator of the entry set", retrievedObject);
    }

    /**
     * This test ensures that when entrySet() is called, the remove() method works.
     */
    public void testEntrySetRemoveMethodWorks() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        String key = "key2";

        BooleanValue value = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        // create the map
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           value.getClass(), false);
        typedMap.put(key, value);

        // Get the entry from the entry set and remove it
        Set entrySet = typedMap.entrySet();
        Map.Entry entry = (Map.Entry) entrySet.toArray()[0];
        entrySet.remove(entry);

        // ensure that the map does not contain the value we put in earlier
        Object retrievedObject = typedMap.get(key);

        assertNull("The value put in the map should be removed after it has been removed" +
                   "via the iterator of the entry set", retrievedObject);
    }

    /**
     * Test that null keys are rejected when they are not allowed.
     */
    public void testNullKeysRejected() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();

        BooleanValue value = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        // create the map
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           value.getClass(), false);
        try {
            typedMap.put(null, value);
            fail("Expected exception due to null key");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    /**
     * Test that null values are rejected when they are not allowed.
     */
    public void testNullValuesRejected() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        String key = "key";

        BooleanValue value = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        // create the map
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           value.getClass(), false);
        try {
            typedMap.put(key, null);
            fail("Expected exception due to null value");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    /**
     * Test that null keys are accepted when they are allowed.
     */
    public void testNullValuesAccepted() {
        MetaDataFactory f = MetaDataFactory.getDefaultInstance();
        String key = "key";

        BooleanValue value = (BooleanValue) f.getValueFactory()
            .createBooleanValue();
        // create the map
        TypedMap typedMap = createTypedMap(new HashMap(),
                                           value.getClass(), true);
        typedMap.put(key, null);
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 13-Jan-05	6560/1	tom	VBM:2004122401 More Metadata API implementation

 ===========================================================================
*/
