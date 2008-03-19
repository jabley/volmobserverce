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
import com.volantis.mcs.eclipse.builder.editors.common.TargetComparator;
import com.volantis.mcs.eclipse.builder.editors.common.TargetLabelProvider;
import com.volantis.mcs.eclipse.builder.editors.common.TargetSelectionListener;
import com.volantis.mcs.eclipse.builder.editors.common.TargetSelectionEvent;
import com.volantis.mcs.eclipse.common.ProjectReceiver;
import com.volantis.synergetics.ObjectHelper;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.core.resources.IProject;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A component that allows selection of targets, either device or category
 * references.
 */
public class TargetSelector extends Composite implements ProjectReceiver {
    private static final String RESOURCE_PREFIX = "TargetSelector.";

    private static final String DEVICE_LIST_LABEL =
            EditorMessages.getString(RESOURCE_PREFIX + "deviceList.label");
    private static final String DEVICE_TREE_LABEL =
            EditorMessages.getString(RESOURCE_PREFIX + "deviceTree.label");
    private static final String DEVICE_CATEGORY_LABEL =
            EditorMessages.getString(RESOURCE_PREFIX + "deviceCategory.label");
    private static final String SELECTION_LIST_LABEL =
            EditorMessages.getString(RESOURCE_PREFIX + "selectionList.label");
    private static final String REMOVE_LABEL =
            EditorMessages.getString(RESOURCE_PREFIX + "remove.label");

    private static final int WIDTH_HINT = 600;
    private static final int LIST_HEIGHT_HINT = 150;
    private static final int TAB_HEIGHT_HINT = 300;

    private Collection targets = new TreeSet(new TargetComparator());

    private TableViewer selectedTargets;

    private TargetSelectionMonitor monitor;

    private IProject project;

    private List targetSelectionListeners = new ArrayList();

    private List projectReceivers = new ArrayList();

    /**
     * A selection listener that adds/removes items from the list of selected
     * targets and checks/unchecks them within the tabs.
     *
     * Also passes on the selection event to any external listeners.
     */
    private final TargetSelectionListener selectionListener =
            new TargetSelectionListener() {
                // Javadoc inherited
                public void targetSelectionChanged(TargetSelectionEvent event) {
                    if (event.isSelected()) {
                        targets.add(event.getTarget());
                        selectedTargets.refresh();
                    } else {
                        targets.remove(event.getTarget());
                        selectedTargets.refresh();
                    }
                    monitor.modifySelection(event.getTarget(), event.isSelected());

                    fireTargetSelectionEvent(event);
                }
            };

    /**
     * The categories that are valid targets for this selector.
     */
    private List categories;

    /**
     * Constructs a target selector with no initial selection and no categories
     * tab.
     *
     * @param parent The parent composite
     */
    public TargetSelector(Composite parent, IProject project) {
        this(parent, null, null, project);
    }

    /**
     * Constructs a target selector with the specified initial selection and
     * categories.
     *
     * @param parent The parent composite
     * @param categories The categories that are valid targets for this
     *                   selector - if empty or null, then the category tab
     *                   will not be displayed
     * @param selectedTargets The targets that are initially selected
     */
    public TargetSelector(Composite parent, List categories,
                          Collection selectedTargets, IProject project) {
        super(parent, SWT.NONE);
        this.project = project;
        this.categories = categories;

        setLayout(new GridLayout(1, true));
        addTabs(this);
        addSelectedTargets(this);

        if (selectedTargets != null && !selectedTargets.isEmpty()) {
            setSelectedTargets(selectedTargets);
        }
    }

