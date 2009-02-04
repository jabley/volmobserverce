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

package com.volantis.styling.device;


/**
 * An enumeration of the possible outlooks on the information provided about
 * the device.
 */
public class DeviceOutlook {

    /**
     * The device information is reasonably accurate but there are gaps.
     */
    public static final DeviceOutlook REALISTIC = new DeviceOutlook(
            "REALISTIC");

    /**
     * The device information is perfect.
     */
    public static final DeviceOutlook OPTIMISTIC = new DeviceOutlook(
            "OPTIMISTIC");

    /**
     * The name.
     */
    private final String name;

    /**
     * Initialise.
     *
     * @param name The name.
     */
    private DeviceOutlook(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }
}
