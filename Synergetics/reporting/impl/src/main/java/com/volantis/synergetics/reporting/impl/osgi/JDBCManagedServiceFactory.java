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
package com.volantis.synergetics.reporting.impl.osgi;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.event.EventHandler;
import org.osgi.framework.BundleContext;

/**
 * A managedServiceFactory that handles JDBCEventHandlers.
 */
public class JDBCManagedServiceFactory extends AbstractManagedServiceFactory {

    /**
     * Creates a new JDBC MS factory.
     *
     * @param context the bundle context
     */
    public JDBCManagedServiceFactory(final BundleContext context) {
        super(context);
    }

    // Javadoc inherited
    public String getName() {
        return "Volantis Synergetics: Reporting: JDBC Managed Service Factory";
    }

    /**
     * Create and configure a new JDBCReportHandler
     */
    // Rest of Javadoc inherited
    protected ReportEventHandler createEventHandler(Dictionary configuration)
        throws ConfigurationException {
        return new JDBCReportHandler(configuration);
    }
}
