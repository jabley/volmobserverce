/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic.temporal;

import com.volantis.xml.expression.atomic.AtomicValue;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Represents an XPath 2.0 dateTime value.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface DateTimeValue extends AtomicValue, TimeValue, DateValue {

    /**
     * Provides the internal "time-lined" value derived from the date/time
     * specification.
     *
     * @return the internal "time-lined" value that can be used to compare
     *         date/times with each other
     */
    double getTimeOnTimeline();

    /**
     * Indicates that the date/time was specified in UTC (if true) or in some
     * other timezone (if false). If false then the time may represent a value
     * including any appropriate daylight savings based on the date and the
     * daylight savings adjustments for that timezone.
     *
     * @return whether the date/time was specified in UTC or not
     */
    boolean isTimezoned();

    /**
     * Returns the timezone for the time.
     *
     * @return the timezone for the time. Will not be null
     */
    TimeZone getTimeZone();

    /**
     * Return the dateTime as a calendar instance. Changes to this calendar
     * will not be reflected in the DateTimeValue.
     *
     * @return a Calendar representing this DateTimeValue.
     */
    Calendar getCalendar();
}
