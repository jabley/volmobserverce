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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.capability;

import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a collection of DeviceCapabilities
 */
public class DeviceCapabilityManager {

    /**
     * Collection of element capabilities which maps a element name with
     * a DeviceElementCapability
     */
    private final Map elementCapabilities = new HashMap();

    /**
     * add a DeviceElementCapability to the elementCapabilities map. The map
     * uses the DeviceElementCapability element type as a key
     * @param dec
     */
    void addDeviceElementCapability(DeviceElementCapability dec) {
        if (dec != null) {
            elementCapabilities.put(dec.getElementType(), dec);
        }
    }

    /**
     * Get the device element capability for the given element
     * @param element   whose device capability to find
     * @param create    true if the dec should be created (with a NONE support
     *                  level) if it doesn't exist, false otherwise.
     * @return the device element capability with the element type equal to
     * element. Will never return null if create is true.
     */
    public DeviceElementCapability getDeviceElementCapability(String element,
                                                              boolean create) {
        DeviceElementCapability dec =
                (DeviceElementCapability) elementCapabilities.get(element);
        if (dec == null && create) {
            // if this hasn't been populated because the device doesn't support
            // it, then create a new DEC with NONE support.
            dec = new DeviceElementCapability(element,
                    CapabilitySupportLevel.NONE);
            addDeviceElementCapability(dec);
        }
        return dec;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Oct-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
