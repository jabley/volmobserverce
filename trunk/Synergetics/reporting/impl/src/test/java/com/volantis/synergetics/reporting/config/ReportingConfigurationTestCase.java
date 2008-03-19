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
package com.volantis.synergetics.reporting.config;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Report configuration load tests
 */
public class ReportingConfigurationTestCase extends TestCase {

    /**
     * Legacy configuration
     */
    private String legacyConfiguration =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<reporting-config xmlns=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration\"\n" +
        "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        "     xsi:schemaLocation=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration file:/opt/work/2006050805/Synergetics/src/com/volantis/synergetics/reporting/reporting-configuration.xsd\">\n" +
        "    <jdbc-datasource>\n" +
        "        <name>MYSQLDB</name>\n" +
        "        <driver-class>org.gjt.mm.mysql.Driver</driver-class>\n" +
        "        <connection-string>jdbc:mysql://127.0.0.1/logging?user=logger&amp;password=logpass&amp;database=logging</connection-string>\n" +
        "    </jdbc-datasource>\n" +
        "    \n" +
        "    <internal-pool-datasource>\n" +
        "        <name>POOL</name>\n" +
        "        <driver-class>org.gjt.mm.mysql.Driver</driver-class>\n" +
        "        <url>jdbc:mysql://127.0.0.1/logging?database=logging</url>\n" +
        "        <username>db_user</username>\n" +
        "        <password>db_password</password>\n" +
        "        <max-active>30</max-active>\n" +
        "        <max-idle>10</max-idle>\n" +
        "        <max-wait>5</max-wait>\n" +
        "    </internal-pool-datasource>\n" +
        "    \n" +
        "    <jndi-datasource>\n" +
        "        <name>JNDI</name>\n" +
        "        <jndi-name>jdbc/TestDB</jndi-name>\n" +
        "    </jndi-datasource>\n" +
        "    \n" +
        "    <jndi-datasource>\n" +
        "        <name>MYSQLDB</name>\n" +
        "        <jndi-name>jdbc/TestDB</jndi-name>\n" +
        "    </jndi-datasource>\n" +
        "    \n" +
        "    <report binding=\"com.volantis.synergetics.report.FooBar\" enabled=\"true\">\n" +
        "        <sql-handler>\n" +
        "            <table-name>FOO_BAR_TABLE</table-name>\n" +
        "            <column-mapping>\n" +
        "                <metric>Foo</metric>\n" +
        "                <column>FOO_COLUMN</column>\n" +
        "            </column-mapping>\n" +
        "            <column-mapping>\n" +
        "                <metric>Bar</metric>\n" +
        "                <column>BAR_COLUMN</column>\n" +
        "            </column-mapping>\n" +
        "            <datasource-name>MYSQLDB</datasource-name>\n" +
        "        </sql-handler>    \n" +
        "    </report>\n" +
        "    \n" +
        "    <report binding=\"com.volantis.synergetics.report.Wibble\">\n" +
        "        <generic-handler class=\"com.volantis.Handler\">\n" +
        "            <param>\n" +
        "                <name>This is the parameter name</name>\n" +
        "                <value>This is the parameter value</value>\n" +
        "            </param>\n" +
        "        </generic-handler>\n" +
        "    </report>\n" +
        "   \n" +
        "    \n" +
        "</reporting-config>";


