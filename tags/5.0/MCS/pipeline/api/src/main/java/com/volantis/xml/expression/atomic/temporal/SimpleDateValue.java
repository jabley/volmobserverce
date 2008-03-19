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
public class SimpleDateValue extends AtomicSequence implements DateValue {

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

    // javadoc inherited
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
    public int getYear() {
        int year = calendar.get(Calendar.YEAR);
        if(calendar.get(Calendar.ERA) == GregorianCalendar.BC) {
            year = - year;
        }
        return year;
    }

    // javadoc inherited
    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    // javadoc inherited
    public int getDay() {
        return calendar.get(Calendar.DATE);
    }

    // javadoc inherited
    public Calendar getCalendar() {
        return (Calendar) calendar.clone();
    }
}
