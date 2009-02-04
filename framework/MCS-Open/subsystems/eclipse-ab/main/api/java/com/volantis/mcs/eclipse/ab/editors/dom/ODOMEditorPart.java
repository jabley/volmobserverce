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

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.editors.SaveCommand;
import com.volantis.mcs.eclipse.ab.editors.SaveCommandFactory;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.MarkerGeneratingErrorReporter;
import com.volantis.mcs.eclipse.ab.views.devices.DeviceRepositoryBrowserPage;
import com.volantis.mcs.eclipse.builder.wizards.projects.MCSProjectAssignmentWizard;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoInfo;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.controls.ActionableHandler;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.validation.ValidationUtils;
import com.volantis.mcs.eclipse.core.ProjectDeviceRepositoryChangeListener;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.eclipse.core.ProjectDeviceRepositoryProvider;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.JAXPTransformerMetaFactory;
import com.volantis.mcs.xml.xpath.DOMType;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IElementStateListener;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.List;


/**
 * The EditorPart implementing function that is common to all ODOM Editors.
 */
public abstract class ODOMEditorPart extends EditorPart
        implements IGotoMarker {

    /**
     * The prefix for resources associated with MultiPageODOMEditor.
     */
    private static final String RESOURCE_PREFIX = "ODOMEditorPart.";

    /**
     * The ODOMEditorContext for this ODOMEditorPart.
     */
    private ODOMEditorContext context;

    /**
     * The isDirty flag.
     */
    private boolean isDirty = false;


    /**
     * The root element name.
     */
    private final String rootElementName;

    /**
     * Flag allowing sub-classes to disable/enable changes to the
     * dirty status of this ODOMEditorPart.
     */
    private boolean dirtyStatusEnabled = true;

    /**
     * helper that executes UndoRedo unit of work demarcation
     */
    private ODOMUndoRedoGUIDemarcator odomUndoRedoGUIDemarcator;

    /**
     * The actionable handler.
     */
    private ActionableHandler handler;

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
     * The ProjectDeviceRepositoryChangeListener associated with this
     * ODOMEditorPart.
     */
    private ProjectDeviceRepositoryChangeListener
            projDeviceRepositoryChangeListener;

    /**
     * The value of an IStatus when the user has selected cancel during the
     * MCSProjectAssignmentWizard
     */
    public static final int MCS_ASSIGN_WIZARD_CANCELLED_CODE = 1;

    /**
     * Construct a new ODOMEditorPart with a null ODOMEditorContext.
     *
     * @param rootElementName The name of the root element that this overview
     *                        part will be editing. Must not be null.
     * @throws IllegalArgumentException If rootElementName is null.
     */
    public ODOMEditorPart(String rootElementName) {
        this(rootElementName, null);
    }

    /**
     * Construct a new ODOMEditorPart.
     *
     * @param rootElementName The name of the root element that this overview
     *                        part will be editing. Must not be null.
     * @param context         The ODOMEditorContext for this ODOMEditorPart.
     *                        Can be null and if it is the ODOMEditorPart will
     *                        create its context by calling
     *                        ODOMEditorContext.createODOMEditorContext.
     * @throws IllegalArgumentException If rootElementName is null.
     */
    public ODOMEditorPart(String rootElementName, ODOMEditorContext context) {
        if (rootElementName == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "rootElementName");
        }

        this.rootElementName = rootElementName;
        this.context = context;
    }

    // javadoc inherited
    public void doSave(final IProgressMonitor progressMonitor) {
        if (context.resourceExists()) {
            SaveCommand command =
                    context.getSaveCommandFactory().createSaveCommand();
            performSaveOperation(createSaveOperation(command),
                    progressMonitor);
        } else {
            if (isSaveAsAllowed()) {
                doSaveAs(progressMonitor);
            } else {
                Shell shell = getSite().getShell();
                String title = EditorMessages.getString("Editor.error.save." +
                        "deleted.title");
                String msg = EditorMessages.getString("Editor.error.save." +
                        "deleted.message");
                String type = EclipseCommonMessages.
                        getLocalizedPolicyName(context.getPolicyType());
                String name = context.getPolicyResource().getName();
                String args [] = {type, name};
                MessageFormat format =
                        new MessageFormat(msg);
                MessageDialog.openError(shell, title, format.format(args));
            }
        }
    }

    /**
     * Override getAdapter() to provide the device repository page to callers.
     */
    // rest of javadoc inherited
    public Object getAdapter(Class adapterClass) {
        Object adapter = null;
        if (DeviceRepositoryBrowserPage.class.equals(adapterClass)) {
            IProject project = context.getPolicyResource().getProject();
            DeviceRepositoryAccessorManager dram = null;
            try {
                // When the DeviceRepositoryBrowser is opened use a new
                // dram rather than from the ProjectDeviceRepositoryProvider
                // just in case there have already been unsaved changes to
                // the dram in the ProjectDeviceRepositoryProvider that would
                // be picked up here. I.e. the browser should only see changes
                // to the device repository when there is a save or the
                // property is changed. This is done with the
                // ProjectDeviceRepositoryChangeListener below.
                dram = new DeviceRepositoryAccessorManager(MCSProjectNature.
                        getDeviceRepositoryName(project),
                        new JAXPTransformerMetaFactory(),
                        new DefaultJDOMFactory(), false);

                final DeviceRepositoryBrowserPage page =
                        new DeviceRepositoryBrowserPage(dram);
                projDeviceRepositoryChangeListener =
                        new ProjectDeviceRepositoryChangeListener() {
                            public void changed() {
                                try {
                                    DeviceRepositoryAccessorManager dram =
                                            ProjectDeviceRepositoryProvider.
                                            getSingleton().
                                            getDeviceRepositoryAccessorManager(
                                                    getFile().
                                            getProject());
                                    page.updateDeviceRepositoryAccessorManager(
                                            dram);
                                } catch (RepositoryException e) {
                                    EclipseCommonPlugin.
                                            handleError(ABPlugin.getDefault(),
                                                    e);
                                }
                            }
                        };
                ProjectDeviceRepositoryProvider.getSingleton().
                        addProjectDeviceRepositoryChangeListener(
                                projDeviceRepositoryChangeListener,
                                getFile().getProject());

                adapter = page;
            } catch (RepositoryException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            } catch (IOException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            }

        } else {
            adapter = super.getAdapter(adapterClass);
        }

        return adapter;
    }

    // javadoc inherited
    public void doSaveAs() {
        doSaveAs(getProgressMonitor());
    }

    /**
     * doSaveAs using a given IProgressMonitor.
     *
     * @param progressMonitor The IProgressMonitor.
     */
    private void doSaveAs(final IProgressMonitor progressMonitor) {
        Shell shell = getSite().getShell();

        SaveAsDialog dialog = new SaveAsDialog(shell);

        IFile original = getFile();
        dialog.setOriginalFile(original);

        dialog.create();

        boolean cancelled = false;

        if (dialog.open() == Dialog.CANCEL) {
            if (progressMonitor != null) {
                progressMonitor.setCanceled(true);
                cancelled = true;
            }
        }

        IPath filePath = null;
        if (!cancelled) {
            filePath = dialog.getResult();
            if (filePath == null) {
                if (progressMonitor != null) {
                    progressMonitor.setCanceled(true);
                    cancelled = true;
                }
            }
        }

        if (!cancelled) {
            boolean alreadyOpen = isFileOpenInEditor(filePath);

            // The resource may have been modified in an editor, the editor
            // left open, and the resource then deleted from the Navigator.
            // Therefore we should only display an error dialog during this
            // SaveAs operation if the resource is still being edited and
            // still exists as a filesystem resource.
            if (alreadyOpen && context.resourceExists()) {
                String title = EditorMessages.
                        getString("Editor.error.saveas.problems.title");
                String msg = EditorMessages.
                        getString("Editor.error.saveas.problems.message");
                ODOMEditorContext.showErrorDialog(title,
                        new IllegalStateException(msg),
                        msg, true);

            } else {

                final IFile file = ResourcesPlugin.getWorkspace().getRoot().
                        getFile(filePath);

                // Ensure that the specified file can be written to.
                ValidationStatus status = ValidationUtils.checkFile(file.
                        getLocation().toFile().getAbsolutePath(),
                        ValidationUtils.FILE_CAN_WRITE);

                if (!status.isOK()) {
                    String title = EditorMessages.getString("Editor.error." +
                            "saveas.problems.title");
                    ErrorDialog.openError(shell, title, null, status);
                } else {
                    SaveCommandFactory factory =
                            context.getSaveCommandFactory();
                    SaveCommand command = factory.createSaveAsCommand(file);
                    boolean succeeded =
                            performSaveOperation(createSaveOperation(command),
                                    progressMonitor);

                    if (succeeded) {
                        setInput(new FileEditorInput(file));
                        context.setPolicyResource(file);
                        setTitle(file.getName());
                    }

                    if (progressMonitor != null) {
                        progressMonitor.setCanceled(!succeeded);
                    }
                }
            }
        }
    }

    /**
     * Determine whether or not the file is open in the editor.
     *
     * @param filePath the full file path to check for a matching editor name.
     * @return true if the file is open already, false otherwise.
     */
    private boolean isFileOpenInEditor(IPath filePath) {
        boolean alreadyOpen = false;
        IWorkbenchWindow[] workbenchWindows =
                PlatformUI.getWorkbench().getWorkbenchWindows();

        final String filename = filePath.lastSegment();
        // For each of the workbench windows find the open editors and check
        // to see if the editor's full path name matches that of the file path
        // parameter. If it does return true, otherwise return false.
        for (int i = 0; !alreadyOpen && i < workbenchWindows.length; i++) {
            IWorkbenchPage activePage = workbenchWindows[i].getActivePage();
            if (activePage != null) {
                IEditorReference[] editorReferences = activePage.
                        getEditorReferences();

                for (int j = 0; !alreadyOpen && j < editorReferences.length;
                     j++) {
                    IEditorReference editorReference = editorReferences[j];
                    // Check to see if the last segment of the file name matches
                    // the editor's name (which should be the name and extension
                    // for example, 'chart.mcht'). If it does then check to
                    // see if the full path matches that of the file path. We
                    // do this to reduce calls to x.getEditor(true) which restores
                    // the editor. Looking at how other eclipse editors work,
                    // see AbstractDocumentProvider#saveDocument(..), an internal
                    // map is maintained using within a document provider (which
                    // we don't have). Adopting a similar approach is better
                    // but not feasible within the current time frame.
                    // @todo later evaluate/resolve this comment's issue.
                    if (filename.equals(editorReference.getName())) {
                        IFileEditorInput fileEditorInput = ((IFileEditorInput)
                                editorReference.getEditor(true).getEditorInput());
                        // If the full path matches, then this editor is exists
                        // and is open, so we cannot save over it.
                        if (fileEditorInput.getFile().getFullPath().
                                equals(filePath)) {
                            alreadyOpen = true;
                        }
                    }
                }
            }
        }
        return alreadyOpen;
    }

    /**
     * Returns true if and only if this editor part is the workbenches
     * active editor part.
     *
     * @return true if and only if this IEditorPart is the active page.
     */
    protected boolean isActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().getActiveEditor() == this;
    }

    // javadoc inherited
    public void gotoMarker(IMarker marker) {

        // we need to get hold of the error reporter. This is so that
        // we can get the XPath object that maps to the xpath string
        // that the marker has just provided. Unfortunately the error
        // reporter cannot set the XPath object as an attribute of the
        // marker as the IMarker#setAttribute(String, Object) method only
        // allows Integer, Boolean and String instances as its Object
        // argument. We work around this by getting the
        // MarkerGeneratingErrorReporter class to manage a map of
        // XPath string to XPath object mappings.
        MarkerGeneratingErrorReporter errorReporter =
                context.getErrorReporter();

        if (errorReporter == null) {
            // this should never happen
            throw new IllegalStateException("DOMValidator is null");
        }

        // get the XPath for the marker
        XPath xPath = errorReporter.getXPath(marker);

        // We don't need to do anything with a null XPath since means
        // we cannot set the focus. For example, the policy name (file name
        // is invalid => there is no editor for file names and no way
        // to set the focus).
        if (xPath != null) {
            setFocus(xPath);
        }

    }


    /**
     * Sets the focus to XPathFocusableControls using the provided xPath. This
     * is done by telling the ODOMSelectionManager to select the component
     * corresponding to the xPath. Doing this allows the Element Attributes
     * view to be configured correctly such that the required attribute can have
     * its focus set. If this is not done the Element Attributes view will
     * contain the attributes of whatever is currently selected in the design
     * window rather then the attributes you wish to edit. After selecting the
     * correct component it is then possible to set focus on the correct
     * attribute.
     *
     * @param xPath the xPath of the attribute whose focus you wish to set.
     * Must not be null.
     */
    protected void setFocus(XPath xPath) {

        try {
            XPath elementPath = xPath;
            // get a context and trim the attribute from the XPath
            Element root = getODOMEditorContext().getRootElement();
            if (elementPath.getDOMType() != DOMType.ELEMENT_TYPE) {
                elementPath = elementPath.getParent();
            }

            // select the correct component
            context.getODOMSelectionManager()
                    .setSelection(elementPath.selectNodes(root));
            XPathFocusable[] focusableControls = getXPathFocusableControls();
            for (int i = 0; (focusableControls != null) &&
                    (i < focusableControls.length); i++) {
                focusableControls[i].setFocus(xPath);
            }
        } catch (XPathException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

    }


    // javadoc inherited
    public void init(IEditorSite editorSite, IEditorInput editorInput)
            throws PartInitException {
        // For some reason this is called twice
        IFile file = null;
        if (context == null) {
            file = getFile(editorInput);
            context = ODOMEditorContext.
                    createODOMEditorContext(rootElementName,
                            file,
                            new UndoRedoMementoOriginator() {
                                // nothing to do for a single page editor
                                public UndoRedoMemento takeSnapshot() {
                                    return UndoRedoMemento.NULLOBJ;
                                }

                                // implemented as outer class method to
                                // allow reuse from the multipage editor
                                public void restoreSnapshot(UndoRedoInfo
                                        undoRedoInfo) {
                                    ODOMEditorPart.this.
                                            restoreSnapshot(undoRedoInfo);
                                }
                            });
        }

        // If context was not created then there was a problem so we
        // cannot initialize this editor.
        if (context != null) {
            file = getFile(editorInput);

            // Create and install the file synchronizer.
            synchronizer = new FileSynchronizer((IFile) context.
                    getPolicyResource());
            synchronizer.install();

            // Create and add the element state listener to the synchronizer.
            listener = new IElementStateListener() {
                public void elementDirtyStateChanged(Object element,
                                                     boolean isDirty) {
                }

                public void elementContentAboutToBeReplaced(Object element) {
                }

                public void elementContentReplaced(Object element) {
                }

                public void elementDeleted(Object element) {
                    if (!ODOMEditorPart.this.isDirty()) {
                        close(true);
                    }
                }

                public void elementMoved(Object originalElement,
                                         Object movedElement) {
                }
            };
            synchronizer.addElementStateListener(listener);

            setTitle(file.getName());
            setSite(editorSite);
            setInput(editorInput);
        } else {
            String message = EditorMessages.getString(RESOURCE_PREFIX +
                    "contextCreationFailure.message"); //$NON-NLS-1$
            MessageFormat messageFormat =
                    new MessageFormat(message);
            String args [] = {file.getName()};

            throw new PartInitException(messageFormat.format(args));
        }

        boolean validDeviceRepository = false;
        // if this is an mcs project then check if the device repository is
        // valid
        IProject project = context.getPolicyResource().getProject();
        try {
            if (project.hasNature(MCSProjectNature.NATURE_ID)) {
                MCSProjectNature projectNature;
                projectNature = MCSProjectNature.getMCSProjectNature(project);
                validDeviceRepository = projectNature.
                        hasValidDeviceRepository();
            }
        } catch (CoreException coreException) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), coreException);
        }
        if (validDeviceRepository == false) {
            // project does not have an MCS nature or the device repository has
            // become corrupt so launch wizard to add MCS nature to the project
            MCSProjectAssignmentWizard wizard =
                    new MCSProjectAssignmentWizard(project);
            // Instantiates the wizard container with the wizard and opens it
            Shell shell = getSite().getShell();
            WizardDialog dialog = new WizardDialog(shell, wizard);
            dialog.create();
            int action = dialog.open();
            if (action == WizardDialog.CANCEL) {
                String pluginId = ABPlugin.getDefault().
                        getDescriptor().getUniqueIdentifier();
                Status status = new Status(Status.OK, pluginId,
                        ODOMEditorPart.MCS_ASSIGN_WIZARD_CANCELLED_CODE,
                        EditorMessages.getString(RESOURCE_PREFIX +
                        "projectAssignmentWizardCancelled.reason"),
                        null);
                PartInitException e = new PartInitException(status);
                throw e;
            }
        }
    }


    /**
     * Close the editor asynchronously.
     *
     * @param save the save parameter.
     */
    protected void close(final boolean save) {
        Display display = getSite().getShell().getDisplay();
        display.asyncExec(new Runnable() {
            public void run() {
                getSite().getPage().closeEditor(ODOMEditorPart.this, save);
            }
        });
    }


    /**
     * Get the ODOMEditorContext for this ODOMEditorPart.
     *
     * @return The ODOMEditorContext.
     */
    protected ODOMEditorContext getODOMEditorContext() {
        return context;
    }

    /**
     * Set the ODOMEditorContext for this ODOMEditorPart.
     *
     * @param context The ODOMEditorContext.
     */
    protected void setODOMEditorContext(ODOMEditorContext context) {
        this.context = context;
    }

    /**
     * Set focus to the first focusable control in the EditorPart.
     */
    public abstract void setFocus();

    // javadoc inherited
    public boolean isDirty() {
        return isDirty;
    }

    // javadoc inherited
    public boolean isSaveAsAllowed() {
        return true;
    }

    // javadoc inherited
    public void dispose() {
        if (synchronizer != null) {
            synchronizer.removeElementStateListener(listener);
            synchronizer.uninstall();
        }

        if (projDeviceRepositoryChangeListener != null) {
            ProjectDeviceRepositoryProvider.getSingleton().
                    removeProjectDeviceRepositoryChangeListener(
                            projDeviceRepositoryChangeListener,
                            getFile().getProject());
        }

        try {
            context.dispose();
            if (handler != null) {
                handler.dispose();
                handler = null;
            }
        } finally {
            super.dispose();
        }
    }

    /**
     * Create the part control.
     *
     * @see #createPartControl(Composite)
     */
    protected abstract void createPartControlImpl(Composite parent);

    /**
     * Create the part control by delegating to createPartControlImpl().
     * <p/>
     * A special focus listener is attached to the widget tree to
     * demarcate undo/redo UOWs. It is not unregistered when
     * because at that time all widgets will have already been disposed of.
     * </p>
     */
    // rest of javadoc inherited
    public void createPartControl(Composite parent) {
        initializeActions();

        createPartControlImpl(parent);

        // Listen for changes to the root element so we can
        // set the dirty state if necessary.
        context.addChangeListener(new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                setDirty(true);
            }
        });

        odomUndoRedoGUIDemarcator = new ODOMUndoRedoGUIDemarcator(context.
                getUndoRedoManager());
        odomUndoRedoGUIDemarcator.addFocusDrivenUndoRedoDemarcatorFor(parent);
    }

    /**
     * Creates the Undo/redo actions for the document being edited.
     * <p/>
     * The actions are registered as global handlers in this editor's action
     * bars, which are stored in the editor context and made available
     * for other parts to use. So this step needs to be done only once
     * in the lifecycle of an editor part
     * </p>
     */
    private void initializeActions() {
        // guard to make sure that in a multi-page editor
        // this step happens just once
        if (context.getActionBars() == null) {

            IActionBars editorSiteActionBars = this.getEditorSite().
                    getActionBars();

            if (editorSiteActionBars != null) {
                UndoRedoManager undoRedoManager = context.getUndoRedoManager();
                ODOMEditorContribution.assignUndoRedoAction(editorSiteActionBars,
                        undoRedoManager, IWorkbenchActionConstants.UNDO);

                ODOMEditorContribution.assignUndoRedoAction(editorSiteActionBars,
                        undoRedoManager, IWorkbenchActionConstants.REDO);

                context.setActionBars(editorSiteActionBars);

                context.setHandler(new ActionableHandler(editorSiteActionBars));
            }
        }
    }

    /**
     * Set the isDirty flag and fire a dirty change event if the status of
     * isDirty has changed. If dirtyStatusEnabled is set to false then this
     * method has no effect.
     *
     * @param isDirty The value to set isDirty to.
     */
    protected void setDirty(boolean isDirty) {
        if (dirtyStatusEnabled && this.isDirty != isDirty) {
            this.isDirty = isDirty;
            firePropertyChange(PROP_DIRTY);
        }
    }

    /**
     * Set the dirtyStatusEnabled flag. If this flag is false then the dirty
     * status of this editor part will not change. If this flag is true then
     * the dirty status of this editior part will change if there is a change
     * to the root element (i.e. any element) this ODOMEditorPart is
     * associated with.
     *
     * @param enable Flag to enable/disable the dirtyStatusEnabled flag.
     */
    protected void setDirtyStatusEnabled(boolean enable) {
        dirtyStatusEnabled = enable;
    }

    /**
     * Run a save operation.
     *
     * @param saveOperation The WorkspaceModifyOperation that will do the
     *                      save.
     * @return true If the save succeeded; false otherwise.
     */
    private boolean
            performSaveOperation(WorkspaceModifyOperation saveOperation,
                                 IProgressMonitor progressMonitor) {

        boolean succeeded = false;
        try {
            saveOperation.run(progressMonitor);
            succeeded = true;
            setDirty(false);
        } catch (InterruptedException e) {
            // Cancelled
        } catch (InvocationTargetException e) {
            // todo maybe be more specific about error handling if too general
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        return succeeded;
    }

    /**
     * Create the WorkspaceModifyOperation that will save the resource
     * (policy) being editor.
     * <p/>
     * Editors that do not save policies directly to files on the file
     * system should override this methods with their own implementations.
     *
     * @return The WorkspaceModifyOperation that will save the policy
     *         being edited.
     */
    protected WorkspaceModifyOperation
            createSaveOperation(final SaveCommand saveCommand) {
        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            protected void execute(IProgressMonitor monitor)
                    throws CoreException,
                    InterruptedException {
                try {
                    monitor.beginTask("Editor.save.progress", 2000);
                    saveCommand.save(new SubProgressMonitor(monitor, 1000));
                } finally {
                    monitor.done();
                }
            }
        };

        return op;
    }

    /**
     * Get the XPathFocusable controls in this EditorPart.
     *
     * @return The XPathFocusable controls.
     */
    protected abstract XPathFocusable[] getXPathFocusableControls();

    /**
     * Get the IFile associated with the policy being edited by this
     * editor.
     *
     * @return The IFile associated with the policy being edited.
     */
    private IFile getFile() {
        return getFile(getEditorInput());
    }

    /**
     * Get the IFile assoiciated with a given IEditorInput.
     *
     * @param input The IEditorInput.
     * @return The IFile associated with the given IEditorInput.
     * @throws IllegalArgumentException If an IFile cannot be obtained
     *                                  from the given IEditorInput.
     */
    protected IFile getFile(IEditorInput input) {
        IFile file = null;
        if (input instanceof IFileEditorInput) {
            file = ((IFileEditorInput) input).getFile();
        } else {
            throw new IllegalStateException("Unsupported IEditorInput: " +
                    getEditorInput());
        }

        return file;
    }

    /**
     * Provide a progress monitor that operates on the status line.
     *
     * @return A status line based IProgressMonitor or null if no
     *         StatusLineManager was available.
     */
    private IProgressMonitor getProgressMonitor() {

        IProgressMonitor pm = null;

        IStatusLineManager manager = getStatusLineManager();

        if (manager != null) {
            pm = manager.getProgressMonitor();
        }

        return pm != null ? pm : new NullProgressMonitor();
    }


    /**
     * Get the IStatusLineManager. This method assumes that the
     * IEditorActionBarContributor is an instance of
     * EditorActionBarContributor. This method also assumes that the
     * EditorActionBarContributor will return non-null from getActionBars().
     *
     * @return The IStatusLineManager
     */
    private IStatusLineManager getStatusLineManager() {
        return context.getActionBars().getStatusLineManager();
    }

    /**
     * Non-public implementation of {@link UndoRedoMementoOriginator#restoreSnapshot}
     * as an <code>ODOMEditorPart</code> does not implement the interface
     * explicitly
     * <p/>
     * <p/>
     * This implementation sets the focus to the widget displaying
     * the odom node that was changed last as a result of applying an
     * undo/redo request.
     * It also sets the ODOM selection to the elements that have changed.
     * </p>
     * <p/>
     * <strong>NOTE</strong> : setFocusWith(XPath) must occur before
     * setting the ODOM selections, because setFocus ALSO sets the selection,
     * so if the order of calls is reversed the selection we set here
     * would be destroyed.
     * </p>
     *
     * @param undoRedoInfo provided by the UndoRedoManager
     */
    void restoreSnapshot(UndoRedoInfo undoRedoInfo) {

        List changedNodesXPaths = undoRedoInfo.getChangedNodesXPaths();
        if (!changedNodesXPaths.isEmpty()) {
            setFocus((XPath) changedNodesXPaths.get(0));
        }

        try {
            List elements = undoRedoInfo.getChangedElements(
                    getRootElementDocument());

            if (!elements.isEmpty()) {
                context.getODOMSelectionManager().setSelection(elements);
            }

        } catch (XPathException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
    }

    /**
     * Provide a mechanism for subclasses to get an alternative {@link Document}
     *
     * @return a {@link Document} instance.
     */
    protected Document getRootElementDocument() {
        return context.getRootElement().getDocument();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 02-Nov-05	10064/1	adrianj	VBM:2005110106 Allow GUI to run in Eclipse 3.1

 16-May-05	8260/1	allan	VBM:2005042107 Implement and fix setFocus(XPath) for theme design

 16-May-05	8201/3	allan	VBM:2005042107 Implement and fix setFocus(XPath) for theme design

 05-May-05	8034/2	pcameron	VBM:2005050322 Fixed SaveAs of MCS resources

 05-May-05	8031/1	pcameron	VBM:2005050322 Fixed SaveAs of MCS resources

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 13-May-04	4301/2	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 29-Sep-04	5678/1	adrianj	VBM:2004091007 Fix for gotoMarker setting focus on GUI component

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 11-Jun-04	4691/1	allan	VBM:2004060202 Allow setFocus(XPath) to work with Elements

 18-May-04	4231/5	tom	VBM:2004042704 rework for 2004042704

 10-May-04	4239/2	allan	VBM:2004042207 SaveAs on DeviceEditor.

 05-May-04	4115/5	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 27-Apr-04	3983/8	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 27-Apr-04	3983/6	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 27-Apr-04	3983/4	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 22-Apr-04	3878/6	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 15-Apr-04	3881/1	steve	VBM:2004032606 Allow assignment of MCS nature to imported projects

 08-Apr-04	3813/1	byron	VBM:2004040604 Deleting Components from mcs-policies does not follow the eclipse standard

 08-Apr-04	3796/1	byron	VBM:2004040604 Deleting Components from mcs-policies does not follow the eclipse standard

 25-Mar-04	3519/5	byron	VBM:2004020403 Save as for policy results in Error - ensure save as checks full path name

 24-Mar-04	3519/3	byron	VBM:2004020403 Save as for policy results in Error

 23-Mar-04	3389/6	byron	VBM:2004030905 NLV properties files need adding to build - rework issues

 23-Mar-04	3389/4	byron	VBM:2004030905 NLV properties files need adding to build - missed some comments and exception handling

 23-Mar-04	3389/2	byron	VBM:2004030905 NLV properties files need adding to build

 17-Mar-04	3346/1	byron	VBM:2004021308 Cannot undo deletion or cutting of single or multiple assets

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 17-Feb-04	2988/5	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 12-Feb-04	2924/3	eduardo	VBM:2004021003 editor actions demarcate UndoRedo UOWs

 11-Feb-04	2862/1	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 09-Feb-04	2800/14	eduardo	VBM:2004012802 codestyle fixes

 09-Feb-04	2800/12	eduardo	VBM:2004012802 undo redo works from outline view

 05-Feb-04	2800/7	eduardo	VBM:2004012802 undo redo hooked in eclipse with demarcation. Designed just for single page editors

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 19-Jan-04	2562/3	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 08-Jan-04	2431/2	allan	VBM:2004010404 Fix validation and display update issues.

 07-Jan-04	2447/1	philws	VBM:2004010609 Initial code for revised validation mechanism

 06-Jan-04	2412/4	allan	VBM:2004010407 Rework issues.

 06-Jan-04	2412/2	allan	VBM:2004010407 Fixed dirty status handling when switching editor page.

 06-Jan-04	2391/2	byron	VBM:2003121726 Assets can be pasted into components where they are not valid

 05-Jan-04	2380/1	allan	VBM:2004010406 Improve handling of non-well-formed XML in policy files.

 04-Jan-04	2309/2	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 19-Dec-03	2237/1	byron	VBM:2003112804 Provide an MCS project builder

 17-Dec-03	2219/4	doug	VBM:2003121502 Added dom validation to the eclipse editors

 16-Dec-03	2213/5	allan	VBM:2003121401 More editors and fixes for presentable values.

 15-Dec-03	2213/3	allan	VBM:2003121401 Refactored to reduce the number of resource properties.

 15-Dec-03	2213/1	allan	VBM:2003121401 Commit to make available to Doug

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 02-Dec-03	2069/6	allan	VBM:2003111903 Pre-approve bug fixes.

 02-Dec-03	2069/4	allan	VBM:2003111903 Basic ODOMEditorPart completed with skeleton ImageComponentEditor.

 30-Nov-03	2069/1	allan	VBM:2003111903 Implement init, doSave and supporting methods.

 ===========================================================================
*/
