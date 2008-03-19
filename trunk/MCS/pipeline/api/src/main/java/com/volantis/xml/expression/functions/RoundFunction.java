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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.expression.functions;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;

/**
 * The round function returns the number that is closest to the argument 
 * and that is an integer.
 *
 * <ul>
 *
 * <li>If there are two such numbers, then the one that is closest to
 * positive infinity is returned.</li>
 *
 * <li>If the argument is NaN, then NaN is returned.</li>
 *
 * <li>If the argument is positive infinity, then positive infinity is
 * returned.</li>
 *
 * <li>If the argument is negative infinity, then negative infinity is
 * returned.</li>
 *
 * <li>If the argument is positive zero, then positive zero is returned.</li>
 *
 * <li>If the argument is negative zero, then negative zero is returned.</li>
 *
 * <li>If the argument is less than zero, but greater than or equal to -0.5,
 * then negative zero is returned.</li>
 *
 * </ul>
 *
 * See <a href="http://www.w3schools.com/vbscript/func_round.asp">
 * round(number)</a>.
 *
 * <p>The result is always a double value to be consistent with XPath.</p>
 */
public class RoundFunction implements Function {
    /**
     * Used for localizing exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    RoundFunction.class);

    // Javadoc inherited.
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {

        if (arguments.length != 1){
            throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format(
                            "invalid-num-of-args",
                            new Object[]{
                                new Integer(arguments.length),
                                new Integer(1)}));
        }

        Value val = arguments[0];

        ExpressionFactory factory = context.getFactory();

        if (val instanceof IntValue){
            val = factory.createDoubleValue(((IntValue)val).asJavaInt());
        } else if (val instanceof DoubleValue){
            double value = ((DoubleValue)val).asJavaDouble();

            val = factory.createDoubleValue(Math.round(value));
        } else {
            String string = val.stringValue().asJavaString();

            try {
                double value = Double.parseDouble(string);

                val = factory.createDoubleValue(Math.round(value));
            } catch (NumberFormatException e){
                throw new ExpressionException(e);
            }
        }

        return val;
    }
}
