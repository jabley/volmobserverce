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
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleComponentURI;

/**
 * @mock.generate
 */
public interface PolicyReferenceResolver {
    /**
     * @param policyExpression
     * @return
     */
    RuntimePolicyReference resolvePolicyExpression(
            PolicyExpression policyExpression);

    RuntimePolicyReference resolveUnquotedPolicyExpression(
            String expressionAsString, PolicyType policyType);

    LinkAssetReference resolveQuotedLinkExpression(
            String expression, PageURLType urlType);

    ScriptAssetReference resolveQuotedScriptExpression(
            String expression);

    TextAssetReference resolveQuotedTextExpression(String expression);

    TextAssetReference resolveUnquotedTextExpression(String name);

    /**
     * Resolve the possibly quoted text expression as a style value.
     *
     * @param expression The possibly quoted text expression.
     * @return The style value, may be a {@link StyleString}, or a {@link
     *         StyleComponentURI}.
     */
    StyleValue resolveQuotedTextExpressionAsStyleValue(String expression);
}
