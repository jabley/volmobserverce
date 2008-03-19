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
package com.volantis.mcs.eclipse.builder.wizards;

import com.volantis.mcs.accessors.xml.jdom.XMLAccessorConstants;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.editors.themes.ThemesMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.ProjectDeviceRepositoryProvider;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * A wizard page for selecting a device from the device repository.
 */
public class DeviceSelectionPage extends WizardPage {
    /**
     * The project associated with this page.
     */
    private IProject project;

    /**
     * Store a object array of no elements (used for deselected check items).
     */
    private static final Object[] NO_ELEMENTS = new Object[]{};

    /**
     * The resource prefix used for extracting string resource names.
     */
    private static final String RESOURCE_PREFIX = "DeviceSelectionPage.";

    /**
     * The height hint of the tree control.
     */
    private static final int HEIGHT = 280;

    /**
     * the check box tree viewer.
     */
    protected CheckboxTreeViewer checkboxTreeViewer;

        /**
     * The device tree content provider.
     */
    private final ITreeContentProvider contentProvider =
            new ElementChildrenTreeContentProvider();

    /**
     * Label provider that provides the view of each item in the tree.
     */
    private final ILabelProvider labelProvider =
            new DeviceHierarchyLabelProvider();

    /**
     * Flag to indicate that the viewer has been initialized.
     */
    private boolean viewerInitialized = false;

    /**
     * Create a new device selection page for a given project.
     *
     * @param project The project this page is associated with
     */
    public DeviceSelectionPage(IProject project) {
        super("DeviceSelectionPage");
        this.project = project;
    }

    /**
     * Provide a list of device names that are current checked.
     *
     * @return a list of of device names that are current checked.
     */
    public List getSelectedDevices() {
        List devices = null;
        Object[] checkedItems = checkboxTreeViewer.getCheckedElements();

        for (int i = 0; i < checkedItems.length; i++) {
            if (devices == null) {
                devices = new ArrayList(checkedItems.length);
            }

            String devName = ((Element) checkedItems[i]).getAttributeValue(
                    XMLAccessorConstants.DEVICE_NAME_ATTRIBUTE);
            devices.add(devName);
        }
        return devices;
    }

    // javadoc inherited
    public void createControl(Composite parent) {

        Composite container = new Composite(parent, SWT.NULL);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        container.setLayoutData(gd);

        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        container.setLayout(layout);

        Label label = new Label(container, SWT.NULL);
        label.setText(ThemesMessages.getString(RESOURCE_PREFIX + "tree.label")); //$NON-NLS-1$

        createCheckboxTreeViewer(container, SWT.NULL);

        Button button = new Button(container, SWT.NONE);
        button.setText(ThemesMessages.getString(RESOURCE_PREFIX + "button.label")); //$NON-NLS-1$
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionevent) {
                checkboxTreeViewer.setCheckedElements(NO_ELEMENTS);
            }
        });

        setErrorMessage(null);
        setMessage(null);
        setControl(container);
        showEmptyListMessage(label, button);
    }

    /**
     * Initialize the tree viewer with a content provider and input. This
     * method assumes that project is not null.
     *
     * NOTE: This method only initializes the viewer content if it has
     * not already done so.
     */
    private void initializeViewerContent() {
        if (!viewerInitialized) {
            // Provide data for the viewer.
            checkboxTreeViewer.setContentProvider(contentProvider);
            try {
                DeviceRepositoryAccessorManager dram =
                        ProjectDeviceRepositoryProvider.getSingleton().
                        getDeviceRepositoryAccessorManager(project);
                checkboxTreeViewer.setInput(dram.getDeviceHierarchyDocument().
                        getRootElement());
                viewerInitialized = true;
            } catch (RepositoryException e) {
                EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
            }
        }
    }

    /**
     * Show an empty list message if the list/tree is empty.
     */
    private void showEmptyListMessage(Label label, Button button) {
        if (project != null) {
            try {
                DeviceRepositoryAccessorManager dram =
                        ProjectDeviceRepositoryProvider.getSingleton().
                        getDeviceRepositoryAccessorManager(project);
                if (!contentProvider.
                        hasChildren(dram.getDeviceHierarchyDocument().
                        getRootElement())) {
                    setErrorMessage(ThemesMessages.getString(RESOURCE_PREFIX +
                            "emptyList"));
                    button.setEnabled(false);
                    label.setEnabled(false);
                }
            } catch (RepositoryException e) {
                EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), e);
            }
        }
    }

    /**
     * Helper method for creating the checkbox tree viewer composite control
     * with the appropriate layout, label provider and content provider.
     *
     * @param parent the parent composite.
     * @param style  the style to use for creating the check box tree viewer.
     */
    private void createCheckboxTreeViewer(Composite parent,
                                          int style) {

        checkboxTreeViewer = new CheckboxTreeViewer(parent, style);
        checkboxTreeViewer.setLabelProvider(labelProvider);

        checkboxTreeViewer.setAutoExpandLevel(2);

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        data.heightHint = HEIGHT;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        checkboxTreeViewer.getTree().setLayoutData(data);

        if (project != null) {
            initializeViewerContent();
        }
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 ===========================================================================
*/
