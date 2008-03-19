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

package com.volantis.mcs.policies.impl.jdbc;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.policies.PolicyBuilderResponse;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.jdbc.JDBCPolicySource;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Reads a {@link PolicyBuilder} from a JDBC policy source.
 *
 * <p>This manages its own connection which means that the underlying SQL
 * connection will always be allocated and removed immediately.</p>
 *
 * <p> See VBM 2004030908 for an explanation of the problem that releasing
 * the SQL connection immediately that the policy has been retrieved is
 * attempting to solve.</p>
 */
public class JDBCPolicyBuilderReader
        implements PolicyBuilderReader {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(JDBCPolicyBuilderReader.class);

    /**
     * The underlying accessor.
     */
    private final PolicyBuilderAccessor accessor;

    /**
     * Initialise.
     *
     * @param accessor The underlying accessor.
     */
    public JDBCPolicyBuilderReader(PolicyBuilderAccessor accessor) {
        this.accessor = accessor;
    }

    // Javadoc inherited.
    public PolicyBuilderResponse getPolicyBuilder(
            Project project, String name) {

        InternalProject internalProject = (InternalProject) project;
        JDBCPolicySource source = (JDBCPolicySource)
                internalProject.getPolicySource();
        LocalRepository repository = source.getRepository();

        RepositoryConnection connection = null;
        PolicyBuilderResponse response = null;
        try {
            try {
                connection = repository.connect();
                PolicyBuilder builder = accessor.retrievePolicyBuilder(
                        connection, project, name);
                response = new PolicyBuilderResponse(internalProject, builder);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (RepositoryException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e);
            }
        }

        return response;
    }
}
