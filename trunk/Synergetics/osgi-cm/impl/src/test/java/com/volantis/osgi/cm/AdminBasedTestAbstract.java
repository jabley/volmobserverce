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
import mock.org.osgi.framework.BundleMock;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AdminBasedTestAbstract
        extends TestCaseAbstract {

    protected static final String AUTHORIZED_BUNDLE_LOCATION =
            "authorized bundle location";
    protected static final String UNAUTHORIZED_BUNDLE_LOCATION =
            "unauthorized bundle location";
    protected static final String ATTACKING_BUNDLE_LOCATION =
            "attacking bundle location";

    //    private ConfigurationStoreMock storeMock;
    protected ConfigurationAdmin authorizedAdmin;
    protected ConfigurationManager manager;
    protected ConfigurationAdmin attackingAdmin;
    protected ConfigurationAdmin unauthorizedAdmin;
    protected BundleMock authorizedBundleMock;
    protected BundleMock unauthorizedBundleMock;
    protected BundleMock attackingBundleMock;
    protected static final ServiceReference[] EMPTY_REFERENCES =
            new ServiceReference[0];

    protected void setUp() throws Exception {
        super.setUp();

        // An authorized bundle.
        authorizedBundleMock =
                new BundleMock("authorizedBundleMock", expectations);

        authorizedBundleMock.expects.getLocation()
                .returns(AUTHORIZED_BUNDLE_LOCATION).any();
        authorizedBundleMock.expects
                .hasPermission(SecurityHelper.CONFIGURATION_PERMISSION)
                .returns(true).any();

        // An unauthorized bundle.
        unauthorizedBundleMock =
                new BundleMock("unauthorizedBundleMock", expectations);

        unauthorizedBundleMock.expects.getLocation()
                .returns(UNAUTHORIZED_BUNDLE_LOCATION).any();
        unauthorizedBundleMock.expects
                .hasPermission(SecurityHelper.CONFIGURATION_PERMISSION)
                .returns(false).any();

        // An attacking bundle.
        attackingBundleMock =
                new BundleMock("attackingBundleMock", expectations);

        attackingBundleMock.expects.getLocation()
                .returns(ATTACKING_BUNDLE_LOCATION)
                .any();
        attackingBundleMock.expects
                .hasPermission(SecurityHelper.CONFIGURATION_PERMISSION)
                .returns(false).any();

        ConfigurationManager manager = createAdminManager();

        authorizedAdmin = new BundleConfigurationAdmin(
                manager, authorizedBundleMock);

        unauthorizedAdmin = new BundleConfigurationAdmin(
                manager, unauthorizedBundleMock);

        attackingAdmin = new BundleConfigurationAdmin(
                manager, attackingBundleMock);

        this.manager = manager;
    }

    protected abstract ConfigurationManager createAdminManager()
            throws IOException;

    protected void assertCorrectConfigurations(
            Configuration[] expectedConfigurations,
            Configuration[] configurations) {

        if (expectedConfigurations == null) {
            assertNull(configurations);
        } else {

            assertEquals("Wrong number of configurations provided",
                    expectedConfigurations.length,
                    configurations.length);

            Map map = new HashMap();
            for (int i = 0; i < configurations.length; i++) {
                Configuration configuration = configurations[i];
                map.put(configuration.getPid(), configuration);
            }


            for (int i = 0; i < expectedConfigurations.length; i++) {
                Configuration expectedConfiguration = expectedConfigurations[i];
                String pid = expectedConfiguration.getPid();
                Configuration actual = (Configuration) map.get(pid);
                if (actual == null) {
                    fail("Expected configuration with pid '" + pid +
                            "' but did not find it");
                }
            }
        }
    }
}
