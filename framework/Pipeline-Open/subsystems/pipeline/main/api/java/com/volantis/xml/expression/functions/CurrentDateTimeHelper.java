/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2008. All rights reserved
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.temporal.DateTimeValue;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * This class is responsible for obtaining current date/time. 
 */
public class CurrentDateTimeHelper {

    /**
     * Gets {@link DateTimeValue} for current date and time.
     * 
     * <p>This method is checking, if there is registered in expression context
     * an {@link DateTimeValue} under {@link DateTimeValue#class} key. If yes,
     * then this {@link Value} is returned. Otherwise new {@link DateTimeValue}
     * is created using expression factory, with current {@link Calendar}.
     * In such case there is also checked, if {@link TimeZone} instance is
     * registered as expression context property under {@link TimeZone#class}
     * key. If yes, then it's used while creating new calendar.</p>
     * 
     * @param context the expression context to be used to create new
     *                {@link DateTimeValue} instance
     * @return new date time value for current date and time
     */
    public static DateTimeValue getCurrentDateTimeValue(
            ExpressionContext context) {
        DateTimeValue value = (DateTimeValue) context.getProperty(
                DateTimeValue.class);
        if (value == null) {
            final TimeZone timeZone = (TimeZone) context.getProperty(
                    TimeZone.class);
            final Calendar calendar;
            if (timeZone != null) {
                calendar = Calendar.getInstance(timeZone);
            } else {
                calendar = Calendar.getInstance();
            }
            value = context.getFactory().createDateTimeValue(calendar);
        }
        return value;
    }    
}
