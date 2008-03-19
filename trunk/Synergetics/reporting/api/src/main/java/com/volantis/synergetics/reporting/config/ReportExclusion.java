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

package com.volantis.synergetics.reporting.config;

import java.util.ArrayList;

/**
 * Configuration of report exclustions
 *
 */
public class ReportExclusion {
    /**
     * Event type exclusions
     */
    private ArrayList eventTypeExclusions = new ArrayList();

    /**
     * Metric based exclusions
     */
    private ArrayList metricExclusions = new ArrayList();

    //  javadoc unnecessary
    public ArrayList getEventTypeExclusions() {
        return eventTypeExclusions;
    }

    //  javadoc unnecessary
    public void setEventTypeExclusions(ArrayList eventTypeExclusions) {
        this.eventTypeExclusions = eventTypeExclusions;
    }

    //  javadoc unnecessary
    public ArrayList getMetricExclusions() {
        return metricExclusions;
    }

    //  javadoc unnecessary
    public void setMetricExclusions(ArrayList metricExclusions) {
        this.metricExclusions = metricExclusions;
    }

}
