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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.repository.api.devices.unknowndevices;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration for e-mail sending periods.
 */
public class EmailNotifierPeriodEnum {
    private static final Map NAMES_MAP = new HashMap();

    public static final EmailNotifierPeriodEnum DAY =
        new EmailNotifierPeriodEnum("day", 1, Calendar.DAY_OF_YEAR);
    public static final EmailNotifierPeriodEnum WEEK =
        new EmailNotifierPeriodEnum("week", 7 , Calendar.DAY_OF_YEAR);
    public static final EmailNotifierPeriodEnum MONTH =
        new EmailNotifierPeriodEnum("month", 1, Calendar.MONTH);
    public static final EmailNotifierPeriodEnum QUARTER =
        new EmailNotifierPeriodEnum("quarter", 3, Calendar.MONTH);
    public static final EmailNotifierPeriodEnum YEAR =
        new EmailNotifierPeriodEnum("year", 1, Calendar.YEAR);

    /**
     * Name of the enumeration element.
     */
    private final String name;

    /**
     * Quantity for the time associated to the enumeration element.
     */
    private final int amount;

    /**
     * Unit value of the time associated to the enumeration element
     */
    private final int unit;

    private EmailNotifierPeriodEnum(
            final String name, final int amount, final int unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        NAMES_MAP.put(name, this);
    }

    /**
     * Returns the enumeration element having the specified name.
     *
     * <p>Returns null if no element is found with the specified name.</p>
     *
     * @param name the name to look up
     * @return the enumeration element or null
     */
    public static EmailNotifierPeriodEnum literal(final String name) {
        return (EmailNotifierPeriodEnum) NAMES_MAP.get(name);
    }

    /**
     * Adds the time associated to the enumeration element to the specified
     * calendar entry.
     *
     * @param calendar the calendar entry to increase
     */
    public void addTo(final Calendar calendar) {
        calendar.add(unit, amount);
    }

    // javadoc inherited
    public String toString() {
        return name;
    }
}
