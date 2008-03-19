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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.runtime.policies.cache.PolicyCache;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Runtime specific factory for policy related classes.
 */
public abstract class RuntimePolicyFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.impl.mcs.runtime.policies.RuntimePolicyFactoryImpl",
                        RuntimePolicyFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static RuntimePolicyFactory getDefaultInstance() {
        return (RuntimePolicyFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create an {@link ActivatedPolicyRetriever} that will cache the results.
     *
     * @param activator      The policy activator.
     * @param projectManager The project manager.
     * @param cache          The cache.
     * @return A new {@link ActivatedPolicyRetriever} instance.
     */
    public abstract ActivatedPolicyRetriever createCachingRetriever(
            PolicyActivator activator,
            ProjectManager projectManager, PolicyCache cache);

    /**
     * Create a {@link ActivatedPolicyRetriever} for activating policies.
     *
     * @param activator The policy activator.
     * @param projectManager The project manager.
     * @return A new {@link ActivatedPolicyRetriever} instance.
     */
    public abstract ActivatedPolicyRetriever createActivatingRetriever(
            PolicyActivator activator,
            ProjectManager projectManager);

}
