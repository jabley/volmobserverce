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

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.devices.DeviceEditor;
import com.volantis.mcs.eclipse.ab.search.devices.DeviceSearchMatch;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.IEditorPart;

/**
 * Class to support generic search requirements.
 */
public class SearchSupport {
    /**
     * Given a List and an IResource populate the List with all the IFile
     * descendants of the IResource including the IResource itself that have
     * one of the FileExtensions provided.
     *
     * @param fileMembers the List to be populated with IFile members
     * @param resource whose self and descendent applicable IFile members are
     * used to populate fileMembers
     * @param fileExtensions the FileExtensions that designate the IFile
     * descendents of container that are applicable to the fileMembers List.
     * If fileExtensions is null then all descendants are included as members.
     */
    static void populateFileMembers(List fileMembers, IResource resource,
                                    FileExtension[] fileExtensions) {
        try {
            if (resource instanceof IContainer) {
                IResource[] resources = ((IContainer) resource).members();
                for (int i1 = 0; i1 < resources.length; i1++) {
                    if (resources[i1] instanceof IContainer) {
                        populateFileMembers(fileMembers, resources[i1],
                                fileExtensions);
                    } else if (resources[i1] instanceof IFile) {
                        IFile file = (IFile) resources[i1];
                        addFileToScope(fileMembers, file, fileExtensions);
                    } else {
                        throw new IllegalArgumentException("Expected an" +
                                " IContainer or an IFile but got: " +
                                resources[i1].getClass().getName());
                    }
                }
            } else if (resource instanceof IFile) {
                addFileToScope(fileMembers, (IFile) resource, fileExtensions);
            }
        } catch (CoreException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Add the given IFile to a List representing the scope if the file
     * has one of a given collection of FileExtensions or FileExtensions is
     * null.
     * @param scope the scope
     * @param file the IFile to add
     * @param fileExtensions the FileExtensions that determine if the file
     * should be added to the scope. If this is null file will always be
     * added.
     */
    private static void addFileToScope(List scope, IFile file,
                               FileExtension[] fileExtensions) {
        boolean extensionIsInScope = fileExtensions == null;
        if (!extensionIsInScope) {
            FileExtension extension = FileExtension.
                    getFileExtensionForExtension(file.
                    getFileExtension());
            for (int i2 = 0; i2 < fileExtensions.length &&
                    !extensionIsInScope; i2++) {
                extensionIsInScope =
                        extension == fileExtensions[i2];
            }
        }
        if (extensionIsInScope) {
            scope.add(file);
        }
    }

    /**
     * Goto to the given selection in the device editor opening the
     * device editor if necessary. If the selection does not include
     * DeviceSearchMatch objects then this method will have no effect.
     * @param selection the ISelection which is expected to be an
     * IStructuredSelection.
     */
    public static void gotoSelection(ISelection selection,
                                     IWorkbenchPage workbenchPage) {
        IStructuredSelection structuredSelection =
                (IStructuredSelection) selection;

        // The selection may contain IContainers as well as
        // DeviceSearchMatch objects for several different resources.
        // However we can only goto a selection when there is a single
        // selected object - at least that is the Eclipse convention anyway.
        if (structuredSelection.size() == 1 &&
                structuredSelection.getFirstElement()
                instanceof DeviceSearchMatch) {
            DeviceSearchMatch match =
                    (DeviceSearchMatch) structuredSelection.getFirstElement();

            // Find an editor with which to open the selected match resource.
            IFile matchedFile = match.getFile();
            IEditorDescriptor defaultEditorDescriptor =
                    PlatformUI.getWorkbench().getEditorRegistry().
                    getDefaultEditor(matchedFile.getName());
            try {
                // If device repository is active, workbenchPage returns it -
                // it is wrong, this class can not be used for showing selection.
                // We close all active device repository while expect active
                // device editor or nothing active
                IEditorPart editor;
                while((editor=workbenchPage.getActiveEditor()) != null) {
                    if(editor instanceof MultiPageEditorPart) break;
                    workbenchPage.closeEditor(editor,false);
                }

                DeviceEditor deviceEditor = (DeviceEditor) workbenchPage.
                        openEditor(new FileEditorInput(matchedFile),
                                defaultEditorDescriptor.getId(), true);
                deviceEditor.selectDevice(match.getDeviceName());

            } catch (PartInitException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            }
        }
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
