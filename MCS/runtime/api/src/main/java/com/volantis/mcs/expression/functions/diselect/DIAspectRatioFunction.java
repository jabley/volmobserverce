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
import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.atomic.numeric.SimpleIntValue;

/**
 * This Function retrieves the aspect ratio of the device from the repository
 * If the repository contains no entry for aspect ratio then the supplied
 * default value will be returned. If this default value is not valid, an
 * empty string will be returned.
 * <p/>
 * It is also the superclass of the aspect ratio width and height functions.
 */
public class DIAspectRatioFunction extends ZeroMandatoryArgumentsExpressionFunction{

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(DIAspectRatioFunction.class);

    /**
     * The separator of the ratio values i.e. or width / height
     */
    private static final char SEPARATOR = '/';

    /**
     * The DefaultValueProvider for this class.
     */
    private static final DefaultValueProvider DEFAULT_VALUE_PROVIDER =
            new DefaultValueProvider();

    // Javadoc inherited.
    protected DefaultValueProvider getDefaultValueProvider() {
        return DEFAULT_VALUE_PROVIDER;
    }

    // Javadoc inherited.
    protected Value execute(ExpressionContext expressionContext,
            DevicePolicyValueAccessor accessor, Value defaultValue) {

        return calculateValue(expressionContext, accessor, defaultValue,
                AspectRatioOutputFormat.RATIO);
    }

    /**
     * Retrieve the height and width of the display from the repository in
     * order to calculate the aspect ratio. Return either an individual height
     * or width component as a number, or the entire ratio as a '/' separated
     * string depending on the output format requested.
     * <p/>
     * The returned value will be either:
     * <ul>
     * <li>the requested component of the aspect ratio</li>
     * <li>the default value if the value cannot be calculated</li>
     * <li>an empty value (the empty string or zero depending on the output
     * format requested) if the default value is null</li>
     * </ul>.
     *
     * @param expressionContext     encapsulates all the information required
     *                              to evaluate an expression
     * @param accessor              of device information
     * @param defaultValue          to be used if no value can be retrieved
     * @param outputFormat          the required AspectRatioOutputFormat of the
     *                              returned value, i.e. RATIO, WIDTH, OR HEIGHT
     *
     * @return Value which is the requested component of the aspect ratio either
     * calculated from the repository values, or the default value if the
     * actual value is null, or an empty value ("" or 0 depending on the value
     * type) if the default value is null.
     */
    protected Value calculateValue(ExpressionContext expressionContext,
             DevicePolicyValueAccessor accessor, Value defaultValue,
             AspectRatioOutputFormat outputFormat) {

        Value value = null;
        ExpressionFactory factory = expressionContext.getFactory();

         // calculate the device's display aspect ratio from the repository
        String heightInPixels = accessor.getDependentPolicyValue(
                 DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS);
        String widthInPixels = accessor.getDependentPolicyValue(
                 DevicePolicyConstants.ACTUAL_WIDTH_IN_PIXELS);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Display height and width (from repository) are: " +
                    heightInPixels + " and " + widthInPixels + "respectively");
        }

        if (heightInPixels != null && widthInPixels != null) {
            try {
                int height = new Double(heightInPixels).intValue();
                int width = new Double(widthInPixels).intValue();
                int gcd = findGreatestCommonDenominator(height, width);

                if (outputFormat == AspectRatioOutputFormat.RATIO) {
                    String ratio = "" + (width/gcd) + SEPARATOR + (height/gcd);
                    // return the whole ratio as a string value
                    value = new SimpleStringValue(factory, ratio);
                } else if (outputFormat == AspectRatioOutputFormat.WIDTH) {
                    // return the ratio component as a int value
                    value = new SimpleIntValue(factory, width/gcd);
                } else if (outputFormat == AspectRatioOutputFormat.HEIGHT) {
                    // return the ratio component as a int value
                    value = new SimpleIntValue(factory, height/gcd);
                }
            } catch (NumberFormatException e) {
                // log it but return continue
                String badValue = "One of " + heightInPixels + " or " +
                        widthInPixels;
                String policyNames = "One of "+
                        DevicePolicyConstants.ACTUAL_HEIGHT_IN_PIXELS +
                        " or " + DevicePolicyConstants.ACTUAL_WIDTH_IN_PIXELS;
                LOGGER.info("function-bad-parameter-value",
                        new Object[] {badValue, policyNames});
            }
        }

        if (value == null) {
            // No value could be retrieved from the repository, so verify that
            // the default value is of the correct type.
            if (outputFormat == AspectRatioOutputFormat.RATIO) {
                if (defaultValue instanceof StringValue) {
                    value = defaultValue;
                } else {
                    if (defaultValue != null) {
                        // log the fact that the default was present but wrong
                        LOGGER.info("function-bad-default-value", "StringValue");
                    }
                    value = new SimpleStringValue(factory, "");
                }
            } else if (outputFormat == AspectRatioOutputFormat.WIDTH ||
                    outputFormat == AspectRatioOutputFormat.HEIGHT ) {
                if (defaultValue instanceof IntValue) {
                    value = defaultValue;
                } else {
                    if (defaultValue != null) {
                        // log the fact that the default was present but wrong
                        LOGGER.info("function-bad-default-value", "IntValue");
                    }
                    value = new SimpleIntValue(factory, 0);
                }
            }
        }
        return value;
    }

    // Javadoc inherited.
    protected String getFunctionName() {
        return "cssmq-device-aspect-ratio";
    }

    /**
     * Determine the greatest common denominator of the two parameters passed
     * in.
     *
     * @param a     int whose greatest common denominator with b to find
     * @param b     int whose greatest common denominator with a to find
     * @return the greatest common denominator of a and b
     */
    protected int findGreatestCommonDenominator(int a, int b) {

        while (b != 0) {
            int result = b;
            b = a % b;
            a = result;
        }
        return a;
    }

    /**
     * Typesafe enumeration which indicates the required output format of the
     * aspect ratio.
     */
    protected static class AspectRatioOutputFormat {
        /**
         * int which represents the required output format for the aspect ratio.
         */
        int outputFormat;

        /**
         * Initialize a new instance using the given parameters.
         *
         * @param ratioType
         */
        private AspectRatioOutputFormat(int ratioType) {
            this.outputFormat = ratioType;
        }
        /**
         * Required the aspect ratio as a ratio in the form axb, where a is the
         * width and b the height.
         */
        static final AspectRatioOutputFormat RATIO =
                new AspectRatioOutputFormat(0);

        /**
         * Require just the width component of the aspect ratio.
         */
        static final AspectRatioOutputFormat WIDTH =
                new AspectRatioOutputFormat(1);

        /**
         * Require just the height component of the aspect ratio.
         */
        static final AspectRatioOutputFormat HEIGHT =
                new AspectRatioOutputFormat(2);
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
