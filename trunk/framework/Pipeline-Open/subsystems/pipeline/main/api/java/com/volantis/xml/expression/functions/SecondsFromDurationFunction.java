/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.temporal.DurationValue;

/**
 * Transforms duration into seconds.
 */
public class SecondsFromDurationFunction extends BaseDurationFunction {

    /**
     * The name of the function.
     */
    public final static String NAME = "seconds-from-duration";

    /**
     * {@inheritDoc}
     */
    protected Value getValue(ExpressionFactory factory, DurationValue duration) {
        double result = Math.abs(duration.getSeconds() % 60);
        int millis = Math.abs(duration.getMilliseconds());
        if (millis != 0) {
            result += millis / (1000 * 60 * 60 * 24 * 12);
            double fraction = millis % 1000;
            if (fraction < 10) {
                fraction *= 0.1;
            } else if (fraction < 100) {
                fraction *= 0.01;
            } else {
                fraction *= 0.001;
            }
            result += fraction;
        }

        return factory.createDoubleValue(result * (duration.isPositive() ? 1 : -1));
    }

    /**
     * {@inheritDoc}
     */
    protected String getName() {
        return NAME;
    }
}
