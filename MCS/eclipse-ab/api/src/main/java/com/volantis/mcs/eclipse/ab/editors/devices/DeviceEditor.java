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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.devrep.repository.accessors.MDPRArchiveAccessor;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElementFactory;
import com.volantis.mcs.eclipse.ab.editors.dom.MultiPageODOMEditor;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeSupport;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelection;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.controls.ControlUtils;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.eclipse.core.ProjectDeviceRepositoryProvider;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.JAXPTransformerMetaFactory;
import com.volantis.synergetics.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IGotoMarker;
import org.jdom.Element;

/**
 * Multipage editor that allows devices to be edited
 */
public class DeviceEditor extends MultiPageODOMEditor
        implements IGotoMarker, ISelectionProvider {

    /**
     * The prefix for resources used by this class.
     */
    private static final String RESOURCE_PREFIX =
            "DeviceEditor."; //$NON-NLS-1$

    /**
     * The message asking for permission to automatically modify the
     * device repository.
     */
    private static final String AUTO_MOD_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX +
                    "autoModification.message");

    /**
     * The message asking for permission to automatically modify the
     * device repository.
     */
    private static final String AUTO_MOD_TITLE =
            EditorMessages.getString(RESOURCE_PREFIX +
                    "autoModification.title");

    /**
     * The hierarchy is the default root element for the device editor.
     */
    private static String ROOT_ELEMENT_NAME =
            DeviceRepositorySchemaConstants.HIERARCHY_ELEMENT_NAME;

    /**
     * The intro message for the editor's Information page.
     */
    private static final String INTRO_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "intro.text");

    /**
     * The revision message for the editor's Information page.
     */
    private static final String REVISION_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "revision.text");

    /**
     * The version message for the editor's Information page.
     */
    private static final String VERSION_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "version.text");

    /**
     * Initializes a <code>DeviceEditor</code> instance
     */
    public DeviceEditor() {
        super(ROOT_ELEMENT_NAME);
    }

    /**
     * Override getAdapter() to delegate to getAdapter() of the hierarchy page.
     */
    // rest of javadoc inherited
    public Object getAdapter(Class adapterClass) {
        // get the Overview (hierarchy) editor and return it's adapter.
        // Note, we use an index of 0 as the createPages method adds the
        // overview page first.
        return getEditor(0).getAdapter(adapterClass);
    }

    // javadoc inherited
    protected void createPages() {
        try {
            // The hierarchy page
            DeviceEditorContext editorContext =
                    (DeviceEditorContext) getODOMEditorContext();

            int index = addPage(new DeviceOverviewPart(editorContext),
                    getEditorInput());
            setPageText(index,
                    EditorMessages.getString(RESOURCE_PREFIX +
                            "overview.label"));

            // The device policies page.
            index = addPage(new DevicePoliciesPart(editorContext),
                    getEditorInput());
            setPageText(index,
                    EditorMessages.getString(RESOURCE_PREFIX +
                            "policies.label"));

            // The structure page
            index = addPage(
                    new DeviceStructurePart(editorContext),
                    getEditorInput());
            setPageText(index,
                    EditorMessages.getString(RESOURCE_PREFIX +
                            "structure.label"));

            // The information page. This page is a Control rather than a part.
            index = addPage(createDeviceInformationPage(
                    editorContext.getDeviceRepositoryAccessorManager()));
            setPageText(index,
                    EditorMessages.getString(RESOURCE_PREFIX +
                            "information.label"));

        } catch (PartInitException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
    }

    /**
     * Creates and returns the Composite for the device Information page.
     *
     * @param deviceRAM the manager to use for retrieving information
     * @return the Composite
     */
    private Composite createDeviceInformationPage(
            DeviceRepositoryAccessorManager deviceRAM) {

        final Object[] formatArgs = new Object[1];

        // Create the version message.
        MessageFormat versionFormat = new MessageFormat(VERSION_MESSAGE);
        formatArgs[0] = deviceRAM.getVersion();
        String versionMessage = versionFormat.format(formatArgs);

        // Create the revision message.
        MessageFormat revisionFormat = new MessageFormat(REVISION_MESSAGE);
        formatArgs[0] = deviceRAM.getRevision();
        String revisionMessage = revisionFormat.format(formatArgs);

        // Create and return the controls displaying the version and
        // revision information.
        return ControlUtils.createMessageComposite(this.getContainer(),
                SWT.LEFT,
                new String[]{INTRO_MESSAGE, versionMessage, revisionMessage});
    }

    // javadoc inherited
    protected ODOMEditorContext createODOMEditorContext(String rootElementName,
                                                        final IFile file) {

        ODOMEditorContext context = null;
        try {
            final JAXPTransformerMetaFactory transformerMetaFactory =
                    new JAXPTransformerMetaFactory();
            MDPRArchiveAccessor archiveAccessor =
                    new MDPRArchiveAccessor(file.getLocation().toOSString(),
                            transformerMetaFactory);

            boolean ok = true;
            if (archiveAccessor.willBeModifiedOnLoad()) {
                // Inform the user that the device repository will need
                // to be modified and have them give permission.
                ok = confirmAndModify(archiveAccessor);
            }
            if (ok) {

                ODOMChangeSupport.ChangeSupportDisabledCommand command =
                        new ODOMChangeSupport.ChangeSupportDisabledCommand() {

                            public Object execute() {

                                ODOMEditorContext context = null;
                                try {
                                    boolean isAdminProject = file.getProject().
                                            hasNature(
                                                    DeviceEditorContext.MCS_ADMIN_NATURE_ID);

                                    DeviceRepositoryAccessorManager dram =
                                            new DeviceRepositoryAccessorManager(
                                                    file.getLocation().toOSString(),
                                                    transformerMetaFactory,
                                                    new DeviceODOMElementFactory(),
                                                    isAdminProject);

                                    // If the device repository to be edited is the project
                                    // device repository then give the dram to the
                                    // ProjectDeviceRepositoryProvider for this project so that it
                                    // can see updates immeadiately even before the first save.
                                    if (dram.getDeviceRepositoryName()
                                            .equals(MCSProjectNature.
                                                    getDeviceRepositoryName(
                                                    file.getProject()))) {
                                        ProjectDeviceRepositoryProvider
                                                .getSingleton().
                                                setDeviceRepositoryAccessorManager(
                                                        file.getProject(),
                                                        dram);
                                    }
                                    context = DeviceEditorContext
                                            .createDeviceEditorContext(
                                                    file,
                                                    createUndoRedoMementoOriginator(),
                                                    dram);
                                } catch (RepositoryException e) {
                                    EclipseCommonPlugin.handleError(
                                            ABPlugin.getDefault(), e);
                                } catch (IOException e) {
                                    EclipseCommonPlugin.handleError(
                                            ABPlugin.getDefault(), e);
                                } catch (CoreException e) {
                                    EclipseCommonPlugin.handleError(
                                            ABPlugin.getDefault(), e);
                                }

                                return context;
                            }
                        };
                context = (ODOMEditorContext) ODOMChangeSupport
                        .executeWithoutChangeSupport(command);
            }
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        return context;
    }

    /**
     * Display a question dialog that asks the user to give permission for
     * automatic modification of the device repository that will be
     * performed by the MDPRArchiveAccessor. If the user gives permission
     * then take a backup of the device repository, modify the current
     * repository and save it.
     *
     * @return true if the user allows the modification and the modification
     *         was successful; false otherwise
     */
    private boolean confirmAndModify(MDPRArchiveAccessor archiveAccessor) {
        // Create the name of the backup file.
        String candidateBackupName = archiveAccessor.getArchiveFileName() +
                ".old";
        String backupName = createUniqueFileName(candidateBackupName);

        MessageFormat format = new MessageFormat(AUTO_MOD_MESSAGE);
        String message = format.format(new String[]{backupName});
        boolean ok =
                MessageDialog.openQuestion(new Shell(Display.getDefault()),
                        AUTO_MOD_TITLE, message);

        if (ok) {
            File orig = new File(archiveAccessor.getArchiveFileName());
            try {
                IOUtils.copyFiles(orig, new File(backupName));
                DeviceRepositoryAccessorManager dram =
                        new DeviceRepositoryAccessorManager(archiveAccessor,
                                new ODOMFactory());
                dram.writeRepository();
            } catch (IOException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            } catch (RepositoryException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            }
        }

        return ok;
    }

    /**
     * Ensure the given file name is unique and append an index
     * increment until it is.
     *
     * @param fileName the file name upon which to base the unique name.
     * @return a unique file name that will be fileName if it is already
     *         unique otherwise will be fileName with a suffix that makes it unique.
     */
    private String createUniqueFileName(String fileName) {
        File file = new File(fileName);
        String origName = file.getAbsolutePath();
        int i = 1;
        while (file.exists()) {
            StringBuffer nextName =
                    new StringBuffer(origName.length() + 2);
            nextName.append(origName).append(".").append(i);
            file = new File(nextName.toString());
            i++;
        }

        return file.getAbsolutePath();
    }

    /**
     * Overridden to call the superclass method if there is an active editor.
     * Otherwise, call the method on the editor of the first page.
     */
    // rest of javadoc inherited
    public void gotoMarker(IMarker marker) {
        /*
        if (getActiveEditor() != null) {
            super.gotoMarker(marker);
        } else {
            getEditor(0).gotoMarker(marker);
        }
        */
        // todo implement this to do the above when XPathFocusable works
    }

    /**
     * Overridden to call the superclass method if there is an active editor.
     * Otherwise, call the method on the editor of the first page.
     */
    // rest of javadoc inherited
    public boolean isSaveAsAllowed() {
        boolean allowed = false;
        if (getActiveEditor() != null) {
            allowed = super.isSaveAsAllowed();
        } else {
            allowed = getEditor(0).isSaveAsAllowed();
        }
        return allowed;
    }

    // javadoc inherited
    public void addSelectionChangedListener(
            ISelectionChangedListener listener) {
        getODOMEditorContext().getODOMSelectionManager().
                addSelectionChangedListener(listener);
    }

    /**
     * Gets the current selection by delegating to the ODOMSelectionManager
     * associated with this DeviceEditor's ODOMEditorContext.
     *
     * @return an ODOMElementSelection representing the current selection
     */
    public ISelection getSelection() {
        return getODOMEditorContext().getODOMSelectionManager().getSelection();
    }

    // javadoc inherited
    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        getODOMEditorContext().getODOMSelectionManager().
                removeSelectionChangedListener(listener);
    }

    /**
     * Replaces the current selection with the given selection by delegating to
     * the ODOMSelectionManager associated with this DeviceEditor's
     * ODOMEditorContext.
     *
     * @param selection the new selection. Must be an ODOMElementSelection.
     */
    public void setSelection(ISelection selection) {
        getODOMEditorContext().getODOMSelectionManager()
                .setSelection(selection);
    }

    /**
     * Set the selection to the named device assuming the named device
     * is in the device repository.
     * <p/>
     * This is a convenience method and allows callers to circumvent using
     * DeviceRespositoryAccessor manager to get the hierarchy element
     * for the named device.
     *
     * @param deviceName the name of the device
     */
    public void selectDevice(String deviceName) {
        DeviceEditorContext context =
                (DeviceEditorContext) getODOMEditorContext();
        DeviceRepositoryAccessorManager dram =
                context.getDeviceRepositoryAccessorManager();
        Element device = dram.getHierarchyDeviceElement(deviceName);
        List selection = new ArrayList();
        selection.add(device);
        setSelection(new ODOMElementSelection(selection));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 13-Jul-05	9033/1	allan	VBM:2005071312 Move IOUtils.java that is in cornerstone into Synergetics

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6251/1	allan	VBM:2004111602 Implement IGotoMarker in DeviceEditor

 17-Nov-04	6012/3	allan	VBM:2004051307 Remove standard elements in admin mode.

 16-Nov-04	4394/8	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/4	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4394/1	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 16-Aug-04	5206/2	allan	VBM:2004081201 Auto-migration of mdpr with dialog.

 02-Aug-04	5030/7	pcameron	VBM:2004070705 Using ODOMElementSelection

 02-Aug-04	5030/5	pcameron	VBM:2004070705 Implemented ISelectionProvider on ODOMSelectionManager and DeviceEditor

 02-Aug-04	5030/1	pcameron	VBM:2004070705 Implemented ISelectionProvider on ODOMSelectionManager and DeviceEditor

 14-May-04	4413/2	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 11-May-04	4161/3	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 10-May-04	4239/2	allan	VBM:2004042207 SaveAs on DeviceEditor.

 10-May-04	4068/4	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/2	allan	VBM:2004032103 Structure page policies section.

 04-May-04	4121/4	pcameron	VBM:2004042910 Localised the policy type and composition names, and completed test cases

 04-May-04	4121/2	pcameron	VBM:2004042910 Localised the policy type and composition names, and completed test cases

 30-Apr-04	4081/2	pcameron	VBM:2004031007 Added PoliciesSection

 27-Apr-04	4050/6	pcameron	VBM:2004040701 Added a device Information page and augmented DeviceRepositoryBrowser's title

 27-Apr-04	4016/4	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 26-Apr-04	4040/1	pcameron	VBM:2004032211 Added DeviceStructurePart

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 ===========================================================================
*/
