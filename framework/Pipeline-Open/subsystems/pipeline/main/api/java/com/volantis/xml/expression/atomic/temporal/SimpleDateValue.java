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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Implementation of the DateValue interface
 *
 * @see <a href="http://www.w3.org/TR/xmlschema-2/#date">
 * http://www.w3.org/TR/xmlschema-2/#date</a>
 */
public class SimpleDateValue extends AtomicSequence
        implements DateValueOperations, DateValue {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(SimpleDateValue.class);

    /**
     * The pattern used to parse the Date.
     * Groups 1, 2 and 3 contain the year, month and date respectively.
     */
    private static final Pattern PATTERN = Pattern.compile(
        "^(-?[0-9]{4,})-([0-9]{2})-([0-9]{2})(Z?|[+-][0-9]{2}:[0-9]{2})$");

    /**
     * Used to format the years. Not thread safe so do not make it static
     */
    private final NumberFormat YEAR_FORMAT = new DecimalFormat("0000");

    /**
     * Used to format the month and day components of the date. This is
     * not thread safe so do not make it static
     */
    private final NumberFormat MONTH_DAY_FORMAT = new DecimalFormat("00");

    /**
     * This is not thread safe so must not be made static
     */
    private final TimeZoneHelper tzHelper = new TimeZoneHelper();

    /**
     * The calendar backing this timezone
     */
    private Calendar calendar = new GregorianCalendar();

    /**
     * Create the Simple implementation of the DateValue interface
     *
     * @param factory the expression factory used to create this
     * @param date the string represenatation of the date
     */
    public SimpleDateValue(ExpressionFactory factory, String date) {
        super(factory);
        parseDate(date);
    }

    /**
     * Create the Simple implementation of the DateValue interface
     *
     * @param factory the expression factory used to create this
     * @param calendar the {@link Calendar} representation of the date
     */
    public SimpleDateValue(ExpressionFactory factory, Calendar calendar) {
        super(factory);
        this.calendar = calendar;
    }

    /**
     * Parse the date string
     *
     * @param date the date string to parse
     */
    private void parseDate(String date) {
        Matcher matcher = PATTERN.matcher(date);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                "xs-data-type-invalid", new Object[]{"xs:date", date}));
        } else {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2)) - 1;
            int day = Integer.parseInt(matcher.group(3));

            String timezone = matcher.group(4);
            TimeZone tz = tzHelper.asTimeZone(timezone);

            if (tz == null) {
                calendar =
                    GregorianCalendar.getInstance(TimeZone.getDefault());
            } else {
                calendar = GregorianCalendar.getInstance(tz);
            }

            // ensure the year is positive
            calendar.set(Math.abs(year), month, day);
            calendar.set(Calendar.ERA,
                         ((year < 0) ?
                          GregorianCalendar.BC :
                          GregorianCalendar.AD));
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized String toString() {
        StringBuffer sb = new StringBuffer();
        if (calendar.get(Calendar.ERA) == GregorianCalendar.BC) {
            sb.append('-');
        }
        sb.append(YEAR_FORMAT.format(calendar.get(Calendar.YEAR)));
        sb.append('-');
        sb.append(MONTH_DAY_FORMAT.format(calendar.get(Calendar.MONTH) + 1));
        sb.append('-');
        sb.append(MONTH_DAY_FORMAT.format(calendar.get(Calendar.DATE)));
        // the formater does not handle the timezone in the format we wish so
        // manually format it.
        sb.append(tzHelper.asString(calendar));
        return sb.toString();
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
    public int getYear() {
        int year = calendar.get(Calendar.YEAR);
        if(calendar.get(Calendar.ERA) == GregorianCalendar.BC) {
            year = - year;
        }
        return year;
    }

    /**
     * {@inheritDoc}
     */
    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * {@inheritDoc}
     */
    public int getDay() {
        return calendar.get(Calendar.DATE);
    }

    /**
     * {@inheritDoc}
     */
    public Calendar getCalendar() {
        return (Calendar) calendar.clone();
    }

    /**
     * {@inheritDoc}
     */
    public DateValue add(DurationValue duration) {
        SimpleDateValue dv = new SimpleDateValue(factory, getCalendar());
        dv.calendar.add(Calendar.YEAR, duration.getYears());
        dv.calendar.add(Calendar.MONTH, duration.getMonths());
        dv.calendar.add(Calendar.DATE, duration.getDays());
        dv.calendar.add(Calendar.HOUR, duration.getHours());
        dv.calendar.add(Calendar.MINUTE, duration.getMinutes());
        dv.calendar.add(Calendar.SECOND, duration.getSeconds());
        dv.calendar.add(Calendar.MILLISECOND, duration.getMilliseconds());
        return dv;
    }

    /**
     * {@inheritDoc}
     */
    public boolean lessThan(DateValue value) {
        return PipelineExpressionHelper.compare(this, value) == -1;
    }

    /**
     * {@inheritDoc}
     */
    public boolean greaterThan(DateValue value) {
        return PipelineExpressionHelper.compare(this, value) == 1;
    }

    /**
     * {@inheritDoc}
     */
    public DurationValue subtract(DateValue value) {
        SimpleDateValue dv = (SimpleDateValue)value;
        long millis = calendar.getTimeInMillis() -
            dv.calendar.getTimeInMillis();
        return factory.createDurationValue(millis);
    }
}
