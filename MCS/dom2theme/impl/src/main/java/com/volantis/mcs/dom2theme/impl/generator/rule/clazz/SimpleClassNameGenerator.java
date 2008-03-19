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
 * A simple class name generator that appends the integer representation of the
 * index (+1) to the letter c.
 */
public class SimpleClassNameGenerator
        extends SeededClassNameGenerator {

    /**
     * The array of seeded class names, this should be sufficient for most
     * pages so will not need any additional classes to be generated.
     */
    private static final String[] SEEDED;
    static {
        ClassNameGenerator generator = new SimpleClassNameGenerator(
                new String[0]);
        SEEDED = new String[100];
        for (int i = 0; i < SEEDED.length; i++) {
            SEEDED[i] = generator.getClassName(i);
        }
    }

    /**
     * The default instance.
     */
    private static final SimpleClassNameGenerator DEFAULT_INSTANCE =
            new SimpleClassNameGenerator();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static SimpleClassNameGenerator getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Initialise.
     *
     * @param seeded The seeded array.
     */
    private SimpleClassNameGenerator(String[] seeded) {
        super(seeded);
    }

    /**
     * Initialise.
     */
    private SimpleClassNameGenerator() {
        super(SEEDED);
    }

    // Javadoc inherited.
    protected String generateClassName(int index) {
        // integration test examples start at one ;-)
        index += 1;
        return "c" + index;
    }
}
