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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.impl.directory.monitor;

import java.util.Properties;

public class OnDiskTimestampPersistenceTestCase
    extends TimestampPersistenceTestCaseAbstract {


    public TimestampPersistence getTimestampPersistence(String directoryName,
                                                        boolean recursive) {
        return new OnDiskTimestampPersistence(directoryName, recursive);
    }

    /**
     * This test ensures that the data is modifiable and storeable (but not
     * permenantly persisted
     *
     * @throws Exception
     */
    public void testPersistenceReturnsCorrectValue() throws Exception {

        TimestampPersistence tsp = getTimestampPersistence(directory, false);

        Properties props = tsp.getTimestamps();
        assertTrue("should be empty", props.isEmpty());
        props.setProperty("testfile", "1231234124");

        tsp.storeTimestamps(props);

        // get a new instance and see if it realy did persist
        tsp = getTimestampPersistence(directory, false);

        props = tsp.getTimestamps();
        assertTrue(props.containsKey("testfile"));
        assertEquals("1231234124", props.getProperty("testfile"));
    }
}
