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

import com.volantis.mcs.policies.PolicyType;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.MockFactory;

public class PolicyReferenceResolverTestHelper {

    public static PolicyReferenceResolverMock getCommonExpectations(
            final ExpectationBuilder expectations,
            final MockFactory mockFactory) {

        final PolicyReferenceResolverMock referenceResolverMock =
                new PolicyReferenceResolverMock("referenceResolverMock",
                        expectations);

        final RuntimePolicyReferenceMock rolloverReferenceMock =
                new RuntimePolicyReferenceMock("rolloverReferenceMock",
                        expectations);

        referenceResolverMock.expects
                .resolveUnquotedPolicyExpression("rollover", PolicyType.ROLLOVER_IMAGE)
                .returns(rolloverReferenceMock).any();

        referenceResolverMock.fuzzy
                .resolveQuotedTextExpression(mockFactory.expectsAny())
                .does(new ReturnLiteralTextReference())
                .any();

        referenceResolverMock.fuzzy
                .resolveQuotedTextExpressionAsStyleValue(mockFactory.expectsAny())
                .does(new ReturnStyleStringValue())
                .any();

        referenceResolverMock.fuzzy
                .resolveQuotedLinkExpression(mockFactory.expectsAny(),
                        mockFactory.expectsAny())
                .does(new ReturnLiteralLinkReference())
                .any();

        return referenceResolverMock;
    }
}
