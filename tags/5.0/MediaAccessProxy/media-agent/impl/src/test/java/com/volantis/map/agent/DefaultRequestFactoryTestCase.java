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

import com.volantis.map.agent.impl.DefaultRequestFactory;
import com.volantis.map.agent.impl.DefaultRequest;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.net.URI;

/**
 *  Tests the DefaultRequestFactory class
 */
public class DefaultRequestFactoryTestCase extends TestCaseAbstract {

    /**
     * Tests the
     * {@link DefaultRequestFactory#createRequestFromICSURI(java.net.URI)} }
     * method
     * @throws Exception if an error occurs
     */
    public void testCreateRequestFromICSURI() throws Exception {
        DefaultRequestFactory factory = new DefaultRequestFactory();
        Request request = factory.createRequestFromICSURI(new URI(
                "http://localhost:8080/map/ics/images/gp8/volantis.jpg"));
        assertTrue("expected a DefaultRequest instance", 
                   request instanceof DefaultRequest);

    }
}
