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

package com.volantis.mcs.policies.compatibility;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.compatibility.New2OldConverter;

public class Policy2ComponentContainerTestCase
 extends TestCaseAbstract {

    protected PolicyFactory factory;
    protected New2OldConverter converter;

    protected void setUp() throws Exception {
        super.setUp();

        factory = PolicyFactory.getDefaultInstance();
        converter = new New2OldConverter();
    }

    public void testImagePolicy() {

    }
}