    /**
     * Config with exclusions
     */
    private String configWithExclusions = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<reporting-config xmlns=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration\"\n" +
        "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        "     xsi:schemaLocation=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration file:/opt/work/2006050805/Synergetics/src/com/volantis/synergetics/reporting/reporting-configuration.xsd\">\n" +
        "    <jdbc-datasource>\n" +
        "        <name>MYSQLDB</name>\n" +
        "        <driver-class>org.gjt.mm.mysql.Driver</driver-class>\n" +
        "        <connection-string>jdbc:mysql://127.0.0.1/logging?user=logger&amp;password=logpass&amp;database=logging</connection-string>\n" +
        "    </jdbc-datasource>\n" +
        "    \n" +
        "    <internal-pool-datasource>\n" +
        "        <name>POOL</name>\n" +
        "        <driver-class>org.gjt.mm.mysql.Driver</driver-class>\n" +
        "        <url>jdbc:mysql://127.0.0.1/logging?database=logging</url>\n" +
        "        <username>db_user</username>\n" +
        "        <password>db_password</password>\n" +
        "        <max-active>30</max-active>\n" +
        "        <max-idle>10</max-idle>\n" +
        "        <max-wait>5</max-wait>\n" +
        "    </internal-pool-datasource>\n" +
        "    \n" +
        "    <jndi-datasource>\n" +
        "        <name>JNDI</name>\n" +
        "        <jndi-name>jdbc/TestDB</jndi-name>\n" +
        "    </jndi-datasource>\n" +
        "    \n" +
        "    <jndi-datasource>\n" +
        "        <name>MYSQLDB</name>\n" +
        "        <jndi-name>jdbc/TestDB</jndi-name>\n" +
        "    </jndi-datasource>\n" +
        "    \n" +
        "    <report binding=\"com.volantis.synergetics.report.Test1\" enabled=\"true\">\n" +
        "        <exclusions>\n" +
        "        </exclusions>\n" +
        "        <sql-handler>\n" +
        "            <table-name>FOO_BAR_TABLE</table-name>\n" +
        "            <column-mapping>\n" +
        "                <metric>Foo</metric>\n" +
        "                <column>FOO_COLUMN</column>\n" +
        "            </column-mapping>\n" +
        "            <column-mapping>\n" +
        "                <metric>Bar</metric>\n" +
        "                <column>BAR_COLUMN</column>\n" +
        "            </column-mapping>\n" +
        "            <datasource-name>MYSQLDB</datasource-name>\n" +
        "        </sql-handler>    \n" +
        "    </report>\n" +
        "    \n" +
        "    <report binding=\"com.volantis.synergetics.report.Test2\" enabled=\"true\">\n" +
        "        <exclusions>\n" +
        "            <event type=\"stop\"/>\n" +
        "            <event type=\"update\"/>\n" +
        "        </exclusions>\n" +
        "        <sql-handler>\n" +
        "            <table-name>FOO_BAR_TABLE</table-name>\n" +
        "            <column-mapping>\n" +
        "                <metric>Foo</metric>\n" +
        "                <column>FOO_COLUMN</column>\n" +
        "            </column-mapping>\n" +
        "            <column-mapping>\n" +
        "                <metric>Bar</metric>\n" +
        "                <column>BAR_COLUMN</column>\n" +
        "            </column-mapping>\n" +
        "            <datasource-name>MYSQLDB</datasource-name>\n" +
        "        </sql-handler>    \n" +
        "    </report>\n" +
        "    \n" +
        "    <report binding=\"com.volantis.synergetics.report.Test3\">\n" +
        "        <exclusions>\n" +
        "            <metric name=\"Metric1\" operation=\"starts-with\" value=\"Value1\" ignore-case=\"true\"/>\n" +
        "            <metric name=\"Metric2\" operation=\"matches\" value=\".*2\"/>\n" +
        "        </exclusions>\n" +
        "        <generic-handler class=\"com.volantis.Handler\">\n" +
        "            <param>\n" +
        "                <name>Metric1</name>\n" +
        "                <value>Value1</value>\n" +
        "            </param>\n" +
        "        </generic-handler>\n" +
        "    </report>\n" +
        "   \n" +
        "    <report binding=\"com.volantis.synergetics.report.Test3\">\n" +
        "        <exclusions>\n" +
        "            <event type=\"stop\"/>\n" +
        "            <metric name=\"Metric1\" operation=\"starts-with\" value=\"Value1\" ignore-case=\"true\"/>\n" +
        "            <metric name=\"Metric2\" operation=\"matches\" value=\".*2\"/>\n" +
        "        </exclusions>\n" +
        "        <generic-handler class=\"com.volantis.Handler\">\n" +
        "            <param>\n" +
        "                <name>Metric1</name>\n" +
        "                <value>Value1</value>\n" +
        "            </param>\n" +
        "        </generic-handler>\n" +
        "    </report>\n" +
        "   \n" +
        "    \n" +
        "</reporting-config>";

