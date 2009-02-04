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
import com.volantis.styling.impl.engine.matchers.NthChildMatcher;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test the {@link com.volantis.styling.impl.engine.matchers.NthChildMatcher}.
 */
public class NthChildMatcherTestCase
        extends TestCaseAbstract {

    private ExpectationBuilder expectations;
    private MatcherContextMock matcherContextMock;

    /**
     * Test when <code>a = 0</code>.
     */
    public void testNthChildA0() {
        for (int b = 1; b < 10; b++) {
            // Test that when the position is the same as b that it matches
            // and that when the position is not the same as b that it fails.
            doNthChildTest(0, b, new Sequence[]{
                new Sequence(b, b, 1, MatcherResult.MATCHED),
                new Sequence(b + 1, b + 10, 1, MatcherResult.FAILED)
            });
        }
    }

    /**
     * Test when <code>a = 1</code>.
     */
    public void testNthChildA1() {
        for (int b = 1; b < 100; b += 9) {
            // Test that when the position is greater than or equal to b that
            // it matches and that when it is less than b that it fails.
            doNthChildTest(1, b, new Sequence[]{
                new Sequence(b, b + 50, 1, MatcherResult.MATCHED),
                new Sequence(1, b - 1, 1, MatcherResult.FAILED)
            });
        }
    }

    /**
     * Test when <code>a = -1</code>.
     */
    public void testNthChildANegative1() {
        for (int b = 1; b < 100; b += 9) {
            // Test that when the position is less than or equal to b that it
            // matches and that when it is greater than b that it fails.
            doNthChildTest(-1, b, new Sequence[]{
                new Sequence(b, 1, -1, MatcherResult.MATCHED),
                new Sequence(b + 1, b + 50, 1, MatcherResult.FAILED)
            });
        }
    }

    /**
     * Test when <code>a = 2</code>.
     */
    public void testNthChildA2() {
        for (int b = 1; b < 100; b += 9) {
            // Test that when the position is greater than or equal to b and
            // starts at b that it works but when it starts at an offset that
            // is not a multiple of the step (a) that it fails. Also test that
            // it always fails when it is less than b.
            doNthChildTest(2, b, new Sequence[]{
                new Sequence(b, b + 50, 2, MatcherResult.MATCHED),
                new Sequence(b + 1, b + 51, 2, MatcherResult.FAILED),
                new Sequence(1, b - 1, 1, MatcherResult.FAILED)
            });
        }
    }

    /**
     * Test when <code>a = -2</code>.
     */
    public void testNthChildANegative2() {
        for (int b = 1; b < 100; b += 9) {
            // Test that when the position is less than or equal to b and
            // starts at b that it works but when it starts at an offset that
            // is not a multiple of the step (a) that it fails. Also test that
            // it always fails when it is greater than b..

            doNthChildTest(-2, b, new Sequence[]{
                new Sequence(b, 1, -2, MatcherResult.MATCHED),
                new Sequence(b - 1, 1, -2, MatcherResult.FAILED),
                new Sequence(b + 1, b + 50, 1, MatcherResult.FAILED)
            });
        }
    }

    private void doNthChildTest(int a, int b, Sequence[] sequences) {
        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // Create a sequence of shared expectations.
        expectations = mockFactory.createOrderedBuilder();

        // Create a mock matcher context.
        matcherContextMock = new MatcherContextMock(
                "matcherContext", expectations);

        // Loop over the sequences of positions.
        for (int s = 0; s < sequences.length; s++) {
            Sequence sequence = sequences[s];

            // Loop over the positions.
            for (PositionIterator i = sequence.iterator(); i.hasNext();) {
                int p = i.next();

                // =====================================================================
                //   Set Expectations
                // =====================================================================

                matcherContextMock.expects.getPosition().returns(p);

                // =====================================================================
                //   Test Expectations
                // =====================================================================

                Matcher matcher = new NthChildMatcher(a, b);
                MatcherResult actual = matcher.matches(matcherContextMock);
                assertEquals("Wrong result for position " + p +
                             " within sequence " + sequence + " with " +
                             a + "n + " + b,
                             sequence.expectedResult, actual);
            }
        }
    }

    /**
     * An iterator over the positions, methods have the same meaning as for
     * {@link java.util.Iterator}.
     */
    private interface PositionIterator {

        // As for Iterator.
        boolean hasNext();

        // As for Iterator.
        int next();
    }

    /**
     * A sequence of positions to check.
     */
    private static class Sequence {

        /**
         * The start of the sequence.
         */
        private final int start;

        /**
         * The end of the sequence.
         */
        private final int end;

        /**
         * The step to add to a position in the sequence to create the next
         * position in the sequence.
         */
        private final int step;

        /**
         * The result expected for this sequence.
         */
        private final MatcherResult expectedResult;

        /**
         * Initialise.
         *
         * @param start The start of the sequence.
         * @param end The end of the sequence.
         * @param step The step between items in the sequence.
         * @param expectedResult The result.
         */
        public Sequence(int start, int end, int step,
                        MatcherResult expectedResult) {

            this.start = start;
            this.end = end;
            this.expectedResult = expectedResult;
            this.step = step;
        }

        /**
         * Get an iterator over the sequence from the start to the end.
         *
         * @return The iterator.
         */
        PositionIterator iterator() {
            if (step < 0) {
                return new DecreasingIterator(start, end, step);
            } else {
                return new IncreasingIterator(start, end, step);
            }
        }

        /**
         * Generate a debug representation of the sequence.
         *
         * @return Debug representation.
         */
        public String toString() {
            return "(" + start + ", " + end + ", " + step + ")";
        }
    }


    /**
     * Abstract iterator.
     */
    private static abstract class AbstractIterator
            implements PositionIterator {

        /**
         * Position of the next item.
         */
        protected int p;

        /**
         * The end of the sequence.
         */
        protected final int end;

        /**
         * The step between consecutive items in the sequence.
         */
        private final int step;

        /**
         * Initialise
         *
         * @param start The starting value.
         */
        public AbstractIterator(int start, int end, int step) {
            this.p = start;
            this.end = end;
            this.step = step;
        }

        // Javadoc inherited.
        public int next() {
            int position = p;
            p += step;
            return position;
        }
    }

    /**
     * An iterator that handles sequences that decrease.
     */
    private static class DecreasingIterator
            extends AbstractIterator {

        /**
         * Initialise.
         */
        public DecreasingIterator(int start, int end, int step) {
            super(start, end, step);
        }

        // Javadoc inherited.
        public boolean hasNext() {
            return p >= end;
        }

    }

    /**
     * An iterator that handles sequences that increase.
     */
    private static class IncreasingIterator
            extends AbstractIterator {

        /**
         * Initialise.
         */
        public IncreasingIterator(int start, int end, int step) {
            super(start, end, step);
        }

        // Javadoc inherited.
        public boolean hasNext() {
            return p <= end;
        }
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
