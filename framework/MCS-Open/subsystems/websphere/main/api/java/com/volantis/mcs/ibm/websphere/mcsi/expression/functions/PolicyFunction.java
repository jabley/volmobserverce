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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.ibm.websphere.mcsi.expression.functions;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.functions.AbstractPolicyFunction;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.project.Project;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * The GetPolicyValueFunction ExpressionFunction. Given the name of a device
 * policy for the current device return the appropriate value.
 */
public class PolicyFunction
        extends AbstractPolicyFunction {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                            PolicyFunction.class);

    // javadoc inherited
    protected String getFunctionName() {
        return "policy";
    }

    // javadoc inherited
    public Value invoke(ExpressionContext expressionContext,
                        Value[] values) throws ExpressionException {

        // we expect a project and an asset name.
        assertArgumentCount(values, 2);

        // extract the mariner request context from the expression context
        MarinerRequestContext context =
                (MarinerRequestContext) expressionContext.getProperty(
                        MarinerRequestContext.class);
        MarinerApplication application = context.getMarinerApplication();

        // retrieve the project name
        String projectName = values[0].stringValue().asJavaString();

        // retrieve the asset name
        String componentName = values[1].stringValue().asJavaString();

        Project project = application.getPredefinedProject(projectName);

        if (project == null) {
            throw new ExpressionException(
                    exceptionLocalizer.format("mcsi-policy-project-not-defined",
                                              projectName));
        }

        return createIdentityValue(expressionContext, project,  componentName);
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 12-Aug-04	5181/1	allan	VBM:2004081106 Support branding post MCS 3.0.

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 ===========================================================================
*/
