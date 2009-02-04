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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.eclipse.builder.editors.common.AlertsActionsSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetAttributesSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetContentSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetSectionFactory;
import com.volantis.mcs.eclipse.builder.editors.common.AssetSelectionCriteriaSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetsSection;
import com.volantis.mcs.eclipse.builder.editors.common.BuilderEditorPart;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.diagnostic.DiagnosticEvent;
import com.volantis.mcs.interaction.diagnostic.DiagnosticListener;
import com.volantis.mcs.model.path.Path;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 */
public class ThemeOverview extends BuilderEditorPart {
    private AssetAttributesSection assetAttributesSection;
    private AssetsSection assetsSection;
    private AlertsActionsSection alertsActionsSection;
    private AssetSelectionCriteriaSection selectionCriteriaSection;
    private AssetContentSection contentSection;

    private List formToolkits = new ArrayList();

    public ThemeOverview(EditorContext context) {
        super(context);
    }

    /**
     * Create a form toolkit for use with a given parent, and store it for
     * later disposal.
     *
     * @param parent The parent composite
     * @return A form toolkit
     */
    private FormToolkit getFormToolkit(Composite parent) {
        FormToolkit toolkit = new FormToolkit(parent.getShell().getDisplay());
        formToolkits.add(toolkit);
        return toolkit;
    }

    // Javadoc inherited
    public void dispose() {
        if (formToolkits != null && !formToolkits.isEmpty()) {
            Iterator it = formToolkits.iterator();
            while (it.hasNext()) {
                ((FormToolkit) it.next()).dispose();
            }
        }
        if (alertsActionsSection != null) {
            alertsActionsSection.dispose();
        }
        if (assetAttributesSection != null) {
            assetAttributesSection.dispose();
        }
        if (assetsSection != null) {
            assetsSection.dispose();
        }
        if (contentSection != null) {
            contentSection.dispose();
        }
        if (selectionCriteriaSection != null) {
            selectionCriteriaSection.dispose();
        }
        super.dispose();
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
        Composite rightColumn = new Composite(otherSections, SWT.NONE);
        gridLayout = new GridLayout(1, true);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        gridLayout.verticalSpacing = verticalSpacing;
        rightColumn.setLayout(gridLayout);
        data = new GridData(GridData.FILL_BOTH);
        rightColumn.setLayoutData(data);
        rightColumn.setBackground(composite.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // The Asset Attributes Section
        createAssetSelectionCriteriaSection(rightColumn);

        // The Asset Content Section
        createAssetContentSection(rightColumn);

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

    /**
     * Creates the variant content section for this variable policy.
     *
     * @param parent The parent composite in which the section should be
     *               created
     */
    private void createAssetContentSection(Composite parent) {
        contentSection =
                new AssetContentSection(parent, SWT.NONE, getContext());

        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        contentSection.setLayoutData(data);
    }

    private void createAssetSelectionCriteriaSection(Composite parent) {
        FormToolkit formToolkit =getFormToolkit(parent);
        Form form = formToolkit.createForm(parent);
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        form.setLayoutData(data);

        selectionCriteriaSection =
                new AssetSelectionCriteriaSection(form.getBody(), SWT.NONE, getContext());
        data = new GridData(GridData.FILL_HORIZONTAL);
        selectionCriteriaSection.setLayoutData(data);
    }

    private void createAlertsActionsSection(Composite parent) {
        FormToolkit formToolkit = getFormToolkit(parent);
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
        FormToolkit formToolkit = getFormToolkit(parent);
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
        FormToolkit formToolkit = getFormToolkit(parent);
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

 13-Dec-05	10345/2	adrianj	VBM:2005111601 Add style rule view

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
