/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.temporal.DurationValue;

/**
 * Transforms duration into minutes.
 */
public class MinutesFromDurationFunction extends BaseDurationFunction {

    /**
     * The name of the function.
     */
    public final static String NAME = "minutes-from-duration";

    /**
     * {@inheritDoc}
     */
    protected Value getValue(ExpressionFactory factory, DurationValue duration) {
        int result = Math.abs(duration.getMinutes() % 60);
        result += Math.abs(duration.getSeconds() / (60 * 60));
        result += Math.abs(duration.getMilliseconds() / (1000 * 60 * 60));
        return factory.createIntValue(result * (duration.isPositive() ? 1 : -1));
    }

    /**
     * {@inheritDoc}
     */
    protected String getName() {
        return NAME;
    }
}
