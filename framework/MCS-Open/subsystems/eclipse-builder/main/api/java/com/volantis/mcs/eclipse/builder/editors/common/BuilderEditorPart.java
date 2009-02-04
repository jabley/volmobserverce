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

import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.policies.PolicyBuilder;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Common superclass for all GUI editor parts (editors and editor pages) using
 * the new model.
 */
public abstract class BuilderEditorPart extends EditorPart
        implements InteractionFocussable {
    private static final String RESOURCE_PREFIX = "BuilderEditorPart.";

    private static final String SAVE_ERROR_TITLE =
            EditorMessages.getString(RESOURCE_PREFIX + "saveError.title");

    private static final String SAVE_ERROR_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "saveError.message");

    private static final String MISSING_FILE_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "loadError.deleted");

    private static final String LOAD_ERROR_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "loadError.general");

    private EditorContext context;

    /**
     * A listener for changes in the dirty state of the editor context that
     * propagates the change in dirty state to the Eclipse GUI.
     */
    private DirtyStateListener dirtyStateListener = new DirtyStateListener() {
        public void dirtyStateChanged(boolean dirty) {
            firePropertyChange(PROP_DIRTY);
        }
    };

    public BuilderEditorPart(EditorContext context) {
        this.context = context;
        context.addDirtyStateListener(dirtyStateListener);
    }

    // Javadoc inherited
    public void init(IEditorSite iEditorSite, IEditorInput iEditorInput)
            throws PartInitException {
        IFile file = getFile(iEditorInput);
        try {
            context.loadResource(file);
            setSite(iEditorSite);
            setInput(iEditorInput);
            setPartName(file.getName());

            InteractionEventListener listener = new InteractionEventListenerAdapter() {
                public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
                }

                // Javadoc inherited
                protected void interactionEvent(InteractionEvent event) {
                    if (event.isOriginator ()) {
                        context.setDirty(true);
                    }
                }
            };
            Proxy proxy = context.getInteractionModel();
            proxy.addListener(listener, true);
        } catch (PolicyFileAccessException pfae) {
            String errorMessage = file.exists() ? LOAD_ERROR_MESSAGE :
                    MISSING_FILE_MESSAGE;
            throw new PartInitException(errorMessage);
        } catch (NullPointerException npe) {
            String errorMessage = file.exists() ? LOAD_ERROR_MESSAGE :
                    MISSING_FILE_MESSAGE;
            throw new PartInitException(errorMessage);
        }
    }

    // Javadoc inherited
    public void doSaveAs() {
        // TODO This should eventually create a suitable progress monitor
        doSaveAs(new NullProgressMonitor());
    }

    public void doSaveAs(IProgressMonitor progressMonitor) {
        Shell shell = getSite().getShell();

        SaveAsDialog dialog = new SaveAsDialog(shell);

        IFile original = getFile(getEditorInput());
        if (original != null) {
            dialog.setOriginalFile(original);
        }

        dialog.create();

        boolean cancelled = false;

        if (dialog.open() == Dialog.CANCEL) {
            if (progressMonitor != null) {
                progressMonitor.setCanceled(true);
            }
            cancelled = true;
        }

        IPath filePath = null;
        if (!cancelled) {
            filePath = dialog.getResult();
            if (filePath == null) {
                if (progressMonitor != null) {
                    progressMonitor.setCanceled(true);
                }
                cancelled = true;
            }
        }

        if (!cancelled) {
            // The resource may have been modified in an editor, the editor
            // left open, and the resource then deleted from the Navigator.
            // Therefore we should only display an error dialog during this
            // SaveAs operation if the resource is still being edited and
            // still exists as a filesystem resource.
            // TODO later check for this situation.
            final IFile file = ResourcesPlugin.getWorkspace().getRoot().
                    getFile(filePath);

            // TODO later Check that the file can be written to

            boolean success = false;
            try {
                saveModel(file, progressMonitor);
                success = true;
                setInput(new FileEditorInput(file));
                setPartName(file.getName());
                context.setResource(file);
            } finally {
                if (progressMonitor != null) {
                    progressMonitor.setCanceled(!success);
                }
            }
        }
    }

    // Javadoc inherited
    public boolean isSaveAsAllowed() {
        return true;
    }

    // Javadoc inherited
    public void doSave(IProgressMonitor iProgressMonitor) {
        IFile file = getFile(getEditorInput());
        if (file == null) {
            doSaveAs(iProgressMonitor);
        } else {
            saveModel(file, iProgressMonitor);
        }
    }

    private void saveModel(IFile targetFile, IProgressMonitor monitor) {
        PolicyBuilder policy =
                (PolicyBuilder) context.getInteractionModel().getModelObject();
        try {
            context.getPolicyFileAccessor().savePolicy(policy, targetFile, monitor);
            context.setDirty(false);

            // Update the read/write state of the model to handle cases where
            // implicit locks have become explicit.
            context.getPolicyFileAccessor().updatePolicyProxyState(
                    context.getInteractionModel(), context.getProject());
        } catch (PolicyFileAccessException pae) {
            context.showErrorDialog(SAVE_ERROR_TITLE, SAVE_ERROR_MESSAGE, pae);
        }
    }

    /**
     * Get the IFile assoiciated with a given IEditorInput.
     *
     * @param input The IEditorInput.
     * @return The IFile associated with the given IEditorInput or null if no
     *         such file exists
     */
    protected IFile getFile(IEditorInput input) {
        IFile file = null;
        if (input instanceof IFileEditorInput) {
            file = ((IFileEditorInput) input).getFile();
        }

        return file;
    }

    // Javadoc inherited
    public boolean isDirty() {
        return context.isDirty();
    }

    /**
     * Returns the editor context associated with this editor part
     * @return the editor context
     */
    public EditorContext getContext() {
        return context;
    }

    /**
     * Configure the minimum size of the given {@link ScrolledComposite} so
     * that its contents will shrink with it until it reaches the minimum size.
     * At this point scroll bars will be added and the contents will not shrink
     * any further.
     *
     * @param scroller  ScrolledComposite whose minimum size to configure
     */
    protected void configureScrolling(ScrolledComposite scroller) {

        // The width below which the main editor scrollable composite will
        // cease to resize its contents and add a scrollbar.
        final int minWidth = EditorMessages.getInteger(RESOURCE_PREFIX +
                "scrollerMinWidth").intValue();

        // Find out how much height is really necessary to display the
        // components correctly, while limiting the width to a sensible amount.
        final Point computedSize = scroller.computeSize(minWidth, SWT.DEFAULT);
        // and only shrink the editor contents until that minimum size is
        // reached, after which a scrollbar should be added.
        scroller.setMinSize(computedSize);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/2	adrianj	VBM:2005111601 Add style rule view

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 15-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 01-Nov-05	9886/6	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/5	adrianj	VBM:2005101811 New theme GUI

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
