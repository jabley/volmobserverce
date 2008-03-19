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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/LCMImpl.java,v 1.2 2002/09/25 16:58:33 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;
import java.util.Vector;

/**
 * This is a "default" LCM implementation that utilises prime factoring. Note that the prime factors for given quantities are cached for improved performance.
 */
public final class LCMImpl implements LCM {
    /**
     * Returns the singleton instance of this class. 
     */
    public static LCM getInstance() {
        return instance;
    }

    public int getLCM(int[] quantities) {
        int[][] factors = new int[quantities.length][];
        int result = 1; // Provide an initial multiplication value

        // Get the arrays of prime factors for all of the given values
        for (int i = 0;
             i < quantities.length;
             i++) {
            factors[i] = getPrimeFactors(quantities[i]);
        }

        // Now multiply each factor the greatest number of times that it
        // occurs in any number with the other factors.
        // For example:
        //   prime factors:
        //     30 = 2 * 3 * 5
        //     45 = 3 * 3 * 5
        //   maximum occurances:
        //     2 => 1
        //     3 => 2
        //     5 => 1
        //   LCM = 2 * 3 * 3 * 5 = 90

        // Track how far through each prime factor set we are. Prime factors
        // are numerically ordered in each set (ascending order).
        int[] index = new int[quantities.length]; // all initialize to zero

        // Track the next factor to be counted
        int currentFactor = factors[0][0]; // a reasonable starting point
        boolean finished = false; // true when all factors have been visited

        while (!finished) {
            // Find the smallest factor from the remaining set of factors
            // resulting in an updated currentFactor or finished flag
            finished = true;
            for (int i = 0;
                 i < quantities.length;
                 i++) {
                if (index[i] < factors[i].length) {
                    if (finished ||
                        (factors[i][index[i]] <= currentFactor)) {
                        currentFactor = factors[i][index[i]];
                        finished = false;
                    }
                }
            }

            // If a new factor was available...
            if (!finished) {
                // Count the maximum number of times this factor appears in a
                // given number's set of prime factors, updating the prime
                // factor indexing as the processing continues
                int count = 1; // it has already been found at least once

                for (int i = 0;
                     i < quantities.length;
                     i++) {
                    int currentCount = 0;

                    // Count the occurances of currentFactor in the given
                    // number's set of prime factors
                    while ((index[i] < factors[i].length) &&
                           (factors[i][index[i]] == currentFactor)) {
                        currentCount++;
                        index[i]++;
                    }

                    // Record the maximum number of occurances of this factor
                    // across all numbers' prime number sets
                    if (currentCount > count) {
                        count = currentCount;
                    }
                }

                // Update the intermediate result using the currentFactor and
                // the count
                result *= (int)Math.pow(currentFactor, count);
            }
        }

        return result;
    }

    /**
     * The full set of prime factors are returned for the given number. This set of factors is in ascending numerical order.
     */
    protected int[] getPrimeFactors(int value) {
        // This implementation caches the prime factor results to speed
        // things up
        int[] result;

        // Extend the cache if needed
        for (int i = cache.size();
             i <= value;
             i++) {
            cache.add(null);
        }

        result = (int[])cache.get(value);

        if (result == null) {
            if (value < 2) {
                result = new int[1];
                result[0] = value;
            } else {
                // This algorithm determines all prime factors on an integer >= 2.
                // It first removes all factors of 2 then 3, 5, 7 etc. All factors
                // will be prime numbers as when a factor is tried all of its
                // prime factors will already have been removed.
                Vector factors = new Vector();
                int divisor = 3;
                int input = value;
                Integer two = new Integer(2);

                // First remove all factors of two
                while ((input % 2) == 0) {
                    factors.add(two);
                    input /= 2;
                }

                // now remove odd factors from 3 up. Tests against non-prime
                // factors will simply fail as their component prime factors will
                // have already been used to process the value.
                while ((divisor <= input) &&
                       (input != 1)) {
                    Integer intDiv = null;

                    while ((input % divisor) == 0) {
                        if (intDiv == null) {
                            intDiv = new Integer(divisor);
                        }
                        factors.add(intDiv);
                        input /= divisor;
                    }
                    divisor += 2;
                }

                // Convert the results into the required data type
                result = new int[factors.size()];

                for (int i = 0;
                     i < result.length;
                     i++) {
                    result[i] = ((Integer)factors.get(i)).intValue();
                }
            }

            cache.set(value, result);
        }

        return result;
    }

    /**
     * This is protected to enforce the singleton pattern on this class.
     */
    protected LCMImpl() {
    }

    /**
     * This cache is indexed by a the number to be factored and stores the
     * array of prime factors relevant for that number. Any numbers not
     * yet visited but otherwise within the indices of the cache will have
     * a null array in this cache.
     */
    private final Vector cache = new Vector();

    /**
     * The singleton instance of this class.
     * @supplierRole instance 
     */
    private static final LCM instance = new LCMImpl();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
