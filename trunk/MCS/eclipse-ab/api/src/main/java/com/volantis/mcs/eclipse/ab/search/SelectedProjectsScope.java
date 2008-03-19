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
package com.volantis.mcs.eclipse.ab.search;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import com.volantis.mcs.objects.FileExtension;

/**
 * SearchScope derived from selected projects.
 */
public class SelectedProjectsScope implements SearchScope {

    /**
     * The prefix for resources messages associated with this class.
     */
    private static final String RESOURCE_PREFIX = "SelectedProjectsScope.";

    /**
     * The label for this SearchScope.
     */
    private static final String LABEL =
            SearchMessages.getString(RESOURCE_PREFIX + "label");

    /**
     * The ISelection associated with this scope.
     */
    private final ISelection selection;

    /**
     * The FileExtensions applicable to the search.
     */
    private final FileExtension[] fileExtensions;

    /**
     * Construct a new SelectedProjectsScope.
     * @param selection the ISelection for this SelectedProjectsScope.
     * @param fileExtensions the FileExtensions that are applicable to the
     * scope. If this parameter is null then all files are applicable.
     */
    SelectedProjectsScope(ISelection selection,
                          FileExtension[] fileExtensions) {
        assert(selection != null);
        this.selection = selection;
        this.fileExtensions = fileExtensions;
    }

    // javadoc inherited
    public String getLabel() {
        return LABEL;
    }

    // javadoc inherited
    public IFile[] getFiles() {
        IFile files [];
        if (!selection.isEmpty()) {
            List fileMembers = new ArrayList();
            IStructuredSelection structuredSelection =
                    (IStructuredSelection) selection;
            Iterator adaptables = structuredSelection.iterator();
            List projects = new ArrayList();
            do {
                IAdaptable adaptable = (IAdaptable) adaptables.next();
                if (adaptable instanceof IResource) {
                    IProject project = ((IResource) adaptable).getProject();
                    if (!projects.contains(project)) {
                        SearchSupport.populateFileMembers(fileMembers,
                                ((IResource) adaptable).getProject(),
                                fileExtensions);
                        projects.add(project);
                    }
                }
            } while (adaptables.hasNext());

            files = new IFile[fileMembers.size()];

            files = (IFile[]) fileMembers.toArray(files);
        } else {
            files = new IFile[0];
        }

        return files;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/3	allan	VBM:2004070608 Device search

 ===========================================================================
*/
