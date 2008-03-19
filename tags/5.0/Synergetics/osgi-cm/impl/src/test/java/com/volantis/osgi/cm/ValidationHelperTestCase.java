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

package com.volantis.osgi.cm;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link ValidationHelper}.
 */
public class ValidationHelperTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that pid validation works correctly.
     */
    public void testPID() throws Exception {

        ValidationHelper.checkPID("a");
        ValidationHelper.checkPID("a.b");
        ValidationHelper.checkPID("a0-_");

        checkFailingPID(null);
        checkFailingPID("");
        checkFailingPID("a/b");
        checkFailingPID("a..b");
    }

    private void checkFailingPID(String pid) {
        try {
            ValidationHelper.checkPID(pid);
            fail("Did not detect error");
        } catch (IllegalArgumentException expected) {
            assertEquals("PID '" + pid + "' does not match pattern " +
                    "'[0-9A-Za-z_-]+(\\.[0-9A-Za-z_-]+)*'",
                    expected.getMessage());
        }
    }
}
