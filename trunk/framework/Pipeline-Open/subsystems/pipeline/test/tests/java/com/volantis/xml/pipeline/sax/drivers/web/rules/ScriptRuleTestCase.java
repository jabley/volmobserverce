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

import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.xml.pipeline.sax.drivers.web.Script;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;

/**
 * Test cases for {@link ScriptRule}.
 */
public class ScriptRuleTestCase
        extends DynamicRuleTestAbstract {

    /**
     * Ensure that rule detects missing attributes.
     */
    public void testMissing() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        DynamicElementRule rule = new ScriptRule();
        doStartFailure(rule,
                "Script tag should have the 'ref' parameter set. " +
                "Value is: ref='null'");
    }

    /**
     * Ensure that rule works properly.
     */
    public void testNormal() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        final String ref = "ref";
        contextMock.fuzzy.setProperty(Script.class,
                mockFactory.expectsAny(), Boolean.FALSE).does(new MethodAction() {
                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        Script script = (Script) event.getArgument("value",
                                Object.class);

                        assertEquals(ref, script.getRef());

                        return null;
                    }
                });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        DynamicElementRule rule = new ScriptRule();

        addAttribute("ref", ref);

        rule.startElement(dynamicProcessMock, elementName, attributes);
    }

}
