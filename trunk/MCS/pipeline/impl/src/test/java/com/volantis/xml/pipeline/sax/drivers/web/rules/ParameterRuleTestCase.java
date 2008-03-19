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
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestParameter;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;

/**
 * Test cases for {@link ParameterRule}.
 */
public class ParameterRuleTestCase
        extends HttpMessageEntityRuleTestAbstract {

    /**
     * This method tests the method public void writeOpen (  )
     */
    public void testSimple() throws Exception {
        doTest("name value", "value value", "from value", "target value");
    }

    /**
     * This method tests the method public void writeOpen (  )
     */
    public void testNoName() throws Exception {
        doTest(null, "value value", "from value","target value");
    }

    /**
     * Perform a test to ensure that the parameter is initialised properly.
     *
     * @param name  The name.
     * @param value The value.
     * @param from  The from value.
     * @throws Exception
     */
    private void doTest(
            final String name, final String value, final String from, final String target)
            throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        WebRequestParameter parameter = new WebRequestParameter();
        entityFactoryMock.expects.createParameter().returns(parameter);
        HTTPMessageEntities entities = new SimpleHTTPMessageEntities();
        entityFactoryMock.expects.createHTTPMessageEntities().returns(entities);

        expectAddSimpleElementProcess(parameter);

        contextMock.expects.getProperty(WebRequestParameter.class).returns(
                null);
        contextMock.expects.setProperty(WebRequestParameter.class,
                entities, false);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        addAttribute("name", name);
        addAttribute("value", value);
        addAttribute("from", from);
        addAttribute("target", target);

        DynamicElementRule rule = new ParameterRule(entityFactoryMock);
        XMLProcess process = (XMLProcess) rule.startElement(dynamicProcessMock,
                elementName, attributes);

        dynamicProcessMock.expects.removeProcess(process);

        rule.endElement(dynamicProcessMock, elementName, process);

        assertNotNull(parameter);

        if (name == null) {
            assertEquals("Name value should match", from,
                    parameter.getName());
        } else {
            assertEquals("Name value should match", name,
                    parameter.getName());
        }
        assertEquals("Value value should match", value,
                parameter.getValue());
        assertEquals("From value should match", from,
                parameter.getFrom());
        assertEquals("Target value should match", target,
                parameter.getTarget());
    }
}
