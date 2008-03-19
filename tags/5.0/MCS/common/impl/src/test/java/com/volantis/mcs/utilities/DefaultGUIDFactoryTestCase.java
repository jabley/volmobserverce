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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.utilities;

import java.util.Set;
import java.util.HashSet;

import junit.framework.TestCase;

/**
 * Tests for DefaultGUIDFactory.
 */
public class DefaultGUIDFactoryTestCase extends TestCase {

    public void testUniqueness() {
        final DefaultGUIDFactory factory = new DefaultGUIDFactory("");
        final Set guids = new HashSet();
        final int runs = 20000;
        for (int i = 0; i < runs; i++) {
            guids.add(factory.generateGuid());
        }
        assertEquals(runs, guids.size());
    }
}
