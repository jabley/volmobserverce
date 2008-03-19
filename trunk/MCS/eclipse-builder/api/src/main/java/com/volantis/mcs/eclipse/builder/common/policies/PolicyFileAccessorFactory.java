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

import com.volantis.synergetics.factory.MetaDefaultFactory;
import com.volantis.mcs.eclipse.builder.common.ClassVersionProperties;
import org.eclipse.core.resources.IProject;

/**
 * Factory for PolicyFileAccessors.
 */
public abstract class PolicyFileAccessorFactory {
    /**
     * Dynamically load the default factory instance.
     */
    private static final PolicyFileAccessorFactory DEFAULT_INSTANCE =
            (PolicyFileAccessorFactory) ClassVersionProperties.getInstance(
                    "PolicyFileAccessorFactory.class");

    /**
     * Get the default policy file accessor factory instance.
     *
     * @return The default policy file accessor factory instance
     */
    public static PolicyFileAccessorFactory getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Returns the policy file accessor for a specified project.
     *
     * @param project The project for which the policy file accessor should be
     *                retrieved
     * @return A policy accessor appropriate to the project
     */
    public abstract PolicyFileAccessor getPolicyFileAccessor(IProject project);
}
