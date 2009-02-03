/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.temporal.DurationValue;

/**
 * Test case for xs:*-from-duration() functions.
 */
public class EntitiesFromDurationTestCase extends FunctionTestAbstract {

    /**
     * Test for xs:years-from-duration() function.
     *
     * @throws Exception thrown by tested function
     */
    public void testYearsFromDuration() throws Exception {
        final Function function = new YearsFromDurationFunction();
        DurationValue duration;
        Value result;

        duration =
                factory.createDurationValue(true, 20, 15, 0, 0, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(21));

        duration =
                factory.createDurationValue(false, 0, 15, 0, 0, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(-1));

        duration =
                factory.createDurationValue(false, 0, 0, 2, 15, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(0));
    }

    /**
     * Test for xs:months-from-duration() function.
     *
     * @throws Exception thrown by tested function
     */
    public void testMonthsFromDuration() throws Exception {
        final Function function = new MonthsFromDurationFunction();
        DurationValue duration;
        Value result;

        duration =
                factory.createDurationValue(true, 20, 15, 0, 0, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(3));

        duration =
                factory.createDurationValue(false, 20, 18, 0, 0, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(-6));

        duration =
                factory.createDurationValue(false, 0, 0, 2, 15, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(0));
    }

    /**
     * Test for xs:days-from-duration() function.
     *
     * @throws Exception thrown by tested function
     */
    public void testDaysFromDuration() throws Exception {
        final Function function = new DaysFromDurationFunction();
        DurationValue duration;
        Value result;

        duration =
                factory.createDurationValue(true, 0, 0, 3, 10, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(3));

        duration =
                factory.createDurationValue(true, 0, 0, 3, 55, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(5));

        duration =
                factory.createDurationValue(false, 3, 5, 0, 0, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(0));
    }

    /**
     * Test for xs:hours-from-duration() function.
     *
     * @throws Exception thrown by tested function
     */
    public void testHoursFromDuration() throws Exception {
        final Function function = new HoursFromDurationFunction();
        DurationValue duration;
        Value result;

        duration =
                factory.createDurationValue(true, 0, 0, 3, 10, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(10));

        duration =
                factory.createDurationValue(true, 0, 0, 3, 12, 32, 12, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(12));

        duration =
                factory.createDurationValue(true, 0, 0, 0, 123, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(3));

        duration =
                factory.createDurationValue(false, 0, 0, 3, 10, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(-10));
    }

    /**
     * Test for xs:minutes-from-duration() function.
     *
     * @throws Exception thrown by tested function
     */
    public void testMinutesFromDuration() throws Exception {
        final Function function = new MinutesFromDurationFunction();
        DurationValue duration;
        Value result;

        duration =
                factory.createDurationValue(true, 0, 0, 3, 10, 0, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(0));

        duration =
                factory.createDurationValue(false, 0, 0, 5, 12, 30, 0, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createIntValue(-30));
    }

    /**
     * Test for xs:seconds-from-duration() function.
     *
     * @throws Exception thrown by tested function
     */
    public void testSecondsFromDuration() throws Exception {
        final Function function = new SecondsFromDurationFunction();
        DurationValue duration;
        Value result;

        duration =
                factory.createDurationValue(true, 0, 0, 3, 10, 0, 12, 5);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createDoubleValue(12.5));

        duration =
                factory.createDurationValue(false, 0, 0, 0, 0, 0, 256, 0);
        result = function.invoke(
                expressionContextMock,
                new Value[] { duration });
        assertEquals(result, factory.createDoubleValue(-16.0));
    }

}
