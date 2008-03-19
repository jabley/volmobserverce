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
 * A class name generator that tries to use a seeded name first, and only
 * creates a new one if the seeded one is not available.
 */
public abstract class SeededClassNameGenerator
        implements ClassNameGenerator {

    /**
     * The pre seeded names.
     */
    private final String[] seeded;

    /**
     * Initialise.
     *
     * @param seeded The seeded array.
     */
    protected SeededClassNameGenerator(String[] seeded) {
        this.seeded = seeded;
    }

    // Javadoc inherited.
    public String getClassName(int index) {

        if (index < seeded.length) {
            return seeded[index];
        } else {
            return generateClassName(index);
        }
    }

    /**
     * Generate the class name.
     *
     * @param index The index.
     * @return The generated class name.
     */
    protected abstract String generateClassName(int index);
}
