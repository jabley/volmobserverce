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
package com.volantis.mcs.eclipse.ab.views.devices;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;

/**
 * The main display area for parts that want to show a view/browse a
 * device repository.
 */
public class DeviceRepositoryForm extends Composite {
    /**
     * The prefix for resources associated with this class.
     */
    private static String RESOURCE_PREFIX = "DeviceRepositoryForm.";

    /**
     * Constant for device repository form margin height
     */
    private static int MARGIN_HEIGHT =
                DevicesMessages.getInteger(
                            RESOURCE_PREFIX + "marginHeight").intValue();

    /**
     * Constant for device repository form margin width
     */
    private static int MARGIN_WIDTH =
                DevicesMessages.getInteger(
                            RESOURCE_PREFIX + "marginWidth").intValue();

    /**
     * Constant for device repository form horizontal spacing
     */
    private static int HORIZONTAL_SPACING =
                DevicesMessages.getInteger(
                            RESOURCE_PREFIX + "horizontalSpacing").intValue();

    /**
     * Constant for device repository form vertical spacing
     */
    private static int VERTICAL_SPACING =
                DevicesMessages.getInteger(
                            RESOURCE_PREFIX + "verticalSpacing").intValue();

    /**
     * The DeviceRepositoryManager associated with this
     * DeviceRepositoryForm.
     */
    private DeviceRepositoryAccessorManager accessorManager;

    /**
     * The device hierarchy section for this form.
     */
    private DeviceHierarchySection hierarchy;

    /**
     * The device policies section for this form.
     */
    private DevicePoliciesSection devicePolicies;

    /**
     * The device identification section for this form.
     */
    private DeviceIdentificationSection identification;

    /**
     * Construct a new DeviceRepositoryForm.
     * @param parent The parent Composite.
     * @param style The swt style - unused.
     * @param accessorManager The DeviceRepositoryAccessorManager for
     * obtaining the device information shown in the form.
     */
    public DeviceRepositoryForm(Composite parent, int style,
                                DeviceRepositoryAccessorManager accessorManager) {
        super(parent, style);
        this.accessorManager = accessorManager;

        setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        createForm();
    }

    /**
     * Update the DeviceRepositoryAccessorManager associated with this
     * DeviceRepositoryForm.
     * @param dram the DeviceRepositoryAccessorManager
     */
    public void updateDeviceRepositoryAccessorManager(
            DeviceRepositoryAccessorManager dram) {
        if(dram == null) {
            throw new IllegalArgumentException("Cannot be null: dram");
        }
        ISelection origSelection = hierarchy.getSelection();
        String selectedDeviceName = getSelectedDeviceName();
        hierarchy.updateDeviceRepositoryAccessorManager(dram);
        devicePolicies.updateDeviceRepositoryAccessorManager(dram);
        identification.updateDeviceRepositoryAccessorManager(dram);
        if(dram.deviceExists(selectedDeviceName)) {
            hierarchy.setSelection(origSelection);
        } else {
            // Set the selection to the root device.
            hierarchy.setSelection(new StructuredSelection(accessorManager.
                getDeviceHierarchyDocument().getRootElement().
                getChildren().get(0)));
        }
    }

