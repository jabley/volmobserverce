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

package com.volantis.mcs.project;

import com.volantis.mcs.project.jdbc.JDBCPolicySource;
import com.volantis.mcs.project.xml.XMLPolicySource;
import com.volantis.mcs.project.remote.RemotePolicySource;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.policies.PolicyBuilderReader;

/**
 * Internal extensions to the {@link ProjectFactory}.
 */
public abstract class InternalProjectFactory
        extends ProjectFactory {

    public static InternalProjectFactory getInternalInstance() {
        return (InternalProjectFactory) getDefaultInstance();
    }

    /**
     * Create a {@link XMLPolicySource} for an XML repository.
     *
     * @return The {@link com.volantis.mcs.project.xml.XMLPolicySource}.
     */
    public abstract XMLPolicySource createXMLPolicySource(
            LocalRepository repository, String directory);

    /**
     * Create a {@link JDBCPolicySource} for a JDBC repository.
     *
     * @return The {@link JDBCPolicySource}.
     */
    public abstract JDBCPolicySource createJDBCPolicySource(
            LocalRepository repository, String projectName);

    /**
     * Create a {@link RemotePolicySource} for a remote repository.
     *
     * @return The {@link RemotePolicySource}.
     */
    public abstract RemotePolicySource createRemotePolicySource(
            PolicyBuilderReader reader, String baseURL);
}