    /**
     * Add the tabs for selecting/removing targets.
     *
     * @param parent The parent composite
     */
    private void addTabs(Composite parent) {
        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = TAB_HEIGHT_HINT;
        data.widthHint = WIDTH_HINT;

        TabFolder folder = new TabFolder(parent, SWT.NONE);
        folder.setLayoutData(data);

        TabItem item = new TabItem(folder, SWT.NULL);
        item.setText(DEVICE_LIST_LABEL);
        DeviceSelectionList deviceSelectionList =
                new DeviceSelectionList(folder, SWT.NONE, project);
        item.setControl(deviceSelectionList);
        deviceSelectionList.addTargetSelectionListener(selectionListener);
        projectReceivers.add(deviceSelectionList);

        item = new TabItem(folder, SWT.NULL);
        item.setText(DEVICE_TREE_LABEL);
        DeviceSelectionTree deviceSelectionTree =
                new DeviceSelectionTree(folder, SWT.NONE, project);
        item.setControl(deviceSelectionTree);
        deviceSelectionTree.addTargetSelectionListener(selectionListener);
        projectReceivers.add(deviceSelectionTree);

        CategorySelectionList categoryList = null;
        if (categories != null && !categories.isEmpty()) {
            item = new TabItem(folder, SWT.NULL);
            item.setText(DEVICE_CATEGORY_LABEL);
            categoryList = new CategorySelectionList(folder, SWT.NONE, categories);
            item.setControl(categoryList);
            categoryList.addTargetSelectionListener(selectionListener);
        }

        monitor = new TargetSelectionMulticaster(new TargetSelectionMonitor[] {
            deviceSelectionList, deviceSelectionTree, categoryList });

        monitor.setSelectedTargets(targets);
    }

    /**
     * Add the GUI components for displaying and editing the list of selected
     * targets.
     *
     * @param parent The parent composite
     */
    private void addSelectedTargets(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(SELECTION_LIST_LABEL);
        selectedTargets = new TableViewer(parent);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = LIST_HEIGHT_HINT;
        data.widthHint = WIDTH_HINT;
        selectedTargets.getControl().setLayoutData(data);
        selectedTargets.setLabelProvider(new TargetLabelProvider());
        selectedTargets.setContentProvider(new ArrayContentProvider());
        selectedTargets.setInput(targets);
        Button removeButton = new Button(parent, SWT.NONE);
        removeButton.setText(REMOVE_LABEL);
        removeButton.addSelectionListener(new SelectionListener() {
            // Javadoc inherited
            public void widgetSelected(SelectionEvent event) {
                handleRemove();
            }

            // Javadoc inherited
            public void widgetDefaultSelected(SelectionEvent event) {
                handleRemove();
            }
        });
    }

    /**
     * Handle the remove button being pressed - remove the selected item(s) from
     * the list.
     */
    private void handleRemove() {
        ISelection selection = selectedTargets.getSelection();
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            Object[] selected = structuredSelection.toArray();
            for (int i = 0; i < selected.length; i++) {
                targets.remove(selected[i]);
                monitor.modifySelection(selected[i], false);
                TargetSelectionEvent event =
                        new TargetSelectionEvent(this, selected[i], false);
                fireTargetSelectionEvent(event);
            }
            selectedTargets.refresh();
        }
    }

    private void fireTargetSelectionEvent(TargetSelectionEvent event) {
        Iterator it = targetSelectionListeners.iterator();
        while (it.hasNext()) {
            TargetSelectionListener listener = (TargetSelectionListener) it.next();
            listener.targetSelectionChanged(event);
        }
    }

    /**
     * Returns the targets currently selected within this component.
     *
     * @return The targets currently selected within this component
     */
    public Collection getSelectedTargets() {
        return targets;
    }

    /**
     * Set the targets that are selected within this component.
     *
     * @param targets The targets to be listed as selected
     */
    public void setSelectedTargets(Collection targets) {
        this.targets.clear();
        this.targets.addAll(targets);
        monitor.setSelectedTargets(targets);
    }

    // Javadoc not required
    public void addTargetSelectionListener(TargetSelectionListener newListener) {
        targetSelectionListeners.add(newListener);
    }

    // Javadoc not required
    public void removeTargetSelectionListener(TargetSelectionListener exListener) {
        targetSelectionListeners.remove(exListener);
    }

    // Javadoc inherited
    public void setProject(IProject project) {
        // If the project has actually changed, pass this on to the tabs.
        if (!ObjectHelper.equals(this.project, project)) {
            this.project = project;
            Iterator it = projectReceivers.iterator();
            while (it.hasNext()) {
                ProjectReceiver receiver = (ProjectReceiver) it.next();
                receiver.setProject(project);
            }
        }
    }
}
