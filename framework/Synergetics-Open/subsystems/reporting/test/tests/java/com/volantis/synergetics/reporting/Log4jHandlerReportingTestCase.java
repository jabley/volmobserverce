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

public class Log4jHandlerReportingTestCase extends ReportingTestCaseAbstract {


    public String s = 
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<reporting-config xmlns=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration\"\n" +
        "     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        "     xsi:schemaLocation=\"http://www.volantis.com/xmlns/2005/10/storefront/reporting-configuration file:/opt/work/2006050805/Synergetics/src/com/volantis/synergetics/reporting/reporting-configuration.xsd\">\n" +
        "    <report binding=\"" +
        TestCounterGroup.class.getName() +
        "\" enabled=\"true\">\n" +
        "        <generic-handler class=\"" +
        Log4jReportHandler.class.getName() +
        "\">\n" +
        "            <param>\n" +
        "                <name>message.format</name>\n" +
        "                <value>MY MESSAGE {Floaty} with {Intlike}</value>\n" +
        "            </param>\n" +
        "        </generic-handler>\n" +
        "    </report>\n" +
        "   \n" +
        "</reporting-config>";

    public String getConfig() {
        return s;
    }
}
