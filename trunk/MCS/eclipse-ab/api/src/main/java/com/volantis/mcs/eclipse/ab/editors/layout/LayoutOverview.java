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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.builder.editors.common.AlertsActionsSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetAttributesSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetContentSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetSelectionCriteriaSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetsSection;
import com.volantis.mcs.eclipse.builder.editors.common.BuilderEditorPart;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.builder.editors.common.AssetSectionFactory;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.diagnostic.DiagnosticEvent;
import com.volantis.mcs.interaction.diagnostic.DiagnosticListener;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.VariantType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * The overview page for the layout editor, including selection of variants.
 */
public class LayoutOverview extends BuilderEditorPart {

    private AssetAttributesSection assetAttributesSection;
    private AssetsSection assetsSection;
    private AlertsActionsSection alertsActionsSection;

    public LayoutOverview(EditorContext context) {
        super(context);
        ((LayoutEditorContext) context).setLayoutOverview(this);
        ((PolicyEditorContext) context).setPolicyType(PolicyType.LAYOUT);
        ((PolicyEditorContext) context).setDefaultVariantType(VariantType.LAYOUT);
    }

    // Javadoc inherited
    public void createPartControl(Composite composite) {
        ScrolledComposite scroller = new ScrolledComposite(composite,
                SWT.H_SCROLL | SWT.V_SCROLL);
        scroller.setExpandHorizontal(true);
        scroller.setExpandVertical(true);

        Composite scrollable = new Composite(scroller, SWT.NONE);
        scroller.setContent(scrollable);

        scrollable.setBackground(composite.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // TODO better get these from the appropriate place
        int marginHeight = 15;
        int marginWidth = 13;
        int horizontalSpacing = 15;
        int verticalSpacing = 13;

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = marginHeight;
        gridLayout.marginWidth = marginWidth;
        gridLayout.verticalSpacing = verticalSpacing;
        scrollable.setLayout(gridLayout);
        GridData data = new GridData(GridData.FILL_BOTH);
        scrollable.setLayoutData(data);

        // The Alerts and Actions Section
        createAlertsActionsSection(scrollable);

        // The Assets and Attributes section
        createAssetAttributesSection(scrollable);

        // Make a 2 column composite for the rest of the sections.
        Composite otherSections = new Composite(scrollable, SWT.NONE);
        gridLayout = new GridLayout(2, true);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.horizontalSpacing = horizontalSpacing;
        otherSections.setLayout(gridLayout);
        data = new GridData(GridData.FILL_BOTH);
        otherSections.setLayoutData(data);
        otherSections.setBackground(composite.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // The Assets Section
        createAssetsSection(otherSections);

        // The remaining sections in column 2.
        Composite column2 = new Composite(otherSections, SWT.NONE);
        gridLayout = new GridLayout(1, true);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.verticalSpacing = verticalSpacing;
        column2.setLayout(gridLayout);
        data = new GridData(GridData.FILL_BOTH);
        column2.setLayoutData(data);
        column2.setBackground(composite.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // The Asset Attributes Section
        createAssetSelectionCriteriaSection(column2);
        createAssetContentSection(column2);
        // createAssetMetadataSection(column2);

        configureScrolling(scroller);
    }

    // Javadoc inherited
    public void setFocus() {
    }

    // Javadoc inherited
    public void init(IEditorSite iEditorSite, IEditorInput iEditorInput)
            throws PartInitException {
        super.init(iEditorSite, iEditorInput);

        final Proxy proxy = getContext().getInteractionModel();

        if (proxy != null) {
            proxy.addDiagnosticListener(
                new DiagnosticListener() {
                    public void diagnosticsChanged(DiagnosticEvent event) {
                        alertsActionsSection.updateErrorStatus();
                    }
                }
            );
        }
    }

    private void createAssetSelectionCriteriaSection(Composite parent) {
        FormToolkit formToolkit =
                new FormToolkit(parent.getShell().getDisplay());
        Form form = formToolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        form.setLayoutData(data);

        AssetSelectionCriteriaSection selectionCriteriaSection =
                new AssetSelectionCriteriaSection(form.getBody(), SWT.NONE, getContext());
        data = new GridData(GridData.FILL_HORIZONTAL);
        selectionCriteriaSection.setLayoutData(data);
    }

    private void createAssetContentSection(Composite parent) {
        FormToolkit formToolkit =
                new FormToolkit(parent.getShell().getDisplay());
        Form form = formToolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        form.setLayoutData(data);

        LayoutEditorContext context = (LayoutEditorContext) getContext();

        AssetContentSection contentSection =
                new AssetContentSection(form.getBody(), SWT.NONE, context);
        data = new GridData(GridData.FILL_HORIZONTAL);
        contentSection.setLayoutData(data);

        // Register the content section with the context.
        context.addLayoutModificationListener(
                contentSection.getLayoutModificationListener());
    }

    private void createAlertsActionsSection(Composite parent) {
        FormToolkit formToolkit =
                new FormToolkit(parent.getShell().getDisplay());
        Form form = formToolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        form.setLayoutData(data);

        alertsActionsSection =
                new AlertsActionsSection(form.getBody(), SWT.NONE, getContext());
        data = new GridData(GridData.FILL_HORIZONTAL);
        alertsActionsSection.setLayoutData(data);
    }

    private void createAssetsSection(Composite parent) {
        FormToolkit formToolkit =
                new FormToolkit(parent.getShell().getDisplay());
        Form form = formToolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);

        GridData data = new GridData(GridData.FILL_BOTH);
        form.setLayoutData(data);

        assetsSection = AssetSectionFactory.getDefaultInstance().
                createAssetsSection(form.getBody(), getContext());
        data = new GridData(GridData.FILL_BOTH);
        assetsSection.setLayoutData(data);
    }

    private void createAssetAttributesSection(Composite parent) {
        FormToolkit formToolkit =
                new FormToolkit(parent.getShell().getDisplay());
        Form form = formToolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        form.setLayoutData(data);

        assetAttributesSection = AssetSectionFactory.getDefaultInstance().
                createAssetAttributesSection(form.getBody(), getContext());
        data = new GridData(GridData.FILL_HORIZONTAL);
        assetAttributesSection.setLayoutData(data);
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        assetAttributesSection.setFocus(path);
        assetsSection.setFocus(path);
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 ===========================================================================
*/
