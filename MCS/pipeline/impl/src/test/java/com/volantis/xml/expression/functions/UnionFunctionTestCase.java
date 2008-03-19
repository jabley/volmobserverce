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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * Simple test case for the {@link UnionFunction} class.
 */
public class UnionFunctionTestCase extends FunctionTestAbstract {

    /**
     * Testing the union function.
     * 
     * @throws Exception
     */
    public void testFunction() throws Exception {
        final Function unionFunction = new UnionFunction();
        try {
            unionFunction.invoke(expressionContextMock, new Value[0]);
            fail("Exception wasn't thrown when empty arguments passed" +
                    " to function");
        } catch (Exception e) {
            // ignore it, expected situation
        }
        assertSame("Function invoked with the empty sequences didn't" +
                " return the empty sequence",
                Sequence.EMPTY,
                unionFunction.invoke(expressionContextMock, new Value[] {
                        Sequence.EMPTY, Sequence.EMPTY
                }));
        final Sequence firstSequence = 
                expressionContextMock.getFactory().createSequence(
                        new Item[] {
                                expressionContextMock.getFactory()
                                        .createStringValue("value1"),
                                expressionContextMock.getFactory()
                                        .createStringValue("value2"),
                                expressionContextMock.getFactory()
                                        .createStringValue("value2"),
                                expressionContextMock.getFactory()
                                        .createBooleanValue(true)});
        final Sequence secondSequence = 
            expressionContextMock.getFactory().createSequence(
                    new Item[] {
                            expressionContextMock.getFactory()
                                    .createStringValue("value0"),
                            expressionContextMock.getFactory()
                                    .createStringValue("value0"),
                            expressionContextMock.getFactory()
                                    .createStringValue("value1"),
                            expressionContextMock.getFactory()
                                    .createBooleanValue(false)});
        final Sequence result = (Sequence) unionFunction.invoke(
                expressionContextMock, new Value[] {firstSequence,
                        secondSequence});
        assertEquals("number of elements in returned sequence is incorrect",
                5, result.getLength());
        assertEquals("incorrect element in returned sequence",
                "value1", result.getItem(1).stringValue().asJavaString());
        assertEquals("incorrect element in returned sequence",
                "value2", result.getItem(2).stringValue().asJavaString());
        assertEquals("incorrect element in returned sequence",
                "true", result.getItem(3).stringValue().asJavaString());
        assertEquals("incorrect element in returned sequence",
                "value0", result.getItem(4).stringValue().asJavaString());
        assertEquals("incorrect element in returned sequence",
                "false", result.getItem(5).stringValue().asJavaString());
    }
    
}
