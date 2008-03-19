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

package com.volantis.mcs.policies.impl.io.remote;

import com.volantis.mcs.policies.impl.io.JIBXTestAbstract;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.remote.PolicyBuilders;

import java.util.List;

/**
 * Ensure that reading a response set works properly.
 */
public class ResponseSetReadingTestCase
        extends JIBXTestAbstract {

    public void testReading()
            throws Exception {

        String resource =
                RESOURCE_LOADER.getResourceAsString("set-response.xml");
        PolicyBuilders builders = (PolicyBuilders) doRoundTrip(resource, null);
        List list = builders.getPolicyBuilders();

        assertEquals(4, list.size());
        PolicyBuilder builder;

        builder = (PolicyBuilder) list.get(0);
        assertEquals("http://localhost:8090/europa/Remote/layouts/foo/bar/image1.xml",
                builder.getName());

        builder = (PolicyBuilder) list.get(1);
        assertEquals("http://localhost:8090/europa/Remote/layouts/foo/bar/image2.xml",
                builder.getName());

        builder = (PolicyBuilder) list.get(2);
        assertEquals("http://localhost:8090/europa/Remote/layouts/image3.xml",
                builder.getName());

        builder = (PolicyBuilder) list.get(3);
        assertEquals("http://localhost:8090/europa/Remote/layouts/text1.xml",
                builder.getName());
    }
}
