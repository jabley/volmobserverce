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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-empty">fn:time</a>.
 */ 
public class TimeFunction implements Function {

    /**
     * Constant defining the name under which the function is assumed to be
     * registered. The namespace for this function isn't important in the
     * implementation.
     */
    public static final String NAME = "time";

    /**
     * Used for localizing exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(TimeFunction.class);


    // javadoc inherited
    public Value invoke(ExpressionContext expressionContext, Value[] values)
        throws ExpressionException {

        if (values.length != 1) {
            throw new ExpressionException(EXCEPTION_LOCALIZER.format(
                "invalid-num-of-args",
                new Object[]{
                    NAME,
                    new Integer(1),
                    new Integer(values.length)}));
        }
        Value result = Sequence.EMPTY;
        if(values[0].getSequence() != Sequence.EMPTY) {
            String time = values[0].stringValue().asJavaString();
            result = expressionContext.getFactory().createTimeValue(time);
        }
        return result;
    }
}
