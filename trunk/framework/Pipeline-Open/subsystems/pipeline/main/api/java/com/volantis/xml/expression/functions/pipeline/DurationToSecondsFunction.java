/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2008. All rights reserved
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions.pipeline;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;
import com.volantis.xml.expression.atomic.temporal.DurationValue;
import com.volantis.xml.expression.atomic.temporal.SimpleDateTimeValue;
import com.volantis.xml.expression.functions.CurrentDateTimeHelper;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * Converts duration to number of seconds against current date/time.
 */
public class DurationToSecondsFunction implements Function {

    /**
     * Used to localize exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = 
        LocalizationFactory.createExceptionLocalizer(
                DurationToSecondsFunction.class);  
    
    /**
     * Name of the function.
     */
    private static final String NAME = "duration-to-seconds";
    
    // javadoc inherited
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        if (arguments.length != 1) {
            throw new IllegalArgumentException(
                    EXCEPTION_LOCALIZER.format(
                            "invalid-num-of-args",
                            new Object[]{NAME, new Integer(1), 
                                    new Integer(arguments.length)}));
        } 
        
        Value result = Sequence.EMPTY;
        if (arguments[0].getSequence() != Sequence.EMPTY) {
            DurationValue duration = context.getFactory().createDurationValue(
                    arguments[0].stringValue().asJavaString()); 
            DateTimeValue dateTime = CurrentDateTimeHelper
                    .getCurrentDateTimeValue(context);
            DateTimeValue newDateTime = ((SimpleDateTimeValue) dateTime)
                    .add(duration);
            long seconds = (newDateTime.getCalendar().getTimeInMillis() - dateTime
                    .getCalendar().getTimeInMillis()) / 1000;
            result = context.getFactory().createIntValue((int) seconds);
        }
        return result;
    }
}
