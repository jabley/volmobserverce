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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.project;

import com.volantis.mcs.runtime.RuntimeProject;

import java.util.Map;

public class PredefinedProjects {

    private final RuntimeProject defaultProject;

    private final Map namedProjects;

    public PredefinedProjects(RuntimeProject defaultProject, Map namedProjects) {
        this.defaultProject = defaultProject;
        this.namedProjects = namedProjects;
    }

    public RuntimeProject getDefaultProject() {
        return defaultProject;
    }

    public Map getNamedProjects() {
        return namedProjects;
    }
}
