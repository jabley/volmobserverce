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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * ArrayCacheTestCase
 */
public class ArrayCacheTestCase extends TestCase {

    /**
     * Constructor for ArrayCacheTest.
     *
     * @param arg0
     */
    public ArrayCacheTestCase(String arg0) {
        super(arg0);
    }

    /**
     * Test for void ArrayCache()
     */
    public void testArrayCache() {
        ArrayCache c = new ArrayCache();
        c.put(new String("test"), new String("wibble"));
        assertEquals("Key/Value not added", c.keyIndex("test"), 0);
        assertEquals("Index should not exist", c.keyIndex("fred"), -1);
    }

    /**
     * Test for void ArrayCache(int)
     */
    public void testArrayCacheint() {
        ArrayCache c = new ArrayCache(5);
        c.put(new String("test1"), new String("wibble1"));
        c.put(new String("test2"), new String("wibble2"));
        c.put(new String("test3"), new String("wibble3"));
        c.put(new String("test4"), new String("wibble4"));
        c.put(new String("test5"), new String("wibble5"));
        c.put(new String("test6"), new String("wibble6"));
        assertEquals("test1 Key/Value not added", c.keyIndex("test1"), 0);
        assertEquals("test6 Key/Value not added", c.keyIndex("test6"), 5);
        assertEquals("fred index should not exist", c.keyIndex("fred"), -1);
    }

    public void testSize() {
        ArrayCache c = new ArrayCache(5);
        c.put(new String("test1"), new String("wibble1"));
        c.put(new String("test2"), new String("wibble2"));
        c.put(new String("test3"), new String("wibble3"));
        c.put(new String("test4"), new String("wibble4"));
        c.put(new String("test5"), new String("wibble5"));
        c.put(new String("test6"), new String("wibble6"));
        assertEquals("Should be 6 items.", c.size(), 6);
    }


    public void testIsEmpty() {
        ArrayCache c = new ArrayCache(5);
        assertTrue("Should be empty", c.isEmpty());
        c.put(new String("test1"), new String("wibble1"));
        assertFalse("Should not be empty", c.isEmpty());
    }

    public void testContainsKey() {
        ArrayCache c = new ArrayCache(5);
        c.put(new String("test1"), new String("wibble1"));
        assertEquals("Index should be 0", c.keyIndex("test1"), 0);
    }

    public void testContainsValue() {
        ArrayCache c = new ArrayCache(5);
        c.put(new String("test1"), new String("wibble1"));
        assertEquals("Index should be 0", c.valueIndex("wibble1"), 0);
    }

    public void testGet() {
        ArrayCache c = new ArrayCache(5);
        c.put(new String("test1"), new String("wibble1"));
        String s = (String) c.get("test1");
        assertEquals("Value should be wibble1", s, "wibble1");
    }

    public void testRemove() {
        ArrayCache c = new ArrayCache(5);
        c.put(new String("test1"), new String("wibble1"));
        c.put(new String("test2"), new String("wibble2"));
        c.put(new String("test3"), new String("wibble3"));
        assertEquals("Should be 3 elements", c.size(), 3);
        assertEquals("test2 should exist", c.keyIndex("test2"), 1);
        assertEquals("wibble2 should exist", c.valueIndex("wibble2"), 1);

        c.remove("test2");
        assertEquals("Should be 2 elements", c.size(), 2);
        assertEquals("test2 should not exist", c.keyIndex("test2"), -1);
        assertEquals("wibble2 should not exist", c.valueIndex("wibble2"),
                     -1);
    }

    public void testPutAll() {
        Map m = new HashMap();
        m.put(new String("test1"), new String("wibble1"));
        m.put(new String("test2"), new String("wibble2"));
        m.put(new String("test3"), new String("wibble3"));
        m.put(new String("test4"), new String("wibble4"));
        m.put(new String("test5"), new String("wibble5"));
        m.put(new String("test6"), new String("wibble6"));

        ArrayCache c = new ArrayCache(2);
        c.putAll(m);
        assertEquals(" Should be 6 elements", c.size(), 6);
        assertTrue("test2 should exist", c.keyIndex("test2") > -1);
        assertTrue("wibble2 should exist", c.valueIndex("wibble2") > -1);
    }

    public void testClear() {
        ArrayCache c = new ArrayCache(5);
        c.put(new String("test1"), new String("wibble1"));
        c.put(new String("test2"), new String("wibble2"));
        c.put(new String("test3"), new String("wibble3"));
        assertEquals("Should be 3 elements", c.size(), 3);
        c.clear();
        assertTrue("Should be empty", c.isEmpty());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Sep-03	66/5	steve	VBM:2003082105 Rework and code tidy up

 02-Sep-03	66/3	steve	VBM:2003082105 Rework and code tidy up

 29-Aug-03	66/1	steve	VBM:2003082105 ArrayCache implementation

 ===========================================================================
*/
