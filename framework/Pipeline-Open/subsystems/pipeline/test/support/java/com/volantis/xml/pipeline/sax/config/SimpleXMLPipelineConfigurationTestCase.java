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
package com.volantis.xml.pipeline.sax.config;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Map;

import junitx.util.PrivateAccessor;

public class SimpleXMLPipelineConfigurationTestCase 
        extends TestCaseAbstract {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";
    
    /**
     * A reference to the instance of the class being tested
     */ 
    private XMLPipelineConfiguration config;
    
    /**
     * The internal map that the XMLPipelineConfiguration instance manages
     */ 
    private Map internalConfigMap;
    
    /**
     * Creates a new SimpleXMLPipelineConfigurationTestCase instance 
     * @param name the test name
     */ 
    public SimpleXMLPipelineConfigurationTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        config = new SimpleXMLPipelineConfiguration();
        internalConfigMap = (Map) PrivateAccessor.getField(config, 
                                                           "configurations");
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    /**
     * Tests the storeConfiguration method
     * @throws Exception if an error occurs
     */ 
    public void testStoreConfiguration() throws Exception {
        Object key = new Object();        
        Configuration value = new Configuration() {};
        
        config.storeConfiguration(key, value);
        
        assertEquals("storeConfiguration should store the data in its the " +
                     "internal map", value, internalConfigMap.get(key));
        
        NullPointerException npe = null;
        try {
            config.storeConfiguration(null,value);
        } catch (NullPointerException e) {
            npe = e;
        }
        assertNotNull("calling storeConfiguration with a null key should " +
                      "throw a NullPointerexception", npe);
    }
    
    /**
     * Tests the retrieveConfiguration method
     * @throws Exception if an error occurs
     */
    public void testRetrieveConfiguration() throws Exception {
        Object key = new Object();                        
        Configuration value = new Configuration() {};
        
        internalConfigMap.put(key, value);
        
        // ensure we can retrieve the correct value
        assertEquals("retrieveConfiguration should return the stored value", 
                     value, config.retrieveConfiguration(key));
        
        // ensure null is returned if the value has not been stored.
        assertNull("retrieveConfiguration should return null if no value " +
                   "has been stored", 
                   config.retrieveConfiguration(new Object()));                       
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 24-Jul-03	247/1	doug	VBM:2003072401 Simple XMLPipelineConfiguration implementation

 ===========================================================================
*/
