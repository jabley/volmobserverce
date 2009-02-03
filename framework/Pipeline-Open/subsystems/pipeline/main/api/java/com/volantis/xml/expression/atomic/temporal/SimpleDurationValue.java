/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2006. All rights reserved
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic.temporal;

import com.volantis.xml.expression.atomic.AtomicSequence;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.pipeline.localization.LocalizationFactory;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.DecimalFormat;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Represent the xs:duration element.
 *
 * For further information see
 * <a href="http://www.w3.org/TR/xmlschema-2/#duration">
 * http://www.w3.org/TR/xmlschema-2/#duration</a>
 */
public class SimpleDurationValue extends
    AtomicSequence implements DurationValueOperations, DurationValue {

    /**
     * Provides the DateTimeValue constants used for duration comparisons.
     */
    private static class FixedDateTimeValue extends SimpleDateTimeValue {
        /**
         * Initializes the new instance using the given parameters.
         *
         * @param dateTime the XML Schema lexical dateTime representation
         */
        FixedDateTimeValue(String dateTime) {
            super(null, dateTime);
        }

        /**
         * {@inheritDoc}
         */
        public StringValue stringValue() throws ExpressionException {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * The base DateTimes to use for duration evaluation.
     */
    private static final DateTimeValue[] DATE_TIME_BASES =
            new DateTimeValue[] {
                    new FixedDateTimeValue("1696-09-01T00:00:00Z"),
                    new FixedDateTimeValue("1697-02-01T00:00:00Z"),
                    new FixedDateTimeValue("1903-03-01T00:00:00Z"),
                    new FixedDateTimeValue("1903-07-01T00:00:00Z")
            };

    /**
     * Used to localize exception messages
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            SimpleDurationValue.class);

    /**
     * The regular expression pattern used to match XML Schema duration format
     * values and allowing the sign, years, months, days, to be matched in
     * groups 1-4 inclusive. Group five contains any content after the "T" time
     * delimiter.
     */
    private static final Pattern DATE_PATTERN =
        Pattern.compile("^([-]?)P(?:([0-9]+)Y)?(?:([0-9]+)M)?" +
                        "(?:([0-9]+)D)?(?:T(.*))?$");

    /**
     * The regular expression pattern used to match the Time portion XML
     * Schema duration format values and allowing the Hours, Minutes and 
     * Seconds and fractions of a second to be matched in groups 1-4 inclusive.
     */
    private static final Pattern TIME_PATTERN =
        Pattern.compile("^(?:([0-9]+)H)?(?:([0-9]+)M)?" +
                        "(?:([0-9]+)(?:\\.([0-9]+)?)?S)?$");

    /**
     * The format for writing out the components of the Duration. The
     * components must have at least 1 character even if it is "0"
     */
    private final DecimalFormat NUMBER_FORMAT = 
        new DecimalFormat("###########0");

    /**
     * The average number of days in a month
     */
    private static final double AVERAGE_DAYS_IN_MONTH = 365.25/12;

    /**
     * The number of seconds in a minute
     */
    private static final long MINUTES_MULTIPLIER = 60;

    /**
     * The number of seconds in an hour
     */
    private static final long HOURS_MULTIPLIER = 60 * MINUTES_MULTIPLIER;

    /**
     * the number of seconds in a day
     */
    private static final long DAYS_MULTIPLIER = HOURS_MULTIPLIER * 24;

    /**
     * The number of seconds in a "standard" month
     */
    private static final double MONTHS_MULTIPLIER =
        AVERAGE_DAYS_IN_MONTH * DAYS_MULTIPLIER;

    /**
     * The number of seconds in a YEAR
     */
    private static final double YEARS_MULTIPLIER = 12 * MONTHS_MULTIPLIER;

    /**
     * The number of seconds in a Millisecond
     */
    private static final double MILLIS_MULTIPLIER = 1.0 / 1000.0;


    /**
     * The sign of the duration
     */
    private boolean positive = true;

    /**
     * Absolute number of years
     */
    private int years = 0;

    /**
     * Absolute number of months
     */
    private int months = 0;

    /**
     * Absolute number of days
     */
    private int days = 0;

    /**
     * Absolute number of hours
     */
    private int hours = 0;

    /**
     * Absolute number of minutes
     */
    private int minutes = 0;

    /**
     * Absolute number of seconds
     */
    private int seconds = 0;

    /**
     * Absolute numbre of milliseconds
     */
    private int millis = 0;

    /**
     * The local cached copies of the date times used for comparison.
     */
    private DateTimeValue[] localDateTimes;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory  the expression factory which this instance should use
     * @param duration the XML Schema lexical duration representation
     */
    public SimpleDurationValue(ExpressionFactory factory, String duration) {
        super(factory);
        parseString(duration);
    }


    /**
     * Initialize the Duration using the explicitly provided values for
     * each component of the duration. None of the arguments may be negative
     *
     * @param factory  the expression factory which this instance should use
     * @param positive whether the duration is positive or negative
     * @param years    the years component
     * @param months   the months component
     * @param days     the days component
     * @param hours    the hours component
     * @param minutes  the minutes component
     * @param seconds  the seconds component
     * @param millis   the milliseconds component
     */
    public SimpleDurationValue(ExpressionFactory factory,
                               boolean positive, int years,
                               int months, int days,
                               int hours, int minutes,
                               int seconds, int millis) {
        super(factory);

        this.positive = positive;
        this.years = checkInt(years);
        this.months = checkInt(months);
        this.days = checkInt(days);
        this.hours = checkInt(hours);
        this.minutes = checkInt(minutes);
        this.seconds = checkInt(seconds);
        this.millis = checkInt(millis);
    }

    /**
     * Initialize the Duration using the explicitly provided values for each
     * component of the duration. None of the arguments may be negative
     *
     * @param factory the expression factory which this instance should use
     * @param millis the duration in milliseconds
     */
    public SimpleDurationValue(ExpressionFactory factory, long millis) {
        super(factory);

        if (millis < 0) {
            this.positive = false;
            millis = -millis;
        }

        // figure out how many seconds are neccessary to represent the millis
        long tmpSeconds = 0;

        if (millis > 999) {
            tmpSeconds = millis / 1000;
            millis = millis % 1000;
        }

        // check if we can preserve accurate duration as seconds and millis
        // if not, then do imprecise conversion to years, seconds and millis
        if (tmpSeconds > Integer.MAX_VALUE) {
            years = (int) (tmpSeconds / ((long)YEARS_MULTIPLIER));
            long remaining = tmpSeconds % ((long) YEARS_MULTIPLIER);
            seconds = (int) (remaining);
        } else {
            seconds = (int) tmpSeconds;
        }

        this.millis = (int) millis;
    }


    /**
     * Throw an illegal argument exception if the value is negative.
     *
     * @param value the value to test
     * @return the value
     */
    private int checkInt(int value) {
        if (value < 0) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format(
                    "xs-duration-invalid", new Integer(value)));
        }

        return value;
    }
    
    /**
     * Parse the String representation of the duration. The string
     * representation is defined in
     * <a href="http://www.w3.org/TR/xmlschema-2/#duration">
     * http://www.w3.org/TR/xmlschema-2/#duration</a>
     *
     * @param duration the string representation to parse.
     */
    private void parseString(String duration) {
        Matcher dateMatcher = DATE_PATTERN.matcher(duration);

        if (!dateMatcher.matches()) {
            throw new IllegalArgumentException(
                EXCEPTION_LOCALIZER.format("xs-data-type-invalid",
                                           new Object[]{"xs:duration",
                                                        duration}));
        } else {
            positive = !"-".equals(dateMatcher.group(1));
            years = parseIntString(dateMatcher.group(2));
            months = parseIntString(dateMatcher.group(3));
            days = parseIntString(dateMatcher.group(4));
            String time = dateMatcher.group(5);

            if (time != null) {
                // we appear to have a time component to this duration
                time = time.trim();

                if ("".equals(time)) {
                    throw new IllegalArgumentException(
                        EXCEPTION_LOCALIZER.format(
                            "xs-duration-invalid", duration));
                }

                Matcher timeMatcher = TIME_PATTERN.matcher(time);

                if (!timeMatcher.matches()) {
                    throw new IllegalArgumentException(
                        EXCEPTION_LOCALIZER.format(
                            "xs-duration-invalid", duration));
                } else {
                    hours = parseIntString(timeMatcher.group(1));
                    minutes = parseIntString(timeMatcher.group(2));
                    seconds = parseIntString(timeMatcher.group(3));
                    millis = parseMillis(timeMatcher.group(4)) ;
                }
            }
        }
    }

    /**
     * Parse the milliseconds component of the duration. This should not
     * include the decimal point before the fraction.
     *
     * @param mString the string representation of the milliseconds. May be
     *        null or the empty string (which are equivalent to zero)
     * @return the milliseconds component of the duration as an integer
     */
    private int parseMillis(String mString) {
        int result = 0;

        if ((mString != null) && !"".equals(mString)) {
            mString = "0." + mString;
            double ms = Double.parseDouble(mString);

            result = (int) Math.round(ms * 1000);
        }

        return result;
    }

    /**
     * Parse the string value into an integer. Return 0 if the value is
     * null.
     *
     * @param value the string to parse
     * @return the integer representation of the provided string.
     * @throws NumberFormatException if the string is an invalid format.
     */
    private int parseIntString(String value) {
        int result = 0;

        if (null != value && !"".equals(value)) {
            result = Integer.parseInt(value);
        }

        return result;
    }

    /**
     * Return the string representation of this Duration. The result is
     * always a complete duration will all components present even if
     * they are 0.
     *
     * <p>For further information see
     * <a href="http://www.w3.org/TR/xmlschema-2/#duration">
     * http://www.w3.org/TR/xmlschema-2/#duration</a>.</p>
     *
     * @return the string representation of the duration
     */
    public synchronized String toString() {
        StringBuffer sb = new StringBuffer();

        if (!positive) {
            sb.append('-');
        }

        sb.append('P');
        sb.append(NUMBER_FORMAT.format(years)).append('Y');
        sb.append(NUMBER_FORMAT.format(months)).append('M');
        sb.append(NUMBER_FORMAT.format(days)).append('D');

        sb.append('T');
        sb.append(NUMBER_FORMAT.format(hours)).append('H');
        sb.append(NUMBER_FORMAT.format(minutes)).append('M');
        sb.append(NUMBER_FORMAT.format(seconds)).append('.');

        // no formatting necessary
        sb.append(millis).append('S');

        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPositive() {
        return positive;
    }

    /**
     * {@inheritDoc}
     */
    public int getYears() {
        return isPositive() ? years : years * -1;
    }

    /**
     * {@inheritDoc}
     */
    public int getMonths() {
        return isPositive() ? months : months * -1;
    }

    /**
     * {@inheritDoc}
     */
    public int getDays() {
        return isPositive() ? days : days * -1;
    }

    /**
     * {@inheritDoc}
     */
    public int getHours() {
        return isPositive() ? hours : hours * -1;
    }

    /**
     * {@inheritDoc}
     */
    public int getMinutes() {
        return isPositive() ? minutes : minutes * -1;
    }

    /**
     * {@inheritDoc}
     */
    public int getSeconds() {
        return isPositive() ? seconds : seconds * -1;
    }

    /**
     * {@inheritDoc}
     */
    public int getMilliseconds() {
        return isPositive() ? millis : millis * -1;
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
     * Return a crude representation of this interval in the Timeline. This
     * assumes that there are 365.25 days in each year and 30.4375 days in
     * each month. This can be used as an approximate fallback when a
     * {@link DateTimeValue} is not present to enable accurate evaluation
     * of the interval.
     *
     * @return the interval as represented on the timeline (in seconds).
     */
    public double asJavaDouble() {
        return years * YEARS_MULTIPLIER +
            months * MONTHS_MULTIPLIER +
            days * DAYS_MULTIPLIER +
            hours * HOURS_MULTIPLIER +
            minutes * MINUTES_MULTIPLIER +
            seconds +
            millis * MILLIS_MULTIPLIER;
    }

    /**
     * {@inheritDoc}
     */
    public double getTimeOnTimeline() {
        return asJavaDouble();
    }

    /**
     * Run through the 4 time bases and add this duration to then to create
     * local time bases for comparisons
     */
    private void createLocalDateTimeBases() {
        synchronized(this) {
            // create the local cache if it does not already exist
            if (localDateTimes == null) {
                localDateTimes = new DateTimeValue[DATE_TIME_BASES.length];

                for (int i = 0; i < DATE_TIME_BASES.length; i++) {
                    localDateTimes[i] =
                        ((DateTimeValueOperations)DATE_TIME_BASES[i]).add(
                                this);
                }
            }
        }
    }

    /**
     * Adds two numbers with carry from previous addition.
     *
     * @param left first value
     * @param right second value
     * @param add tell whether to add or subtract arguments
     * @param carry previous carry
     * @param limit maximum value of the result
     * @return result and new carry
     */
    private int[] withCarry(int left, int right, boolean add, int carry, int limit) {
        int result[] = new int[2];
        result[0] = left + (add ? right : -right) + carry;
        boolean negative = result[0] < 0;
        int absolute = Math.abs(result[0]);
        if ((limit != 0) && (absolute >= limit)) {
            result[0] = absolute % limit;
            result[1] = (absolute / limit) * (negative ? -1 : 1);
        } else {
            result[1] = 0;
        }
        if (result[0] < 0) {
            result[0] = limit + result[0];
            result[1]--;
        }
        return result;
    }

    /**
     * Adds or subtracts two values depending on the
     * add argument.
     *
     * @param duration value to add or subtract
     * @param add tells whether to add or subtract arguments
     * @return addition or subtraction result
     */
    private DurationValue addOrSubtract(DurationValue duration, boolean add) {
        int result[] = withCarry(
                getMilliseconds(),
                duration.getMilliseconds(),
                add,
                0,
                1000);
        int newMillis = result[0];
        int carry = result[1];
        result = withCarry(
                getSeconds(),
                duration.getSeconds(),
                add,
                carry,
                60);
        int newSeconds = result[0];
        carry = result[1];
        result = withCarry(
                getMinutes(),
                duration.getMinutes(),
                add,
                carry,
                60);
        int newMinutes = result[0];
        carry = result[1];
        result = withCarry(
                getHours(),
                duration.getHours(),
                add,
                carry,
                24);
        int newHours = result[0];
        carry = result[1];
        result = withCarry(
                getDays(),
                duration.getDays(),
                add,
                carry,
                0);
        int newDays = result[0];
        result = withCarry(
                getMonths(),
                duration.getMonths(),
                add,
                0,
                0);
        int newMonths = result[0];
        result = withCarry(
                getYears(),
                duration.getYears(),
                add,
                0,
                0);
        int newYears = result[0];
        return factory.createDurationValue(
                newYears >= 0,
                newYears,
                newMonths,
                newDays,
                newHours,
                newMinutes,
                newSeconds,
                newMillis);
    }

    /**
     * {@inheritDoc}
     */
    public DurationValue add(DurationValue duration) {
        return addOrSubtract(duration, true);
    }

    /**
     * {@inheritDoc}
     */
    public boolean lessThan(DurationValue duration) {
        // evaluate this and duration against the DateTimeValue bases. If
        // this + k < duration + k for k in DATE_TIME_BASES then this is
        // strictly less then duration
        createLocalDateTimeBases();
        boolean result = true;

        for (int i = 0; i < DATE_TIME_BASES.length && result; i++) {

            if (!((DateTimeValueOperations)localDateTimes[i]).lessThan(
                ((DateTimeValueOperations)DATE_TIME_BASES[i]).add(duration))) {
                result = false;
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean greaterThan(DurationValue duration) {
        // evaluate this and duration against the DateTimeValue bases. If
        // this + k > duration + k for k in DATE_TIME_BASES then this is
        // strictly greater then duration
        createLocalDateTimeBases();
        boolean result = true;

        for (int i = 0; i < DATE_TIME_BASES.length && result; i++) {
            if (!((DateTimeValueOperations)localDateTimes[i]).greaterThan(
                ((DateTimeValueOperations)DATE_TIME_BASES[i]).add(duration))) {
                result = false;
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public DurationValue subtract(DurationValue duration) {
        return addOrSubtract(duration, false);
    }
}
