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
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.expression.functions;

import com.volantis.mcs.expression.RepositoryObjectIdentityValueImpl;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.objects.PolicyIdentity;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.functions.AbstractFunction;

/**
 * Base for all functions that handle policy names.
 */
public abstract class AbstractPolicyFunction
        extends AbstractFunction {

    /**
     * Create an identity value.
     *
     * @param context The context within which the function is running.
     * @param project The project.
     * @param policyName The policy name.
     *
     * @return The value.
     */
    protected Value createIdentityValue(
            ExpressionContext context, Project project, String policyName) {

        FileExtension extension =
                FileExtension.getFileExtensionForLocalPolicy(policyName);
        PolicyType policyType = null;
        if (extension != null) {
            policyType = extension.getPolicyType();

        }
        if (policyType == null) {
            throw new IllegalArgumentException(
                    "Cannot determine policy type for " + policyName +
                    ", file extension is not recognized");
        }

        PolicyIdentity identity =
                new PolicyIdentity(project, policyName, policyType);

        return new RepositoryObjectIdentityValueImpl(
                context.getFactory(), identity);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 ===========================================================================
*/
