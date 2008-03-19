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
public class AlignedSpatialIteratorStylingTestCase {

    public static Test suite() throws Exception {

        XDIMEIntegrationTestSuiteBuilder builder =
                new XDIMEIntegrationTestSuiteBuilder(
                        AlignedSpatialIteratorStylingTestCase.class,
                        "webapp/WEBINF/mcs-config.xml");

        builder.addXDIMETestCase("styling/2007070404/aligned/aligned-spatial.xdime1",
                "styling/2007070404/aligned/aligned-spatial.xdime1.for.XDIME.xml");

        builder.addPCTestCase("styling/2007070404/aligned/aligned-spatial.xdime1",
                "styling/2007070404/aligned/aligned-spatial.xdime1.for.PC.html");

        builder.addWMLTestCase("styling/2007070404/aligned/aligned-spatial.xdime1",
                "styling/2007070404/aligned/aligned-spatial.xdime1.for.WML.xml");

        builder.addXDIMETestCase("styling/2007070404/aligned/lots-of-holes.xdime1",
                "styling/2007070404/aligned/lots-of-holes.xdime1.for.XDIME.xml");

        return builder.getSuite();
    }
}
