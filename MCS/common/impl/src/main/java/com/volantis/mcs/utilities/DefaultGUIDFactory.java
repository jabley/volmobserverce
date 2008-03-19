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
package com.volantis.mcs.utilities;

import java.util.Random;

/**
 * Factory to create GUIDs.
 */
public class DefaultGUIDFactory implements GUIDFactory {

    /**
     * Random number generator.
     */
    private static final Random RND = new Random(System.currentTimeMillis());

    /**
     * The counter to assign individual number to each GUID created.
     */
    private static long counter;

    /**
     * The spatial part assigned to this factory.
     */
    private final String spatialPart;

    DefaultGUIDFactory(final String spatialPart) {

        this.spatialPart = spatialPart;
    }

    // javadoc inherited
    public String generateGuid() {
        final String temporalPart =
            Long.toHexString(System.currentTimeMillis()) +
            Long.toHexString((((long) RND.nextInt()) << 32) + getNextIndex());
        return spatialPart + temporalPart;
    }

    private static synchronized long getNextIndex() {
        return counter++;
    }
}
