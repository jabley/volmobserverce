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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.project;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds configuration information about the overall project configuration.
 * <p>
 * This corresponds to the mcs-config/projects element.
 */ 
public class ProjectsConfiguration  {

    /**
     * The default project to use in this instance of MCS.
     */ 
    private RuntimeProjectConfiguration defaultProject;

    /**
     * A map from project name to {@link RuntimeProjectConfiguration}.
     */
    private Map namedProjects = new HashMap();

    /**
     * Returns the default project.
     */ 
    public RuntimeProjectConfiguration getDefaultProject() {
        return defaultProject;
    }

    /**
     * Sets the default project.
     */ 
    public void setDefaultProject(RuntimeProjectConfiguration defaultProject) {
        this.defaultProject = defaultProject;
    }

    /**
     * Get the named projects.
     *
     * @return Map from name to {@link RuntimeProjectConfiguration}.
     */
    public Map getNamedProjects() {
        return namedProjects;
    }

    /**
     * Adds a named project
     */ 
    public void addProject(RuntimeProjectConfiguration project) {
        String name = project.getName();
        if (name == null) {
            throw new IllegalArgumentException("Project must have a name");
        }

        namedProjects.put(name, project);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Jan-04	2724/3	geoff	VBM:2004011911 Add projects to config (whoops - add javadoc)

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 ===========================================================================
*/
