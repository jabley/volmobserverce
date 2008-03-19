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
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.mcs.expression.functions.diselect.DILengthFunction;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.NumericValue;
import com.volantis.xml.expression.atomic.numeric.SimpleDoubleValue;

/**
 * This Function retrieves the resolution of the display of the device making
 * the request (in dpi, dpcm or dpmm). If the repository contains no entry for
 * this then the supplied default value will be returned. If this default value
 *  is not valid, an empty string will be returned.
 */
public class DIResolutionFunction extends DILengthFunction {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(DIResolutionFunction.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    DIResolutionFunction.class);

    // Javadoc inherited.
    protected Value execute(
            ExpressionContext expressionContext,
            DevicePolicyValueAccessor accessor, String name,
            Value defaultValue) {

        Value value = null;
        ExpressionFactory factory = expressionContext.getFactory();

        // resolution in mm = number of pixels on the screen divided by the
        // area of the screen in millimetres.
        String heightInPixels = accessor.getDependentPolicyValue(
                DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS);
        String heightInMillimetres = accessor.getDependentPolicyValue(
                DevicePolicyConstants.ACTUAL_HEIGHT_IN_MM);

        String widthInPixels = accessor.getDependentPolicyValue(
                DevicePolicyConstants.ACTUAL_WIDTH_IN_PIXELS);
        String widthInMillimetres = accessor.getDependentPolicyValue(
                DevicePolicyConstants.ACTUAL_WIDTH_IN_MM);

        if (heightInMillimetres != null && heightInPixels != null &&
                widthInMillimetres != null && widthInPixels != null) {
            try {
                double totalPixels = Double.parseDouble(widthInPixels) *
                        Double.parseDouble(heightInPixels);
                double areaInMm = Double.parseDouble(widthInMillimetres) *
                        Double.parseDouble(heightInMillimetres);

                double resolution = totalPixels /
                        convertAreaFromMmSquared(areaInMm, name);

                value = new SimpleDoubleValue(factory, resolution);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Resolution calculated from repository " +
                            "values is : " + resolution + " in " + name);
                }
            } catch (NumberFormatException e) {
                // log it but continue
                String badValue = "One of " + heightInPixels + " or " +
                        heightInMillimetres + " or " + widthInPixels + " or " +
                        widthInMillimetres;
                String policyNames = "One of "+
                        DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS +
                        " or " + DevicePolicyConstants.ACTUAL_HEIGHT_IN_MM +
                        " or " + DevicePolicyConstants.ACTUAL_WIDTH_IN_PIXELS +
                        " or " + DevicePolicyConstants.ACTUAL_WIDTH_IN_MM;

                LOGGER.info("function-bad-parameter-value",
                        new Object[] {badValue, policyNames});
            }
        }

        if (value == null) {
            // verify that the default value is of the correct type
            if (defaultValue instanceof NumericValue) {
                value = defaultValue;
            } else {
                if (defaultValue != null) {
                    // If the default value was present but of the wrong type,
                    // then log it
                    LOGGER.info("function-bad-default-value", "NumericValue");
                }
                value = EMPTY_VALUE;
            }
        }

        return value;
    }

    // Javadoc inherited.
    protected String getFunctionName() {
        return "cssmq-resolution";
    }

    /**
     * Converts from a double representing an area in millimetres squared to a
     * double value representing that area in either inches or centimetres
     * squared (as requested).
     *
     * @return double value representing an area in either mm, inches or cm
     * squared.
     * @throws IllegalArgumentException if the string unit specified did not
     * correspond to one of the valid units.
     */
    protected double convertAreaFromMmSquared(double areaInMmSquared,
            String units) throws IllegalArgumentException{

        // convert the string to a double
        double result;

        if (DPMM.equalsIgnoreCase(units)) {
            result = areaInMmSquared;
        } else if (DPCM.equalsIgnoreCase(units)) {
            result = areaInMmSquared * 0.01;
        } else if (DPIN.equalsIgnoreCase(units)) {
            result = areaInMmSquared * (MM_TO_IN * MM_TO_IN);
        } else {
            throw new IllegalArgumentException(exceptionLocalizer.format(
                    "invalid-length-unit", new Object[] {units, "diselect"}));
        }
        return result;
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
