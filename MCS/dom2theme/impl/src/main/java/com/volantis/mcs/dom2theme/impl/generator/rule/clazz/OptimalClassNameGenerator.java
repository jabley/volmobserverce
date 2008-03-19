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

package com.volantis.mcs.dom2theme.impl.generator.rule.clazz;

/**
 * A class name generator that generates optimal class names.
 */
public class OptimalClassNameGenerator
        extends SeededClassNameGenerator {

    /**
     * The start letter used in the name, must be alphabetic, numbers are
     * not allowed.
     */
    private final String[] START = new String[] {
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
    };

    /**
     * The other letters, make be numeric.
     */
    private final String[] OTHER = new String[] {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
    };

    /**
     * The array of seeded class names, this should be sufficient for most
     * pages so will not need any additional classes to be generated.
     */
    private static final String[] SEEDED;
    static {
        ClassNameGenerator generator = new OptimalClassNameGenerator(
                new String[0]);
        SEEDED = new String[100];
        for (int i = 0; i < SEEDED.length; i++) {
            SEEDED[i] = generator.getClassName(i);
        }
    }

    /**
     * The default instance.
     */
    private static final ClassNameGenerator DEFAULT_INSTANCE =
            new OptimalClassNameGenerator();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static ClassNameGenerator getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Initialise.
     *
     * @param seeded The seeded array.
     */
    OptimalClassNameGenerator(String [] seeded) {
        super(seeded);
    }

    /**
     * Initialise.
     */
    public OptimalClassNameGenerator() {
        this(SEEDED);
    }

    // Javadoc inherited.
    protected String generateClassName(int index) {

        if (index < START.length) {
            return START[index];
        } else {
            StringBuffer buffer = new StringBuffer(4);
            buffer.setLength(0);
            int start = index % START.length;
            buffer.append(START[start]);
            int rest = index / START.length;
            do {
                rest -= 1;
                int digit = rest % OTHER.length;
                rest = rest / OTHER.length;
                buffer.append(OTHER[digit]);
            } while (rest > 0);
            return buffer.toString();
        }
    }
}
