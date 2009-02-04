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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.volantis.mcs.objects.FileExtension;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IWorkingSet;

/**
 * A SearchScope derived from collection of "Working Sets" - an Eclipse concept
 * designating a set of resources.
 */
public class WorkingSetScope implements SearchScope {

    /**
     * The prefix for resources messages associated with this class.
     */
    private static final String RESOURCE_PREFIX = "WorkingSetScope.";

    /**
     * The label for this SearchScope.
     */
    private static final MessageFormat LABEL_FORMAT = new MessageFormat(
            SearchMessages.getString(RESOURCE_PREFIX + "label"));

    /**
     * The IWorkingSets that are applicable to this SearchScope.
     */
    private final IWorkingSet[] workingSets;

    /**
     * The FileExtensions that are applicable to this SearchScope.
     */
    private final FileExtension[] fileExtensions;

    /**
     * The label for this scope.
     */
    private String label;

    /**
     * Construct a new WorkingSetScope.
     * @param workingSets the array of IWorkingSet objects that describe the
     * scope.
     * @param fileExtensions the FileExtensions that are applicable to the
     * scope. If this parameter is null then all files are applicable.
     */
    WorkingSetScope(IWorkingSet[] workingSets,
                    FileExtension[] fileExtensions) {
        assert(workingSets != null);
        assert(workingSets.length > 0);
        this.workingSets = workingSets;
        this.fileExtensions = fileExtensions;


        StringBuffer buffer = new StringBuffer(workingSets[0].getName());
        for (int i = 1; i < workingSets.length; i++) {
            buffer.append(", ").append(workingSets[i].getName());
        }
        String workingSetNames = buffer.toString();
        label = LABEL_FORMAT.format(new Object[]{workingSetNames});
    }

    // javadoc inherited
    public String getLabel() {
        return label;
    }

    // javadoc inherited
    public IFile[] getFiles() {
        List fileMembers = new ArrayList();
        for (int i1 = 0; i1 < workingSets.length; i1++) {
            IAdaptable adaptables [] = workingSets[i1].getElements();
            for (int i2 = 0; i2 < adaptables.length; i2++) {
                if (adaptables[i2] instanceof IResource) {
                    SearchSupport.populateFileMembers(fileMembers,
                            (IResource) adaptables[i2], fileExtensions);
                }
            }
        }

        IFile files [] = new IFile[fileMembers.size()];

        return (IFile[]) fileMembers.toArray(files);
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
