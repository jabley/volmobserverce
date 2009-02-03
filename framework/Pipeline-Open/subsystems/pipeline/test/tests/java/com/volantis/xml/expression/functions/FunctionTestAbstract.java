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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContextMock;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;

public abstract class FunctionTestAbstract
        extends TestCaseAbstract {

    protected ExpressionFactory factory;
    protected ExpressionContextMock expressionContextMock;

    protected void setUp() throws Exception {
        super.setUp();

        factory = ExpressionFactory.getDefaultInstance();

        expressionContextMock = new ExpressionContextMock(
                "expressionContextMock", expectations);

        expressionContextMock.expects.getFactory().returns(factory).any();
    }
    /**
     * Tests if two {@link Value}s are equal. They are considered equal here
     * if their string representations are equal.
     * 
     * @param expected expected value
     * @param actual actual value
     * @throws ExpressionException when creating string representation
     * of any value failed
     */
    protected static void assertEquals(Value expected, Value actual) throws ExpressionException {
        assertEquals(expected.stringValue().asJavaString(), 
                actual.stringValue().asJavaString());
    }
}
