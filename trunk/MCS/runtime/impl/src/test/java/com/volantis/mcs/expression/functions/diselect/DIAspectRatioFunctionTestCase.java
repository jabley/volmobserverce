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
import com.volantis.mcs.expression.functions.diselect.DIAspectRatioFunction;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.SimpleIntValue;

/**
 * Defines tests which should be run for {@link DIAspectRatioFunction}
 */
public class DIAspectRatioFunctionTestCase
        extends DIFunctionTestAbstract {

    /**
     * Constants used in testing.
     */
    protected static final String INVALID_VALUE = "squiggle";

    protected static final Value INVALID_DEFAULT = BooleanValue.FALSE;

    protected final SimpleIntValue VALID_INT_DEFAULT =
            new SimpleIntValue(exprFactory, 16);
    
    protected final SimpleStringValue VALID_STRING_DEFAULT =
            new SimpleStringValue(exprFactory, "450");

    // The Value that should be returned if both the value retrieved from the
    // repository and the default value are invalid, and a ratio component (i.e.
    // width or height) was requested.
    protected final SimpleIntValue UNKNOWN_INT_VALUE =
            new SimpleIntValue(exprFactory, 0);

    // The Value that should be returned if both the value retrieved from the
    // repository and the default value are invalid, and a ratio was requested.
    protected static final SimpleStringValue UNKNOWN_STRING_VALUE =
            new SimpleStringValue(exprFactory, "");

    protected static final String VALID_PIXEL_HEIGHT = "450";
    protected static final String VALID_PIXEL_WIDTH = "600";
    protected static final Value VALID_PIXEL_RATIO =
            new SimpleStringValue(exprFactory, "4/3");
    protected static final int VALID_WIDTH = 4;
    protected static final int VALID_HEIGHT = 3;

    /**
     * Return the {@link DIAspectRatioFunction} that should be used in tests.
     *
     * @return DIAspectRatioFunction to be used in tests.
     */
    public DIAspectRatioFunction getFunction() {
        return new DIAspectRatioFunction();
    }

    /**
     * Evaluate the execute function with the supplied parameters.
     */
    protected void doTestExecute(String pixelHeight, String pixelWidth,
            Value defaultValue, Value expectedValue,
            DIAspectRatioFunction.AspectRatioOutputFormat outputFormat) {

        // set expectations
        accessorMock.expects.getDependentPolicyValue(
                DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS).
                returns(pixelHeight);
        accessorMock.expects.getDependentPolicyValue(
                DevicePolicyConstants.ACTUAL_WIDTH_IN_PIXELS).
                returns(pixelWidth);

        // test!
        DIAspectRatioFunction function = getFunction();
        Value result = function.execute(exprContext, accessorMock,
                defaultValue);

        // checks that result is as expected
        if (outputFormat ==
                DIAspectRatioFunction.AspectRatioOutputFormat.RATIO) {
            assertTrue(result instanceof StringValue);
            assertEquals(((StringValue)expectedValue).asJavaString(),
                    ((StringValue)result).asJavaString());

        } else {
            assertTrue(result instanceof SimpleIntValue);
            assertTrue(((SimpleIntValue)expectedValue).asJavaInt() ==
                    ((SimpleIntValue)result).asJavaInt());
        }
    }

   /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is invalid, the function evaluates to an empty string
    * or zero (depending on the output format requested).
     */
    public void testExecuteWithInvalidDefaultValue() {
       doTestExecute(null, null, INVALID_DEFAULT, UNKNOWN_STRING_VALUE,
               DIAspectRatioFunction.AspectRatioOutputFormat.RATIO);
    }

    /**
     * Verify that when no value can be retrieved from the repository and no
     * default value is specifed, the function evaluates to an empty string
    * or zero (depending on the output format requested).
     */
    public void testExecuteWithNoDefaultValue() {
       doTestExecute(null, null, null, UNKNOWN_STRING_VALUE,
               DIAspectRatioFunction.AspectRatioOutputFormat.RATIO);
    }

    /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is valid, the function evaluates to the default value.
     */
    public void testExecuteWithValidDefaultValue() {
        doTestExecute(null, null, VALID_STRING_DEFAULT, VALID_STRING_DEFAULT,
               DIAspectRatioFunction.AspectRatioOutputFormat.RATIO);
    }

    /**
     * Verify that when the value retrieved from the repository is non null and
     * valid, the function evaluates to it.
     */
    public void testExecuteValueWithValidRepositoryValue() {
        doTestExecute(VALID_PIXEL_HEIGHT, VALID_PIXEL_WIDTH,
                VALID_STRING_DEFAULT, VALID_PIXEL_RATIO,
                DIAspectRatioFunction.AspectRatioOutputFormat.RATIO);
    }

    /**
     * Verify that when the value retrieved from the repository is invalid (i.e.
     * doesn't map to a numeric), the function evaluates to the default value.
     */
    public void testExecuteValueWithInvalidRepositoryValue() {
        doTestExecute(INVALID_VALUE, VALID_PIXEL_WIDTH, VALID_STRING_DEFAULT,
                VALID_STRING_DEFAULT,
                DIAspectRatioFunction.AspectRatioOutputFormat.RATIO);
    }

    /**
     * Verify that attempting to find the GCD with a zero parameter results in
     * the non zero parameter value being returned.
     */
    public void testFindGreatestCommonDenominatorWithAZeroParameter() {
        DIAspectRatioFunction function = getFunction();
        int gcd = function.findGreatestCommonDenominator(0, 450);
        assertEquals(450, gcd);

        gcd = function.findGreatestCommonDenominator(450, 0);
        assertEquals(450, gcd);
    }

    /**
     * Verify that attempting to find the GCD with two zero parameters results
     * in zero being returned.
     */
    public void testFindGreatestCommonDenominatorWithTwoZeroParameters() {
        DIAspectRatioFunction function = getFunction();
        int gcd = function.findGreatestCommonDenominator(0, 0);
        assertEquals(0, gcd);
    }

    /**
     * Verify that attempting to find the GCD with two non zero parameters
     * results in the GCD being returned.
     */
    public void testFindGreatestCommonDenominatorWithNonZeroParameters() {
        DIAspectRatioFunction function = getFunction();
        int gcd = function.findGreatestCommonDenominator(600, 450);
        assertEquals(150, gcd);

        gcd = function.findGreatestCommonDenominator(450, 600);
        assertEquals(150, gcd);

        gcd = function.findGreatestCommonDenominator(870, 420);
        assertEquals(30, gcd);
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
