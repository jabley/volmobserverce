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
package com.volantis.xml.pipeline.sax.operations;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.impl.operations.PipelineOperationFactoryImpl;
import org.xml.sax.SAXException;

/**
 * Test case for the {@link PipelineOperationFactory} class
 */ 
public abstract class PipelineOperationFactoryTestAbstract 
        extends TestCaseAbstract {

    /**
     * Creates a new <code>PipelineOperationFactoryTestAbstract</code> instance.
     * @param name the name of the test
     */ 
    public PipelineOperationFactoryTestAbstract(String name) {
        super(name);
    }
    
    /**
     * Tests the {@link PipelineOperationFactory#getDefaultInstance} method.
     * @throws Exception if an error occurs 
     */ 
    public void testGetDefaultInstance() throws Exception {
        PipelineOperationFactory factory = 
                PipelineOperationFactory.getDefaultInstance();
        
        // ensure the correct factory was returned
        assertEquals("getDefaultInstance should return a " +
                     "PipelineOperationFactoryImpl",
                     PipelineOperationFactoryImpl.class,
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
