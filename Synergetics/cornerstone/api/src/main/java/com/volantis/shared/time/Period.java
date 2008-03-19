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

/**
 * A period of time.
 *
 * <p>This is an abstraction of a period of time that supports the concept of
 * an indefinite period. This helps to reduce the amount of special code
 * and values needed to handle indefinite periods which can lead to hard to
 * find errors.</p>
 *
 * <p>This has an accuracy of milliseconds.</p>
 *
 * @deprecated Use similar class in com.volantis.openapi.shared.time
 */
public class Period {

    /**
     * An indefinite period of time.
     */
    public static final Period INDEFINITELY = new Period(Long.MAX_VALUE);

    /**
     * A 0 ms period of time.
     */
    public static final Period ZERO = new Period(0);

    /**
     * The period in milliseconds.
     */
    final long milliSeconds;

    /**
     * Get a period.
     *
     * @param inMillis The number of milliseconds, 0 is treated as
     *                       {@link #INDEFINITELY}.
     * @return The period.
     */
    public static Period treatZeroAsIndefinitely(long inMillis) {
        if (inMillis == 0) {
            return INDEFINITELY;
        } else {
            return new Period(inMillis);
        }
    }

    /**
     * Get a period.
     *
     * @param inMillis The number of milliseconds, 0 or negative values are
     *                 treated as {@link #INDEFINITELY}.
     * @return The period.
     */
    public static Period treatNonPositiveAsIndefinitely(long inMillis) {
        if (inMillis <= 0) {
            return INDEFINITELY;
        } else {
            return new Period(inMillis);
        }
    }

    /**
     * Get a period.
     *
     * @param inSeconds The period in seconds.
     * @return The period.
     */
    public static Period inSeconds(long inSeconds) {
        return new Period(inSeconds * 1000);
    }

    /**
     * Get a period.
     *
     * @param inMilliSeconds The period in milliseconds.
     * @return The period.
     */
    public static Period inMilliSeconds(long inMilliSeconds) {
        return new Period(inMilliSeconds);
    }

    /**
     * Initialise.
     *
     * @param milliSeconds The period in milliseconds.
     */
    Period(long milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    /**
     * Subtract the other period from this one returning the result.
     *
     * <p>If the resulted period is too big to represent as a period then
     * INDEFINITELY is returned. If the resulted period is too small to
     * represent then IllegalStateException is thrown as we don't support
     * negative indefinite values.</p>
     *
     * @param other The other period to subtract from this one.
     * @return The result of subtracting the other period from this one.
     */
    public Period subtract(Period other) {
        if (other == INDEFINITELY) {
            throw new UnsupportedOperationException(
                    "Subtracting an indefinite period is undefined");
        }
        if (this == INDEFINITELY) {
            // Subtracting anything from indefinitely is still indefinitely.
            return this;
        } else {
            return internalAdd(-other.milliSeconds);
        }
    }

    /**
     * Adds the specified period to the current one. The current period is not
     * modified, instead the result is the return value of the method.
     *
     * <p>If either the specified or the current period is INDEFINITELY,
     * INDEFINITELY is returned.</p>
     *
     * <p>If the resulted period is too big to represent as a period then
     * INDEFINITELY is returned. If the resulted period is too small to
     * represent then IllegalStateException is thrown as we don't support
     * negative indefinite values.</p>
     *
     * @param other the period to add to the current period
     * @return the sum of the two periods
     */
    public Period add(final Period other) {
        if (this == INDEFINITELY || other == INDEFINITELY) {
            return INDEFINITELY;
        } else {
            return internalAdd(other.milliSeconds);
        }
    }

    /**
     * Adds the other period value to this period value and returns the created
     * period.
     *
     * <p>Checks overflow in both directions and returns INDEFINITELY if a
     * positive overflow was occured or throws IllegalStateException, if a
     * negative overflow was occured.</p>
     *
     * @param other the other period value to add to the current period
     * @return the period
     */
    private Period internalAdd(final long other) {
        // check if overflow was occured
        if (milliSeconds > 0) {
            // check positive overflow
            final long left = Long.MAX_VALUE - milliSeconds;
            if (other > left) {
                return INDEFINITELY;
            }
        } else {
            // check negative overflow
            final long left = Long.MIN_VALUE - milliSeconds;
            if (other < left) {
                throw new IllegalStateException("Period underflow occured");
            }
        }
        return new Period(other + milliSeconds);
    }

    /**
     * Get the period in milliseconds, converting {@link #INDEFINITELY} as 0.
     *
     * @return The period in milliseconds.
     */
    public long inMillisTreatIndefinitelyAsZero() {
        if (this == INDEFINITELY) {
            return 0;
        } else if (milliSeconds == 0) {
            throw new IllegalStateException("Period is 0");
        } else {
            return milliSeconds;
        }
    }

    /**
     * Get the period in milliseconds.
     *
     * <p>If this period is {@link #INDEFINITELY} then this method will fail as
     * it does not know how to map it to a simple number of milliseconds.</p>
     *
     * @return The period in milliseconds.
     * @throws IllegalStateException If this is {@link #INDEFINITELY}.
     */
    public long inMillis() {
        if (this == INDEFINITELY) {
            throw new IllegalStateException("Period is indefinite");
        }

        return milliSeconds;
    }

    /**
     * Get the period in seconds, converting {@link #INDEFINITELY} as 0.
     *
     * @return The period in seconds.
     */
    public long inSecondsTreatIndefinitelyAsZero() {
        if (this == INDEFINITELY) {
            return 0;
        } else if (milliSeconds == 0) {
            throw new IllegalStateException("Period is 0");
        } else {
            return milliSeconds / 1000;
        }
    }

    /**
     * Get the period in seconds.
     *
     * <p>If this period is {@link #INDEFINITELY} then this method will fail as
     * it does not know how to map it to a simple number of milliseconds.</p>
     *
     * @return The period in seconds
     *
     * @throws IllegalStateException If this is {@link #INDEFINITELY}.
     */
    public long inSeconds() {
        if (this == INDEFINITELY) {
            throw new IllegalStateException("Period is indefinite");
        }

        return milliSeconds / 1000;
    }

    /**
     * Get the string representation of this object.
     *
     * @return The string representation of this object.
     */
    public String toString() {
        if (this == INDEFINITELY) {
            return "indefinitely";
        } else {
            return milliSeconds + "ms";
        }
        }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Period)) return false;

        final Period period1 = (Period) o;

        return milliSeconds == period1.milliSeconds;
    }

    public int hashCode() {
        return (int) (milliSeconds ^ (milliSeconds >>> 32));
    }

    /**
     * Returns the maximum of the specified periods.
     *
     * <p>Returns the first if the specified periods are equal.</p>
     *
     * @param first the first period, may not be null
     * @param second the second period, may not be null
     * @return the maximum of the two periods specified
     */
    public static Period max(final Period first, final Period second) {
        return Comparator.GE.compare(first, second) ? first : second;
    }

    /**
     * Returns the minimum of the specified periods.
     *
     * <p>Returns the first if the specified periods are equal.</p>
     *
     * @param first the first period, may not be null
     * @param second the second period, may not be null
     * @return the minimum of the two periods specified
     */
    public static Period min(final Period first, final Period second) {
        return Comparator.LE.compare(first, second) ? first : second;
    }
}
