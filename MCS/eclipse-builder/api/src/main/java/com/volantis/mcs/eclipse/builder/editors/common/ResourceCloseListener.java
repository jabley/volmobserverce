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
package com.volantis.mcs.eclipse.builder.editors.common;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.swt.widgets.Display;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;

/**
 * A listener to close the editor for a resource if the file representing that
 * resource is moved or deleted, or if the project containing it is closed or
 * deleted.
 */
public class ResourceCloseListener implements IResourceChangeListener {
    /**
     * The editor to monitor.
     */
    private EditorPart resourceEditor;

    /**
     * True if the listener is currently monitoring changes to the editor's
     * resource.
     */
    private boolean started = false;

    public ResourceCloseListener(EditorPart editorPart) {
        resourceEditor = editorPart;
    }

    // Javadoc inherited
    public void resourceChanged(IResourceChangeEvent event) {
        final IFile file =
                ((FileEditorInput) resourceEditor.getEditorInput()).getFile();

        // Close the editor if the resource's project was closed/deleted
        if ((event.getType() == IResourceChangeEvent.PRE_DELETE
                || event.getType() == IResourceChangeEvent.PRE_CLOSE)
                && event.getResource().equals(file.getProject())) {
            closeEditor();
        }

        // Close the editor if its resource was deleted/moved
        if ((event.getType() == IResourceChangeEvent.POST_CHANGE)) {
            IResourceDelta delta = event.getDelta();
            try {
                delta.accept(new IResourceDeltaVisitor() {
                    public boolean visit(IResourceDelta iResourceDelta) {
                        if (file.equals(iResourceDelta.getResource()) &&
                                (iResourceDelta.getKind() ==
                                                IResourceDelta.REMOVED ||
                                 iResourceDelta.getKind() ==
                                                IResourceDelta.MOVED_FROM)) {
                            closeEditor();
                        }
                        return true;
                    }
                });
            } catch (CoreException ce) {
                BuilderPlugin.logError(getClass(), ce);
            }
        }
    }

    /**
     * Close the editor.
     */
    private void closeEditor() {
        Display.getDefault().syncExec(new Runnable() {
                public void run() {
                    resourceEditor.getSite().getPage().
                            closeEditor(resourceEditor, false);
                    stopListener();
                }
            });
    }

    /**
     * Start listening for changes.
     */
    public synchronized void startListener() {
        if (!started) {
            ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
            started = true;
        }
    }

    /**
     * Stop listening for changes.
     */
    public synchronized void stopListener() {
        if (started) {
            ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
            started = false;
        }
    }
}
