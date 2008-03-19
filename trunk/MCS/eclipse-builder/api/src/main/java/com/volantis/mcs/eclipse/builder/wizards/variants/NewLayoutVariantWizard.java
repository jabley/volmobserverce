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

/**
 * New variant wizard for layouts.
 */
public class NewLayoutVariantWizard extends NewVariantWizard {
    /**
     * Construct a new variant wizard instance.
     *
     * @param variantList The list proxy to which the newly created variant
     *                    will be added
     * @param context The editor context for this wizard
     */
    public NewLayoutVariantWizard(ListProxy variantList, EditorContext context) {
        super("layout", variantList, context);
    }

    // Javadoc inherited
    protected VariantWizardPage[] getWizardPages() {
        VariantWizardPage[] pages = {
            new LayoutVariantSelectionCriteriaPage(),
            new TargetSelectionPage(getContext().getProject(),
                getCategories()),
        };
        return pages;
    }
}
