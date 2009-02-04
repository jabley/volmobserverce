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
package com.volantis.mcs.expression.functions.diselect;

import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.SimpleDoubleValue;
import com.volantis.mcs.expression.functions.diselect.DIScanFunction;
import com.volantis.mcs.expression.functions.diselect.DIFunctionTestAbstract;

public class DIScanFunctionTestCase
        extends DIFunctionTestAbstract {

    /**
     * Constants used in testing
     */
    private static final StringValue VALID_DEFAULT =
            new SimpleStringValue(exprFactory, "progressive");
    private static final DoubleValue INVALID_DEFAULT =
            new SimpleDoubleValue(exprFactory, 14);
    private static final String EMPTY_VALUE = "";

    /**
     * Verify that when the default value is invalid, the function evaluates to
     * an empty string.
     */
    public void testExecuteWithInvalidDefaultValue() {

        DIScanFunction function = new DIScanFunction();
        Value result = function.execute(exprContext, accessorMock,
                INVALID_DEFAULT);
        // expect the result to be "" because default value was wrong type
        assertTrue(result instanceof StringValue);
        assertEquals(EMPTY_VALUE, ((StringValue)result).asJavaString());
    }

    /**
     * Verify that when no value can be retrieved from the repository and no
     * default value is specifed, the function evaluates to an empty string.
     */
    public void testExecuteWithNoDefaultValue() {

        DIScanFunction function = new DIScanFunction();
        Value result = function.execute(exprContext, accessorMock, null);
        // expect the result to be "" because default value was null
        assertTrue(result instanceof StringValue);
        assertEquals(EMPTY_VALUE, ((StringValue)result).asJavaString());
    }

    /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is valid, the function evaluates to the default value.
     */
    public void testExecuteWithValidDefaultValue() {

        DIScanFunction function = new DIScanFunction();
        Value result = function.execute(exprContext, accessorMock,
                VALID_DEFAULT);
        // expect the result to be the default value
        assertTrue(result instanceof StringValue);
        assertEquals(VALID_DEFAULT.asJavaString(),
                ((StringValue)result).asJavaString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 ===========================================================================
*/
