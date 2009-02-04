/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression.functions.operator;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.atomic.temporal.SimpleDateValue;
import com.volantis.xml.expression.atomic.temporal.DateValue;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Subtracts two DateValue objects.
 */
public class SubtractDatesFunction implements Function {

    /**
     * Used to localize exception messages.
     */
    private final static ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    SubtractDatesFunction.class);

    /**
     * The name of the function.
     */
    public final static String NAME = "subtract-dates";

    /**
     * {@inheritDoc}
     */
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        if (arguments.length != 2) {
            throw new ExpressionException(
                    EXCEPTION_LOCALIZER.format(
                            "invalid-num-args",
                            new Object[] {
                                    NAME,
                                    new Integer(2),
                                    new Integer(arguments.length)
                            }));
        }
        ExpressionFactory factory = context.getFactory();
        SimpleDateValue first = (SimpleDateValue)
                factory.createDateValue(
                        arguments[0].stringValue().asJavaString());
        DateValue second = factory.createDateValue(
                arguments[1].stringValue().asJavaString());
        return first.subtract(second);
    }

}
