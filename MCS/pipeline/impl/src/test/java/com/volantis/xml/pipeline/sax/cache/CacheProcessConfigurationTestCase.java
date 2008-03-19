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
import com.volantis.cache.Cache;

/**
 * This class tests CacheProcessConfiguration.
 */
public class CacheProcessConfigurationTestCase extends TestCase {
    
    /**
     * Construct a new instance of CacheProcessConfigurationTestCase
     */
    public CacheProcessConfigurationTestCase(String name) {
        super(name);
    }
    
    /**
     * Test the methods createCache() and getCache()    
     */ 
    public void testCreateAndGetCache() throws Exception {
        CacheProcessConfiguration cacheProcessConfiguration =
                new CacheProcessConfiguration(Integer.MAX_VALUE);
        cacheProcessConfiguration.createCache("myCache", "12345", "0");
        
        Cache cache = cacheProcessConfiguration.getCache();

        assertNotNull("Cache 'myCache' should exist", cache);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Sep-03	413/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored

 ===========================================================================
*/
