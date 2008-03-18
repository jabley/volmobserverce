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
package com.volantis.synergetics.reporting;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple Report handler used for test purposes. It simply records the
 * metrics it is asked to report.
 */
public class TestReportHandler implements ReportHandler {

    private static Map savedMetrics = null;

    /**
     * Standard constructor
     *
     * @param report
     * @param bindingName
     */
    public TestReportHandler(Map report, String bindingName) {
    }

    // javadoc inherited
    public void report(Map metrics) {
       savedMetrics = new HashMap(metrics);
    }

    /**
     * Return the metrics this report handler was asked to report
     *
     * @return the metrics this report handler was asked to report
     */
    public static Map getMetrics() {
       return savedMetrics;
    }
}
						    
