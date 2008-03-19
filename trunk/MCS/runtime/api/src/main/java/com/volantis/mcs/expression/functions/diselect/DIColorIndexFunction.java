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
import com.volantis.xml.expression.atomic.numeric.NumericValue;
import com.volantis.xml.expression.atomic.numeric.SimpleIntValue;

/**
 * This Function retrieves the number of entries in the colour lookup table for
 * the device making the request. If the repository contains no entry for this
 * then the supplied default value will be returned. If this default value is
 * not valid, zero will be returned.
 */
public class DIColorIndexFunction extends ZeroMandatoryArgumentsExpressionFunction {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(DIColorIndexFunction.class);

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
            DevicePolicyValueAccessor accessor,Value defaultValue) {
        Value value = null;
        ExpressionFactory factory = expressionContext.getFactory();

        // determine the size of the colour palette
        String palette = accessor.getDependentPolicyValue(
                DevicePolicyConstants.PALETTE);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Size of colour palette (from repository):" +
                    palette);
        }

        try {
            value = factory.createIntValue(Integer.parseInt(palette));
        } catch (NumberFormatException e) {
            // log it but continue
            LOGGER.info("function-bad-parameter-value",
                    new Object[] {palette, DevicePolicyConstants.PALETTE});
        }

        if (value == null) {
            // Verify that the default value is of the correct type
            if (defaultValue instanceof NumericValue) {
                value = defaultValue;
            } else {
                if (defaultValue != null) {
                    // log the fact that the default was present but wrong type
                    LOGGER.info("function-bad-default-value", "NumericValue");
                }
                value = new SimpleIntValue(factory, 0);
            }
        }

        return value;
    }

    // Javadoc inherited.
    protected String getFunctionName() {
        return "cssmq-color-index";
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
