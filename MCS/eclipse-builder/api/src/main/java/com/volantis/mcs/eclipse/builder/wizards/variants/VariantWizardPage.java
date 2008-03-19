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

import org.eclipse.jface.wizard.IWizardPage;

import java.util.List;

/**
 * Interface for wizard pages that act on a policy variant.
 */
public interface VariantWizardPage extends IWizardPage {
    /**
     * Carries out the appropriate operation on the variant(s) in the provided
     * list. If necessary, should first create the variant(s).
     *
     * @param variants A list of variants to operate on
     */
    public void performFinish(List variants);
}
