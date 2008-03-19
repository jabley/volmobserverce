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

package com.volantis.osgi.j2ee.bridge.http.service;

import com.volantis.synergetics.osgi.j2ee.bridge.http.HttpBridge;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A {@link HttpBridge} service that dispatches the request to the service
 * registry.
 *
 * <p>This is a Declarative Services Component, look in the
 * <code>osgi.bnd</code> bundle definition file at the Service-Component
 * header and at the <code>OSGi Declarative Services</code> Wiki page for more
 * information about how the component declarations affect how they behave.</p>
 *
 * <p>This component references the following service(s):</p>
 * <ul>
 * <li>{@link ServletRegistry} - required / static</p>
 * </ul>
 */
public class HttpBridgeService
        implements HttpBridge {

    /**
     * The registry.
     */
    private ServletRegistry registry;

    public HttpBridgeService() {
    }

    public HttpBridgeService(ServletRegistry registry) {
        this.registry = registry;
    }

    // Javadoc inherited.
    public void dispatch(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        registry.dispatchRequest(request, response);
    }

    /**
     * Invoked when {@link ServletRegistry} service becomes available.
     *
     * @param registry The {@link ServletRegistry} service.
     */
    protected void setServletRegistry(ServletRegistry registry) {
        this.registry = registry;
    }
}
