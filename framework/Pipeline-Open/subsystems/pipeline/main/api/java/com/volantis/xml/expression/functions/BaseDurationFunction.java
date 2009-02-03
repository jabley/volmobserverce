/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.atomic.temporal.DurationValue;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.pipeline.localization.LocalizationFactory;

/**
 * Abstract base of all fn:*-from-duration functions.
 */
public abstract class BaseDurationFunction implements Function {

    /**
     * Used to localize exception messages.
     */
    private final static ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    BaseDurationFunction.class);

    /**
     * {@inheritDoc}
     */
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {

        if (arguments.length > 1) {
            throw new ExpressionException(
                    EXCEPTION_LOCALIZER.format(
                            "invalid-num-args-range",
                            new Object[] {
                                    getName(),
                                    new Integer(0),
                                    new Integer(1),
                                    new Integer(arguments.length)
                            }));
        }

        Value result;
        if (arguments.length == 0) {
            result = Sequence.EMPTY;
        } else {
            ExpressionFactory factory = context.getFactory();

            DurationValue duration = factory.createDurationValue(
                    arguments[0].stringValue().asJavaString());
            result = getValue(factory, duration);
        }
        return result;
    }

    /**
     * Calculates the value of the function based on the duration
     * passed as a parameter.
     *
     * @param factory expression factory
     * @param duration passed duration
     * @return the calculated value
     */
    protected abstract Value getValue(
            ExpressionFactory factory,
            DurationValue duration);

    /**
     * Returns the name of the function.
     *
     * @return the function name
     */
    protected abstract String getName();

}
