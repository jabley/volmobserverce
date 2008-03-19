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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.styling.unit.engine.matchers;

import com.volantis.styling.impl.engine.matchers.InlineStyleMatcher;
import com.volantis.styling.impl.engine.matchers.MatcherContextMock;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the InlineStyleMatcher
 */
public class InlineStyleMatcherTestCase extends TestCaseAbstract {

    /**
     * Mock objects required
     */
    private ExpectationBuilder expectations;    

    //setup
    protected void setUp() throws Exception {
        super.setUp();

        // Create a sequence of shared expectations.
        expectations = mockFactory.createOrderedBuilder();
    }

    /**
     * Test the InlineStyleMatcher matches on elements with the same id.
     */
    public void testInlineStyleMatcherMatches() {
        MatcherContextMock matcherContextMock = new MatcherContextMock(
                "MatcherContext", expectations);

        matcherContextMock.expects.getElementId().returns(10);

        InlineStyleMatcher matcher = new InlineStyleMatcher(10);
        MatcherResult matcherResult =
                matcher.matchesWithinContext(matcherContextMock);

        assertEquals("MatcherResult should be MATCHED",
                MatcherResult.MATCHED, matcherResult);
    }

    /**
     * Test the InlineStyleMatcher fails to match on elements with different
     * ids.
     */
    public void testInlineStyleMatcherFails() {
        MatcherContextMock matcherContextMock = new MatcherContextMock(
                "MatcherContext", expectations);

        matcherContextMock.expects.getElementId().returns(1);

        InlineStyleMatcher matcher = new InlineStyleMatcher(10);
        MatcherResult matcherResult =
                matcher.matchesWithinContext(matcherContextMock);

        assertEquals("MatcherResult should be FAILED",
                MatcherResult.FAILED, matcherResult);
    }
}
