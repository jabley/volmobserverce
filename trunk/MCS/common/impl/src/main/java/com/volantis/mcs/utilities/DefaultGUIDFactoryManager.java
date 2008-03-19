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

import com.volantis.mcs.utilities.number.LongHelper;

import java.util.Random;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Default implementation of the GUIDFactoryManager. Uses current system time,
 * hash code and canonical host name to generate the factory independent part of
 * the spatial key.
 */
public class DefaultGUIDFactoryManager extends GUIDFactoryManager {

    /**
     * The factory independent part of the spatial GUID part.
     */
    private final String fixedSpatialPart;

    public DefaultGUIDFactoryManager() {
        long time = System.currentTimeMillis() / 10;
        String part = Long.toHexString((time << 32) + hashCode());

        try {
            final String hostName =
                InetAddress.getLocalHost().getCanonicalHostName();
            part += Long.toHexString(LongHelper.hashCode(hostName));
        } catch (UnknownHostException e) {
            part += Long.toHexString(
                new Random(System.currentTimeMillis()).nextLong());
        }
        fixedSpatialPart = part;
    }

    // javadoc inherited
    public GUIDFactory createGuidFactory(final long id) {
        return new DefaultGUIDFactory(fixedSpatialPart + Long.toHexString(id));
    }
}
