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

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.LocalRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractPolicyBuilderManager
        implements PolicyBuilderManager {

    protected final InternalProject project;
    protected final LocalRepository repository;
    protected final PolicyBuilderAccessor accessor;

    protected AbstractPolicyBuilderManager(InternalProject project) {

        this.project = project;

        LocalPolicySource source = (LocalPolicySource)
                project.getPolicySource();
        repository = source.getRepository();
        accessor = source.getPolicyBuilderAccessor();
    }

    protected List getPolicyBuilderNames(
            RepositoryConnection connection, PolicyType policyType)
            throws RepositoryException {
        List names;
        names = new ArrayList();
        if (policyType == null) {
            Collection policyTypes = PolicyType.getPolicyTypes();
            for (Iterator i = policyTypes.iterator(); i.hasNext();) {
                policyType = (PolicyType) i.next();
                addPolicyBuilderNames(names, policyType, connection);
            }
        } else {
            addPolicyBuilderNames(names, policyType, connection);
        }
        return names;
    }

    // Javadoc inherited.
    protected void addPolicyBuilderNames(
            List names, PolicyType policyType,
            final RepositoryConnection connection)
            throws RepositoryException {

        RepositoryEnumeration enumeration =
                accessor.enumeratePolicyBuilderNames(connection, project,
                        policyType);
        try {
            while (enumeration.hasNext()) {
                String name = (String) enumeration.next();
                names.add(name);
            }
        } finally {
            enumeration.close();
        }
    }

    public Collection getPolicyNames(PolicyType policyType)
            throws RepositoryException {
        return getPolicyBuilderNames(policyType);
    }

    public boolean removePolicy(String name)
            throws RepositoryException {

        return removePolicyBuilder(name);
    }
}
