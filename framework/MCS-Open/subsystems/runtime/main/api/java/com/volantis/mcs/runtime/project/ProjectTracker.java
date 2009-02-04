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

package com.volantis.mcs.runtime.project;

import com.volantis.mcs.context.ProjectStack;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.utilities.MarinerURL;

public class ProjectTracker {

    private final BaseURLTracker baseURLTracker;

    private final ProjectManager projectManager;

    private final ProjectStack projectStack;
    
    public ProjectTracker(
            BaseURLTracker baseURLTracker, ProjectManager projectManager,
            ProjectStack projectStack) {
        
        this.baseURLTracker = baseURLTracker;
        this.projectManager = projectManager;
        this.projectStack = projectStack;
    }

    public void startElement(String systemId) {

        // Get the project of the enclosing elements. This must never be null.
        RuntimeProject enclosingProject = (RuntimeProject)
                projectStack.getCurrentProject();
        if (enclosingProject == null) {
            throw new IllegalStateException("Current project " +
                    "must never be null");
        }

        // Check whether the base URL is different from the enclosing one, if
        // it is then we need to find the project associated with it, otherwise
        // it is the same as the enclosing one.
        boolean baseURLChanged = baseURLTracker.startElement(systemId);
        RuntimeProject newProject;
        if (baseURLChanged) {
            MarinerURL baseURL = baseURLTracker.getBaseURL();
            String baseURLAsString = baseURL.getExternalForm();
            newProject = projectManager.getProject(baseURLAsString,
                                enclosingProject);
            if (newProject == null) {
                // No project could be found so use the enclosing one.
                newProject = enclosingProject;
            } else {

                // If the enclosing project extends the new project then keep
                // using it as otherwise any policies referenced within the
                // page will look in the base project rather than the
                // extended one.
                if (enclosingProject.extendsProject(newProject)) {
                    newProject = enclosingProject;
                }
            }
        } else {
            // The base URL has not changed so use the enclosing project.
            newProject = enclosingProject;
        }
        projectStack.pushProject(newProject);
    }

    public void endElement(String systemId) {
        // Pop the current project.
        projectStack.popProject(null);

        // Update the base URL tracker.
        baseURLTracker.endElement(systemId);
    }
}
