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

package com.volantis.xml.pipeline.sax.drivers.web.rules;

import com.volantis.xml.pipeline.sax.drivers.web.Content;
import com.volantis.xml.pipeline.sax.drivers.web.ContentAction;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 * Test cases for {@link ContentRule}.
 */
public class ContentRuleTestCase
        extends DynamicRuleTestAbstract {

    /**
     * Ensure that missing attributes are handled properly.
     */
    public void testMissingAttributes() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        DynamicElementRule rule = new ContentRule();

        doStartFailure(rule,
                "Content tag should have 'action' and 'type' " +
                "parameter set. Values are: action='null', type='null'");
    }

    /**
     * Ensure that valid attributes are processed correctly.
     */
    public void testValidAttributes() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contextMock.expects.getProperty(Content.class).returns(null);
        contextMock.expects.setProperty(Content.class,
                Collections.EMPTY_MAP, false);

        contextMock.expects.getProperty(Content.class).returns(new HashMap());

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        DynamicElementRule rule = new ContentRule();

        doTest("ignore", "type-ignore", rule, ContentAction.IGNORE);

        doTest("consume", "type-consume", rule, ContentAction.CONSUME);
    }

    private void doTest(
            String actionAsString, String contentType,
            DynamicElementRule rule,
            final ContentAction contentAction) throws SAXException {
        attributes = new AttributesImpl();
        addAttribute("action", actionAsString);
        addAttribute("type", contentType);
        Map map = (Map) rule.startElement(dynamicProcessMock, elementName,
                        attributes);
        rule.endElement(dynamicProcessMock, elementName, map);
        Content content = new Content(contentType, contentAction);
        assertEquals(Collections.singletonMap(contentType, content), map);
    }
}
