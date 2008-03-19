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
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.MontageLayout;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.Iterator;
import java.util.List;

/**
 * Specialisation of the variant selection criteria page to add specification
 * of montage/canvas for layouts.
 */
public class LayoutVariantSelectionCriteriaPage extends VariantSelectionCriteriaPage {
    /**
     * Common prefix for all resource names used by this page.
     */
    private static final String RESOURCE_PREFIX = 
            "LayoutVariantSelectionCriteriaPage.";

    /**
     * The text for the layout variant type label.
     */
    private static final String VARIANT_TYPE_LABEL =
            WizardMessages.getString(RESOURCE_PREFIX + "variantType.label");

    /**
     * The combo viewer used to select the type of layout.
     */
    private ComboViewer layoutType;

    /**
     * An object used to represent null in the layout types - required as null
     * is not accepted as a value in a structured selection by SWT.
     */
    private Object NULL_LAYOUT = new Object();

    public LayoutVariantSelectionCriteriaPage() {
        super(VariantType.LAYOUT, "layout");
    }

    // Javadoc inherited
    public void performFinish(List variants) {
        super.performFinish(variants);
        Iterator it = variants.iterator();
        while (it.hasNext()) {
            VariantBuilder variant = (VariantBuilder) it.next();
            InternalLayoutContentBuilder content = (InternalLayoutContentBuilder)
                    ((InternalPolicyFactory) POLICY_FACTORY).createLayoutContentBuilder();
            LayoutType type = null;
            Object selected = ((IStructuredSelection) layoutType.
                    getSelection()).getFirstElement();
            if (selected instanceof LayoutType) {
                type = (LayoutType) selected;
            }

            Layout layout = null;
            if (type == LayoutType.CANVAS) {
                layout = new CanvasLayout();
            } else if (type == LayoutType.MONTAGE) {
                layout = new MontageLayout(null);
            }

            if (type == null) {
                variant.setContentBuilder(null);
                variant.setVariantType(VariantType.NULL);
            } else {
                content.setLayout(layout);
                variant.setContentBuilder(content);
            }
        }
    }

    // Javadoc inherited
    public boolean canFlipToNextPage() {
        boolean typeSelected =
                ((IStructuredSelection) layoutType.getSelection()).size() == 1;
        return typeSelected && super.canFlipToNextPage();
    }

    // Javadoc inherited
    public boolean isPageComplete() {
        boolean typeSelected =
                ((IStructuredSelection) layoutType.getSelection()).size() == 1;
        return typeSelected && super.isPageComplete();
    }

    // Javadoc inherited
    public void createControl(Composite parent) {
        super.createControl(parent);
        Label label = new Label(topLevel, SWT.NONE);
        label.setText(VARIANT_TYPE_LABEL);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        layoutType = new ComboViewer(topLevel, SWT.READ_ONLY);
        layoutType.getControl().setLayoutData(
                new GridData(GridData.FILL_HORIZONTAL));

        layoutType.setContentProvider(new ArrayContentProvider());
        layoutType.setLabelProvider(new LayoutTypeLabelProvider());
        layoutType.setInput(new Object[] {
            LayoutType.CANVAS, LayoutType.MONTAGE, NULL_LAYOUT });

        layoutType.addSelectionChangedListener(updateButtonsListener);
        layoutType.getCombo().select(0);
    }

    /**
     * Label provider for labeling the various layout types.
     */
    private static class LayoutTypeLabelProvider extends LabelProvider {
        /**
         * The human-readable string for canvas layouts.
         */
        private static String LAYOUT_CANVAS =
                WizardMessages.getString("LayoutType.canvas.name");

        /**
         * The human-readable string for montage layouts.
         */
        private static String LAYOUT_MONTAGE =
                WizardMessages.getString("LayoutType.montage.name");

        /**
         * The human-readable string for montage layouts.
         */
        private static String LAYOUT_NULL =
                WizardMessages.getString("LayoutType.null.name");

        // Javadoc inherited
        public String getText(Object o) {
            String label = "";
            if (LayoutType.CANVAS == o) {
                label = LAYOUT_CANVAS;
            } else if (LayoutType.MONTAGE == o) {
                label = LAYOUT_MONTAGE;
            } else {
                label = LAYOUT_NULL;
            }

            return label;
        }
    }
}
