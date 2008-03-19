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

import com.volantis.styling.impl.engine.matchers.AttributeMatcher;
import com.volantis.styling.impl.engine.matchers.Matcher;
import com.volantis.styling.impl.engine.matchers.MatcherContextMock;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraint;
import com.volantis.styling.impl.engine.matchers.constraints.ValueConstraintMock;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the {@link com.volantis.styling.impl.engine.matchers.AttributeMatcher}.
 */
public class AttributeMatcherTestCase
        extends TestCaseAbstract {

    private ExpectationBuilder expectations;
    private MatcherContextMock matcherContextMock;
    private ValueConstraintMock constraintMock;

    protected void setUp() throws Exception {
        super.setUp();

        // Create a sequence of shared expectations.
        expectations = mockFactory.createOrderedBuilder();
    }

    /**
     * Test that a null local name fails.
     */
    public void testNullLocalNameFails() {
        try {
            ValueConstraint constraint = new ValueConstraintMock(
                    "constraintMock", expectations);
            new AttributeMatcher(null, null, constraint);
            fail("Null localName not detected");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message incorrect",
                         "localName cannot be null",
                         e.getMessage());
        }
    }

    /**
     * Test that a null constraintMock fails.
     */
    public void testNullConstraintFails() {
        try {
            new AttributeMatcher(null, "fred", null);
            fail("Null constraintMock not detected");
        } catch (IllegalArgumentException e) {
            assertEquals("Error message incorrect",
                         "constraint cannot be null",
                         e.getMessage());
        }
    }

    private void setupConstraintFixture(boolean result) {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a mock matcher context.
        matcherContextMock = new MatcherContextMock(
                "matcherContextMock", expectations);

        constraintMock = new ValueConstraintMock(
                "constraintMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        matcherContextMock.expects.hasDirectRelationship().returns(true).any();

        matcherContextMock.expects.getAttributeValue(null, "local")
                .returns("value");

        constraintMock.expects.satisfied("value").returns(result);
    }

    /**
     * Test that it invokes the constraintMock properly and returns the correct
     * result when the constraintMock succeeds.
     */
    public void testMatches() {

        setupConstraintFixture(true);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Matcher matcher = new AttributeMatcher(null, "local", constraintMock);
        MatcherResult actual = matcher.matches(matcherContextMock);
        assertEquals("Wrong result", MatcherResult.MATCHED, actual);
    }

    /**
     * Test that it invokes the constraintMock properly and returns the correct
     * result when the constraintMock fails.
     */
    public void testFailed() {

        setupConstraintFixture(false);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Matcher matcher = new AttributeMatcher(null, "local", constraintMock);
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
