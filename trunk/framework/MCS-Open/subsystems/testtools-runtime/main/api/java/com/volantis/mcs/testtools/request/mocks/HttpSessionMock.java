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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 19-May-2003  Sumit       VBM:2003041502 - Mock for use in Servlet/JSP 
 *                          test cases
 *
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.testtools.request.mocks;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.policy.values.PolicyValue;
import com.volantis.devrep.repository.api.devices.policy.values.PolicyValueFactory;
import com.volantis.mcs.servlet.MarinerServletSessionContext;
import com.volantis.mcs.testtools.request.stubs.HttpSessionStub;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Mock HttpSession that sets up a device 
 */
public class HttpSessionMock  extends HttpSessionStub {

    /**
     * Hashtable containing attributes
     */
    private static Hashtable attributes = new Hashtable();

    // Initialise and set up a valid MarinerSessionContext with a
    // dummy device so prevent NPEs
    static {
        HashMap policy = new HashMap();
        policy.put("protocol","XHTMLBasic");
        DefaultDevice testDevice =
            new DefaultDevice(
                "Test", policy,
                new PolicyValueFactory() {
                    public PolicyValue createPolicyValue(
                        DefaultDevice device, String policyName) {
                        return null;
                    }
                });
        final InternalDevice internalDevice =
            InternalDeviceFactory.getDefaultInstance().createInternalDevice(
                testDevice);
        MarinerServletSessionContext mSessionContext =
            new MarinerServletSessionContext(internalDevice);
        attributes.put(MarinerServletSessionContext.class.getName(),
                mSessionContext);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-May-05	7890/1	pduffin	VBM:2005042705 Committing extensive restructuring changes

 28-Apr-05	7922/1	pduffin	VBM:2005042801 Removed User and UserFactory classes
 18-May-05	7950/1	allan	VBM:2005041317 Some testcases for smart server

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4737/2	allan	VBM:2004062202 Restrict volantis initialization.

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 19-Jul-03	812/1	adrian	VBM:2003071609 Support session scope markup plugins

 ===========================================================================
*/
