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

import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.PolicyBuilder;

/**
 * Wizard for creating a new rollover image policy.
 */
public class NewRolloverImagePolicyWizard extends NewPolicyWizard {
    // Javadoc inherited
    protected PolicyType getPolicyType() {
        return PolicyType.ROLLOVER_IMAGE;
    }

    // Javadoc inherited
    protected PolicyBuilder getPolicyBuilder() {
        return getPolicyFactory().createRolloverImagePolicyBuilder();
    }

    // Javadoc inherited
    protected PolicyWizardPage[] getPolicyWizardPages() {
        return new PolicyWizardPage[0];
    }

    protected String getPolicyTypeName() {
        return "rolloverImage";
    }
}
