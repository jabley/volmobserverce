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
import com.volantis.mcs.remote.PolicyBuilders;

/**
 * Encapsulates a {@link PolicyBuilders} and the {@link Project} to which
 * it belongs.
 */
public class PolicyBuildersResponse {

    /**
     * The owning project.
     */
    private final Project project;

    /**
     * The retrieved builders.
     */
    private final PolicyBuilders builders;

    /**
     * Initialise.
     *
     * @param project  The owning project.
     * @param builders The retrieved builders.
     */
    public PolicyBuildersResponse(Project project, PolicyBuilders builders) {
        this.project = project;
        this.builders = builders;
    }

    /**
     * Get the owning project.
     *
     * @return The owning project.
     */
    public Project getProject() {
        return project;
    }

    /**
     * Get the retrieved builders.
     *
     * @return The retrieved builders.
     */
    public PolicyBuilders getBuilders() {
        return builders;
    }
}
