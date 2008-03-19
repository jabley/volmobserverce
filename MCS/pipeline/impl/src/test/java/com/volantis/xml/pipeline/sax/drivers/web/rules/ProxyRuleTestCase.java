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

import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;

/**
 * Test cases for {@link ProxyRule}.
 */
public class ProxyRuleTestCase
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
        DynamicElementRule rule = new ProxyRule();
        doStartFailure(rule,
                "Proxy tag should have the 'ref' parameter set. " +
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
        contextMock.expects.setProperty(ProxyManager.class, ref, false);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        DynamicElementRule rule = new ProxyRule();

        addAttribute("ref", ref);

        rule.startElement(dynamicProcessMock, elementName, attributes);
    }

}
