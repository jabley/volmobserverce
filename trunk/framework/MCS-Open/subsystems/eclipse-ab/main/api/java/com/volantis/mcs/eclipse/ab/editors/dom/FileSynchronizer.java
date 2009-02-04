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

package com.volantis.mcs.eclipse.ab.editors.dom;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.IElementStateListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.jface.text.Assert;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.ab.ABPlugin;

/**
 * Synchronizes the document with external resource changes.
 */
class FileSynchronizer implements IResourceChangeListener, IResourceDeltaVisitor {

    /**
     * The file
     */
    protected IFile file;

    /**
     * A flag indicating whether this synchronizer is installed or not.
     */
    protected boolean installed = false;

    private List elementStateListeners = new ArrayList();

    /**
     * Creates a new file synchronizer.
     *
     * @param file the file input to be synchronized
     */
    public FileSynchronizer(IFile file) {
        this.file = file;
    };

    /**
     * Returns the file wrapped by the file editor input.
     *
     * @return the file wrapped by the editor input associated with that synchronizer
     */
    protected IFile getFile() {
        return file;
    }

    /**
     * Installs the synchronizer on the input's file.
     */
    public void install() {
        getFile().getWorkspace().addResourceChangeListener(this);
        installed = true;
    }

    /**
     * Uninstalls the synchronizer from the input's file.
     */
    public void uninstall() {
        getFile().getWorkspace().removeResourceChangeListener(this);
        installed = false;
    }

    /*
     * @see IResourceChangeListener#resourceChanged(IResourceChangeEvent)
     */
    public void resourceChanged(IResourceChangeEvent e) {
        IResourceDelta delta = e.getDelta();
        try {
            if (delta != null && installed) {
                delta.accept(this);
            }
        } catch (CoreException x) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), x);
        }
    }

    /*
     * @see IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
     */
    public boolean visit(IResourceDelta delta) throws CoreException {

        if (delta != null && getFile().equals(delta.getResource())) {
            Runnable runnable = null;

            switch (delta.getKind()) {
                case IResourceDelta.REMOVED:
                    if ((IResourceDelta.MOVED_TO & delta.getFlags()) != 0) {
                        // do nothing
                    } else {
                        runnable = new Runnable() {
                            public void run() {
                                handleElementDeleted(getFile());
                            }
                        };
                    }
                    break;
            }

            if (runnable != null) {
                update(runnable);
            }
        }

        return true;
    }

    /**
     * Handle the deletion event which notifies the relevant listeners.
     * @param element the elment that has been deleted.
     */
    private void handleElementDeleted(Object element) {
        Iterator e = new ArrayList(elementStateListeners).iterator();
        while (e.hasNext()) {
            IElementStateListener l = (IElementStateListener) e.next();
            l.elementDeleted(element);
        }
    }

    /**
     * Posts the update code "behind" the running operation.
     *
     * @param runnable the update code
     */
    protected void update(Runnable runnable) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
        if (windows != null && windows.length > 0) {
            Display display = windows[0].getShell().getDisplay();
            display.asyncExec(runnable);
        } else {
            runnable.run();
        }
    }

    public void addElementStateListener(IElementStateListener listener) {
        Assert.isNotNull(listener);
        if (!elementStateListeners.contains(listener))
            elementStateListeners.add(listener);
    }

    /*
     * @see IDocumentProvider#removeElementStateListener(IElementStateListener)
     */
    public void removeElementStateListener(IElementStateListener listener) {
        Assert.isNotNull(listener);
        elementStateListeners.remove(listener);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Apr-04	3796/1	byron	VBM:2004040604 Deleting Components from mcs-policies does not follow the eclipse standard

 ===========================================================================
*/
