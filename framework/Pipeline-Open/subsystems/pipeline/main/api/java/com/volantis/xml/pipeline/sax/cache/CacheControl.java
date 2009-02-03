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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.cache;

import com.volantis.shared.time.Period;

/**
 * This class represents a CacheControl element.  A CacheControl allows
 * specific properties to be added to a cache entry.  The properties consist of
 * a name and a value.
 */
public class CacheControl {

    /**
     * Constant representing a time-to-live of forever.
     */
    public static final String LIVE_FOREVER = "forever";

    /**
     * The time-to-live value of this CacheControl. Defaults to
     * {@link Period.INDEFINITELY}.
     */
    private Period timeToLive = null;

    /**
     * Get the timeToLive property.
     *
     * @return timeToLive
     */
    public Period getTimeToLive() {
        return timeToLive;
    }

    /**
     * Set the timeToLive property providing a String to be converted into
     * an integer.
     *
     * @param timeToLive The timeToLive property as a String. If this parameter
     *                   is null it will unset timeToLive which is the same as
     *                   having a timeToLive of LIVE_FOREVER.
     * @throws IllegalArgumentException If timeToLive is < 0 and not
     *                                  LIVE_FOREVER and not null.
     * @throws NumberFormatException    If timeToLive is not an integer and not
     *                                  LIVE_FOREVER and not null.
     */
    public void setTimeToLive(Period timeToLive) {
        if (null == timeToLive) {
            timeToLive = Period.INDEFINITELY;
        }
        this.timeToLive = timeToLive;
    }

    // javadoc inherited
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof CacheControl) {
            final CacheControl cc = (CacheControl) obj;
            result = getTimeToLive() == null ? cc.getTimeToLive() == null :
                    getTimeToLive().equals(cc.getTimeToLive());
        }

        return result;
    }

    // javadoc inherited
    public int hashCode() {
        final int ttlHashCode;
        if (timeToLive == null) {
            ttlHashCode = 0;
        } else {
            ttlHashCode = timeToLive.hashCode();
        }
        return 31 * ttlHashCode;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 29-Jun-04	761/1	claire	VBM:2004060803 Updated constants to remove dependency on GenericCache constants

 07-Aug-03	316/1	allan	VBM:2003080501 Redesigned CacheControl and added timeToLive

 09-Jun-03	49/3	adrian	VBM:2003060505 updated headers and cleaned up imports following changes required for addition of cacheBody elements

 09-Jun-03	49/1	adrian	VBM:2003060505 Updated xml caching process to include cacheBody element

 ===========================================================================
*/
