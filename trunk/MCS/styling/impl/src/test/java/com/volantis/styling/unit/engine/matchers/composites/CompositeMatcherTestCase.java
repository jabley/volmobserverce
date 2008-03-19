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

package com.volantis.styling.unit.engine.matchers.composites;

import com.volantis.styling.impl.engine.matchers.MatcherBuilderContextMock;
import com.volantis.styling.impl.engine.matchers.MatcherContextMock;
import com.volantis.styling.impl.engine.matchers.MatcherMock;
import com.volantis.styling.impl.engine.matchers.MatcherResult;
import com.volantis.styling.impl.engine.matchers.composites.CompositeMatcher;
import com.volantis.styling.impl.engine.matchers.composites.CompositeStateFactoryMock;
import com.volantis.styling.impl.engine.matchers.composites.CompositeStateMock;
import com.volantis.styling.impl.engine.matchers.composites.DepthChangeListener;
import com.volantis.styling.impl.state.StateIdentifierMock;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.value.ExpectedValue;

public class CompositeMatcherTestCase
        extends TestCaseAbstract {

    private MatcherContextMock matcherContextMock;
    private MatcherMock contextualMock;
    private MatcherMock subjectMock;
    private MatcherBuilderContextMock builderContextMock;
    private StateIdentifierMock identifierMock;
    private CompositeStateMock stateMock;
    private CompositeStateFactoryMock compositeStateFactoryMock;

    private void setupFixture() {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a mock matcher context.
        matcherContextMock = new MatcherContextMock(
                "matcherContext", expectations);

        // Create a couple of mock matchers.
        contextualMock = new MatcherMock("contextual", expectations);

        subjectMock = new MatcherMock("subject", expectations);

        builderContextMock = new MatcherBuilderContextMock(
                "builderContext", expectations);

        identifierMock = new StateIdentifierMock("identifier", expectations);

        stateMock = new CompositeStateMock("state", expectations);

        compositeStateFactoryMock = new CompositeStateFactoryMock(
                "compositeStateFactoryMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        builderContextMock.expects.register(compositeStateFactoryMock)
                .returns(identifierMock);

        ExpectedValue anyDepthChangeListener =
            MockFactory.getDefaultInstance().
            expectsInstanceOf(DepthChangeListener.class);
        builderContextMock.fuzzy.addDepthChangeListener(anyDepthChangeListener);

        matcherContextMock.expects
                .getState(identifierMock)
                .returns(stateMock).any();

        compositeStateFactoryMock.expects
                .getOperator().returns(" DEBUG ")
                .any();
    }

    private void endElement(CompositeMatcher matcher) {
        matcher.afterEndElement(matcherContextMock);
    }

    private void startElement(CompositeMatcher matcher,
                              MatcherResult expectedResult) {

        matcher.beforeStartElement(matcherContextMock);
        MatcherResult actual = matcher.matches(matcherContextMock);
        assertEquals("Wrong result", expectedResult, actual);
    }

    private CompositeMatcher createCompositeMatcher() {
        CompositeMatcher matcher = new CompositeMatcher(
                contextualMock, subjectMock, compositeStateFactoryMock,
                builderContextMock);
        return matcher;
    }

    /**
     * Test that it updates the context matched state when the context matched.
     */
    public void testUpdatesContextMatchedWhenContextMatched() {

        setupFixture();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                stateMock.expects.beforeStartElement();

                stateMock.expects.hasDirectRelationship().returns(false);
                stateMock.expects.hasPotentialIndirectRelationship()
                        .returns(false);

                contextualMock.expects.matches(matcherContextMock)
                        .returns(MatcherResult.MATCHED);

                stateMock.expects.contextMatched();

                stateMock.expects.afterEndElement();
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CompositeMatcher matcher = createCompositeMatcher();

        // Match against "a".
        startElement(matcher, MatcherResult.FAILED);

        // End "a".
        endElement(matcher);
    }

    /**
     * Test that it does not update the context matched state if the context
     * matcher did not match and returns the correct result.
     */
    public void testDoesNotUpdateContextMatchedStateWhenContextDidNotMatch() {
        doDoesNotUpdateContextMatched(MatcherResult.FAILED);
        doDoesNotUpdateContextMatched(MatcherResult.DEFERRED);
    }

    private void doDoesNotUpdateContextMatched(
            final MatcherResult contextMatcherResult) {

        setupFixture();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                stateMock.expects.beforeStartElement();

                stateMock.expects.hasDirectRelationship().returns(false);
                stateMock.expects.hasPotentialIndirectRelationship()
                        .returns(false);

                contextualMock.expects.matches(matcherContextMock)
                        .returns(contextMatcherResult);

                stateMock.expects.afterEndElement();
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CompositeMatcher matcher = createCompositeMatcher();

        // Match against "a". This will always fail as the context matcher did
        // not match.
        startElement(matcher, MatcherResult.FAILED);

        // End "a".
        endElement(matcher);
    }

    /**
     * Test that it tries the subject matcher if the context matched and
     * returns the result from the subject matcher.
     */
    public void testTriesSubjectMatchedIfContextMatched() {
        doUsesSubjectMatcherIfRelationship(MatcherResult.FAILED);
        doUsesSubjectMatcherIfRelationship(MatcherResult.MATCHED);
        doUsesSubjectMatcherIfRelationship(MatcherResult.DEFERRED);
    }

    /**
     * Test that subject matcher is invoked if the context matched.
     *
     * @param expectedSubjectResult The result of the subject matcher which is
     * expected to be returned.

     */
    private void doUsesSubjectMatcherIfRelationship(
            final MatcherResult expectedSubjectResult) {

        setupFixture();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                stateMock.expects.beforeStartElement();

                stateMock.expects.hasDirectRelationship().returns(true);

                matcherContextMock.expects.changeDirectRelationship(true)
                        .returns(false);

                subjectMock.expects.matches(matcherContextMock)
                        .returns(expectedSubjectResult);

                matcherContextMock.expects.changeDirectRelationship(false)
                        .returns(true);

                stateMock.expects.supportsIndirectRelationships()
                        .returns(false);

                stateMock.expects.afterEndElement();
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        CompositeMatcher matcher = createCompositeMatcher();

        // Match against "a".
        startElement(matcher, expectedSubjectResult);

        // End "a".
        endElement(matcher);
    }

    // todo add some more tests to cover the different sorts of relationships.
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	9029/4	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
