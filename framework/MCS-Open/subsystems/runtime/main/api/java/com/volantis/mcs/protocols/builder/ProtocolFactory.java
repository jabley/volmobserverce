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

import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.VolantisProtocol;

/**
 * A "low level" factory for creating protocols and their related
 * configurations.
 * <p>
 * Normally this interface is not used directly, as using a protocol builder
 * is a more natural interface for most uses.
 */
public interface ProtocolFactory {

    /**
     * Create an instance of a protocol configuration for this protocol.
     *
     * @return the configuration created.
     */
    ProtocolConfiguration createConfiguration();

    /**
     * Create an instance of a protocol, using the support factory and
     * configuration supplied.
     *
     * @param supportFactory the support factory for the protocol.
     * @param configuration the configuration for the protocol.
     * @return the created protocol.
     */
    VolantisProtocol createProtocol(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 ===========================================================================
*/
