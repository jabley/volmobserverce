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
package com.volantis.synergetics.reporting.impl;

import java.util.Map;
import java.util.HashMap;

/**
 * Metric enumeration.
 */
public class Metric {

    private static final Map METRICS = new HashMap();

    /**
     * The Event substitution parameter
     */
    public static final Metric EVENT = new Metric(":EVENT");

    /**
     * The Message substitution parameter
     */
    public static final Metric MESSAGE = new Metric(":MESSAGE");

    /**
     * The Transaction id substitution parameter
     */
    public static final Metric TRANSID = new Metric(":TRANSID");

    /**
     * The Parents transaction id substitution parameter
     */
    public static final Metric PTRANSID = new Metric(":PTRANSID");

    /**
     * The Timestamp substitution parameter
     */
    public static final Metric TIMESTAMP = new Metric(":TIMESTAMP");

    /**
     * Returns the metric with the specified name or return null, if the metric
     * cannot be found.
     *
     * @param name the name of the metric
     * @return the metric or null
     */
    public static Metric lookup(final String name) {
        return (Metric) METRICS.get(name);
    }

    /**
     * The name of the metric.
     */
    private final String name;

    /**
     * Creates a new metric with the given name.
     *
     * @param name the name of the metric to be created
     */
    private Metric(final String name) {
        this.name = name;
        METRICS.put(name, this);
    }

    /**
     * Returns the name of the metric
     * @return the name of the metric
     */
    public String getName() {
        return name;
    }

    // javadoc inherited
    public String toString() {
        return name;
    }
}
