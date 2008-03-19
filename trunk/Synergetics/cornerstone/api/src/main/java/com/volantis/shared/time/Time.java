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

package com.volantis.shared.time;

import java.util.Date;


/**
 * A representation of a instant in time.
 *
 * <p>This has an accuracy of milliseconds.</p>
 *
 * @deprecated Use similar class in com.volantis.openapi.shared.time
 */
public class Time {

    /**
     * A time that will never happen.
     */
    public static final Time NEVER = new Time(Long.MAX_VALUE);

    /**
     * A count of the number of milliseconds since the epoch.
     */
    final long milliSeconds;

    /**
     * Get a time in seconds.
     *
     * @param inSeconds The time in seconds.
     * @return The time.
     */
    public static Time inSeconds(long inSeconds) {
        return new Time(inSeconds * 1000);
    }

    /**
     * Get a time in milli seconds.
     *
     * @param inMilliSeconds The time in milli seconds.
     * @return The time.
     */
    public static Time inMilliSeconds(long inMilliSeconds) {
        return new Time(inMilliSeconds);
    }

    /**
     * Initialise.
     *
     * @param time The number of milliseconds since the epoch.
     */
    private Time(long time) {
        if (time < 0) {
            throw new IllegalArgumentException(
                    "Time must be greater than or equal to 0");
        }

        this.milliSeconds = time;
    }

    /**
     * Get the period since the specified time.
     *
     * @param before The time from which the period started, must be before
     *               this time.
     * @return The period.
     */
    public Period getPeriodSince(Time before) {
        if (this == NEVER) {
            return Period.INDEFINITELY;
        } else if (before == NEVER) {
            throw new IllegalStateException("Cannot determine period since " +
                    before);
        } else {
            return new Period(milliSeconds - before.milliSeconds);
        }
    }

    /**
     * Add the period to this time.
     *
     * @param period The period.
     * @return The resulting time.
     */
    public Time addPeriod(Period period) {
        if (period == Period.INDEFINITELY || this == NEVER) {
            return NEVER;
        } else {
            return new Time(milliSeconds + period.inMillis());
        }
    }

    /**
     * Get the time in milliseconds.
     *
     * <p>If this time is {@link #NEVER} then this method will fail as
     * it does not know how to map it to a simple number of milliseconds.</p>
     *
     * @return The time in milliseconds.
     * @throws IllegalStateException If this is {@link #NEVER}.
     */
    public long inMillis() {
        if (this == NEVER) {
            throw new IllegalStateException("Time is never");
        }

        return milliSeconds;
    }

    /**
     * Get the string representation of this object.
     *
     * @return The string representation of this object.
     */
    public String toString() {
        if (this == NEVER) {
            return "never";
        } else {
            return milliSeconds + "ms";
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Time)) return false;

        final Time time1 = (Time) o;

        if (milliSeconds != time1.milliSeconds) return false;

        return true;
    }

    public int hashCode() {
        return (int) (milliSeconds ^ (milliSeconds >>> 32));
    }

    /**
     * Return the time as a string formated to RFC1123 requirements. The string
     * will always be in GMT (useful for HTTP style dates)
     *
     * @return the time as a RFC1123 formated string in the GMT timezone
     */
    public String asRFC1123() {
        return DateFormats.RFC_1123_GMT.create()
            .format(new Date(milliSeconds));
    }

}
