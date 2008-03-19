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

package com.volantis.mcs.policies;

import com.volantis.mcs.project.Project;

/**
 * Reads {@link PolicyBuilder} from any {@link Project} to be read without
 * having to worry about handling connections.
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
public interface PolicyBuilderReader {

    /**
     * Get the {@link PolicyBuilder}.
     *
     * @param project The project from which the builder should be read.
     * @param name    The name of the policy.
     * @return A {@link PolicyBuilderResponse} encapsulating the
     *         {@link PolicyBuilder} as well as the {@link Project} that it was
     *         actually retrieved from which may not be the same as the
     *         specified project.
     */
    PolicyBuilderResponse getPolicyBuilder(Project project, String name);
}
