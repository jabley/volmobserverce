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

/**
 * Exclusion of report events based on report metric
 *
 */
public class MetricExclusion implements ExclusionConfiguration {

    /**
     * metric name
     */
    private String metricName;

    /**
     * operation
     */
    private String operation;

    /**
     * metric value for events being excluded
     */
    private String value;

    /**
     * ignore case of character when matching
     */
    private Boolean ignoreCase;

    //  javadoc unnecessary
    public String getMetricName() {
        return metricName;
    }

    //  javadoc unnecessary
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    //  javadoc unnecessary
    public Boolean getIgnoreCase() {
        //default ignore-case attribute value is false when no initialization occurs
        if(ignoreCase != null) {
            return ignoreCase;
        } else {
            return new Boolean(false);
        }
    }

    //  javadoc unnecessary
    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    //  javadoc unnecessary
    public String getOperation() {
        return operation;
    }

    //  javadoc unnecessary
    public void setOperation(String operation) {
        this.operation = operation;
    }

    //  javadoc unnecessary
    public String getValue() {
        return value;
    }

    //  javadoc unnecessary
    public void setValue(String value) {
        this.value = value;
    }

    // javadoc inherited
    public String getExclusionType() {
        return this.operation;
    }
}
