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

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.expression.functions.diselect.DIColorFunction;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.SimpleIntValue;

/**
 * Defines tests which should be run for {@link DIColorFunction}
 */
public class DIColorFunctionTestCase
        extends DIFunctionTestAbstract {

    /**
     * Constants used in testing.
     */
    private static final String INVALID_VALUE = "squiggle";

    protected static final StringValue INVALID_DEFAULT =
            new SimpleStringValue(exprFactory, "stringValue");

    protected static final SimpleIntValue VALID_DEFAULT =
            new SimpleIntValue(exprFactory, 16);

    // The Value that should be returned if both the value retrieved from the
    // repository and the default value are invalid.
    protected static final SimpleIntValue UNKNOWN_VALUE =
            new SimpleIntValue(exprFactory, 0);

    /**
     * Evaluate the execute function with the supplied parameters.
     */
    private void doTestExecute(String pixelDepth, Value defaultValue,
            SimpleIntValue expectedValue) {

        // set expectations
        accessorMock.expects.getDependentPolicyValue(
                DevicePolicyConstants.PIXEL_DEPTH).returns(pixelDepth);

        // test!
        DIColorFunction function = new DIColorFunction();
        Value result = function.execute(exprContext, accessorMock,
                defaultValue);

        // checks that result is as expected
        assertTrue(result instanceof SimpleIntValue);
        assertTrue(expectedValue.asJavaInt() ==
                ((SimpleIntValue)result).asJavaInt());
    }

   /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is invalid, the function evaluates to zero.
     */
    public void testExecuteWithInvalidDefaultValue() {
       doTestExecute(null, INVALID_DEFAULT, UNKNOWN_VALUE);
    }

    /**
     * Verify that when no value can be retrieved from the repository and no
     * default value is specifed, the function evaluates to zero.
     */
    public void testExecuteWithNoDefaultValue() {
        doTestExecute(null, null, UNKNOWN_VALUE);
    }

    /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is valid, the function evaluates to the default value.
     */
    public void testExecuteWithValidDefaultValue() {
        doTestExecute(null, VALID_DEFAULT, VALID_DEFAULT);
    }

    /**
     * Verify that when the value retrieved from the repository is non null and
     * valid, the function evaluates to it wrapped in a SimpleIntValue
     */
    public void testExecuteValueWithValidRepositoryValue() {
        doTestExecute("32", VALID_DEFAULT, new SimpleIntValue(exprFactory, 32));

    }

    /**
     * Verify that when the value retrieved from the repository is invalid (i.e.
     * doesn't map to a numeric), the function evaluates to the default value.
     */
    public void testExecuteValueWithInvalidRepositoryValue() {
        doTestExecute(INVALID_VALUE, VALID_DEFAULT, VALID_DEFAULT);
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
