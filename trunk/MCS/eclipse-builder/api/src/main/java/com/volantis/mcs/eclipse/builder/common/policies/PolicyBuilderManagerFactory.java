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
package com.volantis.mcs.eclipse.builder.common.policies;

import com.volantis.mcs.project.PolicyBuilderManager;
import com.volantis.synergetics.factory.MetaDefaultFactory;
import org.eclipse.core.resources.IProject;

/**
 */
public abstract class PolicyBuilderManagerFactory {
    /**
     * Set up the meta default factory instance
     */
    private static MetaDefaultFactory metaDefaultFactory =
            new MetaDefaultFactory(
            "com.volantis.mcs.eclipse.builder.common.policies.impl.DefaultPolicyBuilderManagerFactory",
            PolicyBuilderManagerFactory.class.getClassLoader());

    /**
     * @return the default instance of the factory.
     */
    public static PolicyBuilderManagerFactory getDefaultInstance() {
        return (PolicyBuilderManagerFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Gets a policy builder manager that can access the policies associated
     * with the specified project in their on-disc form.
     *
     * @param project The project that the policy builder manager should access
     * @return A policy builder manager
     */
    public abstract PolicyBuilderManager createPolicyBuilderManager(IProject project);
}
