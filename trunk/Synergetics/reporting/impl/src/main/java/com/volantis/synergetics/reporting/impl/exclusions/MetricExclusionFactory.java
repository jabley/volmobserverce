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

import com.volantis.synergetics.reporting.config.MetricExclusion;

/**
 * Creates proper EventExcluder class based on configuration
 *
 */
public class MetricExclusionFactory {

    private static final String METRIC_STARTS_WITH = "starts-with";
    private static final String METRIC_CONTAINS = "contains";
    private static final String METRIC_EQUALS = "equals";
    private static final String METRIC_ENDS_WITH = "ends-with";
    private static final String METRIC_MATCHES = "matches";

    /**
     * No Constructor
     *
     */
    private MetricExclusionFactory() {

    }
    /**
     * Factory method creates proper EventExcluder based on configuration
     * @param configuration MetricExclusion
     * @return EventExcluder
     */
    public static EventExcluder createExcluder(MetricExclusion configuration) {
        EventExcluder ret = null;

        if(configuration.getExclusionType().toLowerCase().equals(METRIC_STARTS_WITH)) {
            ret = new StartsWithConditionExcluder(configuration);
        } else if(configuration.getExclusionType().toLowerCase().equals(METRIC_CONTAINS)) {
            ret = new ContainsConditionExcluder(configuration);
        } else if(configuration.getExclusionType().toLowerCase().equals(METRIC_EQUALS)) {
            ret = new EqualsConditionExcluder(configuration);
        } else if(configuration.getExclusionType().toLowerCase().equals(METRIC_ENDS_WITH)) {
            ret = new EndsWithConditionExcluder(configuration);
        } else if(configuration.getExclusionType().toLowerCase().equals(METRIC_MATCHES)) {
            ret = new RegularExpressionExcluder(configuration);
        }

        return ret;
    }

}
