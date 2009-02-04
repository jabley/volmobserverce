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

package com.volantis.mcs.runtime.repository.remote.xml;

import com.volantis.mcs.policies.PolicyBuilderReader;
import com.volantis.mcs.runtime.project.ProjectManager;

/**
 * Creates remote readers.
 *
 * <p>This is needed to allow {@link ProjectManager} to create
 * {@link PolicyBuilderReader} for remote projects as part of its
 * initialisation.</p>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @mock.generate
 */
public interface RemoteReadersFactory {

    /**
     * Create {@link PolicyBuilderReader} for the specified
     * {@link ProjectManager}.
     *
     * @param projectManager The project manager.
     * @return The newly instantiated {@link PolicyBuilderReader}.
     */
    PolicyBuilderReader createPolicyBuilderReader(
            ProjectManager projectManager);

    /**
     * Create {@link RemotePolicyBuildersReader} for the specified
     * {@link ProjectManager}.
     *
     * @param projectManager The project manager.
     * @return The newly instantiated {@link RemotePolicyBuildersReader}.
     */
    RemotePolicyBuildersReader createPolicyBuildersReader(
            ProjectManager projectManager);
}
