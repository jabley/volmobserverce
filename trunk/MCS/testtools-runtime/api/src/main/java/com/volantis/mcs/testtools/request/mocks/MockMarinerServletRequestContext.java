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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.testtools.request.mocks;

import com.volantis.mcs.servlet.MarinerServletRequestContext;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Supporting class for the expression unit tests
 */
public class MockMarinerServletRequestContext
        extends MarinerServletRequestContext {
    /**
     * The (mock) servlet request represented by this request context
     */
    HttpServletRequest servletRequest;
    private final HttpServletResponse servletResponse;

    /**
     * A mock representation of device policy values.
     */
    Map devicePolicies = new HashMap();

    /**
     * The value that the {@link #getAncestorRelationship} method returns.
     */
    int deviceAncestorRelationship;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param servletRequest
     * @param servletResponse
     */
    public MockMarinerServletRequestContext(HttpServletRequest servletRequest,
                                            HttpServletResponse servletResponse) {
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
    }

    // javadoc inherited
    public HttpServletRequest getHttpRequest() {
        return servletRequest;
    }

    // javadoc inherited
    public String [] getParameterValues(String name) {
        return servletRequest.getParameterValues(name);
    }

    // javadoc inherited
    public String getParameter(String name) {
        return servletRequest.getParameter(name);
    }

    /**
     * Add a device policy to this MarinerServletRequestContext.
     * @param name The policy name
     * @param value The policy value
     */
    public void addDevicePolicy(String name, String value) {
        devicePolicies.put(name, value);
    }

    // javadoc inherited
    public String getDevicePolicyValue(String name) {
        return (String) devicePolicies.get(name);
    }

    // javadoc inherited
    public int getAncestorRelationship(String deviceName) {
        return deviceAncestorRelationship;
    }

    /**
     * Set the value that the {@link #getAncestorRelationship} returns
     * @param deviceAncestorRelationship the value
     */
    public void setAncestorRelationship(int deviceAncestorRelationship) {
        this.deviceAncestorRelationship = deviceAncestorRelationship;
    }

    public ServletResponse getResponse() {
        return servletResponse;
    }
}
