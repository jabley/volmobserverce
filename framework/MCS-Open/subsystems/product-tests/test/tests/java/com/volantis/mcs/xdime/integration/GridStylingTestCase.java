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

import junit.framework.Test;

/**
 * Integration test cases for XDIME (1 and 2) layout styling.
 */
public class GridStylingTestCase {

    public static Test suite() throws Exception {

        XDIMEIntegrationTestSuiteBuilder builder =
                new XDIMEIntegrationTestSuiteBuilder(
                        GridStylingTestCase.class,
                        "webapp/WEBINF/mcs-config.xml");

        builder.addXDIMETestCase("styling/grid/grid-padding.xdime1",
                "styling/grid/grid-padding.xdime1.for.XDIME.xml");

        return builder.getSuite();
    }
}
