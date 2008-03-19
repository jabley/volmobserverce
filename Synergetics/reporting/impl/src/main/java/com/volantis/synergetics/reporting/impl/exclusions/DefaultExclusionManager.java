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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.reporting.impl.exclusions;

import com.volantis.synergetics.reporting.ExclusionManager;
import com.volantis.synergetics.reporting.config.EventTypeExclusion;
import com.volantis.synergetics.reporting.config.MetricExclusion;
import com.volantis.synergetics.reporting.config.ReportExclusion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton pattern
 * Managing event exclusions for reports
 *
 */
public class DefaultExclusionManager extends ExclusionManager {


    /**
     * map report binding name -> List of ReportEventExcluder
     */
    private ConcurrentHashMap cache;

    /**
     *
     */
    public DefaultExclusionManager() {
        cache = new ConcurrentHashMap();
    }

    private Map getCache() {
        return cache;
    }

    /**
     * Add new connection strategy to cache
     * @param binding report binding name
     * @param excluder ReportEventExcluder
     */
    private void addExclussion(
            String binding,
            EventExcluder excluder) {
        ReportExclusions reportExclusions =
            (ReportExclusions) getCache().get(binding);
        if(reportExclusions == null) {
            reportExclusions = new ReportExclusions();
            getCache().put(binding, reportExclusions);
        }
        reportExclusions.addUniqueExclusion(excluder);
    }

    /**
     * Is event excluded from being reported
     * @param binding String report binding name
     * @return metrics Map report event metrics
     */
    public boolean isExcluded(String binding, Map metrics) {
        boolean ret = false;
        ReportExclusions reportExclusions =
            (ReportExclusions) getCache().get(binding);
        if(reportExclusions != null) {
            ret = reportExclusions.isExcluded(metrics);
        }
        return ret;
    }

    /**
     * Initialization of report exclusions
     * @param binding String report binding name
     * @param reportExclusion ReportExclusion (congifuration object)
     */
    public void initializeExclusions(String binding, ReportExclusion reportExclusion) {
        if(reportExclusion != null) {
            addEventTypeExclusions(reportExclusion, binding);

            addMetricExclusions(reportExclusion, binding);
        }

    }

    /**
     * Add the event type exclusions
     * @param binding String report binding name
     * @param reportExclusion ReportExclusion (congifuration object)
     */
    private void addEventTypeExclusions(ReportExclusion reportExclusion,
                                        String binding) {
        ArrayList eventTypeExlusions = reportExclusion.getEventTypeExclusions();
        if(eventTypeExlusions != null) {
            for(Iterator it = eventTypeExlusions.iterator(); it.hasNext();) {
                EventTypeExclusion item = (EventTypeExclusion)it.next();
                addExclussion(binding, EventTypeExclusionFactory.createExcluder(item));
            }
        }
    }

    /**
     * Add the metric exclusions
     * @param binding String report binding name
     * @param reportExclusion ReportExclusion (congifuration object)
     */
    private void addMetricExclusions(ReportExclusion reportExclusion,
                                     String binding) {
        ArrayList metricExlusions = reportExclusion.getMetricExclusions();
        if(metricExlusions != null) {
            for(Iterator it = metricExlusions.iterator(); it.hasNext();) {
                MetricExclusion item = (MetricExclusion)it.next();
                addExclussion(binding, MetricExclusionFactory.createExcluder(item));
            }
        }
    }

}
