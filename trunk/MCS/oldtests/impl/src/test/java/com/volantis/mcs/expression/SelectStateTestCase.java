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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.atomic.BooleanValue;

/**
 * Simple test case
 */
public class SelectStateTestCase extends TestCaseAbstract {
    public SelectStateTestCase(String s) {
        super(s);
    }

    public void testConstructor() throws Exception {
        SelectState state = new SelectState(Precept.MATCH_EVERY,
                                            BooleanValue.TRUE);

        assertSame("Precept not as",
                   Precept.MATCH_EVERY,
                   state.getPrecept());
        assertSame("Expression not as",
                   BooleanValue.TRUE,
                   state.getExpression());
        assertFalse("Expected matched to be false",
                    state.isMatched());
        assertFalse("Expected otherwiseExecuted to be false",
                    state.isOtherwiseExecuted());

        try {
            state = new SelectState(Precept.MATCH_EVERY,
                                    null);
            fail("Should have had an IllegalArgumentException for null " +
                 "expression");
        } catch (IllegalArgumentException e) {
            // Expected condition
        }

        try {
            state = new SelectState(null,
                                    BooleanValue.TRUE);
            fail("Should have had an IllegalArgumentException for null " +
                 "precept");
        } catch (IllegalArgumentException e) {
            // Expected condition
        }
    }

    public void testGetPrecept() throws Exception {
        SelectState state = new SelectState(Precept.MATCH_FIRST,
                                            BooleanValue.TRUE);

        assertSame("Precept not as",
                   Precept.MATCH_FIRST,
                   state.getPrecept());
    }

    public void testGetExpression() throws Exception {
        SelectState state = new SelectState(Precept.MATCH_FIRST,
                                            BooleanValue.FALSE);

        assertSame("Expression not as",
                   BooleanValue.FALSE,
                   state.getExpression());
    }

    public void testIsMatched() throws Exception {
        SelectState state = new SelectState(Precept.MATCH_FIRST,
                                            BooleanValue.FALSE);

        assertFalse("Expected matched to be false",
                    state.isMatched());

        state.setMatched();

        assertTrue("Expected matched to be true",
                   state.isMatched());
    }

    public void testIsOtherwiseExecuted() throws Exception {
        SelectState state = new SelectState(Precept.MATCH_FIRST,
                                            BooleanValue.FALSE);

        assertFalse("Expected otherwiseExecuted to be false",
                    state.isOtherwiseExecuted());

        state.setOtherwiseExecuted();

        assertTrue("Expected otherwiseExecuted to be true",
                   state.isOtherwiseExecuted());
    }

    /**
     * The normal function of setMatched has been implicitly tested in other
     * tests. This just tests the error conditions.
     */
    public void testSetMatched() throws Exception {
        SelectState state = new SelectState(Precept.MATCH_FIRST,
                                            BooleanValue.FALSE);

        state.setOtherwiseExecuted();

        try {
            state.setMatched();

            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected condition
        }
    }

    /**
     * The normal function of setOtherwiseExecuted has been implicitly tested
     * in other tests. This just tests the error conditions.
     */
    public void testSetOtherwiseExecuted() throws Exception {
        SelectState state = new SelectState(Precept.MATCH_FIRST,
                                            BooleanValue.FALSE);

        state.setMatched();

        try {
            state.setOtherwiseExecuted();

            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected condition
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Aug-03	1019/1	philws	VBM:2003080807 Provide MCS core extensions for handling the select markup element's state

 ===========================================================================
*/
