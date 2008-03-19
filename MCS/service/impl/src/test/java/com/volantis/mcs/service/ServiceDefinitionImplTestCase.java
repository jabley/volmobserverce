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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.service;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import com.volantis.shared.metadata.value.immutable.ImmutableStructureValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.shared.metadata.value.immutable.ImmutableStringValue;
import com.volantis.shared.metadata.value.immutable.ImmutableNumberValue;
import com.volantis.shared.metadata.value.immutable.ImmutableListValue;
import com.volantis.shared.metadata.value.immutable.ImmutableBooleanValue;
import com.volantis.shared.metadata.value.immutable.ImmutableSetValue;
import com.volantis.shared.metadata.value.MetaDataValueFactory;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.mutable.MutableStructureValue;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;
import com.volantis.shared.metadata.value.mutable.MutableListValue;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableSetValue;
import com.volantis.shared.metadata.impl.value.DefaultMetaDataValueFactory;
import com.volantis.shared.metadata.MetaDataFactory;
import com.volantis.mcs.service.impl.*;
import com.volantis.mcs.service.impl.ServiceDefinitionImpl;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
/**
 *
 */
public class ServiceDefinitionImplTestCase extends TestCaseAbstract {

    /**
     * Name of servcie
     */
    private static final String TEST_SERVICE_NAME = "testName";

    /**
     * Factory for creating meta data values
     */
    private static final MetaDataValueFactory META_DATA_VALUE_FACTORY =
            MetaDataFactory.getDefaultInstance().getValueFactory();

    /**
     * Creates a structure with the following
     *
     * key1/keyA/a-string
     *     /keyB/11
     *
     * key2[0]/true
     *     [1]/false
     *
     * key3[0]/keyX/b-string
     *        /keyY/c-string
     *     [1]/true
     *     [2]/x-string
     *
     * key4/z-string
     *
     * @return
     */
    private ImmutableStructureValue createStructure() {
        MetaDataValueFactory mdvFactory = new DefaultMetaDataValueFactory();

        // create the top level structure
        MutableStructureValue structure =
                META_DATA_VALUE_FACTORY.createStructureValue();
        Map topMap = structure.getFieldValuesAsMutableMap();

        // create the
        // key1/keyA/a-string
        //     /keyB/11
        //
        // part
        MutableStructureValue subStructure =
                META_DATA_VALUE_FACTORY.createStructureValue();


        Map subMap = subStructure.getFieldValuesAsMutableMap();

        MutableStringValue aString = mdvFactory.createStringValue();
        aString.setValue("a-string");
        subMap.put("keyA", aString);

        MutableNumberValue eleven = mdvFactory.createNumberValue();
        eleven.setValue(new Integer(11));
        subMap.put("keyB", eleven);

        topMap.put("key1", subStructure.createImmutable());

        // create the
        // key2[0]/true
        //     [1]/false
        //
        // part
        MutableListValue metaList = META_DATA_VALUE_FACTORY.createListValue();
        List list = metaList.getContentsAsMutableList();

        MutableBooleanValue trueBool =
                META_DATA_VALUE_FACTORY.createBooleanValue();
        trueBool.setValue(Boolean.TRUE);
        list.add(trueBool);

        MutableBooleanValue falseBool =
                META_DATA_VALUE_FACTORY.createBooleanValue();
        falseBool.setValue(Boolean.FALSE);
        list.add(falseBool);

        topMap.put("key2", metaList.createImmutable());

        // create the
        // key3/set[0]/keyX/b-string
        //            /keyY/c-string
        //     /set[1]/true
        //     /set[2]/x-string
        MutableSetValue metaSet = META_DATA_VALUE_FACTORY.createSetValue();
        Set set = metaSet.getContentsAsMutableSet();

        MutableStructureValue setsStruct =
                META_DATA_VALUE_FACTORY.createStructureValue();

        Map setsStructMap = setsStruct.getFieldValuesAsMutableMap();
        MutableStringValue bString = mdvFactory.createStringValue();
        bString.setValue("b-string");
        setsStructMap.put("keyX", bString);

        MutableStringValue cString = mdvFactory.createStringValue();
        cString.setValue("c-string");
        setsStructMap.put("keyY", cString);

        set.add(setsStruct.createImmutable());


        MutableBooleanValue tBool =
                META_DATA_VALUE_FACTORY.createBooleanValue();
        tBool.setValue(Boolean.TRUE);
        set.add(tBool);

        MutableStringValue xString = mdvFactory.createStringValue();
        xString.setValue("x-string");
        set.add(xString);

        topMap.put("key3", metaSet.createImmutable());
        // create the
        //
        // key4/z-string
        //
        // part
        MutableStringValue zString = mdvFactory.createStringValue();
        zString.setValue("z-string");
        topMap.put("key4", zString);

        return (ImmutableStructureValue) structure.createImmutable();
    }

