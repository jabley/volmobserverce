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
package com.volantis.synergetics.reporting;

import java.util.Map;

/**
 * Implementations of this class must have a constructor that takes Map object
 * containing its configuration and a String representing the interface or
 * binding it will be required to report for.
 *
 * <p>
 * e.g</p>
 * <p><pre>MyReportHandler(Map config, String bindName) {...</pre></p>
 *
 * @deprecated Use OSGi event listeners instead.
 */
public interface ReportHandler {

    /**
     * Report an event to the reporting handler.
     *
     * @param metrics the map of metric names for the event. Must not be null.
     */
    public void report(Map metrics);
}
