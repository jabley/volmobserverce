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

import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.SimpleDoubleValue;

/**
 * This class contains methods and data common to all DISelect functions that
 * are to do with length e.g. di-cssmq-width, di-cssmq-height.
 */
public abstract class DILengthFunction extends SingleMandatoryArgumentExpressionFunction {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    DILengthFunction.class);

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(DILengthFunction.class);

    /**
     * The Strings which represent the valid length units.
     */
    protected static final String PX = "px";
    protected static final String MM = "mm";
    protected static final String IN = "in";
    protected static final String CM = "cm";
    protected static final String PT = "pt";
    protected static final String PC = "pc";

    /**
     * The Strings which represent the valid resolution units.
     */
    protected static final String DPIN = "dpi";
    protected static final String DPMM = "dpmm";
    protected static final String DPCM = "dpcm";

    /**
     * The conversion factors from millimetres to centimetres, inches, point
     * and pica.
     */
    protected static final double MM_TO_CM = 0.1;
    protected static final double MM_TO_IN = 0.0393701;
    protected static final double MM_TO_PT = 2.845358;
    protected static final double MM_TO_PC = 0.2371132;

    /**
     * The Value that should be returned if both the value retrieved from the
     * repository and the default value are invalid.
     */
    protected static final Value EMPTY_VALUE = new SimpleDoubleValue(null, 0);

    /**
     * The DefaultValueProvider for this class.
     */
    private static final DefaultValueProvider DEFAULT_VALUE_PROVIDER =
            new DefaultValueProvider();

    // Javadoc inherited.
    protected DefaultValueProvider getDefaultValueProvider() {
        return DEFAULT_VALUE_PROVIDER;
    }

    protected Value execute(
            ExpressionContext expressionContext, String name,
            Value defaultValue) {

        // Extract the DevicePolicyValueAccessor for the current device.
        DevicePolicyValueAccessor accessor =
                (DevicePolicyValueAccessor) expressionContext.getProperty(
                        DevicePolicyValueAccessor.class);

        return execute(expressionContext, accessor, name, defaultValue);
    }

    protected abstract Value execute(
            ExpressionContext expressionContext,
            DevicePolicyValueAccessor accessor, String name,
            Value defaultValue);

    /**
     * If the String value retrieved from the repository is a length, then
     * convert it to a {@link Value} and return it. However if the actual
     * String value is not a length, and the default value is non null then
     * return that, otherwise return a zero Value.
     *
     * @param expressionContext     encapsulates all the information required
     *                              to evaluate an expression
     * @param accessor              for information about the current device
     * @param units                 in which the length should be returned
     * @param defaultValue          to be used if no value can be retrieved
     * @param actualValue           the value retrieved from the repository or
     *                              calculated
     * @return Value which is the received String converted to a length, or the
     * default value if the actual value is not a length, or zero if the
     * default value is null.
     */
    protected Value calculateLength(ExpressionContext expressionContext,
            DevicePolicyValueAccessor accessor, String units, Value defaultValue,
            DoubleValue actualValue) {

        Value value = actualValue;

        if (actualValue == null || actualValue.asJavaDouble() == 0) {
            // Verify that the default value is of the correct type
            if (defaultValue instanceof DoubleValue) {
                value = defaultValue;
            } else {
                if (defaultValue != null) {
                    LOGGER.info("function-bad-default-value", "DoubleValue");
                }
                value = EMPTY_VALUE;
            }
        }

        return value;
    }

    /**
     * Retrieves the value of the policy of the specified name from the
     * repository and converts it to a double value in the required units. If
     * the value retrieved from the repository cannot be parsed into a number
     * in the correct units, then zero will be returned.
     *
     * @param accessor   for information about the current device
     * @param policyName the name of the policy whose value should be
     *                   retrieved from the repository
     * @param units      the units in which the value is required.
     * @return double which is the length is the specified units or zero if it
     *         could not be retrieved
     */
    protected double retrieveLengthValue(DevicePolicyValueAccessor accessor,
            String policyName, String units) {

        String length = accessor.getDependentPolicyValue(policyName);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The value retrieved from repository for policy:" +
                    policyName + " is : " + length + " in " + units);
        }

        // default to zero length
        double convertedLength = 0;

        try {
            if (PX.equalsIgnoreCase(units)) {
                convertedLength = Double.parseDouble(length);
            } else {
                convertedLength = convertLengthFromMm(
                        Double.parseDouble(length), units);
            }
        } catch(NumberFormatException e) {
            // log it but return zero
            LOGGER.info("function-bad-parameter-value",
                    new Object[] {length, policyName});
        }
        return convertedLength;
    }

    /**
     * Converts from a String value representing a length in millimetres to a
     * double value representing that length in either inches, centimetres,
     * point or pica (as requested).
     *
     * @return double value representing a length in either inches,
     * centimetres, point or pica.
     * @throws IllegalArgumentException if the string unit specified did not
     * correspond to one of the valid units.
     */
    protected double convertLengthFromMm(double lengthInMm, String units)
            throws IllegalArgumentException{

        // convert the string to a double
        double result;

        if (MM.equalsIgnoreCase(units)) {
            result = lengthInMm;
        } else if (CM.equalsIgnoreCase(units)) {
            result = lengthInMm * MM_TO_CM;
        } else if (IN.equalsIgnoreCase(units)) {
            result = lengthInMm * MM_TO_IN;
        } else if (PT.equalsIgnoreCase(units)) {
            result = lengthInMm * MM_TO_PT;
        } else if (PC.equalsIgnoreCase(units)) {
            result = lengthInMm * MM_TO_PC;
        } else {
            throw new IllegalArgumentException(exceptionLocalizer.format(
                    "invalid-length-unit", new Object[] {units, "diselect"}));
        }
        return result;
    }

    /**
     * We don't have the usable display measurements in the repository in
     * millimetres, however it can be calculated because we know the usable
     * dimensions in pixels, and we can calculate how many pixels there are per
     * millimetre because we have the actual measurements in both pixels and
     * millimetres.
     *
     * @param accessor                for information about the current device
     * @param usablePixelLengthPolicy name of the policy which corresponds to
     *                                the usable length in pixels
     * @param actualPixelLengthPolicy name of the policy which corresponds to
     *                                the actual length in pixels
     * @param actualMmLengthPolicy    name of the policy which corresponds to
     *                                the actual length in millimetres
     * @return double usable length in millimetres
     * @throws NumberFormatException if any of the values retrieved from the
     *                               repository cannot be parsed into numbers.
     */
    protected double calculateUsableLengthInMillimetres (
            DevicePolicyValueAccessor accessor, String usablePixelLengthPolicy,
            String actualPixelLengthPolicy, String actualMmLengthPolicy)
            throws NumberFormatException {

        double value;
        String usablePixelLength = accessor.getDependentPolicyValue(
                usablePixelLengthPolicy);
        String actualPixelLength = accessor.getDependentPolicyValue(
                actualPixelLengthPolicy);
        String actualMmLength = accessor.getDependentPolicyValue(
                actualMmLengthPolicy);
        if (usablePixelLength != null && actualPixelLength != null &&
                actualMmLength != null) {
            double ratio = Double.parseDouble(actualMmLength) /
                    Double.parseDouble(actualPixelLength);
            value = Double.parseDouble(usablePixelLength) * ratio;
        } else {
            throw new NumberFormatException(exceptionLocalizer.format(
                    "unexpected-null", "display width and height"));
        }
        return value;
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