    /**
     * Creates an instance of the class that is to be tested
     * @return
     */
    private ServiceDefinitionImpl createService() {
        return new ServiceDefinitionImpl(TEST_SERVICE_NAME, createStructure());
    }

    public void testGetName() {
        ServiceDefinitionImpl service = createService();
        assertEquals("ServiceDefinitionImpl#getName returned the wrong name",
                     TEST_SERVICE_NAME,
                     service.getName());
    }

    public void testSimpleKeyIntoMap() throws Exception {
        ImmutableMetaDataValue value =
                   createService().getCharacteristic("key4");
        assertImmutableStringValue(value, "z-string");
    }

    public void testMapRetrieval() throws Exception {
        ImmutableMetaDataValue value = createService().getCharacteristic("/key1");
        // we should have an ImmutableStructureValue of the form
        // keyA/a-string
        // keyB/11
        ImmutableStructureValue struct = assertImmutableStructure(value, 2);
        // check that there is a string "a-string" at keyA
        Map map = struct.getFieldValuesAsMap();
        assertImmutableStringValue((ImmutableMetaDataValue) map.get("keyA"),
                                   "a-string");

        // check that their is a nubmer 11 at keyB
        assertImmutableNumberValue((ImmutableMetaDataValue) map.get("keyB"),
                                   11);
    }

    public void testStrRetrivalFromMapMissingLeadingSlash() throws Exception {
        ImmutableMetaDataValue value =
                createService().getCharacteristic("key1/keyA");
        assertImmutableStringValue(value, "a-string");
    }

    public void testStrRetrivalFromMap() throws Exception {
        ImmutableMetaDataValue value =
                createService().getCharacteristic("/key1/keyA");
        assertImmutableStringValue(value, "a-string");
    }

    public void testStrRetrivalFromMapMissingTrailingSlash() throws Exception {
        ImmutableMetaDataValue value =
                createService().getCharacteristic("key1/keyA");
        assertImmutableStringValue(value, "a-string");
    }

    public void testListRetrieval() throws Exception {
        ImmutableMetaDataValue value =
                createService().getCharacteristic("key2");

        ImmutableListValue list = assertImmutableList(value, 2);

        // check that this items at index 0 is a boolean of value true
        assertImmutableBooleanValue(
                (ImmutableMetaDataValue)list.getContentsAsList().get(0), true);

        // check that this items at index 1 is a boolean of value false
        assertImmutableBooleanValue(
                (ImmutableMetaDataValue)list.getContentsAsList().get(1), false);
    }

    public void testBooleanRetrievalFromList() throws Exception {
        ImmutableMetaDataValue value =
                createService().getCharacteristic("/key2[0]");
        assertImmutableBooleanValue(value, true);
    }

    public void testAnotherBooleanRetrievalFromList() throws Exception {
        ImmutableMetaDataValue value =
                createService().getCharacteristic("/key2[1]");
        assertImmutableBooleanValue(value, false);
    }

    public void testSetRetrieval() throws Exception {
        ImmutableMetaDataValue value =
                createService().getCharacteristic("/key3");

        ImmutableSetValue set = assertImmutableSet(value, 3);

        Collection collection = set.getContentsAsCollection();
        Iterator iter = collection.iterator();
        // check that this items at index 0 is a boolean of value true
        assertImmutableStructure((ImmutableMetaDataValue)iter.next(), 2);

        // check that this items at index 1 is a boolean of value false
        assertImmutableBooleanValue((ImmutableMetaDataValue)iter.next(), true);
        // finally check item at index 2 is a string
        assertImmutableStringValue((ImmutableMetaDataValue)iter.next(),
                                   "x-string");
    }

