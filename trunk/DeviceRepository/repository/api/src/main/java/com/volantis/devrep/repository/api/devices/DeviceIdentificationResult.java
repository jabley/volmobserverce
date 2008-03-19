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
package com.volantis.devrep.repository.api.devices;

import java.util.Collection;

/**
 * Class to store information about a device identification process that was
 * made against HTTP headers.
 */
public class DeviceIdentificationResult {

    /**
     * The identified device.
     */
    private final DefaultDevice device;

    /**
     * The collection of header names used to identify the device.
     */
    private final Collection headerNames;

    public DeviceIdentificationResult(final DefaultDevice device,
                                      final Collection headerNames) {
        this.device = device;
        this.headerNames = headerNames;
    }

    /**
     * This method returns the name of the device identified.
     *
     * <p>Returns null, if device was not found.</p>
     *
     * @return the device object or null
     */
    public DefaultDevice getDevice() {
        return device;
    }

    /**
     * Returns an iterator over the names of the headers that were used to
     * identify the device. The elements returned by the iterator are Strings.
     *
     * <p>Never returns null. May return a non-empty iterator even if the
     * {@link #getDevice()} method returns null.</p>
     *
     * @return the iterator with the header names
     */
    public Collection getHeaderNamesUsed() {
        return headerNames;
    }
}
