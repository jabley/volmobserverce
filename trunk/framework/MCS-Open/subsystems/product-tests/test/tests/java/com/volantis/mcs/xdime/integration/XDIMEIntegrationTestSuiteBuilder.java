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

package com.volantis.mcs.xdime.integration;

import com.volantis.mcs.servlet.MarinerServletApplication;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Build an {@link XDIMEIntegrationTest} based test suite.
 */
public class XDIMEIntegrationTestSuiteBuilder {

    private static final String XDIME_PROTOCOL_CONTENT_TYPE =
            "x-application/vnd.xdime+xml;charset=ISO-8859-1";

    private static final String WML_PROTOCOL_CONTENT_TYPE =
            "text/vnd.wap.wml;charset=ISO-8859-1";

    private final MarinerServletApplication servletApplication;
    private final TestSuite suite;

    public XDIMEIntegrationTestSuiteBuilder(
            Class clazz, String relativePathToConfig)
            throws Exception {

        WebAppManager manager = WebAppManager.getManager();
        WebApp webApp = manager.getWebApp(clazz,
                relativePathToConfig);

        Logger.getDefaultHierarchy().setThreshold(Level.WARN);

        servletApplication = webApp.getServletApplication();

        suite = new TestSuite();
    }

    public void addPCTestCase(
            String xdimePath, String expectedResult) {
        suite.addTest(new XDIMEIntegrationTest(servletApplication,
                "PC", xdimePath, expectedResult, false));
    }

    public void addXDIMETestCase(
            String xdimePath, String expectedResultPath) {
        XDIMEIntegrationTest test;
        test = new XDIMEIntegrationTest(servletApplication,
                "XDIME", xdimePath, expectedResultPath, true);
        test.setExpectedContentType(XDIME_PROTOCOL_CONTENT_TYPE);

        suite.addTest(test);
    }

    public void addWMLTestCase(
            String xdimePath, String expectedResultPath) {
        XDIMEIntegrationTest test;
        test = new XDIMEIntegrationTest(servletApplication,
                "WAP-Handset", xdimePath, expectedResultPath, true);
        test.setExpectedContentType(WML_PROTOCOL_CONTENT_TYPE);

        suite.addTest(test);
    }

    public Test getSuite() {
        return suite;
    }
}
