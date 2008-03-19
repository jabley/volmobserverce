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

import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.drivers.web.rules.DynamicRuleTestAbstract;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.shared.time.Period;
import org.xml.sax.SAXParseException;

/**
 * TestCase for CacheControlRule
 */
public class CacheControlRuleTestCase
        extends DynamicRuleTestAbstract {


    public void testCalculateTimeToLiveWitNullReturnsIndefinitely()  throws Exception {
        assertEquals(Period.INDEFINITELY,
                CacheControlRule.calculateTimeToLive(null));
    }

    public void testCalculateTimeToLiveWithLiveForeverReturnsIndefinitely()  throws Exception {
        assertEquals(Period.INDEFINITELY,
                CacheControlRule.calculateTimeToLive(
                        CacheControl.LIVE_FOREVER));
    }


    public void testCalculateTimeToLiveWithNonIntegerThrowsNumberFormatException()  throws Exception {
        try {
            CacheControlRule.calculateTimeToLive("hello");
            fail("Shouldn't have parsed \"hello\" as a valid number.");
        } catch (NumberFormatException expected) {
            // OK
        }
    }

    public void testCalculateTimeToLiveWithValidIntegerValue()  throws Exception {
        assertEquals(Period.inMilliSeconds(60000),
                CacheControlRule.calculateTimeToLive("60"));
    }

    /*
     * Test: attribute set. Properties found on the stack.
     */
    public void testAttributesOK() throws Exception {

        final CacheControl cacheControl = new CacheControl();
        final String timeToLive = "997";
        cacheControl.setTimeToLive(
            CacheControlRule.calculateTimeToLive(timeToLive));

        addAttribute("timeToLive", timeToLive);

        CacheProperties properties = new CacheProperties();

        // Prepare mocks
        contextMock.expects.findObject(CacheProperties.class).
                returns(properties).any();

        DynamicElementRule rule = new CacheControlRule();
        XMLProcess process = (XMLProcess) rule.
                startElement(dynamicProcessMock, elementName, attributes);

        assertNull(process);

        rule.endElement(dynamicProcessMock, elementName, process);

        assertNotNull(properties);
        assertNotNull(properties.getCacheControl());
        assertEquals(cacheControl, properties.getCacheControl());
    }

    /*
     * Test: attribute set but properties not found.
     */
    public void testAttributesFail() throws Exception {

        final String timeToLive = "997";
        addAttribute("timeToLive", timeToLive);

        // Prepare mocks
        contextMock.expects.findObject(CacheProperties.class).
                returns(null).any();

        dynamicProcessMock.fuzzy.error(mockFactory.
                expectsInstanceOf(SAXParseException.class));

        DynamicElementRule rule = new CacheControlRule();

        Object obj = rule.startElement(dynamicProcessMock, elementName,
                attributes);

        assertNull(obj);
        rule.endElement(dynamicProcessMock, elementName, attributes);
    }
}
