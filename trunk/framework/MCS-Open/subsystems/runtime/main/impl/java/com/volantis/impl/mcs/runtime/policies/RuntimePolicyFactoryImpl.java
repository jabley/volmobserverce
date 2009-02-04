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

package com.volantis.impl.mcs.runtime.policies;

import com.volantis.mcs.runtime.policies.ActivatedPolicyRetriever;
import com.volantis.mcs.runtime.policies.PolicyActivator;
import com.volantis.mcs.runtime.policies.RuntimePolicyFactory;
import com.volantis.mcs.runtime.policies.cache.PolicyCache;
import com.volantis.mcs.runtime.project.ProjectManager;

/**
 * Implementation of {@link RuntimePolicyFactory}.
 */
public class RuntimePolicyFactoryImpl
        extends RuntimePolicyFactory {

    // Javadoc inherited.
    public ActivatedPolicyRetriever createCachingRetriever(
            PolicyActivator activator,
            ProjectManager projectManager, PolicyCache cache) {

        ActivatedPolicyRetriever retriever = createActivatingRetriever(
                activator, projectManager);
        return new CachingActivatedPolicyRetriever(cache, retriever);
    }

    public ActivatedPolicyRetriever createActivatingRetriever(
            PolicyActivator activator,
            ProjectManager projectManager) {

        return new ActivatedPolicyRetrieverImpl(activator, projectManager);
    }

}
