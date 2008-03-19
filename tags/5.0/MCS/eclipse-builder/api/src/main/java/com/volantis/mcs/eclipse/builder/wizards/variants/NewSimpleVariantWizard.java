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

import com.volantis.mcs.eclipse.builder.editors.common.EditorContext;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.policies.variants.VariantType;

/**
 * A simple new variant wizard for variants that consist solely of a selection
 * criteria page (and the optional targeted selection page).
 */
public class NewSimpleVariantWizard extends NewVariantWizard {
    private VariantType type;

    /**
     * Construct a new variant wizard instance.
     *
     * @param variantList The list proxy to which the newly created variant
     *                    will be added
     * @param context The editor context for this wizard
     */
    public NewSimpleVariantWizard(VariantType type, String variantTypeName,
                                  ListProxy variantList, EditorContext context) {
        super(variantTypeName, variantList, context);
        this.type = type;
        this.variantTypeName = variantTypeName;
    }

    // Javadoc inherited
    protected VariantWizardPage[] getWizardPages() {
        VariantWizardPage[] pages = {
            new VariantSelectionCriteriaPage(type, variantTypeName),
            new TargetSelectionPage(getContext().getProject(),
                getCategories()),
        };
        return pages;
    }
}
