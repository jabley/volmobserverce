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
 * Encapsulates a {@link PolicyBuilder} and the {@link Project} to which
 * it belongs.
 */
public class PolicyBuilderResponse {

    /**
     * The owning project.
     */
    private final Project project;

    /**
     * The retrieved builder.
     */
    private final PolicyBuilder builder;

    /**
     * Initialise.
     *
     * @param project The owning project.
     * @param builder The retrieved builder.
     */
    public PolicyBuilderResponse(Project project, PolicyBuilder builder) {
        this.project = project;
        this.builder = builder;
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
     * Get the retrieved builder.
     *
     * @return The retrieved builder.
     */
    public PolicyBuilder getBuilder() {
        return builder;
    }
}
