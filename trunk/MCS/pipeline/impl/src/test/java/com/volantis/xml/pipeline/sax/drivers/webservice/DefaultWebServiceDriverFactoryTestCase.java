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
package com.volantis.xml.pipeline.sax.drivers.webservice;

import com.volantis.xml.namespace.MutableExpandedName;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.config.MockDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.impl.drivers.webservice.WebServiceDriverFactoryImpl;

/**
 * Test case for the {@link com.volantis.xml.pipeline.sax.impl.drivers.webservice.WebServiceDriverFactoryImpl} class
 */
public class DefaultWebServiceDriverFactoryTestCase
        extends WebServiceDriverFactoryTestAbstract {

    /**
     * Instance of the class being tested
     */
    protected WebServiceDriverFactory factory;

    /**
     * Dynamic process configuration
     */
    protected DynamicProcessConfiguration configuration;

    /**
     * Creates a new <code>DefaultWebServiceDriverFactoryTestCase</code> 
     * instance
     * @param name the name of the test
     */
    public DefaultWebServiceDriverFactoryTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        factory = new WebServiceDriverFactoryImpl();
        configuration = new MockDynamicProcessConfiguration();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        factory = null;
        configuration = null;
    }

    /**
     * Tests the {@link WebServiceDriverFactoryImpl#getRuleConfigurator}
     * method
     * @throws Exception if an error occurs
     */
    public void testGetRuleConfigurator() throws Exception {
        DynamicRuleConfigurator configurator =
                factory.getRuleConfigurator();

        configurator.configure(configuration);

        MutableExpandedName expandedName = new MutableExpandedName();
        expandedName.setNamespaceURI(Namespace.WEB_SERVICE_DRIVER.getURI());
        
        // ensure the request rule has been added
        expandedName.setLocalName("request");
        assertNotNull("request rule has not been added",
                      configuration.getRule(expandedName));        
        
        // ensure the message rule has been added
        expandedName.setLocalName("message");
        assertNotNull("message rule has not been added",
                      configuration.getRule(expandedName));
        
        // ensure the wsdl-operation rule has been added
        expandedName.setLocalName("wsdl-operation");
        assertNotNull("wsdl-operation rule has not been added",
                      configuration.getRule(expandedName));
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
