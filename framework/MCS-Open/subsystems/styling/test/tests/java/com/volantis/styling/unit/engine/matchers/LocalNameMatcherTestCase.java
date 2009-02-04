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

import com.volantis.styling.impl.engine.matchers.LocalNameMatcher;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherContextMock;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the {@link com.volantis.styling.impl.engine.matchers.LocalNameMatcher}.
 */
public class LocalNameMatcherTestCase
        extends TestCaseAbstract {

    private ExpectationBuilder expectations;
    private MatcherContextMock matcherContextMock;

    protected void setUp() throws Exception {
        super.setUp();

        // Create a sequence of shared expectations.
        expectations = mockFactory.createOrderedBuilder();
    }

    private void setupLocalNameFixture(String localName) {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a mock matcher context.
        matcherContextMock = new MatcherContextMock(
                "matcherContextMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        matcherContextMock.expects.hasDirectRelationship().returns(true).any();

        matcherContextMock.expects.getLocalName().returns(localName);
    }

    /**
     * Test that a null is not allowed.
     */
    public void testNullFails() {
        try {
            new LocalNameMatcher(null);
            fail("Null namespace not detected");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message incorrect",
                         "localName cannot be null",
                         e.getMessage());
        }
    }

    /**
     * Test that it matches the current elements local name.
     */
    public void testMatches() {

        setupLocalNameFixture("xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Matcher matcher = new LocalNameMatcher("xyz");
        MatcherResult actual = matcher.matches(matcherContextMock);
        assertEquals("Wrong result", MatcherResult.MATCHED, actual);
    }

    /**
     * Test that it matches the current elements local name.
     */
    public void testDoesntMatch() {

        setupLocalNameFixture("xyz");

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Matcher matcher = new LocalNameMatcher("abc");
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
