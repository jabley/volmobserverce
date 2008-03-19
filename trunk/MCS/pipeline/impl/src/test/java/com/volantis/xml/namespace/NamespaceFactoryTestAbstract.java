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
package com.volantis.xml.namespace;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for the <code>NamespaceFactory</code>
 */ 
public abstract class NamespaceFactoryTestAbstract extends TestCaseAbstract {
    
    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Create a new NamespaceFactoryTestAbstract instance
     * @param name the name of the test case
     */ 
    public NamespaceFactoryTestAbstract(String name) {
        super(name);
    }

    /**
     * Tests the getDefaultInstance mehtod
     * @throws Exception if an error occurs
     */ 
    public void testGetDefaultInstace() throws Exception {
        NamespaceFactory factory = NamespaceFactory.getDefaultInstance();
        assertTrue("getDefaultInstance should return a " +
                   "DefaultNamespaceFactory",
                   factory instanceof DefaultNamespaceFactory);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 29-Jul-03	266/4	doug	VBM:2003072901 Provided default implementation of the NamespaceFactory

 29-Jul-03	266/1	doug	VBM:2003072901 Provided default implementation of the NamespaceFactory

 ===========================================================================
*/
