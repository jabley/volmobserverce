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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.logging.impl;

import com.volantis.synergetics.log.Log4jHelper;
import com.volantis.synergetics.log.ServletContextConfigurationResolver;

import javax.servlet.ServletContext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

/**
 * A component activator.
 *
 */
public class LoggingActivator {

    /**
     * The default location of the log4j configuration file for MAP.
     */
    private static final String DEFAULT_LOG4J_LOCATION =
            "/WEB-INF/map-log4j.xml";

    /**
     * The location of the predefined log4j configuration file resource for
     * MAP. This is used if a location was not specified in the web.xml, and
     * if the default location does not exist. The predefined location is the
     * last resort fallback.
     */
    private static final String PREDEFINED_LOG4J_LOCATION =
            "com/volantis/map/webservice/map-log4j-predefined.xml";


    /**
     * The OSGI bundle context.
     */
    private BundleContext context;

 
    protected void activate(final ComponentContext componentContext) {
        context = componentContext.getBundleContext();
        initializeLogging();
     }

    private void initializeLogging() {
        final ServiceReference serviceReference =
            context.getServiceReference("javax.servlet.ServletContext");
        final ServletContext servletContext =
            (ServletContext) context.getService(serviceReference);
        final String log4jConfigFile =
            servletContext.getInitParameter("map.log4j.config.file");
        Log4jHelper.initializeLogging(log4jConfigFile,
            new ServletContextConfigurationResolver(servletContext),
            DEFAULT_LOG4J_LOCATION, PREDEFINED_LOG4J_LOCATION);
    }


    protected void deactivate(ComponentContext context) {
         this.context = null;
    }

 
 
}
