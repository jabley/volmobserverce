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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.policy;

import com.volantis.mcs.context.CurrentProjectProvider;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.expression.functions.AbstractPolicyFunction;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;

/**
 * This function maps to the getUnbrandedPolicy expression. Invocation of
 * this function will produce a RepositoryObjectIdentityValue whose
 * RepositoryObjectIdentity is has an unbranded policy name. The policy
 * name is expected as only argument on the expression. The unbranded name
 * is the policy name prefixed with a ^ character.
 */
public class GetUnbrandedPolicyNameFunction
        extends AbstractPolicyFunction {

    /**
     * Provide the name of the function associated with this
     * class. Note that this name differs from the class name because
     * although the function really does provide the name of an unbranded
     * policy as far as creator of the expression is concerned they are
     * using the getUnbrandedPolicy function.
     * @return the name of the function associated with this class.
     */
    protected String getFunctionName() {
        return "getUnbrandedPolicy";
    }

    /**
     * Return a RepositoryObjectIdentity that represents the policy
     * named in the first and only Value and whose name is an unbranded
     * representation of that policy i.e. a ^ will be prepended to the
     * name.
     * @param context the ExpressionContext
     * @param arguments the arguments to the expression. There should be
     * only a single argument which is the name of a policy.
     * @return the RepositoryObjectIdentityValue whose identity name is the
     * unbranded name of the policy name provided in the arguments
     * @throws ExpressionException
     */
    public Value invoke(ExpressionContext context,
                        Value[] arguments)
            throws ExpressionException {

        assertArgumentCount(arguments, 1);

        // Extract the current project from the expression context.
        // This must be set at the time the expression is about to be evaluated
        // as either the MRC current project (for XDIME/PAPI) or the related
        // owning policy project (for themes/layouts containing expressions)
        CurrentProjectProvider projectProvider = (CurrentProjectProvider)
                context.getProperty(CurrentProjectProvider.class);
        Project currentProject = projectProvider.getCurrentProject();
        if (currentProject == null) {
            // This should never happen.
            throw new IllegalStateException("No current project found to " +
                    "resolve against.");
        }

        // retrieve the policy name
        String policyName = arguments[0].stringValue().asJavaString();

        // create an identity value prepending the ^ character to prevent
        // branding
        return createIdentityValue(context, currentProject, "^" + policyName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 17-Feb-05	6957/2	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Aug-04	5181/3	allan	VBM:2004081106 Support branding post MCS 3.0.

 ===========================================================================
*/
