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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/url/SortedURLTreeMapTestCase.java,v 1.3 2002/10/09 16:39:39 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Sep-02    Mat             VBM:2002092512 - Created to test the SortedURLTreeMap
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.url;

import junit.framework.TestCase;

/**
 * @author mat
 */
public class SortedURLTreeMapTestCase extends TestCase {

    private SortedURLTreeMap tree;

    /**
     * Creates a new SortedURLTreeTest object.
     *
     * @param testName DOCUMENT ME!
     */
    public SortedURLTreeMapTestCase(java.lang.String testName) {
        super(testName);
    }

    public void setUp() {
        tree = new SortedURLTreeMap();
    }

    public void tearDown() {
    }

    /**
     * Test of put method, of class com.volantis.synergetics.url.SortedURLTreeMap.
     */
    public void testPut() {
        tree.put("http://mat/cars", "http://mat/cars");
        String s = (String) tree.get("http://mat/cars");
        assertTrue("put operation did not work", s.equals("http://mat/cars"));
    }

    /**
     * Test of get method, of class com.volantis.synergetics.url.SortedURLTreeMap.
     */
    public void testGet() {

        tree.put("http://mat/cars", "http://mat/cars");
        tree.put("http://mat/cars/pics", "http://mat/cars/pics");
        tree.put("http://mat/cars/pics/audi", "http://mat/cars/pics/audi");
        tree.put("http://steve/cars", "http://steve/cars");
        tree.put("http://sumit/cars", "http://sumit/cars");
        tree.put("http://sumit/cars/bike", "http://sumit/cars/bike");
        tree.put("http://mat/cars/pics", "http://mat/cars/shouldnotexist");

        String result;

        result = (String) tree.get("http://mat/cars/pics/links");
        assertTrue("1st get failed", result.equals("http://mat/cars/pics"));
        result = (String) tree.get("http://sumit/cars/bike/pics/links");
        assertTrue("2nd get failed", result.equals("http://sumit/cars/bike"));
        result = (String) tree.get("http://mat");
        assertNull("3rd get failed", result);
        result = (String) tree.get("http://dave/cars/links");
        assertNull("4th get failed", result);
        result = (String) tree.get("http://mat/cars/links");
        assertTrue("5th get failed", result.equals("http://mat/cars"));
    }
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-May-04	219/1	allan	VBM:2004052101 Add URLIntrospector and SortedURLTreeMap

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
