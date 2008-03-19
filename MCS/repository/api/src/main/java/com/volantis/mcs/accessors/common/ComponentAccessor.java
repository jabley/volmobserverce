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
package com.volantis.mcs.accessors.common;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.objects.CacheableRepositoryObject;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides access to Component objects stored in a repository.
 */
public class ComponentAccessor extends DependentAccessor {

    /**
     * The type of policy supported by this accessor.
     */
    private final PolicyType policyType;

    /**
     * Initialise.
     *
     * @param containerAccessor - the parent component container accessor
     * @param defaultProject
     */
    public ComponentAccessor(PolicyBuilderAccessor containerAccessor,
            Project defaultProject, PolicyType policyType) {
        super(containerAccessor, defaultProject);
        this.policyType = policyType;
    }

    // Javadoc inherited from super class.
    public void addObject(final RepositoryConnection connection,
                          RepositoryObject object)
            throws RepositoryException {

        checkConnection(connection);

        PolicyBuilder policyBuilder = old2New.component2VariablePolicyBuilder(
                                        (CacheableRepositoryObject) object);
        policyAccessor.addPolicyBuilder(connection, getProject(object),
                policyBuilder);
    }

    // Javadoc inherited from super class.
    public void removeObject(
            final RepositoryConnection connection, String name)
            throws RepositoryException {

        checkConnection(connection);

        policyAccessor.removePolicyBuilder(connection, defaultProject, name);
    }

    // Javadoc inherited from super class.
    public RepositoryObject retrieveObject(
            final RepositoryConnection connection, String name)
            throws RepositoryException {

        checkConnection(connection);

        RepositoryObject component = null;

        VariablePolicyBuilder policy = getVariablePolicyBuilder(
                connection, defaultProject, name, false);
        if (policy != null) {
            component = new2Old.variablePolicyBuilder2Component(policy);
        }

        return component;
    }

    // Javadoc inherited from super class.
    public void renameObject(
            RepositoryConnection connection,
            String name, String newName)
            throws RepositoryException {

        checkConnection(connection);

        policyAccessor.renamePolicyBuilder(connection, defaultProject, name, newName);
    }

    // Javadoc inherited from super class.
    public RepositoryEnumeration enumerateComponentNames(
            final RepositoryConnection connection)
            throws RepositoryException {

        checkConnection(connection);

        return policyAccessor.enumeratePolicyBuilderNames(connection, defaultProject, policyType);
    }

    // Javadoc inherited from super class.
    public Collection retrieveAllComponents(RepositoryConnection connection)
            throws RepositoryException {

        checkConnection(connection);

        Collection collection = new ArrayList();
        RepositoryEnumeration enumeration = policyAccessor.
                enumeratePolicyBuilderNames(connection, defaultProject, policyType);
        try {
            while(enumeration.hasNext()) {
                String name = (String) enumeration.next();
                collection.add(retrieveObject(connection, name));
            }
        }
        finally {
            enumeration.close();
        }

        return collection;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Oct-05	9789/2	emma	VBM:2005101113 Migrate JDBC Accessors to chunked accessors

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 28-Sep-05	9445/4	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 ===========================================================================
*/
