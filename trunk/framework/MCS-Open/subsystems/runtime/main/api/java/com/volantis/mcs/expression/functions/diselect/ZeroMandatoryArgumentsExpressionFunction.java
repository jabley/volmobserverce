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
import com.volantis.mcs.expression.functions.AbstractExpressionFunction;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * This is a general class for handling expression functions that have no
 * mandatory arguments, but can have an optional default value argument
 */
public abstract class ZeroMandatoryArgumentsExpressionFunction
        extends AbstractExpressionFunction {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(
                    ZeroMandatoryArgumentsExpressionFunction.class);

    /**
     * A generic implementation of Function.invoke that interprets any
     * default value passed in and delegates to the specific expression
     * function implementations.
     *
     * @param expressionContext     encapsulates all the information required
     *                              to evaluate an expression
     * @param values                Value array which should either be empty or
     *                              contain a single default Value
     * @return The Value returned by invoking the function.
     * @throws ExpressionException If there is a problem with the expression or
     * function invocation.
     */
    public Value invoke(ExpressionContext expressionContext, Value[] values)
            throws ExpressionException {

        // Extract the DevicePolicyValueAccessor for the current device.
        DevicePolicyValueAccessor accessor =
                (DevicePolicyValueAccessor) expressionContext.getProperty(
                        DevicePolicyValueAccessor.class);

        Value defaultValue = Sequence.EMPTY;

        if (values != null && values.length != 0) {
            if (values.length > 1) {
                LOGGER.warn("too-many-arguments",
                        new Object[]{getFunctionName()});
            } else {
                defaultValue = getDefaultValueProvider().provideDefaultValue(
                        values[0]);
            }
        }
        return execute(expressionContext, accessor, defaultValue);
    }

    /**
     * Execute the specific, zero argument, function.
     *
     * @param expressionContext The ExpressionContext used by this function
     * @param accessor          of device information
     * @param defaultValue      Value to return if the parameter does not exist
     * @return The Value returned by the execution of the function.
     */
    protected abstract Value execute(ExpressionContext expressionContext,
            DevicePolicyValueAccessor accessor, Value defaultValue);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 ===========================================================================
*/
