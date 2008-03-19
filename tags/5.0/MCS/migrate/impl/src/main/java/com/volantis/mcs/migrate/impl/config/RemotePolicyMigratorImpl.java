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

package com.volantis.mcs.migrate.impl.config;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.migrate.api.framework.FrameworkFactory;
import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.config.RemotePolicyMigrator;
import com.volantis.mcs.migrate.api.config.ConfigFactory;
import com.volantis.mcs.migrate.api.config.ByteArrayOutputCreator;
import com.volantis.mcs.migrate.notification.NotificationFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.InputStream;

public class RemotePolicyMigratorImpl implements RemotePolicyMigrator {

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(RemotePolicyMigratorImpl.class);

    public String migratePolicy(InputStream stream, String url)
            throws ResourceMigrationException, IOException {

        NotificationFactory notificationFactory =
                NotificationFactory.getDefaultInstance();
        ConfigFactory configFactory = ConfigFactory.getDefaultInstance();
        FrameworkFactory frameworkFactory =
                FrameworkFactory.getDefaultInstance();

        NotificationReporter notifier =
                notificationFactory.createLogDispatcherReporter(logger);

        ByteArrayOutputCreator outputCreator = new ByteArrayOutputCreator();
        ResourceMigrator migrator = configFactory.createDefaultResourceMigrator(
                notifier, true);
        InputMetadata meta = frameworkFactory.createInputMetadata(url, false);
        migrator.migrate(meta, stream, outputCreator);
        return outputCreator.getOutputStream().toString();
    }
}
