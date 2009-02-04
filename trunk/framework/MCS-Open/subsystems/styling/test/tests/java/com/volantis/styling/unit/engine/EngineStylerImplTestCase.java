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

package com.volantis.styling.unit.engine;

import com.volantis.styling.impl.engine.EngineStyler;
import com.volantis.styling.impl.engine.EngineStylerImpl;
import com.volantis.styling.impl.engine.StylerContextMock;
import com.volantis.styling.impl.engine.StylerResult;
import com.volantis.styling.impl.engine.matchers.InternalMatcherContextMock;
import com.volantis.styling.impl.sheet.IndexableStylerMock;
import com.volantis.styling.impl.sheet.StylerMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

/**
 * Test cases for {@link EngineStylerImpl}.
 */
public class EngineStylerImplTestCase
        extends TestCaseAbstract {
    private static final int DEPTH = 20;

    /**
     * Test that it sets the container used by the delegating state container
     * before it
     *  
     * @throws Exception
     */
    public void test() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final StylerMock stylerMock =
                new IndexableStylerMock("stylerMock", expectations);

        final StylerContextMock stylerContextMock =
                new StylerContextMock("stylerContextMock", expectations);

        final InternalMatcherContextMock matcherContextMock =
                new InternalMatcherContextMock("matcherContextMock",
                        expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                stylerContextMock.expects.getMatcherContext()
                        .returns(matcherContextMock);

                stylerMock.expects.style(stylerContextMock)
                        .returns(StylerResult.STYLED);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        EngineStyler styler = new EngineStylerImpl(
                stylerMock,
                DEPTH);
        assertEquals("Depth", DEPTH, styler.getDepth());

        assertEquals("Styler result", StylerResult.STYLED,
                     styler.style(stylerContextMock));
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
