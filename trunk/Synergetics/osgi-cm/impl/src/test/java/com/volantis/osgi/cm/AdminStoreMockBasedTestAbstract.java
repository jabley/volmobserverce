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

import com.volantis.osgi.cm.dispatcher.DelayedReferenceContainer;
import com.volantis.osgi.cm.dispatcher.DispatcherMock;
import com.volantis.osgi.cm.plugin.PluginManagerMock;
import com.volantis.osgi.cm.store.ConfigurationStoreMock;
import mock.org.osgi.framework.BundleContextMock;
import mock.org.osgi.service.log.LogServiceMock;

import java.io.IOException;

public abstract class AdminStoreMockBasedTestAbstract
        extends AdminBasedTestAbstract {

    protected DispatcherMock dispatcherMock;
    protected ConfigurationStoreMock storeMock;
    protected BundleContextMock contextMock;
    private PluginManagerMock pluginManagerMock;
    protected LogServiceMock logServiceMock;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected ConfigurationManager createAdminManager() throws IOException {
        dispatcherMock =
                new DispatcherMock("dispatcherMock", expectations);

        storeMock = new ConfigurationStoreMock("storeMock",
                expectations);

        storeMock.expects.load().returns(null).any();

        contextMock = new BundleContextMock("contextMock", expectations);

        pluginManagerMock = new PluginManagerMock(
                "pluginManagerMock", expectations);

        logServiceMock = new LogServiceMock("logServiceMock", expectations);

        DelayedReferenceContainer referenceContainer =
                new DelayedReferenceContainer();
        referenceContainer.setReference(null);

        return new ConfigurationAdminManager(
                contextMock, logServiceMock, dispatcherMock, storeMock);
    }
}