    /**
     * Load legacy configuration test case
     */
    public void testLegacyConfiguration() throws Exception {

        InputStream bais = new ByteArrayInputStream(legacyConfiguration.getBytes());
        JibxReportingConfigParser parser = new JibxReportingConfigParser();
        ReportingConfig config = parser.parse(bais);
        List reportElements = config.getReportElements();
        assertEquals("There should be 2 'report' elements", 2,
                reportElements.size());
        ReportElement report1 = (ReportElement) reportElements.get(0);
        ReportElement report2 = (ReportElement) reportElements.get(1);

        // report1 should be a sql report
        assertNotNull(report1);
        assertNotNull(report1.getSqlHandler());

        // report2 should be a generic report
        assertNotNull(report2);
        assertNull(report2.getSqlHandler());
        GenericHandler handler = report2.getGenericHandler();
        assertNotNull(handler);

        Map datasourcesMap = config.getDatasourceConfigMap();
        //last definition of datasource should be used
        assertEquals(datasourcesMap.get("MYSQLDB") instanceof JNDIDatasource, true);
        assertEquals(datasourcesMap.get("POOL") instanceof InternalPoolDatasource, true);
        assertEquals(datasourcesMap.get("JNDI") instanceof JNDIDatasource, true);

    }

    /**
     * Load configuration with exclusions test case
     */
    public void testConfigurationWithExclusions() throws Exception {

        InputStream bais = new ByteArrayInputStream(configWithExclusions.getBytes());
        JibxReportingConfigParser parser = new JibxReportingConfigParser();
        ReportingConfig config = parser.parse(bais);
        List reportElements = config.getReportElements();
        assertEquals("There should be 4 'report' elements", 4,
                reportElements.size());
        ReportElement report1 = (ReportElement) reportElements.get(0);
        ReportElement report2 = (ReportElement) reportElements.get(1);
        ReportElement report3 = (ReportElement) reportElements.get(2);
        ReportElement report4 = (ReportElement) reportElements.get(3);

        assertNotNull(report1);
        assertEquals("There should be 0 event type exclusion in 1st report", 0, report1.getReportExclusion().getEventTypeExclusions().size());
        assertEquals("There should be 0 metric exclusion in 1st report", 0, report1.getReportExclusion().getMetricExclusions().size());

        assertNotNull(report2);
        assertEquals("There should be 2 event type exclusion in 2nd report", 2, report2.getReportExclusion().getEventTypeExclusions().size());
        assertEquals("There should be 0 metric exclusion in 2nd report", 0, report2.getReportExclusion().getMetricExclusions().size());

        assertNotNull(report3);
        assertEquals("There should be 0 event type exclusion in 3rd report", 0, report3.getReportExclusion().getEventTypeExclusions().size());
        assertEquals("There should be 2 metric exclusion in 3rd report", 2, report3.getReportExclusion().getMetricExclusions().size());
        ArrayList exclusions = report3.getReportExclusion().getMetricExclusions();
        MetricExclusion me1 = (MetricExclusion)exclusions.get(0);
        MetricExclusion me2 = (MetricExclusion)exclusions.get(1);
        assertEquals("ignore-case set to true", true, me1.getIgnoreCase().booleanValue());
        assertEquals("ignore-case default is false", false, me2.getIgnoreCase().booleanValue());

        assertNotNull(report4);
        assertEquals("There should be 1 event type exclusion in 2nd report", 1, report4.getReportExclusion().getEventTypeExclusions().size());
        assertEquals("There should be 2 metric exclusion in 2nd report", 2, report4.getReportExclusion().getMetricExclusions().size());

    }

}
