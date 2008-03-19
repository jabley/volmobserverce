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

import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.sequence.Item;

public class SubsequenceFunctionTestCase
        extends FunctionTestAbstract {

    private Item[] flintstoneAndRubbleItems;

    protected void setUp() throws Exception {
        super.setUp();

        flintstoneAndRubbleItems = new Item[] {
                    factory.createStringValue("fred"),
                    factory.createStringValue("wilma"),
                    factory.createStringValue("barney"),
                    factory.createStringValue("betty"),
                };
    }

    /**
     * Test that an empty input sequence returns an empty sequence.
     */
    public void testEmpty() throws Exception {

        Sequence sequence = factory.createSequence(new Item[0]);
        Function function = new SubsequenceFunction();

        Value result;

        result = invokeSubsequence(function, sequence, 1);
        assertSame(sequence, result);

        result = invokeSubsequence(function, sequence, -2);
        assertSame(sequence, result);

        result = invokeSubsequence(function, sequence, 100, 40);
        assertSame(sequence, result);
    }

    /**
     * Test that a range within the middle that does not overlap the ends
     * works.
     */
    public void testMiddleRange() throws Exception {

        Sequence sequence = factory.createSequence(flintstoneAndRubbleItems);
        Function function = new SubsequenceFunction();

        Value result;

        result = invokeSubsequence(function, sequence, 2, 2);
        Sequence subsequence = result.getSequence();
        assertEquals(subsequence.getLength(), 2);
        assertSame(flintstoneAndRubbleItems[1], subsequence.getItem(1));
        assertSame(flintstoneAndRubbleItems[2], subsequence.getItem(2));
    }

    /**
     * Test that a range that overlaps the start works.
     */
    public void testOverlappingStartRange() throws Exception {

        Sequence sequence = factory.createSequence(flintstoneAndRubbleItems);
        Function function = new SubsequenceFunction();

        Value result;

        result = invokeSubsequence(function, sequence, -2, 6);
        Sequence subsequence = result.getSequence();
        assertEquals(subsequence.getLength(), 3);
        assertSame(flintstoneAndRubbleItems[0], subsequence.getItem(1));
        assertSame(flintstoneAndRubbleItems[1], subsequence.getItem(2));
        assertSame(flintstoneAndRubbleItems[2], subsequence.getItem(3));
    }

    /**
     * Test that a range that overlaps the end works.
     */
    public void testOverlappingEndRange() throws Exception {

        Sequence sequence = factory.createSequence(flintstoneAndRubbleItems);
        Function function = new SubsequenceFunction();

        Value result;

        result = invokeSubsequence(function, sequence, 2, 6);
        Sequence subsequence = result.getSequence();
        assertEquals(subsequence.getLength(), 3);
        assertSame(flintstoneAndRubbleItems[1], subsequence.getItem(1));
        assertSame(flintstoneAndRubbleItems[2], subsequence.getItem(2));
        assertSame(flintstoneAndRubbleItems[3], subsequence.getItem(3));
    }

    /**
     * Test that a range that overlaps the whole sequence works.
     */
    public void testOverlappingRange() throws Exception {

        Sequence sequence = factory.createSequence(flintstoneAndRubbleItems);
        Function function = new SubsequenceFunction();

        Value result;

        result = invokeSubsequence(function, sequence, -2, 60);
        Sequence subsequence = result.getSequence();
        assertSame(sequence, subsequence);
    }

    /**
     * Invoke the 2 argument form of the subsequence function.
     * @param function The function.
     * @param sequence The sequence.
     * @param start The start position.
     * @return The subsequence value.
     * @throws ExpressionException
     */
    private Value invokeSubsequence(
            Function function, Sequence sequence, final int start)
            throws ExpressionException {

        return function.invoke(expressionContextMock, new Value[]{
            sequence,
            factory.createIntValue(start)
        });
    }

    /**
     * Invoke the 3 argument form of the subsequence function.
     * @param function The function.
     * @param sequence The sequence.
     * @param start The start position.
     * @param length The length of the subsequence.
     * @return The subsequence value.
     * @throws ExpressionException
     */
    private Value invokeSubsequence(
            Function function, Sequence sequence, final int start,
            final int length)
            throws ExpressionException {

        return function.invoke(expressionContextMock, new Value[]{
            sequence,
            factory.createIntValue(start),
            factory.createIntValue(length),
        });
    }
}
