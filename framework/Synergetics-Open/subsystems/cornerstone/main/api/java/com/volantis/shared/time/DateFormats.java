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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.time;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Type safe enumeration of Formats for date/time combinations
 */
public class DateFormats {

    /**
     * Map the names of the created enums to thier instances. Note that the
     * position of the map is important. It must be initialized before the enum
     * instances are created
     */
    private static final Map enums = new HashMap();

    /**
     * A helpful timezone to have
     */
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

    /**
     * An ASC_GMT instance
     */
    public static final DateFormats ASC_GMT = new DateFormats("ASC_GMT",
                                                    "EEE MMM d HH:mm:ss yyyy",
                                                    Locale.UK,
                                                    GMT);

    /**
     * An ISO_8601_GMT instance. Default locale/timezone
     */
    public static final DateFormats ISO_8601 = new DateFormats("ISO_8601",
                                                     "yyyy-MM-dd'T'HH:mm:ssZ",
                                                     null,
                                                     null);


    /**
     * An ISO_8601_GMT instance
     */
    public static final DateFormats ISO_8601_GMT = new DateFormats("ISO_8601_GMT",
                                                         "yyyy-MM-dd'T'HH:mm:ssZ",
                                                         Locale.UK,
                                                         GMT);

    /**
     * An ISO_8601_DATE instance. Date only. Default locale/timezone
     */
    public static final DateFormats ISO_8601_DATE =
        new DateFormats("ISO_8601_DATE",
                   "yyyy-MM-dd",
                   null,
                   null);

    /**
     * An ISO_8601_DATE instance. Date only.
     */
    public static final DateFormats ISO_8601_DATE_GMT =
        new DateFormats("ISO_8601_DATE_GMT",
                   "yyyy-MM-dd",
                   Locale.UK,
                   GMT);

    /**
     * An ISO_8601_TIME_GMT instance. Time only
     */
    public static final DateFormats ISO_8601_TIME_GMT =
        new DateFormats("ISO_8601_TIME_GMT",
                   "HH:mm:ss",
                   Locale.UK,
                   GMT);

    /**
     * An ISO_8601_TIME instance.  Time only. Default locale/timezone
     */
    public static final DateFormats ISO_8601_TIME =
        new DateFormats("ISO_8601_TIME",
                   "HH:mm:ss",
                   null,
                   null);

    /**
     * An RFC_1036_GMT instance
     */
    public static final DateFormats RFC_1036_GMT = new DateFormats("RFC_1036_GMT",
                                                     "EEEE, dd-MMM-yy HH:mm:ss zzz",
                                                     Locale.UK,
                                                     GMT);

    /**
     * An RFC_1036 instance Default timezone/local
     */
    public static final DateFormats RFC_1036 = new DateFormats("RFC_1036",
                                                         "EEEE, dd-MMM-yy HH:mm:ss zzz",
                                                         null,
                                                         null);

    /**
     * An RFC_1123_GMT instance
     */
    public static final DateFormats RFC_1123_GMT = new DateFormats("RFC_1123_GMT",
                                                         "EEE, dd MMM yyyy HH:mm:ss z",
                                                         Locale.UK,
                                                         GMT);

    /**
     * The name of the enumeration entry
     */
    private final String myName;

    /**
     * The pattern string for
     */
    private final String pattern;

    /**
     * Optional locale
     */
    private final Locale locale;

    /**
     * An optional timezone
     */
    private final TimeZone timezone;

    /**
     * Construct a type safe enum instance.
     *
     * @param name the name of the enumeration entry.
     * @param pattern the pattern string for the SimpleDateFormat
     * @param locale an optional locale
     */
    private DateFormats(String name, String pattern, Locale locale, TimeZone timezone) {
        this.myName = name;
        this.pattern = pattern;
        this.locale = locale;
        this.timezone = timezone;
        if (enums.containsKey(name)) {
            throw new IllegalArgumentException(
                "Enum '" + name + "' already exists");
        }
        enums.put(name, this);
    }

    /**
     * Return the name of the enumeration entry
     */
    public String toString() {
        return getMyName();
    }



    /**
     * Return the DateFormats instance corresponding to the specified name.
     *
     * @param name the name of the instance to return.
     * @throws IllegalArgumentException if an enum does not exist for the
     *                                  specified name.
     */
    public static DateFormats literal(String name) {
        DateFormats result = null;
        if (enums.containsKey(name)) {
            result = (DateFormats) enums.get(name);
        } else {
            throw new IllegalArgumentException(
                "Enum '" + name + "' does not exist");
        }
        return result;
    }

    /**
     * Create a DateFormat that will format Date objects according to the
     * specified format.
     *
     * <p><b>WARNING: DateFormat objects are NOT threadsafe.</b>
     * </p>
     *
     * @return a DateFormat that can parse and format according to the
     * appropriate specification
     */
    public DateFormat create() {
        return create(null, null);
    }

    /**
     * Create a DateFormat that will format Date obejcts according to the
     * specified format.
     * <p><b>WARNING: DateFormat objects are NOT threadsafe.</b></p>
     *
     * @param locale an optional local that can be used to override the default
     * for a particular format
     * @param timezone an optional timezone that can be used to override the
     * default for a particular format
     *
     * @return a DateFormat that can parse and format according to the
     *         appropriate specification
     */
    public DateFormat create(Locale locale, TimeZone timezone) {

        Locale tmpLocale = locale;
        if (null == tmpLocale) {
            tmpLocale = getLocale();
            if (null == tmpLocale) {
                tmpLocale = Locale.getDefault();
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, tmpLocale);

        TimeZone tmpTimezone = timezone;
        if (null == timezone) {
            tmpTimezone = getTimezone();
        }
        // set up a timezone if one was specified use default otherwise
        if (null != tmpTimezone) {
            formatter.setTimeZone(tmpTimezone);
        }
        return formatter;
    }

    // Javadoc unnecessary
    protected String getMyName() {
        return myName;
    }

    // Javadoc unnecessary
    protected String getPattern() {
        return pattern;
    }

    // Javadoc unnecessary
    protected Locale getLocale() {
        return locale;
    }

    // Javadoc unnecessary
    protected TimeZone getTimezone() {
        return timezone;
    }
}
