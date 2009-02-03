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

import com.volantis.synergetics.reporting.ReportService;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ManagedServiceFactory;

/**
 * This class is reposnsible for activating the Reporting bundle. This includes
 * creating an registering a ReportServiceFactory and a couple of
 * ManagedServiceFactories to handle the listeners.
 */
public class ReportServiceActivator implements BundleActivator {

    /**
     * The report handler is created when this BundleActivator is started as it
     * needs a bundle context to register itself.
     */
    private EventProducerReportHandler reportHandler = null;

    /**
     * List of ServiceRegistrations to clean up when stop is called
     */
    private List registrations = new ArrayList();

    // Javadoc inherited
    public void start(BundleContext bundleContext) throws Exception {
        // This should only be called once when this bundle starts
        // so we create a new instance.
        this.reportHandler = new EventProducerReportHandler(bundleContext);
        // register the ReportServiceFactory with the framework. This uses
        // Dictionary objects (which are very old)
        //noinspection UseOfObsoleteCollectionType
        registrations.add(bundleContext.registerService(ReportService.class.getName(),
                                      new ReportServiceFactory(reportHandler),
                                      new Hashtable()));

        // register the managed service factory responsible for the
        // Log4J ReportEventHandler
        Dictionary initDict = new Hashtable();
        initDict.put(Constants.SERVICE_PID,
                     Log4JReportHandler.CONFIG_PROPERTIES_PREFIX);
        registrations.add(bundleContext.registerService(
            ManagedServiceFactory.class.getName(),
            new Log4JManagedServiceFactory(bundleContext), initDict));

        // register the managed service factory responsible for the
        // JDBC ReportEventHandler
        initDict.put(Constants.SERVICE_PID,
                     JDBCReportHandler.CONFIG_PROPERTIES_PREFIX);
        registrations.add(bundleContext.registerService(
            ManagedServiceFactory.class.getName(),
            new JDBCManagedServiceFactory(bundleContext), initDict));

    }

    // Javadoc inherited
    public void stop(BundleContext bundleContext) throws Exception {
        // run through our registrations and unregister them
        Iterator it = registrations.iterator();
        while (it.hasNext()) {
            ServiceRegistration reg = (ServiceRegistration) it.next();
            reg.unregister();
        }
        // clear the reference just to make sure
        reportHandler.dispose();
    }
}
