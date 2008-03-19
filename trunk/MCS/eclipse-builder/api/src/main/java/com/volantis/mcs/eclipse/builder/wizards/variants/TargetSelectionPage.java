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
package com.volantis.mcs.eclipse.builder.wizards.variants;

import com.volantis.mcs.eclipse.builder.common.targets.TargetSelector;
import com.volantis.mcs.eclipse.common.ProjectReceiver;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import java.util.Iterator;
import java.util.List;

/**
 * Wizard page for specifying target selections for
 */
public class TargetSelectionPage extends WizardPage
        implements VariantWizardPage, ProjectReceiver {
    private TargetSelector selector;

    private IProject project;

    private List categories;

    public TargetSelectionPage(IProject project, List categories) {
        super("TargetSelectionPage");
        this.project = project;
        this.categories = categories;
    }

    public void createControl(Composite composite) {
        selector = new TargetSelector(composite, categories, null, project);
        selector.setLayoutData(new GridData(
                GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        setControl(selector);
    }

    /**
     * Adds the selected targets to any provided variants with a targeted
     * selection. It should always be the case that when this page is
     * displayed there is a single variant with a targeted selection, but
     * this approach is safer.
     *
     * @param variants A list of variants being created by this wizard
     */
    public void performFinish(List variants) {
        Iterator it = variants.iterator();
        while (it.hasNext()) {
            VariantBuilder variant = (VariantBuilder) it.next();
            SelectionBuilder selection = variant.getSelectionBuilder();
            if (selection instanceof TargetedSelectionBuilder) {
                TargetedSelectionBuilder targeted = (TargetedSelectionBuilder) selection;
                List categoryReferences = targeted.getModifiableCategoryReferences();
                List deviceReferences = targeted.getModifiableDeviceReferences();

                Iterator newTargets = selector.getSelectedTargets().iterator();
                while (newTargets.hasNext()) {
                    Object target = newTargets.next();
                    if (target instanceof CategoryReference) {
                        categoryReferences.add(target);
                    } else if (target instanceof DeviceReference) {
                        deviceReferences.add(target);
                    }
                }
            }
        }
    }

    public void setProject(IProject project) {
        selector.setProject(project);
    }
}
