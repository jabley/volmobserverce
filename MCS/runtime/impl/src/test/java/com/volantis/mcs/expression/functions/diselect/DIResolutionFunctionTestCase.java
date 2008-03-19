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
import com.volantis.mcs.expression.functions.diselect.DIResolutionFunction;
import com.volantis.mcs.expression.functions.diselect.DIFunctionTestAbstract;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.SimpleDoubleValue;

/**
 * Defines tests which should be run for DIResolutionFunction.
 */
public class DIResolutionFunctionTestCase extends DIFunctionTestAbstract {

    /**
     * Constants used in testing.
     */
    private static final String pixelsWidthPolicyName =
            DevicePolicyConstants.ACTUAL_WIDTH_IN_PIXELS;
    private static final String pixelsHeightPolicyName =
            DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS;
    private static final String mmWidthPolicyName =
            DevicePolicyConstants.ACTUAL_WIDTH_IN_MM;
    private static final String mmHeightPolicyName =
            DevicePolicyConstants.ACTUAL_HEIGHT_IN_MM;

    private static final String INVALID_VALUE = "squiggle";
    private static final String VALID_PIXEL_HEIGHT = "100";
    private static final String VALID_PIXEL_WIDTH = "50";
    private static final String VALID_MM_HEIGHT = "20";
    private static final String VALID_MM_WIDTH = "10";
    private static final double EXPECTED_DPMM_RESOLUTION = 25;
    private static final double EXPECTED_DPCM_RESOLUTION = 2500;
    private static final double EXPECTED_DPIN_RESOLUTION =
            25 / (0.0393701 * 0.0393701);

    private static final StringValue INVALID_DEFAULT =
            new SimpleStringValue(exprFactory, "stringValue");

    private static final DoubleValue VALID_DEFAULT =
            new SimpleDoubleValue(exprFactory, 100);

    // The Value that should be returned if both the value retrieved from the
    // repository and the default value are invalid.
    private static final double UNKNOWN_VALUE = 0;

   /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is invalid, the function evaluates to zero.
     */
    public void testExecuteWithInvalidDefaultValue()
            throws ExpressionException {

        doTestExecute(INVALID_VALUE, INVALID_VALUE, INVALID_VALUE,
                INVALID_VALUE, DIResolutionFunction.DPMM, INVALID_DEFAULT,
                UNKNOWN_VALUE);
    }

    /**
     * Verify that when no value can be retrieved from the repository and no
     * default value is specifed, the function evaluates to zero.
     */
    public void testExecuteWithNoDefaultValue() {

        doTestExecute(INVALID_VALUE, INVALID_VALUE, INVALID_VALUE,
                INVALID_VALUE, DIResolutionFunction.DPMM, null, UNKNOWN_VALUE);
    }

    /**
     * Verify that when a valid value can be retrieved from the repository, and
     * the result is requested in dpmm, it evaluates to the correct value.
     */
    public void testExecuteWithValidRepositoryValuesReturningDPMM() {

        doTestExecute(VALID_PIXEL_HEIGHT, VALID_PIXEL_WIDTH, VALID_MM_HEIGHT,
                VALID_MM_WIDTH, DIResolutionFunction.DPMM, VALID_DEFAULT,
                EXPECTED_DPMM_RESOLUTION);
    }

     /**
     * Verify that when a valid value can be retrieved from the repository, and
     * the result is requested in dpcm, it evaluates to the correct value.
     */
    public void testExecuteWithValidRepositoryValuesReturningDPCM() {

        doTestExecute(VALID_PIXEL_HEIGHT, VALID_PIXEL_WIDTH, VALID_MM_HEIGHT,
                VALID_MM_WIDTH, DIResolutionFunction.DPCM, VALID_DEFAULT,
                EXPECTED_DPCM_RESOLUTION);
    }

