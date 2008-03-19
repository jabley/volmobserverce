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
package com.volantis.xml.pipeline.sax;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.impl.XMLPipelineFactoryImpl;

/**
 *  Test Case for the {@link XMLPipelineFactory} class
 */ 
public class XMLPipelineFactoryTestCase extends TestCaseAbstract {

    /**
     * Creates a new <code>XMLPipelineFactoryTestCase</code> instance 
     * @param name the name of test  
     */  
    public XMLPipelineFactoryTestCase(String name) {
        super(name);
    }
    
    /**
     * Tests the {@link XMLPipelineFactory#getDefaultInstance} method. 
     * @throws Exception if an error occurs
     */ 
    public void testGetDefaultInstance() throws Exception {
        XMLPipelineFactory factory = XMLPipelineFactory.getDefaultInstance();
        assertEquals("getDefaultInstance should return a " + 
                     "XMLPipelineFactoryImpl instance",
                     XMLPipelineFactoryImpl.class,
                     factory.getClass());
    }
    
    
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 ===========================================================================
*/
