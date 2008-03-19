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
package com.volantis.mcs.eclipse.builder.common.targets;

import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.common.DeviceTreeContentProvider;
import com.volantis.mcs.eclipse.builder.editors.common.TargetLabelProvider;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.common.ProjectReceiver;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.ProjectDeviceRepositoryProvider;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.Collection;

/**
 * Component for selecting devices from a tree containing the full hierarchy.
 */
public class DeviceSelectionTree extends TargetSelectionComponent implements ProjectReceiver {
    private static final String RESOURCE_PREFIX = "DeviceSelectionTree.";

    private IProject project;

    private static final String SELECT_TREE_LABEL = EditorMessages.getString(
            RESOURCE_PREFIX + "selectTree.label");

    private CheckboxTreeViewer deviceTree;

    public DeviceSelectionTree(Composite parent, int style, IProject project) {
        super(parent, style);
        this.project = project;
        populateComponent();
    }

    public void setCheckedElements(Object[] toCheck) {
        deviceTree.setCheckedElements(toCheck);
    }

    public void setChecked(Object toCheck, boolean checked) {
        deviceTree.setChecked(toCheck, checked);
    }

    private void populateComponent() {
        GridLayout layout = new GridLayout(1, true);
        this.setLayout(layout);

        Label listLabel = new Label(this, SWT.NONE);
        listLabel.setText(SELECT_TREE_LABEL);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        listLabel.setLayoutData(data);

        deviceTree = new CheckboxTreeViewer(this, SWT.BORDER);
        data = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
        deviceTree.getControl().setLayoutData(data);

        deviceTree.setLabelProvider(new TargetLabelProvider());
        deviceTree.setContentProvider(new DeviceTreeContentProvider());

        setProject(project);

        deviceTree.addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                fireSelectionEvent(event.getElement(), event.getChecked());
            }
        });
    }

    // Javadoc inherited
    public void setSelectedTargets(Collection selection) {
        deviceTree.setCheckedElements(selection.toArray());
    }

    // Javadoc inherited
    public void modifySelection(Object target, boolean selected) {
        deviceTree.setChecked(target, selected);
    }

    // Javadoc inherited
    public void setProject(IProject project) {
        this.project = project;
        if (project == null || !project.exists()) {
            deviceTree.setInput(null);
        } else {
            try {
                DeviceRepositoryAccessorManager dram =
                        ProjectDeviceRepositoryProvider.getSingleton().
                        getDeviceRepositoryAccessorManager(project);
                deviceTree.setInput(dram.getDeviceHierarchyDocument().
                        getRootElement());
                deviceTree.refresh();
            } catch (RepositoryException re) {
                EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                        getClass(), re);
            }
        }
    }
}
