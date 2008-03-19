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

package com.volantis.mcs.runtime.project;

import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.project.InternalProjectFactory;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.project.jdbc.JDBCPolicySource;
import com.volantis.mcs.project.remote.RemotePolicySource;
import com.volantis.mcs.project.xml.XMLPolicySource;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.runtime.RepositoryContainer;

/**
 * Implementation of {@link RuntimePolicySourceFactory}.
 */
public class RuntimePolicySourceFactoryImpl
        implements RuntimePolicySourceFactory {

    /**
     * The underlying factory for creating the {@link PolicySource}.
     */
    private final InternalProjectFactory factory;

    /**
     * The reader to use for the remote policy source.
     */
    private final PolicyBuilderReader remoteReader;

    /**
     * The xml repository for use by xml policy sources.
     */
    private final LocalRepository xmlRepository;

    /**
     * The JDBC repository for use by JDBC policy sources.
     */
    private final LocalRepository jdbcRepository;

    /**
     * Initialise.
     *
     * @param factory             The underlying factory for creating the
     *                            {@link PolicySource}.
     * @param repositoryContainer The container for the different repositories.
     * @param remoteReader        The reader to use for the remote policy source.
     */
    public RuntimePolicySourceFactoryImpl(
            InternalProjectFactory factory,
            RepositoryContainer repositoryContainer,
            PolicyBuilderReader remoteReader) {

        this.factory = factory;
        this.remoteReader = remoteReader;
        xmlRepository = repositoryContainer.getXMLRepository();
        jdbcRepository = repositoryContainer.getJDBCRepository();
    }

    // Javadoc inherited.
    public XMLPolicySource createXMLPolicySource(String directory) {
        return factory.createXMLPolicySource(xmlRepository, directory);
    }

    // Javadoc inherited.
    public JDBCPolicySource createJDBCPolicySource(String projectName) {
        return factory.createJDBCPolicySource(jdbcRepository, projectName);
    }

    // Javadoc inherited.
    public RemotePolicySource createRemotePolicySource(String baseURL) {
        return factory.createRemotePolicySource(remoteReader, baseURL);
    }
}
