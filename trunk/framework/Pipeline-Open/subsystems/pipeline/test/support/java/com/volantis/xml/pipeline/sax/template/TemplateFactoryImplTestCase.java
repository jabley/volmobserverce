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
package com.volantis.xml.pipeline.sax.template;

import com.volantis.xml.namespace.MutableExpandedName;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.config.MockDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.impl.template.TemplateFactoryImpl;

/**
 * TestCase for the {@link TemplateFactoryImpl} class
 */
public class TemplateFactoryImplTestCase
        extends TemplateFactoryTestAbstract {

    /**
     * Instance of the class being tested
     */
    protected TemplateFactory factory;

    /**
     * Dynamic process configuration
     */
    protected DynamicProcessConfiguration configuration;

    /**
     * Creates a new <code>TemplateFactoryImplTestCase</code> instance
     * @param name the name of the test
     */
    public TemplateFactoryImplTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        factory = new TemplateFactoryImpl();
        configuration = new MockDynamicProcessConfiguration();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        factory = null;
        configuration = null;
    }

    /**
     * Tests the {@link com.volantis.xml.pipeline.sax.impl.template.TemplateFactoryImpl#getRuleConfigurator}
     * method
     * @throws Exception if an error occurs
     */
    public void testGetRuleConfigurator() throws Exception {
        DynamicRuleConfigurator configurator =
                factory.getRuleConfigurator();

        configurator.configure(configuration);

        MutableExpandedName expandedName = new MutableExpandedName();
        expandedName.setNamespaceURI(Namespace.TEMPLATE.getURI());
        
        // ensure the apply rule has been added
        expandedName.setLocalName("apply");
        assertNotNull("apply rule has not been added",
                      configuration.getRule(expandedName));
        
        // ensure the binding rule has been added
        expandedName.setLocalName("binding");
        assertNotNull("binding rule has not been added",
                      configuration.getRule(expandedName));
        
        // ensure the definition rule has been added
        expandedName.setLocalName("definition");
        assertNotNull("definition rule has not been added",
                      configuration.getRule(expandedName));
        
        // ensure the documentation rule has been added
        expandedName.setLocalName("documentation");
        assertNotNull("documentation rule has not been added",
                      configuration.getRule(expandedName));
        
        // ensure the parameter rule has been added
        expandedName.setLocalName("parameter");
        assertNotNull("parameter rule has not been added",
                      configuration.getRule(expandedName));
        
        // ensure the value rule has been added
        expandedName.setLocalName("value");
        assertNotNull("value rule has not been added",
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