    public void testSetRetrievalWithStruct() throws Exception {
         // key3/set[0]/keyX/b-string
        //            /keyY/c-string
        ImmutableMetaDataValue value =
                createService().getCharacteristic("/key3[0]/keyX");
        assertImmutableStringValue(value,"b-string");

        value = createService().getCharacteristic("/key3[0]/keyY");
        assertImmutableStringValue(value,"c-string");

    }



    public void testSetRetrievalWithBool() throws Exception {
        //     /key3/set[1]/true
        ImmutableMetaDataValue value =
                createService().getCharacteristic("/key3[1]");
        assertImmutableBooleanValue(value, true);
    }

    public void testSetRetrievalWitgStr() throws Exception {
        //     /key3/set[2]/x-string
        ImmutableMetaDataValue value =
                createService().getCharacteristic("/key3[2]");
        assertImmutableStringValue(value, "x-string");


    }
    private ImmutableStructureValue assertImmutableStructure(
            ImmutableMetaDataValue value, 
            int expectedSize) {

        if (!(value instanceof ImmutableStructureValue)) {
            fail("Expected a ImmutableStructureValue but got a " +
                 value.getClass().toString());
        }

        ImmutableStructureValue struct = (ImmutableStructureValue) value;
        assertEquals("structure should have size of " + expectedSize,
                     expectedSize,
                     struct.getFieldValuesAsMap().size());
        return struct;
    }

    private ImmutableListValue assertImmutableList(
            ImmutableMetaDataValue value,
            int expectedSize) {

        if (!(value instanceof ImmutableListValue)) {
            fail("Expected a ImmutableListValue but got a " +
                 value.getClass().toString());
        }

        ImmutableListValue list = (ImmutableListValue) value;
        assertEquals("structure should have size of " + expectedSize,
                     expectedSize,
                     list.getContentsAsList().size());
        return list;
    }

    private ImmutableSetValue assertImmutableSet(
            ImmutableMetaDataValue value,
            int expectedSize) {

        if (!(value instanceof ImmutableSetValue)) {
            fail("Expected a ImmutableSetValue but got a " +
                 value.getClass().toString());
        }

        ImmutableSetValue set = (ImmutableSetValue) value;
        assertEquals("set should have size of " + expectedSize,
                     expectedSize,
                     set.getContentsAsCollection().size());
        return set;
    }

    private ImmutableStringValue assertImmutableStringValue(
            ImmutableMetaDataValue value,
            String expectedStr) {
         if (!(value instanceof ImmutableStringValue)) {
            fail("Expected a ImmutableStringValue but got a " +
                 value.getClass().toString());
        }

        ImmutableStringValue str = (ImmutableStringValue) value;
        assertEquals("ImmutableStringValue is invalid",
                     expectedStr,
                     str.getAsString());
        return str;
    }

    private ImmutableBooleanValue assertImmutableBooleanValue(
            ImmutableMetaDataValue value,
            boolean expected) {
         if (!(value instanceof ImmutableBooleanValue)) {
            fail("Expected a ImmutableBooleanValue but got a " +
                 value.getClass().toString());
        }

        ImmutableBooleanValue bool = (ImmutableBooleanValue) value;
        assertEquals("ImmutableBooleanValue is invalid",
                     expected,
                     bool.getValueAsBoolean().booleanValue());
        return bool;
    }

    private ImmutableNumberValue assertImmutableNumberValue(
            ImmutableMetaDataValue value,
            int expectedInt) {
         if (!(value instanceof ImmutableNumberValue)) {
            fail("Expected a ImmutableNumberValue but got a " +
                 value.getClass().toString());
        }

        ImmutableNumberValue num = (ImmutableNumberValue) value;
        assertEquals("ImmutableNumberValue is invalid",
                     expectedInt,
                     num.getValueAsNumber().intValue());

        return num;
    }
}
