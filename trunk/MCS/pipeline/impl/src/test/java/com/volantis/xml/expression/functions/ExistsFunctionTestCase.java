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

package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * Tests for the {@link ExistsFunction}.
 */
public class ExistsFunctionTestCase
        extends FunctionTestAbstract {

    /**
     * Test that the function returns false for an empty sequence.
     */
    public void testEmptySequence() throws Exception {

        // Create an empty sequence.
        Sequence sequence = Sequence.EMPTY;

        Function function = new ExistsFunction();
        Value result =
                function.invoke(expressionContextMock, new Value[] {sequence});
        assertSame(result, BooleanValue.FALSE);
    }

    /**
     * Test that the function returns true for a non-empty sequence.
     */
    public void testNonEmptySequence() throws Exception {

        // Create a non empty sequence.
        Sequence sequence = factory.createSequence(new Item[] {
            BooleanValue.TRUE
        });

        Function function = new ExistsFunction();
        Value result =
                function.invoke(expressionContextMock, new Value[] {sequence});
        assertSame(result, BooleanValue.TRUE);
    }

}
