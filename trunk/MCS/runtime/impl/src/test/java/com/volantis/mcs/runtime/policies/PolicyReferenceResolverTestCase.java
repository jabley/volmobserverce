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

import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.runtime.policies.expression.PolicyExpressionParserMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContextMock;

public class PolicyReferenceResolverTestCase
        extends TestCaseAbstract {

    private ExpressionContextMock expressionContextMock;
    private AssetResolverMock assetResolverMock;
    private PolicyReferenceResolver resolver;
    private PolicyExpressionParserMock expressionParserMock;

    protected void setUp() throws Exception {
        super.setUp();

        expressionContextMock = new ExpressionContextMock(
                "expressionContextMock", expectations);

        assetResolverMock = new AssetResolverMock("assetResolverMock",
                expectations);

        expressionParserMock = new PolicyExpressionParserMock(
                "expressionParserMock", expectations);

        resolver = new PolicyReferenceResolverImpl(expressionContextMock,
                assetResolverMock, null, expressionParserMock, null);
    }


    public void testResolveLiteralScript() {
        ScriptAssetReference reference =
                resolver.resolveQuotedScriptExpression("script");
        assertEquals("Script literal", "script", reference.getScript());
    }

    public void testResolveURLScript() {
        ScriptAssetReference reference =
                resolver.resolveQuotedScriptExpression("script");
        assertEquals("Script literal", "script", reference.getScript());
    }
}
