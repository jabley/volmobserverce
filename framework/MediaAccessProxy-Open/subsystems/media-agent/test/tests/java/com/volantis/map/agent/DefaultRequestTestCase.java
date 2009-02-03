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
package com.volantis.map.agent;

import com.volantis.map.agent.impl.DefaultRequest;
import com.volantis.map.common.param.MutableParameters;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * TestCase for the {@link DefaultRequest} class
 */
public class DefaultRequestTestCase extends TestCaseAbstract {
    /**
     * factors an instance of the thing being tested
     * @return a DefaultRequest instance
     */
    private DefaultRequest createTestable() {
        return new DefaultRequest("test-type", "source-url");
    }

    /**
     * Tests the {@link DefaultRequest#getResourceType()} method
     */
    public void testGetResourceType() throws Exception {
        DefaultRequest dr = createTestable();
        assertEquals("wrong resource type returned",
                     dr.getResourceType(),
                     "test-type");
    }

    /**
     * Tests the {@link DefaultRequest#getSourceURL()} ()} method
     */
    public void testGetSourceURL() throws Exception {
        DefaultRequest dr = createTestable();
        assertNull("expected null source url",
                    dr.getSourceURL());

       ((MutableParameters)dr.getInputParams()).setParameterValue("source-url",
                                                                  "the-url");
        assertEquals("wrong source url returned",
                     dr.getSourceURL(),
                     "the-url");
    }

    /**
     * Tests the {@link DefaultRequest#getOutputParams()} ()} method
     */
    public void testGetOutputParams() throws Exception {
        DefaultRequest dr = createTestable();
        assertNotNull("expected non null output parameters",
                    dr.getOutputParams());
    }

    /**
     * Tests the {@link DefaultRequest#getInputParams()} ()} method
     */
    public void testGetInputParams() throws Exception {
        DefaultRequest dr = createTestable();
        assertNotNull("expected non null output parmeters", 
                    dr.getInputParams());
    }
}
