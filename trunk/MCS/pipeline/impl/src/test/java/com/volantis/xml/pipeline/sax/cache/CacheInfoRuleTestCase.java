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

import com.volantis.xml.pipeline.sax.drivers.web.rules.DynamicRuleTestAbstract;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;
import org.xml.sax.SAXParseException;

/**
 * TestCase for CacheInfoRule
 */
public class CacheInfoRuleTestCase
        extends DynamicRuleTestAbstract {

    /*
     * Test: properties found on the stack.
     */
    public void testAttributesOK() throws Exception {

        CacheProperties properties = new CacheProperties();

        // Prepare mocks
        contextMock.expects.findObject(CacheProperties.class).
                returns(properties).any();

        DynamicElementRule rule = new CacheInfoRule();
        Object obj = rule.startElement(dynamicProcessMock, elementName,
                attributes);

        assertNull(obj);

        rule.endElement(dynamicProcessMock, elementName, attributes);
        assertNotNull(properties);
    }

    /*
     * Test: properties can not be found on stack
     */
    public void testAttributesFail() throws Exception {

        // Prepare mocks
        contextMock.expects.findObject(CacheProperties.class).
                returns(null).any();

        dynamicProcessMock.fuzzy.error(
                mockFactory.expectsInstanceOf(SAXParseException.class));

        DynamicElementRule rule = new CacheInfoRule();
        EndElementAction action = (EndElementAction) rule.startElement(
                dynamicProcessMock, elementName, attributes);
        assertNull(action);
        rule.endElement(dynamicProcessMock, elementName, action);
    }

    /*
     * Test: properties with cacheKey attribute set found on the stack.
     */
    public void testAttributesFailCacheKey() throws Exception {
        //if cacheKey is set, then cacheInfo element is not expected
        CacheProperties properties = new CacheProperties();
        CacheKey cacheKey = new CacheKey();
        cacheKey.addKey("key313");
        properties.setCacheKey(cacheKey);

        // Prepare mocks
        contextMock.expects.findObject(CacheProperties.class).
                returns(properties).any();

        dynamicProcessMock.fuzzy.error(mockFactory.
                expectsInstanceOf(SAXParseException.class));


        DynamicElementRule rule = new CacheInfoRule();
        EndElementAction action = (EndElementAction) rule.
                startElement(dynamicProcessMock, elementName, attributes);

        assertNull(action);

        rule.endElement(dynamicProcessMock, elementName, action);
        assertNotNull(properties);
    }
}
