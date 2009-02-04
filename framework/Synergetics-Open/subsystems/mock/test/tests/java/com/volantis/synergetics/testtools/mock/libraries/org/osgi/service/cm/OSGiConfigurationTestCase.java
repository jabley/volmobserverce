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

package com.volantis.synergetics.testtools.mock.libraries.org.osgi.service.cm;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;
import mock.org.osgi.service.cm.ConfigurationListenerMock;
import mock.org.osgi.service.cm.ManagedServiceFactoryMock;
import mock.org.osgi.service.cm.ManagedServiceMock;

/**
 * Tests for OSGi Configuration related mock objects.
 */
public class OSGiConfigurationTestCase
        extends MockTestCaseAbstract {

    /**
     * Tests that all the mock objects can be initialised correctly.
     */
    public void testInitialisation() {
        new ConfigurationListenerMock("configurationListenerMock",
                expectations);
        new ManagedServiceMock("managedServiceMock", expectations);
        new ManagedServiceFactoryMock("managedServiceFactoryMock",
                expectations);
    }
}