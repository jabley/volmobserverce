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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.cache;

import com.volantis.shared.time.Period;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineProcessImpl;
import com.volantis.xml.pipeline.sax.cache.body.CacheBodyOperationProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfigurationMock;
import com.volantis.xml.pipeline.sax.drivers.web.rules.DynamicRuleTestAbstract;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;

import org.xml.sax.SAXParseException;

/**
 * TestCase for CacheRule
 */
public class CacheRuleTestCase
        extends DynamicRuleTestAbstract {
    private CacheProcessConfiguration cacheProcessConfig;
    private DynamicElementRule cacheRule;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        final XMLPipelineConfigurationMock pipelineConfigurationMock =
                new XMLPipelineConfigurationMock("pipelineConfigurationMock",
                        expectations);

        // Set the expectation that the PipelineConfiguration will be
        // requested.
        contextMock.expects.getPipelineConfiguration()
            .returns(pipelineConfigurationMock).any();

        cacheProcessConfig = new CacheProcessConfiguration();
        pipelineConfigurationMock.expects.storeConfiguration(
            CacheProcessConfiguration.class, cacheProcessConfig);
        pipelineConfigurationMock.expects.retrieveConfiguration(
            CacheProcessConfiguration.class).returns(cacheProcessConfig).any();

        dynamicProcessMock.getPipelineContext().getPipelineConfiguration().
            storeConfiguration(CacheProcessConfiguration.class,
                cacheProcessConfig);

        cacheRule = new CacheRule();
    }

    /*
    * Test: cache element without name attribute.
    */
    public void testNoCacheName() throws Exception {
        // no cache name attribute set

        dynamicProcessMock.fuzzy.error(mockFactory.
                expectsInstanceOf(SAXParseException.class)).returns().any();

        EndElementAction action = (EndElementAction) cacheRule.startElement(
                dynamicProcessMock, elementName, attributes);
        assertNotNull(action);
        assertEquals(action, EndElementAction.DO_NOTHING);
    }

    /*
     * Test: cache element without cacheKey attribute.
     */
    public void testAttributesNoCacheKey() throws Exception {
        final String cacheName = "cache107";
        cacheProcessConfig.createCache(cacheName, "4", "0");

        assertNotNull(
            cacheProcessConfig.getCache("cache107").getRootGroup());

        addAttribute("name", cacheName);

        // Prepare mocks
        contextMock.fuzzy.pushObject(mockFactory.
                expectsInstanceOf(CacheProperties.class), Boolean.FALSE);

        EndElementAction action = (EndElementAction) cacheRule.
                startElement(dynamicProcessMock, elementName, attributes);
        assertNotNull(action);
        assertNotEquals(action, EndElementAction.DO_NOTHING);

        // Prepare mocks
        contextMock.expects.popObject().returns(new CacheProperties());
        cacheRule.endElement(dynamicProcessMock, elementName, action);
    }

    /*
     * Test: cache element with both name and cacheKey attributes set.
     * CacheEntry for those attributes does not exist in the cache.
     */
    public void testAttributes() throws Exception {

        XMLPipeline pipeline = new XMLPipelineProcessImpl(contextMock);

        final String cacheName = "cache107";
        final String cacheKeyValue = "key107";

        CacheKey cacheKey = new CacheKey();
        cacheKey.addKey(cacheKeyValue);

        cacheProcessConfig.createCache(cacheName, "4", "0");

        // @todo peterca
        //cache.put(cacheKey, new CacheEntry(null, false));

        addAttribute("name", cacheName);
        addAttribute("key", cacheKeyValue);

        dynamicProcessMock.expects.getPipeline().returns(pipeline);
        dynamicProcessMock.fuzzy.addProcess(mockFactory.
                expectsInstanceOf(CacheBodyOperationProcess.class));

        EndElementAction action = (EndElementAction) cacheRule.
                startElement(dynamicProcessMock, elementName, attributes);
        assertNotNull(action);
        assertNotEquals(action, EndElementAction.DO_NOTHING);

        // Prepare mocks
        dynamicProcessMock.expects.removeProcess().
                returns(new XMLPipelineProcessImpl(contextMock));
        cacheRule.endElement(dynamicProcessMock, elementName, action);
    }

    /**
     * Tests if CacheRule correctly delegates the time to live and expire mode
     * values to the CacheBodyOperationProcess.
     */
    public void testTimeToLiveWithKeyAttr() throws Exception {

        final XMLPipeline pipeline = new XMLPipelineProcessImpl(contextMock);
        dynamicProcessMock.expects.getPipeline().returns(pipeline);

        final CacheBodyOperationProcess[] cacheBodyOperationProcess =
            new CacheBodyOperationProcess[1];

        dynamicProcessMock.fuzzy.addProcess(mockFactory.expectsInstanceOf(
            CacheBodyOperationProcess.class)).does(new MethodAction() {
                public Object perform(final MethodActionEvent event)
                        throws Throwable {
                    cacheBodyOperationProcess[0] =
                        (CacheBodyOperationProcess) event.getArguments()[0];
                    return null;
                }
            });

        final String cacheName = "cache108";
        cacheProcessConfig.createCache(cacheName, "4", "123");
        addAttribute("name", cacheName);
        addAttribute("key", "key108");

        cacheProcessConfig.setFixedExpiryMode(false);

        cacheRule.startElement(dynamicProcessMock, elementName, attributes);

        final CacheBodyOperationProcess process = cacheBodyOperationProcess[0];
        final CacheControl cacheControl = process.getCacheControl();
        assertEquals(Period.inSeconds(123), cacheControl.getTimeToLive());
        assertFalse(process.isFixedExpiryMode());
    }

    /**
     * Tests if CacheProcess with the right expiry mode is created when no
     * expiry mode attribute is specified on the cache element and
     * CacheProcessConfiguration is in fixed-age expiry mode.
     */
    public void testExpiryModeNoExpiryModeAttrNoKeyAttr() throws Exception {

        final CacheProperties[] cacheProperties = new CacheProperties[1];

        contextMock.fuzzy.pushObject(mockFactory.expectsInstanceOf(
            CacheProperties.class), Boolean.FALSE).does(new MethodAction() {
                public Object perform(final MethodActionEvent event)
                        throws Throwable {
                    cacheProperties[0] =
                        (CacheProperties) event.getArguments()[0];
                    return null;
                }
            });

        final String cacheName = "cache108";
        cacheProcessConfig.createCache(cacheName, "4", "123");
        addAttribute("name", cacheName);

        cacheRule.startElement(dynamicProcessMock, elementName, attributes);
        assertTrue(cacheProperties[0].isFixedExpiryMode());
    }

    /**
     * Tests if CacheProcess with the right expiry mode is created when no
     * expiry mode attribute is specified on the cache element and
     * CacheProcessConfiguration is in auto expiry mode.
     */
    public void testExpiryModeNoExpiryModeAttrNoKeyAttr2() throws Exception {

        final CacheProperties[] cacheProperties = new CacheProperties[1];

        contextMock.fuzzy.pushObject(mockFactory.expectsInstanceOf(
            CacheProperties.class), Boolean.FALSE).does(new MethodAction() {
                public Object perform(final MethodActionEvent event)
                        throws Throwable {
                    cacheProperties[0] = (CacheProperties) event.getArguments()[0];
                    return null;
                }
            });

        final String cacheName = "cache108";
        cacheProcessConfig.createCache(cacheName, "4", "123");
        addAttribute("name", cacheName);

        cacheProcessConfig.setFixedExpiryMode(false);

        cacheRule.startElement(dynamicProcessMock, elementName, attributes);
        assertFalse(cacheProperties[0].isFixedExpiryMode());
    }

    /**
     * Tests if CacheProcess with the right expiry mode is created when
     * expiry-mode attribute is set to "fixed-mode" on the cache element.
     */
    public void testExpiryModeNoKeyAttrFixedAge() throws Exception {

        final CacheProperties[] cacheProperties = new CacheProperties[1];

        contextMock.fuzzy.pushObject(mockFactory.expectsInstanceOf(
            CacheProperties.class), Boolean.FALSE).does(new MethodAction() {
                public Object perform(final MethodActionEvent event)
                        throws Throwable {
                    cacheProperties[0] = (CacheProperties) event.getArguments()[0];
                    return null;
                }
            });

        final String cacheName = "cache108";
        cacheProcessConfig.createCache(cacheName, "4", "123");
        addAttribute("name", cacheName);
        addAttribute("expiry-mode", "fixed-age");

        cacheRule.startElement(dynamicProcessMock, elementName, attributes);
        assertTrue(cacheProperties[0].isFixedExpiryMode());
    }

    /**
     * Tests if CacheProcess with the right expiry mode is created when
     * expiry-mode attribute is set to "auto" on the cache element.
     */
    public void testExpiryModeNoKeyAttrAuto() throws Exception {

        final CacheProperties[] cacheProperties = new CacheProperties[1];

        contextMock.fuzzy.pushObject(mockFactory.expectsInstanceOf(
            CacheProperties.class), Boolean.FALSE).does(new MethodAction() {
                public Object perform(final MethodActionEvent event)
                        throws Throwable {
                    cacheProperties[0] = (CacheProperties) event.getArguments()[0];
                    return null;
                }
            });

        final String cacheName = "cache108";
        cacheProcessConfig.createCache(cacheName, "4", "123");
        addAttribute("name", cacheName);
        addAttribute("expiry-mode", "auto");

        cacheRule.startElement(dynamicProcessMock, elementName, attributes);
        assertFalse(cacheProperties[0].isFixedExpiryMode());
    }
    
    
    public void testCalculateMaxWaitTimeWitNullReturnsDefault()  throws Exception {
        assertEquals(CacheBodyOperationProcess.DEFAULT_MAX_WAIT_TIME,
                CacheRule.calculateMaxWaitTime(null));
    }

    public void testCalculateMaxWaitTimeWithLiveForeverReturnsIndefinitely()  throws Exception {
        assertEquals(Period.INDEFINITELY,
                CacheRule.calculateMaxWaitTime(
                        CacheControl.LIVE_FOREVER));
    }


    public void testCalculateMaxWaitTimeWithInvalidIntegerThrowsException()  throws Exception {
        try {
            CacheRule.calculateMaxWaitTime("hello");
            fail("Shouldn't have parsed \"hello\" as a valid number.");
        } catch (NumberFormatException expected) {
            // OK
        }
        
        try {
            CacheRule.calculateMaxWaitTime("-5");
            fail("Shouldn't have parsed \"-5\" as a valid number.");
        } catch (IllegalArgumentException expected) {
            // OK
        }        
    }

    public void testCalculateMaxWaitTimeWithValidIntegerValue()  throws Exception {
        assertEquals(Period.inMilliSeconds(60000),
                CacheRule.calculateMaxWaitTime("60"));
    }
       
}
