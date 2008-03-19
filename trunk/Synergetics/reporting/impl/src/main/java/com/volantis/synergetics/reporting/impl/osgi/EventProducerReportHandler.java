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

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.reporting.DynamicReport;
import com.volantis.synergetics.reporting.ReportHandler;
import com.volantis.synergetics.reporting.impl.DefaultMetrics;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

/**
 * This is a report handler that will dispatch reports as OSGi Events. It is
 * exclusively used by the OSGi DefaultReportService.
 *
 * @noinspection UseOfObsoleteCollectionType
 */
public class EventProducerReportHandler implements ReportHandler {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(EventProducerReportHandler.class);

    /**
     * The service traker from which we get the EventAdmin
     */
    private final ServiceTracker tracker;

    /**
     * Construct with an OSGi bundle context.
     *
     * @param bundle the bundle context to use.
     */
    EventProducerReportHandler(BundleContext bundle) {
        this.tracker = new ServiceTracker(
            bundle, EventAdmin.class.getName(), null);
        this.tracker.open();
    }

    // Javadoc inherited
    public void report(Map metrics) {

        EventAdmin sa = (EventAdmin) tracker.getService();
        if (null != sa) {
            Hashtable dict = new Hashtable();

            // process the input metrics map and copy appropriate content to the
            // dictionary we will use for event signalling
            Iterator it = metrics.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (null != value) {
                    if (key instanceof String) {
                        //noinspection StringContatenationInLoop
                        dict.put(DefaultMetrics.REPORT_PROPERTIES_PREFIX + key,
                                 value);
                    } else if (key instanceof Class) {
                        dict.put(DefaultMetrics.REPORT_CLASS,
                                 ((Class) value).getName());
                    } else if (key instanceof DynamicReport) {
                        // subtypes exist only for DynamicReport instances.
                        dict.put(DefaultMetrics.REPORT_SUBTYPE, value);
                    } else {
                        LOGGER.warn("unknown-event-property", key);
                    }
                }
            }
            // post the event (asynchronous)
            sa.postEvent(new Event(DefaultMetrics.TOPIC, dict));
        }
    }

    /**
     * Release any resources used by this handler.
     */
    public void dispose() {
        tracker.close();
    }
}
