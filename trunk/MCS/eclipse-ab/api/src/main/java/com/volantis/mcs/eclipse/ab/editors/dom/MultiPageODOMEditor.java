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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoInfo;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.IElementStateListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.jface.dialogs.ErrorDialog;

import java.text.MessageFormat;

/**
 * A base multi-page editor implementation for ODOMEditor and potentially
 * other XML type editor parts.
 */
public abstract class MultiPageODOMEditor extends MultiPageEditorPart {
    /**
     * The prefix for resources associated with MultiPageODOMEditor.
     */
    private static final String RESOURCE_PREFIX = "MultiPageODOMEditor."; //$NON-NLS-1$

    /**
     * The ODOMEditorContext for this MultiPageEditor.
     */
    private ODOMEditorContext context;

    /**
     * The root element name.
     */
    private final String rootElementName;

    /**
     * The isDirty status of this editor.
     */
    private boolean isDirty;

    /**
     * The file synchronizer that will ensure that the editor is closed if its
     * file is deleted.
     */
    private FileSynchronizer synchronizer;

    /**
     * The listener that is associated with standardized element changes such
     * as deletion, moving, content replacement and dirty state changes).
     */
    private IElementStateListener listener;

    /**
     * Construct a new MultiPageODOMEditor.
     * @param rootElementName The name of the rootElement for this editor.
     */
    protected MultiPageODOMEditor(String rootElementName) {
        this.rootElementName = rootElementName;
    }


    // javadoc inherited
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException {
        IFile file = null;
        if (input instanceof IFileEditorInput) {
            file = ((IFileEditorInput) input).getFile();
        } else {
            throw new IllegalStateException("Unsupported IEditorInput: " + //$NON-NLS-1$
                    getEditorInput());
        }

        context = createODOMEditorContext(rootElementName, file);

        // If context was not created then there was a problem so we
        // cannot initialize this editor.
        if (context != null) {
            setTitle(file.getName());

            // Create and install the file synchronizer.
            synchronizer = new FileSynchronizer((IFile) context.getPolicyResource());
            synchronizer.install();

            // Create and add the element state listener to the synchronizer.
            listener = new IElementStateListener() {
                public void elementDirtyStateChanged(Object element, boolean isDirty) {
                }

                public void elementContentAboutToBeReplaced(Object element) {
                }

                public void elementContentReplaced(Object element) {
                }

                public void elementDeleted(Object element) {
                    if (!MultiPageODOMEditor.this.isDirty()) {
                        close(true);
                    }
                }

                public void elementMoved(Object originalElement, Object movedElement) {
                }
            };
            synchronizer.addElementStateListener(listener);
            super.init(site, input);
        } else {
            String message = EditorMessages.getString(RESOURCE_PREFIX +
                    "contextCreationFailure.message"); //$NON-NLS-1$
            MessageFormat messageFormat =
                    new MessageFormat(message);
            String args [] = {file.getName()};


            throw new PartInitException(messageFormat.format(args));
        }
    }

    /**
     * Shows a dialog warning the user that they have cancelled the
     * MCSProjectAssignmentWizard and that they will not be able to edit
     * The MCS resource they are attempting to edit, unless they complete
     * the wizard
     * @param e the PartInitException thrown
     */
    protected void showMCSProjectAssignmentCancelledDialog(PartInitException e) {
        IWorkbenchWindow activeWindow =
                PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        Shell parentShell = activeWindow.getShell();
        String pluginId = ABPlugin.getDefault().
                getDescriptor().getUniqueIdentifier();
        String errorTitle = EditorMessages.getString(RESOURCE_PREFIX +
                "projectAssignmentWizardCancelled.title");
        String errorMessage = EditorMessages.getString(RESOURCE_PREFIX +
                "projectAssignmentWizardCancelled.message");
        ErrorDialog.openError(parentShell, errorTitle, errorMessage,
                new Status(IStatus.WARNING,
                        pluginId,
                        IStatus.WARNING,
                        e.getMessage(),
                        null), Status.WARNING);
    }


    /**
     * Close the editor asynchronously.
     * @param save the save parameter.
     */
    protected void close(final boolean save) {
        Display display = getSite().getShell().getDisplay();
        display.asyncExec(new Runnable() {
            public void run() {
                getSite().getPage().closeEditor(MultiPageODOMEditor.this, save);
            }
        });
    }


    /**
     * Create the ODOMEditorContext for this MultiPageODOMEditor. Sub-classes
     * should override this method to provide their own context.
     * @param rootElementName The name of the root element for the policy
     * being edited.
     * @param file The IFile associated with the policy being edited.
     */
    protected ODOMEditorContext createODOMEditorContext(String rootElementName,
                                                        IFile file) {
        return ODOMEditorContext.createODOMEditorContext(rootElementName,
                file,
                createUndoRedoMementoOriginator());
    }

