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
 * $Header: /src/voyager/com/volantis/mcs/gui/policyobject/PolicyObjectChooser.java,v 1.1 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-May-03    Adrian          VBM:2003030509 - Created this junit testcase 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.cache;

import junit.framework.TestCase;


/**
 * This class tests CacheKey.
 */
public class CacheKeyTestCase extends TestCase {
    /**
     * Construct a new instance of CacheKeyTestCase
     */
    public CacheKeyTestCase(String name) {
        super(name);
    }

    /**
     * Test CacheKey.equals()     
     */ 
    public void testEquals() throws Exception {
        CacheKey key1 = new CacheKey();
        key1.addKey("itemA");
        key1.addKey("itemB");
        
        CacheKey key2 = new CacheKey();
        key2.addKey("itemA");
        key2.addKey("itemB");
        
        assertEquals("The CacheKeys should be equal as they contain " +
                "Objects that are equal and in the same order.", key1, key2);
        
        CacheKey key3 = new CacheKey();
        key3.addKey("itemB");
        key3.addKey("itemA");
        
        assertTrue("The CacheKeys should not be equal as the Objects, " +
                "although equal, are in a different order", !key1.equals(key3));
        
        CacheKey key4 = new CacheKey();
        key4.addKey("itemA");
        assertTrue("The CacheKeys should not be equal as they do not " +
                "contain all the same equal Objects", !key1.equals(key4));
        
        CacheKey key5 = new CacheKey();
        key5.addKey("itemC");
        key5.addKey("itemD");
        assertTrue("The CacheKeys should not be equal as they do not " +
                "contain the same equal Objects", !key1.equals(key5));
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
