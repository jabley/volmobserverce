/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2006. All rights reserved
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic.temporal;

/**
 * Internal API exposing the operations that Durations can have performed on
 * them.
 */
public interface DurationValueOperations extends DurationValue {

    /**
     * Add the specified duration to the date and time represented by this
     * value and return the result as a new DateTimeValue
     *
     * @param duration the duration to add to this dateTime
     * @return a new value that represents the duration added to this
     *         date time.
     */
    DurationValue add(DurationValue duration);

    /**
     * Return true if this is less than the provided value
     *
     * @param value the value to compare this against
     * @return true if this is less than the provided value
     */
    boolean lessThan(DurationValue value);

    /**
     * Return true if this is greater than the provided value
     *
     * @param value the date time to compare this against
     * @return true if this is greater than the provided value
     */
    boolean greaterThan(DurationValue value);

    /**
     * Return the duration value representing the difference between this
     * value and the specified value
     *
     * @param value the value to subtract from this value
     * @return a DurationValue representing the difference between the two
     *         dateTimeValues
     */
    DurationValue subtract(DurationValue value);
}
