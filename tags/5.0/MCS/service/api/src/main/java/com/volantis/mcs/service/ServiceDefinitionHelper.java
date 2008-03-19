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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Helper method to store at retrived {@link ServiceDefinition} instances
 * from a request
 */
public class ServiceDefinitionHelper {

     /**
     * To be used as a key when storing/retrieving implematations of this
     * from data structures
     */
    static final String SERVICE_DEFINITION_KEY =
            ServiceDefinition.class.getName();

    /**
     * Helper method to retrieve a ServiceDefinition from a
     * {@link HttpServletRequest} instance.
     * @param request the request
     * @return a {@link ServiceDefinition} instance.
     */
    public static ServiceDefinition retrieveService(ServletRequest request) {
        return (ServiceDefinition) request.getAttribute(SERVICE_DEFINITION_KEY);
    }

    /**
     * Helper method to store a {@link ServiceDefinition} away in a
     * {@link ServletRequest} instance.
     * @param request the request
     * @param service a ServiceDefintion
     */
    public static void storeService(ServletRequest request,
                                             ServiceDefinition service) {
        request.setAttribute(SERVICE_DEFINITION_KEY ,service);
    }
}
