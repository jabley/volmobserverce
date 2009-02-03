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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Dictionary;
import java.util.Enumeration;

/**
 * A {@link ServletConfig} that wraps a dictionary.
 */
public class ServletConfigImpl
        implements ServletConfig {

    /**
     * The name of the servlet.
     */
    private final String name;

    /**
     * The servlet context.
     */
    private final ServletContext servletContext;

    /**
     * The set of initialisation properties.
     */
    private final Dictionary properties;

    /**
     * Initialise.
     *
     * @param name           The name of the servlet.
     * @param servletContext The servlet context.
     * @param properties     The set of initialisation properties.
     */
    public ServletConfigImpl(
            String name, ServletContext servletContext, Dictionary properties) {
        this.name = name;
        this.servletContext = servletContext;
        this.properties = properties;
    }

    // Javadoc inherited.
    public String getServletName() {
        return name;
    }

    // Javadoc inherited.
    public ServletContext getServletContext() {
        return servletContext;
    }

    // Javadoc inherited.
    public String getInitParameter(String name) {
        return (String) properties.get(name);
    }

    // Javadoc inherited.
    public Enumeration getInitParameterNames() {
        return properties.keys();
    }
}
