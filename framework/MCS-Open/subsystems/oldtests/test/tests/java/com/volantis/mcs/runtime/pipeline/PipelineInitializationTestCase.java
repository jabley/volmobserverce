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
package com.volantis.mcs.runtime.pipeline;


import com.volantis.mcs.runtime.PluggableAssetTranscoderManager;
import com.volantis.mcs.runtime.configuration.MCSConfiguration;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.PipelineConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.cache.CacheProcessConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDriverConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;

/**
 * TestCase for the PipelineInitialization method.
 */ 
public class PipelineInitializationTestCase extends TestCaseAbstract {
    
    /**
     * Creates a new PipelineInitializationTestCase instance
     * @param name the name
     */ 
    public PipelineInitializationTestCase(String name) {
        super(name);
    }
    
    /**
     * Instance of the class being tested.
     */ 
    private PipelineInitialization pi;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();


        MarinerConfiguration marinerConfiguration = new MarinerConfiguration();
        PipelineConfiguration pipelineConfiguration =
                    new PipelineConfiguration();

        pipelineConfiguration.setWsDriverConfiguration(
                    new WSDriverConfiguration());
        
        marinerConfiguration.setPipelineConfiguration(pipelineConfiguration);
        MCSConfiguration mcsConfiguration =
                    new MCSConfiguration(marinerConfiguration);

        // create the pipeline intitialization
        pi = new PipelineInitialization(marinerConfiguration,
                                        mcsConfiguration,
                                        new PluggableAssetTranscoderManager(""));
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        
        pi = null;
    }

    /**
     * Tests the getPipelineFactory() Method.
     * @throws Exception
     */ 
    public void testGetPipelineFactory() throws Exception {
        XMLPipelineFactory factory = pi.getPipelineFactory();


        assertTrue("getPipelineFactory() should return an instance " +
                   " of the MCSPipelineFactory class",
                   factory.getClass().getName().equals(
                               "com.volantis.mcs.runtime.pipeline." +
                               "PipelineInitialization$MCSPipelineFactory"));
    }
    
    /**
     * Tests that the XMLPipelineFactory returned from the
     * PipelineInitialization is valid
     * @throws Exception if an error occurs
     */
    public void testGetPipelineConfiguration() throws Exception {
        // invoke the method we are testing and retrieve the configuration 
        XMLPipelineConfiguration config =
                    pi.getPipelineFactory().createPipelineConfiguration();

        // ensure that a DynamicProcessConfiguration was added
        assertNotNull("DynamicProcessConfiguration was not added to " +
                      "Pipeline configuration",
                      config.retrieveConfiguration(
                              DynamicProcessConfiguration.class));

        // ensure the the Web Driver configuration was added
        assertNotNull("WebDriverConfiguration was not added to " +
                   "Pipeline configuration",
                   config.retrieveConfiguration(
                           WebDriverConfiguration.class));
        
        // ensure the the Cache configuration was added
        assertNotNull("CacheProcessConfiguration was not added to " +
                      "Pipeline configuration",
                      config.retrieveConfiguration(
                              CacheProcessConfiguration.class));
        
        // ensure the the Web Service configuration was added
        assertNotNull("WebServiceConfiguration was not added to " +
                      "Pipeline configuration",
                      config.retrieveConfiguration(
                              WSDriverConfiguration.class));

    }
    
    /**
     * Tests the {@link PipelineInitialization#defineNamespaceURIs} method
     * @throws Exception if an error occurs
     */ 
    public void testDefineNamespaceURIs() throws Exception {
        // Check some default form URIs
        assertEquals("CDM namespace URI not as",
                     "http://www.volantis.com/xmlns/marlin-cdm",
                     Namespace.literal("cdm").getURI());

        assertEquals("PIPELINE namespace URI not as",
                     "http://www.volantis.com/xmlns/marlin-pipeline",
                     Namespace.PIPELINE.getURI());

        // Check the special form URIs
        assertEquals("REQUEST namespace URI not as",
                     "http://www.volantis.com/xmlns/mariner/request",
                     Namespace.literal("request").getURI());

        assertEquals("DEVICE namespace URI not as",
                     "http://www.volantis.com/xmlns/mariner/device",
                     Namespace.literal("device").getURI());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/7	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Aug-03	954/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 04-Aug-03	880/4	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 01-Aug-03	880/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 01-Aug-03	923/1	philws	VBM:2003080103 Fix the DEVICE and REQUEST namespace URIs

 28-Jul-03	874/1	doug	VBM:2003072310 Refactored usages of XMLQName to ExpandedName

 25-Jun-03	473/1	philws	VBM:2003061913 Renaming of pipeline:includeURI, renaming of vt:include to vt:usePipeline and intro of new vt:include

 17-Jun-03	366/3	doug	VBM:2003041502 Ensured Test Case extended TestCaseAbstract

 16-Jun-03	366/1	doug	VBM:2003041502 Integration with pipeline JSPs

 ===========================================================================
*/
