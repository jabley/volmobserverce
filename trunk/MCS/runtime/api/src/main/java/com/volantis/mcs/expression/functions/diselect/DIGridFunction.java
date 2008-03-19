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

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.expression.DevicePolicyValueAccessor;
import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.BooleanValue;

/**
 * This Function returns true if the device is grid based (i.e. supports only
 * fixed width fonts) and false if it is bitmap based. If the repository
 * contains no entry for this then the supplied default value will be returned.
 * If this default value is not valid, false will be returned.
 * <p/>
 * NB: This function is not supported and will always return the default or
 * FALSE. 
 */
public class DIGridFunction extends ZeroMandatoryArgumentsExpressionFunction {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(DIGridFunction.class);

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

        Value value = null;

        // this function is currently not supported, so log it and return the
        // default or unknown value
        LOGGER.info("function-unsupported", getFunctionName());

        // verify that the default value is of the correct type
        if (defaultValue instanceof BooleanValue) {
            value = defaultValue;
        } else {
            if (defaultValue != null) {
                // If the default value wasn't of the correct type, then log it
                LOGGER.info("function-bad-default-value", "BooleanValue");
            }
            value = BooleanValue.FALSE;
        }

        return value;
    }

    // Javadoc inherited.
    protected String getFunctionName() {
        return "cssmq-grid";
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
