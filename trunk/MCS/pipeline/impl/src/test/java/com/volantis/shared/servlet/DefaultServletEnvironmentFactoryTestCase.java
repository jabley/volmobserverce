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
package com.volantis.shared.servlet;

import com.volantis.shared.environment.EnvironmentInteraction;

/**
 * TestCase for the <code>efaultServletEnvironmentFactory</code> class 
 */ 
public class DefaultServletEnvironmentFactoryTestCase 
    extends ServletEnvironmentTestAbstract {

    /**
     * Instance of the class being tested
     */ 
    private ServletEnvironmentFactory factory;
    
    /**
     * Creates a new DefaultServletEnvironmentFactoryTestCase instance
     * @param name the name of the test
     */ 
    public DefaultServletEnvironmentFactoryTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        factory = new DefaultServletEnvironmentFactory();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the createEnvironment method
     * @throws Exception if an exception occurs
     */ 
    public void testCreateEnvironment() throws Exception {
        ServletEnvironment environment = factory.createEnvironment(null);
        assertTrue("createEnvironment should create a " + "" +
                   "ServletEnvironment instance",
                    environment instanceof ServletEnvironment);
    }
    
    /**
     * Tests the createEnvironmentInteraction method
     * @throws Exception if an exception occurs
     */          
    public void test() throws Exception {
        EnvironmentInteraction interaction = 
                factory.createEnvironmentInteraction(null, 
                                                     null, 
                                                     null, 
                                                     null, 
                                                     null);        
        assertEquals("createEnvironmentInteraction should create a " +
                     "ServletEnvironmentInteractionImpl instance",
                     ServletEnvironmentInteractionImpl.class,
                     interaction.getClass());        
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	271/1	doug	VBM:2003073002 Implemented various environment fatories

 ===========================================================================
*/
