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

package com.volantis.mcs.runtime.repository.remote.xml;

import com.volantis.mcs.migrate.api.config.ConfigFactory;
import com.volantis.mcs.migrate.api.config.RemotePolicyMigrator;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.shared.net.http.HttpMethodFactory;
import com.volantis.shared.net.http.HttpMethodFactoryImpl;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;

/**
 * Default implementation of {@link RemoteReadersFactory}.
 */
public class RemoteReadersFactoryImpl
        implements RemoteReadersFactory {

    /**
     * The factory to use for creating HTTP methods.
     */
    private final HttpMethodFactory factory;

    /**
     * The parser for parsing remote policy responses.
     */
    private final RemotePolicyBuilderParser parser;

    /**
     * Initialise.
     *
     * @param timeout The connection timeout.
     * @param clock   The system clock.
     */
    public RemoteReadersFactoryImpl(Period timeout, SystemClock clock) {

        factory = new HttpMethodFactoryImpl(timeout, clock);

        ConfigFactory migratorFactory = ConfigFactory.getDefaultInstance();
        RemotePolicyMigrator migrator =
                migratorFactory.createRemotePolicyMigrator();
        parser = new RemotePolicyBuilderParser(migrator);

    }

    // Javadoc inherited.
    public PolicyBuilderReader createPolicyBuilderReader(
            ProjectManager projectManager) {
        return new RemotePolicyBuilderReader(factory, projectManager, parser);
    }

    // Javadoc inherited.
    public RemotePolicyBuildersReader createPolicyBuildersReader(
            ProjectManager projectManager) {

        return new RemotePolicyBuildersReader(factory, projectManager, parser);
    }
}
