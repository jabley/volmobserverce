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

import com.volantis.mcs.eclipse.ab.editors.devices.DeviceHierarchyLabelProvider;
import com.volantis.mcs.eclipse.ab.editors.dom.ElementChildrenTreeContentProvider;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Element;

/**
 * A FormSection showing the device hierarchy.
 */
public class DeviceHierarchySection extends FormSection implements ISelectionProvider {
    /**
     * The prefix for resources associated with this class.
     */
    private static String RESOURCE_PREFIX = "DeviceHierarchySection.";

    /**
     * The default minimum width for device hierarchy sections.
     */
    private static final int DEFAULT_MIN_WIDTH = DevicesMessages.getInteger(
            RESOURCE_PREFIX + "minWidth").intValue();

    /**
     * The TreeViewer used for viewing the device hierarchy.
     */
    private TreeViewer viewer;

    /**
     * The DeviceRepositoryAccessorManager associated with this section.
     */
    private final DeviceRepositoryAccessorManager accessorManager;

    /**
     * The display area composite.
     */
    private Composite displayArea;

    /**
     * Construct a new DeviceHierarchySection that derives its
     * content using a specified DeviceRepositoryAccessorManager.
     * @param parent The parent Composite.
     * @param style The swt style - unused.
     * @param accessorManager The DeviceRepositoryAccessorManager from
     * which to obtain the device hierarchy.
     * @throws IllegalArgumentException If accessorManager is null.
     */
    public DeviceHierarchySection(Composite parent, int style,
                                  DeviceRepositoryAccessorManager accessorManager) {
        super(parent, style);

        setMinWidth(DEFAULT_MIN_WIDTH);

        if (accessorManager == null) {
            throw new
                    IllegalArgumentException("Cannot be null: accessorManager");
        }

        this.accessorManager = accessorManager;

        createDisplayArea(DevicesMessages.getString(RESOURCE_PREFIX + "title"),
                DevicesMessages.getString(RESOURCE_PREFIX + "message"));
    }

    /**
     * Create the displayArea for this section.
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, SWT.NONE, title, message);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        ITreeContentProvider contentProvider =
                new ElementChildrenTreeContentProvider();
        ILabelProvider labelProvider = new DeviceHierarchyLabelProvider();
        Element document = accessorManager.getDeviceHierarchyDocument().
                getRootElement();

        displayArea = new Composite(section, SWT.BORDER);
        section.setClient(displayArea);

        displayArea.setLayout(new FillLayout());
        displayArea.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        data = new GridData(GridData.FILL_BOTH);
        displayArea.setLayoutData(data);
        Tree tree = new Tree(displayArea, SWT.SINGLE | SWT.H_SCROLL |
                SWT.V_SCROLL);
        tree.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        viewer = new TreeViewer(tree);
        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(labelProvider);
        viewer.setInput(document);
        viewer.expandToLevel(2);

    }

    /**
     * Update the DeviceRepositoryAccessorManager associated with this
     * DevicePoliciesSection. This method will cause the table of policies
     * to be refreshed with content provided by the given
     * DeviceRepositoryAccessorManager.
     * @param dram the DeviceRepositoryAccessorManager
     */
    void updateDeviceRepositoryAccessorManager(
            DeviceRepositoryAccessorManager dram) {
        viewer.setInput(dram.getDeviceHierarchyDocument().getRootElement());
        viewer.expandToLevel(2);
    }

    // implement ISelectionProvider

    /**
     * Add a selection listener to listen for selection of devices in the
     * hierarchy.
     * @throws IllegalStateException If the tree viewer associated with this
     * section has not yet been initialized.
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        if (viewer == null) {
            throw new IllegalStateException("Tree viewer not yet initialized.");
        }
        viewer.addSelectionChangedListener(listener);
    }

    /**
     * Remove a previously added selection change listener.
     * @throws IllegalStateException If the tree viewer associated with this
     * section has not yet been initialized.
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        if (viewer == null) {
            throw new IllegalStateException("Tree viewer not yet initialized.");
        }
        viewer.removeSelectionChangedListener(listener);
    }

    // JavaDoc inherited
    public ISelection getSelection() {
        return viewer.getSelection();
    }

    // javaDoc inherited
    public void setSelection(ISelection selection) {
        viewer.setSelection(selection);
        viewer.reveal(((IStructuredSelection)selection).getFirstElement());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Nov-04	6218/1	adrianj	VBM:2004102021 Enhanced sizing for FormSections

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 30-Apr-04	4130/1	matthew	VBM:2004043001 Modify DeviceHierarchySection to implement ISelectionProvider

 29-Apr-04	4072/4	matthew	VBM:2004042601 Sorting of device hierarchy views removed

 29-Apr-04	4072/2	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 11-Feb-04	2862/3	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
