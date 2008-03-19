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

import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.common.ResourceDiagnosticsAdapter;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessor;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessorFactory;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.policies.PolicyModel;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A context containing contextual information shared between parts of an
 * editor.
 */
public abstract class EditorContext {
    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(EditorContext.class);

    private static final PolicyFileAccessorFactory POLICY_FILE_ACCESSOR_FACTORY =
            PolicyFileAccessorFactory.getDefaultInstance();
    /**
     * The interaction model around the model object being edited.
     */
    private Proxy interactionModel;

    /**
     * A list of dirty state listeners.
     */
    private List dirtyStateListeners = new ArrayList();

    /**
     * Flag to indicate whether the model is dirty (ie. edited and unsaved).
     */
    private boolean dirty = false;

    /**
     * The project which the resource being edited forms part of.
     */
    private IProject project;

    /**
     * The resource being edited.
     */
    private IResource resource;

    // Javadoc not required
    public Proxy getInteractionModel() {
        return interactionModel;
    }

    /**
     * Sets the interaction model. Should only be called once, when the initial
     * value for the interaction model is set - future changes should be to the
     * underlying model object, not the interaction layer.
     *
     * @param newModel The interaction model
     * @throws IllegalStateException if a second attempt to set the model is
     *                               made
     */
    protected void setInteractionModel(Proxy newModel) {
        if (interactionModel != null) {
            throw new IllegalStateException("Interaction model already set");
        }
        interactionModel = newModel;
    }

    // Javadoc not required
    public void setDirty(boolean newDirty) {
        boolean oldDirty = dirty;
        dirty = newDirty;
        if (oldDirty != newDirty) {
            Iterator it = dirtyStateListeners.iterator();
            while (it.hasNext()) {
                DirtyStateListener dsl = (DirtyStateListener) it.next();
                dsl.dirtyStateChanged(newDirty);
            }
        }
    }

    /**
     * Add a listener for changes in dirty state.
     *
     * @param dsl The listener to add
     */
    public void addDirtyStateListener(DirtyStateListener dsl) {
        dirtyStateListeners.add(dsl);
    }

    /**
     * Remove a listener for changes in dirty state.
     *
     * @param dsl The listener to remove
     */
    public void removeDirtyStateListener(DirtyStateListener dsl) {
        dirtyStateListeners.remove(dsl);
    }

    // Javadoc not required
    public boolean isDirty() {
        return dirty;
    }

    public PolicyFileAccessor getPolicyFileAccessor() {
        return POLICY_FILE_ACCESSOR_FACTORY.getPolicyFileAccessor(project);
    }

    /**
     * Executes an operation.
     *
     * @param op The operation to execute
     * @todo later This should add the operation to the undo/redo stack.
     */
    public void executeOperation(Operation op) {
        op.execute();
    }

    /**
     * Loads a specified file resource into the context, if the context has not
     * already been initialised.
     *
     * @param file The file to load
     * @throws PolicyFileAccessException if an error occurs while loading
     */
    public abstract void loadResource(IFile file) throws PolicyFileAccessException;

    // Javadoc not required
    public void setResource(IResource newResource) {
        resource = newResource;
    }

    // Javadoc not required
    public IResource getResource() {
        return resource;
    }

    /**
     * Retrieves the type of model object edited with this context. This
     * must be a class which corresponds to a repository object with a
     * suitable JiBX mapping.
     *
     * @return a class representing the model type edited by this part
     */
    protected abstract Class getModelType();

    /**
     * Retrieves the interaction layer model descriptor for this part.
     *
     * @return the model descriptor for this part
     */
    protected ModelDescriptor getModelDescriptor() {
        return PolicyModel.MODEL_DESCRIPTOR;
    }


    // Javadoc not required
    public IProject getProject() {
        return project;
    }

    protected void setProject(IProject project) {
        this.project = project;
    }

    /**
     * Report all diagnostic errors against the current resource.
     */
    protected void reportErrors() {
        if (resource != null) {
            try {
                if (resource.exists()) {
                    new ResourceDiagnosticsAdapter(resource).
                            setDiagnostics(interactionModel.getDiagnostics());
                }
            } catch (CoreException ce) {
                EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), ce);
            }
        }
    }

    /**
     * Displays an error dialog with the specified title and message.
     *
     * @param title The title of the error dialog
     * @param message The message of the error dialog
     * @param throwable
     */
    public void showErrorDialog(
            String title, String message, Throwable throwable) {
        StringBuffer messageBuffer = new StringBuffer();
        if (message != null) {
            messageBuffer.append(message);
        }

        String pluginId = BuilderPlugin.getDefault().
                getDescriptor().getUniqueIdentifier();
        Status status = new Status(Status.ERROR, pluginId,
                Status.ERROR, messageBuffer.toString(), throwable);

        Shell shell = Display.getCurrent().getActiveShell();
        ErrorDialog.openError(shell, title, null, status);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10756/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 17-Nov-05	10341/4	pduffin	VBM:2005111410 Fixed issue with mapping classes to type descriptors

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 16-Nov-05	10315/5	pduffin	VBM:2005111410 Added support for copying model objects

 15-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects
 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 07-Nov-05	10175/1	adrianj	VBM:2005110437 Validation in incremental builder

 07-Nov-05	10150/1	adrianj	VBM:2005110437 Validation in incremental builder

 01-Nov-05	10062/1	adrianj	VBM:2005101811 New theme GUI

 01-Nov-05	9886/9	adrianj	VBM:2005101811 New theme GUI

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 31-Oct-05	9886/5	adrianj	VBM:2005101811 New theme GUI

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
