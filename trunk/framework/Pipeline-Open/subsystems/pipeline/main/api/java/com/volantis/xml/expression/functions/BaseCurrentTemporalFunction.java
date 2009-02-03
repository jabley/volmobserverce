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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Base abstract class for all functions related to retrieving
 * {@link DateValue}, {@link TimeValue} and {@link DateTimeValue} for
 * current date and time.
 * 
 * <p>To specify own current date and time there should be registered
 * as expression context's property, {@link DateTimeValue} under
 * {@link DateTimeValue#class} key. Otherwise new {@link DateTimeValue}
 * instance will be created.</p>
 * 
 * <p>In case when not {@link DateTimeValue} is specified in expression
 * context user may register {@link TimeZone} instance under
 * {@link TimeZone#class} key to specify time zone for newly created
 * {@link DateTimeValue}, {@link DateValue} or {@link TimeValue} instance.</p>
 */
public abstract class BaseCurrentTemporalFunction implements Function {

    /**
     * Used to localize exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    BaseCurrentTemporalFunction.class);

    // javadoc inherited
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        if (arguments.length != 0) {
            throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format(
                            "invalid-num-of-args",
                            new Object[]{getName(), new Integer(0), 
                                    new Integer(arguments.length)}));
        }
        return createReturnValue(context, CurrentDateTimeHelper
                .getCurrentDateTimeValue(context));
    }

    /**
     * Creates {@link Value} to be returned by {@link Function#invoke}.
     * 
     * @param context the expression context
     * @param currentDateTime the {@link DateTimeValue} with current date and
     *                        time
     * @return value to be returned by function
     */
    protected abstract Value createReturnValue(ExpressionContext context,
            DateTimeValue currentDateTime);

    /**
     * Name of function.
     * 
     * @return name of function
     */
    protected abstract String getName();

}
