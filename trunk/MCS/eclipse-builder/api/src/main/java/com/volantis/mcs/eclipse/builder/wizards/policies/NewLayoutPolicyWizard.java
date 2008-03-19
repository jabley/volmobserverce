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

import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.eclipse.builder.wizards.variants.VariantWizardPage;
import com.volantis.mcs.eclipse.builder.wizards.variants.LayoutVariantSelectionCriteriaPage;
import com.volantis.mcs.eclipse.builder.wizards.variants.TargetSelectionPage;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.IProject;

/**
 * Wizard for creating a new layout policy.
 */
public class NewLayoutPolicyWizard extends NewVariablePolicyWizard {
    protected VariantWizardPage[] getVariantWizardPages() {
    	IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject();
        VariantWizardPage[] pages = {
            new LayoutVariantSelectionCriteriaPage(),
            new TargetSelectionPage(project, null)
        };
        return pages;
    }

    // Javadoc inherited
    protected VariantType getVariantType() {
        return VariantType.LAYOUT;
    }

    // Javadoc inherited
    protected String getVariantTypeName() {
        return "layout";
    }

    // Javadoc inherited
    protected PolicyType getPolicyType() {
        return PolicyType.LAYOUT;
    }
}
