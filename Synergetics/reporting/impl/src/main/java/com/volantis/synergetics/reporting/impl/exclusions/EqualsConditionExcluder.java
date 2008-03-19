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

import java.util.Map;

import com.volantis.synergetics.reporting.config.MetricExclusion;
import com.volantis.synergetics.reporting.Event;

/**
 * Exclude event when metric value equals particular string
 *
 */
public class EqualsConditionExcluder implements EventExcluder {

    /**
     * metric name
     */
    private String metric;

    /**
     * value
     */
    private String value;

    /**
     * ignore case matching
     */
    private boolean ignoreCase;

    /**
     * Constructor
     * @param metric String
     * @param value String
     * @param ignoreCase boolean
     */
    public EqualsConditionExcluder(com.volantis.synergetics.reporting.impl.Metric metric, Event value, boolean ignoreCase) {
        this(metric.getName(), value.toString(), ignoreCase);
    }

    /**
     * Constructor
     * @param configuration MetricExclusion
     */
    public EqualsConditionExcluder(MetricExclusion configuration) {
        this(configuration.getMetricName(),
             configuration.getValue(),
             configuration.getIgnoreCase().booleanValue());
    }

    /**
     * Creates a new instance.
     */
    public EqualsConditionExcluder(String metric, String value, boolean ignoreCase) {
        this.metric = metric;
        this.value = value;
        this.ignoreCase = ignoreCase;
    }

    // javadoc inherited
    public boolean isExcluded(Map metrics) {
        boolean excluded = false;
        Object obj = metrics.get(metric);
        if(obj != null) {
            if(ignoreCase) {
                excluded = obj.toString().equalsIgnoreCase(value);
            } else {
                excluded = obj.toString().equals(value);
            }
        }
        return excluded;
    }

    // javadoc inherited
    public boolean isEqual(Object object) {
        if ( this == object ) {
            return true;
        }

        if ( !(object instanceof EqualsConditionExcluder) ) {
            return false;
        }
        EqualsConditionExcluder excluder = (EqualsConditionExcluder)object;

        return
          this.metric.equals(excluder.metric) &&
          this.value.equals(excluder.value) &&
          (this.ignoreCase == excluder.ignoreCase) ;
      }
}
