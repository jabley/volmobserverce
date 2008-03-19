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

/**
 * Test case for the <code>DefaultNamespaceFactory</code> class
 */ 
public class DefaultNamespaceFactoryTestCase 
        extends NamespaceFactoryTestAbstract {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";
         
    /**
     * Creates a new DefaultNamespace
     * @param name
     */ 
    public DefaultNamespaceFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Tests the createPrefixTracker method 
     * @throws Exception if an error occurs
     */    
    public void testCreatePrefixTracker() throws Exception {
        NamespaceFactory factory = new DefaultNamespaceFactory();
        NamespacePrefixTracker tracker = factory.createPrefixTracker();
        assertTrue("createPrefixTracker should return a " +
                   "DefaultNamespacePrefixTracker",
                   tracker instanceof DefaultNamespacePrefixTracker);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 29-Jul-03	266/3	doug	VBM:2003072901 Provided default implementation of the NamespaceFactory

 29-Jul-03	266/1	doug	VBM:2003072901 Provided default implementation of the NamespaceFactory

 ===========================================================================
*/