    /**
     * Create the composite that is the device repository form.
     */
    private void createForm() {

        // create the top level layout for the form
        GridLayout layout = new GridLayout();
        layout.marginHeight = MARGIN_HEIGHT;
        layout.marginWidth = MARGIN_WIDTH;
        layout.verticalSpacing = VERTICAL_SPACING;
        layout.horizontalSpacing = HORIZONTAL_SPACING;
        GridData data = new GridData(GridData.FILL_BOTH);
        setLayout(layout);
        setLayoutData(data);


         // add a two column contianer for the form sections. The first
        // column will display the hierarchy section and the second
        // column will display the identifiers and policies sections.
        Composite formContainer = new Composite(this, SWT.NONE);
        GridLayout formLayout = new GridLayout(2, false);
        formLayout.marginWidth = 0;
        formLayout.marginHeight = 0;
        formLayout.horizontalSpacing = HORIZONTAL_SPACING;
        GridData formData = new GridData(GridData.FILL_BOTH);
        formContainer.setLayout(formLayout);
        formContainer.setLayoutData(formData);
        Color white = getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        formContainer.setBackground(white);

        // add the devide hierarchy section
        hierarchy = new DeviceHierarchySection(formContainer,
                                               SWT.NONE,
                                               accessorManager);

        GridData hierarchyData = new GridData(GridData.FILL_VERTICAL);
        hierarchy.setLayoutData(hierarchyData);

        // the right hand column will be scrollable
        ScrolledComposite controlsScroller =
                    new ScrolledComposite(formContainer,
                                          SWT.H_SCROLL | SWT.V_SCROLL);
        controlsScroller.setLayout(new FillLayout());
        controlsScroller.setLayoutData(new GridData(GridData.FILL_BOTH));
        controlsScroller.setExpandHorizontal(true);
        controlsScroller.setExpandVertical(true);
        controlsScroller.setAlwaysShowScrollBars(false);
        controlsScroller.setBackground(white);

        // add the container for the identification section
        Composite column2 =
                    new Composite(controlsScroller, SWT.NONE);
        GridLayout column2Layout = new GridLayout();
        column2Layout.marginHeight = 0;
        column2Layout.marginWidth = 0;
        column2Layout.verticalSpacing = VERTICAL_SPACING;
        GridData column2Data = new GridData(GridData.FILL_BOTH);
        column2.setLayout(column2Layout);
        column2.setLayoutData(column2Data);
        column2.setBackground(white);

        // add the identification section
        identification = new DeviceIdentificationSection(
                    column2,
                    SWT.NONE,
                    accessorManager);
        GridData identificationData = new GridData(GridData.FILL_HORIZONTAL);
        identification.setLayoutData(identificationData);

        // add the policies section
        devicePolicies =
                new DevicePoliciesSection(column2,
                                          SWT.NONE, accessorManager);
        devicePolicies.setLayoutData(new GridData(GridData.FILL_BOTH));

        final ISelectionChangedListener listener =
                new ISelectionChangedListener() {
                    public void selectionChanged(SelectionChangedEvent event) {
                        updateSelection();
                    }
                };

        hierarchy.addSelectionChangedListener(listener);

        // force selection of the hierarchy. This appears to persuade the tree
        // to build its entire internal representation and therefore stops a
        // large delay from occuring when the tree first obtains focus.
        hierarchy.setSelection(new StructuredSelection(accessorManager.
                getDeviceHierarchyDocument().getRootElement().
                getChildren().get(0)));

        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                hierarchy.removeSelectionChangedListener(listener);
            }
        });

        column2.setSize(column2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        column2.layout();
        controlsScroller.setMinSize(
                    column2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        controlsScroller.setContent(column2);
        layout();
    }


    /**
     * Update the selected device.
     */
    private void updateSelection() {
        IStructuredSelection selection =
                (IStructuredSelection) hierarchy.getSelection();
        String selectedDevice = null;
        if (!selection.isEmpty()) {
            selectedDevice = getSelectedDeviceName();
            devicePolicies.setDevice((Element) selection.getFirstElement());
        }
        identification.setDevice(selectedDevice);
    }

    /**
     * Get the name of the currently selected device.
     * @return the name of the currently selected device in the hierarchy
     * section
     */
    private String getSelectedDeviceName() {
        IStructuredSelection selection =
                (IStructuredSelection) hierarchy.getSelection();
        String selectedDevice = null;
        if (!selection.isEmpty()) {
            selectedDevice = ((Element) selection.
                    getFirstElement()).getAttributeValue(
                            DeviceRepositorySchemaConstants.
                    DEVICE_NAME_ATTRIBUTE);
        }

        return selectedDevice;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 02-Mar-05	7226/1	doug	VBM:2005022101 Fixed Scroll bar issues with DeviceRepositoryBrowser page

 02-Mar-05	7163/3	doug	VBM:2005022101 Fixed Scroll bar issues with DeviceRepositoryBrowser page

 14-Jan-05	6681/3	allan	VBM:2004081607 Rework issues

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Nov-04	6218/1	adrianj	VBM:2004102021 Enhanced sizing for FormSections

 28-Sep-04	5671/1	adrianj	VBM:2004090111 Fix for closed 'twisties' taking too much space

 13-May-04	4335/1	matthew	VBM:2004051206 Fix Device Repository Viewer so that it actually shows device policies

 30-Apr-04	4130/1	matthew	VBM:2004043001 Modify DeviceHierarchySection to implement ISelectionProvider

 29-Apr-04	4072/1	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 11-Feb-04	2862/5	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
