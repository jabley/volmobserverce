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
package com.volantis.mcs.eclipse.builder.wizards.policies;

import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.wizards.WizardMessages;
import com.volantis.mcs.eclipse.builder.wizards.variants.VariantWizardPage;
import com.volantis.mcs.eclipse.builder.wizards.variants.VariantSelectionCriteriaPage;
import com.volantis.mcs.eclipse.builder.wizards.variants.TargetSelectionPage;
import com.volantis.mcs.eclipse.common.ProjectReceiver;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.VariantBuilder;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Wizard for creating a new variable policy. By default this allows the
 * standard creation of a single policy and optionally a single variant within
 * that policy.
 */
public abstract class NewVariablePolicyWizard extends NewPolicyWizard {
    private static final String VARIANT_WIZARD_RESOURCE_PREFIX = "NewVariantWizard.";

    private VariantWizardPage[] variantPages;

    // Javadoc inherited
    protected PolicyBuilder getPolicyBuilder() {
        VariablePolicyType policyType = (VariablePolicyType) getPolicyType();
        return getPolicyFactory().createVariablePolicyBuilder(policyType);
    }

    // Javadoc inherited
    protected void projectChanged(IProject project) {
        super.projectChanged(project);
        for (int i = 0; i < variantPages.length; i++) {
            if (variantPages[i] instanceof ProjectReceiver) {
                ((ProjectReceiver) variantPages[i]).setProject(project);
            }
        }
    }

    /**
     * Returns the pages related to creating variant(s) within the policy. If
     * the policy type has no variants, this should be an empty array.
     *
     * @return An array of pages for creating variants
     */
    protected VariantWizardPage[] getVariantWizardPages() {
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject();
        VariantWizardPage[] pages = {
            new VariantSelectionCriteriaPage(getVariantType(), getVariantTypeName()),
            new TargetSelectionPage(project, null),
        };
        return pages;
    }

    /**
     * Returns the type of variant associated with this variable policy.
     *
     * @return The type of variant associated with this variable policy
     */
    protected abstract VariantType getVariantType();

    /**
     * Returns the name used to reference the type for this variable policy in
     * the resource bundle.
     *
     * @return The name used to reference the type for this variable policy
     */
    protected abstract String getVariantTypeName();

    protected PolicyWizardPage[] getPolicyWizardPages() {
        PolicyWizardPage[] pages = {
        };
        return pages;
    }

    // Javadoc inherited
    protected String getPolicyTypeName() {
        // In most cases the policy and variant names will be the same, so we
        // default to this behaviour.
        return getVariantTypeName();
    }

    // Javadoc inherited
    protected PolicyBuilder getPolicy() {
        VariablePolicyBuilder policy = (VariablePolicyBuilder) super.getPolicy();
        boolean variantReady = true;
        for (int i = 0; variantReady && i < variantPages.length; i++) {
            variantReady = variantPages[i].isPageComplete();
        }

        if (variantReady) {
            List variants = new ArrayList();
            for (int i = 0; i < variantPages.length; i++) {
                variantPages[i].performFinish(variants);
            }
            Iterator it = variants.iterator();
            while (it.hasNext()) {
                policy.addVariantBuilder((VariantBuilder) it.next());
            }
        }

        return policy;
    }

    // Javadoc inherited
    public void init(IWorkbench iWorkbench, IStructuredSelection iStructuredSelection) {
        super.init(iWorkbench, iStructuredSelection);

        String imagePath = WizardMessages.getString(VARIANT_WIZARD_RESOURCE_PREFIX + policyTypeName + ".banner");
        ImageDescriptor imageDescriptor = BuilderPlugin.getImageDescriptor(imagePath);
        String title = WizardMessages.getString(VARIANT_WIZARD_RESOURCE_PREFIX + policyTypeName + ".title");

        variantPages = getVariantWizardPages();
        for (int i = 0; i < variantPages.length; i++) {
            variantPages[i].setImageDescriptor(imageDescriptor);
            variantPages[i].setTitle(title);
            addPage(variantPages[i]);
        }
    }
}
