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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.impl;

/**
 * Simple data structure representing a device name and a TAC.
 */
public class DeviceTACPair {
    private long tac;
    private String deviceName;

    /**
     * Initialise the device/TAC pair with specified values.
     *
     * @param initialTAC The TAC for this device
     * @param initialName The name of the device
     */
    public DeviceTACPair(long initialTAC, String initialName) {
        tac = initialTAC;
        deviceName = initialName;
    }

    /**
     * Retrieve the TAC for this device
     *
     * @return The TAC for this device
     */
    public long getTAC() {
        return tac;
    }

    /**
     * Retrieve the name of this device
     *
     * @return The name of this device
     */
    public String getDeviceName() {
        return deviceName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Aug-04	5065/3	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 04-Aug-04	5065/1	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 ===========================================================================
*/
