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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.expression.PolicyExpression;
import com.volantis.mcs.expression.RepositoryObjectIdentityValue;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.objects.PolicyIdentity;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.assets.AssetReference;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.components.QuotedReferenceFactory;
import com.volantis.mcs.runtime.policies.expression.PolicyExpressionParser;
import com.volantis.mcs.runtime.policies.expression.RuntimePolicyReferenceExpression;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;

/**
 * Provides facilities to resolve a component expression into an identity.
 */
public class PolicyReferenceResolverImpl implements PolicyReferenceResolver {


    /**
     * The expression context within which the expression will be evaluated.
     */
    private final ExpressionContext expressionContext;

    private final AssetResolver assetResolver;

    /**
     * The parser.
     */
    private final PolicyExpressionParser expressionParser;
    private final PolicyReferenceBrander brander;

    private final PolicyReferenceFactory referenceFactory;

    /**
     * Initialise.
     */
    public PolicyReferenceResolverImpl(
            ExpressionContext expressionContext,
            AssetResolver assetResolver,
            PolicyReferenceFactory referenceFactory,
            PolicyExpressionParser expressionParser,
            PolicyReferenceBrander brander) {

        this.expressionContext = expressionContext;
        this.assetResolver = assetResolver;
        this.referenceFactory = referenceFactory;
        this.expressionParser = expressionParser;
        this.brander = brander;
    }

    public RuntimePolicyReference resolvePolicyExpression(
            PolicyExpression policyExpression) {

        RuntimePolicyReferenceExpression runtimePolicyExpression;
        if (policyExpression instanceof RuntimePolicyReferenceExpression) {
            runtimePolicyExpression =
                    (RuntimePolicyReferenceExpression) policyExpression;

        } else {
            String expressionAsString = policyExpression.getAsString();
            runtimePolicyExpression = expressionParser
                    .parsePolicyOrUnquotedExpression(
                            expressionAsString, null);
        }

        return evaluateRuntimePolicyExpression(runtimePolicyExpression);

    }

    private RuntimePolicyReference evaluateRuntimePolicyExpression(
            RuntimePolicyReferenceExpression policyExpression) {
        Expression expression = policyExpression.getExpression();
        RuntimeProject project = (RuntimeProject) policyExpression.getProject();
        MarinerURL baseURL = policyExpression.getBaseURL();
        String brandName = policyExpression.getBrandName();

        RuntimePolicyReference reference = null;
        try {
            Value value = expression.evaluate(expressionContext);
            if (value instanceof RepositoryObjectIdentityValue) {
                RepositoryObjectIdentityValue identityValue =
                        (RepositoryObjectIdentityValue) value;

                PolicyIdentity identity = identityValue.asPolicyIdentity();

                reference = referenceFactory.createLazyNormalizedReference(
                        project, baseURL, identity.getName(),
                        identity.getPolicyType());

                if (brandName != null) {
                    reference = brander.getBrandedReference(reference,
                            brandName);
                }
            }
        } catch (ExpressionException e) {
            throw new ExtendedRuntimeException(e);
        }
        return reference;
    }

    public RuntimePolicyReference resolveUnquotedPolicyExpression(
            String expressionAsString, PolicyType policyType) {

        RuntimePolicyReferenceExpression runtimePolicyExpression =
                expressionParser.parsePolicyOrUnquotedExpression(
                        expressionAsString, policyType);

        return resolvePolicyExpression(runtimePolicyExpression);
    }

    public LinkAssetReference resolveQuotedLinkExpression(
            String expression, PageURLType urlType) {

        return (LinkAssetReference) resolveQuotedPolicyExpression(expression,
                QuotedReferenceFactory.LINK_REFERENCE_FACTORY, urlType);
    }

    public ScriptAssetReference resolveQuotedScriptExpression(
            String expression) {
        return (ScriptAssetReference) resolveQuotedPolicyExpression(expression,
                QuotedReferenceFactory.SCRIPT_REFERENCE_FACTORY, null);
    }

    public TextAssetReference resolveQuotedTextExpression(String expression) {
        return (TextAssetReference) resolveQuotedPolicyExpression(expression,
                QuotedReferenceFactory.TEXT_REFERENCE_FACTORY, null);
    }

    private AssetReference resolveQuotedPolicyExpression(
            String possibleExpression, QuotedReferenceFactory factory,
            PageURLType urlType) {

        if (possibleExpression == null) {
            return null;
        }

        AssetReference assetReference;
        if (isPAPIQuotedExpression(possibleExpression)) {
            // strip off the PAPI quoted style braces
            final String unquotedExpression = possibleExpression.substring(1,
                    possibleExpression.length() - 1);

            // Create an identity from the unquoted PAPI policy expression.
            RuntimePolicyReference reference = resolveUnquotedPolicyExpression(
                    unquotedExpression, factory.getPolicyType());

            assetReference = factory.createExpressionReference(reference,
                    assetResolver, urlType);

        } else if (possibleExpression.startsWith("\\{") &&
                possibleExpression.endsWith("\\}")) {

            // strip off the escape characters, remember that \\ actually
            // is just \ escaped

            String literal = possibleExpression.substring(1,
                    possibleExpression.length() - 2) + "}";
            if (urlType != null) {
                literal = assetResolver.rewriteURLWithPageURLRewriter(
                        literal, urlType);
            }
            assetReference = factory.createLiteralReference(literal);

        } else {
            // The String provided is not an PAPI quoted expression
            String literal = possibleExpression;
            if (urlType != null) {
                literal = assetResolver.rewriteURLWithPageURLRewriter(
                        literal, urlType);
            }
            assetReference =
                    factory.createLiteralReference(possibleExpression);
        }

        return assetReference;

    }

    /**
     * Check to see if an expression appears to be a quoted MCS PAPI style
     * expression.
     * <p>
     * See ANO25 for the related definitions.
     *
     * @return true if the given string appers to be a quoted MCS PAPI style
     *         expression; false otherwise.
     */
    public static boolean isPAPIQuotedExpression(String expression) {
        return expression.startsWith("{") && expression.endsWith("}");
    }

    public TextAssetReference resolveUnquotedTextExpression(String name) {

        QuotedReferenceFactory factory = QuotedReferenceFactory.TEXT_REFERENCE_FACTORY;

        // Create an identity from the unquoted PAPI policy expression.
        RuntimePolicyReference reference = resolveUnquotedPolicyExpression(
                name, factory.getPolicyType());

        return (TextAssetReference)
                factory.createExpressionReference(reference,
                        assetResolver, null);
    }

    // Javadoc inherited.
    public StyleValue resolveQuotedTextExpressionAsStyleValue(
            String possibleExpression) {

        if (possibleExpression == null) {
            return null;
        }

        StyleValueFactory factory = StyleValueFactory.getDefaultInstance();


        StyleValue styleValue;
        if (isPAPIQuotedExpression(possibleExpression)) {
            // strip off the PAPI quoted style braces
            final String unquotedExpression = possibleExpression.substring(1,
                    possibleExpression.length() - 1);

            styleValue = factory.getComponentURI(null, unquotedExpression);

        } else if (possibleExpression.startsWith("\\{") &&
                possibleExpression.endsWith("\\}")) {

            // strip off the escape characters, remember that \\ actually
            // is just \ escaped

            String literal = possibleExpression.substring(1,
                    possibleExpression.length() - 2) + "}";
            styleValue = factory.getString(null, literal);

        } else {
            // The String provided is not an PAPI quoted expression
            String literal = possibleExpression;

            styleValue = factory.getString(null, literal);
        }

        return styleValue;
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
