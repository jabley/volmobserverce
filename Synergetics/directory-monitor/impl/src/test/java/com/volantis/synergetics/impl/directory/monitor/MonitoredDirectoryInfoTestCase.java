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

import com.volantis.synergetics.directory.monitor.DirectoryMonitorCallback;
import com.volantis.synergetics.directory.monitor.DirectoryMonitorCallbackMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class MonitoredDirectoryInfoTestCase extends TestCaseAbstract {


    /**
     * Simply check that the MonitoredDirectoryInfo returns the values it was
     * provided with when constructed.
     * @throws Exception
     */
    public void testMonitoredDirectoryInfo() throws Exception {

        String dirName = "directoryName";
        DirectoryMonitorCallback callback = new DirectoryMonitorCallbackMock("bla") ;

        TimestampPersistenceFactoryMock persistenceFactory =
                new TimestampPersistenceFactoryMock(
                        "persistenceFactory", expectations);
        TimestampPersistence persistence =
                new TimestampPersistenceMock("persistence", expectations);

        persistenceFactory.expects.createTimestampPersistence(
                dirName, false).returns(persistence);

        MonitoredDirectoryInfo mdi = new MonitoredDirectoryInfo(
            dirName, 32L, callback, persistenceFactory, false);

        assertEquals("expect directory name", dirName, mdi.getCanonicalDirname());
        assertEquals("expect 32", 32L, mdi.getBundleId());
        assertSame("callbacks should be the same instance", callback, mdi.getCallback());
        assertSame("persistence should be the same", persistence, mdi.getPersistence());
    }
}
