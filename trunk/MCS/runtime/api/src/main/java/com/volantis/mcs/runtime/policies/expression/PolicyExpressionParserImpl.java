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

package com.volantis.mcs.runtime.policies.expression;

import com.volantis.mcs.context.BaseURLProvider;
import com.volantis.mcs.context.BrandNameProvider;
import com.volantis.mcs.context.CurrentProjectProvider;
import com.volantis.mcs.objects.PolicyIdentity;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionParser;

public class PolicyExpressionParserImpl
        implements PolicyExpressionParser {

    public final static String validComponenNameChars =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_";

    private final CurrentProjectProvider projectProvider;

    private final BaseURLProvider baseURLProvider;

    private final BrandNameProvider brandNameProvider;

    private ExpressionParser expressionParser;

    public PolicyExpressionParserImpl(
            CurrentProjectProvider projectProvider,
            BaseURLProvider baseURLProvider,
            BrandNameProvider brandNameProvider) {
        
        this.projectProvider = projectProvider;
        this.baseURLProvider = baseURLProvider;
        this.brandNameProvider = brandNameProvider;
    }

    /**
     * Is this a policy name.
     */
    public static boolean isPolicyName(String name) {
        final int nameLength = name.length();
        boolean valid;
        // We cannot convert the name to lower case as we must avoid creating
        // any unnecessary objects. Instead we explicitly test the characters
        // that we are interested in.
        // TODO: refactor with cut and paste code from RemoteRepositoryHelper?
        if (nameLength > 7 && name.charAt(4) == ':'
                && name.charAt(5) == '/'
                && name.charAt(6) == '/'
                && Character.toLowerCase(name.charAt(0)) == 'h'
                && Character.toLowerCase(name.charAt(1)) == 't'
                && Character.toLowerCase(name.charAt(2)) == 't'
                && Character.toLowerCase(name.charAt(3)) == 'p') {
            // It is a url in the form of http://.... which is valid
            valid = true;
        } else if (nameLength > 1 && name.charAt(0) == '/') {
            // It is a url int the form of /blah.... which is valid
            valid = true;
        } else {
            // Assume it is valid unless we find an invalid character.
            valid = true;
            for (int n = 0; valid && n < nameLength; n++) {
                if (validComponenNameChars.indexOf(name.charAt(n)) == -1) {
                    if (name.charAt(n) != '.' && name.charAt(n) != '/') {
                        // Not a valid component name
                        valid = false;
                    }
                }
            }
        }
        // If we got her it is not a vlaid component name
        return valid;
    }

    public RuntimePolicyReferenceExpression parseUnquotedExpression(String unquotedExpression) {
        ExpressionParser parser = getExpressionParser();

        Expression expression;
        try {
            expression = parser.parse(unquotedExpression);
        } catch (ExpressionException e) {
            throw new ExtendedRuntimeException(e);
        }

        RuntimeProject project = projectProvider.getCurrentProject();
        String brandName = getCurrentBrandName();
        return new RuntimePolicyReferenceExpression(project,
                baseURLProvider.getBaseURL(), brandName, expression,
                unquotedExpression);
    }

    public RuntimePolicyReferenceExpression parsePolicyOrUnquotedExpression(
            String expressionAsString, PolicyType expectedPolicyType) {

        if (PolicyExpressionParserImpl.isPolicyName(expressionAsString)) {
            Project project = projectProvider.getCurrentProject();
            PolicyIdentity identity = new PolicyIdentity(project,
                    expressionAsString, expectedPolicyType);
            IdentityValueExpression valueExpression =
                    new IdentityValueExpression(identity);
            String brandName = getCurrentBrandName();
            return new RuntimePolicyReferenceExpression(project,
                    baseURLProvider.getBaseURL(),
                    brandName, valueExpression, expressionAsString);
        } else {
            return parseUnquotedExpression(expressionAsString);
        }
    }

    private ExpressionParser getExpressionParser() {
        if (expressionParser == null) {
            ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
            expressionParser = factory.createExpressionParser();
        }

        return expressionParser;
    }

    private String getCurrentBrandName() {
        return brandNameProvider == null ? null :
                brandNameProvider.getCurrentBrandName();
    }
}
