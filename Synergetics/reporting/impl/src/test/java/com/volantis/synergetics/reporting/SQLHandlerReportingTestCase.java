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

public class SQLHandlerReportingTestCase extends ReportingTestCaseAbstract {


    public String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<reporting-config xmlns=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration\"\n" +
        "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        "     xsi:schemaLocation=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration file:/opt/work/2006050805/Synergetics/src/com/volantis/synergetics/reporting/reporting-configuration.xsd\">\n" +
        "    <jdbc-datasource>\n" +
        "        <name>Haddock</name>\n" +
        "        <driver-class>oracle.jdbc.driver.OracleDriver</driver-class>\n" +
        "        <connection-string>jdbc:oracle:thin:geoff/geoff@haddock:1526:dev1</connection-string>\n" +
        "    </jdbc-datasource>\n" +
        "    \n" +
        "    <report interface=\"" +
        ReportingTestCaseAbstract.TestCounterGroup.class.getName() +
        "\" enabled=\"true\">\n" +
        "        <sql-handler>\n" +
        "            <table-name>REPORTA</table-name>\n" +
        "            <column-mapping>\n" +
        "                <metric>Floaty</metric>\n" +
        "                <column>FLOATY</column>\n" +
        "            </column-mapping>\n" +
        "            <column-mapping>\n" +
        "                <metric>Intlike</metric>\n" +
        "                <column>INTLIKE</column>\n" +
        "            </column-mapping>\n" +
        "            <datasource-name>Haddock</datasource-name>\n" +
        "        </sql-handler>    \n" +
        "    </report>\n" +
        "</reporting-config>";

    public String getConfig() {
        return s;
    }

    public void testDefaultInterface() throws Exception {
        // @todo disable the test as it needs a DB to run correctly
    }

    public void testCounterInterface() throws Exception {
        // @todo disable the test as it needs a DB to run correctly
    }

    public void testCounterInterfaceAgain() throws Exception {
        // @todo disable the test as it needs a DB to run correctly
    }
}
