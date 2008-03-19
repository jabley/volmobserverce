/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic.temporal;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.atomic.AtomicSequence;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.StringValueHelper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
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
public class SimpleTimeValue extends AtomicSequence implements TimeValue {

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

    // javadoc inherited
    public StringValue stringValue() throws ExpressionException {
        return factory.createStringValue(toString());
    }

    // javadoc inherited
    public void streamContents(ContentHandler contentHandler)
        throws ExpressionException, SAXException {
        char[] chars = toString().toCharArray();

        contentHandler.characters(chars, 0, chars.length);
    }

    // javadoc inherited
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


    // javadoc inherited
    public int getHours() {
        return hours;
    }

    // javadoc inherited
    public int getMinutes() {
        return minutes;
    }

    // javadoc inherited
    public int getSeconds() {
        return seconds;
    }

    // javadoc inherited
    public int getMilliseconds() {
        return millis;
    }

    // javadoc inherited
    public boolean isTimezoned() {
        return timeZone != null;
    }

    // javadoc inherited
    public TimeZone getTimeZone() {
        return timeZone;
    }
}
