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

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Dynamic report interface tests
 */
public class DynamicReportTestCase extends TestCaseAbstract {

    /**
     * configuration
     */
    public String s = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<reporting-config xmlns=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration\"\n" +
        "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        "     xsi:schemaLocation=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration file:/opt/work/2006050805/Synergetics/src/com/volantis/synergetics/reporting/reporting-configuration.xsd\">\n" +
        "    <report binding=\"Logger\" enabled=\"true\">\n" +
        "        <generic-handler class=\"" +
        TestReportHandler.class.getName() +
        "\">\n" +
        "            <param>\n" +
        "                <name>message.format</name>\n" +
        "                <value>MY MESSAGE {Floaty} with {Intlike}</value>\n" +
        "            </param>\n" +
        "        </generic-handler>\n" +
        "    </report>\n" +
        "   \n" +
        "</reporting-config>";

    /**
     * Create ReportingTransactionFactory
     * @return ReportingTransactionFactory
     */
    public ReportingTransactionFactory createFactory() throws Exception {

        String s = getConfig();
        ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
        Map map = new HashMap();
        map.put("reporting.config.location", is);
        ReportingTransactionFactory rtf =
            ReportingTransactionMetaFactory.getDefaultInstance().getReportingTransactionFactory(map);

        return rtf;
    }

    /**
     * Get config
     * @return String
     */
    public String getConfig() {
        return s;
    }

    /**
     * Dynamic report interface test case
     */
    public void testDynamicReportInterface() throws Exception {
        ReportingTransactionFactory proxy = createFactory();
        DynamicReport dynamicReport = proxy.createDynamicReport("Logger");
        dynamicReport.start();

        dynamicReport.setParameter("Boolean", Boolean.TRUE);
        dynamicReport.setParameter("Byte", new Byte((byte)2));
        dynamicReport.setParameter("Character", new Character('a'));
        dynamicReport.setParameter("Double", new Double(7.2));
        dynamicReport.setParameter("Float", new Float(6.2));
        dynamicReport.setParameter("Integer", new Integer(5));
        dynamicReport.setParameter("Long", new Long(1000L));
        dynamicReport.setParameter("Short", new Short((short)3));
        dynamicReport.setParameter("String", "A String");
        dynamicReport.setParameter("Date", new Date(10));

        dynamicReport.stop(Status.SUCCESS, "Hello");

        Map metrics = TestReportHandler.getMetrics();

        assertEquals(new Integer(5), metrics.get("Integer"));
        assertEquals(Boolean.TRUE, metrics.get("Boolean"));
        assertEquals(new Byte((byte)2), metrics.get("Byte"));
        assertEquals(new Character('a'), metrics.get("Character"));
        assertEquals(new Double(7.2), metrics.get("Double"));
        assertEquals(new Float(6.2), metrics.get("Float"));
        assertEquals(new Integer(5), metrics.get("Integer"));
        assertEquals(new Long(1000L), metrics.get("Long"));
        assertEquals(new Short((short)3), metrics.get("Short"));
        assertEquals("A String", metrics.get("String"));
        assertEquals(new Date(10), metrics.get("Date"));
    }
}
