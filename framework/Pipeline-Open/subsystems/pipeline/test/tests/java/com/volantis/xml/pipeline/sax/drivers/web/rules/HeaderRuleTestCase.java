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

import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestHeader;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;

/**
 * Test cases for {@link HeaderRule}.
 */
public class HeaderRuleTestCase
        extends HttpMessageEntityRuleTestAbstract {

    /**
     * This method tests the method public void writeOpen (  )
     */
    public void testSimple() throws Exception {
        doTest("name value", "value value", "from value");
    }

    /**
     * This method tests the method public void writeOpen (  )
     */
    public void testNoName() throws Exception {
        doTest(null, "value value", "from value");
    }

    /**
     * Perform a test to ensure that the header is initialised properly.
     *
     * @param name  The name.
     * @param value The value.
     * @param from  The from value.
     * @throws Exception
     */
    private void doTest(
            final String name, final String value, final String from)
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        WebRequestHeader header = new WebRequestHeader();
        entityFactoryMock.expects.createHeader().returns(header);
        HTTPMessageEntities entities = new SimpleHTTPMessageEntities();
        entityFactoryMock.expects.createHTTPMessageEntities().returns(entities);

        expectAddSimpleElementProcess(header);

        contextMock.expects.getProperty(WebRequestHeader.class).returns(null);
        contextMock.expects.setProperty(WebRequestHeader.class,
                entities, false);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        addAttribute("name", name);
        addAttribute("value", value);
        addAttribute("from", from);

        DynamicElementRule rule = new HeaderRule(entityFactoryMock);
        XMLProcess process = (XMLProcess) rule.startElement(dynamicProcessMock,
                elementName, attributes);

        dynamicProcessMock.expects.removeProcess(process);

        rule.endElement(dynamicProcessMock, elementName, process);

        assertNotNull(header);

        if (name == null) {
            assertEquals("Name value should match", from,
                    header.getName());
        } else {
            assertEquals("Name value should match", name,
                    header.getName());
        }
        assertEquals("Value value should match", value,
                header.getValue());
        assertEquals("From value should match", from,
                header.getFrom());
    }
}
