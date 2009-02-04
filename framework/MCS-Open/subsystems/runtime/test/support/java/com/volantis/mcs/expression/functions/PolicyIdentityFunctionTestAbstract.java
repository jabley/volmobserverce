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

import com.volantis.mcs.application.MarinerApplicationMock;
import com.volantis.mcs.context.CurrentProjectProvider;
import com.volantis.mcs.context.CurrentProjectProviderMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.expression.RepositoryObjectIdentityValue;
import com.volantis.mcs.objects.PolicyIdentity;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContextMock;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactoryMock;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;

public abstract class PolicyIdentityFunctionTestAbstract
        extends TestCaseAbstract {

    protected static final String AUDIO_COMPONENT_NAME =
            "AUDIO COMPONENT NAME.mauc";

    protected ExpressionContextMock expressionContextMock;
    protected ExpressionFactoryMock expressionFactoryMock;
    protected MarinerRequestContextMock marinerRequestContextMock;
    protected MarinerApplicationMock marinerApplicationMock;
    protected static final String PROJECT_NAME = "PROJECT";
    protected RuntimeProjectMock projectMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        expressionContextMock = new ExpressionContextMock(
                "expressionContextMock", expectations);

        expressionFactoryMock = new ExpressionFactoryMock(
                "expressionFactoryMock", expectations);

        projectMock = new RuntimeProjectMock("projectMock", expectations);

        final CurrentProjectProviderMock projectProviderMock =
                new CurrentProjectProviderMock("projectProviderMock",
                        expectations);

        marinerRequestContextMock = new MarinerRequestContextMock(
                "marinerRequestContextMock", expectations);

        marinerApplicationMock = new MarinerApplicationMock(
                "marinerApplicationMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Initialise the expression context.
        expressionContextMock.expects.getFactory()
                .returns(expressionFactoryMock).any();
        expressionContextMock.expects.getProperty(CurrentProjectProvider.class)
                .returns(projectProviderMock).any();
        projectProviderMock.expects.getCurrentProject()
                .returns(projectMock).any();
        expressionContextMock.expects.getProperty(MarinerRequestContext.class)
                .returns(marinerRequestContextMock).any();

        // Initialise the request context.
        marinerRequestContextMock.expects.getMarinerApplication()
                .returns(marinerApplicationMock).any();

        // Initialise the application.
        marinerApplicationMock.expects.getPredefinedProject(PROJECT_NAME)
                .returns(projectMock).any();
    }

    protected void doTestInvokeCreateIdentity(
            Function function, Value[] arguments,
            PolicyIdentity expectedIdentity)
            throws ExpressionException {

        Value value = function.invoke(expressionContextMock, arguments);

        RepositoryObjectIdentityValue identityValue =
                (RepositoryObjectIdentityValue) value;
        PolicyIdentity actualIdentity =
                identityValue.asPolicyIdentity();
        assertEquals("Identity", expectedIdentity, actualIdentity);
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
