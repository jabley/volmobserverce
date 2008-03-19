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

import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.PolicyFactory;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Wizard for creating new variants for a variable policy.
 */
public abstract class NewVariantWizard extends Wizard implements INewWizard {
    private static final String RESOURCE_PREFIX = "NewVariantWizard.";

    private VariantWizardPage[] pages;

    private ListProxy variantList;

    private EditorContext context;

    protected String variantTypeName;

    /**
     * Construct a new variant wizard instance.
     *
     * @param variantList The list proxy to which the newly created variant
     *                    will be added
     */
    public NewVariantWizard(String variantTypeName, ListProxy variantList,
                            EditorContext context) {
        this.variantList = variantList;
        this.variantTypeName = variantTypeName;
        this.context = context;
    }

    protected EditorContext getContext() {
        return context;
    }

    // Javadoc inherited
    public boolean performFinish() {
        boolean finished = false;

        boolean canFinish = true;
        for (int i = 0; canFinish && i < pages.length; i++) {
             canFinish = pages[i].isPageComplete();
        }

        if (canFinish) {
            List variants = new ArrayList();
            for (int i = 0; i < pages.length; i++) {
                pages[i].performFinish(variants);
            }

            Iterator it = variants.iterator();
            while (it.hasNext()) {
                VariantBuilder next = (VariantBuilder) it.next();
                Operation addVariantOperation = variantList.prepareAddModelItemOperation(next);
                context.executeOperation(addVariantOperation);
            }

            finished = true;
        }

        return finished;
    }

    public boolean canFinish() {
        boolean canFinish = true;
        for (int i = 0; canFinish &&  i < pages.length; i++) {
            canFinish = pages[i].isPageComplete();
        }
        return canFinish;
    }

    // Javadoc inherited
    public void init(IWorkbench iWorkbench, IStructuredSelection iStructuredSelection) {
        String imagePath = WizardMessages.getString(RESOURCE_PREFIX + variantTypeName + ".banner");
        ImageDescriptor imageDescriptor = BuilderPlugin.getImageDescriptor(imagePath);
        String title = WizardMessages.getString(RESOURCE_PREFIX + variantTypeName + ".title");
        pages = getWizardPages();
        for (int i = 0; i < pages.length; i++) {
            pages[i].setImageDescriptor(imageDescriptor);
            pages[i].setTitle(title);
            addPage(pages[i]);
        }
    }

    protected List getCategories() {
        PolicyFactory policyFactory = PolicyFactory.getDefaultInstance();

        PolicyEditorContext context = (PolicyEditorContext)getContext();

        List categories = new ArrayList();
        List categoryValues = context.getCategoryValues();
        if (categoryValues != null && !categoryValues.isEmpty()) {
            Iterator it = categoryValues.iterator();
            while (it.hasNext()) {
                String value = (String) it.next();
                CategoryReference categoryRef =
                        policyFactory.createCategoryReference(value);
                categories.add(categoryRef);
            }
        }
        return categories;
    }

    protected abstract VariantWizardPage[] getWizardPages();

    public static NewVariantWizard createNewVariantWizard(VariantType type, ListProxy variantList, EditorContext context) {
        NewVariantWizard wizard = null;
        if (type == VariantType.AUDIO) {
            wizard = new NewSimpleVariantWizard(type, "audio", variantList, context);
        } else if (type == VariantType.CHART) {
            wizard = new NewSimpleVariantWizard(type, "chart", variantList, context);
        } else if (type == VariantType.IMAGE) {
            wizard = new NewImageVariantWizard(variantList, context);
        } else if (type == VariantType.LAYOUT) {
            wizard = new NewLayoutVariantWizard(variantList, context);
        } else if (type == VariantType.LINK) {
            wizard = new NewSimpleVariantWizard(type, "link", variantList, context);
        } else if (type == VariantType.SCRIPT) {
            wizard = new NewSimpleVariantWizard(type, "script", variantList, context);
        } else if (type == VariantType.TEXT) {
            wizard = new NewSimpleVariantWizard(type, "text", variantList, context);
        } else if (type == VariantType.THEME) {
            wizard = new NewSimpleVariantWizard(type, "theme", variantList, context);
        } else if (type == VariantType.VIDEO) {
            wizard = new NewSimpleVariantWizard(type, "video", variantList, context);
        }
        return wizard;
    }
}
