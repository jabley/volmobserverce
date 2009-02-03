/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * This is a simple, generic implementation of the DateTimeValue interface.
 */
public class SimpleDateTimeValue extends AtomicSequence
        implements DateTimeValueOperations, DateTimeValue {

    /**
     * Used to localize the exception messages
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            SimpleDateTimeValue.class);

    /**
     * The regular expression pattern used to match XML Schema date/time
     * format values and allowing the year, month, day, hours, minutes,
     * seconds, milliseconds and timezone values to be extracted as groups 1
     * through 8 inclusive.
     */
    private static final Pattern PATTERN = Pattern.compile(
            "^(-?[0-9]{4,})-([0-9]{2})-([0-9]{2})" +
            "T([0-9]{2}):([0-9]{2}):([0-9]{2})(?:\\.([0-9]+))?" +
            "(Z?|[+-][0-9]{2}:[0-9]{2})$");

    /**
     * Used to format the seconds and milliseconds. Formaters are not thread
     * safe so do not make this static
     */
    private final NumberFormat SECONDS_FORMATER =
            new DecimalFormat("00.0##",
                              StringValueHelper.DECIMAL_FORMAT_SYMBOLS);

    /**
     * Used to format the years. Not thread safe so do not make it static
     */
    private final NumberFormat YEAR_FORMAT = new DecimalFormat("0000");

    /**
     * Used to format the month and day components of the date. This is not
     * thread safe so do not make it static
     */
    private final NumberFormat MONTH_DAY_FORMAT = new DecimalFormat("00");

    /**
     * The timezone helper is NOT threadsafe so it cannot be static
     */
    private final TimeZoneHelper tzHelper = new TimeZoneHelper();

    /**
     * The year, month, day, hours, minutes and whole seconds components
     */
    private Calendar calendar;

    /**
     * True if this dateTime is timezoned false otherwise
     */
    private boolean timezoned = false;

    /**
     * Initializes the new instance using the given parameters. The date/time
     * is set to the date and time at which the constructor is executed and is
     * associated with the default JRE timezone.
     * 
     * @param factory  the factory that can be used to create new instances of
     *                 other data types without hard-coding the concrete
     *                 classes used
     */ 
    public SimpleDateTimeValue(ExpressionFactory factory) {
        this(factory, (TimeZone)null);
    }

    /**
     * Initializes the new instance using the given parameters. The date/time
     * is set to the date and time at which the constructor is executed and is
     * associated with the default JRE timezone.
     *
     * @param factory the factory that can be used to create new instances of
     *                other data types without hard-coding the concrete classes
     *                used
     */
    public SimpleDateTimeValue(ExpressionFactory factory,
                               TimeZone timezone) {
        super(factory);

        createCalendar(timezone);
    }
    
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory  the factory that can be used to create new instances of
     *                 other data types without hard-coding the concrete
     *                 classes used
     * @param dateTime the fully specified date/time
     */
    public SimpleDateTimeValue(ExpressionFactory factory,
                               Calendar dateTime) {
        super(factory);
        timezoned = true;
        calendar = (Calendar)dateTime.clone();
    }

    /**
     * Initializes the new instance using the given parameters. The dateTime
     * parameter must be a string of the form:
     *
     * <pre>
     * '-'? y*yyyy '-' mm '-' dd 'T' HH ':' MM ':' SS ('.' S+)? (zzzzzz)?
     * </pre>
     *
     * where:
     *
     * <dl>
     *
     * <dt>'-'? y*yyyy</dt>
     *
     * <dd>is a four-or-more digit optionally negative-signed numeral that
     * represents the year; if more than four digits, leading zeros are
     * prohibited, and '0000' is prohibited; also note that a plus sign is not
     * permitted);</dd>
     *
     * <dt>remaining '-'s</dt>
     *
     * <dd>are separators between parts of the date portion;</dd>
     *
     * <dt>mm</dt>
     *
     * <dd>is a two-digit numeral that represents the month;</dd>
     *
     * <dt>dd</dt>
     *
     * <dd>is a two-digit numeral that represents the day;</dd>
     *
     * <dt>'T'</dt>
     *
     * <dd>is a separator indicating that time-of-day follows;</dd>
     *
     * <dt>HH</dt>
     *
     * <dd>is a two-digit numeral that represents the hour; '24' is permitted
     * if the minutes and seconds represented are zero, and the dateTime value
     * so represented is the first instant of the following day (the hour
     * property of a dateTime object in the ·value space· cannot have a value
     * greater than 23);</dd>
     *
     * <dt>':'</dt>
     *
     * <dd>is a separator between parts of the time-of-day portion;</dd>
     *
     * <dt>MM</dt>
     *
     * <dd>is a two-digit numeral that represents the minute;</dd>
     *
     * <dt>SS</dt>
     *
     * <dd>is a two-integer-digit numeral that represents the whole
     * seconds;</dd>
     *
     * <dt>'.' S+</dt>
     *
     * <dd>(if present) represents the fractional seconds;</dd>
     *
     * <dt>zzzzzz</dt>
     *
     * <dd>(if present) represents the timezone (as described below).</dd>
     *
     * </dl>
     *
     * The timezone can be specified as 'Z' to indicate UTC or as:
     *
     * <pre>
     * [-+] hh ':' mm
     * </pre>
     *
     * that defines a GMT offset at hh hours and mm minutes before or after
     * GMT.
     *
     * @param factory  the factory that can be used to create new instances of
     *                 other data types without hard-coding the concrete
     *                 classes used
     * @param dateTime the XML Schema date/time specification string
     */
    public SimpleDateTimeValue(ExpressionFactory factory,
                               String dateTime) {
        super(factory);

        initialize(dateTime);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory  the factory that can be used to create new instances of
     *                 other data types without hard-coding the concrete
     *                 classes used
     * @param year     the year component
     * @param month    the month in year component
     * @param day      the day in month component
     * @param hours    the hours in day component
     * @param minutes  the minutes in hour component
     * @param seconds  the seconds in the minute component
     * @param millis   the milliseconds in the second component
     * @param timezone optionally defines the required timezone. The timezone
     *                 is set to the JRE's default if this is null
     */
    public SimpleDateTimeValue(ExpressionFactory factory,
                               int year,
                               int month,
                               int day,
                               int hours,
                               int minutes,
                               int seconds,
                               int millis,
                               TimeZone timezone) {
        super(factory);

        initialize(year, month, day, hours, minutes,
                   seconds, millis, timezone);
    }

    /**
     * Initializes the members based on the given parameters.
     *
     * @param dateTime the XML Schema date/time format string. Only supports
     *                 numbered (or "custom") timezones (relative to GMT), UTC
     *                 or the default timezone
     * @see #SimpleDateTimeValue(ExpressionFactory, String) for details of the
     *      dateTime format
     */
    private void initialize(String dateTime) {
        // Parse the date, time and timezone from the given string
        Matcher matcher = PATTERN.matcher(dateTime);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                "xs-data-type-invalid",
                new Object[]{"xs:dateTime", dateTime}));
        } else {
            int year = Integer.valueOf(matcher.group(1)).intValue();
            int month = Integer.valueOf(matcher.group(2)).intValue();
            int day = Integer.valueOf(matcher.group(3)).intValue();
            int hours = Integer.valueOf(matcher.group(4)).intValue();
            int minutes = Integer.valueOf(matcher.group(5)).intValue();
            int seconds = Integer.valueOf(matcher.group(6)).intValue();

            String mString = matcher.group(7);
            int millis = parseMillis(mString);
            String timezone = matcher.group(8);
            TimeZone tz = tzHelper.asTimeZone(timezone);
            initialize(year, month, day,
                       hours, minutes, seconds,
                       millis, tz);
        }
    }

    /**
     * Parse the milliseconds component of the time
     *
     * @param mString the string representation of the milliseconds
     * @return the number of milliseconds or 0
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
     * Initializes the members based on the given parameters.
     *
     * @param year         the year component
     * @param month        the month in year component
     * @param day          the day in month component
     * @param hours        the hours in day component
     * @param minutes      the minutes in hour component
     * @param seconds      the whole seconds in the minute component
     * @param milliseconds the milliseconds in the seconds component
     * @param timezone     optionally defines the required timezone. The
     *                     timezone is set to the JRE's default value if this
     *                     is null
     */
    private void initialize(int year,
                            int month,
                            int day,
                            int hours,
                            int minutes,
                            int seconds,
                            int milliseconds,
                            TimeZone timezone) {
        createCalendar(timezone);

        // Handle negative years and adjust the month number as the Calendar
        // class requires
        calendar.set(Math.abs(year), month - 1, day,
                     hours, minutes, (int)seconds);
        calendar.set(Calendar.ERA,
                     ((year < 0) ?
                      GregorianCalendar.BC :
                      GregorianCalendar.AD));

        // Account for fractions of a second
        calendar.set(Calendar.MILLISECOND,
                     milliseconds);
    }

    /**
     * Supporting method that creates the {@link #calendar}, initializing the
     * timezone from the given parameter unless it is null in which case the
     * JRE's default timezone is applied.
     *
     * @param timezone the timezone to be used, or null for the default
     */
    private void createCalendar(TimeZone timezone) {
        if (timezone == null) {
            calendar = GregorianCalendar.getInstance(TimeZone.getDefault());
        } else {
            timezoned = true;
            calendar = GregorianCalendar.getInstance(timezone);
        }
    }

    // javadoc inherited
    public void streamContents(ContentHandler contentHandler)
            throws SAXException, ExpressionException {
        String value = toString();

        contentHandler.characters(value.toCharArray(), 0, value.length());
    }

    // javadoc inherited
    public StringValue stringValue() throws ExpressionException {
        return factory.createStringValue(toString());
    }

    // javadoc inherited
    public synchronized String toString() {
        StringBuffer result = new StringBuffer(32);

        if (calendar.get(Calendar.ERA) == GregorianCalendar.BC) {
            result.append('-');
        }

        result.append(YEAR_FORMAT.format(calendar.get(Calendar.YEAR)));
        result.append('-');
        result.append(MONTH_DAY_FORMAT.format(
            calendar.get(Calendar.MONTH) + 1));
        result.append('-');
        result.append(MONTH_DAY_FORMAT.format(calendar.get(Calendar.DATE)));
        result.append('T');
        result.append(MONTH_DAY_FORMAT.format(
            calendar.get(Calendar.HOUR_OF_DAY)));
        result.append(':');
        result.append(MONTH_DAY_FORMAT.format(calendar.get(Calendar.MINUTE)));
        result.append(':');

        // convert seconds and millis into a double and format that.
        double secs = calendar.get(Calendar.SECOND) +
            calendar.get(Calendar.MILLISECOND) / 1000d;
        result.append(SECONDS_FORMATER.format((secs)));

        // returns the timezone as a string or an empty string
        if (isTimezoned()) {
            String tz = tzHelper.asString(calendar);
            result.append(tz);
        }

        return result.toString();
    }

    // javadoc inherited
    public int getYear() {
        // Handle re-constructing negative years from the Calendar state
        int year = calendar.get(Calendar.YEAR);

        if (calendar.get(Calendar.ERA) == GregorianCalendar.BC) {
            year = -year;
        }

        return year;
    }

    // javadoc inherited
    public int getMonth() {
        // Adjust month numbers into the XML Schema value space from the
        // Calendar class values
        return calendar.get(Calendar.MONTH) + 1;
    }

    // javadoc inherited
    public int getDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    // javadoc inherited
    public int getHours() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    // javadoc inherited
    public int getMinutes() {
        return calendar.get(Calendar.MINUTE);
    }

    // javadoc inherited
    public int getSeconds() {
        return calendar.get(Calendar.SECOND);
    }

    public int getMilliseconds() {
        return calendar.get(Calendar.MILLISECOND);
    }

    // javadoc inherited
    public boolean isTimezoned() {
        return timezoned;
    }

    // javadoc inherited
    public TimeZone getTimeZone() {
        TimeZone result = null;
        if(isTimezoned()) {
            result = calendar.getTimeZone();
        }
        return result;
    }

    // javadoc inherited
    public double getTimeOnTimeline() {
        return asJavaDouble();
    }

    // javadoc inherited
    public double asJavaDouble() {
        return asJavaLong() / 1000.0;
    }

    // javadoc inherited
    public long asJavaLong() {
        return calendar.getTimeInMillis();
    }

    // javadoc inherited
    public DateTimeValue add(DurationValue duration) {
        // creating a new instance clones the calendar
        SimpleDateTimeValue dtv  = new SimpleDateTimeValue(factory, getCalendar());
        // so add duration to the newly created DateTimeValue.
        dtv.calendar.add(Calendar.YEAR, duration.getYears());
        dtv.calendar.add(Calendar.MONTH, duration.getMonths());
        dtv.calendar.add(Calendar.DATE, duration.getDays());
        dtv.calendar.add(Calendar.HOUR, duration.getHours());
        dtv.calendar.add(Calendar.MINUTE, duration.getMinutes());
        dtv.calendar.add(Calendar.SECOND, duration.getSeconds());
        dtv.calendar.add(Calendar.MILLISECOND, duration.getMilliseconds());
        return dtv;
    }

    // javadoc inherited
    public boolean lessThan(DateTimeValue dateTime) {
        return PipelineExpressionHelper.compare(this, dateTime) == -1;
    }

    // javadoc inherited
    public boolean greaterThan(DateTimeValue dateTime) {
        return PipelineExpressionHelper.compare(this, dateTime) == 1;
    }

    /**
     * Subtract the supplied dateTime value from this dateTime value returning
     * the result as a DurationValue.
     *
     * @param dateTime the date and time to subtract from this date and
     * time
     * @return a Duration
     */
    public DurationValue subtract(DateTimeValue dateTime) {
        // todo later add the ability to handle DateTimeValues that are not
        // SimpleDateTimeValues
        SimpleDateTimeValue dateTimeValue = (SimpleDateTimeValue) dateTime;
        long millis = calendar.getTimeInMillis() -
            dateTimeValue.calendar.getTimeInMillis();
        return factory.createDurationValue(millis);
    }

    // javadoc inherited
    public Calendar getCalendar() {
        return (Calendar) calendar.clone();
    }
}

