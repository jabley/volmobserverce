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

import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.theme.ThemeContentBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionType;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.List;

/**
 * Page for selecting the selection criteria
 */
public class VariantSelectionCriteriaPage extends WizardPage
        implements VariantWizardPage {
    private static final String RESOURCE_PREFIX = "VariantSelectionCriteriaPage.";

    protected static final PolicyFactory POLICY_FACTORY =
            PolicyFactory.getDefaultInstance();

    protected Composite topLevel;

    private ListViewer criteriaList;

    private VariantType variantType;
    
    protected ISelectionChangedListener updateButtonsListener =
            new ISelectionChangedListener() {
                public void selectionChanged(SelectionChangedEvent event) {
                    getWizard().getContainer().updateButtons();
                }
            };

    public VariantSelectionCriteriaPage(VariantType variantType, String variantTypeName) {
        super(variantTypeName + ".VariantSelectionCriteriaPage");
        setDescription(WizardMessages.getString(RESOURCE_PREFIX + variantTypeName + ".description"));
        this.variantType = variantType;
    }

    // Javadoc inherited
    public void createControl(Composite parent) {
        topLevel = new Composite(parent, SWT.NONE);
        topLevel.setLayout(new GridLayout());
        topLevel.setLayoutData(new GridData(
                GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        topLevel.setFont(parent.getFont());

        GridLayout layout = new GridLayout(2, false);
        topLevel.setLayout(layout);

        String criteriaLabelText = WizardMessages.getString(RESOURCE_PREFIX +
                "selectionCriteria.label");
        Label criteriaLabel = new Label(topLevel, SWT.NONE);
        criteriaLabel.setText(criteriaLabelText);
        criteriaLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));

        criteriaList = new ListViewer(topLevel, SWT.BORDER | SWT.SINGLE);
        criteriaList.setContentProvider(new ArrayContentProvider());

        SelectionType[] selectionTypes =
                PolicyFactory.getDefaultInstance().getSelectionTypes(variantType);
        criteriaList.setInput(selectionTypes);

        criteriaList.addSelectionChangedListener(updateButtonsListener);
        criteriaList.getControl().setLayoutData(
                new GridData(GridData.FILL_HORIZONTAL));

        criteriaList.getList().deselectAll();
        criteriaList.getList().select(0);

        setControl(topLevel);
    }

    // Javadoc inherited
    public boolean isPageComplete() {
        IStructuredSelection selection =
                (IStructuredSelection) criteriaList.getSelection();
        return selection.size() == 1;
    }

    // Javadoc inherited
    public boolean canFlipToNextPage() {
        IStructuredSelection selection =
                (IStructuredSelection) criteriaList.getSelection();
        SelectionType type = (SelectionType) selection.getFirstElement();
        return SelectionType.TARGETED == type && (getNextPage() != null);
    }

    // Javadoc inherited
    public void performFinish(List variants) {
        if (variants.isEmpty()) {
            final VariantBuilder variantBuilder =
                POLICY_FACTORY.createVariantBuilder(variantType);
            variants.add(variantBuilder);
            if (variantType == VariantType.THEME) {
                final ThemeContentBuilder contentBuilder =
                    InternalPolicyFactory.getInternalInstance().
                        createThemeContentBuilder();
                variantBuilder.setContentBuilder(contentBuilder);
            }
        }
        if (variants.size() == 1) {
            VariantBuilder variant = (VariantBuilder) variants.get(0);
            variant.setSelectionBuilder(getSelectionBuilder());
        }
    }

    /**
     * Get a selection builder appropriate to the selected selection type.
     *
     * @return A selection builder, or null if no appropriate one can be found
     */
    private SelectionBuilder getSelectionBuilder() {
        IStructuredSelection selection = (IStructuredSelection) criteriaList.getSelection();
        SelectionType type = (SelectionType) selection.getFirstElement();

        SelectionBuilder builder = null;
        if (SelectionType.DEFAULT == type) {
            builder = POLICY_FACTORY.createDefaultSelectionBuilder();
        } else if (SelectionType.ENCODING == type) {
            builder = POLICY_FACTORY.createEncodingSelectionBuilder();
        } else if (SelectionType.GENERIC_IMAGE == type) {
            builder = POLICY_FACTORY.createGenericImageSelectionBuilder();
        } else if (SelectionType.TARGETED == type) {
            builder = POLICY_FACTORY.createTargetedSelectionBuilder();
        }

        return builder;
    }
}
