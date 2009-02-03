/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2006. All rights reserved
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic.temporal;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.atomic.AtomicSequence;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.StringValueHelper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Simple implementation of the xs:time type.
 *
 * @see <a href="http://www.w3.org/TR/xmlschema-2/#time">
 * http://www.w3.org/TR/xmlschema-2/#time</a>
 */
public class SimpleTimeValue extends AtomicSequence
        implements TimeValueOperations, TimeValue {

    /**
     * Localize Exception messages
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(SimpleTimeValue.class);

    /**
     * This is not thread safe so do not make it static
     */
    private final TimeZoneHelper tzHelper = new TimeZoneHelper();

    /**
     * Used to format the hours, minutes parts of the time
     * (all require 2 digits). DecimalFormat is not thread safe so
     * create one per instance.
     */
    private final NumberFormat NUMBER_FORMAT = new DecimalFormat("00");

    /**
     * Used to format the seconds and milliseconds part of the time.
     * DecimalFormat is not thread safe so do not make it static
     */
    private final DecimalFormat SECONDS_FORMAT =
        new DecimalFormat("00.0######",
                          StringValueHelper.DECIMAL_FORMAT_SYMBOLS);

    /**
     * The pattern used to parse the time.
     * Stores the hours, minutes and seconds into groups 1, 2 and 3
     * respectively. The timezone is in group 4 or it is empty
     */
    private static final Pattern PATTERN = Pattern.compile(
        "^([0-9]{2}):([0-9]{2}):([0-9]{2})(?:\\.([0-9]*))?" +
        "(Z?|[+-][0-9]{2}:[0-9]{2})");

    /**
     * The hour
     */
    private int hours = 0;

    /**
     * The minute
     */
    private int minutes = 0;

    /**
     * The second
     */
    private int seconds = 0;

    /**
     * The milliseconds
     */
    private int millis = 0;

    /**
     * The timeZone if any
     */
    private TimeZone timeZone = null;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory the factory that created this SimpleTimeValue
     * @param value the string representation of the Time.
     */
    public SimpleTimeValue(ExpressionFactory factory, String value) {
        super(factory);
        parseString(value);
    }
    
    /**
     * Initializes the new instance using the given parameters.
     * 
     * @param factory the factory that created this SimpleTimeValue
     * @param value the {@link Calendar} representation of the Time.
     */
    public SimpleTimeValue(ExpressionFactory factory, Calendar value) {
        super(factory);
        parseCalendar(value);
    }

    /**
     * Initializes the new instance using the given parameters (which must
     * have positive values).
     *
     * @param factory the expression factory that created this instance
     * @param hours the number of hours
     * @param minutes the number of minutes
     * @param seconds the number of seconds
     * @param millis the number of milliseconds
     */
    public SimpleTimeValue(ExpressionFactory factory, int hours, int minutes,
                           int seconds, int millis) {
        super(factory);

        this.hours = checkPositive(hours, "hours");
        this.minutes = checkPositive(minutes, "minutes");
        this.seconds = checkPositive(seconds, "seconds");
        this.millis = checkPositive(millis, "milliseconds");
    }

    /**
     * Return the parameter if it is positive. Throw an exception otherwise
     *
     * @param value the value to check
     * @return the value if it is positive.
     */
    private int checkPositive(int value, String paramName) {
        if (value < 0) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                "illegal-method-argument-not-positive", paramName));
        }

        return value;
    }

    /**
     * Parse the {@link Calendar} representation of the Time.
     * 
     * @param value the calendar with time to be parsed
     */
    private void parseCalendar(Calendar value) {
        hours = value.get(Calendar.HOUR_OF_DAY);
        minutes = value.get(Calendar.MINUTE);
        seconds = value.get(Calendar.SECOND);
        millis = value.get(Calendar.MILLISECOND);
        timeZone = value.getTimeZone();
    }

    /**
     * Parse the string representation of the time and store it within this
     * instance's attributes.
     *
     * @param value the time string to be parsed
     */
    private void parseString(String value) {
        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("xs-data-type-invalid",
                                           new Object[]{"xs:time", value}));
        } else {
            hours = Integer.valueOf(matcher.group(1)).intValue();
            minutes = Integer.valueOf(matcher.group(2)).intValue();
            seconds = Integer.valueOf(matcher.group(3)).intValue();
            // convert to milliseconds by multiplying by 100
            String mString = matcher.group(4);
            millis = parseMillis(mString);

            String timezone = matcher.group(5);
            timeZone = tzHelper.asTimeZone(timezone);
        }
    }

    /**
     * Parse the milliseconds component of the time
     *
     * @param mString the string representation of the milliseconds
     * @return the milliseconds component as an integer
     */
    private int parseMillis(String mString) {
        int result = 0;

        if (mString != null && !"".equals(mString)) {
            mString = "0." + mString;
            double ms = Double.valueOf(mString).doubleValue();
            result = (int) Math.round(ms * 1000);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public StringValue stringValue() throws ExpressionException {
        return factory.createStringValue(toString());
    }

    /**
     * {@inheritDoc}
     */
    public void streamContents(ContentHandler contentHandler)
        throws ExpressionException, SAXException {
        char[] chars = toString().toCharArray();

        contentHandler.characters(chars, 0, chars.length);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(NUMBER_FORMAT.format(hours)).append(':');
        buffer.append(NUMBER_FORMAT.format(minutes)).append(':');
        double secs = seconds + millis / 1000.0;
        buffer.append(SECONDS_FORMAT.format(secs));

        if (isTimezoned()) {
            buffer.append(tzHelper.asString(timeZone));
        }
        
        return buffer.toString();
    }

    /**
     * {@inheritDoc}
     */
    public int getHours() {
        return hours;
    }

    /**
     * {@inheritDoc}
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * {@inheritDoc}
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * {@inheritDoc}
     */
    public int getMilliseconds() {
        return millis;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isTimezoned() {
        return timeZone != null;
    }

    /**
     * {@inheritDoc}
     */
    public TimeZone getTimeZone() {
        return timeZone;
    }

    /**
     * {@inheritDoc}
     */
    public TimeValue add(DurationValue duration) {
        Calendar calendar = GregorianCalendar.getInstance(timeZone);
        calendar.set(Calendar.HOUR, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, millis);
        calendar.add(Calendar.HOUR, duration.getHours());
        calendar.add(Calendar.MINUTE, duration.getMinutes());
        calendar.add(Calendar.SECOND, duration.getSeconds());
        calendar.add(Calendar.MILLISECOND, duration.getMilliseconds());
        return factory.createTimeValue(calendar);
    }

    /**
     * {@inheritDoc}
     */
    public boolean lessThan(TimeValue value) {
        return PipelineExpressionHelper.compare(this, value) == -1;
    }

    /**
     * {@inheritDoc}
     */
    public boolean greaterThan(TimeValue value) {
        return PipelineExpressionHelper.compare(this, value) == 1;
    }

    /**
     * {@inheritDoc}
     */
    public DurationValue subtract(TimeValue value) {
        SimpleTimeValue tv = (SimpleTimeValue)value;
        Calendar first = GregorianCalendar.getInstance(timeZone);
        first.set(Calendar.HOUR, hours);
        first.set(Calendar.MINUTE, minutes);
        first.set(Calendar.SECOND, seconds);
        first.set(Calendar.MILLISECOND, millis);
        Calendar second = GregorianCalendar.getInstance(tv.timeZone);
        second.set(Calendar.HOUR, tv.hours);
        second.set(Calendar.MINUTE, tv.minutes);
        second.set(Calendar.SECOND, tv.seconds);
        second.set(Calendar.MILLISECOND, tv.millis);
        long result = first.getTimeInMillis() -
                second.getTimeInMillis();
        return factory.createDurationValue(result);
    }
}
