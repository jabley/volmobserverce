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
import com.volantis.mcs.eclipse.builder.editors.common.TargetLabelProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.Collection;
import java.util.List;

/**
 * A component for selecting from a list of available categories.
 */
public class CategorySelectionList extends TargetSelectionComponent {
    private static final String RESOURCE_PREFIX = "CategorySelectionList.";

    private static final String SELECT_CATEGORY_LABEL =
            EditorMessages.getString(RESOURCE_PREFIX + "selectCategory.label");

    private List categories;

    private CheckboxTableViewer categoryList;

    public CategorySelectionList(Composite parent, int style, List categories) {
        super(parent, style);
        this.categories = categories;
        populateComponent();
    }

    private void populateComponent() {
        GridLayout layout = new GridLayout(1, true);
        this.setLayout(layout);

        Label listLabel = new Label(this, SWT.NONE);
        listLabel.setText(SELECT_CATEGORY_LABEL);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        listLabel.setLayoutData(data);

        categoryList = CheckboxTableViewer.newCheckList(this, SWT.BORDER);
        data = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
        categoryList.getControl().setLayoutData(data);

        categoryList.setLabelProvider(new TargetLabelProvider());
        categoryList.setContentProvider(new ArrayContentProvider());
        categoryList.setInput(categories);

        categoryList.addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                fireSelectionEvent(event.getElement(), event.getChecked());
            }
        });
    }

    // Javadoc inherited
    public void setSelectedTargets(Collection selection) {
        categoryList.setCheckedElements(selection.toArray());
    }

    // Javadoc inherited
    public void modifySelection(Object target, boolean selected) {
        categoryList.setChecked(target, selected);
    }
}
