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
package com.volantis.mcs.eclipse.builder.editors.policies;

import com.volantis.mcs.eclipse.builder.editors.common.AssetContentSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetMetadataSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetSelectionCriteriaSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetsSection;
import com.volantis.mcs.eclipse.builder.editors.common.AssetSectionFactory;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.policies.variants.VariantType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Provides shared structure and functionality for all variable policy editors.
 * Specific editors for particular policy types should extend this class and
 * provide specific details for their policy type.
 */
public abstract class VariablePolicyEditor extends PolicyEditor {
    /**
     * The assets section listing the individual variants.
     */
    private AssetsSection assetsSection;

    /**
     * The section allowing definition of the selection criteria for the
     * selected variant.
     */
    private AssetSelectionCriteriaSection selectionCriteriaSection;

    /**
     * The section allowing definition of the content for the selected variant.
     */
    private AssetContentSection contentSection;

    /**
     * The section allowing definition of the metadata for the selected variant.
     */
    private AssetMetadataSection metadataSection;

    public VariablePolicyEditor() {
        super();
        ((PolicyEditorContext) getContext()).setDefaultVariantType(getDefaultVariantType());
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        super.setFocus(path);
        setFocusIfExists(assetsSection, path);
        setFocusIfExists(selectionCriteriaSection, path);
        setFocusIfExists(contentSection, path);
        setFocusIfExists(metadataSection, path);
    }

    // Javadoc inherited
    public void dispose() {
        if (contentSection != null) {
            contentSection.dispose();
        }
        if (metadataSection != null) {
            metadataSection.dispose();
        }
        if (selectionCriteriaSection != null) {
            selectionCriteriaSection.dispose();
        }
        if (assetsSection != null) {
            assetsSection.dispose();
        }
        super.dispose();
    }

    /**
     * Specifies the default type of variant for the policy edited with this
     * editor.
     *
     * @return The default variant type for this policy
     */
    protected abstract VariantType getDefaultVariantType();

    // Javadoc inherited
    public void createPartControl(Composite composite) {
        ScrolledComposite scroller =
                new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL);
        scroller.setExpandHorizontal(true);
        scroller.setExpandVertical(true);

        GridData data = new GridData(GridData.FILL_BOTH);
        scroller.setLayoutData(data);

        Composite scrollable = new Composite(scroller, SWT.NONE);
        scroller.setContent(scrollable);

        data = new GridData(GridData.FILL_BOTH);
        scrollable.setLayoutData(data);

        setDefaultColour(scrollable);
        setDefaultColour(scroller);

        // TODO better get these from the appropriate place
        int marginHeight = 15;
        int marginWidth = 13;
        int horizontalSpacing = 15;
        int verticalSpacing = 13;

        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = marginHeight;
        gridLayout.marginWidth = marginWidth;
        gridLayout.verticalSpacing = verticalSpacing;
        scrollable.setLayout(gridLayout);

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
        setDefaultColour(otherSections);

        createAssetsSection(otherSections);

        Composite rightColumn = new Composite(otherSections, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        rightColumn.setLayoutData(data);
        setDefaultColour(rightColumn);

        gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        rightColumn.setLayout(gridLayout);

        PolicyModelSet modelSet = PolicyModelSet.getModelSet(getPolicyType());

        createAssetSelectionCriteriaSection(rightColumn);

        // Only allow content editing if there are content classes to create
        if (modelSet.getContentClasses() != null) {
            createAssetContentSection(rightColumn);
        }

        if (hasMetaData()) {
            createAssetMetadataSection(rightColumn);
        }
        configureScrolling(scroller);
    }

    /**
     * Creates the variant selection criteria section for this variable policy.
     *
     * @param parent The parent composite in which the section should be
     *               created
     */
    private void createAssetSelectionCriteriaSection(Composite parent) {
        selectionCriteriaSection =
                new AssetSelectionCriteriaSection(parent, SWT.NONE, getContext());
        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        data.heightHint = 200;
        selectionCriteriaSection.setLayoutData(data);
        selectionCriteriaSection.packSection();
        setDefaultColour(selectionCriteriaSection);
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

    /**
     * Creates the variant metadata section for this variable policy.
     *
     * @param parent The parent composite in which the section should be
     *               created
     */
    private void createAssetMetadataSection(Composite parent) {
        metadataSection =
                new AssetMetadataSection(parent, SWT.NONE, getContext(),
                        getComboDescriptors());

        GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        metadataSection.setLayoutData(data);
    }

    /**
     * Creates the variants section for this variable policy.
     *
     * @param parent The parent composite in which the section should be
     *               created
     */
    private void createAssetsSection(Composite parent) {
        assetsSection = AssetSectionFactory.getDefaultInstance().
                createAssetsSection(parent, getContext());
        GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
        assetsSection.setLayoutData(data);
    }

    /**
     * Flag to indicate whether the editor contains a metadata section. By
     * default it will, but children can override this.
     *
     * @return True if the editor contains a metadata section, false otherwise
     */
    protected boolean hasMetaData() {
        return true;
    }
}
