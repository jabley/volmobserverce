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
package com.volantis.synergetics.reporting.impl;

import com.volantis.synergetics.reporting.Report;
import com.volantis.synergetics.reporting.ReportService;

/**
 * Clas containing the names of the default metrics.
 */
public class DefaultMetrics {

    /**
     * The topic under which all OSGi Report events will be sent
     */
    public static final String TOPIC =
        ReportService.class.getName().replaceAll("\\.", "/");

    /**
     * The prefix to apply to all OSGi reporting properties (Config and event).
     */
    public static final String REPORT_PREFIX =
        Report.class.getName() + ".";

    /**
     * Property name prefix for metric properties (event properties)
     */
    public static final String REPORT_PROPERTIES_PREFIX =
        REPORT_PREFIX + "property.";

    /**
     * Property name for the report class
     */
    public static final String REPORT_CLASS = REPORT_PREFIX + "class";

    /**
     * Used to indicate the report subtype (used for
     * {@link com.volantis.synergetics.reporting.DynamicReport} reporting)
     */
    public static final String REPORT_SUBTYPE = REPORT_PREFIX + "subtype";


    private DefaultMetrics() {
        // hide it
    }
}
