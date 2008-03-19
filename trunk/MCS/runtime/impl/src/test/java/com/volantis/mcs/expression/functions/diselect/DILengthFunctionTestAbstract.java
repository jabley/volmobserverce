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

import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.SimpleDoubleValue;
import com.volantis.mcs.expression.functions.diselect.DILengthFunction;
import com.volantis.mcs.expression.functions.diselect.DIFunctionTestAbstract;

/**
 * Abstract tests class which defines tests which should be run for all
 * {@link DILengthFunction}s.
 */
public abstract class DILengthFunctionTestAbstract
        extends DIFunctionTestAbstract {

    /**
     * Test objects
     */
    DILengthFunction function;
    private String usablePixelsPolicyName;
    private String actualPixelsPolicyName;
    private String actualMmPolicyName;

    /**
     * Default values.
     */
    private static final StringValue INVALID_DEFAULT =
            new SimpleStringValue(exprFactory, "stringValue");

    private static final DoubleValue VALID_DEFAULT =
            new SimpleDoubleValue(exprFactory, 100);
    /**
     * The Value that should be returned if both the value retrieved from the
     * repository and the default value are invalid.
     */
    private static final SimpleDoubleValue UNKNOWN_VALUE =
            new SimpleDoubleValue(null, 0);

    // Javadoc inherited.
    public void setUp() throws Exception {
        super.setUp();

        // Initialise test objects.
        function = getFunction();
        String[] policyNames = getPolicyNames();
        usablePixelsPolicyName = policyNames[2];
        actualPixelsPolicyName = policyNames[0];
        actualMmPolicyName = policyNames[1];
    }

    /**
     * Retrieve the {@link DILengthFunction} instance that should be tested.
     *
     * @return DILengthFunction to be tested
     */
    public abstract DILengthFunction getFunction();

    /**
     * Retrieve the list of policy names that should be expected to be
     * retrieved from the device repository in the order: actual length in
     * pixels, actual length in millimetres, usable length in pixels.
     *
     * @return String[] containing the policy names that are expected to be
     * retrieved from the repository in the order: actual length in
     * pixels, actual length in millimetres, usable length in pixels
     */
    public abstract String[] getPolicyNames();

    public void testConvertLengthFromMMWithInvalidUnit() {
        DILengthFunction function = getFunction();
        try {
            function.convertLengthFromMm(10, "mmm");
            fail("Attempting to convert a length when the specified unit is " +
                    "invalid should cause an exception to be thrown");
        } catch(IllegalArgumentException e) {
            // do nothing, correct behaviour
        }
    }

    public void testConvertLengthFromMMWithValidUnits() {
        DILengthFunction function = getFunction();

        final double lengthInMm = 15;
        final double lengthInCm = 15 * 0.1;
        final double lengthInIn = 15 * 0.0393701;
        final double lengthInPt = 15 * 2.845358;
        final double lengthInPc = 15 * 0.2371132;

        double result = function.convertLengthFromMm(lengthInMm,
                DILengthFunction.MM);
        assertTrue(lengthInMm == result);

        result = function.convertLengthFromMm(lengthInMm,
                DILengthFunction.CM);
        assertTrue(lengthInCm == result);

        result = function.convertLengthFromMm(lengthInMm,
                DILengthFunction.IN);
        assertTrue(lengthInIn == result);

        result = function.convertLengthFromMm(lengthInMm,
                DILengthFunction.PT);
        assertTrue(lengthInPt == result);

        result = function.convertLengthFromMm(lengthInMm,
                DILengthFunction.PC);
        assertTrue(lengthInPc == result);
    }

    public void doTestCalculateUsableLengthInMillimetresFails(String actualMm,
            String actualPixels, String usablePixels) {

        // set expectation for usable height in pixels
        accessorMock.expects.getDependentPolicyValue(actualPixelsPolicyName).
                returns(actualPixels).optional();
        // set expectation for actual height in pixels
        accessorMock.expects.getDependentPolicyValue(actualMmPolicyName).
                returns(actualMm).optional();
        // set expectation for actual height in millimetres
        accessorMock.expects.getDependentPolicyValue(usablePixelsPolicyName).
                returns(usablePixels);

        try {
            function.calculateUsableLengthInMillimetres(
                accessorMock, usablePixelsPolicyName, actualPixelsPolicyName,
                actualMmPolicyName);
            fail("Should not be able to calculate the usable width in mm");
        } catch (NumberFormatException e) {
            // correct behaviour, do nothing
        }
    }

    public void testCalculateUsableLengthInMillimetresSuccess() {

        // set expectation for usable height in pixels
        accessorMock.expects.getDependentPolicyValue(actualPixelsPolicyName).
                returns("100").optional();
        // set expectation for actual height in pixels
        accessorMock.expects.getDependentPolicyValue(actualMmPolicyName).
                returns("400").optional();
        // set expectation for actual height in millimetres
        accessorMock.expects.getDependentPolicyValue(usablePixelsPolicyName).
                returns("60");

        double result = function.calculateUsableLengthInMillimetres(
                accessorMock, usablePixelsPolicyName, actualPixelsPolicyName,
                actualMmPolicyName);

        assertTrue(result == 240);
    }

    public void testCalculateUsableLengthInMillimetresWithMissingUsablePixelLength() {

        doTestCalculateUsableLengthInMillimetresFails("400", "100", null);
    }

    public void testCalculateUsableLengthInMillimetresWithMissingActualPixelLength() {

        doTestCalculateUsableLengthInMillimetresFails("400", null, "60");
    }

    public void testCalculateUsableLengthInMillimetresWithMissingActualMmLength() {

        doTestCalculateUsableLengthInMillimetresFails("400", "100", null);
    }


    /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is invalid, the function evaluates to zero.
     */
    public void testCalculateLengthWithInvalidDefaultValue()
            throws ExpressionException {
        // test!
        Value result = function.calculateLength(exprContext, accessorMock,
                DILengthFunction.PX, INVALID_DEFAULT, null);
        // expect the result to be false because default value was wrong type
        assertTrue(result instanceof SimpleDoubleValue);
        assertTrue(UNKNOWN_VALUE.asJavaDouble() ==
                ((SimpleDoubleValue)result).asJavaDouble());
    }

    /**
     * Verify that when no value can be retrieved from the repository and no
     * default value is specifed, the function evaluates to zero.
     */
    public void testCalculateLengthWithNoDefaultValue() {
        // test!
        Value result = function.calculateLength(exprContext, accessorMock,
                DILengthFunction.PX, null, null);
        // expect the result to be false because no default value was specified
        assertTrue(result instanceof SimpleDoubleValue);
        assertTrue(UNKNOWN_VALUE.asJavaDouble() ==
                ((SimpleDoubleValue)result).asJavaDouble());
    }

    /**
     * Verify that when no value can be retrieved from the repository and the
     * default value is valid, the function evaluates to the default value.
     */
    public void testCalculateLengthWithValidDefaultValue() {

        // test!
        Value result = function.calculateLength(exprContext, accessorMock,
                DILengthFunction.PX, VALID_DEFAULT, null);
        // expect the result to be the default value
        assertTrue(result instanceof SimpleDoubleValue);
        assertTrue(VALID_DEFAULT.asJavaDouble() ==
                ((SimpleDoubleValue)result).asJavaDouble());
    }

    /**
     * Verify that when the value retrieved from the repository is non null and
     * valid, then retrieveLengthValue returns it as a double
     */
    public void testRetrieveLengthValueWithValidRepositoryValue() {

        // set expectations
        final String policyName = "doesn't matter";
        accessorMock.expects.getDependentPolicyValue(policyName).
                returns("400");
        // test!
        double result = function.retrieveLengthValue(accessorMock, policyName,
                DILengthFunction.PX);
        // expect the result to be that retrieved from the repository
        assertTrue(400 == result);
    }


    /**
     * Verify that when the value retrieved from the repository is invalid (i.e.
     * doesn't map to a numeric), then tretrieveLengthValue returns zero.
     */
    public void testRetrieveLengthValueWithInvalidRepositoryValue() {

        // set expectations
        final String policyName = "doesn't matter";
        accessorMock.expects.getDependentPolicyValue(policyName).
                returns("four hundred");

        // test!
        double result = function.retrieveLengthValue(accessorMock, policyName,
                DILengthFunction.PX);
        // expect the result to be the default value
        assertTrue(0 == result);
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
