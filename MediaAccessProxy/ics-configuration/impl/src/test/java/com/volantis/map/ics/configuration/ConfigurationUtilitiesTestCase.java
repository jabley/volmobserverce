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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.configuration;

import junit.framework.TestCase;

import java.util.ArrayList;

public class ConfigurationUtilitiesTestCase extends TestCase {

    final static int DEF_ONE[] = {111};

    final static int DEF_MANY[] = {111, 222, 333, 444};

    void assertEquals(int l[], int r[]) {
        assertEquals(l.length, r.length);
        for (int i = 0; i < l.length; i++) {
            assertEquals(l[i], r[i]);
        }
    }

    public void testEmptyList() {
        ArrayList list = ConfigurationUtilities.listToIntList("");
        assertEquals(list.size(), 0);
        list = ConfigurationUtilities.listToIntList("    ");
        assertEquals(list.size(), 1);

        int arr[] = null;
        arr = ConfigurationUtilities.listToIntArray("", DEF_ONE);
        assertEquals(arr, DEF_ONE);

        arr = ConfigurationUtilities.listToIntArray("     ", DEF_MANY);
        assertEquals(arr, DEF_MANY);

        arr = ConfigurationUtilities.listToIntArray("     ", DEF_ONE);
        assertEquals(arr, DEF_ONE);

        arr = ConfigurationUtilities.listToIntArray("     ", DEF_MANY);
        assertEquals(arr, DEF_MANY);
    }

    public void testEmptyArgs() {
        ArrayList list = null;
        list = ConfigurationUtilities.listToIntList(",");
        assertEquals(2, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertNull(list.get(i));
        }
        list = ConfigurationUtilities.listToIntList("  ,  ");
        assertEquals(2, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertNull(list.get(i));
        }
        list = ConfigurationUtilities.listToIntList(",,");
        assertEquals(3, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertNull(list.get(i));
        }
        list = ConfigurationUtilities.listToIntList("  ,  ,  ");
        assertEquals(3, list.size());
        for (int i = 0; i < list.size(); i++) {
            assertNull(list.get(i));
        }

        int arr[] = null;
        arr = ConfigurationUtilities.listToIntArray(",,", DEF_MANY);
        assertEquals(arr, DEF_MANY);
        arr = ConfigurationUtilities.listToIntArray("   ,  ,   ", DEF_MANY);
        assertEquals(arr, DEF_MANY);
    }

    public void testMain() {
        int arr[] = null;
        arr = ConfigurationUtilities.listToIntArray("1", DEF_ONE);
        assertEquals(DEF_ONE.length, arr.length);
        assertEquals(1, arr[0]);
        arr = ConfigurationUtilities.listToIntArray("1,2,3,", DEF_ONE);
        assertEquals(DEF_ONE.length, arr.length);
        assertEquals(1, arr[0]);

        arr = ConfigurationUtilities.listToIntArray("1,2,3,", DEF_MANY);
        assertEquals(DEF_MANY.length, arr.length);
        assertEquals(1, arr[0]);
        assertEquals(2, arr[1]);
        assertEquals(3, arr[2]);
    }

    public void testInvalid() {
        int arr[] = null;
        try {
            arr = ConfigurationUtilities.listToIntArray("1,xxxxxxddd,3,", DEF_MANY);
            fail("No exception was thrown");
        } catch (NumberFormatException ignore) {

        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Nov-05	554/1	pszul	VBM:2005102504 intelligent clipping implemented

 ===========================================================================
*/