    /**
     * Verify that when a valid value can be retrieved from the repository, and
     * the result is requested in dpin, it evaluates to the correct value.
     */
    public void testExecuteWithValidRepositoryValuesReturningDPIN() {

        doTestExecute(VALID_PIXEL_HEIGHT, VALID_PIXEL_WIDTH, VALID_MM_HEIGHT,
                VALID_MM_WIDTH, DIResolutionFunction.DPIN, VALID_DEFAULT,
                EXPECTED_DPIN_RESOLUTION);
    }


    /**
     * Verify that when an invalid value is retrieved from the repository and
     * a valid default value is supplied, the function evaluates to the default.
     */
    public void testExecuteWithInvalidHeightInPixelsFromRepository() {

        doTestExecute(INVALID_VALUE, VALID_PIXEL_WIDTH, VALID_MM_HEIGHT,
                VALID_MM_WIDTH, DIResolutionFunction.DPMM, VALID_DEFAULT,
                VALID_DEFAULT.asJavaDouble());
    }

    /**
     * Verify that when an invalid value is retrieved from the repository and
     * a valid default value is supplied, the function evaluates to the default.
     */
    public void testExecuteWithInvalidWidthInPixelsFromRepository() {

        doTestExecute(VALID_PIXEL_HEIGHT, INVALID_VALUE, VALID_MM_HEIGHT,
                VALID_MM_WIDTH, DIResolutionFunction.DPMM, VALID_DEFAULT,
                VALID_DEFAULT.asJavaDouble());
    }

    /**
     * Verify that when an invalid value is retrieved from the repository and
     * a valid default value is supplied, the function evaluates to the default.
     */
    public void testExecuteWithInvalidHeightInMmFromRepository() {

        doTestExecute(VALID_PIXEL_HEIGHT, VALID_PIXEL_WIDTH, INVALID_VALUE,
                VALID_MM_WIDTH, DIResolutionFunction.DPMM, VALID_DEFAULT,
                VALID_DEFAULT.asJavaDouble());
    }

    /**
     * Verify that when an invalid value is retrieved from the repository and
     * a valid default value is supplied, the function evaluates to the default.
     */
    public void testExecuteWithInvalidWidthInMmFromRepository() {

        doTestExecute(VALID_PIXEL_HEIGHT, VALID_PIXEL_WIDTH, VALID_MM_HEIGHT,
                INVALID_VALUE, DIResolutionFunction.DPMM, VALID_DEFAULT,
                VALID_DEFAULT.asJavaDouble());
    }

    /**
     * Verify that when a null value is retrieved from the repository and
     * a valid default value is supplied, the function evaluates to the default.
     */
    public void testExecuteWithNullValueFromRepository() {

        doTestExecute(VALID_PIXEL_HEIGHT, VALID_PIXEL_WIDTH, VALID_MM_HEIGHT,
                null, DIResolutionFunction.DPMM, VALID_DEFAULT,
                VALID_DEFAULT.asJavaDouble());
    }

    /**
     * Evaluate the execute function with the supplied parameters.
     */
    public void doTestExecute(String heightInPixels, String widthInPixels,
            String heightInMm, String widthInMm, String units,
            Value defaultValue, double expectedResolution) {

        // set expectations
        accessorMock.expects.getDependentPolicyValue(pixelsHeightPolicyName).
                returns(heightInPixels).optional();
        accessorMock.expects.getDependentPolicyValue(pixelsWidthPolicyName).
                returns(widthInPixels).optional();
        accessorMock.expects.getDependentPolicyValue(mmHeightPolicyName).
                returns(heightInMm);
        accessorMock.expects.getDependentPolicyValue(mmWidthPolicyName).
                returns(widthInMm);

        // test!
        DIResolutionFunction function = new DIResolutionFunction();
        Value result = function.execute(exprContext,
                units, defaultValue);

        // expect the result to be the default
        assertTrue(result instanceof SimpleDoubleValue);
        assertTrue( expectedResolution ==
                ((SimpleDoubleValue)result).asJavaDouble());
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
