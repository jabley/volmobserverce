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

package com.volantis.mcs.project.impl;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;

/**
 * Wraps an accessot to add validation of the input.
 */
public class ValidatingPolicyBuilderAccessor
        implements PolicyBuilderAccessor {

    /**
     * The accessor being wrapped.
     */
    private final PolicyBuilderAccessor delegate;

    /**
     * Initialise.
     *
     * @param delegate The accessor being wrapped.
     */
    public ValidatingPolicyBuilderAccessor(PolicyBuilderAccessor delegate) {
        this.delegate = delegate;
    }

    // Javadoc inherited.
    public RepositoryEnumeration enumeratePolicyBuilderNames(
            RepositoryConnection connection,
            Project project,
            PolicyType policyType)
            throws RepositoryException {

        return delegate.enumeratePolicyBuilderNames(connection, project,
                policyType);
    }

    // Javadoc inherited.
    public boolean removePolicyBuilder(
            RepositoryConnection connection,
            Project project, String name)
            throws RepositoryException {

        // Make sure that the name is valid.
        ensureNameValidity(name);

        return delegate.removePolicyBuilder(connection, project, name);
    }

    // Javadoc inherited.
    public PolicyBuilder retrievePolicyBuilder(
            RepositoryConnection connection, Project project,
            String name)
            throws RepositoryException {

        // Make sure that the name is valid.
        ensureNameValidity(name);

        return delegate.retrievePolicyBuilder(connection, project, name);
    }

    // Javadoc inherited.
    public void addPolicyBuilder(
            RepositoryConnection connection, Project project,
            PolicyBuilder builder)
            throws RepositoryException {

        // Make sure that the builder is valid.
        ensureBuilderValidity(builder);

        delegate.addPolicyBuilder(connection, project, builder);
    }

    /**
     * Make sure that the builder is valid.
     *
     * <p>The name must be valid, see {@link #ensureNameValidity(String)} and
     * the policy type of the builder must match the policy type for the
     * extension.</p>
     *
     * @param builder The builder to check.
     */
    private void ensureBuilderValidity(PolicyBuilder builder) {

        // Make sure that the policy type associated with the name matches
        // the policy type of the builder.
        String name = builder.getName();
        PolicyType policyTypeFromName = ensureNameValidity(name);
        PolicyType policyTypeFromBuilder = builder.getPolicyType();

        if (policyTypeFromName != policyTypeFromBuilder) {
            throw new IllegalArgumentException(
                    "Expected builder to have a policy type of " +
                    policyTypeFromName + " based on the name of '" +
                    name + "' but was " + policyTypeFromBuilder);
        }
    }

    /**
     * Ensure that the policy name is valid.
     *
     * <p>The name must end with an extension that matches one of the
     * extensions used for policy types and the name must start with a leading
     * /.</p>
     *
     * @param name The name to check.
     * @return The policy type of the name.
     */
    private PolicyType ensureNameValidity(String name) {
        PolicyType policyTypeFromName = FileExtension.getPolicyTypeForPolicy(
                name);
        if (policyTypeFromName == null) {
            throw new IllegalArgumentException("Policy name '" + name +
                    "' has an unknown file extension");
        }

        if (!name.startsWith("/")) {
            throw new IllegalArgumentException(
                    "Policy name '" + name + "' must start with a /");
        }

        return policyTypeFromName;
    }

    // Javadoc inherited.
    public boolean updatePolicyBuilder(
            RepositoryConnection connection, Project project,
            PolicyBuilder builder)
            throws RepositoryException {

        // Make sure that the builder is valid.
        ensureBuilderValidity(builder);

        return delegate.updatePolicyBuilder(connection, project, builder);
    }

    // Javadoc inherited.
    public void renamePolicyBuilder(
            RepositoryConnection connection, Project project, String name,
            String newName) throws RepositoryException {

        // Make sure that the old and new names are valid and have the same
        // policy type.
        PolicyType oldPolicyType = ensureNameValidity(name);
        PolicyType newPolicyType = ensureNameValidity(newName);
        if (oldPolicyType != newPolicyType) {
            throw new IllegalArgumentException("Rename cannot change policy type, " +
                    "attempting to rename a " + oldPolicyType +
                    " to a " + newPolicyType);
        }

        delegate.renamePolicyBuilder(connection, project, name, newName);
    }
}
