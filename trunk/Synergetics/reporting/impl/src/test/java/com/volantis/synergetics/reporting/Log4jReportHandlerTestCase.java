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

import com.volantis.synergetics.reporting.Log4jReportHandler;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Log4jReportHandlerTestCase extends TestCaseAbstract {


    public void testFormat() throws Exception {

        Map m = new HashMap();
        m.put(Log4jReportHandler.MESSAGE_FORMAT_KEY,
              "First subst {A} for B then {B} for C and put the message {:MESSAGE} here event {:EVENT} and date {:TIMESTAMP}");
        Log4jReportHandler h = new Log4jReportHandler(m, getClass().getName());

        Map metrics = new HashMap();
        metrics.put("A", "B");
        metrics.put("B", "C");
        metrics.put(":MESSAGE", "This message");
        metrics.put(":EVENT", Event.STOP.toString());
        metrics.put(":STATUS", Status.SUCCESS);
        metrics.put(":TIMESTAMP", new Date());
        h.report(metrics);
    }
}
