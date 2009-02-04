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

package com.volantis.styling.unit.sheet;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.styling.impl.sheet.StylerIterateeMock;
import com.volantis.styling.impl.sheet.StylerMock;
import com.volantis.styling.impl.sheet.Styler;
import com.volantis.styling.impl.sheet.StylerIterator;
import com.volantis.shared.iteration.IterationAction;

/**
 * Base class for testing all implementations of {@link StylerIterator}.
 */
public abstract class StylerIteratorTestAbstract
        extends TestCaseAbstract {
    protected StylerIterateeMock iterateeMock;
    protected StylerMock stylerMock1;
    protected StylerMock stylerMock2;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        iterateeMock = new StylerIterateeMock(
                "iterateeMock", expectations);

        stylerMock1 = new StylerMock("stylerMock1", expectations);
        stylerMock2 = new StylerMock("stylerMock2", expectations);
    }

    /**
     * Create a {@link StylerIterator} to test.
     *
     * @param styler1 The first styler that the iterator contains.
     * @param styler2 The second styler that the iterator contains.
     *
     * @return A {@link StylerIterator}.
     */
    protected abstract StylerIterator createStylerIterator(
            Styler styler1, Styler styler2);

    /**
     * Test that when the first iterator returns break the iteration ends
     * immediately without invoking any other iterators.
     */
    public void testBreak() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                iterateeMock.expects.next(stylerMock1)
                        .returns(IterationAction.BREAK);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        StylerIterator iterator = createStylerIterator(
                stylerMock1, stylerMock2);
        assertEquals("Action", IterationAction.BREAK,
                     iterator.iterate(iterateeMock));
    }

    /**
     * Test that when the first iterator returns continue the iteration
     * carries onto the next iterator.
     */
    public void testContinue() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                iterateeMock.expects.next(stylerMock1)
                        .returns(IterationAction.CONTINUE);
                iterateeMock.expects.next(stylerMock2)
                        .returns(IterationAction.CONTINUE);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        StylerIterator iterator = createStylerIterator(
                stylerMock1, stylerMock2);
        assertEquals("Action", IterationAction.CONTINUE,
                     iterator.iterate(iterateeMock));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 ===========================================================================
*/
