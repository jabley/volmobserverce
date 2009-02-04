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

package com.volantis.styling.unit.engine.matchers;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.styling.impl.engine.matchers.DefaultNamespaceMatcher;
import com.volantis.styling.impl.engine.matchers.MatcherContextMock;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.mcs.xdime.XDIMESchemata;

/**
 * Test cases for {@link DefaultNamespaceMatcher}.
 */
public class DefaultNamespaceMatcherTestCase
        extends TestCaseAbstract {

    /**
     * Test that it matches those namespaces that are treated as default but
     * not others.
     */
    public void testMatchesDefaultNamespaces() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MatcherContextMock matcherContextMock =
                new MatcherContextMock("matcherContextMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        matcherContextMock.expects.hasDirectRelationship().returns(true).any();


        // =====================================================================
        //   Test Expectations
        // =====================================================================
        DefaultNamespaceMatcher matcher = new DefaultNamespaceMatcher();
        MatcherResult result;

        // CDM namespace should match.
        matcherContextMock.expects.getNamespace()
                .returns(XDIMESchemata.CDM_NAMESPACE);
        result = matcher.matches(matcherContextMock);
        assertEquals(result, MatcherResult.MATCHED);

        // XHTML 2 namespace should match.
        matcherContextMock.expects.getNamespace()
                .returns(XDIMESchemata.XHTML2_NAMESPACE);
        result = matcher.matches(matcherContextMock);
        assertEquals(result, MatcherResult.MATCHED);

        // XForms namespace should not match.
        matcherContextMock.expects.getNamespace()
                .returns(XDIMESchemata.XFORMS_NAMESPACE);
        result = matcher.matches(matcherContextMock);
        assertEquals(result, MatcherResult.FAILED);
    }

}
