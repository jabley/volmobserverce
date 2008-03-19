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

/**
 * Exclude report event when metric value ends with particular suffix
 *
 */
public class EndsWithConditionExcluder implements EventExcluder {

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
    public EndsWithConditionExcluder(String metric, String value, boolean ignoreCase) {
        this.metric = metric;
        this.value = value;
        this.ignoreCase = ignoreCase;
    }

    /**
     * Constructor
     * @param configuration MetricExclusion
     */
    public EndsWithConditionExcluder(MetricExclusion configuration) {
        this(configuration.getMetricName(),
             configuration.getValue(),
             configuration.getIgnoreCase().booleanValue());
    }

    // javadoc inherited
    public boolean isExcluded(Map metrics) {
        boolean excluded = false;
        Object obj = metrics.get(metric);
        if(obj != null) {
            if(ignoreCase) {

                excluded = obj.toString().toUpperCase().endsWith(
                        value.toUpperCase());
            } else {
                excluded = obj.toString().endsWith(value);
            }
        }
        return excluded;
    }

    // javadoc inherited
    public boolean isEqual(Object object) {
        if ( this == object ) {
            return true;
        }

        if ( !(object instanceof EndsWithConditionExcluder) ) {
            return false;
        }
        EndsWithConditionExcluder excluder = (EndsWithConditionExcluder)object;

        return
          this.metric.equals(excluder.metric) &&
          this.value.equals(excluder.value) &&
          (this.ignoreCase == excluder.ignoreCase) ;
      }

}
