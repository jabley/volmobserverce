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

import com.volantis.shared.system.SystemClock;

/**
 * An abstraction of a clock that counts down.
 */
public abstract class Countdown {

    private static final Countdown INDEFINITE = new IndefiniteCountdown();

    /**
     * Get a {@link Countdown} that will count down the specified period.
     *
     * @param period The period to count down.
     * @return The {@link Countdown} instance.
     */
    public static Countdown getCountdown(Period period) {
        return getCountdown(period, SystemClock.getDefaultInstance());
    }

    /**
     * Get a {@link Countdown} that will count down the specified period.
     *
     * @param period The period to count down.
     * @param clock  The clock to use.
     * @return The {@link Countdown} instance.
     */
    public static Countdown getCountdown(Period period, SystemClock clock) {
        if (period == Period.INDEFINITELY) {
            return INDEFINITE;
        } else {
            return new PeriodCountdown(period, clock);
        }
    }

    /**
     * Count down.
     *
     * @return The {@link Period} representing the time remaining, or 0 if the
     *         count down has finished.
     * @throws TimedOutException If this is called after it has previously
     *                           returned a {@link Period} of 0.
     */
    public abstract Period countdown()
            throws TimedOutException;

    /**
     * A {@link Countdown} that handles {@link Period#INDEFINITELY} and so
     * never finishes.
     */
    private static class IndefiniteCountdown
            extends Countdown {

        // Javadoc inherited.
        public Period countdown() {
            return Period.INDEFINITELY;
        }
    }

    /**
     * A {@link Countdown} that handles limited {@link Period}s.
     */
    private static class PeriodCountdown
            extends Countdown {

        /**
         * The clock to use.
         */
        private final SystemClock clock;

        /**
         * The original timeout.
         */
        private final Period timeout;

        /**
         * The time that this was created, i.e. when it started.
         */
        private final Time start;

        /**
         * The time at which this is due to terminate.
         */
        private final Time end;

        /**
         * Initialise.
         *
         * @param timeout The timeout period.
         * @param clock   The clock to use.
         */
        public PeriodCountdown(Period timeout, SystemClock clock) {
            this.clock = clock;
            this.timeout = timeout;
            start = clock.getCurrentTime();
            end = start.addPeriod(timeout);
        }

        // Javadoc inherited.
        public Period countdown() throws TimedOutException {

            Time now = clock.getCurrentTime();

            // Compare now to the end time to see if they match.
            boolean expired = Comparator.GE.compare(now, end);
            if (expired) {
                // The timer has expired.
                throw new TimedOutException("Operation that started at " +
                        start + " and had a timeout of " + timeout +
                        " timed out at " + now);
            } else {
                // Calculate the remaining time.
                return end.getPeriodSince(now);
            }
        }
    }
}
