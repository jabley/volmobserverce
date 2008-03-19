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

package com.volantis.mcs.runtime.policies.cache;

import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.synergetics.ObjectHelper;

/**
 * A project specific key.
 */
public class ProjectSpecificKey {

    /**
     * The project.
     */
    private final RuntimeProject project;

    /**
     * The name.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param project The project.
     * @param name    The name.
     */
    public ProjectSpecificKey(RuntimeProject project, String name) {
        if (project == null) {
            throw new IllegalArgumentException("project cannot be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }

        this.project = project;
        this.name = name;
    }

    /**
     * Get the project.
     *
     * @return The project.
     */
    public RuntimeProject getProject() {
        return project;
    }

    /**
     * Get the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    // Javadoc inherited.
    public String toString() {
        return project.toString() + " - " + name;
    }

    // Javadoc inherited.
    public int hashCode() {
        int result = 0;
        result = 37 * result + ObjectHelper.hashCode(project);
        result = 37 * result + name.hashCode();
        return result;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!(obj instanceof ProjectSpecificKey)) {
            return false;
        }

        ProjectSpecificKey other = (ProjectSpecificKey) obj;
        return project == other.project && name.equals(other.name);
    }
}
