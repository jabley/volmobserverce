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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.mcs.project.jdbc.JDBCPolicySource;

/**
 * This class tests ArgumentElement
 */
public class JdbcPoliciesElementTestCase
        extends PortletContextChildElementTestAbstract {

    /**
     * Test the element
     */
    public void testElement() throws Exception {

        JdbcPoliciesElement element = new JdbcPoliciesElement();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pageContextMock.expects.pushMCSIElement(element);
        pageContextMock.expects.popMCSIElement().returns(element);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        JdbcPoliciesAttributes attrs = new JdbcPoliciesAttributes();
        attrs.setName(POLICY_SOURCE);

        int result;
        result = element.elementStart(requestContextMock, attrs);
        assertEquals("Unexpected result from elementStart.",
                MCSIConstants.PROCESS_ELEMENT_BODY, result);

        result = element.elementEnd(requestContextMock, null);
        assertEquals("Unexpected result from elementEnd.",
                MCSIConstants.CONTINUE_PROCESSING, result);

        String policySource =
            ((JDBCPolicySource)parent.getPolicySource()).getName();
        assertEquals("BaseUrl has unexpected value",POLICY_SOURCE,policySource);
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
