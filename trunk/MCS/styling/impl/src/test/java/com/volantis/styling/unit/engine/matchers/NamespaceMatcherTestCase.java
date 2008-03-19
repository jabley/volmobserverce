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

import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherContextMock;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.styling.impl.engine.matchers.NamespaceMatcher;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the {@link com.volantis.styling.impl.engine.matchers.NamespaceMatcher}.
 */
public class NamespaceMatcherTestCase
        extends TestCaseAbstract {

    private ExpectationBuilder expectations;
    private MatcherContextMock matcherContextMock;

    private void setupNamespaceFixture(String namespace) {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a sequence of shared expectations.
        expectations = mockFactory.createOrderedBuilder();

        // Create a mock matcher context.
        matcherContextMock = new MatcherContextMock(
                "matcherContext", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        matcherContextMock.expects.hasDirectRelationship().returns(true).any();

        matcherContextMock.expects.getNamespace().returns(namespace);
    }

    /**
     * Test that a null is not allowed.
     */
    public void testNullFails() {
        try {
            new NamespaceMatcher(null);
            fail("Null namespace not detected");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message incorrect",
                         "namespace cannot be null",
                         e.getMessage());
        }
    }

    /**
     * Test that it matches the current elements local name.
     */
    public void testMatches() {

        setupNamespaceFixture("xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Matcher matcher = new NamespaceMatcher("xyz");
        MatcherResult actual = matcher.matches(matcherContextMock);
        assertEquals("Wrong result", MatcherResult.MATCHED, actual);
    }

    /**
     * Test that it matches the current elements local name.
     */
    public void testDoesntMatch() {

        setupNamespaceFixture("xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Matcher matcher = new NamespaceMatcher("abc");
        MatcherResult actual = matcher.matches(matcherContextMock);
        assertEquals("Wrong result", MatcherResult.FAILED, actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/