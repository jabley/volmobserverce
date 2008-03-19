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
 * Provides facilities for comparing {@link Period} and {@link Time}
 *
 * @deprecated Use similar class in com.volantis.openapi.shared.time
 */
public class Comparator {

    /**
     * Comparator that checks to see if parameter 1 is greater than
     * parameter 2.
     */
    public static final Comparator GT =
            new Comparator(NumericComparator.GT);

    /**
     * Comparator that checks to see if parameter 1 is greater than or equal to
     * parameter 2.
     */
    public static final Comparator GE =
            new Comparator(NumericComparator.GE);

    /**
     * Comparator that checks to see if parameter 1 is less than parameter 2.
     */
    public static final Comparator LT =
            new Comparator(NumericComparator.LT);

    /**
     * Comparator that checks to see if parameter 1 is less than or equal to
     * parameter 2.
     */
    public static final Comparator LE =
            new Comparator(NumericComparator.LE);

    /**
     * Comparator that checks to see if parameter 1 is equal to parameter 2.
     */
    public static final Comparator EQ =
            new Comparator(NumericComparator.EQ);

    /**
     * Comparator that checks to see if parameter 1 is not equal to
     * parameter 2.
     */
    public static final Comparator NE =
            new Comparator(NumericComparator.NE);

    /**
     * The underlying numerical comparator.
     */
    private final NumericComparator comparator;

    /**
     * Initialise.
     *
     * @param comparator The comparator.
     */
    Comparator(NumericComparator comparator) {
        this.comparator = comparator;
    }

    /**
     * Compare two periods.
     *
     * @param p1 Period 1.
     * @param p2 Period 2.
     * @return The result of performing the check.
     */
    public boolean compare(Period p1, Period p2) {
        long n1;
        long n2;

        if (p1 == Period.INDEFINITELY) {
            if (p2 == Period.INDEFINITELY) {
                return false;
            } else {
                n1 = Long.MAX_VALUE;
                n2 = 0;
            }
        } else if (p2 == Period.INDEFINITELY) {
            n1 = 0;
            n2 = Long.MAX_VALUE;
        } else {
            n1 = p1.milliSeconds;
            n2 = p2.milliSeconds;
        }

        return comparator.compare(n1, n2);
    }

    /**
     * Compare two times.
     *
     * @param t1 Time 1.
     * @param t2 Time 2.
     * @return The result of performing the check.
     */
    public boolean compare(Time t1, Time t2) {
        long n1;
        long n2;

        if (t1 == Time.NEVER) {
            n1 = Long.MAX_VALUE;
            if (t2 == Time.NEVER) {
                return false;
            } else {
                n2 = 0;
            }
        } else if (t2 == Time.NEVER) {
            n1 = 0;
            n2 = Long.MAX_VALUE;
        } else {
            n1 = t1.milliSeconds;
            n2 = t2.milliSeconds;
        }

        return comparator.compare(n1, n2);
    }

    /**
     * A comparator for numbers.
     */
    private static abstract class NumericComparator {

        /**
         * Comparator that checks to see if parameter 1 is greater than
         * parameter 2.
         */
        public static final NumericComparator GT = new NumericComparator() {
            public boolean compare(long n1, long n2) {
                return n1 > n2;
            }
        };

        /**
         * Comparator that checks to see if parameter 1 is greater than or
         * equal to parameter 2.
         */
        public static final NumericComparator GE = new NumericComparator() {
            public boolean compare(long n1, long n2) {
                return n1 >= n2;
            }
        };

        /**
         * Comparator that checks to see if parameter 1 is less than
         * parameter 2.
         */
        public static final NumericComparator LT = new NumericComparator() {
            public boolean compare(long n1, long n2) {
                return n1 < n2;
            }
        };

        /**
         * Comparator that checks to see if parameter 1 is less than or equal
         * to parameter 2.
         */
        public static final NumericComparator LE = new NumericComparator() {
            public boolean compare(long n1, long n2) {
                return n1 <= n2;
            }
        };

        /**
         * Comparator that checks to see if parameter 1 is  equal to
         * parameter 2.
         */
        public static final NumericComparator EQ = new NumericComparator() {
            public boolean compare(long n1, long n2) {
                return n1 == n2;
            }
        };

        /**
         * Comparator that checks to see if parameter 1 is not equal to
         * parameter 2.
         */
        public static final NumericComparator NE = new NumericComparator() {
            public boolean compare(long n1, long n2) {
                return n1 != n2;
            }
        };

        /**
         * Compare two numbers.
         *
         * @param n1 Number 1
         * @param n2 Number 2
         * @return The result of performing the check.
         */
        public abstract boolean compare(long n1, long n2);
    }

}
