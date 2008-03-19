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

package com.volantis.mcs.accessors;

import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;

/**
 * @mock.generate
 */
public interface PolicyBuilderAccessor {

    /**
     * Enumerate the names of the policies in the repository.
     *
     * @param connection The <code>RepositoryConnection</code> to use to access
     *                   the repository.
     * @param project    The project to use.
     * @return An enumeration of policy names as {@link String}.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    RepositoryEnumeration enumeratePolicyBuilderNames(
            RepositoryConnection connection,
            Project project,
            PolicyType policyType)
            throws RepositoryException;

    /**
     * Remove the specified policy from the repository.
     *
     * @param connection The <code>RepositoryConnection</code> to use to access
     *                   the repository.
     * @param project
     * @param name       The name which identifies the policy to remove from the
     *                   repository.
     * @return True if the policy existed, false if it did not.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    boolean removePolicyBuilder(
            RepositoryConnection connection,
            Project project, String name)
            throws RepositoryException;

    /**
     * Retrieve the specified policy builder from the repository.
     *
     * @param connection The <code>RepositoryConnection</code> to use to access
     *                   the repository.
     * @param project
     * @param name       The name which identifies the policy to retrieve from
     *                   the repository.
     * @return The <code>PolicyBuilder</code> or null if it could not be found.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    PolicyBuilder retrievePolicyBuilder(
            RepositoryConnection connection, Project project,
            String name)
            throws RepositoryException;

    /**
     * Add the policy builder to the repository.
     *
     * @param connection The <code>RepositoryConnection</code> to use to access
     *                   the repository.
     * @param project    The project to use.
     * @param builder    The <code>PolicyBuilder</code> to add to the repository.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    void addPolicyBuilder(
            RepositoryConnection connection, Project project,
            PolicyBuilder builder)
            throws RepositoryException;

    /**
     * Update a policy in the repository whose identity is unchanged.
     *
     * @param project The project to use.
     * @param builder the <code>PolicyBuilder</code> object to update
     *                containing the updated properties.
     * @throws RepositoryException If there was a problem accessing the
     *                             repository.
     */
    boolean updatePolicyBuilder(
            RepositoryConnection connection, Project project,
            PolicyBuilder builder)
            throws RepositoryException;

    /**
     * Rename a policy.
     *
     * @param connection the connection to use.
     * @param project    the project the policy is in.
     * @param name       the old name of the policy.
     * @param newName    the new name of the policy.
     * @throws RepositoryException
     * @deprecated This was added simply to support the old accessors and
     *             should not be used for new code.
     */
    void renamePolicyBuilder(
            RepositoryConnection connection, Project project, String name,
            String newName) throws RepositoryException;
}
