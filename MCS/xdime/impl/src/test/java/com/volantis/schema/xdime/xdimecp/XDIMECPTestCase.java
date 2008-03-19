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

package com.volantis.schema.xdime.xdimecp;

import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.schema.SchemaTestBuilder;
import com.volantis.schema.ContentNotCompleteOneChild;
import com.volantis.schema.InvalidContentStartingWith;
import com.volantis.schema.xdime.common.XDIMECommonTests;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import junit.framework.Test;

/**
 * Test case for the XDIME 2 schemata.
 */
public class XDIMECPTestCase
        extends TestCaseAbstract {

    /**
     * Build a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {

        SchemaTestBuilder builder = XDIMECommonTests.getSchemaTestBuilder(
                XDIMESchemata.ALL_XDIME_CP_SCHEMATA);
        builder.useClass(XDIMECPTestCase.class);

        builder.addInvalidDocument("xml/unknown-link.xml",
                new InvalidContentStartingWith("link"));

        return builder.getSuite();
    }
}
