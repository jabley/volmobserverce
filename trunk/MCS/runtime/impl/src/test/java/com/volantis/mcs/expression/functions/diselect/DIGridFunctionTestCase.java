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
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.mcs.expression.functions.diselect.DIGridFunction;
import com.volantis.mcs.expression.functions.diselect.DIFunctionTestAbstract;

/**
 * Defines tests which should be run for {@link DIGridFunction}
 */
public class DIGridFunctionTestCase extends DIFunctionTestAbstract {

    /**
     * Constants used in testing.
     */
    private static final StringValue INVALID_DEFAULT =
            new SimpleStringValue(exprFactory, "stringValue");

    /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is invalid, the function evaluates to FALSE.
     */
    public void testExecuteWithInvalidDefaultValue() {

        DIGridFunction function = new DIGridFunction();
        Value result = function.execute(exprContext, accessorMock, INVALID_DEFAULT);
        // expect the result to be false because default value was wrong type
        assertEquals(BooleanValue.FALSE, result);
    }

    /**
     * Verify that when no value can be retrieved from the repository and no
     * default value is specifed, the function evaluates to FALSE.
     */
    public void testExecuteWithNoDefaultValue() {

        DIGridFunction function = new DIGridFunction();
        Value result = function.execute(exprContext, accessorMock, null);
        // expect the result to be false because no default value was specified
        assertEquals(BooleanValue.FALSE, result);
    }

    /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is valid, the function evaluates to the default value.
     */
    public void testExecuteWithValidDefaultValue() {

        DIGridFunction function = new DIGridFunction();
        Value result = function.execute(exprContext, accessorMock,
                BooleanValue.TRUE);
        // expect the result to be the default value
        assertEquals(BooleanValue.TRUE, result);
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
