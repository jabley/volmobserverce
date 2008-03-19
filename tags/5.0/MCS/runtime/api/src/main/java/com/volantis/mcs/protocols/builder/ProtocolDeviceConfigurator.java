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
package com.volantis.mcs.protocols.builder;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;

/**
 * Implementations of this interface extract a subset of information from a
 * device in order to set up part of a protocol configuration.
 * <p>
 * Each logical grouping of data within the device / configuration should be
 * dealt with by a instance of this class which is responsible for that
 * grouping.
 */
public interface ProtocolDeviceConfigurator {

    /**
     * Initialise some part of the protocol configuration with information
     * from the device.
     *
     * @param configuration the configuration to be updated.
     * @param device the device which contains the relevant information.
     */
    public void initialise(ProtocolConfigurationImpl configuration,
            InternalDevice device);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 ===========================================================================
*/
