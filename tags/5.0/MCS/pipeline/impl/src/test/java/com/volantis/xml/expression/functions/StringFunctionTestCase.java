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

import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.sequence.Sequence;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Simple test case for {@link StringFunction}.
 */
public class StringFunctionTestCase extends FunctionTestAbstract {

    /**
     * Testing values returned by function.
     * 
     * @throws Exception
     */
    public void testStringFunction() throws Exception {
        final Value testValue = new Value() {

            /**
             * Counter to be incremented per every {@link #stringValue} method
             * invoke.
             */
            private int count = 0;

            // javadoc inherited
            public StringValue stringValue() throws ExpressionException {
                return factory.createStringValue(String.valueOf(count ++));
            }

            // javadoc inherited
            public void streamContents(ContentHandler contentHandler)
                    throws ExpressionException, SAXException {
                // do nothing
            }

            // javadoc inherited
            public Sequence getSequence() throws ExpressionException {
                // do nothing
                return null;
            }
            
        };
        final Function stringFunction = new StringFunction();
        try {
            stringFunction.invoke(expressionContextMock, new Value[0]);
            fail("Exception wasn't thrown when empty arguments passed" +
                    " to function");
        } catch (Exception e) {
            // ignore it, expected situation
        }
        for (int i = 0; i < 10; i++) {
            final Value result = stringFunction.invoke(expressionContextMock,
                    new Value[] {testValue});
            assertTrue("Incorrect or null Value from StringFunction",
                    result != null && result instanceof StringValue);
            assertEquals(((StringValue) result).asJavaString(),
                    String.valueOf(i));
        }
    }
    
}
