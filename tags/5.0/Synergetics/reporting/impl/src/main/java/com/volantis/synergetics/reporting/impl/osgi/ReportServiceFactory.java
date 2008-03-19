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

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;

/**
 * This class is a ServiceFactory so it can create new instances the
 * ReportService for each bundle that requests one. This behaviour is essential
 * as it allows all references to a bundle that is unloaded to be dropped by
 * the ReportService. All references are cached in the ReportService instance
 * created for a bundle. When the bundle goes all those references go with it.
 */
public class ReportServiceFactory implements ServiceFactory {

    /**
     * The report handler to use. This is a special handler that sends reported
     * events through the OSGi event mechansim
     */
    private final EventProducerReportHandler reportHandler;

    /**
     * Construct the ReportServiceFactory with the appropriate report handler.
     *
     * @param reportHandler
     */
    ReportServiceFactory(EventProducerReportHandler reportHandler) {
        this.reportHandler = reportHandler;
    }

    // Javadoc inherited
    public Object getService(Bundle bundle,
                             ServiceRegistration serviceRegistration) {
        Object result = null;
        if (null != reportHandler) {
            result = new DefaultReportService(reportHandler);
        }
        return result;
    }

    // Javadoc inherited
    public void ungetService(Bundle bundle,
                             ServiceRegistration serviceRegistration,
                             Object object) {
        // we shouldn't need to do anything here.
    }
}
