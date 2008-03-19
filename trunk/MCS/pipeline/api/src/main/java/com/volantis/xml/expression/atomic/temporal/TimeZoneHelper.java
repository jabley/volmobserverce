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

import java.util.TimeZone;
import java.util.Calendar;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * Some methods to help when dealing with Timezones and thier string
 * representations as described in
 * <a href="http://www.w3.org/TR/xmlschema-2/#Timezones">
 * http://www.w3.org/TR/xmlschema-2/#Timezones</a>
 */
public class TimeZoneHelper {


    /**
     * The number of milliseconds in a nominal hour.
     */
    private static final int MILLIS_TO_HOURS_MULTIPLIER = 3600000;

    /**
     * The number of milliseconds in a nominal minute.
     */
    private static final int MILLIS_TO_MINUTES_MULTIPLIER = 60000;

    /**
     * The UTC timezone
     */
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    /**
     * The formater for the timezone components. This is not thread safe so
     * must not be made static.
     */
    private final NumberFormat FORMATER = new DecimalFormat("00");

    /**
     * Convert the provided string containing a timezone in the format
     * described in <a href="http://www.w3.org/TR/xmlschema-2/#Timezones">
     * http://www.w3.org/TR/xmlschema-2/#Timezones</a> into a Timezone object
     *
     * @param tz the string representation of a timezone
     * @return the Timezone or null if the input is null or empty
     */
    public TimeZone asTimeZone(String tz) {
        TimeZone timezone = null;
        if ("Z".equals(tz)) {
            timezone = UTC;
        } else if ((tz != null) && !"".equals(tz)) {
            // Timezone offsets are based on GMT
            timezone = TimeZone.getTimeZone("GMT" + tz);
        }
        return timezone;
    }

    /**
     * Convert the provided timezone to its string representation as described
     * in <a href="http://www.w3.org/TR/xmlschema-2/#Timezones">
     * http://www.w3.org/TR/xmlschema-2/#Timezones</a>
     *
     * @param tz the timezone to convert to a string
     * @return the timezone in a string representation or the empty string if
     * the input timezone is null
     */
    public String asString(TimeZone tz) {
        String result = "";
        if (tz != null) {
            if (UTC.equals(tz)) {
                result = "Z";
            } else {
                int offsetMillis = tz.getRawOffset();
                result = createZone(offsetMillis);
            }
        }
        return result;
    }

    /**
     * Convert the provided Calendar to the string representation of its
     * timezone as described in
     * <a href="http://www.w3.org/TR/xmlschema-2/#Timezones">
     * http://www.w3.org/TR/xmlschema-2/#Timezones</a>. This is potentially
     * more accureate then the method that takes a TimeZone as its argument.
     *
     * @param calendar the calendar to convert to a string
     * @return the timezone in a string representation or the empty string if
     *         the input timezone is null
     */
    public String asString(Calendar calendar) {

        String result = "";
        if (calendar != null) {
            final TimeZone tz = calendar.getTimeZone();

            if (UTC.equals(tz)) {
                result = "Z";
            } else {
                int offsetMillis = calendar.get(Calendar.ZONE_OFFSET);
                result = createZone(offsetMillis);
            }
        }
        return result;
    }

    /**
     * Convert the timezone offset into a string representation
     *
     * @param offsetMillis the number of milliseconds of offset
     * @return the timezone offset as a string.
     */
    private String createZone(int offsetMillis) {
        StringBuffer result = new StringBuffer();
        // this truncates to the floor of the hours
        int hOffset = offsetMillis / MILLIS_TO_HOURS_MULTIPLIER;
        int mOffset = offsetMillis % MILLIS_TO_HOURS_MULTIPLIER;
        mOffset = mOffset / MILLIS_TO_MINUTES_MULTIPLIER;

        if (hOffset >= 0) {
            result.append("+");
        }
        result.append(FORMATER.format(hOffset)).append(":");
        result.append(FORMATER.format(mOffset));

        return result.toString();
    }
}
