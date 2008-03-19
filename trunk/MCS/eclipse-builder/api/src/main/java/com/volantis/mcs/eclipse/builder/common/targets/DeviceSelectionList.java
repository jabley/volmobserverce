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

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.common.StringMatcher;
import com.volantis.mcs.eclipse.builder.editors.common.TargetComparator;
import com.volantis.mcs.eclipse.builder.editors.common.TargetLabelProvider;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.common.ProjectReceiver;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.ProjectDeviceRepositoryProvider;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * A component for selecting devices from a searchable list.
 */
public class DeviceSelectionList extends TargetSelectionComponent implements ProjectReceiver {
    private static final String RESOURCE_PREFIX = "DeviceSelectionList.";

    private static final PolicyFactory POLICY_FACTORY =
            PolicyFactory.getDefaultInstance();

    private static final String FILTER_LABEL = EditorMessages.getString(RESOURCE_PREFIX + "filter.label");
    private static final String SELECT_LIST_LABEL = EditorMessages.getString(RESOURCE_PREFIX + "selectList.label");

    private CheckboxTableViewer deviceList;

    private IProject project;

    private Collection selection = new ArrayList();

    public DeviceSelectionList(Composite parent, int style, IProject project) {
        super(parent, style);
        this.project = project;
        populateComponent();
    }

    private void populateComponent() {
        GridLayout layout = new GridLayout(1, true);
        this.setLayout(layout);

        Label filterLabel = new Label(this, SWT.NONE);
        filterLabel.setText(FILTER_LABEL);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        filterLabel.setLayoutData(data);

        final Text filter = new Text(this, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        filter.setLayoutData(data);

        Label listLabel = new Label(this, SWT.NONE);
        listLabel.setText(SELECT_LIST_LABEL);
        data = new GridData(GridData.FILL_HORIZONTAL);
        listLabel.setLayoutData(data);

        deviceList = CheckboxTableViewer.newCheckList(this, SWT.BORDER);
        data = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
        deviceList.getControl().setLayoutData(data);

        final DeviceFilteringContentProvider contentProvider =
                new DeviceFilteringContentProvider();

        deviceList.setLabelProvider(new TargetLabelProvider());
        deviceList.setContentProvider(contentProvider);

        setProject(project);

        deviceList.addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                DeviceReference deviceRef = (DeviceReference) event.getElement();
                fireSelectionEvent(deviceRef, event.getChecked());
            }
        });

        filter.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                contentProvider.setFilterString(filter.getText());
                deviceList.refresh();
                deviceList.setCheckedElements(selection.toArray());
            }
        });
    }

    // Javadoc inherited
    public void setSelectedTargets(Collection newSelection) {
        selection.clear();
        selection.addAll(newSelection);
        deviceList.setCheckedElements(selection.toArray());
    }

    // Javadoc inherited
    public void modifySelection(Object target, boolean selected) {
        if (selected) {
            selection.add(target);
        } else {
            selection.remove(target);
        }
        deviceList.setCheckedElements(selection.toArray());
    }

    // Javadoc inherited
    public void setProject(IProject project) {
        // If the project changes, repopulate the list of devices
        this.project = project;
        if (project == null || !project.exists()) {
            deviceList.setInput(null);
        } else {
            try {
                DeviceRepositoryAccessorManager dram =
                        ProjectDeviceRepositoryProvider.getSingleton().
                        getDeviceRepositoryAccessorManager(project);
                deviceList.setInput(dram.getDeviceHierarchyDocument().
                        getRootElement());
                deviceList.refresh();
            } catch (RepositoryException re) {
                EclipseCommonPlugin.logError(BuilderPlugin.getDefault(),
                        getClass(), re);
            }
        }
    }

    /**
     * A content provider that filters devices from a list based on a simple
     * search string.
     */
    private class DeviceFilteringContentProvider implements IStructuredContentProvider {
        private Collection filteredValues = new TreeSet(new TargetComparator());

        private Element devicesRoot;

        private StringMatcher filter = new StringMatcher("");

        // Javadoc inherited
        public Object[] getElements(Object o) {
            return filteredValues.toArray();
        }

        // Javadoc not required
        public void setFilterString(String newFilter) {
            filter.setFilter(newFilter);
            filterInput();
        }

        // Javadoc inherited
        public void dispose() {
        }

        // Javadoc inherited
        public void inputChanged(Viewer viewer, Object o, Object o1) {
            devicesRoot = (Element) o1;
            filterInput();
        }

        /**
         * Filter the input data based on the currently set filter string.
         */
        private void filterInput() {
            filteredValues.clear();
            if (devicesRoot != null) {
                addNode(devicesRoot);
            }
        }

        /**
         * Recursively add matching devices from the hierarchy into the list
         * of filtered values.
         *
         * @param element The node to process
         */
        private void addNode(Element element) {
            String deviceName = element.getAttributeValue(DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
            if (deviceName != null && filter.matches(deviceName)) {
                filteredValues.add(POLICY_FACTORY.createDeviceReference(deviceName));
            }
            Iterator children = element.getChildren().iterator();
            while (children.hasNext()) {
                addNode((Element) children.next());
            }
        }
    }
}
