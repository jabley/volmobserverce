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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.unit.config.lpdm.xsl;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Test;

/**
 * migration XSL test cases.
 */
public class RPDM200512To200602TestCase
        extends TestCaseAbstract {

    public static Test suite() {
        MigrationTestSuiteBuilder builder =
                new MigrationTestSuiteBuilder(
                "/com/volantis/mcs/migrate/impl/config/lpdm/xsl/rpdm-200512-to-200602.xsl",
                "200512_lpdm_files/", "200602_lpdm_files/");

        builder.addTestFiles(new String[] {
            "set-response",
            "single-response",
            "three-images",
        });

        return builder.getSuite();
    }
}
