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

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpService;

/**
 * The factory for the {@link HttpService}.
 *
 * <p>This is needed because an instance of the service needs to be created for
 * each bundle that requests the service in order to ensure access to resources
 * within that bundle.</p>
 */
public class HttpServiceFactory
        implements ServiceFactory {

    /**
     * The servlet registry to which all the services delegate to.
     */
    private final ServletRegistry registry;

    /**
     * The service factory
     *
     * @param registry The servlet registry.
     */
    public HttpServiceFactory(ServletRegistry registry) {
        this.registry = registry;
    }

    // Javadoc inherited.
    public Object getService(Bundle bundle, ServiceRegistration registration) {
        return new HttpServiceImpl(registry, bundle);
    }

    // Javadoc inherited.
    public void ungetService(
            Bundle bundle, ServiceRegistration registration, Object service) {
        registry.destroyService(bundle);
    }
}
