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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common;

import org.eclipse.core.resources.IProject;

/**
 * An implementation of both a ProjectProvider and ProjectReceiver that
 * provides the project it receives.
 */
public class ProjectProviderReceiver
        implements ProjectProvider, ProjectReceiver {
    /**
     * The project.
     */
    private IProject project;

    // javadoc inherited
    public IProject getProject() {
        return project;
    }

    // javadoc inherited
    public void setProject(IProject project) {
        this.project = project;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Feb-04	2985/1	allan	VBM:2004012803 Fix for null project in ProjectProviders

 ===========================================================================
*/
