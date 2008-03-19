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

import com.volantis.synergetics.reporting.impl.DefaultReportingTransactionFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class ReportingTestCaseAbstract extends TestCaseAbstract {

    public ReportingTransactionFactory createFactory() throws Exception {

        String s = getConfig();
        ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
        Map map = new HashMap();
        map.put("reporting.config.location", is);
        ReportingTransactionFactory rtf = new DefaultReportingTransactionFactory(map);
        return rtf;
    }

    public abstract String getConfig();

    public void testDefaultInterface() throws Exception {

        ReportingTransactionFactory proxy = createFactory();
        Report group = proxy.createReport(Report.class);
        group.start();
        group.update("a");
        group.stop(Status.SUCCESS, "MESSAGE");

    }

    public void testCounterInterface() throws Exception {

        ReportingTransactionFactory proxy = createFactory();
        TestCounterGroup group = (TestCounterGroup)
            proxy.createReport(TestCounterGroup.class);
        group.setFloatyCounter(1.0F);

        group.setLongishCounter(45L);
        group.update("a");
    }

    public void testCounterInterfaceAgain() throws Exception {

        ReportingTransactionFactory proxy = createFactory();
        TestCounterGroup group = (TestCounterGroup)
            proxy.createReport(TestCounterGroup.class);
        group.setFloatyCounter(17.0F);
        group.setIntlikeCounter(3);
        group.setLongishCounter(45L);
        group.update("a");
        group.setLongishCounter(7);
        group.stop(Status.SUCCESS, "Hello");

    }

    public interface TestCounterGroup extends Report {

        public void setFloatyCounter(float counter);

        public void setIntlikeCounter(int counter);

        public void setLongishCounter(long counter);
    }

}
