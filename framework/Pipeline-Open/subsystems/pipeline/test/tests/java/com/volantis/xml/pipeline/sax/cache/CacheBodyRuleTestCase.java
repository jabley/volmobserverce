/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.Â  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.Â  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.cache;

import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.cache.body.CacheBodyOperationProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfigurationMock;
import com.volantis.xml.pipeline.sax.drivers.web.rules.DynamicRuleTestAbstract;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.DependencyContextMock;

/**
 * TestCase for CacheBodyRule
 */
public class CacheBodyRuleTestCase extends DynamicRuleTestAbstract {

    /*
     * Test: Cache properties with set all attributes can be found on
     * the stack, but cache with those attributes does not exist. 
     */
    public void testAttributesOK() throws Exception {

        DynamicElementRule rule = new CacheBodyRule();
        XMLPipeline pipeline = new XMLPipelineProcessImpl(contextMock);

        final String cacheName = "cache406";
        final String cacheKeyValue = "cacheKey406";

        //Create and set attributes for CacheProperties instance
        CacheKey cacheKey = new CacheKey();
        cacheKey.addKey(cacheKeyValue);
        CacheControl cacheControl = new CacheControl();
        cacheControl.setTimeToLive(CacheControlRule.calculateTimeToLive("997"));
        CacheProperties properties = new CacheProperties();
        properties.setCacheControl(cacheControl);
        properties.setCacheKey(cacheKey);
        properties.setCacheName(cacheName);

        // Prepare mocks for startElement method
        contextMock.expects.findObject(CacheProperties.class).
                returns(properties).any();

        final DependencyContextMock dependencyContextMock =
            new DependencyContextMock("dependencyContextMock", expectations);
        contextMock.expects.getDependencyContext().returns(
            dependencyContextMock).any();

        // VBM:2006120611 - using Mocks can be fragile. Needed to add new mock
        // and expectation to get this test to pass again.
        final XMLPipelineConfigurationMock pipelineConfigurationMock =
            new XMLPipelineConfigurationMock("pipelineConfigurationMock",
                expectations);
        pipelineConfigurationMock.expects.retrieveConfiguration(
                CacheProcessConfiguration.class).returns(
                new CacheProcessConfiguration() {
                    {
                        createCache(cacheName, "100", "0");
                    }
                });

        contextMock.expects.getPipelineConfiguration()
                .returns(pipelineConfigurationMock);

        dynamicProcessMock.expects.getPipeline().returns(pipeline);
        dynamicProcessMock.fuzzy.addProcess(mockFactory.
                expectsInstanceOf(CacheBodyOperationProcess.class));

        //startElement
        EndElementAction action = (EndElementAction) rule.
                startElement(dynamicProcessMock, elementName, attributes);
        assertNotNull(action);
        assertNotEquals(action, EndElementAction.DO_NOTHING);

        //Prepare mocks for endElement method
        dynamicProcessMock.expects.removeProcess().
                returns(new XMLPipelineProcessImpl(contextMock));

        //endElement
        rule.endElement(dynamicProcessMock, elementName, action);
    }

    /*
     * Test: cacheProperties can not be found. 
     * cache element has a cacheKey attribute set.
     */
    public void testAttributesNoProperties() throws Exception {

        DynamicElementRule rule = new CacheBodyRule();

        // Prepare mocks for startElement method
        contextMock.expects.findObject(CacheProperties.class).
                returns(null).any();

        //startElement    
        EndElementAction action = (EndElementAction) rule.
                startElement(dynamicProcessMock, elementName, attributes);

        assertNotNull(action);
        assertEquals(action, EndElementAction.DO_NOTHING);

        //endElement - no expectations
        rule.endElement(dynamicProcessMock, elementName, action);
    }
}
