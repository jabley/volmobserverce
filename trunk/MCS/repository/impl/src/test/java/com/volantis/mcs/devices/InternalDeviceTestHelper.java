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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.devices;

import com.volantis.devrep.repository.api.devices.DefaultDevice;

import java.util.HashMap;

public class InternalDeviceTestHelper {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
            InternalDeviceFactory.getDefaultInstance();

    public static InternalDevice createTestDevice() {
        return INTERNAL_DEVICE_FACTORY.createInternalDevice(
                new DefaultDevice("Unknown Device", new HashMap(),
                        null));
    }
}
