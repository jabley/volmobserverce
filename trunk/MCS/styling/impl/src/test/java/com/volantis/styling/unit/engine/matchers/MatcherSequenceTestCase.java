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
import com.volantis.styling.impl.engine.matchers.MatcherMock;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.styling.impl.engine.matchers.MatcherSequence;
import com.volantis.styling.impl.engine.matchers.SimpleMatcherMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Arrays;

/**
 * Test the {@link com.volantis.styling.impl.engine.matchers.MatcherSequence}.
 */
public class MatcherSequenceTestCase
        extends TestCaseAbstract {

    private ExpectationBuilder expectations;
    private MatcherContextMock matcherContextMock;
    private SimpleMatcherMock matcher1Mock;
    private SimpleMatcherMock matcher2Mock;
    private MatcherSequence sequence;

    private void doSelectorSequenceIteratorTest(
            MatcherResult result1, MatcherResult result2,
            MatcherResult expected) {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a sequence of shared expectations.
        expectations = mockFactory.createOrderedBuilder();

        // Create a mock matcher context.
        matcherContextMock = new MatcherContextMock(
                "matcherContextMock", expectations);

        // Create a couple of mock matchers.
        matcher1Mock = new SimpleMatcherMock(
                "matcher1Mock - returns " + result1, expectations);

        matcher2Mock = new SimpleMatcherMock(
                "matcher2Mock - returns " + result2, expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // Always assume that the selector has a direct relationship with
        // the contextual element.
        matcherContextMock.expects.hasDirectRelationship().returns(true);

        matcher1Mock.expects.matchesWithinContext(matcherContextMock)
                .returns(result1);

        if (result2 != null) {
            matcher2Mock.expects.matchesWithinContext(matcherContextMock)
                    .returns(result2);
        }

        // Create the object to test.
        sequence = new MatcherSequence(Arrays.asList(new Matcher[]{
            matcher1Mock, matcher2Mock}));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        MatcherResult actual = sequence.matches(matcherContextMock);
        assertEquals("Wrong result", expected, actual);
    }

    /**
     * Test that a null is not allowed.
     */
    public void testNullFails() {
        try {
            new MatcherSequence(null);
            fail("Null list not detected");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message incorrect",
                         "list cannot be null",
                         e.getMessage());
        }
    }

    /**
     * Test that it iterates through when the matchers matched.
     */
    public void testMatched() {

        doSelectorSequenceIteratorTest(MatcherResult.MATCHED,
                                       MatcherResult.MATCHED,
                                       MatcherResult.MATCHED);
    }

    /**
     * Test that it stops immediately if deferred.
     */
    public void testDeferred() {

        doSelectorSequenceIteratorTest(MatcherResult.DEFERRED, null,
                                       MatcherResult.DEFERRED);
    }

    /**
     * Test that it stops immediately if failed.
     */
    public void testFailed() {

        doSelectorSequenceIteratorTest(MatcherResult.FAILED, null,
                                       MatcherResult.FAILED);
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