    /**
     * Get the ODOMEditorContext associated with this MultiPageODOMEditor.
     * @return context.
     */
    protected ODOMEditorContext getODOMEditorContext() {
        return context;
    }


    /**
     * This method is an overridable <strong>step</step> of
     * {@link #createODOMEditorContext}
     * @return a memento originator for the ODOMEditorContext's UndoRedoManger
     */
    protected UndoRedoMementoOriginator createUndoRedoMementoOriginator() {
        return new MPOEMementoOriginator();
    }


    /**
     * Implementation of {@link UndoRedoMemento}
     * that stores information about the current page
     * in this multi-page editor.
     */
    protected class MPOEMemento implements UndoRedoMemento {
        // javadoc unnecessary
        protected int pageIndex;

        // javadoc inherited
        public MPOEMemento() {
            this.pageIndex = getActivePage();
        }

        // javadoc inherited
        public int getPageIndex() {
            return pageIndex;
        }
    }


    /**
     * Implementation of {@link UndoRedoMementoOriginator} that restores
     * the page stored in the associated 
     * {@link MultiPageODOMEditor.MPOEMemento}
     * and then delegates to the single-page implementation
     */
    protected class MPOEMementoOriginator implements UndoRedoMementoOriginator {
        // javadoc inherited
        public UndoRedoMemento takeSnapshot() {
            return new MPOEMemento();
        }

        // javadoc inherited
        public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
            MPOEMemento memento = (MPOEMemento) undoRedoInfo.getMemento();

            setActivePage(memento.getPageIndex());

            IEditorPart currentEditor = getEditor(memento.getPageIndex());
            if (currentEditor instanceof ODOMEditorPart) {
                ((ODOMEditorPart) currentEditor).restoreSnapshot(undoRedoInfo);
            }
        }
    }

    // javadoc inherited
    protected abstract void createPages();

    /**
     * Override handlePropertyChange() in order to maintain the dirty
     * status of this MultiPageEditor.
     */
    // rest of javadoc inherited
    protected void handlePropertyChange(int propertyId) {
        if (propertyId == PROP_DIRTY) {
            boolean activeEditorIsDirty = getActiveEditor().isDirty();
            isDirty = activeEditorIsDirty;
        }
        super.handlePropertyChange(propertyId);
    }

    /**
     * Override isDirty() to use our isDirty flag rather than inspecting the
     * dirty status of nested editors.
     */
    // rest of javadoc inherited
    public boolean isDirty() {
        return isDirty;
    }

    // javadoc inherited
    public void doSave(IProgressMonitor progressMonitor) {
        getActiveEditor().doSave(progressMonitor);
    }

    // javadoc inherited
    public void doSaveAs() {
        getActiveEditor().doSaveAs();
    }

    // javadoc inherited
    public void gotoMarker(IMarker marker) {
        ((IGotoMarker)getActiveEditor()).gotoMarker(marker);
    }

    // javadoc inherited
    public boolean isSaveAsAllowed() {
        return getActiveEditor().isSaveAsAllowed();
    }

    // javadoc inherited
    public void dispose() {
        if (synchronizer != null) {
            synchronizer.removeElementStateListener(listener);
            synchronizer.uninstall();
        }
        context.dispose();

        super.dispose();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 18-May-04	4231/4	tom	VBM:2004042704 rework for 2004042704

 05-May-04	4115/1	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 15-Apr-04	3881/1	steve	VBM:2004032606 Allow assignment of MCS nature to imported projects

 08-Apr-04	3813/1	byron	VBM:2004040604 Deleting Components from mcs-policies does not follow the eclipse standard

 08-Apr-04	3796/1	byron	VBM:2004040604 Deleting Components from mcs-policies does not follow the eclipse standard

 17-Feb-04	2988/4	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 21-Jan-04	2659/1	allan	VBM:2003112801 RuleSection basics (read only)

 19-Jan-04	2665/1	allan	VBM:2003112702 Provide ThemeEditorContext.

 19-Jan-04	2562/5	allan	VBM:2003112010 Handle outline view showing and closing.

 18-Jan-04	2562/3	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 07-Jan-04	2447/1	philws	VBM:2004010609 Initial code for revised validation mechanism

 06-Jan-04	2412/3	allan	VBM:2004010407 Rework issues.

 06-Jan-04	2412/1	allan	VBM:2004010407 Fixed dirty status handling when switching editor page.

 05-Jan-04	2380/1	allan	VBM:2004010406 Improve handling of non-well-formed XML in policy files.

 04-Jan-04	2309/2	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 ===========================================================================
*/
