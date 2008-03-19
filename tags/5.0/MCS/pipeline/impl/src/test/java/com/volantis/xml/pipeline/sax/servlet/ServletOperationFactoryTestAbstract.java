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
package com.volantis.xml.pipeline.sax.servlet;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for the {@link ServletOperationFactory} class
 */
public abstract class ServletOperationFactoryTestAbstract
        extends TestCaseAbstract {

    /**
     * Creates a new <code>ServletOperationFactoryTestAbstract</code> instance
     * @param name the name of the test
     */
    public ServletOperationFactoryTestAbstract(String name) {
        super(name);
    }

    /**
     * Tests the {@link ServletOperationFactory#getDefaultInstance} method.
     * @throws Exception if an error occurs 
     */
    public void testGetDefaultInstance() throws Exception {
        ServletOperationFactory factory =
                ServletOperationFactory.getDefaultInstance();
        
        // ensure the correct factory was returned
        assertEquals("getDefaultInstance should return a " +
                     "DefaultServletOperationFactory instance",
                     DefaultServletOperationFactory.class,
                     factory.getClass());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 ===========================================================================
*/
