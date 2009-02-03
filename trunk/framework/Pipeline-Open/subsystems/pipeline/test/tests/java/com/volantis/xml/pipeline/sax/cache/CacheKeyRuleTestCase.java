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
 * TestCase for CacheKeyRule
 */
public class CacheKeyRuleTestCase
        extends DynamicRuleTestAbstract {

    /*
     * Test: key attribute set. Properties found on stack.
     */
    public void testAttributesOK() throws Exception {

        final String keyValue = "keyValue";
        final CacheKey cacheKey = new CacheKey();
        cacheKey.addKey(keyValue);

        addAttribute("value", keyValue);

        CacheProperties properties = new CacheProperties();

        // Prepare mocks
        contextMock.expects.findObject(CacheProperties.class).
                returns(properties).any();

        DynamicElementRule rule = new CacheKeyRule();
        EndElementAction action = (EndElementAction) rule.
                startElement(dynamicProcessMock, elementName, attributes);
        assertNull(action);

        rule.endElement(dynamicProcessMock, elementName, action);

        assertNotNull(properties);
        assertNotNull(properties.getCacheKey());
        assertEquals("Key should match", cacheKey, properties.getCacheKey());
    }

    /*
     * Test: key attribute set. Properties not found on the stack.
     */
    public void testAttributesFail() throws Exception {

        final String keyValue = "keyValue";
        addAttribute("value", keyValue);

        // Prepare mocks
        contextMock.expects.findObject(CacheProperties.class).
                returns(null).any();
        dynamicProcessMock.fuzzy.error(mockFactory.
                expectsInstanceOf(SAXParseException.class));

        DynamicElementRule rule = new CacheKeyRule();
        EndElementAction action = (EndElementAction) rule.
                startElement(dynamicProcessMock, elementName, attributes);
        assertNull(action);
        rule.endElement(dynamicProcessMock, elementName, action);
    }
}
